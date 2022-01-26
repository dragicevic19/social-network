package rest;

import java.util.HashMap;

import beans.Korisnik;
import dao.KorisnikDAO;

public class KorisniciService {

	public KorisniciService() {
	}

	public static Korisnik login(String kIme, String lozinka, KorisnikDAO korisniciDAO) {
		HashMap<String, Korisnik> korisnici = korisniciDAO.getKorisnici();
		if (korisnici.containsKey(kIme) && korisnici.get(kIme).getLozinka().equals(lozinka)) {
			return korisnici.get(kIme);
		} else {
			return null;
		}
	}

	public static Korisnik register(Korisnik k, KorisnikDAO korisniciDAO) {
		return korisniciDAO.sacuvaj(k);
		// u funkciji sacuvaj() se proverava da li vec postoji username
	}

	public static Korisnik changePassword(Korisnik k, KorisnikDAO korisniciDAO) {
		Korisnik korisnik = korisniciDAO.pronadjiKorisnika(k.getKorisnickoIme());
		if (korisnik == null) {
			return null;
		}
		korisnik.setLozinka(k.getLozinka());	// sacuvaj u bazu
		return korisnik;
	}

	public static Korisnik update(Korisnik k, KorisnikDAO korisniciDAO) {
		Korisnik korisnik = korisniciDAO.pronadjiKorisnika(k.getKorisnickoIme());
		if (korisnik == null) return null;
		korisnik.setEmail(k.getEmail());
		korisnik.setIme(k.getIme());
		korisnik.setPrezime(k.getPrezime());
		korisnik.setDatumRodjenja(k.getDatumRodjenja());
		return korisnik;
	}
}
