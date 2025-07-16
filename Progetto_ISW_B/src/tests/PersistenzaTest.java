package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.reflect.TypeToken;

import applicazione.Gerarchia;
import persistenza.GestorePersistenza;

class PersistenzaTest {
	
	private final static String FILE_GERARCHIE = "../Progetto_ISW_B/src/tests/gerarchie.json";
	private ArrayList<Gerarchia> gerarchie = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		AmbienteTest aT = new AmbienteTest();
		gerarchie.add(aT.creaAmbiente());
	}

	@Test
	void testSalvataggioDati() {
		GestorePersistenza gp = new GestorePersistenza();
		gp.getSalvatore().salva(gerarchie, FILE_GERARCHIE);
		
		ArrayList<Gerarchia> gCaricate = gp.getCaricatore().caricaLista(new TypeToken<ArrayList<Gerarchia>>() {}, FILE_GERARCHIE);
		Gerarchia g = gerarchie.get(0);
		
		assertEquals(g.getCatRadice().getNome(), gCaricate.get(0).getCatRadice().getNome());
		assertEquals(g.getNomeComprensorio(), gCaricate.get(0).getNomeComprensorio());
	}
	
	@Test
	void testCaricamentoDati() {
		GestorePersistenza gp = new GestorePersistenza();
		Gerarchia g = gerarchie.get(0);
		
		ArrayList<Gerarchia> gCaricate = gp.getCaricatore().caricaLista(new TypeToken<ArrayList<Gerarchia>>() {}, FILE_GERARCHIE);
		
		assertEquals(g.getCatRadice().getNome(), gCaricate.get(0).getCatRadice().getNome());
		assertEquals(g.getNomeComprensorio(), gCaricate.get(0).getNomeComprensorio());
	}

}
