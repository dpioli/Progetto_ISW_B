package controller;

import java.util.ArrayList;

import applicazione.Comprensorio;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import util.InputDati;
import util.Utilitaria;
import vista.Vista;

/**
 * Controller GRASP che contiene la logica applicativa riguardante gli oggetti Comprensorio.
 */
public class GestoreComprensori {

	private static final String MSG_CREAZIONE_COMPRENSORIO = "Stai creando un nuovo comprensorio, inserisci le informazioni necessarie";
	private static final String MSG_NOME_COMPRENSORIO = "Inserisci il nome del comprensorio > ";
	private static final String MSG_NOME_COMP_NON_VALIDO = "Il nome inserito non è valido in quanto gia presente, riprova";
	private static final String MSG_INSERISCI_COMUNI = "Inserisci il nome del comune (o 'fine' per terminare) > ";
	private static final String MSG_ERRORE_INSERIMENTO_COMUNI = "Errore: Il comprensorio deve contenere almeno un comune.";
	private static final String MSG_SUCCESSO_COMPRENSORIO = "Comprensorio creato con successo!";
	private static final String MSG_SEL_COMPRENSORIO = "Seleziona il comprensorio di appartenenza della gerarchia > ";
	private static final String MSG_TERMINAZIONE = "fine";
	
	
	LogicaPersistenza logica;
	ArrayList<Comprensorio> comprensori; 
	Vista v;
	
	public GestoreComprensori(LogicaPersistenza logica, Vista v) {
		this.logica = logica;
		this.comprensori = logica.getComprensori();
		this.v = v;
	}

	/**
	 * Metodo di creazione Comprensorio Geografico:
	 * -crea il nuovo oggetto (assicutandosi della sua unicità);
	 * -lo aggiunge agli altri comprensori nella logica di slavataggio
	 * -completa il salvataggio.
	 */
	public void creaComprensorio() {
		v.mostraMessaggio(MSG_CREAZIONE_COMPRENSORIO);
		String nomeComprensorio = InputDati.leggiStringaNonVuota(MSG_NOME_COMPRENSORIO);
		for (Comprensorio c : logica.getComprensori()) {
			if (c.getNome().equals(nomeComprensorio)) {
				v.mostraErrore(MSG_NOME_COMP_NON_VALIDO);
				return;
			}
		}

		ArrayList<String> comuni = new ArrayList<>();
		boolean continua = true;

		while (continua) {
			String comune = InputDati.leggiStringaNonVuota(MSG_INSERISCI_COMUNI);
			if (comune.equalsIgnoreCase(MSG_TERMINAZIONE)) {
				continua = false;
			} else {
				comuni.add(comune);
			}
		}
		if (comuni.isEmpty()) {
			v.mostraErrore(MSG_ERRORE_INSERIMENTO_COMUNI);
			return;
		}
		Comprensorio nuovoComprensorio = new Comprensorio(nomeComprensorio, comuni);
		logica.addComprensorio(nuovoComprensorio);
		GestorePersistenza.salvaComprensori(logica.getComprensori());
		
		v.mostraMessaggio(MSG_SUCCESSO_COMPRENSORIO);
	}
	
	public Comprensorio selezionaComprensorio() {
		ArrayList<Comprensorio> comprensori = logica.getComprensori();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < comprensori.size(); i++) {
			sb.append(i + ": " + formattaComprensorio(comprensori.get(i)) + "\n"); ///////////////////////////////
		}
		
		v.mostraMessaggio(sb.toString());
		int scelta = InputDati.leggiIntero(MSG_SEL_COMPRENSORIO, 0, comprensori.size());
		return comprensori.get(scelta);
	}
	
	/**
	 * Metodo di visualizzazione dei comprensori.
	 */
	public String formattaComprensori() {
		return Utilitaria.formattaLista(comprensori);
	}
	public String formattaComprensorio(Comprensorio c) {
		return Utilitaria.formattaComprensorio(c);
	}

}
