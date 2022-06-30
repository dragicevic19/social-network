package rest;

import java.util.List;

import beans.Komentar;
import beans.Objava;
import dao.KorisnikDAO;
import dao.ObjaveDAO;
import dto.ObjavaDTO;

public class ObjaveService {

		ObjaveService(){
			
		}
		
		public static Objava update(Objava objava, ObjaveDAO objaveDAO)
		{
			Objava upObj = objaveDAO.pronadjiObjavu(objava.getId());
			upObj.setKomentari(objava.getKomentari());
			upObj.setKorisnik(objava.getKorisnik());
			upObj.setObrisana(objava.isObrisana());
			upObj.setSlika(objava.getSlika());
			upObj.setTekst(objava.getTekst());
			return upObj;
			
		}
		
		public static Objava addComment(Komentar k, ObjaveDAO objaveDAO, String objavaID)
		{
			Objava objava = objaveDAO.pronadjiObjavu(objavaID);
			List<Komentar> komentari = objava.getKomentari();
			komentari.add(k);
			objava.setKomentari(komentari);
			return objava;
			
		}

		public static Objava makePost(ObjavaDTO objavaDTO, ObjaveDAO objaveDAO, KorisnikDAO korisnikDAO) {
			Objava objava = new Objava();
			objava.setKorisnik(korisnikDAO.pronadjiKorisnika(objavaDTO.getKorisnickoIme()));
			objava.setObrisana(false);
			objava.setSlika(objavaDTO.getSlika());
			objava.setTekst(objavaDTO.getTekst());
			objaveDAO.sacuvaj(objava, korisnikDAO);
			
			return objava;
		}
}
