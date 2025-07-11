package menu;

import java.util.*;

import applicazione.CampoCaratteristico;
import applicazione.Categoria;
import applicazione.CategoriaFoglia;
import applicazione.Gerarchia;
import applicazione.InsiemeChiuso;
import applicazione.Proposta;
import applicazione.PropostaScambio;
import applicazione.StatoProposta;
import applicazione.TipoProposta;
import controller.GestoreGerarchie;
import controller.GestoreProposte;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Fruitore;
import util.InputDati;
import util.Menu;
import util.Utilitaria;
import vista.Vista;
import vista.VistaFruitore;

/**
 * Classe per definite il menu delle funzionalità del fruitore
 * 
 * @author Irene Romano 736566
 *
 */
public class MenuFruitore extends Menu{
	
	private Fruitore fruit;
	private LogicaPersistenza logica;
	private VistaFruitore vf;
	
	private GestoreGerarchie gGerarchie;
	private GestoreProposte gProposte;
	
	private static final String titolo = "\tMENU FRUITORE";
	
	private static final String NAVIGA = "Naviga tra le gerarchie";
	private static final String RICHIEDI_PRESTAZIONI = "Richiedi prestazioni al sistema";
	private static final String RITIRA_PROPOSTE = "Ritira proposte dal sistema";
	private static final String VISUALIZZA_PROPOSTE = "Visualizza le tue proposte";
	private final static String MSG_P_PRECEDENTE = "Ritorna alla pagina di autenticazione";
	
	
	private static String[] vociFruit = {NAVIGA, RICHIEDI_PRESTAZIONI, RITIRA_PROPOSTE, VISUALIZZA_PROPOSTE, MSG_P_PRECEDENTE};
	
	/**
	 * Construttore di MenuFruitore
	 * 
	 * @param fruit
	 * @param logica
	 */
	public MenuFruitore(Fruitore fruit, LogicaPersistenza logica) {
		super(titolo, vociFruit);
		this.fruit = fruit;
		this.logica = logica;
		this.vf = new VistaFruitore();
		this.gGerarchie = new GestoreGerarchie(logica, vf);
	}
	
	/**
	 * Metodo per navigare in profondita' tra le gerarchie
	 */
	public void naviga() {
		gGerarchie.naviga(fruit);
	}
	
	/**
	 * Metodo per la formulazione di una richiesta di scambio
	 * 1. il fruitore visualizza le categorie foglia a disposizione e seleziona categoria della richiesta
	 * 2. gProposte crea e verifica il soddisfacimento degli scambi
	 */
	public void richiediPrestazioni() {
		ArrayList<CategoriaFoglia> foglie = gGerarchie.recuperaFoglieDisponibili(fruit);
		gProposte.richiediPrestazioni(fruit, foglie);
	}
	
	
	/**
	 * Metodo per poter ritirare le proposte pendenti
	 */
	public void ritiraProposte() {
		gProposte.ritiraProposte(fruit);
	}
	
	
	public void mostraProposte() {
		gProposte.mostraProposte(fruit, vf);
	}

}
