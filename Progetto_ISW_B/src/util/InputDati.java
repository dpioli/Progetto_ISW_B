package util;

import java.util.*;

import vista.Vista;

/**
 * Classe che si occupa unicamente dell'interazione utente. Permette di ricavare input da usare in classi con logica applicativa.
 */
public class InputDati {
	
	private static Scanner lettore = creaScanner();
	private static Vista v = new Vista();

	private final static String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
	private final static String ERRORE_MINIMO = "Attenzione: e' richiesto un valore maggiore o uguale a ";
	private final static String ERRORE_STRINGA_VUOTA = "Attenzione: non hai inserito alcun carattere";
	private final static String ERRORE_MASSIMO = "Attenzione: e' richiesto un valore minore o uguale a ";
	private final static String MESSAGGIO_AMMISSIBILI = "Attenzione: i caratteri ammissibili sono: ";

	private final static char RISPOSTA_SI = 'S';
	private final static char RISPOSTA_NO = 'N';

	private static Scanner creaScanner() {
		Scanner creato = new Scanner(System.in);
		creato.useDelimiter(System.getProperty("line.separator"));
		return creato;
	}

	public static String leggiStringa(String messaggio) {
		v.mostraMessaggio(messaggio);
		return lettore.next();
	}

	public static String leggiStringaNonVuota(String messaggio) {
		boolean finito = false;
		String lettura = null;
		do {
			lettura = leggiStringa(messaggio);
			lettura = lettura.trim();
			if (lettura.length() > 0)
				finito = true;
			else
				v.mostraErrore(ERRORE_STRINGA_VUOTA);
		} while (!finito);

		return lettura;
	}

	public static char leggiChar(String messaggio) {
		boolean finito = false;
		char valoreLetto = '\0';
		do {
			v.mostraMessaggio(messaggio);
			String lettura = lettore.next();
			if (lettura.length() > 0) {
				valoreLetto = lettura.charAt(0);
				finito = true;
			} else {
				v.mostraErrore(ERRORE_STRINGA_VUOTA);
			}
		} while (!finito);
		return valoreLetto;
	}

	public static char leggiUpperChar(String messaggio, String ammissibili) {
		boolean finito = false;
		char valoreLetto = '\0';
		do {
			valoreLetto = leggiChar(messaggio);
			valoreLetto = Character.toUpperCase(valoreLetto);
			if (ammissibili.indexOf(valoreLetto) != -1)
				finito = true;
			else
				v.mostraMessaggio(MESSAGGIO_AMMISSIBILI + ammissibili);
		} while (!finito);
		return valoreLetto;
	}

	public static int leggiIntero(String messaggio) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			v.mostraMessaggio(messaggio);
			try {
				valoreLetto = lettore.nextInt();
				finito = true;
			} catch (InputMismatchException e) {
				v.mostraErrore(ERRORE_FORMATO);
				String daButtare = lettore.next();
			}
		} while (!finito);
		return valoreLetto;
	}

	public static int leggiInteroPositivo(String messaggio) {
		return leggiInteroConMinimo(messaggio, 1);
	}

	public static int leggiInteroNonNegativo(String messaggio) {
		return leggiInteroConMinimo(messaggio, 0);
	}

	public static int leggiInteroConMinimo(String messaggio, int minimo) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			valoreLetto = leggiIntero(messaggio);
			if (valoreLetto >= minimo)
				finito = true;
			else
				v.mostraErrore(ERRORE_MINIMO + minimo);
		} while (!finito);

		return valoreLetto;
	}
	/**
	 * Metodo richiede all'utente un valore intero tenendo conto di condizioni limite di minimo e di massimo.
	 * Continua a richiedere il valore finché esso non è valido.
	 * @param messaggio visualizzato dall'utente
	 * @param minimo
	 * @param massimo
	 * @return valore letto
	 */
	public static int leggiInteroConMINeMAX(String messaggio, int minimo, int massimo) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			valoreLetto = leggiIntero(messaggio);
			if (valoreLetto < minimo || valoreLetto > massimo) {
				v.mostraErrore(ERRORE_MINIMO + minimo);
				v.mostraErrore( ERRORE_MASSIMO + massimo);
			}
			else finito = true;
		} while (!finito);
		
		return valoreLetto;
	}

	public static int leggiIntero(String messaggio, int minimo, int massimo) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			valoreLetto = leggiIntero(messaggio);
			if (valoreLetto >= minimo && valoreLetto <= massimo)
				finito = true;
			else if (valoreLetto < minimo)
				v.mostraErrore(ERRORE_MINIMO + minimo);
			else
				v.mostraErrore(ERRORE_MASSIMO + massimo);
		} while (!finito);

		return valoreLetto;
	}

	public static double leggiDouble(String messaggio) {
		boolean finito = false;
		double valoreLetto = 0;
		do {
			v.mostraMessaggio(messaggio);
			try {
				valoreLetto = lettore.nextDouble();
				finito = true;
			} catch (InputMismatchException e) {
				v.mostraErrore(ERRORE_FORMATO);
				String daButtare = lettore.next();
			}
		} while (!finito);
		return valoreLetto;
	}

	public static double leggiDoubleConMinimo(String messaggio, double minimo) {
		boolean finito = false;
		double valoreLetto = 0;
		do {
			valoreLetto = leggiDouble(messaggio);
			if (valoreLetto >= minimo)
				finito = true;
			else
				v.mostraErrore(ERRORE_MINIMO + minimo);
		} while (!finito);

		return valoreLetto;
	}
	
	/**
	 * Metodo richiede all'utente un valore intero tenendo conto di condizioni limite di minimo e di massimo.
	 * Continua a richiedere il valore finché esso non è valido.
	 * @param messaggio visualizzato dall'utente
	 * @param minimo
	 * @param massimo
	 * @return valore letto
	 */
	public static double leggiDoubleConMINeMAX(String messaggio, double minimo, double massimo) {
		boolean finito = false;
		double valoreLetto = 0;
		do {
			valoreLetto = leggiDouble(messaggio);
			if (valoreLetto < minimo || valoreLetto > massimo) {
				v.mostraErrore(ERRORE_MINIMO + minimo);
				v.mostraErrore(ERRORE_MASSIMO + massimo);
			}
			else finito = true;
		} while (!finito);
		
		return valoreLetto;
	}

	public static boolean yesOrNo(String messaggio) {
		String mioMessaggio = messaggio + "(" + RISPOSTA_SI + "/" + RISPOSTA_NO + ")" + " ? ";
		char valoreLetto = leggiUpperChar(mioMessaggio, String.valueOf(RISPOSTA_SI) + String.valueOf(RISPOSTA_NO));

		if (valoreLetto == RISPOSTA_SI)
			return true;
		else
			return false;
	}

}
