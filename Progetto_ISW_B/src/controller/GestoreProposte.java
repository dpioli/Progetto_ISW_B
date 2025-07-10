package controller;

import java.util.ArrayList;

import applicazione.CategoriaFoglia;
import applicazione.PropostaScambio;
import persistenza.LogicaPersistenza;
import util.Utilitaria;
import vista.Vista;

public class GestoreProposte {
	
	/**
	 * PROPOSTE
	 */
	private static final String MSG_ASSENZA_PROPOSTE_PER_PRESTAZIONE = "\nNon è presente nessuna proposta per questa prestazione.\n";
	private static final String FRUITORE_ASSOCIATO = "Fruitore associato: ";
	private static final String MSG_ELENCO_PROPOSTE = "\nElenco proposte in cui è presente -> ";
	private static final String MSG_OPERAZIONE_ANNULLATA = "\nOperazione annullata....\n";
	
	
	
	ArrayList<PropostaScambio> proposte; 
	LogicaPersistenza logica;
	Vista v;
	
	public GestoreProposte(LogicaPersistenza logica, Vista v) {
		this.proposte = logica.getScambi();
		this.logica = logica;
		this.v = v;
	}
	
	
	/**
	 * Metodo per visualizzare le proposte relative ad una specifica categoria foglia
	 */
	public String formattaProposte(GestoreGerarchie gGerarchie) {
		
		ArrayList<CategoriaFoglia> categorieFoglia = logica.getCategorieFoglia();
		ArrayList<PropostaScambio> proposte = logica.getScambi();
		
		int selezionata = gGerarchie.selezionaCategoria(categorieFoglia);
		
		if(selezionata == categorieFoglia.size()) {
			return MSG_OPERAZIONE_ANNULLATA;
		}
			
		CategoriaFoglia f = categorieFoglia.get(selezionata);
		StringBuffer sb = new StringBuffer();
		boolean presenteProposta = false;
		
		for(PropostaScambio p : proposte) {
			boolean presenteRichiesta = p.getNomeRichiesta().equals(f.getNome());
			boolean presenteOfferta = p.getNomeOfferta().equals(f.getNome());
			
			if(presenteRichiesta || presenteOfferta) {
				if(!presenteProposta) {
					sb.append(MSG_ELENCO_PROPOSTE)
						.append(f.getNome().toUpperCase())
						.append(":\n");
					presenteProposta = true;
				}
				
				sb.append("> ")
					.append(p.toString())
					.append("\n")
					.append("\t")
					.append(FRUITORE_ASSOCIATO 
							+ p.getAssociato().getMail() 
							+ "\n");
			}
		}
		if(presenteProposta) {
			return sb.toString();
		} else {
			return MSG_ASSENZA_PROPOSTE_PER_PRESTAZIONE;
		}
		
	}
	
	/**
	 * Metodo per andare a visualizzare l'insiemi chiusi in modo da poter poi contatare i fruitori associati
	 */
	public String formattaInsiemiChiusi() {
		return Utilitaria.formatta(logica.getInsiemi());
	}
	

}
