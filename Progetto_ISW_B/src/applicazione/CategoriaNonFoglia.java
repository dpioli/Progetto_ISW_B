package applicazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe per andare a identificare una caetgoria intermedia all'interno di una gerarchia.
 * APPLICATO COMPOSITE: una categoria foglia è Composite dell'albero degenere.
 * @author Diego Pioli 736160
 *
 */
public class CategoriaNonFoglia extends CategoriaComponent{
	
	private List<CategoriaComponent> figli = new ArrayList<>();
    private CampoCaratteristico campo;
    private Boolean completo;
    private Integer dominio;

    public CategoriaNonFoglia(String nome, CampoCaratteristico campo, Boolean completo, Integer dominio) {
        super(nome);
        this.campo = campo;
        this.completo = completo;
        this.dominio = dominio;
    }
    
    public CategoriaNonFoglia(String nome, CampoCaratteristico campo, Integer dominio) {
    	super(nome);
    	this.campo = campo;
    	this.dominio = dominio;
    }
    
    /**
     * 
     * GETTERS
     * 
     */
    public CampoCaratteristico getCampo() {
		return campo;
	}
	
	public Boolean getCompleto() {
		return completo;
	}
	
	public Integer getDominio() {
		return dominio;	
	}

	/**
	 * Metodo per ritornare i valori del campo caratteristico
	 * @return coppia di valori valore=descrizione presenti nel campo caratteristico della gerarchia
	 */
	public HashMap<String, String> getValoriCampo(){
		return campo.getValori();
	}
	
	/**
	 * 
	 * METODI DA IMPLEMENTARE
	 * 
	 */

    @Override
    public void aggiungi(CategoriaComponent componente) {
        figli.add(componente);
    }

    @Override
    public void rimuovi(CategoriaComponent componente) {
        figli.remove(componente);
    }

    @Override
    public List<CategoriaComponent> getFigli() {
        return figli;
    }
    
    @Override
    public boolean isFoglia() {
        return false;
    }
    /**
     * 
     * METODI AGGIUNTI
     * 
     */
	/**
	 * Metodo per verificare se e' presente una categoria con quel nome all'interno di una gerarchia
	 * @param nomeCat
	 * @return true o false
	 */

    public boolean contieneNome(String nome) {
        return figli.stream().anyMatch(c -> c.eUguale(nome));
    }

}
