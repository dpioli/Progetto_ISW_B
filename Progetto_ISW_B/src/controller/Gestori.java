package controller;

import java.util.ArrayList;

import applicazione.CategoriaFoglia;
import applicazione.PropostaScambio;
import persistenza.LogicaPersistenza;
import utenti.Configuratore;
import utenti.Fruitore;
import vista.Vista;
import vista.VistaFruitore;

/**
 * Classe punto di accesso a un sottosistema di gestori (controller GRASP).
 * Non implementa logica applicativa ma delega i compiti al sottosistema.
 */
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
		
	public String formattaComprensori() {
		return gCom.formattaComprensori();
	}
	public String formattaGerarchie() {
		return gGer.formattaGerarchie();
	}
	
	/**
	 * Metodo per visualizzare le proposte relative ad una specifica categoria foglia
	 */
	public String formattaProposteDiFoglia(ArrayList<CategoriaFoglia> categorieFoglia, ArrayList<PropostaScambio> proposte) {
		return gGer.formattaProposteDiFoglia(categorieFoglia, proposte);
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
	
	
	public void mostraPropostePerFruitore(Fruitore fruit, VistaFruitore vf) {
		gProp.mostraPropostePerFruitore(fruit, vf);
	}
	

}
