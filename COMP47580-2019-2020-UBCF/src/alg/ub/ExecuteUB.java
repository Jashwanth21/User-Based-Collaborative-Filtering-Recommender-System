/**
 * Executes the user-based CF algorithm.
 */

package alg.ub;

import java.io.File;

import alg.ub.neighbourhood.*;
import alg.ub.predictor.*;
import similarity.metric.*;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteUB
{
	public static void main(String[] args)
	{
		// configure the user-based CF algorithm - set the predictor, neighbourhood and similarity metric ...
		Predictor predictor = new SimpleAveragePredictor();
		Neighbourhood neighbourhood = new NearestNeighbourhood(10);
		SimilarityMetric metric = new CosineMetric();
		
		// set the paths and filenames of the item file, genome scores file, train file and test file ...
		String folder = "ml-20m-2019-2020-CF";
		String itemFile = folder + File.separator + "movies-sample.txt";
		String itemGenomeScoresFile = folder + File.separator + "genome-scores-sample.txt";
		String trainFile = folder + File.separator + "train.txt";
		String testFile = folder + File.separator + "test.txt";	
		
		// set the path and filename of the output file ...
		String outputFile = "results" + File.separator + "predictions.txt";
		
		/////////////////////////////////////////////////////////////////////////////////
		// Evaluates the CF algorithm:
		// - the RMSE (if actual ratings are available) and coverage are output to screen
		// - the output file is created
		DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);
		UserBasedCF ubcf = new UserBasedCF(predictor, neighbourhood, metric, reader);
		
		Evaluator eval = new Evaluator(ubcf, reader.getTestData());
		eval.writeResults(outputFile); // write predictions to output file
		Double RMSE = eval.getRMSE(); // calculate RMSE over all predictions made
		double coverage = eval.getCoverage(); // calculate coverage over all predictions
		System.out.printf("RMSE: %.6f\nCoverage: %.2f%c\n", RMSE, coverage, '%');
	}
}
