package model;

import java.time.LocalDate;
import java.util.LinkedList;

public class Putovanje {
	private int id;
	private User putnik;
	private LinkedList<Zemlja> zemlje;
	private LocalDate datum_prijave;
	private LocalDate datum_ulaska;
	private LocalDate datum_izlaska;
	private Transport transport;
	private boolean placa_se;
	public Putovanje(User putnik, LinkedList<Zemlja> zemlja, LocalDate datum_prijave, LocalDate datum_ulaska,
			LocalDate datum_izlaska, Transport transport, boolean placa_se) {
		if (!validacijaPutovanja(putnik, zemlja, datum_prijave, datum_ulaska, datum_izlaska, transport, placa_se))
			throw new RuntimeException("Invalid trip info");
		
		this.putnik = putnik;
		this.zemlje = zemlja;
		this.datum_prijave = datum_prijave;
		this.datum_ulaska = datum_ulaska;
		this.datum_izlaska = datum_izlaska;
		this.transport = transport;
		this.placa_se = placa_se;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getPutnik() {
		return putnik;
	}
	public LinkedList<Zemlja> getZemlja() {
		return zemlje;
	}
	public LocalDate getDatum_prijave() {
		return datum_prijave;
	}
	public LocalDate getDatum_ulaska() {
		return datum_ulaska;
	}
	public LocalDate getDatum_izlaska() {
		return datum_izlaska;
	}
	public Transport getTransport() {
		return transport;
	}
	public boolean isPlaca_se() {
		return placa_se;
	}
	
	
	
	public void setPutnik(User putnik) {
		this.putnik = putnik;
	}

	public void setZemlja(LinkedList<Zemlja> zemlja) {
		this.zemlje = zemlja;
	}

	public void setDatum_prijave(LocalDate datum_prijave) {
		this.datum_prijave = datum_prijave;
	}

	public void setDatum_ulaska(LocalDate datum_ulaska) {
		this.datum_ulaska = datum_ulaska;
	}

	public void setDatum_izlaska(LocalDate datum_izlaska) {
		this.datum_izlaska = datum_izlaska;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

	public void setPlaca_se(boolean placa_se) {
		this.placa_se = placa_se;
	}

	private boolean validacijaPutovanja(User putnik, LinkedList<Zemlja> zemlje, LocalDate datum_prijave, LocalDate datum_ulaska,
			LocalDate datum_izlaska, Transport transport, boolean placa_se) {
		return true; // To be implemented
	}
}
