package menu;

import controller.GestoreComprensori;
import controller.Gestori;
import persistenza.LogicaPersistenza;
import vista.Vista;


/**
 * Classe per la visualizzazione della schermata principale del programma
 * @author Diego Pioli 736160
 *
 */

public class MenuPrincipale extends Menu {
	
	private LogicaPersistenza logica;
	private Gestori g;
	private GestoreComprensori gC;
	
	/**
	 * Menu Iniziale
	 */
	private static final String MSG_BENVENUTO = "   BENVENUTO -- PAGINA INIZIALE";
	private static final String MSG_ACC_CONFIG = "Accedi come configuratore";
	private static final String MSG_ACC_FRUIT = "Accedi come fruitore";
	private static String[] voci = {MSG_ACC_CONFIG, MSG_ACC_FRUIT};
	private static final int CASE_CONFIGURATORE = 1;
	private static final int CASE_FRUITORE = 2;
	
	/**
	 * Menu Autenticazione 
	 */
	private static final String MSG_AUTENT = "\tAUTENTICAZIONE";
	private static final String MSG_PRIMO_ACCESSO = "Registrati";
	private static final String MSG_CONFIG_REGISTRATO= "Accedi";
	private final static String MSG_P_PRECEDENTE = "Ritorna alla pagina iniziale";

	private static final int CASE_PRIMO_ACCESSO = 1;
	private static final int CASE_ACCESSO = 2;
	private static final int CASE_P_INIZIALE = 3;
	private String[] vociAutenticazione = {MSG_PRIMO_ACCESSO, MSG_CONFIG_REGISTRATO, MSG_P_PRECEDENTE};
	
	/**
	 * Voci Menu Configuratore
	 */
	private static final int CASE_N_COMPRENSORIO = 1;
	private static final int CASE_N_GERARCHIA = 2;
	private static final int CASE_V_COMPRENSORI = 3;
	private static final int CASE_V_GERARCHIE = 4;
	private static final int CASE_V_FAT_CONV = 5;
	private static final int CASE_V_PROPOSTE = 6;
	private static final int CASE_V_INSIEMI_CHIUSI = 7;
	private static final int CASE_SALVA = 8;
	private static final int CASE_P_AUTENTICAZIONE = 9;
	private static final int CASE_USCITA = 0;
	
	/**
	 * Voci Menu Fruitore
	 */
	private static final int CASE_NAVIGA = 1;
	private static final int CASE_RICHIEDI_PRESTAZIONI = 2;
	private static final int CASE_RITIRA_PROPOSTE = 3;
	private static final int CASE_V_FRUIT_PROPOSTE = 4;
	private static final int CASE_P_AUT = 5;

	/**
	 * Costruttore della pagina iniziale del programma
	 * @param logica
	 */
	public MenuPrincipale(LogicaPersistenza logica) {
		super(MSG_BENVENUTO, voci);
		this.logica = logica;
		this.g= new Gestori(logica, new Vista());
		this.gC = g.getgCom();
	}
	
	/**
	 * Metodo per mostrare le azioni che un utente puo' svolgere.
	 */
	public void azioniMenuPrincipale() {
		int scelta;
		do {
			scelta = chiediScelta();
			switch(scelta) {
				case CASE_CONFIGURATORE -> autenticazioneConfig();
				case CASE_FRUITORE -> autenticazioneFruit();
			}
		} while (scelta != CASE_USCITA );
	}
	
	
	private void autenticazioneConfig() {
		LogicaAutenticazione.gestioneAutenticazione(
	        () -> new Autenticazione(logica).primoAccessoConfig(),
	        () -> new Autenticazione(logica).accessoConfiguratore(),
	        config -> avviaMenuConfiguratore(new MenuConfiguratore(config, logica))
	    );
	}
	
	
	private void autenticazioneFruit() {
	    LogicaAutenticazione.gestioneAutenticazione(
	        () -> new Autenticazione(logica).primoAccessoFruit(gC),
	        () -> new Autenticazione(logica).accessoFruitore(),
	        fruit -> avviaMenuFruitore(new MenuFruitore(fruit, logica))
	    );
	}


	/**
	 * Metodo per mostrare le azione che un configuratore puo' svolgere
	 * @param menuConfig
	 */
	private void avviaMenuConfiguratore(MenuConfiguratore menuConfig) {
		LogicaAutenticazione.gestioneMenuUtente(menuConfig, CASE_P_AUTENTICAZIONE, scelta -> {
	        switch (scelta) {
	            case CASE_N_COMPRENSORIO -> menuConfig.creaComprensorio();
	            case CASE_N_GERARCHIA -> menuConfig.creaGerarchia();
	            case CASE_V_COMPRENSORI -> menuConfig.mostraComprensori();
	            case CASE_V_GERARCHIE -> menuConfig.mostraGerarchie();
	            case CASE_V_FAT_CONV -> menuConfig.mostraFatConv();
	            case CASE_V_PROPOSTE -> menuConfig.mostraProposte();
	            case CASE_V_INSIEMI_CHIUSI -> menuConfig.mostraInsiemiChiusi();
	            case CASE_SALVA -> menuConfig.salva();
	            default -> System.exit(CASE_USCITA);
	        }
	    });
	}
	
	
	/**
	 * Metodo per mostrare le azione che un fruitore puo' svolgere
	 * @param menuFruit
	 */
	private void avviaMenuFruitore(MenuFruitore menuFruit) {
		LogicaAutenticazione.gestioneMenuUtente(menuFruit, CASE_P_AUT, scelta -> {
	        switch (scelta) {
	            case CASE_NAVIGA -> menuFruit.naviga();
	            case CASE_RICHIEDI_PRESTAZIONI -> menuFruit.richiediPrestazioni();
	            case CASE_RITIRA_PROPOSTE -> menuFruit.ritiraProposte();
	            case CASE_V_FRUIT_PROPOSTE -> menuFruit.mostraProposte();
	            default -> System.exit(CASE_USCITA);
	        }
	    });
	}
	
}
