package util;

import java.util.ArrayList;

import applicazione.Comprensorio;
import vista.IVista;
import vista.Vista;

/**
 * Questa classe rappresenta un menu testuale generico a piu' voci
 * Si suppone che la voce per uscire sia sempre associata alla scelta 0 
 * e sia presentata in fondo al menu
 * @author Diego Pioli 736160
 *
 */
public class Menu {
	
	final private static String CORNICE = "--------------------------------";
	final private static String VOCE_USCITA = "0\tEsci dal programma";
	final private static String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";

	private String titolo;
	private String[] voci;
	private Vista vista;

	public Menu(String titolo, String[] voci) {
		this.titolo = titolo;
		this.voci = voci;
		this.vista = new Vista();
	}
	
	public Vista getVista() {
		return vista;
	}
	
	public void setVista(Vista vista) {
		this.vista = vista;
	}

	public int chiediScelta() {
		vista.mostraMenu(this);
		return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 1, voci.length);
	}
	
	public String formattaMenu() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(CORNICE + "\n");
		sb.append(titolo  + "\n");
		sb.append(CORNICE + "\n");
		
		for (int i = 0; i < voci.length; i++) {
			sb.append((i + 1) + "\t" + voci[i] + "\n");
		}
	    	
		sb.append(VOCE_USCITA);
		sb.append("\n");
		
		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
