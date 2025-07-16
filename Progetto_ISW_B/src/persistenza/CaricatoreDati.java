package persistenza;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class CaricatoreDati {

    private static final String MSG_FILE_NON_TROVATO = "File non trovato: ";
    private static final String MSG_ERRORE_CARICAMENTO_FILE = "Errore durante il caricamento: ";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    
    /**
	 * Metodo generico per cericare i dati salvati su un file json
	 * @param <T>
	 * @param obj
	 * @param fpath
	 * @return oggetto generico salvato sul file
	 */
    public static <T> T carica(Type typeOfT, String fpath) {
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
	public static <T> ArrayList<T> caricaLista(TypeToken<ArrayList<T>> typeToken, String nomeFile) {
		 Type listType = typeToken.getType();
		 ArrayList<T> lista = carica(listType, nomeFile);
		 return (lista != null) ? lista : new ArrayList<>();
	}
}
