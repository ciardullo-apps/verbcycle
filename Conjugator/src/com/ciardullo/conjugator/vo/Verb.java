package com.ciardullo.conjugator.vo;

import java.util.ArrayList;
import java.util.List;

public class Verb extends CommonVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String participioPassato;
	private String gerundio;
	private String participioPresente;
	private List<Conjugation> conjugations;
	public String getParticipioPassato() {
		return participioPassato;
	}
	public void setParticipioPassato(String participioPassato) {
		this.participioPassato = participioPassato;
	}
	public boolean isTransitivo() {
		/**
		 * Check the 24th item in the conjugations list: 
		 * this is the first person passato prossimo. If no
		 * conjugations exist, assume it is transitive
		 */
		boolean b = true;
		if(isValid()) {
			if(conjugations.get(24).getConjugation().startsWith("sono")) {
				b = false;
			}
		}
		
		return b;
	}

	public int getForma() {
		int n = 0;
		String verbName = getTheName();
		if(verbName.endsWith("are") || verbName.endsWith("arsi"))
			n = 1;
		else if(verbName.endsWith("ere") || verbName.endsWith("ersi"))
			n = 2;
		else if(verbName.endsWith("ire") || verbName.endsWith("irsi"))
			n = 3;
		
		return n;
	}
	public String getGerundio() {
		return gerundio;
	}
	public void setGerundio(String gerundio) {
		this.gerundio = gerundio;
	}
	public String getParticipioPresente() {
		return participioPresente;
	}
	public void setParticipioPresente(String participioPresente) {
		this.participioPresente = participioPresente;
	}
	public List<Conjugation> getConjugations() {
		return conjugations;
	}
	public void setConjugations(List<Conjugation> conjugations) {
		this.conjugations = conjugations;
	}
	public void addConjugation(Conjugation conj) {
		if(conjugations == null) {
			conjugations = new ArrayList<Conjugation>();
		}
		conjugations.add(conj);
	}
	public Verb() {
		super();
	}
	public Verb(int theId, String theName) {
		super(theId, theName);
	}
	
	public boolean isValid() {
		return (conjugations != null && conjugations.size() == 90);
	}
	
	/**
	 * Override setTheId() and set the verb id for all conjugations
	 * that may be in the list
	 */
	@Override
	public void setTheId(int theId) {
		super.setTheId(theId);
		if(conjugations != null) {
			for(Conjugation conj : conjugations) {
				conj.setVerbId(theId);
			}
		}
	}

}
