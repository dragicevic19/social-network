package dto;

import java.util.List;

import beans.Slika;

public class ZahtevDTO {
	private String idZahteva;
	private String korisnickoIme;
	private String ime;
	private String prezime;
	private List<String> prijateljiPosiljaoca;
	private Slika profilnaSlika;

	public ZahtevDTO() {

	}

	public ZahtevDTO(String idZahteva, String korisnickoIme, String ime, String prezime,
			List<String> prijateljiPosiljaoca, Slika profilnaSlika) {
		super();
		this.idZahteva = idZahteva;
		this.korisnickoIme = korisnickoIme;
		this.ime = ime;
		this.prezime = prezime;
		this.prijateljiPosiljaoca = prijateljiPosiljaoca;
		this.profilnaSlika = profilnaSlika;
	}



	public String getIdZahteva() {
		return idZahteva;
	}

	public void setIdZahteva(String idZahteva) {
		this.idZahteva = idZahteva;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public List<String> getPrijateljiPosiljaoca() {
		return prijateljiPosiljaoca;
	}

	public void setPrijateljiPosiljaoca(List<String> prijateljiPosiljaoca) {
		this.prijateljiPosiljaoca = prijateljiPosiljaoca;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public Slika getProfilnaSlika() {
		return profilnaSlika;
	}

	public void setProfilnaSlika(Slika profilnaSlika) {
		this.profilnaSlika = profilnaSlika;
	}

	
}
