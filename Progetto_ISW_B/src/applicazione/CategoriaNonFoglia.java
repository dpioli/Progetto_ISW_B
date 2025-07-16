package applicazione;

/**
 * Classe per andare a identificare una caetgoria intermedia all'interno di una gerarchia
 * @author Diego Pioli 736160
 *
 */
public class CategoriaNonFoglia extends Categoria{
	
	public CategoriaNonFoglia(String nome, CampoCaratteristico campCaratt, Boolean completo, Integer dominio) {
		super(nome, campCaratt, completo, dominio, false);
	}
	
	public CategoriaNonFoglia(String nome, CampoCaratteristico campCaratt, Integer dominio) {
		super(nome, campCaratt, dominio, false);
	}
}

/**
 * // CategoriaComponent.java
public abstract class CategoriaComponent {
    protected String nome;

    public CategoriaComponent(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract boolean eUguale(String nomeCat);

    public void aggiungi(CategoriaComponent componente) {
        throw new UnsupportedOperationException();
    }

    public void rimuovi(CategoriaComponent componente) {
        throw new UnsupportedOperationException();
    }

    public java.util.List<CategoriaComponent> getFigli() {
        throw new UnsupportedOperationException();
    }

    public abstract void stampaStruttura(int livello);
}
//////////////////////
 * // CategoriaFoglia.java
public class CategoriaFoglia extends CategoriaComponent {
    private int id;

    public CategoriaFoglia(String nome, int ultimoId) {
        super(nome);
        this.id = ultimoId + 1;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean eUguale(String nomeCat) {
        return nome.equalsIgnoreCase(nomeCat);
    }

    @Override
    public void stampaStruttura(int livello) {
        System.out.println("  ".repeat(livello) + "- " + nome + " (Foglia ID: " + id + ")");
    }
}

//////////////////////////////////
 * // CategoriaComposita.java
import java.util.ArrayList;
import java.util.List;

public class CategoriaComposita extends CategoriaComponent {
    private List<CategoriaComponent> figli = new ArrayList<>();
    private CampoCaratteristico campo;
    private Boolean completo;
    private Integer dominio;

    public CategoriaComposita(String nome, CampoCaratteristico campo, Boolean completo, Integer dominio) {
        super(nome);
        this.campo = campo;
        this.completo = completo;
        this.dominio = dominio;
    }

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
    public boolean eUguale(String nomeCat) {
        return nome.equalsIgnoreCase(nomeCat);
    }

    @Override
    public void stampaStruttura(int livello) {
        System.out.println("  ".repeat(livello) + "+ " + nome);
        for (CategoriaComponent c : figli) {
            c.stampaStruttura(livello + 1);
        }
    }
}


 * */
