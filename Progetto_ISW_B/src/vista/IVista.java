package vista;

import menu.Menu;

public interface IVista {
	
	public default void mostraMenu(Menu menu) {
		System.out.println(menu.formattaMenu());
	}
	    
    public default void mostraMessaggio(String msg) {
        System.out.println(msg);
    }

    public default void mostraErrore(String msg) {
        System.err.println("Errore: " + msg);
    }




	

}
