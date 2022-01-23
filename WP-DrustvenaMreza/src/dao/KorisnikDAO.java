package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Korisnik;
import beans.Objava;
import beans.Pol;
import beans.Slika;
import beans.Uloga;
import beans.ZahtevZaPrijateljstvo;

public class KorisnikDAO {

	private HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
	private SlikeDAO slikeDAO;

	public KorisnikDAO() {
	}

	public HashMap<String, Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	public KorisnikDAO(String contextPath, SlikeDAO slikeDAO) {
		this.slikeDAO = slikeDAO;
		ucitajKorisnike(contextPath);
	}

	public Collection<Korisnik> pronadjiSve() {
		return korisnici.values();
	}

	public Korisnik pronadjiKorisnika(String kIme) {
		return korisnici.containsKey(kIme) ? korisnici.get(kIme) : null;
	}

	public Korisnik sacuvaj(Korisnik korisnik) {
		if (korisnici.containsKey(korisnik.getKorisnickoIme())) {
			return null; // greska
		}
		korisnici.put(korisnik.getKorisnickoIme(), korisnik);

		// DODAJ U FAJL
		return korisnik; // ok
	}

	private void ucitajKorisnike(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/korisnici.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, korisnickoIme = "", lozinka = "", email = "", ime = "", prezime = "", sPol = "", sUloga = "",
					idProfilnaSlika = "", idSvihSlika = "", idObjava = "", idZahteva = "", kImePrijatelja = "",
					privatan = "", obrisan = "";

			Date datumRodjenja = null;
			Pol pol = null;
			Uloga uloga = null;
			Slika profilnaSlika = new Slika();
			List<Slika> slike = new ArrayList<Slika>();
			StringTokenizer st;

			while ((line = in.readLine()) != null) {
				List<String> prijatelji = new ArrayList<String>();
				List<String> objave = new ArrayList<String>();
				List<String> zahtevi = new ArrayList<String>();

				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					korisnickoIme = st.nextToken().trim();
					lozinka = st.nextToken().trim();
					email = st.nextToken().trim();
					ime = st.nextToken().trim();
					prezime = st.nextToken().trim();
					datumRodjenja = new SimpleDateFormat("dd/MM/yyyy").parse(st.nextToken().trim());
					sPol = st.nextToken().trim();
					sUloga = st.nextToken().trim();
					kImePrijatelja = st.nextToken().trim();
					idProfilnaSlika = st.nextToken().trim();
					idSvihSlika = st.nextToken().trim();
					idObjava = st.nextToken().trim();
					idZahteva = st.nextToken().trim();
					privatan = st.nextToken().trim();
					obrisan = st.nextToken().trim();
					pol = Pol.valueOf(sPol);
					uloga = Uloga.valueOf(sUloga);

					slike = slikeDAO.pronadjiSlike(idSvihSlika);
					profilnaSlika = slikeDAO.pronadjiSliku(idProfilnaSlika);

					// lista prijatelja ce biti lista
					if (!kImePrijatelja.equals("/")) { // korisnickih imena zbog rekurzije tako i za zahteve i
						prijatelji = parseIds(kImePrijatelja); // objave
					}
					if (!idObjava.equals("/")) {
						objave = parseIds(idObjava);
					}
					if (!idZahteva.equals("/")) {
						zahtevi = parseIds(idZahteva);
					}
				}
				korisnici.put(korisnickoIme,
						new Korisnik(korisnickoIme, lozinka, email, ime, prezime, datumRodjenja, pol, uloga,
								profilnaSlika, objave, slike, zahtevi, prijatelji, Boolean.parseBoolean(privatan),
								Boolean.parseBoolean(obrisan)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private List<String> parseIds(String ids) {
		List<String> retList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(ids, ",");
		while (st.hasMoreTokens()) {
			retList.add(st.nextToken().trim());
		}
		return retList;
	}

//	public void onUcitaneObjave(Collection<Objava> objave) {
//		for (Korisnik k : korisnici.values()) {
//			List<Objava> objaveKorisnika = new ArrayList<Objava>();
//			for (Objava objava : objave) {
//				if (k.getKorisnickoIme().equals(objava.getKorisnik().getKorisnickoIme())) {
//					objaveKorisnika.add(objava);
//				}
//			}
//			k.setObjave(objaveKorisnika);
//		}
//	}

//	public void onUcitaniZahtevi(Collection<ZahtevZaPrijateljstvo> zahtevi) {
//		for (ZahtevZaPrijateljstvo zahtev : zahtevi) {
//			korisnici.get(zahtev.getPrimalac().getKorisnickoIme()).getZahteviZaPrijateljstvo().add(zahtev);
//		}
//	}
//	public void onUcitaniZahtevi(Collection<ZahtevZaPrijateljstvo> zahtevi) {
//		for (Korisnik korisnik : korisnici.values()) {
//			List<ZahtevZaPrijateljstvo> zahteviKorisnika = new ArrayList<ZahtevZaPrijateljstvo>();
//			for (ZahtevZaPrijateljstvo zahtev : zahtevi) {
//				if (korisnik.getKorisnickoIme().equals(zahtev.getPrimalac().getKorisnickoIme())) {
//					zahteviKorisnika.add(zahtev);
//				}
//			}
//			korisnik.setZahteviZaPrijateljstvo(zahteviKorisnika);
//		}
//	}

	public void ispisiSve() {
		for (Korisnik k : korisnici.values()) {
			System.out.println(k);
		}
	}

//	public Korisnik update(Korisnik k, Korisnik noviKorisnik) {
//		k.setLozinka(noviKorisnik.getLozinka());
//		k.setEmail(noviKorisnik.getEmail());
//		k.setIme(noviKorisnik.getIme());
//		k.setPrezime(noviKorisnik.getPrezime());
//		// sacuvaj u bazi
//		return k;
//	}
}
