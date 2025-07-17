package applicazione;

/**
 * Classe per andare a identificare una classe foglia di una gerarchia.
 * APPLICATO COMPOSITE: una categoria foglia è Leaf dell'albero degenere.
 * @author Irene Romano 736566
 *
 */
public class CategoriaFoglia extends CategoriaComponent{
	
	private int id;
	
	/**
	 * Costruttore oggetto foglia, assegna un numero identificativo passato come parametro
	 * per mantenere consistenza nella persistenza.
	 * @param nome
	 * @param ultimoID, identificativo foglia precedente (salvata nel file .json)
	 */
	public CategoriaFoglia(String nome, Integer ultimoID) {
		super(nome);
		this.id = ++ultimoID;
	}


	public int getId() {
		return id;
	}
	
	@Override
    public boolean isFoglia() {
        return true;
    }
}
