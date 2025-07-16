package persistenza;

//import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

//import com.google.gson.*;
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
 * Classe per la gestione della persistenza dei dati
 * 
 * @author Erjona Maxhaku 735766
 */
public class GestorePersistenza {
	
	private static final String FILE_CONFIGURATORI = "../Progetto_ISW_B/src/dati/configuratori.json";
	private static final String FILE_GERARCHIE = "../Progetto_ISW_B/src/dati/gerarchie.json";
	private static final String FILE_COMPRENSORI = "../Progetto_ISW_B/src/dati/comprensori.json";
	private static final String FILE_FATT_CONVERSIONE = "../Progetto_ISW_B/src/dati/fattConversione.json";
	private static final String FILE_CATEGORIEFOGLIA = "../Progetto_ISW_B/src/dati/categorieFoglia.json";
	private static final String FILE_FRUITORI = "../Progetto_ISW_B/src/dati/fruitori.json";
	private static final String FILE_PROPOSTE = "../Progetto_ISW_B/src/dati/proposte.json";
	private static final String FILE_INSIEMI_CHIUSI = "../Progetto_ISW_B/src/dati/insiemiChiusi.json";
	
	/**
	 * Costruttore della classe GestorePersistenza
	 */
	public GestorePersistenza() {}
	
	
	/*
	 * 
	 * 
	 * METODI PER SALVARE SU FILE TRAMITE JSON
	 * 
	 *  
	 */
	
	/**
	 * Metodo per salvare le gerarchie
	 * @param gerarchie
	 */
	public static void salvaGerarchie(ArrayList<Gerarchia> gerarchie) {
		SalvatoreDati.salva(gerarchie, FILE_GERARCHIE);
	}
	
	/**
	 * Metodo per salvare i comprensori
	 * @param comprensori
	 */
	public static void salvaComprensori(ArrayList<Comprensorio> comprensori) {
		SalvatoreDati.salva(comprensori, FILE_COMPRENSORI);
	}
	
	/**
	 * Metodo per salvare i configuratori
	 * @param configuratori
	 */
	public static void salvaConfiguratori(ArrayList<Configuratore> configuratori) {
		SalvatoreDati.salva(configuratori, FILE_CONFIGURATORI);
	}
	
	/**
	 * Metodo per salvare i fattori di conversione
	 * @param fatConversione
	 */
	public static void salvaFatConversione(FatConversione fatConversione) {
		SalvatoreDati.salva(fatConversione, FILE_FATT_CONVERSIONE);
	}
	
	/**
	 * Metodo per salvare le categorie foglia di tutte le gerarchie presenti nel sistema
	 * @param categorieFoglia
	 */
	public static void salvaCategorieFoglia(ArrayList<CategoriaFoglia> categorieFoglia) {
		SalvatoreDati.salva(categorieFoglia, FILE_CATEGORIEFOGLIA);
	}
	
	/**
	 * Metodo per salvare i profili dei fruitori
	 * @param fruitori
	 */
	public static void salvaFruitori(ArrayList<Fruitore> fruitori) {
		SalvatoreDati.salva(fruitori, FILE_FRUITORI);
	}
	
	/**
	 * Metodo per salvare le proposte di scambio che sono state accettate
	 * @param scambi
	 */
	public static void salvaScambi(ArrayList<PropostaScambio> scambi) {
		SalvatoreDati.salva(scambi, FILE_PROPOSTE);
	}
	
	/**
	 * Metodo per salvare l'insimei chiusi che sono stati completati
	 * @param insiemi
	 */
	public static void salvaInsiemiChiusi(ArrayList<InsiemeChiuso> insiemi) {
		SalvatoreDati.salva(insiemi, FILE_INSIEMI_CHIUSI);
	}
	
	
	public static void salvaTutto(LogicaPersistenza logica) {
		salvaGerarchie(logica.getGerarchie());
		salvaCategorieFoglia(logica.getCategorieFoglia());
		salvaFatConversione(logica.getFatConversione());
		salvaConfiguratori(logica.getConfiguratori());
		salvaComprensori(logica.getComprensori());
		salvaFruitori(logica.getFruitori());
		salvaScambi(logica.getScambi());
		salvaInsiemiChiusi(logica.getInsiemi());
	}
	
	
	
	/*
	 * 
	 * 
	 * METODI PER CARICARE I FILE DA JSON
	 * 
	 * 
	 */
	
	
	/**
	 * Metodo per caricare i configuratori
	 * @return insieme dei configuratori
	 */
	public static ArrayList<Configuratore> caricaConfiguratori() {
	     return CaricatoreDati.caricaLista(new TypeToken<ArrayList<Configuratore>>() {}, FILE_CONFIGURATORI);
	}

	
	/**
	 * Metodo per caricare i fruitori
	 * @return lista dei fruitori registrati
	 */
	public static ArrayList<Fruitore> caricaFruitori() {
		return CaricatoreDati.caricaLista(new TypeToken<ArrayList<Fruitore>>() {}, FILE_FRUITORI);
	}
	
	
	/**
	 * Metodo per caricare i comprensori
	 * @return insieme dei comprensori
	 */
	 public static ArrayList<Comprensorio> caricaComprensorio() {
	     return CaricatoreDati.caricaLista(new TypeToken<ArrayList<Comprensorio>>() {}, FILE_COMPRENSORI);
	 }
	
	
	/**
	 * Metodo per caricare le categorie foglia presenti
	 * @return lista delle categorie foglia
	 */
	public static ArrayList<CategoriaFoglia> caricaCategorieFoglia() {
	     return CaricatoreDati.caricaLista(new TypeToken<ArrayList<CategoriaFoglia>>() {}, FILE_CATEGORIEFOGLIA);
	}
	
	
	/**
	 * Metodo per caricare le gerarchie
	 * @return insieme delle gerarchie
	 */
	public static ArrayList<Gerarchia> caricaGerarchie() {
        return CaricatoreDati.caricaLista(new TypeToken<ArrayList<Gerarchia>>() {}, FILE_GERARCHIE);
    }

	
	/**
	 * Metodo per caricare le proposte di scambio che sono state accettate
	 * @return lista delle proposte accettate
	 */
	public static ArrayList<PropostaScambio> caricaScambi() {
		return CaricatoreDati.caricaLista(new TypeToken<ArrayList<PropostaScambio>>() {}, FILE_PROPOSTE);
	}
	
	
	/**
	 * Metodo per caricare gli insiemi chiusi
	 * @return lista degli insiemi chiusi
	 */
	public static ArrayList<InsiemeChiuso> caricaInsiemi() {
		return CaricatoreDati.caricaLista(new TypeToken<ArrayList<InsiemeChiuso>>() {}, FILE_INSIEMI_CHIUSI);
	}
	
	
	/**
	 * Metodo per caricare i fattori di conversione
	 * @return insieme dei fattori di conversione
	 */
	public static FatConversione caricaFatConversione(){
		Type listType = new TypeToken<FatConversione>() {}.getType();
		FatConversione fatConversione = CaricatoreDati.carica(listType, FILE_FATT_CONVERSIONE);
		if(fatConversione == null) {
			return new FatConversione();
		}
		return fatConversione;
	}

}
