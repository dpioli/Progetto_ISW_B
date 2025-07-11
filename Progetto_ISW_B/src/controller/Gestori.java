package controller;

import persistenza.LogicaPersistenza;
import vista.Vista;

public class Gestori {
	
	private GestoreComprensori gCom;
	private GestoreCategorie gCat;
	private GestoreGerarchie gGer;
	private GestoreProposte gProp;
	
	
	public Gestori(LogicaPersistenza logica, Vista vista) {
		this.gCom = new GestoreComprensori(logica, vista);
		this.gCat = new GestoreCategorie(logica, vista);
		this.gGer = new GestoreGerarchie(logica, vista);
		this.gProp = new GestoreProposte(logica, vista);
	}


	public GestoreComprensori getgCom() {
		return gCom;
	}


	public GestoreCategorie getgCat() {
		return gCat;
	}


	public GestoreGerarchie getgGer() {
		return gGer;
	}


	public GestoreProposte getgProp() {
		return gProp;
	}
	
	
	

}
