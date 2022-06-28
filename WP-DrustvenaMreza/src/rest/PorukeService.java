package rest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import beans.DirektnaPoruka;
import beans.Korisnik;
import dao.KorisnikDAO;
import dao.PorukeDAO;
import dto.PorukaDTO;
import dto.PorukaPayload;

public class PorukeService {

	public static DirektnaPoruka newMessage(PorukaPayload p, PorukeDAO porukeDAO, KorisnikDAO korisniciDAO) {
		Korisnik posiljalac = korisniciDAO.pronadjiKorisnika(p.getPosiljalac());
		Korisnik primalac = korisniciDAO.pronadjiKorisnika(p.getPrimalac());
		
		DirektnaPoruka poruka = new DirektnaPoruka(posiljalac, primalac, p.getPoruka(), LocalDateTime.now());
		
		return porukeDAO.sacuvaj(poruka);
	}

	public static List<PorukaDTO> getChat(Korisnik userOne, Korisnik userTwo, PorukeDAO porukeDAO) {
		
		List<PorukaDTO> poruke = porukeDAO.getChat(userOne, userTwo);
		return poruke;
		
	}

	
	
}
