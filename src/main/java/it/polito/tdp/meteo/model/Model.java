package it.polito.tdp.meteo.model;

import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private List<Citta> citta;
	private MeteoDAO dao;
	
	public Model() {
		dao = new MeteoDAO();
		citta = new ArrayList<>(dao.getAllLocalita());
	}

	public String getUmiditaMedia(int mese) {
		String output = "";
		for(Citta c : citta) {
			double somma = 0.0;
			List<Rilevamento> ril = new ArrayList<>(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			for(Rilevamento r : ril) {
				somma += (double) r.getUmidita()/100;
			}
		
			if(output!="")
				output+="\n";
			output += c.toString() + ": " + (somma/ril.size());
		}
		return output;
	}
	/*
	 * Sapendo che nel database sono presenti 3 città, supponiamo che un tecnico debba compiere delle analisi 
		tecniche della durata di un giorno in ciascuna città. Le analisi hanno un costo per ogni giornata, 
		determinato dalla somma di due contributi: un fattore costante (di valore 100) ogniqualvolta il tecnico si 
		deve spostare da una città ad un’altra in due giorni successivi, ed un fattore variabile pari al valore 
		numerico dell’umidità della città nel giorno considerato. Si trovi la sequenza delle città da visitare nei 
		primi 15 giorni del mese selezionato, tale da minimizzare il costo complessivo rispettando i seguenti 
		vincoli:
- Nei primi 15 giorni del mese, tutte le città devono essere visitate almeno una volta
- In nessuna città si possono trascorrere più di 6 giornate (anche non consecutive)
- Scelta una città, il tecnico non si può spostare prima di aver trascorso 3 giorni consecutivi.
	 
	 */

	private int minimo = Integer.MAX_VALUE;
	private List<Citta> soluzione;
	
	public String trovaSequenza(int mese) {
		minimo = Integer.MAX_VALUE;
		this.soluzione = null;
		this.percorso_ricorsione(1, new ArrayList<Citta>(), 0, mese);
		String output = "Il percorso che minimizza i costi ha costo totale: € "+minimo;
		for(Citta c : soluzione) {
			output+="\n"+c.toString();
		}
		return output;
	}
	
	private void percorso_ricorsione(int livello, List<Citta> parziale, int costo, int mese) {
		// casi terminali
		// non ci sono più giorni
		if(livello==NUMERO_GIORNI_TOTALI+1) {
			if(costo<minimo && isValida(parziale)) {
				this.soluzione = new ArrayList<Citta>(parziale);
				minimo = costo;
			}
			return;
		}
		
		// caso ricorsivo:
		else {
			
			// scorro lungo le citta
			for(Citta c : this.citta) { //int i=livello; i<NUMERO_GIORNI_TOTALI; i++
				int aggiunta = 0;
				if(!ceNeSonoTroppe(parziale, c)) {
					try {
						aggiunta += c.getUmiditaByDate(LocalDate.of(2013, mese, livello));
					} catch(UnexpectedException ue) {
						ue.printStackTrace();
					}
					
					if(parziale.size()!=0 && !parziale.get(parziale.size()-1).equals(c))
						aggiunta += COST;
					c.increaseCounter();
					parziale.add(c);
					costo += aggiunta;
					livello+=1;
					this.percorso_ricorsione(livello, parziale, costo, mese);
					costo -= aggiunta;
					parziale.remove(parziale.size()-1);
					livello -= 1;
					c.decreaseCounter();
				}
			}
		}
	}

	private boolean isValida(List<Citta> parziale) {
		int t = 0;
		for(Citta c : this.citta)
			if(parziale.contains(c) && c.getCounter() >= NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
				t++;
			}
		if(t==3)
			return true;
		return false;
	}

	private boolean ceNeSonoTroppe(List<Citta> parziale, Citta c) {
		int count = 0;
		for(Citta t : parziale) {
			if(t.equals(c))
				count++;
		}
		if(count > NUMERO_GIORNI_CITTA_MAX -1) {
			return true;
		}
		return false;
	}
	
}