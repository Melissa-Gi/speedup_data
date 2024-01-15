// Authour M. Kuttel 2023 EDITTED BY M. Githinji 2023
//Test class which manages the searches and output's the results

package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool; 

class MonteCarloMinimizationParallel
{
	static final boolean DEBUG=false;	//Flag
	static long startTime = 0;
	static long endTime = 0;

	//Parallelism additions
	static ForkJoinPool pool;

	static int[] doOptimization(SearchParallel[] arr)
	{
		return pool.invoke(new SearchParallel(arr,0,arr.length)); //alive the threads
	}

	//timers - note milliseconds
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static void tock(){
		endTime=System.currentTimeMillis(); 
	}
	
    public static void main(String[] args) throws OutOfMemoryError
    {
    	int rows, columns; //grid size
    	double xmin, xmax, ymin, ymax; //x and y terrain limits
    	TerrainArea terrain;  //object to store the heights and grid points visited by searches
    	double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

     	int num_searches;		// Number of searches
    	SearchParallel [] searches;		// Array of searches
    	Random rand = new Random();  //the random number generator
    	
    	if (args.length!=7) {  
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}
    	/* Read argument values */
    	rows =Integer.parseInt( args[0] );
    	columns = Integer.parseInt( args[1] );
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] );
  
    	if(DEBUG) {
    		/* Print arguments */
    		System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
    		System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax );
    		System.out.printf("Arguments, searches_density: %f\n", searches_density );
    		System.out.printf("\n");
    	}
    	
    	// Initialize
    	terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
    	searches= new SearchParallel [num_searches];
    	for (int i=0;i<num_searches;i++) 
		{
			try 
			{
				searches[i]=new SearchParallel(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
			} 
			catch (OutOfMemoryError oome) 
			{
				System.err.println("Too many searches");
				System.err.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());
			}
    		
		}

      	if(DEBUG) {
    		/* Print initial values */
    		System.out.printf("Number searches: %d\n", num_searches);
    		//terrain.print_heights();
    	}
		pool = new ForkJoinPool();
    	//start timer
    	tick();
    	
		//assign tasks to threads
		int[] ans = doOptimization(searches);

   		//end timer
   		tock();
   		
    	if(DEBUG) {
    		/* print final state */
    		terrain.print_heights();
    		terrain.print_visited();
    	}
    	
		//Output
		System.out.println("Parallel solution:");
		System.out.printf("Run parameters\n");
		System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
		System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax );
		System.out.printf("\t Search density: %f (%d searches)\n", searches_density,num_searches );

		//Total computation time
		System.out.printf("Time: %d ms\n",endTime - startTime );
		int tmp=terrain.getGrid_points_visited();
		System.out.printf("Grid points visited: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
		tmp=terrain.getGrid_points_evaluated();
		System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n",tmp,(tmp/(rows*columns*1.0))*100.0, "%");
	
		//Results
		System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n", ans[0], terrain.getXcoord(searches[ans[1]].getPos_row()), terrain.getYcoord(searches[ans[1]].getPos_col()));
		

		//Orchestration for speed up graphs
		//return (double)(endTime-startTime);
	}
}
