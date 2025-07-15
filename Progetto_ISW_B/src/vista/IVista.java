package vista;
/**
 * Interfaccia per vista nella separazioneMVC
 */
public interface IVista {
	    
    public default void mostraMessaggio(String msg) {
        System.out.println(msg);
    }

    public default void mostraErrore(String msg) {
        System.err.println("Errore: " + msg);
    }

}
