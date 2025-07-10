package vista;


public class VistaConfiguratore extends Vista implements IVistaConfiguratore{
	
	/**
	 * ERRORI / ANNULLAMENTO
	 */
	private static final String NESSUN_COMPRENSORIO = "Non è ancora presente nessun comprensorio";
	private static final String NESSUNA_GERARCHIA = "Non è ancora presente nessuna gerarchia";
	private static final String NESSUN_FDC = "Nessun fattore di conversione presente";
	
	private static final String MSG_OPERAZIONE_ANNULLATA = "\nOperazione annullata....\n";
	private static final String MSG_ASSENZA_PROPOSTE_PER_PRESTAZIONE = "\nNon è presente nessuna proposta per questa prestazione.\n";
	private static final String MSG_ASSENZA_INSIEMECHIUSO = "\nNon è presente nessun insieme chiuso.\n";
	
	/**
	 * Metodo di visualizzazione dei comprensori geografici.
	 */
	public void visualizzaComprensori(String sb) {
		visualizza(sb, NESSUN_COMPRENSORIO);
	}

	/**
	 * Metodo di visualizzazione delle gerarchie.
	 */
	public void visualizzaGerarchie(String sb) {
		visualizza(sb, NESSUNA_GERARCHIA);
	}

	/**
	 * Metodo di visualizzazione della matrice dei fattori di conversione.
	 */
	public void visualizzaFatConv(String sb) {
		visualizza(sb, NESSUN_FDC);
	}
	
	/**
	 * Metodo per visualizzare le proposte relative ad una specifica categoria foglia
	 */
	public void visualizzaProposte(String sb) {
		if(sb.equals(MSG_OPERAZIONE_ANNULLATA))
			visualizza(sb, MSG_OPERAZIONE_ANNULLATA);
		else
			visualizza(sb, MSG_ASSENZA_PROPOSTE_PER_PRESTAZIONE);
	}
	
	/**
	 * Metodo per andare a visualizzare l'insiemi chiusi in modo da poter poi contatare i fruitori associati
	 */
	public void visualizzaInsiemiChiusi(String sb) {
		visualizza(sb, MSG_ASSENZA_INSIEMECHIUSO);
		
	}

}
