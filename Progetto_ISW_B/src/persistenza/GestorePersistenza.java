package persistenza;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Classe per la gestione della persistenza dei dati
 * 
 * @author Erjona Maxhaku 735766
 */
public class GestorePersistenza {
	
	private final Gson gson;
    private final CaricatoreDati caricatore;
    private final SalvatoreDati salvatore;
    
    /**
	 * Costruttore della classe GestorePersistenza
	 */
    public GestorePersistenza() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.caricatore = new CaricatoreDati(gson);
        this.salvatore = new SalvatoreDati(gson);
    }

    public CaricatoreDati getCaricatore() {
        return caricatore;
    }

    public SalvatoreDati getSalvatore() {
        return salvatore;
    }
	
	

}
