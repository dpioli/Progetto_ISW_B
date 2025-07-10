package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import applicazione.CampoCaratteristico;
import applicazione.Categoria;
import applicazione.CategoriaFoglia;
import applicazione.CategoriaNonFoglia;
import applicazione.Comprensorio;
import applicazione.Gerarchia;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Configuratore;
import util.InputDati;
import util.Utilitaria;
import vista.Vista;

public class GestoreGerarchie {
	
	/**
	 * CREAZIONE GERARCHIA
	 */
	private static final String MSG_CREAZIONE_GERARCHIA = "Stai creando una nuova gerarchie, inserisci"
			+ " le informazioni necessarie";
	private static final String MSG_NOME_GERARCHIA = "Inserisci il nome della nuova gerarchia >";
	private static final String MSG_NOME_GERARCHIA_NON_VALIDO = "E' già presente una gerarchia con questo nome.";
	private static final String MSG_NOME_CAMPOCARATT = "Inserisci il nome del campo caratteristico > ";
	private static final String MSG_VALORE_CAMPOCARATT = "Inserisci il valore ('fine' per terminare) > ";
	private static final String MSG_DESCRIZIONE_CAMPOCARATT = "Inserisci la descrizione per questo valore (premere invio altrimenti) > ";
	private static final String MSG_INSERISCI_SOTTOCATEG = "Inserisci le sottocategorie della gerarchia appena creata:\n ";
	private static final String MSG_GERARCHIA_CREATA_CON_SUCCESSO = "La gerarchia è stata creata con successo!";
	
	private static final String MSG_CERAZIONE_NODI = "Vuoi creare una categoria intermedia (1) o una foglia (2)? > ";
	
	private static final String MSG_CATEGORIA_NON_FOGLIA = "Stai creando una categroia intermedia:";
	private static final String MSG_CATEGORIA_FOGLIA = "Stai creando una categoria foglia: ";
	private static final String MSG_NOME_CATEGORIA = "Inserisci il nome della categoria >";
	private static final String MSG_NOME_CATEGORIA_NON_VALIDO = "E' già presente una categoria con questo nome.";
	private static final String MSG_NESSUN_COMPRENSORIO = "Non è presente nessun comprensorio all'interno del sistema, creane uno prima di creare una gerarchia.";

	private static final String MSG_SELEZIONA_PRESTAZIONE = "\nSeleziona la prestazione per cui vuoi ottenere le proposte (annulla altrimenti) > ";
	private static final String MSG_ANNULLA = ": Annulla";
	private static final String MSG_PRESTAZIONI_DISPONIBILI = "Prestazioni disponibili: ";
	private static final String MSG_LISTA_INSIEMI_CHIUSI = "Lista degli insiemi chiusi: \n";
	
	private static final String MSG_TERMINAZIONE = "fine";
	
	
	
	ArrayList<Gerarchia> gerarchie; 
	LogicaPersistenza logica;
	Vista v;
	
	public GestoreGerarchie(LogicaPersistenza logica, Vista v) {
		this.gerarchie = logica.getGerarchie();
		this.logica = logica;
		this.v = v;
	}
	
