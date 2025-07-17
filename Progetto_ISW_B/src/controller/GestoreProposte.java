package controller;

import java.util.ArrayList;

import applicazione.CategoriaFoglia;
import applicazione.FatConversione;
import applicazione.InsiemeChiuso;
import applicazione.Proposta;
import applicazione.PropostaScambio;
import applicazione.StatoProposta;
import applicazione.TipoProposta;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Fruitore;
import util.InputDati;
import util.Utilitaria;
import vista.Vista;
import vista.VistaFruitore;

/**
 * Controller GRASP che contiene la logica applicativa riguardante gli oggetti Proposta e PropostaScambio.
 */
public class GestoreProposte {
	
	//Configuratore
	private static final String MSG_OPERAZIONE_ANNULLATA = "\nOperazione annullata....\n";
	
	//Fruitore
	private static final String MSG_MENU_PRINCIPALE = "Ritorno al menu principale.\n";
	
	private static final String MSG_SEL_PRESTAZIONE = "Seleziona la prestazione di interesse: ";
	private static final String MSG_INS_ORE = "Inserisci il numero di ore di questa prestazione che ti interessano:";
	private static final String MSG_SEL_OFFERTA = "Quale prestazione offri in cambio?";
	private static final String MSG_CONFERMA = "Confermi la seguente proposta di scambio?\n";
	private static final String MSG_Y_N = "\nVuoi confermare ";
	private static final String MSG_ANNULLATO_SCAMBIO = "Hai annullato la proposta di scambio...";
	private static final String MSG_PRESTAZIONE_NON_VALIDA = "Non puoi offrire la stessa prestazione che richiedi!";
	
	private static final String MSG_PROPOSTA_RITIRATA = "\nLa proposta è stata ritirata.\n";
	private static final String MSG_SELEZIONA_PROPOSTA_DA_RITIRATE = "Seleziona la proposta che vuoi ritirare (annulla altrimenti) > ";
	private static final String MSG_PROPOSTE_RITIRABILI = "Proposte ritirabili: ";
	private static final String MSG_CONFERMA_RITIRO_PROPOSTA = "\nSei sicuro di ritirare questa proposta ";
	private static final String MSG_ASSENZA_PROPOSTE_RITIRATE = "\nNon hai proposte da ritirare.\n";
	
	private static final String MSG_PROPOSTA_SODDISFATTA = "\nLa tua proposta è stata soddisfatta verrai contatato a breve con tutte le informazioni!!\n";
	private static final String MSG_PROPOSTA_NON_SODDISFATTA = "\nAl momemnto non ci sono proposte che soddisfano la tua proposta.\n"
			+ "Sarai contattato appena verra' soddisfatta !!";
	
	
	LogicaPersistenza logica;
	ArrayList<PropostaScambio> proposte; 
	Vista v;
	
	public GestoreProposte(LogicaPersistenza logica, Vista v) {
		this.logica = logica;
		this.proposte = logica.getScambi();
		this.v = v;
	}
	//PER TEST
	public GestoreProposte(LogicaPersistenza logica) {
		this.logica = logica;
	}
	
