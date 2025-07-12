package util;

import java.util.ArrayList;
import java.util.List;

import applicazione.CampoCaratteristico;
import applicazione.Categoria;
import applicazione.Comprensorio;
import applicazione.Gerarchia;
import applicazione.Proposta;
import applicazione.PropostaScambio;
import utenti.IUtente;

public abstract class Utilitaria {
	
	private static final double SOGLIA_BASSA = 0.4;
	private static final double SOGLIA_ALTA = 0.6;
	
	public static <T> String formatta(ArrayList<T> lista) {
		
		StringBuffer sb = new StringBuffer();
		if(lista.isEmpty()) {
			return null;
		} else {
			for (T e : lista) {
				sb.append(e.toString());
			}
			return sb.toString();
		}	
	}
	
	public static double arrotondaCustom(double valore) {
	    int intero = (int) Math.floor(valore);
	    double decimale = valore - intero;
	    
	    return (decimale < SOGLIA_BASSA) ? intero  // arrotonda per difetto
	            : (decimale < SOGLIA_ALTA) ? intero + 0.5
	            : intero + 1;  // arrotonda per eccesso
	}
	
	/**
	 * 
	 * COMPRENSORIO
	 * 
	 */
	public String formattaComprensorio(Comprensorio comprensorio) {
		return String.format("Comprensorio: %s\nComuni: %s", comprensorio.getNome(), comprensorio.getComuni());
	}
	
	/**
	 * 
	 * GERACHIE E CATEGORIE
	 * 
	 */
	/**
	 * Metodo per ottenere la stringa di visualizzazione della gerarchia
	 * @param gerarchia
	 * @return stringa dell'albero generato
	 */
	public static String generaAlberoStringa(Gerarchia gerarchia) {
        StringBuilder builder = new StringBuilder();
        costruisciStringa(gerarchia.getCatRadice(), "", false, builder);
        return builder.toString();
    }
	
	/**
	 * Metodo per generare il grafo della gerarchia 
	 * @param categoria
	 * @param prefisso
	 * @param èUltimo
	 * @param builder
	 */
    private static void costruisciStringa(Categoria categoria, String prefisso, boolean èUltimo, StringBuilder builder) {
        builder.append(prefisso);
        if (!prefisso.isEmpty()) {
            builder.append(èUltimo ? "└── " : "├── ");
        }
        if(!(categoria.isFoglia())) {
        	builder.append(categoria.getNome()).append(categoria.getValoriCampo().toString()).append("\n");
        } else {
        	builder.append(categoria.getNome()).append("\n");
        }
        
        List<Categoria> figli = categoria.getSottoCateg();
        for (int i = 0; i < figli.size(); i++) {
            boolean ultimo = (i == figli.size() - 1);
            String nuovoPrefisso = prefisso + (prefisso.isEmpty() ? " " : (èUltimo ? "    " : "│   "));
            costruisciStringa(figli.get(i), nuovoPrefisso, ultimo, builder);
        }
    }
    
    public static String formattaCampoCaratteristico(CampoCaratteristico cc) {
		return String.format("%s --> %s", cc.getNomeCampo(), cc.getValori().keySet());
	}
    
    /**
     * 
     * PROPOSTA
     * 
     */
    public static String formattaProposta(Proposta p) {
		return " [prestazione " + p.getTipo().name().toLowerCase() + ": "+ p.getPrestazione().getNome() + ", ore: " + p.getQuantitaOre() + "]";
	}
    
    public static String formattaProposteScambio(PropostaScambio ps) {
		return String.format("Richiesta: " + ps.getNomeRichiesta() + " -> " + " Offerta: " + ps.getNomeOfferta() + " | " + " Stato: " + ps.getStatoFinale().toString());
	}

    /**
     * 
     * UTENTI
     * 
     */
    public String formattaUtente(IUtente u) {
		return String.format("Username = %s", u.getUsername());
	}

}
