//Test class purely for testing purposes

package MonteCarloMini;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Run 
{
    public static void main(String [] args) throws IOException
    {
        FileWriter writer = null;

        try 
        {
            writer = new FileWriter(new File ("SearchDensitySpeedUp.txt"));
            writer.write("Speed up graph data for fixed grid and changing search density\n X     Y\n");
            //writer.write("Speed up graph data for fixed search density and changing grid size\n X     Y\n");
            //writer.write("Speed up graph data for fixed parameters and changing cutoff\n X     Y\n");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        double[][] plot = new double [2][25];
        //Changing search density
       	for(double i = 0.1; i < 1; i=i+0.2)
        {
        //Changing cutoff
       /* for(int i = 10000; i<100000;i=i+10000)
        {*/
            plot= test(i);

            writer.write("\n");
            for (int k = 0; k<plot[0].length;k++)
            {
                writer.write(plot[1][k] + "\n");
            }    
        }
        for (int j = 0; j<plot[0].length;j++)
        {
            writer.write(plot[0][j] + "\n");
        }
        writer.close();
    }
    public static double[][] test(double param)
    {
        //1 optimization
        /*String [] initialConditions = {"1000","9000","0","100","0","100","0.3"};
        MonteCarloMinimization.main(initialConditions);*/

        //Many optimizations for a dataset 
        double[]x = new double [25];
        double[]y = new double[25];

        int count = 0;
        
        //Changing grid size
        for(int i = 20; i<2500;i=i+100)
        {
            //Changing grid size
            int newX = i;
            int newY = i + 100;

            String [] newInitialConditions = {""+newX,""+newY,"0","500","0","500",""+param};
            //SearchParallel.cutoff = (int)param;
            //double tSerial = MonteCarloMinimization.main(newInitialConditions);
            //double tParallel = MonteCarloMinimizationParallel.main(newInitialConditions);

            x[count] = newX*newY;
            //y[count] = tSerial/tParallel;
            
            count++;
        }
        double plot [][] = {x,y};
        return plot;
    }
}

