package persistenza;

import java.io.*;
import java.util.ArrayList;

import com.google.gson.*;

import applicazione.CategoriaFoglia;
import applicazione.Comprensorio;
import applicazione.FatConversione;
import applicazione.Gerarchia;
import applicazione.InsiemeChiuso;
import applicazione.PropostaScambio;
import utenti.Configuratore;
import utenti.Fruitore;

/**
 * Classe per salvare i dati
 * 
 * @author Irene Romano 736566
 */
public class SalvatoreDati {
	
	private static final String FILE_CONFIGURATORI = "../Progetto_ISW_B/src/dati/configuratori.json";
	private static final String FILE_GERARCHIE = "../Progetto_ISW_B/src/dati/gerarchie.json";
	private static final String FILE_COMPRENSORI = "../Progetto_ISW_B/src/dati/comprensori.json";
	private static final String FILE_FATT_CONVERSIONE = "../Progetto_ISW_B/src/dati/fattConversione.json";
	private static final String FILE_CATEGORIEFOGLIA = "../Progetto_ISW_B/src/dati/categorieFoglia.json";
	private static final String FILE_FRUITORI = "../Progetto_ISW_B/src/dati/fruitori.json";
	private static final String FILE_PROPOSTE = "../Progetto_ISW_B/src/dati/proposte.json";
	private static final String FILE_INSIEMI_CHIUSI = "../Progetto_ISW_B/src/dati/insiemiChiusi.json";

    private static final String MSG_ERRORE_SALVATAGGIO = "Errore durante il salvataggio: ";

    private final Gson gson;

    public SalvatoreDati(Gson gson) {
        this.gson = gson;
    }

    /**
	 * Metodo generico per salvare i dati su un file json
	 * @param <T>
	 * @param oggetto
	 * @param fpath
	 */
    public <T> void salva(T oggetto, String fpath) {
        try (FileWriter wr = new FileWriter(fpath)) {
            gson.toJson(oggetto, wr);
        } catch (IOException e) {
            System.err.println(MSG_ERRORE_SALVATAGGIO + e.getMessage());
        }
    }
    
    
    /**
	 * Metodo per salvare le gerarchie
	 * @param gerarchie
	 */
	public void salvaGerarchie(ArrayList<Gerarchia> gerarchie) {
		salva(gerarchie, FILE_GERARCHIE);
	}
	
	/**
	 * Metodo per salvare i comprensori
	 * @param comprensori
	 */
	public void salvaComprensori(ArrayList<Comprensorio> comprensori) {
		salva(comprensori, FILE_COMPRENSORI);
	}
	
	/**
	 * Metodo per salvare i configuratori
	 * @param configuratori
	 */
	public void salvaConfiguratori(ArrayList<Configuratore> configuratori) {
		salva(configuratori, FILE_CONFIGURATORI);
	}
	
	/**
	 * Metodo per salvare i fattori di conversione
	 * @param fatConversione
	 */
	public void salvaFatConversione(FatConversione fatConversione) {
		salva(fatConversione, FILE_FATT_CONVERSIONE);
	}
	
	/**
	 * Metodo per salvare le categorie foglia di tutte le gerarchie presenti nel sistema
	 * @param categorieFoglia
	 */
	public void salvaCategorieFoglia(ArrayList<CategoriaFoglia> categorieFoglia) {
		salva(categorieFoglia, FILE_CATEGORIEFOGLIA);
	}
	
	/**
	 * Metodo per salvare i profili dei fruitori
	 * @param fruitori
	 */
	public void salvaFruitori(ArrayList<Fruitore> fruitori) {
		salva(fruitori, FILE_FRUITORI);
	}
	
	/**
	 * Metodo per salvare le proposte di scambio che sono state accettate
	 * @param scambi
	 */
	public void salvaScambi(ArrayList<PropostaScambio> scambi) {
		salva(scambi, FILE_PROPOSTE);
	}
	
	/**
	 * Metodo per salvare l'insimei chiusi che sono stati completati
	 * @param insiemi
	 */
	public void salvaInsiemiChiusi(ArrayList<InsiemeChiuso> insiemi) {
		salva(insiemi, FILE_INSIEMI_CHIUSI);
	}
	
	
	public void salvaTutto(LogicaPersistenza logica) {
		salvaGerarchie(logica.getGerarchie());
		salvaCategorieFoglia(logica.getCategorieFoglia());
		salvaFatConversione(logica.getFatConversione());
		salvaConfiguratori(logica.getConfiguratori());
		salvaComprensori(logica.getComprensori());
		salvaFruitori(logica.getFruitori());
		salvaScambi(logica.getScambi());
		salvaInsiemiChiusi(logica.getInsiemi());
	}
	
}
