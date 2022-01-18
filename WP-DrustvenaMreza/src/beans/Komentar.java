package beans;

import java.util.Date;

public class Komentar {
	private String id;
	private Korisnik korisnik;
	private Date datumKomentara;
	private Date datumIzmene;
	private boolean obrisan;

	public Komentar() {
	}

	public Komentar(String id, Korisnik korisnik, Date datumKomentara, Date datumIzmene, boolean obrisan) {
		super();
		this.id = id;
		this.korisnik = korisnik;
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

}
