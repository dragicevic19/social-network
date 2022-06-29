package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import beans.DirektnaPoruka;
import beans.Korisnik;
import beans.Status;
import beans.ZahtevZaPrijateljstvo;
import dto.GrupisanePorukeDTO;
import dto.PorukaDTO;
import dto.ZahtevDTO;

public class PorukeDAO {

	private Map<String, DirektnaPoruka> poruke = new LinkedHashMap<String, DirektnaPoruka>();
	private KorisnikDAO korisniciDAO;
	private String contextPath;

	public PorukeDAO() {

	}

	public PorukeDAO(String contextPath, KorisnikDAO korisniciDAO) {
		this.korisniciDAO = korisniciDAO;
		ucitajPoruke(contextPath);
	}

	private void ucitajPoruke(String contextPath) {
		this.contextPath = contextPath;
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/poruke.csv");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line, id = "", idPoslao = "", idPrimio = "", poruka = "";
			LocalDateTime datumSlanja = null;
			Korisnik poslao = null, primio = null;

			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					idPoslao = st.nextToken().trim();
					idPrimio = st.nextToken().trim();
					// datumSlanja = LocalDateTime.parse(st.nextToken().trim());
					
					poruka = st.nextToken().trim().replace("/ff", ",");
					poslao = korisniciDAO.pronadjiKorisnika(idPoslao);
					primio = korisniciDAO.pronadjiKorisnika(idPrimio);
				}
				poruke.put(id, new DirektnaPoruka(id, poslao, primio, poruka, datumSlanja));
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

	public DirektnaPoruka sacuvaj(DirektnaPoruka poruka) {
		if (poruke.containsKey(poruka.getId())) {
			return null; // greska
		}
		Integer maxId = -1;
		for (String id : poruke.keySet()) {
			int idNum = Integer.parseInt(id);
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		maxId++;
		poruka.setId(maxId.toString());
		poruke.put(poruka.getId(), poruka);

		upisiUFajl(poruka);
		return poruka; // ok
	}

	private void upisiUFajl(DirektnaPoruka poruka) {
		try {
			File csvFile = new File(contextPath + "/poruke.csv");
			Writer upis = new BufferedWriter(new FileWriter(csvFile, true));
			upis.append(poruka.getId());
			upis.append(",");
			upis.append(poruka.getPosiljalac().getKorisnickoIme());
			upis.append(",");
			upis.append(poruka.getPrimalac().getKorisnickoIme());
			upis.append(",");
			upis.append(poruka.getSadrzajPoruke().replace(",", "/ff"));
			upis.append("\n");
			upis.flush();
			upis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<DirektnaPoruka> pronadjiSve() {
		return poruke.values();
	}

	public DirektnaPoruka pronadjiPoruku(String id) {
		return poruke.containsKey(id) ? poruke.get(id) : null;
	}

	public void ispisiSve() {
		for (DirektnaPoruka p : poruke.values()) {
			System.out.println(p);
		}
	}

	public List<GrupisanePorukeDTO> getGrupisanePorukeZaKorisnika(String korisnickoIme) {
		List<GrupisanePorukeDTO> grupisanePoruke = new ArrayList<GrupisanePorukeDTO>();

		for (DirektnaPoruka poruka : poruke.values()) {
			Korisnik posiljalac = poruka.getPosiljalac();
			Korisnik primalac = poruka.getPrimalac();

			if (posiljalac.getKorisnickoIme().equals(korisnickoIme)
					|| primalac.getKorisnickoIme().equals(korisnickoIme)) {

				GrupisanePorukeDTO grupisana = findInGrupisane(posiljalac, primalac, grupisanePoruke);

				if (grupisana != null) {
					grupisana.setPosiljalac(poruka.getPosiljalac());
					grupisana.setPrimalac(poruka.getPrimalac());
					grupisana.setPoslednjaPoruka(poruka.getSadrzajPoruke());
				}

				else {
					grupisana = new GrupisanePorukeDTO(posiljalac, primalac, poruka.getSadrzajPoruke());
					grupisanePoruke.add(grupisana);
				}
			}
		}
		return grupisanePoruke;
	}

	private GrupisanePorukeDTO findInGrupisane(Korisnik posiljalac, Korisnik primalac,
			List<GrupisanePorukeDTO> grupisanePoruke) {

		for (GrupisanePorukeDTO grupisana : grupisanePoruke) {
			if (grupisana.getPrimalac().getKorisnickoIme().equals(primalac.getKorisnickoIme())
					&& grupisana.getPosiljalac().getKorisnickoIme().equals(posiljalac.getKorisnickoIme())) {
				return grupisana;
			}

			if (grupisana.getPrimalac().getKorisnickoIme().equals(posiljalac.getKorisnickoIme())
					&& grupisana.getPosiljalac().getKorisnickoIme().equals(primalac.getKorisnickoIme())) {
				return grupisana;
			}
		}
		return null;
	}

	public List<PorukaDTO> getChat(Korisnik userOne, Korisnik userTwo) {

		List<PorukaDTO> retPoruke = new ArrayList<PorukaDTO>();

		for (DirektnaPoruka poruka : poruke.values()) {
			if (poruka.getPosiljalac().getKorisnickoIme().equals(userOne.getKorisnickoIme())
					&& poruka.getPrimalac().getKorisnickoIme().equals(userTwo.getKorisnickoIme())) {
				retPoruke.add(new PorukaDTO(poruka));
			}

			else if (poruka.getPosiljalac().getKorisnickoIme().equals(userTwo.getKorisnickoIme())
					&& poruka.getPrimalac().getKorisnickoIme().equals(userOne.getKorisnickoIme())) {
				retPoruke.add(new PorukaDTO(poruka));
			}

		}

		return retPoruke;

	}
}
