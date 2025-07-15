package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import applicazione.CampoCaratteristico;
import applicazione.Categoria;
import applicazione.CategoriaFoglia;
import applicazione.Comprensorio;
import applicazione.FatConversione;
import applicazione.Gerarchia;
import applicazione.InsiemeChiuso;
import applicazione.Proposta;
import applicazione.PropostaScambio;
import utenti.Fruitore;
import utenti.IUtente;

public abstract class Utilitaria {
	
	private static final double SOGLIA_BASSA = 0.4;
	private static final double SOGLIA_ALTA = 0.6;
	
	private static final String LEGENDA = "----- LEGENDA -----\n";

	static final Map<Class<?>, Function<Object, String>> FORMATTERS = new HashMap<>();

	static {
		FORMATTERS.put(IUtente.class, o -> formattaUtente((IUtente) o));
		FORMATTERS.put(Comprensorio.class, o -> formattaComprensorio((Comprensorio) o));
	    FORMATTERS.put(CampoCaratteristico.class, o -> formattaCampoCaratteristico((CampoCaratteristico) o));
	    FORMATTERS.put(Gerarchia.class, o -> generaAlberoStringa((Gerarchia) o));
	    FORMATTERS.put(Proposta.class, o -> formattaProposta((Proposta) o));
	    FORMATTERS.put(PropostaScambio.class, o -> formattaScambio((PropostaScambio) o));
	    FORMATTERS.put(InsiemeChiuso.class, o -> formattaInsiemeChiuso((InsiemeChiuso) o));
	}
	
	public static <T> String formattaLista(ArrayList<T> lista) {
		
		if (lista.isEmpty()) return null;

	    StringBuilder sb = new StringBuilder();

	    for (T e : lista) {
	        String formatted;
	        Function<Object, String> formatter = FORMATTERS.get(e.getClass());

	        if (formatter != null) {
	            formatted = formatter.apply(e);
	        } else {
	            formatted = e.toString(); // fallback se non registrato
	        }

	        sb.append(formatted).append("\n");
	    }

	    return sb.toString();
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
	public static String formattaComprensorio(Comprensorio comprensorio) {
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
        costruisciStringaGrafo(gerarchia.getCatRadice(), "", false, builder);
        return builder.toString();
    }
	
	/**
	 * Metodo per generare il grafo della gerarchia 
	 * @param categoria
	 * @param prefisso
	 * @param eUltimo
	 * @param builder
	 */
    private static void costruisciStringaGrafo(Categoria categoria, String prefisso, boolean eUltimo, StringBuilder builder) {
        builder.append(prefisso);
        if (!prefisso.isEmpty()) {
            builder.append(eUltimo ? "└── " : "├── ");
        }
        if(!(categoria.isFoglia())) {
        	builder.append(categoria.getNome())
        			.append(formattaCampoCaratteristico(categoria.getCampCaratt()))
        			.append("\n");
        } else {
        	builder.append(categoria.getNome()).append("\n");
        }
        
        List<Categoria> figli = categoria.getSottoCateg();
        for (int i = 0; i < figli.size(); i++) {
            boolean ultimo = (i == figli.size() - 1);
            String nuovoPrefisso = prefisso + (prefisso.isEmpty() ? " " : (eUltimo ? "    " : "│   "));
            costruisciStringaGrafo(figli.get(i), nuovoPrefisso, ultimo, builder);
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
		return " [prestazione " + p.getTipo().name().toLowerCase() 
				+ ": "+ p.getPrestazione().getNome() 
				+ ", ore: " + p.getQuantitaOre() + "]\n";
	}
    
    public static String formattaScambio(PropostaScambio ps) {
		return String.format("Richiesta: " + ps.getNomeRichiesta() 
					+ " -> " + " Offerta: " + ps.getNomeOfferta() + " | " 
								+ " Stato: " + ps.getStatoFinale().name());
	}
    /**
     * INSIEME CHIUSO
     */
    public static String formattaInsiemeChiuso(InsiemeChiuso ic) {
		ArrayList<Fruitore> fruitoriAssociati = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("\n- id: ");
		sb.append(ic.getId() + "\n\t");
		
		for(PropostaScambio p: ic.getProposte()) {
			sb.append(formattaScambio(p))
				.append("| Proposta da: " + p.getAssociato().getMail() + "\n\t");
			fruitoriAssociati.add(p.getAssociato());
		}
		
		sb.append(formattaFruitori(fruitoriAssociati));
		return sb.toString();
	}


    /**
     * 
     * UTENTI
     * 
     */
    public static String formattaUtente(IUtente u) {
		return String.format("Username = %s", u.getUsername());
	}
    
    public static String formattaFruitori(ArrayList<Fruitore> fruitoriAssociati) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\tFruitori coinvolti: \n\t");
		
		for(Fruitore f: fruitoriAssociati) {
			sb.append("- " + f.getUsername())
				.append(" -> " + f.getMail())
				.append(" | " + f.getNomeComprensorio() + "\n\t");
		}
		return sb.toString();
    }
    
    /**
     * 
     * FATTORI DI CONVERSIONE
     * 
     */
    /**
	 * Metodo di visualizzazione della matrice dei fattori di conversione.
	 */
	public static String formattaFatConv(FatConversione fdc, ArrayList<CategoriaFoglia> foglie) {
		
		if(fdc == null)
			return null;
		
		StringBuffer sb = new StringBuffer();
		sb.append(toStringFat(fdc));
		sb.append(formattaLegenda(foglie));
		
		return sb.toString();
	}
	
	/**
	 * Metodo di visualizzazione legenda relativa alle categorie foglia della matrice dei fattori di conversione.
	 */
	private static String formattaLegenda(ArrayList<CategoriaFoglia> foglie) {
	    StringBuilder sb = new StringBuilder(LEGENDA);
	    for (CategoriaFoglia f : foglie) {
	        sb.append("F")
	          .append(f.getId())
	          .append(" : ")
	          .append(f.getNome())
	          .append("\n");
	    }
	    return sb.toString();
	}

	
	/**
	 * Metodo di formattazione stringa da stampare a video.
	 */
	public static String toStringFat(FatConversione fdc) {
		
		if(fdc.eVuoto()) return null;
		
		StringBuffer sb = new StringBuffer();
		//STAMPO LA PRIMA RIGA CON GLI IDENTIFICATIVI
		for (int i = 0; i < fdc.prendiRiga(0).size(); i++) {
	        sb.append(String.format("%-15s", i == 0 ? "IDENTIFICATIVI" : "F" + i));
	    }
		sb.append("\n");

	    //STAMPO IL RESTO DELLA MATRICE
	    for (int i = 1; i < fdc.dimensione(); i++) {	
	        for (int j = 0; j < fdc.prendiRiga(i).size(); j++) {

	        	String value =  (j == 0 ? "F" + i : String.format("%.2f", fdc.prendiRiga(i).get(j)));
	            sb.append(String.format("%-15s", value));
	        }
	        sb.append("\n");
	    }

	    return sb.toString();
	}
	

}
