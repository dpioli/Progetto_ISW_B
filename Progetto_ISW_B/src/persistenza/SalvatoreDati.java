package persistenza;

import java.io.*;
import com.google.gson.*;

public class SalvatoreDati {

    private static final String MSG_ERRORE_SALVATAGGIO = "Errore durante il salvataggio: ";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    
    /**
	 * Metodo generico per salvare i dati su un file json
	 * @param <T>
	 * @param oggetto
	 * @param fpath
	 */
    public static <T> void salva(T oggetto, String fpath) {
        try (FileWriter wr = new FileWriter(fpath)) {
            gson.toJson(oggetto, wr);
        } catch (IOException e) {
            System.err.println(MSG_ERRORE_SALVATAGGIO + e.getMessage());
        }
    }
}
