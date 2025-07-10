package menu;

import java.util.*;

import applicazione.CampoCaratteristico;
import applicazione.Categoria;
import applicazione.CategoriaFoglia;
import applicazione.Gerarchia;
import applicazione.InsiemeChiuso;
import applicazione.Proposta;
import applicazione.PropostaScambio;
import applicazione.StatoProposta;
import applicazione.TipoProposta;
import persistenza.GestorePersistenza;
import persistenza.LogicaPersistenza;
import utenti.Fruitore;
import util.InputDati;
import util.Menu;
import vista.Vista;
import vista.VistaFruitore;

/**
 * Classe per definite il menu delle funzionalità del fruitore
 * 
 * @author Irene Romano 736566
 *
 */
public class MenuFruitore extends Menu{
	
	private Fruitore fruit;
	private LogicaPersistenza logica;
	private VistaFruitore vf;
	
	private static final String titolo = "\tMENU FRUITORE";
	
	private static final String NAVIGA = "Naviga tra le gerarchie";
	private static final String RICHIEDI_PRESTAZIONI = "Richiedi prestazioni al sistema";
	private static final String RITIRA_PROPOSTE = "Ritira proposte dal sistema";
	private static final String VISUALIZZA_PROPOSTE = "Visualizza le tue proposte";
	private final static String MSG_P_PRECEDENTE = "Ritorna alla pagina di autenticazione";
	private static final String X = "\n******************************************";
	private static final String MSG_INIZIALE = "Gerarchie presenti nel tuo comprensorio:";
	private static final String MSG_ASSENZA_GERARCH = "Non ci sono gerarchie presenti per il tuo comprensorio.";
	private static final String MSG_SELEZ_GERARCH = "Seleziona una gerarchia > ";
	private static final String CATEGORIA_CORRENTE = "\nCategoria corrente: ";
	private static final String VALORI_IMPOSTATI = "Valori impostati finora: ";
	private static final String MSG_CATEG_FOGLIA = "Sei arrivato a una categoria foglia: ";
	private static final String MSG_MENU_PRINCIPALE = "Ritorno al menu principale.\n";
	private static final String MSG_ASSENZA_SOTTOCATEG = "Non ci sono sottocategorie. Ritorno al menu principale.";
	private static final String MSG_CAMPO_CARATT = "Campo caratteristico: ";
	private static final String MSG_SOTTOCATEG_DISP = "Sottocategorie disponibili:";
	private static final String DOT = ". ";
	private static final String COLON = ": ";
	private static final String NEW_LINE = "\n";
	private static final String MSG_VOCE_TORNA_INDIETRO = "0. Torna al menu";
	//versione 3
	private static final String MSG_SEL_PRESTAZIONE = "Seleziona la prestazione di interesse: ";
	private static final String MSG_INS_ORE = "Inserisci il numero di ore di questa prestazione che ti interessano:";
	private static final String MSG_SEL_OFFERTA = "Quale prestazione offri in cambio?";
	private static final String MSG_CONFERMA = "Confermi la seguente proposta di scambio?\n";
	private static final String MSG_Y_N = "\nVuoi confermare ";
	private static final String MSG_CHECK_COMPRENSORIO = "\nNon ci sono Gerarchie appartenenti al tuo comprensorio geografico.\n";
	private static final String MSG_ANNULLATO_SCAMBIO = "Hai annullato la proposta di scambio...";
	private static final String MSG_PRESTAZIONE_NON_VALIDA = "Non puoi offrire la stessa prestazione che richiedi!";
	//versione 4
	private static final String MSG_PRESTAZIONI_A_DISPOSIZIONE = "Prestazioni a disposizione >>\n";
	private static final String MSG_PROPOSTA_RITIRATA = "\nLa proposta è stata ritirata.\n";
	private static final String NEW_LINE_ARROW = "\n> ";
	private static final String MSG_ASSENZA_PROPOSTE = "\nNon ci sono proposte presenti.\n";
	private static final String MSG_SELEZIONA_PROPOSTA_DA_RITIRATE = "Seleziona la proposta che vuoi ritirare (annulla altrimenti) > ";
	private static final String ANNULLA_SELEZIONE = ": Annulla selezione";
	private static final String MSG_PROPOSTE_RITIRABILI = "Proposte ritirabili: ";
	private static final String MSG_CONFERMA_RITIRO_PROPOSTA = "\nSei sicuro di ritirare questa proposta ";
	private static final String MSG_OPERAZIONE_ANNULLATA = "\nOperazione annullata....\n";
	private static final String MSG_ASSENZA_PROPOSTE_RITIRATE = "\nNon hai proposte da ritirare.\n";
	private static final String MSG_PROPOSTA_SODDISFATTA = "\nLa tua proposta è stata soddisfatta verrai contatato a breve con tutte le informazioni!!\n";
	private static final String MSG_PROPOSTA_NON_SODDISFATTA = "\nAl momemnto non ci sono proposte che soddisfano la tua proposta.\n"
			+ "Sarai contattato appena verra' soddisfatta !!";
	
