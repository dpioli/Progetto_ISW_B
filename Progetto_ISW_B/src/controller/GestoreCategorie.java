package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import applicazione.CampoCaratteristico;
import applicazione.CategoriaComponent;
import applicazione.CategoriaFoglia;
import applicazione.CategoriaNonFoglia;
import applicazione.Gerarchia;
import persistenza.LogicaPersistenza;
import utenti.Fruitore;
import util.InputDati;
import vista.Vista;

/**
 * Controller GRASP che contiene la logica applicativa riguardante gli oggetti Categoria.
 */
public class GestoreCategorie {
	
	/**
	 * CONFIGURATORE
	 */
	private static final String MSG_NOME_CAMPOCARATT = "Inserisci il nome del campo caratteristico > ";
	private static final String MSG_VALORE_CAMPOCARATT = "Inserisci il valore ('fine' per terminare) > ";
	private static final String MSG_DESCRIZIONE_CAMPOCARATT = "Inserisci la descrizione per questo valore (premere invio altrimenti) > ";
	
	private static final String MSG_CERAZIONE_NODI = "Vuoi creare una categoria intermedia (1) o una foglia (2)? > ";
	
	private static final String MSG_CATEGORIA_NON_FOGLIA = "Stai creando una categroia intermedia:";
	private static final String MSG_CATEGORIA_FOGLIA = "Stai creando una categoria foglia: ";
	private static final String MSG_NOME_CATEGORIA = "Inserisci il nome della categoria >";
	private static final String MSG_NOME_CATEGORIA_NON_VALIDO = "E' già presente una categoria con questo nome.";

	private static final String MSG_SELEZIONA_PRESTAZIONE = "\nSeleziona la prestazione per cui vuoi ottenere le proposte (annulla altrimenti) > ";	
	private static final String MSG_TERMINAZIONE = "fine";

	/*
	 * FRUITORE
	 */
	private static final String MSG_SELEZ_GERARCH = "Seleziona una gerarchia > ";
	private static final String CATEGORIA_CORRENTE = "\nCategoria corrente: ";
	private static final String VALORI_IMPOSTATI = "Valori impostati finora: ";
	private static final String MSG_CATEG_FOGLIA = "Sei arrivato a una categoria foglia: ";
	private static final String MSG_MENU_PRINCIPALE = "Ritorno al menu principale.\n";
	private static final String MSG_ASSENZA_SOTTOCATEG = "Non ci sono sottocategorie. Ritorno al menu principale.";
	private static final String MSG_CAMPO_CARATT = "Campo caratteristico: ";
	private static final String ERRORE_AGGIUNTA_FIGLI = "Errore nell'aggiunta di sottocategorie a una NonFoglia.";
	private static final String MSG_SOTTOCATEG_DISP = "Sottocategorie disponibili:";
	private static final String MSG_CHECK_COMPRENSORIO = "\nNon ci sono Gerarchie appartenenti al tuo comprensorio geografico.\n";
	private static final String MSG_PRESTAZIONI_A_DISPOSIZIONE = "Prestazioni a disposizione >>\n";
	
	private static final String DOT = ". ";
	private static final String COLON = ": ";
	private static final String NEW_LINE = "\n";
	private static final String MSG_VOCE_TORNA_INDIETRO = "0. Torna al menu";
	
	LogicaPersistenza logica;
	Vista v;
	
	public GestoreCategorie(LogicaPersistenza logica, Vista v) {
		this.logica = logica;
		this.v = v;
	}
	
	
	/**
	 * Metodo di aggiunta sottocategoria. 
	 * Richiama la funzione di creazione categoria per ogni valore del campo caratteristico
	 * della categoria passata come parametro.
	 * @param categoria a cui aggiungere sottocategorie
	 */
	public void addSottoCategorie(CategoriaComponent categoria) {
		if(categoria.isFoglia()) return;
	    
		if(categoria instanceof CategoriaNonFoglia nonFoglia) {
			for(Entry<String, String> v: nonFoglia.getValoriCampo().entrySet()) {
				creaCategoria(categoria);
			}
		} else {
	        throw new IllegalStateException(ERRORE_AGGIUNTA_FIGLI);
		}
	}
	
