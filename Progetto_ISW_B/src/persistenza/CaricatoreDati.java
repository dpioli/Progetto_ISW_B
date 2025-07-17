package persistenza;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import applicazione.CategoriaFoglia;
import applicazione.Comprensorio;
import applicazione.FatConversione;
import applicazione.Gerarchia;
import applicazione.InsiemeChiuso;
import applicazione.PropostaScambio;
import utenti.Configuratore;
import utenti.Fruitore;


/**
 * Classe con responsabilità di caricamento dati
 * 
 * @author Irene Romano 736566
 */
public class CaricatoreDati {
	
	private static final String FILE_CONFIGURATORI = "../Progetto_ISW_B/src/dati/configuratori.json";
	private static final String FILE_GERARCHIE = "../Progetto_ISW_B/src/dati/gerarchie.json";
	private static final String FILE_COMPRENSORI = "../Progetto_ISW_B/src/dati/comprensori.json";
	private static final String FILE_FATT_CONVERSIONE = "../Progetto_ISW_B/src/dati/fattConversione.json";
	private static final String FILE_CATEGORIEFOGLIA = "../Progetto_ISW_B/src/dati/categorieFoglia.json";
	private static final String FILE_FRUITORI = "../Progetto_ISW_B/src/dati/fruitori.json";
	private static final String FILE_PROPOSTE = "../Progetto_ISW_B/src/dati/proposte.json";
	private static final String FILE_INSIEMI_CHIUSI = "../Progetto_ISW_B/src/dati/insiemiChiusi.json";

    private static final String MSG_FILE_NON_TROVATO = "File non trovato: ";
    private static final String MSG_ERRORE_CARICAMENTO_FILE = "Errore durante il caricamento: ";

    private final Gson gson;
    
    public CaricatoreDati(Gson gson) {
    	this.gson = gson;
    }
    
    /**
	 * Metodo generico per cericare i dati salvati su un file json
	 * @param <T>
	 * @param obj
	 * @param fpath
	 * @return oggetto generico salvato sul file
	 */
    public <T> T carica(Type typeOfT, String fpath) {
        T oggetto = null;
        File file = new File(fpath);
        if (!file.exists()) {
            System.err.println(MSG_FILE_NON_TROVATO + fpath);
            return null;
        }
        try (FileReader rd = new FileReader(fpath)) {
            oggetto = gson.fromJson(rd, typeOfT);
        } catch (IOException e) {
            System.err.println(MSG_ERRORE_CARICAMENTO_FILE + e.getMessage());
        }
        return oggetto != null ? oggetto : null; // per collezioni non creiamo nuovi oggetti vuoti
    }
    
    
    /**
	 * Metodo generico per caricare una lista di oggetti
	 * @param <T>
	 * @param typeToken
	 * @param nomeFile
	 * @return
	 */
	public <T> ArrayList<T> caricaLista(TypeToken<ArrayList<T>> typeToken, String nomeFile) {
		 Type listType = typeToken.getType();
		 ArrayList<T> lista = carica(listType, nomeFile);
		 return (lista != null) ? lista : new ArrayList<>();
	}
	
	
	/**
	 * Metodo per caricare i configuratori
	 * @return insieme dei configuratori
	 */
	public ArrayList<Configuratore> caricaConfiguratori() {
	     return caricaLista(new TypeToken<ArrayList<Configuratore>>() {}, FILE_CONFIGURATORI);
	}

	
	/**
	 * Metodo per caricare i fruitori
	 * @return lista dei fruitori registrati
	 */
	public ArrayList<Fruitore> caricaFruitori() {
		return caricaLista(new TypeToken<ArrayList<Fruitore>>() {}, FILE_FRUITORI);
	}
	
	
	/**
	 * Metodo per caricare i comprensori
	 * @return insieme dei comprensori
	 */
	 public ArrayList<Comprensorio> caricaComprensorio() {
	     return caricaLista(new TypeToken<ArrayList<Comprensorio>>() {}, FILE_COMPRENSORI);
	 }
	
	
	/**
	 * Metodo per caricare le categorie foglia presenti
	 * @return lista delle categorie foglia
	 */
	public ArrayList<CategoriaFoglia> caricaCategorieFoglia() {
	     return caricaLista(new TypeToken<ArrayList<CategoriaFoglia>>() {}, FILE_CATEGORIEFOGLIA);
	}
	
	
	/**
	 * Metodo per caricare le gerarchie
	 * @return insieme delle gerarchie
	 */
	public ArrayList<Gerarchia> caricaGerarchie() {
        return caricaLista(new TypeToken<ArrayList<Gerarchia>>() {}, FILE_GERARCHIE);
    }

	
	/**
	 * Metodo per caricare le proposte di scambio che sono state accettate
	 * @return lista delle proposte accettate
	 */
	public ArrayList<PropostaScambio> caricaScambi() {
		return caricaLista(new TypeToken<ArrayList<PropostaScambio>>() {}, FILE_PROPOSTE);
	}
	
	
	/**
	 * Metodo per caricare gli insiemi chiusi
	 * @return lista degli insiemi chiusi
	 */
	public ArrayList<InsiemeChiuso> caricaInsiemi() {
		return caricaLista(new TypeToken<ArrayList<InsiemeChiuso>>() {}, FILE_INSIEMI_CHIUSI);
	}
	
	
	/**
	 * Metodo per caricare i fattori di conversione
	 * @return insieme dei fattori di conversione
	 */
	public FatConversione caricaFatConversione(){
		Type listType = new TypeToken<FatConversione>() {}.getType();
		FatConversione fatConversione = carica(listType, FILE_FATT_CONVERSIONE);
		if(fatConversione == null) {
			return new FatConversione();
		}
		return fatConversione;
	}
}
