package beans;

import java.util.ArrayList;
import java.util.List;

public class Objava {
	private String id;
	private Korisnik korisnik;
	private String slika; // putanja
	private String tekst;
	private List<Komentar> komentari;
	private boolean obrisana;
	private boolean isSlika;

	public Objava() {
		this.komentari = new ArrayList<Komentar>();
	}

	public Objava(String id, Korisnik korisnik, String slika, String tekst, List<Komentar> komentari,
			boolean obrisana, boolean isSlika) {
		this();
		this.id = id;
		this.korisnik = korisnik;
		this.slika = slika;
		this.tekst = tekst;
		this.komentari = komentari;
		this.obrisana = obrisana;
		this.isSlika = isSlika;
	}

	public boolean isSlika() {
		return isSlika;
	}

	public void setSlika(boolean isSlika) {
		this.isSlika = isSlika;
	}

	public Objava(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
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

	@Override
	public String toString() {
		return "Objava [id=" + id + ", korisnik=" + korisnik + ", slika=" + slika + ", tekst=" + tekst + ", komentari="
				+ komentari + ", obrisana=" + obrisana + "]";
	}
	
	

}
