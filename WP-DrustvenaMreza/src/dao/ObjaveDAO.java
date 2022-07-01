package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Komentar;
import beans.Korisnik;
import beans.Objava;
import beans.Slika;

public class ObjaveDAO {

    private HashMap<String, Objava> objave = new HashMap<String, Objava>();
    private KomentariDAO komentariDAO;
    private KorisnikDAO korisniciDAO;

    public ObjaveDAO() {
    }

    public ObjaveDAO(String contextPath, KorisnikDAO korisniciDAO, KomentariDAO komentariDAO) {
        this.komentariDAO = komentariDAO;
        this.korisniciDAO = korisniciDAO;
        ucitajObjave(contextPath);
    }

    public Collection<Objava> pronadjiSve() {
        return objave.values();
    }

    public Objava pronadjiObjavu(String id) {
        return objave.containsKey(id) ? objave.get(id) : null;
    }

    public Objava sacuvaj(Objava objava, KorisnikDAO korisnikDAO) {
        if (objave.containsKey(objava.getId())) {
            return null; // greska
        }
        Integer maxId = -1;
        for (String id : objave.keySet()) {
            int idNum = Integer.parseInt(id);
            if (idNum > maxId) {
                maxId = idNum;
            }
        }
        maxId++;
        objava.setId(maxId.toString());
        objave.put(objava.getId(), objava);
        Korisnik korisnik = korisnikDAO.pronadjiKorisnika(objava.getKorisnik().getKorisnickoIme());
        List<String> korisnickeOBJ = korisnik.getObjave();
        korisnickeOBJ.add(objava.getId());
        korisnik.setObjave(korisnickeOBJ);
        // DODAJ U FAJL
        return objava; // ok
    }

    private void ucitajObjave(String contextPath) {
        BufferedReader in = null;
        try {
            File file = new File(contextPath + "/objave.csv");
            System.out.println(file.getCanonicalPath());
            in = new BufferedReader(new FileReader(file));
            String line, id = "", kIme = "", putanjaSlike = "", tekst = "", idKomentari = "", obrisana = "", isSlika ="";
            List<Komentar> komentari = new ArrayList<Komentar>();
            Korisnik k = null;

            StringTokenizer st;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;
                st = new StringTokenizer(line, ";");
                while (st.hasMoreTokens()) {
                    id = st.nextToken().trim();
                    kIme = st.nextToken().trim();
                    putanjaSlike = st.nextToken().trim();
                    putanjaSlike = (putanjaSlike.equals("/")) ? null : putanjaSlike; 
                    tekst = st.nextToken().trim();
                    idKomentari = st.nextToken().trim();
                    obrisana = st.nextToken().trim();
                    isSlika = st.nextToken().trim();
                    komentari = komentariDAO.pronadjiKomentare(idKomentari);
                    k = korisniciDAO.pronadjiKorisnika(kIme);
                }
                objave.put(id, new Objava(id, k, putanjaSlike, tekst, komentari, Boolean.parseBoolean(obrisana), Boolean.parseBoolean(isSlika)));
            }

         //   korisniciDAO.onUcitaneObjave(objave.values()); // da se popune objave za sve korisnike

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

	public void ispisiSve() {
		for (Objava o : objave.values()) {
			System.out.println(o);
		}
		
	}

}