	/**
	 * Metodo di creazione Gerarchia:
	 * -crea il nuovo oggetto (assicutandosi della sua unicità);
	 * -lo aggiunge alle altre gerarchie nella logica di slavataggio;
	 * -completa il salvataggio.
	 */
	public void creaGerarchia(GestoreComprensori gComprensori, Configuratore config) {
		v.mostraMessaggio(MSG_CREAZIONE_GERARCHIA);
		String nomeGerarchia = InputDati.leggiStringaNonVuota(MSG_NOME_GERARCHIA);
	
		for(Gerarchia g: logica.getGerarchie()) {
			if(g.eNomeUguale(nomeGerarchia)) {
				v.mostraErrore(MSG_NOME_GERARCHIA_NON_VALIDO);
				return;
			}
		}
		Comprensorio comp = null;
		if(logica.getComprensori().isEmpty()) {
			v.mostraErrore(MSG_NESSUN_COMPRENSORIO);
			return;
		} else {
			comp = gComprensori.selezionaComprensorio();
		}
		
		String nomeCampo = InputDati.leggiStringaNonVuota(MSG_NOME_CAMPOCARATT);
		HashMap<String, String> valoriCampo = new HashMap<>();
		
		boolean continua = true;
		while(continua) {
			String valore = InputDati.leggiStringaNonVuota(MSG_VALORE_CAMPOCARATT);
			if(valore.equalsIgnoreCase(MSG_TERMINAZIONE)) {
				continua = false;
			} else {
				String desc = InputDati.leggiStringa(MSG_DESCRIZIONE_CAMPOCARATT);
				valoriCampo.put(valore, desc);
			}
		}
		int dimensioneDominio = valoriCampo.size();
		
		Gerarchia nuovaGerarchia = addGerarchia(nomeGerarchia, config, comp, nomeCampo, valoriCampo, dimensioneDominio);
		v.mostraMessaggio(MSG_INSERISCI_SOTTOCATEG);
		addSottoCategorie(nuovaGerarchia.getCatRadice());
		
		logica.addGerarchia(nuovaGerarchia);
		salvaGerarchieEFoglie();
	
		v.mostraMessaggio(MSG_GERARCHIA_CREATA_CON_SUCCESSO);
	}
	
	
	/**
	 * Metodo di aggiunta gerarchia.
	 * Crea l'oggetto gerarchia e lo aggiunge alle gerarchie della logica.
	 * @param nomeGerarchia
	 * @param nomeCampo
	 * @param valoriCampo
	 * @param dimensioneDominio
	 * @return nuova gerarchia
	 */
	private Gerarchia addGerarchia(String nomeGerarchia, Configuratore config, Comprensorio compr, String nomeCampo, HashMap<String, String> valoriCampo,
			Integer dimensioneDominio) {
		CampoCaratteristico campoCaratt = new CampoCaratteristico(nomeCampo, valoriCampo);
		CategoriaNonFoglia radice = new CategoriaNonFoglia(nomeGerarchia, campoCaratt, dimensioneDominio);
		Gerarchia nuovaGerarchia = new Gerarchia(radice, config, compr);
		return nuovaGerarchia;
	}
	
	/**
	 * Metodo di aggiunta sottocategoria. 
	 * Richiama la funzione di creazione categoria per ogni valore del campo caratteristico
	 * della categoria passata come parametro.
	 * @param categoria a cui aggiungere sottocategorie
	 */
	private void addSottoCategorie(Categoria categoria) {
		for(Entry<String, String> v: categoria.getValoriCampo().entrySet()) {
			creaCategoria(categoria);
		}
		
	}
	
	/**
	 * Metodo di creazione categoria che rimanda a uno dei due casi tra foglia e non foglia.
	 * @param radice = categoria di aggancio
	 */
	private void creaCategoria(Categoria radice) {
		int scelta = InputDati.leggiIntero(MSG_CERAZIONE_NODI, 1, 2);
		
		switch(scelta) {
		case 1:
			creaCategoriaNonFoglia(radice);
			break;
		case 2:
			creaCategoriaFoglia(radice);
			break;
		default:
			return;
		}
	}

