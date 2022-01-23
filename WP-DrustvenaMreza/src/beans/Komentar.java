package beans;

import java.util.Date;

public class Komentar {
	private String id;
	private Korisnik korisnik;
	private String tekst;
	private Date datumKomentara;
	private Date datumIzmene;
	private boolean obrisan;

	public Komentar() {
	}

	public Komentar(String id, Korisnik korisnik, String tekst, Date datumKomentara, Date datumIzmene,
			boolean obrisan) {
		super();
		this.id = id;
		this.korisnik = korisnik;
		this.tekst = tekst;
		this.datumKomentara = datumKomentara;
		this.datumIzmene = datumIzmene;
		this.obrisan = obrisan;
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

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public Date getDatumKomentara() {
		return datumKomentara;
	}

	public void setDatumKomentara(Date datumKomentara) {
		this.datumKomentara = datumKomentara;
	}

	public Date getDatumIzmene() {
		return datumIzmene;
	}

	public void setDatumIzmene(Date datumIzmene) {
		this.datumIzmene = datumIzmene;
	}

	public boolean isObrisan() {
		return obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}

	@Override
	public String toString() {
		return "Komentar [id=" + id + ", korisnik=" + korisnik + ", tekst=" + tekst + ", datumKomentara="
				+ datumKomentara + ", datumIzmene=" + datumIzmene + ", obrisan=" + obrisan + "]";
	}

}
