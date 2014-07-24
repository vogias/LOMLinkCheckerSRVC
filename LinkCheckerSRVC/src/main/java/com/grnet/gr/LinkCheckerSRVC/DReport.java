package com.grnet.gr.LinkCheckerSRVC;

import java.util.Date;
import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */

/**
 * @author vogias
 * 
 */

@XmlRootElement(name = "Detailed_Report")
public class DReport {
	Vector<String> broken, live, notWellFormed;
	String date,repository;

	public DReport() {
		broken = new Vector<String>();
		live = new Vector<String>();
		notWellFormed = new Vector<String>();
		date = new Date().toGMTString();
	}

	
	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}


	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}


	/**
	 * @return the date
	 */
	@XmlElement(name = "Date")
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the broken
	 */
	@XmlElement(name = "Broken")
	public Vector<String> getBroken() {
		return broken;
	}

	/**
	 * @return the live
	 */
	@XmlElement(name = "Live")
	public Vector<String> getLive() {
		return live;
	}

	/**
	 * @return the notWellFormed
	 */
	@XmlElement(name = "BadlyFormed")
	public Vector<String> getNotWellFormed() {
		return notWellFormed;
	}

	/**
	 * @param broken
	 *            the broken to set
	 */
	public void addBroken(String broken) {
		this.broken.addElement(broken);
	}

	/**
	 * @param live
	 *            the live to set
	 */
	public void addLive(String live) {
		this.live.addElement(live);
	}

	/**
	 * @param notWellFormed
	 *            the notWellFormed to set
	 */
	public void addNotWellFormed(String notWellFormed) {
		this.notWellFormed.addElement(notWellFormed);
	}

}
