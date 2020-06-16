/**
 * Used to evaluate the user-based CF algorithm
 */

package alg.ub;

import java.io.File;

import alg.ub.neighbourhood.*;
import alg.ub.predictor.*;
import similarity.metric.*;
import util.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteUB_Expt3
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

		// create an array of similarity metrics
		SimilarityMetric[] metrics = {
				new CosineMetric(),
				new PearsonMetric(),
				new PearsonSigWeightingMetric(50),
				new MeanSquaredDifferenceMetric(1, 5)
		};

		// calculate RMSE and coverage for each predictor using different similarity metrics
		System.out.println("Metric,RMSE,Coverage");
		String[] labels = {"Cosine", "Pearson", "Pearson Sig", "MSD"};
		for (int s = 0; s < metrics.length; s++) // iterate over similarity metrics
		{
			// configure the user-based CF algorithm - set the predictor, neighbourhood and similarity 
			// metric and evaluate the algorithm
			Predictor predictor = new DeviationFromUserMeanPredictor();
			Neighbourhood neighbourhood = new NearestNeighbourhood(200);
			UserBasedCF ubcf = new UserBasedCF(predictor, neighbourhood, metrics[s], reader);
			Evaluator eval = new Evaluator(ubcf, reader.getTestData());
			Double RMSE = eval.getRMSE();
			double coverage = eval.getCoverage();	
			System.out.printf("%s,%.6f,%.2f%c\n", labels[s], RMSE, coverage, '%');
		}
	}
}
