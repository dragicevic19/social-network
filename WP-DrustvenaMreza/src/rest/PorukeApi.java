package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.DirektnaPoruka;
import beans.Korisnik;
import dao.KorisnikDAO;
import dao.PorukeDAO;
import dto.PorukaPayload;
import spark.Request;
import spark.Response;
import spark.Session;

public class PorukeApi {
	
	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();


	public static Object newMessage(Request req, Response res, PorukeDAO porukeDAO, KorisnikDAO korisniciDAO) {
		
		res.type("application/json");
		PorukaPayload p = g.fromJson(req.body(), PorukaPayload.class);
		DirektnaPoruka poruka = PorukeService.newMessage(p, porukeDAO, korisniciDAO);
		if (poruka != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(poruka)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Error while saving the message!"));
		}
	}
	
	

}
