package rest;

import java.util.List;

import beans.Komentar;
import beans.Objava;
import dao.ObjaveDAO;

public class ObjaveService {

		ObjaveService(){
			
		}
		
		public static Objava update()
		{
			return null;
			
		}
		
		public static Objava addComment(Komentar k, ObjaveDAO objaveDAO, String objavaID)
		{
			Objava objava = objaveDAO.pronadjiObjavu(objavaID);
			List<Komentar> komentari = objava.getKomentari();
			komentari.add(k);
			objava.setKomentari(komentari);
			return objava;
			
		}
}
