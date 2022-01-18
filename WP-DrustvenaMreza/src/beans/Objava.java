package beans;

import java.util.ArrayList;
import java.util.List;

public class Objava {
	private String id;
	private String slika; // putanja
	private String tekst;
	private List<Komentar> komentari;
	private boolean obrisana;

	public Objava() {
		this.komentari = new ArrayList<Komentar>();
	}

	public Objava(String id, String slika, String tekst, List<Komentar> komentari, boolean obrisana) {
		this();
		this.id = id;
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