	/**
	 * Metodo per la formulazione di una richiesta di scambi.
	 * 1. inserice ore (durata della prestazione d'opera desiderata)
	 * 2. creo oggetto Proposta di tipo richiesta
	 * 3. seleziona quale prestazione offre in cambio
	 * 4. formulo l'offerta
	 * 5. chiedo conferma degli oggetti
	 * 6. se confermato salvo lo scambio (aperta), salvando anche il fruitore
	 */
	public void richiediPrestazioni(Fruitore fruit, ArrayList<CategoriaFoglia> foglie) {
		
		//RICHIESTA
		int scelta = InputDati.leggiInteroConMINeMAX(MSG_SEL_PRESTAZIONE, 0, foglie.size()- 1);
		double ore = InputDati.leggiDoubleConMinimo(MSG_INS_ORE, 0);
		Proposta richiesta = new Proposta(foglie.get(scelta), TipoProposta.RICHIESTA, ore);

		//OFFERTA
		int incambio = 0;
		do {
			incambio = InputDati.leggiInteroConMINeMAX(MSG_SEL_OFFERTA, 0, foglie.size()- 1);
			if(incambio == scelta) {
				v.mostraErrore(MSG_PRESTAZIONE_NON_VALIDA);
			}
		} while(incambio == scelta);
		
		ArrayList<Double> fattori = logica.getFatConversione().prendiRiga(scelta + 1); 
		//prendendo tutti i fdc dalla tabella uscenti da id della prestazione richiesta
	    double valore = (fattori.get(incambio + 1) * ore);
	    
	    double arrotondato = Utilitaria.arrotondaCustom(valore);
		Proposta offerta = new Proposta(foglie.get(incambio), TipoProposta.OFFERTA, arrotondato);
		
		int id = recuperaId();
		
		//SCAMBIO
		PropostaScambio scambio = new PropostaScambio(richiesta, offerta, id);
		boolean sn = InputDati.yesOrNo(MSG_CONFERMA + Utilitaria.formattaScambio(scambio) + MSG_Y_N);
		
		if(sn) { //aggiunto alle proposte aperte
			scambio.setFruitoreAssociato(fruit);
			logica.addScambio(scambio);
			GestorePersistenza gp = new GestorePersistenza();
			gp.getSalvatore().salvaScambi(logica.getScambi());
			// verifica che lo scambio venga soddisfatto da delle proposte preesistenti
			verificaSoddisfacimento(scambio, gp);
		} else {
			v.mostraErrore(MSG_ANNULLATO_SCAMBIO + MSG_MENU_PRINCIPALE);
			return;
		}
	}
	
	//PER TEST
	public Double calcolaOfferta(Integer catScelta, Integer catOfferta, Double oreRichieste, FatConversione fdc) {
		ArrayList<Double> fattori = fdc.prendiRiga(catScelta + 1);
		double valore = (fattori.get(catOfferta + 1) * oreRichieste);
		double arrotondato = Utilitaria.arrotondaCustom(valore);
		return arrotondato;
	}
	
	/**
	 * Metodo per verificare se una porposta inserita può essere soddisfatta da una singola proposta o da una catena
	 * 1. Recupero le proposte valide (proposte presenti nel mio stesso comprensorio e non formulate dalla persona stessa)
	 * 2. Verifico se questa lista è vuota => non ci sono proposte compatibili quindi rimarra' pendente.
	 * 3. Controllo verticale per la ricerca di un accoppiamento perfetto con un'altra proposta -> coppiaPerfetta(PropostaScambio p1, PropostaScambio p2)
	 * 4. In caso non ci sia una coppia perfetta si va alla ricerca di una catena
	 * 5. Creo l'array della catena in cui inserisco la proposta appena generata dal fruitore
	 * 6. inizio la ricerca con cercaCatena(ArrayList<PropostaScambio> catena, ArrayList<PropostaScambio> proposteValide)
	 * 7. In entrambi i casi quando si verifica un acatena o un accoppiamento  perfetto si crea un nuovo InsiemeChiuso
	 * 		viene aggiornato lo stato di ogni proposta presente in CHIUSA e aggiunta all'insieme. Infine vado a salvare 
	 * 		sia l'insieme che le proposte in modo persistente.
	 * @param proposta
	 */
	private void verificaSoddisfacimento(PropostaScambio proposta, GestorePersistenza gp) {
		
		ArrayList<PropostaScambio> proposteValide = selezionaProposteValide(logica.getScambi(), proposta);
		if(proposteValide.isEmpty()) {
			v.mostraErrore(MSG_PROPOSTA_NON_SODDISFATTA);
			return;
		}
		
		for(PropostaScambio p: proposteValide) {
			if(coppiaPerfetta(proposta, p)) {
				InsiemeChiuso ins = new InsiemeChiuso(recuperaIdInsiemeChiuso());
				aggiornaStatoAChiusa(proposta, logica.getScambi());
				aggiornaStatoAChiusa(p, logica.getScambi());
				
				ins.aggiungiProposteAInsiemeChiuso(proposta);
				ins.aggiungiProposteAInsiemeChiuso(p);
				
				logica.aggiungiInsieme(ins);
				gp.getSalvatore().salvaInsiemiChiusi(logica.getInsiemi());
				gp.getSalvatore().salvaScambi(logica.getScambi());
				
				v.mostraMessaggio(MSG_PROPOSTA_SODDISFATTA);
				return;
			}
		}
		
		ArrayList<PropostaScambio> catena = new ArrayList<PropostaScambio>();
		catena.add(proposta);
		
		if(cercaCatena(catena, proposteValide)) {
			InsiemeChiuso insC = new InsiemeChiuso(recuperaIdInsiemeChiuso());
			
			for(PropostaScambio p: catena) {
				aggiornaStatoAChiusa(p, logica.getScambi());
				insC.aggiungiProposteAInsiemeChiuso(p);
			}
			logica.aggiungiInsieme(insC);
			gp.getSalvatore().salvaInsiemiChiusi(logica.getInsiemi());
			gp.getSalvatore().salvaScambi(logica.getScambi());
			
			v.mostraMessaggio(MSG_PROPOSTA_SODDISFATTA);
			return;
		} else {
			v.mostraMessaggio(MSG_PROPOSTA_NON_SODDISFATTA);
			return;
		}
	}
	
