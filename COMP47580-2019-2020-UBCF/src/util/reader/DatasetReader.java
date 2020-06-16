/**
 * DatasetReader is used to read in user and item profiles from the MovieLens dataset. Also reads in the test data.
 */

package util.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import profile.Profile;

import util.Item;
import util.UserItemPair;

public class DatasetReader 
{
	private Map<Integer,Profile> userProfileMap;
	private Map<Integer,Profile> itemProfileMap;
	private Map<Integer,Profile> itemGenomeScoresMap;
	private Map<Integer,Item> itemMap;
	private Map<UserItemPair,Double> testData;

	/** 
	 * Constructs a DatasetReader from the MovieLens 100K dataset
	 * @param itemFile the path of the file containing item descriptions
	 * @param trainFile the path of the file containing the training user-item ratings
	 * @param testFile the path of the file containing the test user-item ratings
	 */
	public DatasetReader(final String itemFile, final String trainFile, final String testFile)
	{
		itemGenomeScoresMap = new HashMap<Integer,Profile>(); // not available for MovieLens 100K dataset
		loadItemsML100K(itemFile); // must be called before loadProfiles()
		loadProfiles(trainFile);
		loadTestData(testFile);
	}

	/** 
	 * Constructs a DatasetReader from the MovieLens 20M dataset
	 * @param itemFile the path of the file containing item descriptions
	 * @param itemGenomeScoresFile the path of the file containing item genome scores
	 * @param trainFile the path of the file containing the training user-item ratings
	 * @param testFile the path of the file containing the test user-item ratings
	 */
	public DatasetReader(final String itemFile, final String itemGenomeScoresFile, final String trainFile, final String testFile)
	{
		loadGenomeScoresML20M(itemGenomeScoresFile); // must be called before loadItemsML20M()
		loadItemsML20M(itemFile); // must be called before loadProfiles()
		loadProfiles(trainFile);
		loadTestData(testFile);
	}

	/**
	 * Returns all items loaded.
	 * @return a HashMap containing items
	 */
	public Map<Integer,Item> getItems()
	{
		return itemMap;
	}

	/**
	 * Returns an item.
	 * @return an Item object
	 * @param the numeric ID of the Item object
	 */
	public Item getItem(Integer id)
	{
		return itemMap.get(id);
	}

	/**
	 * Returns all the user profiles loaded.
	 * @return a HashMap containing user profiles
	 */
	public Map<Integer,Profile> getUserProfiles()
	{
		return userProfileMap;
	}

	/**
	 * Returns all the item profiles loaded.
	 * @return a HashMap containing item profiles
	 */
	public Map<Integer,Profile> getItemProfiles()
	{
		return itemProfileMap;
	}

	/**
	 * Returns genome scores for all the items loaded.
	 * @return a HashMap containing item profiles
	 */
	public Map<Integer,Profile> getItemGenomeScores()
	{
		return itemGenomeScoresMap;
	}

	/**
	 * Returns the test data.
	 * @return a HashMap containing the test data
	 */
	public Map<UserItemPair,Double> getTestData()
	{
		return testData;
	}

	/**
	 * Loads all user and item profiles.
	 * @param the path of the file containing the training user-item ratings
	 */
	private void loadProfiles(final String filename) 
	{
		userProfileMap = new HashMap<Integer,Profile>();
		itemProfileMap = new HashMap<Integer,Profile>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, ", \t\n\r\f");
				if(st.countTokens() != 3)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}

				Integer userId = Integer.valueOf(st.nextToken());
				Integer itemId = Integer.valueOf(st.nextToken());
				Double rating = Double.valueOf(st.nextToken());

				// add data to user profile map
				Profile up = userProfileMap.containsKey(userId) ? userProfileMap.get(userId) : new Profile(userId);
				up.addValue(itemId, rating);
				userProfileMap.put(userId, up);

				// add data to item profile map
				Profile ip = itemProfileMap.containsKey(itemId) ? itemProfileMap.get(itemId) : new Profile(itemId);
				ip.addValue(userId, rating);
				itemProfileMap.put(itemId, ip);
			}

			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Loads all test data.
	 * @param the path of the file containing the training user-item ratings
	 */
	private void loadTestData(final String filename) 
	{
		testData = new HashMap<UserItemPair,Double>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, ", \t\n\r\f");
				if(st.countTokens() != 2 && st.countTokens() != 3)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}

				Integer userId = Integer.valueOf(st.nextToken());
				Integer itemId = Integer.valueOf(st.nextToken());
				Double rating = (st.countTokens() == 1) ? Double.valueOf(st.nextToken()) : null; // check to see if one more token (i.e. the rating) remains
				testData.put(new UserItemPair(userId, itemId), rating);	// add data to user test data map
			}

			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Used for dataset MovieLens 100K
	 * @param filename
	 */
	private void loadItemsML100K(final String filename) 
	{
		itemMap = new HashMap<Integer,Item>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, "|");
				if(st.countTokens() < 2)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}

				Integer id = Integer.valueOf(st.nextToken());
				String name = st.nextToken();
				Item item = new Item(id, name);
				itemMap.put(id, item);
			}

			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Used for dataset MovieLens 20M
	 * @param filename
	 */
	private void loadItemsML20M(final String filename) 
	{
		itemMap = new HashMap<Integer,Item>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			while ((line = br.readLine()) != null) 
			{
				int firstIndex = line.indexOf(",");
				int lastIndex = line.lastIndexOf(",");

				Integer movieId = Integer.valueOf(line.substring(0, firstIndex));
				String title = line.substring(firstIndex + 1, lastIndex);
				String genreStr = line.substring(lastIndex + 1);

				// read genres
				Set<String> genres = new HashSet<String>();
				StringTokenizer st = new StringTokenizer(genreStr, "|");
				int ntokens = st.countTokens();
				if(ntokens < 1)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}

				for (int i = 0; i < ntokens; i++)
					genres.add(st.nextToken());

				// create and add Item object
				Item item = new Item(movieId, title, genres, itemGenomeScoresMap.get(movieId));
				itemMap.put(movieId, item);
			}

			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Used for dataset MovieLens 20M
	 * @param filename
	 */
	private void loadGenomeScoresML20M(final String filename)
	{
		itemGenomeScoresMap = new HashMap<Integer,Profile>(); 

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line;
			br.readLine(); // read header line
			while ((line = br.readLine()) != null) 
			{
				StringTokenizer st = new StringTokenizer(line, ",");
				int ntokens = st.countTokens();
				if(ntokens != 3)
				{
					System.out.println("Error reading from file \"" + filename + "\"");
					System.exit(1);
				}

				Integer movieId = Integer.valueOf(st.nextToken());
				Integer tagId = Integer.valueOf(st.nextToken());
				Double relevance = Double.valueOf(st.nextToken());

				Profile p = (itemGenomeScoresMap.containsKey(movieId) ? itemGenomeScoresMap.get(movieId) : new Profile(movieId));
				p.addValue(tagId, relevance);
				itemGenomeScoresMap.put(movieId, p);
			}

			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
