/**
 * This represents an item that the users can rate.
 */

package util;

import java.util.HashSet;
import java.util.Set;

import profile.Profile;

public class Item
{
	private Integer id; // the numeric ID of the item
	private String name; // the name of the item
	private Set<String> genres; // a hash set containing genres
	private Profile genomeScores; // a profile with genome scores
	
	/**
	 * constructor - creates a new Item object
	 * @param id
	 * @param name
	 */
	public Item(final Integer id, final String name)
	{
		this.id = id;
		this.name = name;
		this.genres = new HashSet<String>();
		this.genomeScores = new Profile(id);
	}

	/**
	 * constructor - creates a new Item object
	 * @param id
	 * @param name
	 * @param genres
	 * @param genomeScores
	 */
	public Item(final Integer id, final String name, final Set<String> genres, final Profile genomeScores)
	{
		this.id = id;
		this.name = name;
		this.genres = genres;
		this.genomeScores = genomeScores;
	}
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the genres
	 */
	public Set<String> getGenres() {
		return genres;
	}

	/**
	 * @param genres the genres to set
	 */
	public void setGenres(Set<String> genres) {
		this.genres = genres;
	}

	/**
	 * @return the genomeScores
	 */
	public Profile getGenomeScores() {
		return genomeScores;
	}

	/**
	 * @param genomeScores the genomeScores to set
	 */
	public void setGenomeScores(Profile genomeScores) {
		this.genomeScores = genomeScores;
	}	
}
