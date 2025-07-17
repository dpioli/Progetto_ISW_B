package applicazione;

import utenti.Configuratore;

/**
 * Classe per identificare le proprieta' di una gerarchia.
 * dopo APPLICAZIONE COMPOSITE: la radice è una categoria componente da cui si dirama la gerarchia.
 * @author Diego Pioli 736160
 *
 */
public class Gerarchia {
	
	private CategoriaComponent catRadice;
	private Configuratore proprietario;
	private Comprensorio comprensorio;
	
	/**
	 * Costruttore della classe gerarchia
	 * @param catRadice
	 * @param prorpietario
	 * @param compr 
	 */
	public Gerarchia (CategoriaComponent catRadice, Configuratore prorpietario, Comprensorio comprensorio) {
		this.catRadice = catRadice;
		this.proprietario = prorpietario;
		this.comprensorio = comprensorio;
	}

	public CategoriaComponent getCatRadice() {
		return catRadice;
	}

	public Configuratore getProprietario() {
		return proprietario;
	}
	
	public Comprensorio getComprensorio() {
		return comprensorio;
	}
	
	public String getNomeComprensorio() {
		return comprensorio.getNome();
	}
	
	public String getNomeRadice() {
		return catRadice.getNome();
	}
	
	/**
	 * Metodo per verificare se una gerarchia ha lo stesso nome di un'altra
	 * @param nomeGerarchia
	 * @return true se è già presente una gerarchia con quel nome
	 */
	public boolean eNomeUguale(String nomeGerarchia) {
		return catRadice.eUguale(nomeGerarchia);
	}
	
}