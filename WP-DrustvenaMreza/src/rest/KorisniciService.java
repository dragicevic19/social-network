package rest;

import java.util.HashMap;
import java.util.List;

import beans.Korisnik;
import beans.Status;
import beans.ZahtevZaPrijateljstvo;
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

	public static void acceptRequest(ZahtevZaPrijateljstvo zahtev) {
		Korisnik primalac = zahtev.getPrimalac();
		Korisnik posiljalac = zahtev.getPosiljalac();
		zahtev.setStatus(Status.PRIHVACENO);	
		primalac.getPrijatelji().add(posiljalac.getKorisnickoIme());
		posiljalac.getPrijatelji().add(primalac.getKorisnickoIme());
	}

	public static List<Korisnik> getFriendsForUser(String username, KorisnikDAO korisniciDAO) {
		Korisnik k = korisniciDAO.pronadjiKorisnika(username);
		return korisniciDAO.pronadjiPrijateljeZaKorisnika(k);
	}

}
