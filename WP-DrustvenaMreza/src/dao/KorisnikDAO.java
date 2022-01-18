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

//	private static KorisnikDAO instance = null;
//	
//	public static KorisnikDAO getInstance(){
//		if (instance == null) {
//			instance = new KorisnikDAO();
//			return instance;
//		}
//		else 
//			return instance;
//	}

	public KorisnikDAO() {
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
					idProfilnaSlika = "", idSvihObjava = "", idSvihSlika = "", idZahteviZaPrijateljstvo = "",
					kImePrijatelja = "", privatan = "", obrisan = "";

			Date datumRodjenja = null;
			Pol pol = null;
			Uloga uloga = null;
			Slika profilnaSlika = new Slika();
			List<Objava> objave = new ArrayList<Objava>();
			List<Slika> slike = new ArrayList<Slika>();
			List<ZahtevZaPrijateljstvo> zahtevi = new ArrayList<ZahtevZaPrijateljstvo>();
			List<Korisnik> prijatelji = new ArrayList<Korisnik>();

			StringTokenizer st;

			while ((line = in.readLine()) != null) {
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
					idProfilnaSlika = st.nextToken().trim();
					idSvihObjava = st.nextToken().trim();
					idSvihSlika = st.nextToken().trim();
					idZahteviZaPrijateljstvo = st.nextToken().trim();
					kImePrijatelja = st.nextToken().trim();
					privatan = st.nextToken().trim();
					obrisan = st.nextToken().trim();

					pol = Pol.valueOf(sPol);
					uloga = Uloga.valueOf(sUloga);

					slike = slikeDAO.pronadjiSlike(idSvihSlika);
					profilnaSlika = slikeDAO.pronadjiSliku(idProfilnaSlika);
					prijatelji = unesiPrijateljeSamoId(kImePrijatelja);

				}
				korisnici.put(korisnickoIme,
						new Korisnik(korisnickoIme, lozinka, email, ime, prezime, datumRodjenja, pol, uloga,
								profilnaSlika, objave, slike, zahtevi, prijatelji, Boolean.parseBoolean(privatan),
								Boolean.parseBoolean(obrisan)));
			}
			onUcitaniKorisnici(); // popunjavam prijatelje pravim objektima

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

	private List<Korisnik> unesiPrijateljeSamoId(String kImePrijatelji) {
		List<Korisnik> retList = new ArrayList<Korisnik>();
		StringTokenizer st = new StringTokenizer(kImePrijatelji, ",");
		String kIme = "";
		while (st.hasMoreTokens()) {
			kIme = st.nextToken().trim();
			Korisnik k = new Korisnik(kIme);
			retList.add(k);
		}
		return retList;
	}

	public void onUcitaneObjave(Collection<Objava> objave) {
		for (Objava objava : objave) {
			korisnici.get(objava.getKorisnik().getKorisnickoIme()).getObjave().add(objava);
		}
	}

	public void onUcitaniZahtevi(Collection<ZahtevZaPrijateljstvo> zahtevi) {
		for (ZahtevZaPrijateljstvo zahtev : zahtevi) {
			korisnici.get(zahtev.getPrimalac().getKorisnickoIme()).getZahteviZaPrijateljstvo().add(zahtev);
		}
	}

	public void onUcitaniKorisnici() {
		for (Korisnik k : korisnici.values()) {
			for (Korisnik prijatelj : k.getPrijatelji()) {
				prijatelj = korisnici.get(prijatelj.getKorisnickoIme());
			}
		}
	}

//	private List<Objava> unesiObjaveSamoId(String idSvihObjava) {
//		List<Objava> retList = new ArrayList<Objava>();
//		StringTokenizer st = new StringTokenizer(idSvihObjava, ",");
//		String id = "";
//		while (st.hasMoreTokens()) {
//			id = st.nextToken().trim();
//			Objava o = new Objava(id);
//			retList.add(o);
//		}
//		return retList;
//	}

//	private List<ZahtevZaPrijateljstvo> unesiZahteveSamoId(String idZahteviZaPrijateljstvo) {
//		List<ZahtevZaPrijateljstvo> retList = new ArrayList<ZahtevZaPrijateljstvo>();
//		StringTokenizer st = new StringTokenizer(idZahteviZaPrijateljstvo, ",");
//		String id = "";
//
//		while (st.hasMoreTokens()) {
//			id = st.nextToken().trim();
//			ZahtevZaPrijateljstvo z = new ZahtevZaPrijateljstvo(id);
//			retList.add(z);
//		}
//		return retList;
//	}

//	public Korisnik update(Korisnik k, Korisnik noviKorisnik) {
//		k.setLozinka(noviKorisnik.getLozinka());
//		k.setEmail(noviKorisnik.getEmail());
//		k.setIme(noviKorisnik.getIme());
//		k.setPrezime(noviKorisnik.getPrezime());
//		// sacuvaj u bazi
//		return k;
//	}
}
