package dto;

import java.util.ArrayList;
import java.util.List;

import beans.Komentar;
import beans.Korisnik;

public class ObjavaDTO {

	private String id;
	private String korsinickoIme;
	private String slika; // putanja
	private String tekst;
	private List<Komentar> komentari;
	private boolean obrisana;

	public ObjavaDTO() {
		this.komentari = new ArrayList<Komentar>();
	}

	public ObjavaDTO(String id, String korisnik, String slika, String tekst, List<Komentar> komentari,
			boolean obrisana) {
		this();
		this.id = id;
		this.korsinickoIme = korisnik;
		this.slika = slika;
		this.tekst = tekst;
		this.komentari = komentari;
		this.obrisana = obrisana;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKorisnickoIme() {
		return korsinickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korsinickoIme = korisnickoIme;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public List<Komentar> getKomentari() {
		return komentari;
	}

	public void setKomentari(List<Komentar> komentari) {
		this.komentari = komentari;
	}

	public boolean isObrisana() {
		return obrisana;
	}

	public void setObrisana(boolean obrisana) {
		this.obrisana = obrisana;
	}
}
