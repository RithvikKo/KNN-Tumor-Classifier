import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class BreastCancerClassify {
	// K is number of neighbors that we check
	public static Integer K = 5;
    public static final Integer BENIGN = 2;
    public static final Integer MALIGNANT = 4;
    // How confident we must be to make a prediction
    public static double confidenceFactor = .2;
    
    

	public static double calculateDistance(int[] first, int[] second)
	{
		// calculates distance between any two points
		int sum = 0; 
		// does the square root of the sum of the squares
		for(int i = 0; i < first.length; i++) {
			if(i != 0 && i!= first.length - 1) {
				sum += (first[i] - second[i]) * (first[i] - second[i]);
			}
		}
		return Math.sqrt(sum);
	}
	
	// stores all distances from the point testInstance to all other points
	public static double[] getAllDistances(int[][] trainData, int[] testInstance)
	{
		double[] allDistances = new double[trainData.length];
		// iterates through all other points and stores distance 
		for(int i = 0; i < trainData.length; i++) {
			allDistances[i] = calculateDistance(trainData[i], testInstance);
		}
		return allDistances;
	}
	
	// given all of the distances from one point to the others, find the K closest points
	public static int[] findKClosestEntries(double[] allDistances)
	{
		int[] kClosestIndexes = new int[K];
		int max = 0;
		// finds the index of the point that is the farthest / has the maximum distance
		for(int l = 0; l < allDistances.length; l++) {
			if (allDistances[l] > allDistances[max]) {
				max = l;
			}
		}
		// temporarily set the current KClosest to have the maximum distance
		for(int l = 0; l < K; l++) {
			kClosestIndexes[l] = max;
		}
		// iterate through allDistances
		for(int i = 0; i < allDistances.length; i++) {
			double dist = allDistances[i];
			int biggest = 0;
			// finds the biggest distance that needs replacing in KClosestIndexes and store its index
			for(int j = 0; j < K; j++) {
				if (allDistances[kClosestIndexes[j]] > allDistances[kClosestIndexes[biggest]]) {
					biggest = j;
				}
			}
			// if dist is smaller than the current biggest distance in K-Closest, replace it
			if (dist < allDistances[kClosestIndexes[biggest]]) {
				kClosestIndexes[biggest] = i;
			}
		}
		// slowly the distances in KClosestIndexes get whittled down, and then finally return it
		return kClosestIndexes;
	}
	
	// given the data set of KClosestIndexes, it makes a prediction of either Malignant or Benign
	public static int classify(int[][] trainData, int[] kClosestIndexes)
	{
		// numMag counts the proportion of K-Closest points that are Malignant
		int numMag = 0;
		for(int i = 0; i < kClosestIndexes.length; i++) {
			// given the index of the point, it grabs the the Cancer_State from the trainData
			int lastEle = trainData[kClosestIndexes[i]].length - 1;
			int cancer = trainData[kClosestIndexes[i]][lastEle];
			if(cancer == MALIGNANT) {
				numMag += 1;
			}
		}
		// returns Malignant if there are more Malignant points and Benign if more Benign points closer
		if(numMag * 2 >= kClosestIndexes.length) {
			return MALIGNANT;
		}
		else {
			return BENIGN;
		}
	}
	
	// returns the result of the algorithm on testing points
	public static int[] kNearestNeighbors(int[][] trainData, int[][] testData){
		// results holds the Algorithms predicted classifications of test-data points
		int[] myResults = new int[testData.length];
		// iterates through all points and stores result in myResults
		for(int i = 0; i < testData.length; i++) {
			double[] allDist = getAllDistances(trainData, testData[i]);
			int[] kClosest = findKClosestEntries(allDist);
			int result = classify(trainData, kClosest);
			myResults[i] = result;
		}
		return myResults;
	}

	// returns accuracy of algorithm
	public static String getAccuracy(int[] myResults, int[][] testData) {
		// calculates the percentage of points correctly classified
		int numCorrect = 0;
		for(int i = 0; i < testData.length; i++) {
			int lastEle = testData[i].length - 1;
			if(myResults[i] == testData[i][lastEle]) {
				numCorrect += 1;
			}
		};
		double percentage = (double)numCorrect/testData.length * 100;
		return String.format("%.2f", percentage) + "%";
	}
	
	// this calculates the most optimal K-value that generates the highest accuracy
	public static void mostEffecient(int[][] trainData, int[][] testData) {
		K = 0;
		// bestK stores the best K value that gives us bestAccuracy
		int bestK = 0;
		double bestAccuracy = 0;
		// iterate over all possible K values
		for(int i = 0; i < testData.length; i++) {
			// calculate accuracy
			K += 1;
			int[] myResults = kNearestNeighbors(trainData, testData);
			double tempAccuracy = Double.parseDouble(getAccuracy(myResults, testData).substring(0,5));
			// if accuracy is improved, change bestK and bestAccuracy accordingly
			if (tempAccuracy > bestAccuracy) {
				bestAccuracy = tempAccuracy;
				bestK = K;
			}
		}
		System.out.println("this is the best accuracy:");
		System.out.println(bestAccuracy);
		System.out.println("this is the K value: ");
		System.out.println(bestK);
	}
	public static void main(String[] args) {
		// populate data into trainData and testData
		int[][] trainData = InputHandler.populateData("./datasets/train_data.csv");
		int[][] testData = InputHandler.populateData("./datasets/test_data.csv"); 
		// create graphical representation of data
		Grapher.createGraph(trainData);

		int[] myResults = kNearestNeighbors(trainData, testData);
		System.out.println("Model Accuracy: " + getAccuracy(myResults, testData));
	}

}
