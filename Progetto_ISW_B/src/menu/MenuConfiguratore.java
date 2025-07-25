package menu;

import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import controller.Gestori;
import utenti.Configuratore;
import util.Utilitaria;
import vista.VistaConfiguratore;

/**
 * Classe per definite il menu delle funzionalità del configuratore
 * 
 * @author Erjona Maxhaku 735766
 *
 */
public class MenuConfiguratore extends Menu {
	
	private Configuratore config;
	private LogicaPersistenza logica;
	private VistaConfiguratore vc;
	
	private Gestori g;

	private final static String titolo = "\tMENU CONFIGURATORE";

	private static final String MSG_NUOVO_COMPRENSORIO = "Aggiungi un nuovo comprensorio";
	private static final String MSG_NUOVA_GERARCHIA = "Inserisci una nuova gerarchia";
	private static final String MSG_VISUALIZZA_COMPRENSORI = "Visualizza tutti i comprensori presenti";
	private static final String MSG_VISUALIZZA_GERARCHIE = "Visualizza tutte le gerarchie";
	private static final String MSG_VISUALIZZA_FAT_CONV = "Visualizza tutti i fattori di conversione";
	private static final String MSG_VISUALIZZA_PROPOSTE = "Visualizza le proposte relative ad una categoria";
	private static final String MSG_VISUALIZZA_INSIEMI_CHIUSI = "Visualizza gli insiemi chiusi";
	private static final String MSG_SALVA = "Salva tutte le modifiche effettuate";
	private final static String MSG_P_PRECEDENTE = "Ritorna alla pagina di autenticazione";
	
	
	/**
	 * SALVATAGGIO DATI
	 */
	private static final String MSG_SALVATAGGIO = "Salvataggio effettuato con successo!";
	
	
	private static String[] vociConfig = {MSG_NUOVO_COMPRENSORIO,
			MSG_NUOVA_GERARCHIA,
			MSG_VISUALIZZA_COMPRENSORI,
			MSG_VISUALIZZA_GERARCHIE,
			MSG_VISUALIZZA_FAT_CONV,
			MSG_VISUALIZZA_PROPOSTE,
			MSG_VISUALIZZA_INSIEMI_CHIUSI,
			MSG_SALVA,
			MSG_P_PRECEDENTE
	};

	/**
	 * Construttore di MenuConfiguratore
	 * 
	 * @param config
	 * @param logica
	 */
	public MenuConfiguratore(Configuratore config, LogicaPersistenza logica) {
		super(titolo, vociConfig);
		this.config = config;
		this.logica = logica;
		this.vc = new VistaConfiguratore();
		this.g = new Gestori(logica, vc);
	}
	

	/**
	 * Metodo di creazione Comprensorio Geografico:
	 */
	public void creaComprensorio() {
		g.creaComprensorio();;
	}

	/**
	 * Metodo di creazione Gerarchia:
	*/
	public void creaGerarchia() {
		g.creaGerarchia(config);
	}
	
	public void mostraComprensori() {
		vc.visualizzaComprensori(g.formattaComprensori());
	}
	
	public void mostraGerarchie() {
		vc.visualizzaGerarchie(g.formattaGerarchie());
	}
	
	public void mostraFatConv() {
		vc.visualizzaFatConv(Utilitaria.formattaFatConv(logica.getFatConversione(), logica.getCategorieFoglia()));
	}
	
	public void mostraProposte() {
		vc.visualizzaProposte(g.formattaProposteDiFoglia( logica.getCategorieFoglia(), logica.getScambi()));
	}
	
	public void mostraInsiemiChiusi() {
		vc.visualizzaInsiemiChiusi(g.formattaInsiemiChiusi());
	}
	

	/**
	 * Metodo di salvataggio dei file gson.
	 */
	public void salva() {
		GestorePersistenza gp = new GestorePersistenza();
		gp.getSalvatore().salvaTutto(logica);
		vc.mostraMessaggio(MSG_SALVATAGGIO);
	}
	
}
