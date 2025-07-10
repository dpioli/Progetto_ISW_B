package vista;

public class VistaFruitore extends Vista implements IVistaFruitore{
	
	private static final String MSG_ASSENZA_PROPOSTE = "\nNon ci sono proposte presenti.\n";
	
	/**
	 * Metodo di visualizzazione delle proposte.
	 */
	public void visualizzaProposte(String sb) {
		visualizza(sb, MSG_ASSENZA_PROPOSTE);
	}
	
	

}
