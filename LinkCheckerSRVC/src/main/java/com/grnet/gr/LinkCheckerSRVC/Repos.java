/**
 * 
 */
package com.grnet.gr.LinkCheckerSRVC;

import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author vogias
 * 
 */
@XmlRootElement(name = "Repositories")
public class Repos {

	Vector<String> repositories;

	public Repos() {
		repositories = new Vector<String>();

	}

	/**
	 * @return the repositories
	 */
	@XmlElement(name = "Repositories")
	public Vector<String> getRepositories() {
		return repositories;
	}

	/**
	 * @param repositories
	 *            the repositories to set
	 */
	
	public void addRepositories(String repository) {
		repositories.addElement(repository);
	}

}
