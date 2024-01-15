package MonteCarloMini;

import java.util.concurrent.RecursiveTask;
/* M. Kuttel 2023 EDITTED BY M. Githinji 2023
 * Searcher class that lands somewhere random on the surfaces and 
 * then moves downhill, stopping at the local minimum.
 */

import MonteCarloMini.TerrainArea.Direction;

public class SearchParallel extends RecursiveTask<int[]>{
	private int id;				// Searcher identifier
	private int pos_row, pos_col;		// Position in the grid
	private int steps; //number of steps to end of search
	private boolean stopped;			// Did the search hit a previous trail?
	
	private SearchParallel[] arr;
	static final int cutoff = 50000;
	int lo;
	int hi;
	static final boolean DEBUG=false;	//Flag

	private TerrainArea terrain;

	public SearchParallel(int id, int pos_row, int pos_col, TerrainArea terrain) {
		this.id = id;
		this.pos_row = pos_row; //randomly allocated
		this.pos_col = pos_col; //randomly allocated
		this.terrain = terrain;
		this.stopped = false;
	}

	//Constructor for compute to do calculation
	public SearchParallel(SearchParallel[] arr,int l, int h)
	{
		this.arr = arr;
		lo = l;
		hi = h;
	}
	
	protected int[] compute()
	{
		int min=Integer.MAX_VALUE;
		int local_min=Integer.MAX_VALUE;
		int finder =-1;

		if((hi-lo)<cutoff)	//The maximum number of tasks wanted per thread
		{
			for(int i = lo; i < hi; i++)
			{
				local_min=arr[i].find_valleys();
				if((!arr[i].isStopped())&&(local_min<min)) //don't look at  those who stopped because hit exisiting path
				{
					min=local_min;
					finder=i; //keep track of who found it
				}
				if(DEBUG) System.out.println("Search "+arr[i].getID()+" finished at  "+local_min + " in " +arr[i].getSteps());
				//arr[i]=null;
			}
			int a []= {min,finder};
			return a;
		}
		else
		{
			SearchParallel left = new SearchParallel(arr, lo, (hi+lo)/2);
			SearchParallel right = new SearchParallel(arr,(hi+lo)/2,hi);

			left.fork();
			int[] rightAns = right.compute();
			int[] leftAns = left.join();
			if(rightAns[0]<leftAns[0])
			{
				return rightAns;
			}
			else
			{
				return leftAns;
			}
		}
	}

	public int find_valleys() {	
		int height=Integer.MAX_VALUE;
		Direction next = Direction.STAY_HERE;
		while(terrain.visited(pos_row, pos_col)==0) { // stop when hit existing path
			height=terrain.get_height(pos_row, pos_col);
			terrain.mark_visited(pos_row, pos_col, id); //mark current position as visited
			steps++;
			next = terrain.next_step(pos_row, pos_col);
			switch(next) {
				case STAY_HERE: return height; //found local valley
				case LEFT: 
					pos_row--;
					break;
				case RIGHT:
					pos_row=pos_row+1;
					break;
				case UP: 
					pos_col=pos_col-1;
					break;
				case DOWN: 
					pos_col=pos_col+1;
					break;
			}
		}
		stopped=true;
		return height;
	}

	public int getID() {
		return id;
	}

	public int getPos_row() {
		return pos_row;
	}

	public int getPos_col() {
		return pos_col;
	}

	public int getSteps() {
		return steps;
	}
	public boolean isStopped() {
		return stopped;
	}

}
