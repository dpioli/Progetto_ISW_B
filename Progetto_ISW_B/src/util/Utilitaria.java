package util;

import java.util.ArrayList;

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

}
