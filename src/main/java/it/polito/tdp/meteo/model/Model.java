package it.polito.tdp.meteo.model;

import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private List<Citta> citta;
	private MeteoDAO dao;
	private int minimo = Integer.MAX_VALUE;
	private int giornoPartenza;
	
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
	
	public String trovaSequenza(int mese) {
		for(Citta c : citta) {
			c.setRilevamenti(new ArrayList<>(this.dao.getAllRilevamentiLocalitaMese(mese, c.getNome())));
		}
		this.setGiornoPartenza(citta.get(0).getRilevamenti().get(0).getData());
		int costo = this.calcoloCosto_ricorsivo(0, citta, citta.get(0).getRilevamenti().get(0).getData(), new HashMap<LocalDate, Citta>());
		return "Costo: "+costo;
	}
	
	/* in questa soluzione l'operatore non puo' tornare due volte nella stessa citta' */
	private int calcoloCosto_ricorsivo(int costo, List<Citta> cittaMancanti, LocalDate giorno, Map<LocalDate, Citta> sol) {
		// caso terminale
		// le città sono state tutte visitate:
		if(cittaMancanti.size()==0) {
			if(minimo > costo)
				minimo = costo;
			return minimo;
		}
		
		// caso ricorsivo
		for(int i=giorno.getDayOfMonth(); i<=this.giornoPartenza+NUMERO_GIORNI_TOTALI; i++) {
			for(Citta c : cittaMancanti) {
				int aggiunta = 0;
				if(sol.size()!=0)
					aggiunta += COST;
				sol.put(giorno, c);
				c.increaseCounter(3);
				
				try {
					aggiunta += c.getUmiditaByDate(giorno);
				} catch (UnexpectedException e) {
					e.printStackTrace();
					throw new RuntimeException("Controllare la ricezione del valore di umidita'");
				}
				costo += aggiunta;
				cittaMancanti.remove(c);
				
				// richiamo la ricorsione
				LocalDate nuovoGiorno =LocalDate.of(giorno.getYear(), giorno.getMonthValue(), giorno.getDayOfMonth()+NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN);
				if(this.calcoloCosto_ricorsivo(costo, cittaMancanti, nuovoGiorno, sol) < minimo) {
				//  backtracking
					c.decreaseCounter(3);
					sol.remove(giorno);
					costo-=aggiunta;
					cittaMancanti.add(c);
				}
				
			}
			
		}
		
		throw new RuntimeException("Errore nel processo ricorsivo");
	}
	private void setGiornoPartenza(LocalDate giorno) {
		this.giornoPartenza = giorno.getDayOfMonth();
	}
}
