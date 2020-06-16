/**
 * A class to evaluate a collaborative filtering algorithm.
 */

package util.evaluator;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import alg.CFAlgorithm;

import util.RatingsPair;
import util.UserItemPair;

public class Evaluator 
{	
	private static double DELTA = 0.0001;
	
	private Map<UserItemPair,RatingsPair> results; // a map to store all test data predictions
	
	/**
	 * constructor - creates a new Evaluator object
	 * @param alf - the CF algorithm
	 * @param testData - a map containing the test data
	 */
	public Evaluator(final CFAlgorithm alg, final Map<UserItemPair,Double> testData)
	{
		results = new HashMap<UserItemPair,RatingsPair>(); // instantiate the results hash map
		
		for(Iterator<Map.Entry<UserItemPair,Double>> it = testData.entrySet().iterator(); it.hasNext(); ) // iterate over test data and make predictions for all user-item pairs
		{
			Map.Entry<UserItemPair,Double> entry = (Map.Entry<UserItemPair,Double>)it.next();
			UserItemPair pair = entry.getKey();
			Double actualRating = entry.getValue();
			Double predictedRating = alg.getPrediction(pair.getUserId(), pair.getItemId());
			results.put(pair, new RatingsPair(actualRating, predictedRating));
			
			//System.out.println(pair.getUserId() + " " +  pair.getItemId() + " " + actualRating + " " + predictedRating);
		}
	}

	/**
	 * @returns the coverage (as a percentage)
	 */
	public double getCoverage()
	{
		int counter = 0;
		for(RatingsPair ratings: results.values())
			if(ratings.getPredictedRating() != null)
				counter++;
		
		return (results.size() > 0) ? counter * 100.0 / results.size() : 0;
	}
	
	/**
	 * @returns the root mean square error (RMSE) or null if the actual ratings are not available
	 */
	public Double getRMSE()
	{
		int counter = 0;
		double squareError = 0;
		for(RatingsPair ratings: results.values())
		{	
			if(ratings.getActualRating() == null) // actual ratings not available, exit loop
				break;
				
			if(ratings.getPredictedRating() != null) // a predicted rating has been computed
			{
				squareError += Math.pow(ratings.getActualRating().doubleValue() - ratings.getPredictedRating().doubleValue(), 2);
				counter++;
			}
		}

		if(counter == 0)
			return null;
		else
			return new Double(Math.sqrt(squareError / counter));	
	}
	
	/**
	 * @param targetRating - RMSE is computed where actual rating = target rating
	 * @returns the root mean square error (RMSE) or null if the actual ratings are not available
	 */
	public Double getRMSE(double targetRating)
	{
		int counter = 0;
		double squareError = 0;
		for(RatingsPair ratings: results.values())
		{	
			if(ratings.getActualRating() == null) // actual ratings not available, exit loop
				break;
			
			if(Math.abs(ratings.getActualRating().doubleValue() - targetRating) < DELTA)
			{
				if(ratings.getPredictedRating() != null) // a predicted rating has been computed
				{
					squareError += Math.pow(ratings.getActualRating().doubleValue() - ratings.getPredictedRating().doubleValue(), 2);
					counter++;
				}
			}
		}

		if(counter == 0)
			return null;
		else
			return new Double(Math.sqrt(squareError / counter));	
	}
	
	/**
	 * @returns the mean absolute error (MAE) or null if the actual ratings are not available
	 */
	public Double getMAE()
	{
		int counter = 0;
		double error = 0;
		for(RatingsPair ratings: results.values())
		{	
			if(ratings.getActualRating() == null) // actual ratings not available, exit loop
				break;
				
			if(ratings.getPredictedRating() != null) // a predicted rating has been computed
			{
				error += Math.abs(ratings.getActualRating().doubleValue() - ratings.getPredictedRating().doubleValue());
				counter++;
			}
		}

		if(counter == 0)
			return null;
		else
			return new Double(error / counter);	
	}
	
	/**
	 * @param filename - the path and filename of the output file
	 */
	public void writeResults(final String outputFile)
	{
		try
        {
			PrintWriter pw = new PrintWriter(new FileWriter(outputFile)); // open output file for writing
		
			for(Iterator<Map.Entry<UserItemPair,RatingsPair>> it = results.entrySet().iterator(); it.hasNext(); ) // iterate over all predictions
			{
				Map.Entry<UserItemPair,RatingsPair> entry = (Map.Entry<UserItemPair,RatingsPair>)it.next();
				UserItemPair pair = entry.getKey();
				RatingsPair ratings = entry.getValue();

				if(ratings.getPredictedRating() != null) // a predicted rating has been computed
				{
					pw.print(pair.toString() + " ");
					if(ratings.getActualRating() != null) // print the actual rating if available
						pw.print(ratings.getActualRating() + " ");
					pw.println(ratings.getPredictedRating());
				}
			}
			
			pw.close(); // close output file
        }
		catch(IOException e)
		{
			System.out.println("Error writing to output file ...\n");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
