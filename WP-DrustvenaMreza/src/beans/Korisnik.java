package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Korisnik {

	private String korisnickoIme;
	private String lozinka;
	private String email;
	private String ime;
	private String prezime;
	private Date datumRodjenja;
	private Pol pol;
	private Uloga uloga;
	private Slika profilnaSlika; // samo putanja?
	private List<Objava> objave;
	private List<Slika> slike; // ?
	private List<Korisnik> prijatelji;
	private List<ZahtevZaPrijateljstvo> zahteviZaPrijateljstvo;
	private boolean privatan;
	private boolean obrisan;

	public Korisnik() {
		this.objave = new ArrayList<Objava>();
		this.slike = new ArrayList<Slika>();
		this.prijatelji = new ArrayList<Korisnik>();
		this.zahteviZaPrijateljstvo = new ArrayList<ZahtevZaPrijateljstvo>();
	}

	public Korisnik(String korisnickoIme, String lozinka, String email, String ime, String prezime, Date datumRodjenja,
			Pol pol, Uloga uloga, Slika profilnaSlika, List<Objava> objave, List<Slika> slike,
			List<ZahtevZaPrijateljstvo> zahteviZaPrijateljstvo, List<Korisnik> prijatelji, boolean privatan,
			boolean obrisan) {
		this();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.email = email;
		this.ime = ime;
		this.prezime = prezime;
		this.datumRodjenja = datumRodjenja;
		this.pol = pol;
		this.uloga = uloga;
		this.profilnaSlika = profilnaSlika;
		this.objave = objave;
		this.slike = slike;
		this.prijatelji = prijatelji;
		this.zahteviZaPrijateljstvo = zahteviZaPrijateljstvo;
		this.privatan = privatan;
		this.obrisan = obrisan;
	}

	public Korisnik(String kIme) {
		this();
		this.korisnickoIme = kIme;
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

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Date getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public Pol getPol() {
		return pol;
	}

	public void setPol(Pol pol) {
		this.pol = pol;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

	public Slika getProfilnaSlika() {
		return profilnaSlika;
	}

	public void setProfilnaSlika(Slika profilnaSlika) {
		this.profilnaSlika = profilnaSlika;
	}

	public List<Objava> getObjave() {
		return objave;
	}

	public void setObjave(List<Objava> objave) {
		this.objave = objave;
	}

	public List<Slika> getSlike() {
		return slike;
	}

	public void setSlike(List<Slika> slike) {
		this.slike = slike;
	}

	public List<Korisnik> getPrijatelji() {
		return prijatelji;
	}

	public void setPrijatelji(List<Korisnik> prijatelji) {
		this.prijatelji = prijatelji;
	}

	public List<ZahtevZaPrijateljstvo> getZahteviZaPrijateljstvo() {
		return zahteviZaPrijateljstvo;
	}

	public void setZahteviZaPrijateljstvo(List<ZahtevZaPrijateljstvo> zahteviZaPrijateljstvo) {
		this.zahteviZaPrijateljstvo = zahteviZaPrijateljstvo;
	}

	public boolean isPrivatan() {
		return privatan;
	}

	public void setPrivatan(boolean privatan) {
		this.privatan = privatan;
	}

	public boolean isObrisan() {
		return obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}

}
