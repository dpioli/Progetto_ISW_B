package applicazione;

/**
 * Classe astratta che fa da base del pattern GoF COMPOSITE.
 * APPLICATO COMPOSITE: una categoria foglia è il Component dell'albero degenere.

 */
public abstract class CategoriaComponent{

	private static final String OP_NON_SUPPORTATA_FIGLI = "Le foglie non possono avere figli!";
	private static final String AGGIUNTA_NON_SUPPORTATA = "Operazione di aggiunta componente non supportata per foglie";
	private static final String RIMOZIONE_NON_SUPPORTATA = "Operazione non rimozione supportata per foglie";
	
	protected String nome;
	
	public CategoriaComponent(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public boolean eUguale(String nCategoria) {
        return nome.equalsIgnoreCase(nCategoria);
	}
	
	public void aggiungi(CategoriaComponent componente) {
        throw new UnsupportedOperationException(AGGIUNTA_NON_SUPPORTATA);
    }

    public void rimuovi(CategoriaComponent componente) {
        throw new UnsupportedOperationException(RIMOZIONE_NON_SUPPORTATA);
    }
    
    public java.util.List<CategoriaComponent> getFigli() {
        throw new UnsupportedOperationException(OP_NON_SUPPORTATA_FIGLI);
    }
    
    public abstract boolean isFoglia();
	
}
