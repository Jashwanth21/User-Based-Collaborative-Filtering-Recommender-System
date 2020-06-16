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

public class ExecuteUB_Expt1
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

		// create an array of predictors
		Predictor[] predictors = {
				new SimpleNonPersonalisedPredictor(),
				new SimpleAveragePredictor(),
				new WeightedAveragePredictor(),
				new DeviationFromUserMeanPredictor()
		};

		// calculate RMSE and coverage for each predictor at different neighbourhood sizes
		System.out.println("k,NP RMSE,NP Coverage,SAP RMSE,SAP Coverage,WAP RMSE,WAP Coverage,DFUM RMSE,DFUM Coverage");
		for (int k = 10; k <= 250; k+=10) // iterate over neighbourhood sizes
		{
			System.out.printf("%d", k);
			for (int p = 0; p < predictors.length; p++) // iterate over predictors
			{
				// configure the user-based CF algorithm - set the predictor, neighbourhood and similarity 
				// metric and evaluate the algorithm
				Neighbourhood neighbourhood = new NearestNeighbourhood(k);
				SimilarityMetric metric = new CosineMetric();
				UserBasedCF ubcf = new UserBasedCF(predictors[p], neighbourhood, metric, reader);
				Evaluator eval = new Evaluator(ubcf, reader.getTestData());
				Double RMSE = eval.getRMSE();
				double coverage = eval.getCoverage();			
				System.out.printf(",%.6f,%.2f%c", RMSE, coverage, '%');
			}
			System.out.println();
		}
	}
}
