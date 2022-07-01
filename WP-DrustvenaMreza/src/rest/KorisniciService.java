package rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import beans.Korisnik;
import beans.Pol;
import beans.Slika;
import beans.Status;
import beans.Uloga;
import beans.ZahtevZaPrijateljstvo;
import dao.KorisnikDAO;
import dao.SlikeDAO;
import dao.ZahteviDAO;
import dto.RegistracijaKorisnikDTO;

public class KorisniciService {

	public KorisniciService() {
	}

	public static Korisnik login(String kIme, String lozinka, KorisnikDAO korisniciDAO) {
		HashMap<String, Korisnik> korisnici = korisniciDAO.getKorisnici();
		if (korisnici.containsKey(kIme) && korisnici.get(kIme).getLozinka().equals(lozinka)) {
			Korisnik k = korisnici.get(kIme);
			if (k.isBlokiran()) return null;
			return k;
		} else {
			return null;
		}
	}

	public static Korisnik register(RegistracijaKorisnikDTO k, KorisnikDAO korisniciDAO, SlikeDAO slikeDAO) {
		Korisnik newUser = new Korisnik();
		newUser.setEmail(k.getEmail());
		newUser.setIme(k.getIme());
		newUser.setPrezime(k.getPrezime());
		newUser.setKorisnickoIme(k.getKorisnickoIme());
		newUser.setLozinka(k.getLozinka());
		newUser.setPol(Pol.valueOf(k.getPol()));
		newUser.setUloga(Uloga.valueOf(k.getUloga()));
		newUser.setObrisan(false);
		newUser.setPrivatan(true);
		newUser.setObjave(new ArrayList<>());
		newUser.setPrijatelji(new ArrayList<>());
		newUser.setZahteviZaPrijateljstvo(new ArrayList<>());
		newUser.setSlike(new ArrayList<>());
		newUser.setProfilnaSlika(slikeDAO.pronadjiSliku("1")); // default avatar.png
		try {
			newUser.setDatumRodjenja(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1970"));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return korisniciDAO.sacuvaj(newUser);
		// u funkciji sacuvaj() se proverava da li vec postoji username i email
	}

	public static Korisnik changePassword(Korisnik k, KorisnikDAO korisniciDAO) {
		Korisnik korisnik = korisniciDAO.pronadjiKorisnika(k.getKorisnickoIme());
		if (korisnik == null) {
			return null;
		}
		korisnik.setLozinka(k.getLozinka()); // sacuvaj u bazu
		korisniciDAO.upisiUFajl();
		return korisnik;
	}

	public static Korisnik update(Korisnik k, KorisnikDAO korisniciDAO) {
		Korisnik korisnik = korisniciDAO.pronadjiKorisnika(k.getKorisnickoIme());
		if (korisnik == null)
			return null;
		korisnik.setEmail(k.getEmail());
		korisnik.setIme(k.getIme());
		korisnik.setPrezime(k.getPrezime());
		korisnik.setDatumRodjenja(k.getDatumRodjenja());
		korisnik.setPrivatan(k.isPrivatan());
		korisniciDAO.upisiUFajl();
		return korisnik;
	}

	public static void acceptRequest(ZahtevZaPrijateljstvo zahtev, KorisnikDAO korisniciDAO, ZahteviDAO zahteviDAO) {
		Korisnik primalac = zahtev.getPrimalac();
		Korisnik posiljalac = zahtev.getPosiljalac();
		zahtev.setStatus(Status.PRIHVACENO);
		primalac.getPrijatelji().add(posiljalac.getKorisnickoIme());
		posiljalac.getPrijatelji().add(primalac.getKorisnickoIme());
		
		zahteviDAO.sacuvajUFajl();
		korisniciDAO.upisiUFajl();
	}

	public static List<Korisnik> getFriendsForUser(String username, KorisnikDAO korisniciDAO) {
		Korisnik k = korisniciDAO.pronadjiKorisnika(username);
		return korisniciDAO.pronadjiPrijateljeZaKorisnika(k);
	}

	public static List<Korisnik> getMutualFriends(String usernameOne, String usernameTwo, KorisnikDAO korisniciDAO) {
		List<Korisnik> retList = new ArrayList<Korisnik>();
		Korisnik userOne = korisniciDAO.pronadjiKorisnika(usernameOne);
		Korisnik userTwo = korisniciDAO.pronadjiKorisnika(usernameTwo);
		for (String friendOne : userOne.getPrijatelji()) {
			for (String friendTwo : userTwo.getPrijatelji()) {
				if (friendOne.equals(friendTwo)) {
					retList.add(korisniciDAO.pronadjiKorisnika(friendOne));
					break;
				}
			}
		}
		return retList;
	}

	public static boolean removeFriend(String userOne, String userTwo, KorisnikDAO korisniciDAO) {
		Korisnik k1 = korisniciDAO.pronadjiKorisnika(userOne);
		Korisnik k2 = korisniciDAO.pronadjiKorisnika(userTwo);

		boolean found = false;
		for (String kIme : k1.getPrijatelji()) {
			if (kIme.equals(k2.getKorisnickoIme())) {
				found = true;
				break;
			}
		}

		if (!found)
			return false;

		k1.getPrijatelji().remove(k2.getKorisnickoIme());
		k2.getPrijatelji().remove(k1.getKorisnickoIme());

		korisniciDAO.upisiUFajl();
		
		return true;
	}

	public static List<Korisnik> search(String query, String options, String sort, KorisnikDAO korisniciDAO) {
		List<Korisnik> searchRes = new ArrayList();

		for (Korisnik k : korisniciDAO.getKorisnici().values()) {
			if (k.search(query, options)) searchRes.add(k);
		}
		
		switch(sort) {
		case "name":
			Collections.sort(searchRes, new Comparator<Korisnik>() {
			  @Override
			  public int compare(Korisnik k1, Korisnik k2) {
			    return k1.getIme().toUpperCase().compareTo(k2.getIme().toUpperCase());
			  }
			});
			break;
		case "lastName":
			Collections.sort(searchRes, new Comparator<Korisnik>() {
			  @Override
			  public int compare(Korisnik k1, Korisnik k2) {
			    return k1.getPrezime().toUpperCase().compareTo(k2.getPrezime().toUpperCase());
			  }
			});
			break;
		case "none":
			break;
		default:
			break;
		}
		
		return searchRes;
	}

	public static void block(Korisnik k, KorisnikDAO korisniciDAO) {
		k.setBlokiran(true);
		korisniciDAO.upisiUFajl();
		
	}

	public static void unblock(Korisnik k, KorisnikDAO korisniciDAO) {
		k.setBlokiran(false);
		korisniciDAO.upisiUFajl();
	}

	public static void setProfilePic(Korisnik korisnik, Slika slika, KorisnikDAO korisniciDAO) {
		korisnik.setProfilnaSlika(slika);
		korisniciDAO.upisiUFajl();
	}

}
