package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Slika;

public class SlikeDAO {

	private HashMap<String, Slika> slike = new HashMap<String, Slika>();

	public SlikeDAO() {
	}

	public SlikeDAO(String contextPath) {
		ucitajSlike(contextPath);
	}

	private void ucitajSlike(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/slike.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", putanjaSlike = "", obrisana = "";

			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					putanjaSlike = st.nextToken().trim();
					obrisana = st.nextToken().trim();
				}
				slike.put(id, new Slika(id, putanjaSlike, Boolean.parseBoolean(obrisana)));
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

	public Collection<Slika> pronadjiSve() {
		return slike.values();
	}

	public Slika pronadjiSliku(String id) {
		return slike.containsKey(id) ? slike.get(id) : null;
	}

	public List<Slika> pronadjiSlike(String idSvihSlika) { // idSvihSlika="23,4,5"
		List<Slika> retList = new ArrayList<Slika>();
		StringTokenizer st = new StringTokenizer(idSvihSlika, ",");
		String id = "";

		while (st.hasMoreTokens()) {
			id = st.nextToken().trim();
			Slika s = pronadjiSliku(id);
			if (s != null)
				retList.add(s);
		}
		return retList;
	}

	public Slika sacuvaj(Slika slika) {
		if (slike.containsKey(slika.getId())) {
			return null; // greska
		}
		Integer maxId = -1;
		for (String id : slike.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		slika.setId(maxId.toString());
		slike.put(slika.getId(), slika);
		// DODAJ U FAJL
		return slika; // ok
	}

	public void ispisiSve() {
		for (Slika s : slike.values()) {
			System.out.println(s);
		}
		
	}

}