	/**
	 * Metodo di creazione categoria che rimanda a uno dei due casi tra foglia e non foglia.
	 * @param radice = categoria di aggancio
	 */
	private void creaCategoria(CategoriaComponent radice) {
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
	private void creaCategoriaNonFoglia(CategoriaComponent radice) {
		v.mostraMessaggio(MSG_CATEGORIA_NON_FOGLIA);
		
		String nomeCatNonFl = InputDati.leggiStringaNonVuota(MSG_NOME_CATEGORIA);
		if (esisteNome(radice, nomeCatNonFl))
		    return;
		
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
		radice.aggiungi(catNnF1);
		addSottoCategorie(catNnF1);
		
	}

	/**
	 * Metodo che crea una categoria foglia controllando l'unicità del nome e recuperando l'ID
	 * della foglia precedente per ricavare quello nuovo.
	 * Chiama il metodo di aggiunta dell'FDC per il calcolo dei fattori rispetto a quelle preesistenti.
	 * @param radice = categoria di aggancio
	 */
	private void creaCategoriaFoglia(CategoriaComponent radice) {
		
		v.mostraMessaggio(MSG_CATEGORIA_FOGLIA);
		
		String nomeFoglia = InputDati.leggiStringaNonVuota(MSG_NOME_CATEGORIA);
		if (esisteNome(radice, nomeFoglia))
		    return;
		
		int ultimoID = recuperaUltimoID();
		CategoriaFoglia nuovaCategFoglia = new CategoriaFoglia(nomeFoglia, ultimoID);
		radice.aggiungi(nuovaCategFoglia);
		
		logica.addCategoriaFoglia(nuovaCategFoglia);
		aggiungiFDC(nuovaCategFoglia.getId());
	}
	
	/**
	 * Metodo che mi permette di controllare l'unicità del nome inserito nella gerarchia.
	 * @param categoria = radice della gerarchia degenere
	 * @param nome
	 * @return vero se ho passato effettivamente una radice ed essa contiene tra i suoi figli una categoria con lo stesso nome.
	 */
	private boolean esisteNome(CategoriaComponent categoria, String nome) {
		if (!categoria.isFoglia() && ((CategoriaNonFoglia) categoria).contieneNome(nome)) {
		    v.mostraErrore(MSG_NOME_CATEGORIA_NON_VALIDO);
		    return true;
		}
		return false;
	}
	
	/**
	 * Metodo per recuperare ultimo identificativo registrato, in modo da mantenere la persistenza coerente.
	 * @return
	 */
	private int recuperaUltimoID() {
		ArrayList<CategoriaFoglia> categorieFoglia = logica.getCategorieFoglia();
		if(categorieFoglia.isEmpty())
			return 0;
		int ultimo = categorieFoglia.size() - 1;
		CategoriaFoglia f = categorieFoglia.get(ultimo);
		return f.getId();
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
	
	/**
	 * Metodo per andare a modificare l'output in base ai percorsi selezionati
	 * @param categoriaCorrente
	 * @param valoriImpostati
	 */
	public void navigaCategoria(CategoriaComponent categoriaCorrente, Map<String, String> valoriImpostati) {
	   v.mostraMessaggio(CATEGORIA_CORRENTE + categoriaCorrente.getNome());

	    if (!valoriImpostati.isEmpty()) {
	 	   v.mostraMessaggio(VALORI_IMPOSTATI + valoriImpostati);
	    }

	    if (categoriaCorrente.isFoglia()) {
	 	   v.mostraMessaggio(MSG_CATEG_FOGLIA + categoriaCorrente.getNome());
		   v.mostraMessaggio(MSG_MENU_PRINCIPALE);
	       return;
	    }

	    List<CategoriaComponent> sottocategorie = new ArrayList<>();
	    if(categoriaCorrente.isFoglia() || categoriaCorrente.getFigli().isEmpty()) {
	    	 v.mostraErrore(MSG_ASSENZA_SOTTOCATEG);
		       return;
	    }
	    sottocategorie = categoriaCorrente.getFigli();

	    CampoCaratteristico campo = ((CategoriaNonFoglia) categoriaCorrente).getCampo();
	    CategoriaComponent prossimaCategoria = null;

	    if (campo != null) {
	 	   v.mostraMessaggio(MSG_CAMPO_CARATT + campo.getNomeCampo());
	    }

	    prossimaCategoria = selezionaSottoCategoria(sottocategorie);

	    if (prossimaCategoria != null) {
	        navigaCategoria(prossimaCategoria, new HashMap<>(valoriImpostati));
	    }
	}
	
	/**
	 * Metodo per selezionare una sottocategoria presente
	 * @param sottocategorie
	 * @return
	 */
	private CategoriaComponent selezionaSottoCategoria(List<CategoriaComponent> sottocategorie) {
		v.mostraMessaggio(MSG_SOTTOCATEG_DISP);
		
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < sottocategorie.size(); i++) {
	       sb.append((i + 1) + DOT + sottocategorie.get(i).getNome() + "\n");
	    }
	    sb.append(MSG_VOCE_TORNA_INDIETRO);
	    v.mostraMessaggio(sb.toString());
	    
	    int scelta = InputDati.leggiIntero(MSG_SELEZ_GERARCH, 0, sottocategorie.size());
	    if (scelta == 0)
	    	return null;
	    
	    return sottocategorie.get(scelta - 1);
	}
	
	
	
	/**
	 * Metodo che recupera le foglie disponibili nel comprensorio geografico del fruitore,
	 *  partendo dalle gerarchie salvate in LogicaPersistenza.
	 * @return array delle foglie a disposizione del fruitore
	 */
	public ArrayList<CategoriaFoglia> recuperaFoglieDisponibili(Fruitore fruit) {
	    ArrayList<CategoriaFoglia> disponibili = new ArrayList<>();
	    
	    for (Gerarchia g : logica.getGerarchie()) {
	        if (g.getNomeComprensorio().equals(fruit.getNomeComprensorio())) {
	            disponibili.addAll(raccoltaFoglie(g.getCatRadice()));
	        }
	    }
	    
	    if(disponibili.isEmpty()) {
			v.mostraErrore(MSG_CHECK_COMPRENSORIO);
			return null;
		}
	    
	    mostraPrestazioni(disponibili); 
	    return disponibili;
	}
	
	/**
	 * Metodo che contolla le sottocategorie di una categoria, inserendo quelle foglia in un array.
	 * @param categoria da cui ricavare le sottocategorie
	 * @return array di foglie
	 */
	private ArrayList<CategoriaFoglia>  raccoltaFoglie(CategoriaComponent cat) {
		ArrayList<CategoriaFoglia> foglie = new ArrayList<>();

		if(cat.isFoglia()) {
			CategoriaFoglia foglia = getFogliaDaNome(cat);
			if (foglia != null) 
				foglie.add(foglia);
	    } else if(cat.getFigli() != null) {
	        for (CategoriaComponent sotto : cat.getFigli()) {
	            foglie.addAll(raccoltaFoglie(sotto));
	        }
	    }
		return foglie;
	}
	
	/**
	 * Metodo che restituisce una foglia presente nel file categorieFoglia
	 * @param categoria che verifico sia di tipo foglia
	 * @return foglia 
	 */
	private CategoriaFoglia getFogliaDaNome(CategoriaComponent cat) {
	    String nomeCategoria = cat.getNome().trim().toLowerCase();

	    for (CategoriaFoglia f : logica.getCategorieFoglia()) {
	        if (f.getNome().trim().toLowerCase().equals(nomeCategoria)) {
	            return f;
	        }
	    }
	    return null;
	}
	
	/**
	 * Metodo di stampa delle prestazioni disponibili
	 * @param foglie
	 */
	private void mostraPrestazioni(ArrayList<CategoriaFoglia> foglie) {
		v.mostraMessaggio(formattaCategorie(foglie));
	}


	private String formattaCategorie(ArrayList<CategoriaFoglia> foglie) {
		StringBuffer sb = new StringBuffer();
		int i = 0; //contatore per legenda
		sb.append(MSG_PRESTAZIONI_A_DISPOSIZIONE);		
		for(CategoriaFoglia f : foglie) {
			sb.append(i++);
			sb.append(COLON);
			sb.append(f.getNome());
			sb.append(NEW_LINE);
		}
		return sb.toString();
	}

}
