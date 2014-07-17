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

@XmlRootElement(name = "Report")
public class Report {

	long duration = 0;
	int numberofcheckedrecords = 0;
	int brokenlinks = 0;
	int notwellformed = 0;
	int healthy = 0;
	Vector<String> problems;

	public Report() {
		problems = new Vector<String>();
	}

	/**
	 * @return the problems
	 */
	@XmlElement(name = "Problems")
	public Vector<String> getProblems() {
		
		
		return problems;
	}

	/**
	 * @param problems
	 *            the problems to set
	 */
	public void addProblems(String problem) {
		problems.addElement(problem);
	}

	/**
	 * @return the healthy
	 */
	public int getHealthy() {
		healthy = numberofcheckedrecords - (notwellformed + brokenlinks);
		return healthy;
	}

	/**
	 * @param healthy
	 *            the healthy to set
	 */
	public void setHealthy(int healthy) {
		this.healthy = healthy;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @return the numberofcheckedrecords
	 */
	public int getNumberofcheckedrecords() {
		return numberofcheckedrecords;
	}

	/**
	 * @return the brokenlinks
	 */
	public int getBrokenlinks() {
		return brokenlinks;
	}

	/**
	 * @return the notwellformed
	 */
	public int getNotwellformed() {
		return notwellformed;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @param numberofcheckedrecords
	 *            the numberofcheckedrecords to set
	 */
	public void setNumberofcheckedrecords(int numberofcheckedrecords) {
		this.numberofcheckedrecords = numberofcheckedrecords;
	}

	/**
	 * @param brokenlinks
	 *            the brokenlinks to set
	 */
	public void setBrokenlinks(int brokenlinks) {
		this.brokenlinks = brokenlinks;
	}

	/**
	 * @param notwellformed
	 *            the notwellformed to set
	 */
	public void setNotwellformed(int notwellformed) {
		this.notwellformed = notwellformed;
	}

}
