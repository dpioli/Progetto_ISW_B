package vista;


public class Vista implements IVista{
	
	public void visualizza(String sb, String msgErrore) {
		if(sb.equals(null)) {
			mostraErrore(msgErrore);
		} else {
			mostraMessaggio(sb);
		}	
	}
	    
}
