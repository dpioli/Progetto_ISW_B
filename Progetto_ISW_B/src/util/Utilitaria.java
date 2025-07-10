package util;

import java.util.ArrayList;

public abstract class Utilitaria {
	
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

}
