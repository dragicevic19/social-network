package rest;

import java.util.List;

import beans.Komentar;
import beans.Korisnik;
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

			upObj.setSlika(objava.isSlika());

			objaveDAO.upisiUFajl();

			return upObj;
		}
		
		public static Objava addComment(Komentar k, ObjaveDAO objaveDAO, String objavaID)
		{
			Objava objava = objaveDAO.pronadjiObjavu(objavaID);
			objava.getKomentari().add(k);
			objaveDAO.upisiUFajl();
			return objava;
		}

		public static Objava makePost(ObjavaDTO objavaDTO, ObjaveDAO objaveDAO, KorisnikDAO korisnikDAO) {
			Objava objava = new Objava();
			Korisnik korisnik = korisnikDAO.pronadjiKorisnika(objavaDTO.getKorisnickoIme());
			objava.setKorisnik(korisnik);
			objava.setObrisana(false);
			objava.setSlika(false);
			objava.setSlika(objavaDTO.getSlika().equals("/") ? null : objavaDTO.getSlika());
			objava.setTekst(objavaDTO.getTekst());
			Objava novaObjava = objaveDAO.sacuvaj(objava, korisnikDAO);
			korisnik.getObjave().add(novaObjava.getId());
			korisnikDAO.upisiUFajl();
			
			return objava;
		}

		public static Objava makeSlika(ObjavaDTO objavaDTO, ObjaveDAO objaveDAO, KorisnikDAO korisnikDAO) {
			Objava objava = new Objava();
			Korisnik korisnik = korisnikDAO.pronadjiKorisnika(objavaDTO.getKorisnickoIme());
			objava.setKorisnik(korisnik);
			objava.setObrisana(false);
			objava.setSlika(true);
			objava.setSlika(objavaDTO.getSlika());
			objava.setTekst(objavaDTO.getTekst().equals("/") ? null : objavaDTO.getTekst());
			Objava novaObjava = objaveDAO.sacuvaj(objava, korisnikDAO);
			korisnik.getObjave().add(novaObjava.getId());
			korisnikDAO.upisiUFajl();
			
			return objava;
		}
}
