package controller;

import java.util.ArrayList;

import applicazione.CategoriaFoglia;
import persistenza.LogicaPersistenza;
import utenti.Configuratore;
import utenti.Fruitore;
import vista.Vista;
import vista.VistaFruitore;

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
	
	public void creaComprensorio() {
	    gCom.creaComprensorio();
	}
	
	public void creaGerarchia(Configuratore config) {
		gGer.creaGerarchia(gCom, config);
	}
	
	////////////////////////DA SPOSTARE IN UTILITARIA CHECK PLS
	
	public String formattaComprensori() {
		return gCom.formattaComprensori();
	}
	public String formattaGerarchie() {
		return gGer.formattaGerarchie();
	}
	
	/**
	 * Metodo per visualizzare le proposte relative ad una specifica categoria foglia
	 */
	public String formattaProposte() {
		return gProp.formattaProposteDiFoglia(gGer);
	}
	public String formattaInsiemiChiusi() {
		return gProp.formattaInsiemiChiusi();
	}
	
	/* 
	 * 
	 * FRUITORE
	 * 
	 */
	public void naviga(Fruitore fruit) {
		gGer.naviga(fruit);
	}
	public void richiediPrestazioni(Fruitore fruit) {
		ArrayList<CategoriaFoglia> foglie = gGer.recuperaFoglieDisponibili(fruit);
		gProp.richiediPrestazioni(fruit, foglie);
	}
	
	
	/**
	 * Metodo per poter ritirare le proposte pendenti
	 */
	public void ritiraProposte(Fruitore fruit) {
		gProp.ritiraProposte(fruit);
	}
	
	
	public void mostraProposte(Fruitore fruit, VistaFruitore vf) {
		gProp.mostraProposte(fruit, vf);
	}
	

}
