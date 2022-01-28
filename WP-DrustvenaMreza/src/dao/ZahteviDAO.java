package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Korisnik;
import beans.Slika;
import beans.Status;
import beans.ZahtevZaPrijateljstvo;
import dto.ZahtevDTO;

public class ZahteviDAO {

	private HashMap<String, ZahtevZaPrijateljstvo> zahtevi = new HashMap<String, ZahtevZaPrijateljstvo>();
	private KorisnikDAO korisniciDAO;

	public ZahteviDAO() {

	}

	public ZahteviDAO(String contextPath, KorisnikDAO korisniciDAO) {
		this.korisniciDAO = korisniciDAO;
		ucitajZahteve(contextPath);
	}

	private void ucitajZahteve(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/zahtevi.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", idPoslao = "", idPrimio = "";
			Status status = null;
			Date datumZahteva = null;
			Korisnik poslao = null, primio = null;

			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					idPoslao = st.nextToken().trim();
					idPrimio = st.nextToken().trim();
					status = Status.valueOf(st.nextToken().trim());
					datumZahteva = new SimpleDateFormat("dd/MM/yyyy").parse(st.nextToken().trim());
					poslao = korisniciDAO.pronadjiKorisnika(idPoslao);
					primio = korisniciDAO.pronadjiKorisnika(idPrimio);
				}
				zahtevi.put(id, new ZahtevZaPrijateljstvo(id, poslao, primio, status, datumZahteva));
			}

			// korisniciDAO.onUcitaniZahtevi(zahtevi.values()); // da se popune zahtevi za
			// sve korisnike

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

	public ZahtevZaPrijateljstvo sacuvaj(ZahtevZaPrijateljstvo zahtev) {
		if (zahtevi.containsKey(zahtev.getId())) {
			return null; // greska
		}
		Integer maxId = -1;
		for (String id : zahtevi.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		zahtev.setId(maxId.toString());
		zahtevi.put(zahtev.getId(), zahtev);
		// DODAJ U FAJL
		return zahtev; // ok
	}

	public Collection<ZahtevZaPrijateljstvo> pronadjiSve() {
		return zahtevi.values();
	}

	public ZahtevZaPrijateljstvo pronadjiZahtev(String id) {
		return zahtevi.containsKey(id) ? zahtevi.get(id) : null;
	}

	public void ispisiSve() {
		for (ZahtevZaPrijateljstvo z : zahtevi.values()) {
			System.out.println(z);
		}
	}

	public List<ZahtevDTO> getZahteviNaCekanjuZaKorisnika(String korisnickoIme) {
		List<ZahtevDTO> retList = new ArrayList<ZahtevDTO>();
		for (ZahtevZaPrijateljstvo zahtev : zahtevi.values()) {
			if (zahtev.getPrimalac().getKorisnickoIme().equals(korisnickoIme)) {
				if (zahtev.getStatus() == Status.NA_CEKANJU) {
					ZahtevDTO z = new ZahtevDTO(zahtev.getId(), zahtev.getPosiljalac().getKorisnickoIme(),
							zahtev.getPosiljalac().getIme(), zahtev.getPosiljalac().getPrezime(),
							zahtev.getPosiljalac().getPrijatelji(), zahtev.getPosiljalac().getProfilnaSlika());
					retList.add(z);
				}
			}
		}
		return retList;
	}

	public List<ZahtevDTO> getPoslatiZahteviNaCekanjuZaKorisnika(String korisnickoIme) {
		List<ZahtevDTO> retList = new ArrayList<ZahtevDTO>();
		for (ZahtevZaPrijateljstvo zahtev : zahtevi.values()) {
			if (zahtev.getPosiljalac().getKorisnickoIme().equals(korisnickoIme)) {
				if (zahtev.getStatus() == Status.NA_CEKANJU) {
					ZahtevDTO z = new ZahtevDTO(zahtev.getId(), zahtev.getPrimalac().getKorisnickoIme(),
							zahtev.getPrimalac().getIme(), zahtev.getPrimalac().getPrezime(),
							zahtev.getPrimalac().getPrijatelji(), zahtev.getPrimalac().getProfilnaSlika());
					retList.add(z);
				}
			}
		}
		return retList;
	}

}
