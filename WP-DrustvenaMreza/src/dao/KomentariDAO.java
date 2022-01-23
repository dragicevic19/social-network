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

import beans.Komentar;
import beans.Korisnik;

public class KomentariDAO {

	private HashMap<String, Komentar> komentari = new HashMap<String, Komentar>();
	private KorisnikDAO korisniciDAO;

	public KomentariDAO() {
	}

	public KomentariDAO(String contextPath, KorisnikDAO korisniciDAO) {
		this.korisniciDAO = korisniciDAO;
		ucitajKomentare(contextPath);
	}

	private void ucitajKomentare(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/komentari.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", kImeKorisnika = "", tekst = "", obrisana = "";
			Date datumKomentara = null, datumIzmene = null;
			Korisnik korisnik = null;

			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					kImeKorisnika = st.nextToken().trim();
					tekst = st.nextToken().trim();
					datumKomentara = new SimpleDateFormat("dd/MM/yyyy").parse(st.nextToken().trim());
					String datumIzmeneStr = st.nextToken().trim();
					if (datumIzmeneStr.length() != 1)
						datumIzmene = new SimpleDateFormat("dd/MM/yyyy").parse(datumIzmeneStr);
					obrisana = st.nextToken().trim();

					korisnik = korisniciDAO.pronadjiKorisnika(kImeKorisnika);
				}
				komentari.put(id,
						new Komentar(id, korisnik, tekst, datumKomentara, datumIzmene, Boolean.parseBoolean(obrisana)));
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

	public List<Komentar> pronadjiKomentare(String idKomentari) { // idKomentari = 1,2,3,4
		List<Komentar> retList = new ArrayList<Komentar>();
		StringTokenizer st = new StringTokenizer(idKomentari, ",");
		String id = "";

		while (st.hasMoreTokens()) {
			id = st.nextToken().trim();
			Komentar k = pronadjiKomentar(id);
			retList.add(k);
		}
		return retList;
	}

	private Komentar pronadjiKomentar(String id) {
		return komentari.containsKey(id) ? komentari.get(id) : null;
	}

	public Collection<Komentar> pronadjiSve() {
		return komentari.values();
	}

	public void ispisiSve() {
		for (Komentar k : komentari.values()) {
			System.out.println(k);
		}
	}

	public Komentar sacuvaj(Komentar komentar) {
		if (komentari.containsKey(komentar.getId())) {
			return null; // greska
		}
		Integer maxId = -1;
		for (String id : komentari.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		komentar.setId(maxId.toString());
		komentari.put(komentar.getId(), komentar);
		// DODAJ U FAJL
		return komentar; // ok
	}

}
