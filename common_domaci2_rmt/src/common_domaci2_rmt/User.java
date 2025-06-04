package common_domaci2_rmt;

import java.time.LocalDate;

public class User {
	private int id;
	private String username;
	private String password;
	private String ime;
	private String prezime;
	private String email;
	private String jmbg;
	private String broj_pasosa;
	private LocalDate datum_rodjenja;
	public User() {
		super();
	}
	public LocalDate getDatum_rodjenja() {
		return datum_rodjenja;
	}
	public void setDatum_rodjenja(LocalDate datum_rodjenja) {
		if (datum_rodjenja.isBefore(LocalDate.now()))
				this.datum_rodjenja = datum_rodjenja;
		else throw new RuntimeException("datum_rodjenja has to be before LocalDate.now()");
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if (username == null || username.length()<3)
			throw new RuntimeException("username cant be null nor shorter than 3 characters");
		else
			this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if (password == null || password.length()<8)
			throw new RuntimeException("password cant be null nor shorter than 8 characters");
		this.password = password;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		if (ime == null || ime.length()<2)
			throw new RuntimeException("Ime cant be null nor shorter than 2 characters");
		else
			this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		if (prezime == null || prezime.length()<2)
			throw new RuntimeException("Prezime cant be null nor shorter than 2 characters");
		else
			this.prezime = prezime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if (email == null || !email.contains("@") || !email.endsWith(".com"))
			throw new RuntimeException("Invalid email");
		else
			this.email = email;
	}
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		if (jmbg == null || jmbg.length() != 13 || isAllDigits(jmbg))
			throw new RuntimeException("jmbg has to be exactly 13 digits");
		this.jmbg = jmbg;
	}
	public String getBroj_pasosa() {
		return broj_pasosa;
	}
	public void setBroj_pasosa(String broj_pasosa) {
		if (!broj_pasosa.matches("^[A-Z0-9]{6,9}$")) // Stolen from ThE iNtErNeT
			throw new RuntimeException("broj_pasosa is not valid");
		this.broj_pasosa = broj_pasosa;
	}
	
	private boolean isAllDigits (String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i)))
				return false;
		}
		return true;
	}
}
