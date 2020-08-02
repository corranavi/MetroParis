package it.polito.tdp.metroparis.model;

public class CoppiaFermate {
	private Fermata fp;
	private Fermata fa;
	
	public CoppiaFermate(Fermata fp, Fermata fa) {
		this.fp=fp;
		this.fa=fa;
	}
	public Fermata getFp() {
		return this.fp;
	}
	
	public Fermata getFa() {
		return this.fa;
	}
}