	/**
	 * Metodo di creazione categoria non foglia.
	 * Controlla l'unicità del nome.
	 * Richiede l'inserimento del nome del campo caratteristico e dei sui valori.
	 * @param radice = categoria di aggancio
	 */
	private void creaCategoriaNonFoglia(Categoria radice) {
		v.mostraMessaggio(MSG_CATEGORIA_NON_FOGLIA);
		
		String nomeCatNonFl = InputDati.leggiStringaNonVuota(MSG_NOME_CATEGORIA);
		for(Categoria c: radice.getSottoCateg()) {
			if(c.eUguale(nomeCatNonFl)) {
				v.mostraErrore(MSG_NOME_CATEGORIA_NON_VALIDO);
				return;
			}
		}
		String nomeCampo = InputDati.leggiStringaNonVuota(MSG_NOME_CAMPOCARATT);
		HashMap<String, String> valoriCampo = new HashMap<>();
		
		boolean continua = true;
		while(continua) {
			String valore = InputDati.leggiStringaNonVuota(MSG_VALORE_CAMPOCARATT);
			if(valore.equalsIgnoreCase(MSG_TERMINAZIONE)) {
				continua = false;
			} else {
				String desc = InputDati.leggiStringa(MSG_DESCRIZIONE_CAMPOCARATT);
				valoriCampo.put(valore, desc);
			}
		}
		int dimensioneDominio = valoriCampo.size();
		CampoCaratteristico cC = new CampoCaratteristico(nomeCampo, valoriCampo);
		CategoriaNonFoglia catNnF1 = new CategoriaNonFoglia(nomeCatNonFl, cC, dimensioneDominio);
		
		radice.getSottoCateg().add(catNnF1);
		addSottoCategorie(catNnF1);
		
	}

	/**
	 * Metodo che crea una categoria foglia controllando l'unicità del nome e recuperando l'ID
	 * della foglia precedente per ricavare quello nuovo.
	 * Chiama il metodo di aggiunta dell'FDC per il calcolo dei fattori rispetto a quelle preesistenti.
	 * @param radice = categoria di aggancio
	 */
	private void creaCategoriaFoglia(Categoria radice) {
		
		v.mostraMessaggio(MSG_CATEGORIA_FOGLIA);
		String nomeFoglia = InputDati.leggiStringaNonVuota(MSG_NOME_CATEGORIA);
		
		for(Categoria c: radice.getSottoCateg()) {
			if(c.eUguale(nomeFoglia)) {
				v.mostraErrore(MSG_NOME_CATEGORIA_NON_VALIDO);
				return;
			}
		}
		int ultimoID = logica.recuperaUltimoID();
		CategoriaFoglia nuovaCategFoglia = new CategoriaFoglia(nomeFoglia, ultimoID);
		radice.getSottoCateg().add(nuovaCategFoglia);
		
		logica.addCategoriaFoglia(nuovaCategFoglia);
		aggiungiFDC(nuovaCategFoglia.getId());
	}
	
	/**
	 * Metodo che aggiunge un fattore di conversione alla logica.
	 * @param id della foglia nuova
	 */
	private void aggiungiFDC(Integer nuova) {
		logica.aggiungiFDC(nuova);
	}
	
	
	public int selezionaCategoria(ArrayList<CategoriaFoglia> categorieFoglia) {
		v.mostraMessaggio(formattaCategorie(categorieFoglia));
		return InputDati.leggiInteroConMINeMAX(MSG_SELEZIONA_PRESTAZIONE, 0, categorieFoglia.size());
	}


	private String formattaCategorie(ArrayList<CategoriaFoglia> categorieFoglia) {
		StringBuffer sb = new StringBuffer();
		sb.append(MSG_PRESTAZIONI_DISPONIBILI);
		for(int i = 0; i < categorieFoglia.size(); i++) {
			sb.append(i + ": " + categorieFoglia.get(i).getNome());
		}
		sb.append(categorieFoglia.size() + MSG_ANNULLA);
		return sb.toString();
	}
	
	/**
	 * Metodo di visualizzazione delle gerarchie.
	 */
	public String formattaGerarchie() {
		return Utilitaria.formatta(gerarchie);
	}
	
	/**
	 * Metodo di salvataggio gerarchie, categorie foglia e matrice dei fattori di conversione.
	 */
	public void salvaGerarchieEFoglie() {
		GestorePersistenza.salvaGerarchie(logica.getGerarchie());
		GestorePersistenza.salvaCategorieFoglia(logica.getCategorieFoglia());
		GestorePersistenza.salvaFatConversione(logica.getFatConversione());
	}

}
