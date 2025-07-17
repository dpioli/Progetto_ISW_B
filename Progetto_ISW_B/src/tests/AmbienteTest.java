package tests;

import java.util.ArrayList;
import java.util.HashMap;

import applicazione.CampoCaratteristico;
import applicazione.CategoriaFoglia;
import applicazione.CategoriaNonFoglia;
import applicazione.Comprensorio;
import applicazione.Gerarchia;
import utenti.Configuratore;

public class AmbienteTest {
	
	public Gerarchia creaAmbiente() {
		ArrayList<String> zone = new ArrayList<>();
		zone.add("zona1");
		zone.add("zona2");
		Comprensorio comprensorio = new Comprensorio("ZonaTest", zone);
		
		Configuratore configuratore = new Configuratore("Admin", "Test");
		
		HashMap<String, String> valoriCampo = new HashMap<>();
		valoriCampo.put("Ramo 1", "Primo ramo della radice");
		valoriCampo.put("Ramo 2", "Secondo ramo della radice");
		
		CampoCaratteristico campoCaratteristico = new CampoCaratteristico("CampoCTest", valoriCampo);
		
		CategoriaNonFoglia radice = new CategoriaNonFoglia("Radice", campoCaratteristico, true, 2);
		
		CategoriaFoglia foglia1 = new CategoriaFoglia("Foglia 1", 0);
		CategoriaFoglia foglia2 = new CategoriaFoglia("Foglia 2", 1);
		
		radice.getFigli().add(foglia1);
		radice.getFigli().add(foglia2);
		
		Gerarchia gerarchia = new Gerarchia(radice, configuratore, comprensorio);
		return gerarchia;
	}

}
