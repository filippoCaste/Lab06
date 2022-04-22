package it.polito.tdp.meteo.model;

import java.rmi.UnexpectedException;
import java.time.LocalDate;
import java.util.List;

public class Citta {
	
	
	private String nome;
	private List<Rilevamento> rilevamenti;
	private int counter = 0;
	
	public Citta(String nome) {
		this.nome = nome;
	}
	
	public Citta(String nome, List<Rilevamento> rilevamenti) {
		this.nome = nome;
		this.rilevamenti = rilevamenti;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Rilevamento> getRilevamenti() {
		return rilevamenti;
	}

	public void setRilevamenti(List<Rilevamento> rilevamenti) {
		this.rilevamenti = rilevamenti;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void increaseCounter(int i) {
		this.counter += i;
	}
	
	public void decreaseCounter(int i) {
		this.counter -= i;
	}
	
	public void increaseCounter() {
		this.counter += 1;
	}
	
	public void decreaseCounter() {
		this.counter -= 1;
	}
	
	public int getUmiditaByDate(LocalDate giorno) throws UnexpectedException {
		for(Rilevamento r : rilevamenti) {
			if(r.getData().getDayOfMonth()==giorno.getDayOfMonth() & r.getData().getMonthValue()==giorno.getMonthValue())
				return r.getUmidita();
		}
		throw new UnexpectedException("Qualcosa è andato storto nella ricerca dell'umidità");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citta other = (Citta) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}
	

}
