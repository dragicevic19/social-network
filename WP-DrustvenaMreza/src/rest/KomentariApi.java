package rest;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Komentar;
import beans.Korisnik;
import dao.KomentariDAO;
import dao.KorisnikDAO;
import dao.ObjaveDAO;
import dao.ZahteviDAO;
import dto.KomentariDTO;
import spark.Request;
import spark.Response;

public class KomentariApi {
	
	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	public static Object postKomment(Request req, Response res, KomentariDAO komentariDAO, KorisnikDAO korisnikDAO, ObjaveDAO objaveDAO) {
		res.type("application/json");
		KomentariDTO k = g.fromJson(req.body(), KomentariDTO.class);
		Komentar kom = new Komentar();
		kom.setKorisnik(korisnikDAO.pronadjiKorisnika(k.getKorisnik()));
		kom.setObrisan(false);
		kom.setTekst(k.getTekst());
		kom.setDatumKomentara(new Date());
		kom.setDatumIzmene(null);
		kom = komentariDAO.sacuvaj(kom);
		
		if (kom != null) {
			ObjaveService.addComment(kom, objaveDAO, k.getObjavaID());
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(kom)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists!"));
		}
	}
	public static Object deleteComment(Request req, Response res, KomentariDAO komentariDAO) {
		res.type("application/json");
		KomentariDTO k = g.fromJson(req.body(), KomentariDTO.class);
		Komentar kom = komentariDAO.pronadjiKomentar(k.getId());
		kom.setObrisan(true);
		kom = KomentariService.updateKomentar(kom, komentariDAO);
		
		if (kom != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(kom)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists!"));
		}
	}
}
