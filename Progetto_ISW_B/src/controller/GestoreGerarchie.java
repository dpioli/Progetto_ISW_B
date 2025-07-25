package controller;

import java.util.ArrayList;
import java.util.HashMap;
import applicazione.CampoCaratteristico;
import applicazione.CategoriaFoglia;
import applicazione.CategoriaNonFoglia;
import applicazione.Comprensorio;
import applicazione.Gerarchia;
import applicazione.PropostaScambio;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Configuratore;
import utenti.Fruitore;
import util.InputDati;
import util.Utilitaria;
import vista.Vista;

/**
 * Controller GRASP che contiene la logica applicativa riguardante gli oggetti Gerarchia.
 * Mantiene un riferimento a un GestoreCategoria in quanto le gerarchie sono viste come liste di categorie a partire da una categoria radice.
 */
public class GestoreGerarchie {
	
	private static final String MSG_NESSUN_COMPRENSORIO = "Non è presente nessun comprensorio all'interno del sistema, creane uno prima di creare una gerarchia.";
	private static final String MSG_CREAZIONE_GERARCHIA = "Stai creando una nuova gerarchie, inserisci le informazioni necessarie";	

	private static final String MSG_NOME_GERARCHIA = "Inserisci il nome della nuova gerarchia >";
	private static final String MSG_NOME_GERARCHIA_NON_VALIDO = "E' già presente una gerarchia con questo nome.";
	private static final String MSG_NOME_CAMPOCARATT = "Inserisci il nome del campo caratteristico > ";
	private static final String MSG_VALORE_CAMPOCARATT = "Inserisci il valore ('fine' per terminare) > ";
	private static final String MSG_DESCRIZIONE_CAMPOCARATT = "Inserisci la descrizione per questo valore (premere invio altrimenti) > ";
	private static final String MSG_INSERISCI_SOTTOCATEG = "Inserisci le sottocategorie della gerarchia appena creata:\n ";
	private static final String MSG_GERARCHIA_CREATA_CON_SUCCESSO = "La gerarchia è stata creata con successo!";
	
	private static final String MSG_ASSENZA_PROPOSTE_PER_PRESTAZIONE = "\nNon è presente nessuna proposta per questa prestazione.\n";
	private static final String MSG_ELENCO_PROPOSTE = "\nElenco proposte in cui è presente -> ";
	private static final String MSG_OPERAZIONE_ANNULLATA = "\nOperazione annullata....\n";

	
	private static final String MSG_INIZIALE = "Gerarchie presenti nel tuo comprensorio:";
	private static final String MSG_SELEZ_GERARCH = "Seleziona una gerarchia > ";
	private static final String MSG_ANNULLATO_SCAMBIO = "Hai annullato la proposta di scambio...";	
	private static final String MSG_TERMINAZIONE = "fine";
	private static final String COLON = ": ";
	private static final String X = "\n******************************************";
	
	LogicaPersistenza logica;
	ArrayList<Gerarchia> gerarchie; 
	Vista v;
	GestoreCategorie gC;
	
	public GestoreGerarchie(LogicaPersistenza logica, Vista v) {
		this.logica = logica;
		this.gerarchie = logica.getGerarchie();
		this.v = v;
		this.gC = new GestoreCategorie(logica, v);
	}

	
	/*
	 * 
	 * 
	 *  CONFIGURATORE
	 * 
	 * 
	 */
	
	
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
		gC.addSottoCategorie(nuovaGerarchia.getCatRadice());
		
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
	 * Metodo di visualizzazione delle gerarchie.
	 */
	public String formattaGerarchie() {
		return Utilitaria.formattaLista(gerarchie);
	}
	
	
	/**
	 * Metodo di salvataggio gerarchie, categorie foglia e matrice dei fattori di conversione.
	 */
	public void salvaGerarchieEFoglie() {
		GestorePersistenza gp = new GestorePersistenza();
		gp.getSalvatore().salvaGerarchie(logica.getGerarchie());
		gp.getSalvatore().salvaCategorieFoglia(logica.getCategorieFoglia());
		gp.getSalvatore().salvaFatConversione(logica.getFatConversione());
	}
	
	
	/*
	 * 
	 * 
	 *  FRUITORE
	 * 
	 * 
	 */
	
