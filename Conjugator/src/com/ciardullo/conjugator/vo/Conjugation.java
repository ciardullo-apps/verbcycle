package com.ciardullo.conjugator.vo;

import java.io.Serializable;

public class Conjugation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int verbId;
	private String spName;
	private int spId;
	private int tenseId;
	private String conjugation;
	private String yourResponse;

	public int getVerbId() {
		return verbId;
	}

	public void setVerbId(int verbId) {
		this.verbId = verbId;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	public int getTenseId() {
		return tenseId;
	}

	public void setTenseId(int tenseId) {
		this.tenseId = tenseId;
	}

	public String getConjugation() {
		return conjugation;
	}

	public void setConjugation(String conjugation) {
		this.conjugation = conjugation;
	}

	public String getYourResponse() {
		return yourResponse;
	}

	public void setYourResponse(String yourResponse) {
		this.yourResponse = yourResponse;
	}

	public Conjugation(int verbId, int tenseId, String spName, int spId, String conjugation) {
		super();
		this.verbId = verbId;
		this.tenseId = tenseId;
		this.spName = spName;
		this.spId = spId;
		this.conjugation = conjugation;
	}

	public Conjugation() {
	}

	public String toString() {
		return getVerbId() + " " + getTenseId() + " " + getSpId() + " " + getSpName() + " " + getConjugation();
	}
}