	private boolean cercaCatena(ArrayList<PropostaScambio> catena, ArrayList<PropostaScambio> proposteValide) {
		ArrayList<PropostaScambio> propostePendenti = new ArrayList<>(proposteValide);
		ArrayList<PropostaScambio> nuoveProposteValide = new ArrayList<>(proposteValide);
		for(PropostaScambio p: propostePendenti) {
			if(verificaRichiestaOfferta(catena.get(catena.size() - 1), p)) {
				catena.add(p);
				if(verificaOffertaRichiesta(catena.get(0), p)) {
					return true;
				}
				nuoveProposteValide.remove(p);
				if(cercaCatena(catena, nuoveProposteValide)) {
					return true;
				}
				
				catena.remove(catena.size() - 1);
			}
			continue;
		}
		return false;
	}
	
	/**
	 * Metodo che permette di controllare se una coppia di proposte si soddisfa avvicevolmente
	 * @param p1
	 * @param p2
	 * @return true se due proposte si soddisfano avvicevolmente, false altrimenti
	 */
	private boolean coppiaPerfetta(PropostaScambio p1, PropostaScambio p2) {
		if(verificaSoddisfacimentoNome(p1, p2) && verificaSoddisfacimentoOre(p1, p2))
			return true;
		return false;
	}
	
	/**
	 * Metodo per selezionare le possibili proposte valide per soddisfare al proposta appena creata
	 * @param proposte
	 * @param proposta
	 * @return ArrayList<PropostaScambio> proposte che possono soddisfare una proposta inserita
	 */
	private ArrayList<PropostaScambio> selezionaProposteValide(ArrayList<PropostaScambio> proposte, PropostaScambio proposta){
		ArrayList<PropostaScambio> proposteV = new ArrayList<PropostaScambio>();
		
		for(PropostaScambio p: proposte) {
			if(controlloStato(p) && verificaFruitore(proposta, p)) {
				proposteV.add(p);
			}
		}
		return proposteV;
	}
	
	/**
	 * Metodo per verificare che i due fruitori siano diversi
	 * @param p1
	 * @param p2
	 * @return true se i fruitori sono diversi e se il cmprensorio è lo stesso, false altrimenti
	 */
	private boolean verificaFruitore(PropostaScambio p1, PropostaScambio p2) {
		boolean stessoNome = p1.getNomeAssociato().equals(p2.getNomeAssociato());
		boolean stessoComprensorio = p1.getComprensorio().equals(p2.getComprensorio());
		
		return stessoNome ? false
				: stessoComprensorio ? true
				: false;
	}
	
	/**
	 * Metodo per verificare che la prestazione richiesta combacia con la prestazione offerta e viceversa
	 * @param p1
	 * @param p2
	 * @return true veirfica soddisfatta / false verifica non soddisfatta
	 */
	private boolean verificaSoddisfacimentoNome(PropostaScambio p1, PropostaScambio p2) {
		boolean soddisfRO= p1.getNomeRichiesta().equals(p2.getNomeOfferta());
		boolean soddisfOR = p1.getNomeOfferta().equals(p2.getNomeRichiesta());
		
		return (soddisfRO && soddisfOR) ? true
				: false;
	}
	