	/**
	 * Metodo per navigare in profondita' tra le gerarchie
	 */
	public void naviga(Fruitore fruit) {
		v.mostraMessaggio(X);
		v.mostraMessaggio(MSG_INIZIALE);
		ArrayList<Gerarchia> gerarchia = new ArrayList<Gerarchia>();
		for(Gerarchia g: logica.getGerarchie()) {
			if(g.getNomeComprensorio().equals(fruit.getNomeComprensorio())) {
				gerarchia.add(g);
			}
		}
		Gerarchia gScelta;
		if(gerarchia.isEmpty()) {
			v.mostraErrore(MSG_ANNULLATO_SCAMBIO);
			return;
		} else {
			gScelta = selezionaGerarchia(gerarchia);
            gC.navigaCategoria(gScelta.getCatRadice(), new HashMap<>()); 
            // Inizializziamo la mappa dei valori di campo per la navigazione
		}	
	}
	
	
	/**
	 * Metodo per selezionare una gerarchia presente all'interno del comprensorio selezionato
	 * @param gerarch
	 * @return
	 */
	private Gerarchia selezionaGerarchia(ArrayList<Gerarchia> gerarch) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < gerarch.size(); i++) {
			sb.append(i + COLON + gerarch.get(i).getNomeRadice() + "\n"); 
		}
		v.mostraMessaggio(sb.toString());
		
		int scelta = InputDati.leggiIntero(MSG_SELEZ_GERARCH, 0, gerarch.size() - 1);
		return gerarch.get(scelta);
	}
	
	public int selezionaCategoria(ArrayList<CategoriaFoglia> categorieFoglia) {
		return gC.selezionaCategoria(categorieFoglia);
	}
	
	public ArrayList<CategoriaFoglia> recuperaFoglieDisponibili(Fruitore fruit) {
	   return gC.recuperaFoglieDisponibili(fruit);
	}
	
	/**
	 * Metodo di formattazione prestazioni
	 * @param categorieFoglia = lista delle categorie foglia a disposizione tra cui cercare
	 * @param proposte = lista delle proposte
	 * @return stringa delle proposte (richieste/offerte) presenti tra le foglie
	 */
	public String formattaProposteDiFoglia(ArrayList<CategoriaFoglia> categorieFoglia, ArrayList<PropostaScambio> proposte) {
		//return gProp.formattaProposteDiFoglia(gGer);
		int selezionata = selezionaCategoria(categorieFoglia);
		
		if(selezionata == categorieFoglia.size()) {
			return MSG_OPERAZIONE_ANNULLATA;
		}
			
		CategoriaFoglia f = categorieFoglia.get(selezionata);
		StringBuffer sb = new StringBuffer();
		boolean presenteProposta = false;
		
		for(PropostaScambio p : proposte) {
			boolean presenteRichiesta = p.getNomeRichiesta().equals(f.getNome());
			boolean presenteOfferta = p.getNomeOfferta().equals(f.getNome());
			
			if(presenteRichiesta || presenteOfferta) {
				if(!presenteProposta) {
					sb.append(MSG_ELENCO_PROPOSTE)
						.append(f.getNome().toUpperCase())
						.append(":\n");
					presenteProposta = true;
				}
				
				sb.append("> ")
					.append( Utilitaria.formattaScambio(p) )
					.append("\n\t")
					.append(Utilitaria.formattaAssociato(p));
			}
		}
		if(presenteProposta) {
			return sb.toString();
		} else {
			return MSG_ASSENZA_PROPOSTE_PER_PRESTAZIONE;
		}
	}

}
