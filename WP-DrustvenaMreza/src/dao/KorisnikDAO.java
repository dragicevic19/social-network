package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.DirektnaPoruka;
import beans.Korisnik;
import beans.Objava;
import beans.Pol;
import beans.Slika;
import beans.Uloga;
import beans.ZahtevZaPrijateljstvo;

public class KorisnikDAO {

	private HashMap<String, Korisnik> korisnici = new HashMap<String, Korisnik>();
	private SlikeDAO slikeDAO;
	private String contextPath;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

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
	
	public Korisnik pronadjiKorisnikaPoEmail(String email) {
		for (Korisnik k : korisnici.values()) {
			if (k.getEmail().equalsIgnoreCase(email)) return k;
		}
		return null;
	}

	public Korisnik sacuvaj(Korisnik korisnik) {
		if (korisnici.containsKey(korisnik.getKorisnickoIme()))	return null;
		
		if (pronadjiKorisnikaPoEmail(korisnik.getEmail()) != null) return null;
		
		korisnici.put(korisnik.getKorisnickoIme(), korisnik);

		return upisiUFajl() ? korisnik : null;
	}
	
	public boolean upisiUFajl() {
		try {
			File csvFile = new File(contextPath + "/korisnici.csv");
			Writer upis = new BufferedWriter(new FileWriter(csvFile, false));
			
			for(Korisnik k : korisnici.values()) {
				upis.append(k.getKorisnickoIme());
				upis.append(";");
				upis.append(k.getLozinka());
				upis.append(";");
				upis.append(k.getEmail());
				upis.append(";");
				upis.append(k.getIme());
				upis.append(";");
				upis.append(k.getPrezime());
				upis.append(";");
				upis.append(df.format(k.getDatumRodjenja()));
				upis.append(";");
				upis.append(k.getPol().name());
				upis.append(";");
				upis.append(k.getUloga().name());
				upis.append(";");
				upis.append((k.getPrijatelji().size() == 0) ? "/" : String.join(",", k.getPrijatelji()));
				upis.append(";");
				upis.append((k.getProfilnaSlika() == null) ? "/" : k.getProfilnaSlika().getId());
				upis.append(";");
				if (k.getSlike().size() == 0) {
					upis.append("/");
				}
				else {
					String slike = "";
					for(Slika slika : k.getSlike()) {
						slike += slika.getId() + ",";
					}
					slike.substring(0, slike.length() - 1); // -2?
					upis.append(slike);
				}
				upis.append(";");
				
				upis.append((k.getObjave().size() == 0) ? "/" : String.join(",", k.getObjave()));
				upis.append(";");

				upis.append((k.getZahteviZaPrijateljstvo().size() == 0) ? "/" : String.join(",", k.getZahteviZaPrijateljstvo())); 
				upis.append(";");
				
				upis.append(String.valueOf(k.isPrivatan()));
				upis.append(";");
				upis.append(String.valueOf(k.isObrisan()));
				upis.append("\n");
			}
			
			upis.flush();
			upis.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void ucitajKorisnike(String contextPath) {
		this.contextPath = contextPath;
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

					if (!idSvihSlika.equals("/"))
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

	public List<Korisnik> pronadjiPrijateljeZaKorisnika(Korisnik k) {
		List<Korisnik> retList = new ArrayList<Korisnik>();
		if (k.getPrijatelji() == null) return retList;
		for (String kIme : k.getPrijatelji()) {
			retList.add(korisnici.get(kIme));
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