	private static final double SOGLIA_BASSA = 0.4;
	private static final double SOGLIA_ALTA = 0.6;
	
	
	private static String[] vociFruit = {NAVIGA, RICHIEDI_PRESTAZIONI, RITIRA_PROPOSTE, VISUALIZZA_PROPOSTE, MSG_P_PRECEDENTE};
	
	/**
	 * Construttore di MenuFruitore
	 * 
	 * @param fruit
	 * @param logica
	 */
	public MenuFruitore(Fruitore fruit, LogicaPersistenza logica) {
		super(titolo, vociFruit);
		this.fruit = fruit;
		this.logica = logica;
		this.vf = new VistaFruitore();
	}
	
	/**
	 * Metodo per navigare in profondita' tra le gerarchie
	 */
	public void naviga() {
		vf.mostraMessaggio(X);
		vf.mostraMessaggio(MSG_INIZIALE);
		ArrayList<Gerarchia> gerarch = new ArrayList<Gerarchia>();
		for(Gerarchia g: logica.getGerarchie()) {
			if(g.getNomeComprensorio().equals(fruit.getNomeComprensorio())) {
				gerarch.add(g);
			}
		}
		Gerarchia gScelta;
		if(gerarch.isEmpty()) {
			vf.mostraErrore(MSG_ANNULLATO_SCAMBIO);
			return;
		} else {
			gScelta = selezionaGerarchia(gerarch);
            navigaCategoria(gScelta.getCatRadice(), new HashMap<>()); 
            // Inizializziamo la mappa dei valori di campo per la navigazione
		}	
	}
	
	
	/**
	 * Metodo per selezionare una gerarchia presente all'interno del comprensorio selezionato
	 * @param gerarch
	 * @return
	 */
	private Gerarchia selezionaGerarchia(ArrayList<Gerarchia> gerarch) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < gerarch.size(); i++) {
			sb.append(i + COLON + gerarch.get(i).getCatRadice().getNome());
		}
		vf.mostraMessaggio(sb.toString());
		
		int scelta = InputDati.leggiIntero(MSG_SELEZ_GERARCH, 0, gerarch.size() - 1);
		return gerarch.get(scelta);
	}



	/**
	 * Metodo per andare a modificare l'output in base ai percorsi selezionati
	 * @param categoriaCorrente
	 * @param valoriImpostati
	 */
	private void navigaCategoria(Categoria categoriaCorrente, Map<String, String> valoriImpostati) {
	   vf.mostraMessaggio(CATEGORIA_CORRENTE + categoriaCorrente.getNome());

	    if (!valoriImpostati.isEmpty()) {
	 	   vf.mostraMessaggio(VALORI_IMPOSTATI + valoriImpostati);
	    }

	    if (categoriaCorrente.isFoglia()) {
	 	   vf.mostraMessaggio(MSG_CATEG_FOGLIA + categoriaCorrente.getNome());
		   vf.mostraMessaggio(MSG_MENU_PRINCIPALE);
	       return;
	    }

	    List<Categoria> sottocategorie = categoriaCorrente.getSottoCateg();
	    if (sottocategorie == null || sottocategorie.isEmpty()) {
	 	   vf.mostraErrore(MSG_ASSENZA_SOTTOCATEG);
	       return;
	    }

	    CampoCaratteristico campo = categoriaCorrente.getCampCaratt();
	    Categoria prossimaCategoria = null;

	    if (campo != null) {
	 	   vf.mostraMessaggio(MSG_CAMPO_CARATT + campo.getNomeCampo());
	    }

	    prossimaCategoria = selezionaSottoCategoria(sottocategorie);

	    if (prossimaCategoria != null) {
	        navigaCategoria(prossimaCategoria, new HashMap<>(valoriImpostati));
	    }
	}
	
	/**
	 * Metodo per selezionare una sottocategoria presente
	 * @param sottocategorie
	 * @return
	 */
	private Categoria selezionaSottoCategoria(List<Categoria> sottocategorie) {
		vf.mostraMessaggio(MSG_SOTTOCATEG_DISP);
		
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < sottocategorie.size(); i++) {
	       sb.append((i + 1) + DOT + sottocategorie.get(i).getNome());
	    }
	    sb.append(MSG_VOCE_TORNA_INDIETRO);
	    vf.mostraMessaggio(sb.toString());
	    
	    int scelta = InputDati.leggiIntero(MSG_SELEZ_GERARCH, 0, sottocategorie.size());
	    if (scelta == 0)
	    	return null;
	    
	    return sottocategorie.get(scelta - 1);
	}
	
	/**
	 * Metodo per la formulazione di una richiesta di scambio
	 * 1. il fruitore visualizza le categorie foglia a disposizione
	 * 2. seleziona categoria della richiesta
	 * 3. inserice ore (durata della prestazione d'opera desiderata)
	 * 4. creo oggetto Proposta di tipo richiesta
	 * 5. seleziona quale prestazione offre in cambio
	 * 6. formulo l'offerta
	 * 7. chiedo conferma degli oggetti
	 * 8. se confermato salvo lo scambio (aperta), salvando anche il fruitore
	 */
	public void richiediPrestazioni() {
		
		ArrayList<CategoriaFoglia> foglie = recuperaFoglieDisponibili();
		
		if(foglie.isEmpty()) {
			vf.mostraErrore(MSG_CHECK_COMPRENSORIO);
			return;
		}
	
		formattaPrestazioni(foglie); 
		
		//RICHIESTA
		int scelta = InputDati.leggiInteroConMINeMAX(MSG_SEL_PRESTAZIONE, 0, foglie.size()- 1);
		double ore = InputDati.leggiDoubleConMinimo(MSG_INS_ORE, 0);
		Proposta richiesta = new Proposta(foglie.get(scelta), TipoProposta.RICHIESTA, ore);

		//OFFERTA
		int incambio = 0;
		do {
			incambio = InputDati.leggiInteroConMINeMAX(MSG_SEL_OFFERTA, 0, foglie.size()- 1);
			if(incambio == scelta) {
				vf.mostraErrore(MSG_PRESTAZIONE_NON_VALIDA);
			}
		} while(incambio == scelta);
		
		ArrayList<Double> fattori = logica.getFatConversione().prendiRiga(scelta + 1); 
		//prendendo tutti i fdc dalla tabella uscenti da id della prestazione richiesta
	    double valore = (fattori.get(incambio + 1) * ore);
	    
	    double arrotondato = arrotondaCustom(valore);
		Proposta offerta = new Proposta(foglie.get(incambio), TipoProposta.OFFERTA, arrotondato);
		
		int id = logica.recuperaId();
		
		//SCAMBIO
		PropostaScambio scambio = new PropostaScambio(richiesta, offerta, id);
		boolean sn = InputDati.yesOrNo(MSG_CONFERMA + scambio.toString() + MSG_Y_N);
		
		if(sn) { //aggiunto alle proposte aperte
			scambio.setFruitoreAssociato(fruit);
			logica.addScambio(scambio);
			GestorePersistenza.salvaScambi(logica.getScambi());
			// verifica che lo scambio venga soddisfatto da delle proposte preesistenti
			verificaSoddisfacimento(scambio);
		} else {
			vf.mostraErrore(MSG_ANNULLATO_SCAMBIO + MSG_MENU_PRINCIPALE );
			return;
		}
	}
	
	public static double arrotondaCustom(double valore) {
	    int intero = (int) Math.floor(valore);
	    double decimale = valore - intero;
	    
	    return (decimale < SOGLIA_BASSA) ? intero  // arrotonda per difetto
	            : (decimale < SOGLIA_ALTA) ? intero + 0.5
	            : intero + 1;  // arrotonda per eccesso
	}
	
	/**
	 * Metodo che recupera le foglie disponibili nel comprensorio geografico del fruitore,
	 *  partendo dalle gerarchie salvate in LogicaPersistenza.
	 * @return array delle foglie a disposizione del fruitore
	 */
	private ArrayList<CategoriaFoglia> recuperaFoglieDisponibili() {
	    ArrayList<CategoriaFoglia> disponibili = new ArrayList<>();
	    
	    for (Gerarchia g : logica.getGerarchie()) {
	        if (g.getNomeComprensorio().equals(fruit.getNomeComprensorio())) {
	            disponibili.addAll(raccoltaFoglie(g.getCatRadice()));
	        }
	    }
	    return disponibili;
	}
	
	/**
	 * Metodo che contolla le sottocategorie di una categoria, inserendo quelle foglia in un array.
	 * @param categoria da cui ricavare le sottocategorie
	 * @return array di foglie
	 */
	private ArrayList<CategoriaFoglia>  raccoltaFoglie(Categoria cat) {
		ArrayList<CategoriaFoglia> cf = new ArrayList<>();

		if(cat.isFoglia()) {
			CategoriaFoglia foglia = getFogliaDaNome(cat);
			if (foglia != null) {
			    cf.add(foglia);
			}
	    } else if(cat.getSottoCateg() != null) {
	        for (Categoria sotto : cat.getSottoCateg()) {
	            cf.addAll(raccoltaFoglie(sotto));
	        }
	    }
		return cf;
	}
	
	/**
	 * Metodo che restituisce una foglia presente nel file categorieFoglia
	 * @param categoria che verifico sia di tipo foglia
	 * @return foglia 
	 */
	private CategoriaFoglia getFogliaDaNome(Categoria cat) {
	    String nomeCategoria = cat.getNome().trim().toLowerCase();

	    for (CategoriaFoglia f : logica.getCategorieFoglia()) {
	        if (f.getNome().trim().toLowerCase().equals(nomeCategoria)) {
	            return f;
	        }
	    }
	    return null;
	}
	
	/**
	 * Metodo di stampa delle prestazioni disponibili
	 * @param foglie
	 */
	private void formattaPrestazioni(ArrayList<CategoriaFoglia> foglie) {
		StringBuffer sb = new StringBuffer();
		int i = 0; //contatore per legenda
		sb.append(MSG_PRESTAZIONI_A_DISPOSIZIONE);		
		for(CategoriaFoglia f : foglie) {
			sb.append(i++);
			sb.append(COLON);
			sb.append(f.getNome());
			sb.append(NEW_LINE);
		}
		vf.mostraMessaggio(sb.toString());
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
	private void verificaSoddisfacimento(PropostaScambio proposta) {
		
		ArrayList<PropostaScambio> proposteValide = selezionaProposteValide(logica.getScambi(), proposta);
		if(proposteValide.isEmpty()) {
			vf.mostraErrore(MSG_PROPOSTA_NON_SODDISFATTA);
			return;
		}
		
		for(PropostaScambio p: proposteValide) {
			if(coppiaPerfetta(proposta, p)) {
				InsiemeChiuso ins = new InsiemeChiuso(logica.recuperaIdInsiemeChiuso());
				aggiornaStatoAChiusa(proposta, logica.getScambi());
				aggiornaStatoAChiusa(p, logica.getScambi());
				
				ins.aggiungiProposteAInsiemeChiuso(proposta);
				ins.aggiungiProposteAInsiemeChiuso(p);
				
				logica.aggiungiInsieme(ins);
				GestorePersistenza.salvaInsiemiChiusi(logica.getInsiemi());
				GestorePersistenza.salvaScambi(logica.getScambi());
				
				vf.mostraMessaggio(MSG_PROPOSTA_SODDISFATTA);
				return;
			}
		}
		
		ArrayList<PropostaScambio> catena = new ArrayList<PropostaScambio>();
		catena.add(proposta);
		
		if(cercaCatena(catena, proposteValide)) {
			InsiemeChiuso insC = new InsiemeChiuso(logica.recuperaIdInsiemeChiuso());
			
			for(PropostaScambio p: catena) {
				aggiornaStatoAChiusa(p, logica.getScambi());
				insC.aggiungiProposteAInsiemeChiuso(p);
			}
			logica.aggiungiInsieme(insC);
			GestorePersistenza.salvaInsiemiChiusi(logica.getInsiemi());
			GestorePersistenza.salvaScambi(logica.getScambi());
			
			vf.mostraMessaggio(MSG_PROPOSTA_SODDISFATTA);
			return;
		} else {
			vf.mostraMessaggio(MSG_PROPOSTA_NON_SODDISFATTA);
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
	public void ritiraProposte() {
		ArrayList<PropostaScambio> proposte = logica.getScambi();
		ArrayList<PropostaScambio> proposteFruit = new ArrayList<>();
		
		for(PropostaScambio p: proposte) {
			boolean corrisponde = this.fruit.getUsername().equals(p.getNomeAssociato());
			if(corrisponde && (p.getStatoFinale() == null)) {
				proposteFruit.add(p);
			}
		}
		
		if(proposteFruit.isEmpty()) {
			vf.mostraErrore(MSG_ASSENZA_PROPOSTE_RITIRATE);
			return;
		}
		
		int selezionata = selezionaPropostaRitirabile(proposteFruit);
		
		if(selezionata == proposteFruit.size()) {
			vf.mostraErrore(MSG_OPERAZIONE_ANNULLATA);
			return;
		}
		
		PropostaScambio p = proposteFruit.get(selezionata);
		boolean conferma = InputDati.yesOrNo(MSG_CONFERMA_RITIRO_PROPOSTA);
		if(conferma) {
			aggiornaStatoARitirata(p, proposte);
			GestorePersistenza.salvaScambi(proposte);
			vf.mostraMessaggio(MSG_PROPOSTA_RITIRATA);
		} else {
			vf.mostraMessaggio(MSG_OPERAZIONE_ANNULLATA);
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
				vf.mostraMessaggio(p.toString());
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
		vf.mostraMessaggio(MSG_PROPOSTE_RITIRABILI);
		vf.mostraMessaggio(formattaProposte(proposte));
		
		int propostaSelezionata = InputDati.leggiInteroConMINeMAX(MSG_SELEZIONA_PROPOSTA_DA_RITIRATE, 0, proposte.size());
		return propostaSelezionata;
	}

	private String formattaProposte(ArrayList<PropostaScambio> proposte) {
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < proposte.size(); i++) {
			sb.append(i + COLON + proposte.get(i).toString());
		}
		sb.append(proposte.size() + ANNULLA_SELEZIONE);
		
		return sb.toString();
	}
	
	/**
	 * Metodo per la visualizzazione delle proposte da parte del fruitore
	 * sia che siano aperte, chiuse, ritirate.
	 * Effettuato solo il controllo per vedere quelle di cui il fruitore è autore
	 */
	public String formattaPropostePerFruitore() {
		ArrayList<PropostaScambio> proposte = logica.getScambi();
		ArrayList<PropostaScambio> proposteFruit = new ArrayList<>();
		
		StringBuffer sb = new StringBuffer();
		
		if(proposte.isEmpty()) {
			return MSG_ASSENZA_PROPOSTE;
		}
			
		for(PropostaScambio p: proposte) {
			boolean corrisponde = this.fruit.getUsername().equals(p.getNomeAssociato());
			if(corrisponde)
				proposteFruit.add(p);
		}
		
		if(proposteFruit.isEmpty()) {
			return MSG_ASSENZA_PROPOSTE;
		} else {
			for(PropostaScambio p: proposteFruit) {
				sb.append(NEW_LINE_ARROW + p.toString());
			}
			return sb.toString();
				
		}
	}
	
	public void mostraProposte() {
		vf.visualizzaProposte(formattaPropostePerFruitore());
	}

}
