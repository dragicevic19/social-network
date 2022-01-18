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
	private ObjaveDAO objaveDAO;
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

	public KorisnikDAO(String contextPath, ObjaveDAO o, SlikeDAO s) {
		this.objaveDAO = o;
		this.slikeDAO = s;
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
					idProfilnaSlika = "", idObjave = "", idSlike = "", idZahteviZaPrijateljstvo = "", idPrijatelji = "",
					privatan = "", obrisan = "";

			Date datumRodjenja = new Date();
			Pol pol = Pol.MUSKI;
			Uloga uloga = Uloga.KORISNIK;
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
					idObjave = st.nextToken().trim();
					idSlike = st.nextToken().trim();
					idZahteviZaPrijateljstvo = st.nextToken().trim();
					idPrijatelji = st.nextToken().trim();
					privatan = st.nextToken().trim();
					obrisan = st.nextToken().trim();

					pol = Pol.valueOf(sPol);
					uloga = Uloga.valueOf(sUloga);
					slike = slikeDAO.pronadjiSlike(idSlike);
					profilnaSlika = slikeDAO.pronadjiSliku(idProfilnaSlika, slike);
					objave = objaveDAO.pronadjiObjave(idObjave);
					// zahtevi = zahteviDAO.pronadjiZahteve(idZahteviZaPrijateljstvo);
					// ostavljam sad praznu listu koja ce se popuniti kad se ucitaju zahtevi u
					// ZahteviDAO

					prijatelji = pronadjiKorisnike(idPrijatelji);

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

	private List<Korisnik> pronadjiKorisnike(String kImePrijatelja) { // kImePrijatelja = pera,mika,djura
		List<Korisnik> retList = new ArrayList<Korisnik>();
		StringTokenizer st = new StringTokenizer(kImePrijatelja, ",");
		String korisnickoIme = "";

		while (st.hasMoreTokens()) {
			korisnickoIme = st.nextToken().trim();
			Korisnik k = pronadjiKorisnika(korisnickoIme);
			retList.add(k);
		}

		return retList;
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
