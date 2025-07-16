package menu;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applicazione.Comprensorio;
import controller.GestoreComprensori;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Configuratore;
import utenti.Fruitore;
import util.InputDati;
import vista.Vista;


/**
 * Classe dedita ad autenticare gli utenti prima di poter agire all'interno del programma
 * @author Diego Pioli 736160
 *
 */
public class Autenticazione {

	private LogicaPersistenza logica;
	private Vista v;
	
	private static final String USERNAME_PREDEFINITO = "configuratore";
	private static final String PASSWORD_PREDEFINITA = "password";

	private static final String MSG_ASK_USER_PREDEF = "Inserisci l'username predefinito > ";
	private static final String MSG_ASK_PSW_PREDEF = "Inserisci la password predefinita > ";
	
	private static final String MSG_NEW_USERNAME = "Inserisci un nuovo username > ";
	private static final String MSG_NEW_PASSWORD = "Inserisci una nuova password > ";
	
	private static final String MSG_RICHIESTA_AUTENTICAZIONE = "Inserire le credenziali di autenticazione: ";
	private static final String MSG_NON_VALIDO = "Username non valido, utente già registrato nel sistema. Riprova. ";
	
	///////////////////
	private static final String MSG_ASSENZA_COMPRENSORIO = "Non è presente nessun comprensorio, creane uno prima di continuare.";
	private static final String MSG_SELEZ_COMP = "\nScegli il comprensorio a cui appartieni tra quelli presenti";
	private static final String MSG_SUCC_REGIST = "\n\nRegistrazione avvenuta con successo.\n";
	private static final String MSG_INSERISCI_MAIL = "Inserisci la tua mail > ";
	private static final String FORMATO_MAIL_ERRATO = "\nPer piacere, inserire l'indirizzo email nel formato corretto.";
	private static final String FILTER = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z]";
	
	
	/**
	 * Costruttore della classe Autenticazione 
	 * Riceve come parametro al logica di persistenza per effettuare i controlli
	 * di validita' delle credenziali
	 * @param logica
	 */
	public Autenticazione(LogicaPersistenza logica) {
		this.logica = logica;
		this.v = new Vista();
	}
	

	
	/**
	 * Metodo che permette di accedere alle funzionalita' di un configuratore.
	 * Se rileva a un primo accesso rimanda alla pagina di registrazione.
	 * @return Configuratore
	 */
	public Configuratore accessoConfiguratore() {
	    return LogicaAutenticazione.accessoGenerico(
	        () -> logica.getConfiguratori(),
	        true,
	        this::primoAccessoConfig,
	        v
	    );
	}
	

	/**
	 * Metodo che verifica le credenziali del fruitore
	 * @return Fruitore
	 */
	public Fruitore accessoFruitore() {
	    return LogicaAutenticazione.accessoGenerico(
	        () -> logica.getFruitori(),
	        false,
	        null,
	        v
	    );
	}
	
	
	/***
	 * METODO PER REGISTRARSI PER LA PRIMA VOLTA COME CONFIGUTATORE
	 * 1. Controlla che abbia i permessi.
	 * 2. Inserimento nuove credenziali.
	 * 3. Salvaraggio.
	 */
	public void primoAccessoConfig() {
		GestorePersistenza gp = new GestorePersistenza();
	    controllaPredefinite();

	    LogicaAutenticazione.primoAccessoGenerico(
	        () -> {
	            String username = inserisciUsername();
	            String password = InputDati.leggiStringaNonVuota(MSG_NEW_PASSWORD);
	            return new Configuratore(username, password);
	        },
	        config -> logica.addConfiguratore(config),
	        () -> gp.getSalvatore().salvaConfiguratori(logica.getConfiguratori()),
	        true,
	        MSG_SUCC_REGIST,
	        v
	    );
	}

	
	/**
	 * METODO PER REGISTRARSI PER LA PRIMA VOLTA COME FRUITORE
	 * 1. Controllo presenza di comprensori
	 * 2. Selezione comprensorio di appartenenza
	 * 3. Inserimento username
	 * 4. Inserimento password
	 * 5. Inserimento mail
	 */
	public void primoAccessoFruit(GestoreComprensori gC) {
		GestorePersistenza gp = new GestorePersistenza();
		
	    if (logica.getComprensori().isEmpty()) {
	        v.mostraErrore(MSG_ASSENZA_COMPRENSORIO);
	        return;
	    }

	    v.mostraMessaggio(MSG_SELEZ_COMP);
	    Comprensorio comp = gC.selezionaComprensorio();

	    LogicaAutenticazione.primoAccessoGenerico(
	        () -> {
	            String username = inserisciUsername();
	            String password = InputDati.leggiStringaNonVuota(MSG_NEW_PASSWORD);
	            String email = inserisciEmail();
	            return new Fruitore(comp, username, password, email);
	        },
	        fruitore -> logica.addFruitore(fruitore),
	        () -> gp.getSalvatore().salvaFruitori(logica.getFruitori()),
	        false,
	        MSG_SUCC_REGIST, 
	        v
	    );
	}
	

	/***
	 * Metodo che controlla se l'username inserito è già presente nel sistema.
	 * @param username da controllare
	 * @return true se trova corrispondenza nei configuratori salvati
	 */
	public boolean ePresenteConfiguratore(String username) {
		for(Configuratore c: logica.getConfiguratori()) {
			if(c.getUsername().equals(username)) {
				v.mostraErrore(MSG_NON_VALIDO);
				return true;
			}
		}
		return false;
	}
	
	/***
	 * Metodo che si assicura che l'utente, che cerca di accedere come configuratore, inserisca l'username e la password predefinita 
	 * (controlla quindi che abbia l'autorizzazione per accedere).
	 */
	public void controllaPredefinite() {
		boolean predefinito;
		do {
			v.mostraMessaggio(MSG_RICHIESTA_AUTENTICAZIONE);
			String username = InputDati.leggiStringaNonVuota(MSG_ASK_USER_PREDEF);
			String password = InputDati.leggiStringaNonVuota(MSG_ASK_PSW_PREDEF);
			predefinito = rilevaPrimoAccesso(username, password);
		}while(!predefinito);
	}
	
	/***
	 * Metodo che controlla se username e password inseriti corrispondono a quelli predefiniti.
	 * @param username
	 * @param password
	 * @return true se la corrispondenza è vera
	 */
	public boolean rilevaPrimoAccesso(String username, String password) {
		return (username.equals(USERNAME_PREDEFINITO) && password.equals(PASSWORD_PREDEFINITA)) ? true : false;
	}
	
	/***
	 * Metodo per ricezione stringa di input del nuovo username.
	 * Controlla che la stringa non sia vuota e che non sia già registrata.
	 * @return stringa username
	 */
	public String inserisciUsername() {
		
		String newUsername = "";
		do {
			newUsername = InputDati.leggiStringaNonVuota(MSG_NEW_USERNAME);
			if(ePresenteConfiguratore(newUsername) || ePresenteFruitore(newUsername)) {
				v.mostraErrore(MSG_NON_VALIDO);
			} else 
				return newUsername;
		} while(true);
	}
	
	
	/**
	 * Metodo per inserire la mail del fruitore
	 * @return mail corretta
	 */
	public String inserisciEmail() {
		String email;
		do {
			email = InputDati.leggiStringaNonVuota(MSG_INSERISCI_MAIL);
			if(controllaEmail(email)) 
				return email;
			else
				v.mostraErrore(FORMATO_MAIL_ERRATO);
		} while (true);
	}
	
	/**
	 * Metodo che effettua il controllo sulla formattazione della mail 
	 * @param daControllare
	 * @return true se formato corretto | false se formato incorretto
	 */
	private boolean controllaEmail(String daControllare) {
		if (daControllare == null || daControllare.isEmpty()) {
            return false;
        }
        String range = FILTER;
        Pattern pattern = Pattern.compile(range);
        Matcher matcher = pattern.matcher(daControllare);
        
        return matcher.matches();
	}
	
	/**
	 * Metodo per verificare la presenza di un fruitore nel sistema
	 * @param username
	 * @return true e' presente | false non e' presente
	 */
	public boolean ePresenteFruitore(String username) {
		for(Fruitore f: logica.getFruitori()) {
			if(f.getUsername().equals(username)) {
				v.mostraErrore(MSG_NON_VALIDO);
				return true;
			}
		}
		return false;
	}

	

}