	private boolean verificaRichiestaOfferta(PropostaScambio p1, PropostaScambio p2) {
		double errore = 0.5;
		boolean nomeRichiestaOfferta =  p1.getNomeRichiesta().equals(p2.getNomeOfferta());
		boolean oreRichiestaOfferta = Math.abs(p1.getOreRichiesta() - p2.getOreOfferta()) < errore;
		
		return (nomeRichiestaOfferta && oreRichiestaOfferta) ? true
				: false;
	}
	
	private boolean verificaOffertaRichiesta(PropostaScambio p1, PropostaScambio p2) {
		double errore = 0.5;
		boolean nomeOffertaRichiesta = p1.getNomeOfferta().equals(p2.getNomeRichiesta());
		boolean oreOffertaRichiesta = Math.abs(p1.getOreOfferta() - p2.getOreRichiesta()) < errore;
		
		return (nomeOffertaRichiesta && oreOffertaRichiesta) ? true
				: false;
	}
	
	/**
	 * Metodo per verificare il soddisfacimento delle ore tra richiesta e offerta e viceversa
	 * @param p1
	 * @param p2
	 * @return true veirfica soddisfatta / false verifica non soddisfatta
	 */
	private boolean verificaSoddisfacimentoOre(PropostaScambio p1, PropostaScambio p2) {
		double errore = 0.5;
		boolean oreRO = Math.abs(p1.getOreRichiesta() - p2.getOreOfferta()) < errore;
		boolean oreOR = Math.abs(p1.getOreOfferta() - p2.getOreRichiesta()) < errore;
		
		return (oreRO && oreOR) ? true 
				: false;
	}
	
	/**
	 * Metodo per verificare se lo stato di una proposta è APERTA
	 * @param proposta
	 * @return true se lo stato della proposta è aperto /false se lo stato della proposta è chiuso 
	 */
	private boolean controlloStato(PropostaScambio proposta) {
		boolean chiusa = proposta.getStatoFinale() != StatoProposta.CHIUSA;
		boolean ritirata = proposta.getStatoFinale() != StatoProposta.RITIRATA;
		
		return (chiusa && ritirata) ? true
				: false;
	}
	
	/**
	 * Metodo per poter ritirare le proposte pendenti
	 */
	public void ritiraProposte(Fruitore fruit) {
		ArrayList<PropostaScambio> proposte = logica.getScambi();
		ArrayList<PropostaScambio> proposteFruit = new ArrayList<>();
		
		for(PropostaScambio p: proposte) {
			boolean corrisponde = fruit.getUsername().equals(p.getNomeAssociato());
			if(corrisponde && (p.getStatoFinale() == StatoProposta.APERTA)) {
				proposteFruit.add(p);
			}
		}
		
		if(proposteFruit.isEmpty()) {
			v.mostraErrore(MSG_ASSENZA_PROPOSTE_RITIRATE);
			return;
		}
		
		int selezionata = selezionaPropostaRitirabile(proposteFruit);
		
		if(selezionata == proposteFruit.size()) {
			v.mostraErrore(MSG_OPERAZIONE_ANNULLATA);
			return;
		}
		
		PropostaScambio p = proposteFruit.get(selezionata);
		boolean conferma = InputDati.yesOrNo(MSG_CONFERMA_RITIRO_PROPOSTA);
		if(conferma) {
			aggiornaStatoARitirata(p, proposte);
			GestorePersistenza gp = new GestorePersistenza();
			gp.getSalvatore().salvaScambi(proposte);
			v.mostraMessaggio(MSG_PROPOSTA_RITIRATA);
		} else {
			v.mostraMessaggio(MSG_OPERAZIONE_ANNULLATA);
		}
	}
	
