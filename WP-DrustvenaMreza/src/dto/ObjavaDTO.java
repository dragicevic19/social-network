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
	private boolean isSlika;

	public ObjavaDTO() {
		this.komentari = new ArrayList<Komentar>();
	}

	public ObjavaDTO(String id, String korisnik, String slika, String tekst, List<Komentar> komentari,
			boolean obrisana, boolean isSlika) {
		this();
		this.id = id;
		this.korsinickoIme = korisnik;
		this.slika = slika;
		this.tekst = tekst;
		this.komentari = komentari;
		this.obrisana = obrisana;
		this.isSlika = isSlika;
	}

	public String getId() {
		return id;
	}

	public String getKorsinickoIme() {
		return korsinickoIme;
	}

	public void setKorsinickoIme(String korsinickoIme) {
		this.korsinickoIme = korsinickoIme;
	}

	public boolean isSlika() {
		return isSlika;
	}

	public void setSlika(boolean isSlika) {
		this.isSlika = isSlika;
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
