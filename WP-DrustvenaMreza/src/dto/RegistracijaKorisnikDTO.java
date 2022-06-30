package dto;

public class RegistracijaKorisnikDTO {

	private String email;
	private String ime;
	private String korisnickoIme;
	private String lozinka;
	private String pol;
	private String prezime;
	private String uloga;
	
	public RegistracijaKorisnikDTO() {
		
	}
	

	public RegistracijaKorisnikDTO(String email, String ime, String korisnickoIme, String lozinka, String pol,
			String prezime, String uloga) {
		super();
		this.email = email;
		this.ime = ime;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.pol = pol;
		this.prezime = prezime;
		this.uloga = uloga;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	
	
	
}