	/**
	 * Metodo per aggiornare lo stato di una proposta che vuole essere ritirata
	 * @param proposta
	 * @param proposte
	 */
	private void aggiornaStatoARitirata(PropostaScambio proposta, ArrayList<PropostaScambio> proposte) {
		for(PropostaScambio p : proposte) {
			if(p.getId() == proposta.getId()) {
				p.setStatoFinale(StatoProposta.RITIRATA);
				v.mostraMessaggio( Utilitaria.formattaScambio(p) );
			}
		}
	}
	
	/**
	 *Metodo per aggiornare lo stato di una proposta a chiusa dopo essere stata soddisfatta
	 * @param proposta
	 * @param proposte
	 */
	private void aggiornaStatoAChiusa(PropostaScambio proposta, ArrayList<PropostaScambio> proposte) {
		for(PropostaScambio p: proposte) {
			if(p.getId() == proposta.getId()) {
				p.setStatoFinale(StatoProposta.CHIUSA);
			}
		}
	}
	
	/**
	 * Metodo per selezionare la proposta da ritirare tra quelle disponibili
	 * @param proposte
	 * @return
	 */
	private int selezionaPropostaRitirabile(ArrayList<PropostaScambio> proposte) {
		v.mostraMessaggio(MSG_PROPOSTE_RITIRABILI);
		v.mostraMessaggio(Utilitaria.formattaProposte(proposte));
		
		int propostaSelezionata = InputDati.leggiInteroConMINeMAX(MSG_SELEZIONA_PROPOSTA_DA_RITIRATE, 0, proposte.size());
		return propostaSelezionata;
	}
	/**
	 * Metodi che recupera l'identificativo,
	 * permette di calcolare quello della successiva per mantenere la persistenza.
	 * @return id 
	 */
	
	private int recuperaId() {
		ArrayList<PropostaScambio> scambi = logica.getScambi();
		if(scambi.isEmpty())
			return 0;
		int ultimo = scambi.size() - 1;
		PropostaScambio p = scambi.get(ultimo);
		return p.getId();
	}
	
	private int recuperaIdInsiemeChiuso() {
		ArrayList<InsiemeChiuso> insiemi = logica.getInsiemi();
		if(insiemi.isEmpty())
			return 0;
		int ultimo = insiemi.size() - 1;
		InsiemeChiuso ins = insiemi.get(ultimo);
		return ins.getId();
	}

	/**
	 * Metodo per andare a visualizzare l'insiemi chiusi in modo da poter poi contatare i fruitori associati
	 */
	public String formattaInsiemiChiusi() {
		return Utilitaria.formattaLista(logica.getInsiemi());
	}
	
	
	public void mostraPropostePerFruitore(Fruitore fruit, VistaFruitore vf) {
		vf.visualizzaPropostePerFruitore(fruit, logica.getScambi());
	}
	//PER TEST
	/**
	 * Metodo per effettuare il testing della funzionalità
	 * @param proposta
	 * @param proposte
	 * @return InsiemeChiuso
	 */
	public InsiemeChiuso verificaSoddisfacimentoTest(PropostaScambio proposta, ArrayList<PropostaScambio> proposte) {
		
		ArrayList<PropostaScambio> proposteValide = selezionaProposteValide(proposte, proposta);
		if(proposteValide.isEmpty()) {
			return null;
		}
		
		for(PropostaScambio p: proposteValide) {
			if(coppiaPerfetta(proposta, p)) {
				InsiemeChiuso ins = new InsiemeChiuso(recuperaIdInsiemeChiuso());
				aggiornaStatoAChiusa(proposta, proposte);
				aggiornaStatoAChiusa(p, proposte);
				ins.aggiungiProposteAInsiemeChiuso(proposta);
				ins.aggiungiProposteAInsiemeChiuso(p);
				return ins;
			}
		}
		
		ArrayList<PropostaScambio> catena = new ArrayList<PropostaScambio>();
		catena.add(proposta);
		
		if(cercaCatena(catena, proposteValide)) {
			InsiemeChiuso insC = new InsiemeChiuso(recuperaIdInsiemeChiuso());
			for(PropostaScambio p: catena) {
				aggiornaStatoAChiusa(p, proposte);
				insC.aggiungiProposteAInsiemeChiuso(p);
			}
			return insC;
		} else {
			return null;
		}
	}

}
