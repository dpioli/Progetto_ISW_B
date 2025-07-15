package menu;

import util.InputDati;
import vista.Vista;

/**
 * Questa classe rappresenta un menu testuale generico a piu' voci
 * Si suppone che la voce per uscire sia sempre associata alla scelta 0 
 * e sia presentata in fondo al menu
 * @author Diego Pioli 736160
 *
 */
public class Menu {
	
	final private static String CORNICE = "--------------------------------\n";
	final private static String VOCE_USCITA = "0\tEsci dal programma\n";
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
		return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
	}
	
	public String formattaMenu() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(CORNICE);
		sb.append(titolo + "\n");
		sb.append(CORNICE);
		
		for (int i = 0; i < voci.length; i++) {
			sb.append((i + 1) + "\t" + voci[i] + "\n");
		}
	    	
		sb.append(VOCE_USCITA);
		
		return sb.toString();
	}

}
