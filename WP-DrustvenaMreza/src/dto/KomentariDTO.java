package dto;

import java.util.Date;

import beans.Korisnik;

public class KomentariDTO {
	private String id;
	private String korisnik;
	private String tekst;
	private Date datumKomentara;
	private Date datumIzmene;
	private boolean obrisan;
	private String objavaID;

	public KomentariDTO() {
	}

	public KomentariDTO(String id, String korisnik, String tekst, Date datumKomentara, Date datumIzmene,
			boolean obrisan, String objavaID) {
		super();
		this.id = id;
		this.korisnik = korisnik;
		this.tekst = tekst;
		this.datumKomentara = datumKomentara;
		this.datumIzmene = datumIzmene;
		this.obrisan = obrisan;
		this.objavaID = objavaID;
	}

	public String getObjavaID() {
		return objavaID;
	}

	public void setObjavaID(String objavaID) {
		this.objavaID = objavaID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(String korisnik) {
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

}
