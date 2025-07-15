package menu;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import utenti.IUtente;
import util.InputDati;
import vista.Vista;

/**
 *  Classe a supporto di Autenticazione. UTILIY CONTROLLER
 *  Le funzioni di LogicaAuten cazione sarebbero responsabilità di Autenticazione.
 */
public class LogicaAutenticazione {
	
	private static final String USERNAME_PREDEFINITO = "configuratore";
	
	private static final String MSG_ASK_USERNAME = "Inserisci l'username (ESC per uscire) > ";
	private static final String MSG_ASK_PASSWORD = "Inserisci la password (ESC per uscire) > ";
	
	private static final String UTENTE_NON_PRESENTE = "Questo utente non è presente nel sistema. \nTornando indietro...";
	private static final String PSW_ERRATA = "Password errata. (ESC per uscire) ";

	private static final String MSG_RILEVA_PRIMO_ACCESSO = "\nSembra che tu non sia ancora registrato!\nREGISTRATI >";
	private static final String MSG_ACCESSO_RIUSCITO = "\nAccesso effettuato con successo -- ";	
	
	private static final String RILEVATA_RICHIESTA_DI_USCITA = "Rilevata richiesta di uscita.";
	private static final String ESC = "ESC";
	
	private static final int CASE_PRIMO_ACCESSO = 1;
	private static final int CASE_ACCESSO = 2;
	private static final int CASE_P_INIZIALE = 3;
	
	private static final String MSG_AUTENT = "\tAUTENTICAZIONE";
	private static final String MSG_PRIMO_ACCESSO = "Registrati";
	private static final String MSG_CONFIG_REGISTRATO= "Accedi";
	private final static String MSG_P_PRECEDENTE = "Ritorna alla pagina iniziale";
	
	private static final String MSG_NEW_CREDENZIALI = "Inserisci le nuove credenziali di seguito\n\n";
	
	private static final int CASE_USCITA = 0;
	
	public static String[] vociAutenticazione = {MSG_PRIMO_ACCESSO, MSG_CONFIG_REGISTRATO, MSG_P_PRECEDENTE};
	
	
	
	public static <T extends IUtente> void gestioneAutenticazione(
	        PrimoAccesso primoAccesso,
	        AccessoUtente<T> accesso,
	        AvvioMenu<T> avvioMenu
	) {
	    Menu menuAccesso = new Menu(MSG_AUTENT, vociAutenticazione);
	    int scelta;

	    do {
	        scelta = menuAccesso.chiediScelta();
	        switch (scelta) {
	            case CASE_PRIMO_ACCESSO -> primoAccesso.esegui();
	            case CASE_ACCESSO -> {
	                T utente = accesso.eseguiAccesso();
	                if (utente != null) {
	                    avvioMenu.avviaMenu(utente);
	                } else {
	                    scelta = CASE_P_INIZIALE;
	                }
	            }
	            case CASE_P_INIZIALE -> {}
	            default -> System.exit(CASE_USCITA);
	        }
	    } while (scelta != CASE_P_INIZIALE);
	}
	
	
	public static void gestioneMenuUtente(Menu menu, int casoIndietro, AzioneMenu azione) {
	    int scelta;
	    do {
	        scelta = menu.chiediScelta();
	        if (scelta == casoIndietro) break;
	        azione.esegui(scelta);
	    } while (true);
	}
	
	
	
	public static <T extends IUtente> void primoAccessoGenerico(
	    CreatoreUtente<T> creaUtente,
	    Consumer<T> aggiungiUtente,
	    Runnable salvataggio,
	    boolean mostraCredenziali,
	    String messaggioFinale,
	    Vista v
	) {
	    if (mostraCredenziali) {
	        v.mostraMessaggio(MSG_NEW_CREDENZIALI);
	    }

	    T utente = creaUtente.crea();
	    if (utente == null) return;

	    aggiungiUtente.accept(utente);
	    salvataggio.run();
	    v.mostraMessaggio(messaggioFinale);
	}

	
	public static <T extends IUtente> T accessoGenerico(
	    Supplier<List<T>> fornitoreLista,
	    boolean abilitaPrimoAccesso,
	    Runnable azionePrimoAccesso,
	    Vista v
	) {
	    String username = InputDati.leggiStringaNonVuota(MSG_ASK_USERNAME);

	    if (richiedeUscita(username)) {
	        v.mostraMessaggio(RILEVATA_RICHIESTA_DI_USCITA);
	        return null;
	    }

	    if (abilitaPrimoAccesso && USERNAME_PREDEFINITO.equals(username)) {
	        v.mostraMessaggio(MSG_RILEVA_PRIMO_ACCESSO);
	        if (azionePrimoAccesso != null) {
	            azionePrimoAccesso.run();
	        }
	    }

	    return recuperaUtente(username, fornitoreLista.get(), v);
	}

	
	public static <T extends IUtente> T recuperaUtente(String username, List<T> lista, Vista v) {
		String password;
		for(T u: lista) {
			if(u.getUsername().equals(username)) {
				do{
					password = InputDati.leggiStringaNonVuota(MSG_ASK_PASSWORD);
					if(u.getPassword().equals(password)) {
						v.mostraMessaggio(MSG_ACCESSO_RIUSCITO + username +"\n");
						return u;
					} else {
						v.mostraErrore(PSW_ERRATA);
					}
				}while(!richiedeUscita(password));
			}
		}
		v.mostraErrore(UTENTE_NON_PRESENTE);
		return null;
	}

	
	/**
	 * Metodo che verifica se l'input corrisponde alla parola chiave di uscita.
	 * @param input
	 * @return true se corrispondono
	 */
	public static boolean richiedeUscita(String in) {
		return in.equalsIgnoreCase(ESC) ? true : false;
	}
	
	
	
	@FunctionalInterface
	public interface PrimoAccesso {
	    void esegui();
	}

	@FunctionalInterface
	public interface AccessoUtente<T extends IUtente> {
	    T eseguiAccesso();
	}

	@FunctionalInterface
	public interface CreatoreUtente<T> {
	    T crea();
	}
	
	@FunctionalInterface
	public interface AvvioMenu<T extends IUtente> {
	    void avviaMenu(T utente);
	}
	
	@FunctionalInterface
	public interface AzioneMenu {
	    void esegui(int scelta);
	}

}
