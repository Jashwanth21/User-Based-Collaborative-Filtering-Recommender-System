/**
 * Used to evaluate the user-based CF algorithm.
 */

package alg.ub;

import java.io.File;

import alg.ub.neighbourhood.*;
import alg.ub.predictor.*;
import similarity.metric.*;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteUB_Expt2
{
	public static void main(String[] args)
	{
		////////////////////////////////////////
		// *** please do not edit this class ***
		////////////////////////////////////////
		
		
		// set the paths and filenames of the item file, genome scores file, train file and test file 
		// and create an instance of the DatasetReader class
		String folder = "ml-20m-2019-2020-CF";
		String itemFile = folder + File.separator + "movies-sample.txt";
		String itemGenomeScoresFile = folder + File.separator + "genome-scores-sample.txt";
		String trainFile = folder + File.separator + "train.txt";
		String testFile = folder + File.separator + "test.txt";	
		DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);
		
		// calculate RMSE and coverage for different neighbourhood thresholds
		System.out.println("t,RMSE,Coverage");
		for (int t = 0; t <= 80; t+=5)  // iterate over neighbourhood thresholds
		{
			// configure the user-based CF algorithm - set the predictor, neighbourhood and similarity 
			// metric and evaluate the algorithm
			Predictor predictor = new DeviationFromUserMeanPredictor();
			double threshold = t / 100.0;
			Neighbourhood neighbourhood = new ThresholdNeighbourhood(threshold);
			SimilarityMetric metric = new CosineMetric();
			UserBasedCF ubcf = new UserBasedCF(predictor, neighbourhood, metric, reader);
			Evaluator eval = new Evaluator(ubcf, reader.getTestData());
			Double RMSE = eval.getRMSE();
			double coverage = eval.getCoverage();			
			System.out.printf("%f,%.6f,%.2f%c\n", threshold, RMSE, coverage, '%');
		}
	}
}
