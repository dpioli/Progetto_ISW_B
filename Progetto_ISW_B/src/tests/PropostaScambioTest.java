package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import applicazione.CategoriaFoglia;
import applicazione.Comprensorio;
import applicazione.FatConversione;
import applicazione.InsiemeChiuso;
import applicazione.Proposta;
import applicazione.PropostaScambio;
import applicazione.StatoProposta;
import applicazione.TipoProposta;
import controller.GestoreProposte;
import menu.MenuFruitore;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Fruitore;
import util.Utilitaria;

class PropostaScambioTest {
	
	private FatConversione fdc = new FatConversione();

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	void testFDC() {
		CategoriaFoglia c1 = new CategoriaFoglia("CF1", 0);
		CategoriaFoglia c2 = new CategoriaFoglia("CF2", 1);
		
		fdc.agganciaTest(c1.getId(), 0.0);
		fdc.agganciaTest(c2.getId(), 1.5);
		
		assertEquals(fdc.prendiRiga(2).get(1), 1/fdc.prendiRiga(1).get(2));
		assertEquals(fdc.prendiRiga(1).get(2), 1/fdc.prendiRiga(2).get(1));
	}

	@Test
	void testOreOfferte() {
		GestorePersistenza gP = new GestorePersistenza();
		LogicaPersistenza logica = new LogicaPersistenza();
		CategoriaFoglia c1 = new CategoriaFoglia("CF1", 0);
		CategoriaFoglia c2 = new CategoriaFoglia("CF2", 1);
		
		fdc.agganciaTest(c1.getId(), 1.0);
		fdc.agganciaTest(c2.getId(), 1.5);
		
		GestoreProposte gp = new GestoreProposte(logica);
		double oreOfferte = gp.calcolaOfferta(0, 1, 10.0, fdc);
		
		assertEquals(Utilitaria.arrotondaCustom(10/1.5), oreOfferte);
	}
	
	@Test
	void soddisfacimentoProposte() {
		GestorePersistenza gP = new GestorePersistenza();
		LogicaPersistenza logica = new LogicaPersistenza();
    	FatConversione fdc = new FatConversione();
    	GestoreProposte gp = new GestoreProposte(logica);
    	ArrayList<String> zoneC = new ArrayList<>();
    	zoneC.add("z1");
    	zoneC.add("z2");
    	Comprensorio comprensorio = new Comprensorio("TestComprensorio", zoneC);
    	CategoriaFoglia c1 = new CategoriaFoglia("c1", 0);
    	CategoriaFoglia c2 = new CategoriaFoglia ("c2", 1);
    	fdc.agganciaTest(c1.getId(), 0.0);
    	fdc.agganciaTest(c2.getId(), 1.5);
    	
    	Fruitore f1 = new Fruitore(comprensorio, "F1", "Test", "f1@test.com");
    	Fruitore f2 = new Fruitore(comprensorio, "F2", "Test2", "f2@test.com");
    	
    	Proposta r1 = new Proposta(c1, TipoProposta.RICHIESTA, 10.0);
    	double oF1 = gp.calcolaOfferta(0, 1, 10.0, fdc);
    	Proposta o1 = new Proposta(c2, TipoProposta.OFFERTA, oF1);
    	
    	Proposta r2 = new Proposta(c2, TipoProposta.RICHIESTA, 7);
    	double oF2 = gp.calcolaOfferta(1, 0, 6.5, fdc);
    	Proposta o2 = new Proposta(c1, TipoProposta.OFFERTA, oF2);
    	
    	PropostaScambio ps1 = new PropostaScambio(r1, o1, 1);
    	ps1.setFruitoreAssociato(f1);
    	PropostaScambio ps2 = new PropostaScambio(r2, o2, 2);
    	ps2.setFruitoreAssociato(f2);
    	
    	ArrayList<PropostaScambio> proposte = new ArrayList<>();
    	proposte.add(ps1);
    	proposte.add(ps2);
    	
    	InsiemeChiuso ic = gp.verificaSoddisfacimentoTest(ps1, proposte);
    	
    	assertEquals(StatoProposta.CHIUSA, ic.getProposte().get(0).getStatoFinale());
	}

}
