package dto;

import java.util.Date;
import java.util.List;

import beans.Korisnik;
import beans.Pol;
import beans.Slika;
import beans.Uloga;

public class UlogovaniKorisnikDTO {

	private String korisnickoIme;
	private String lozinka;
	private String email;
	private String ime;
	private String prezime;
	private Date datumRodjenja;
	private Pol pol;
	private Uloga uloga;
	private Slika profilnaSlika; // samo putanja?
	private List<String> objave;
	private List<Slika> slike; // ?
	private List<String> prijatelji;
	private List<String> zahteviZaPrijateljstvo;
	private boolean privatan;
	private boolean obrisan;

	private List<List<ZahtevDTO>> poslatiIPrimljeniZahtevi;
	private List<GrupisanePorukeDTO> poruke;
	
	public UlogovaniKorisnikDTO() {}

	public UlogovaniKorisnikDTO(Korisnik korisnik, List<List<ZahtevDTO>> zahtevi, List<GrupisanePorukeDTO> poruke) {
		this.korisnickoIme = korisnik.getKorisnickoIme();
		this.lozinka = korisnik.getLozinka();
		this.email = korisnik.getEmail();
		this.ime = korisnik.getIme();
		this.prezime = korisnik.getPrezime();
		this.datumRodjenja = korisnik.getDatumRodjenja();
		this.pol = korisnik.getPol();
		this.uloga = korisnik.getUloga();
		this.profilnaSlika = korisnik.getProfilnaSlika();
		this.objave = korisnik.getObjave();
		this.slike = korisnik.getSlike();
		this.prijatelji = korisnik.getPrijatelji();
		this.zahteviZaPrijateljstvo = korisnik.getZahteviZaPrijateljstvo();
		this.privatan = korisnik.isPrivatan();
		this.obrisan = korisnik.isObrisan();
		
		this.poslatiIPrimljeniZahtevi = zahtevi;
		this.poruke = poruke;
	}

}
