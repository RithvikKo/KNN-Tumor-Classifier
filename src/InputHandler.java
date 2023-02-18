
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * InputHandler processes all input files and also prints the accuracy of results.
 */
public class InputHandler
{
     /**
     * Returns a two dimensional int array corresponding to a csv file (defined by filename) of
     * ints.
     */
    public static int[][] populateData(String filename)
    {
    	ArrayList<int[]> myList = new ArrayList<int[]>();

    	try {
    		// finding file
    		File in = new File("./datasets/test_data.csv");
        	FileReader fr = new FileReader(in);
        	BufferedReader br = new BufferedReader(fr);
        	String line;
        	// while there is data, read it
        	while ((line = br.readLine()) != null) {
        		// split data based on commas, add it to the list
        	   String[] temp = line.split(",");
        	   int[] row = new int[temp.length];
        	   // convert to integers
        	   for(int i = 0; i < temp.length; i++) {
        		   row[i] = Integer.parseInt(temp[i]);
        	   }
        	   // add cvs row to list
        	   myList.add(row);     	   
        	}
        	int[][] finalArray = myList.toArray(new int[myList.size()][11]);
        	// return formatted cvs in form of array
        	return finalArray;
        	        	
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
        return new int[0][0];
    }
}
