package vista;

import java.util.ArrayList;

import applicazione.PropostaScambio;
import utenti.Fruitore;
import util.Utilitaria;

/**
 * Classe che implementa Vista (implementazione di IVista) e implementa IVistaFruitore.
 * La vista del Fruitore potrebbe essere distinta da quella di altri tipi di utente.
 */
public class VistaFruitore extends Vista implements IVistaFruitore{
	
	private static final String MSG_ASSENZA_PROPOSTE = "\nNon ci sono proposte presenti.\n";
	private static final String NEW_LINE_ARROW = "\n> ";
	
	public void visualizzaProposte(String sb) {
		visualizza(sb, MSG_ASSENZA_PROPOSTE);
	}
	public void visualizzaPropostePerFruitore(Fruitore fruit, ArrayList<PropostaScambio> proposte) {
		visualizza(formattaPropostePerFruitore(fruit, proposte), MSG_ASSENZA_PROPOSTE);
	}
	
	
	/**
	 * Metodo per la visualizzazione delle proposte da parte del fruitore
	 * sia che siano aperte, chiuse, ritirate.
	 * Effettuato solo il controllo per vedere quelle di cui il fruitore è autore
	 */
	private String formattaPropostePerFruitore(Fruitore fruit, ArrayList<PropostaScambio> proposte) {
		ArrayList<PropostaScambio> proposteFruit = new ArrayList<>();	
		StringBuffer sb = new StringBuffer();
		
		if(proposte.isEmpty()) {
			return MSG_ASSENZA_PROPOSTE;
		}
			
		for(PropostaScambio p: proposte) {
			boolean corrisponde = fruit.getUsername().equals(p.getNomeAssociato());
			if(corrisponde)
				proposteFruit.add(p);
		}
		
		if(proposteFruit.isEmpty()) {
			return MSG_ASSENZA_PROPOSTE;
		} else {
			for(PropostaScambio p: proposteFruit) {
				sb.append(NEW_LINE_ARROW + Utilitaria.formattaScambio(p));
			}
			return sb.toString();
				
		}
	}

}
