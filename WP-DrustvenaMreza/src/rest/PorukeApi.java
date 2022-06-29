package rest;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.DirektnaPoruka;
import beans.Korisnik;
import dao.KorisnikDAO;
import dao.PorukeDAO;
import dto.PorukaDTO;
import dto.PorukaPayload;
import spark.Request;
import spark.Response;
import spark.Session;

public class PorukeApi {

	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public static Object newMessage(Request req, Response res, PorukeDAO porukeDAO, KorisnikDAO korisniciDAO) {

		res.type("application/json");
		PorukaPayload p = g.fromJson(req.body(), PorukaPayload.class);
		
		Korisnik user = korisniciDAO.pronadjiKorisnika(p.getPosiljalac());
		
		Session ss = req.session(true);
		Korisnik k = ss.attribute("currentUser");
		
		if (k == null || !k.getKorisnickoIme().equals(user.getKorisnickoIme())) {
			res.status(401);
			return g.toJson(new StandardResponse(StatusResponse.ERROR));
		}
		
		DirektnaPoruka poruka = PorukeService.newMessage(p, porukeDAO, korisniciDAO);
		if (poruka != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(poruka)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Error while saving the message!"));
		}
	}

	public static Object getChat(Request req, Response res, PorukeDAO porukeDAO, KorisnikDAO korisniciDAO) {
		res.type("application/json");

		String one = req.queryParams("userOne");
		String two = req.queryParams("userTwo");
		Korisnik userOne = korisniciDAO.pronadjiKorisnika(one);
		Korisnik userTwo = korisniciDAO.pronadjiKorisnika(two);
		
		Session ss = req.session(true);
		Korisnik k = ss.attribute("currentUser");
		
		if (k == null || !k.getKorisnickoIme().equals(userOne.getKorisnickoIme())) {
			res.status(401);
			return g.toJson(new StandardResponse(StatusResponse.ERROR));
		}

		List<PorukaDTO> poruke = PorukeService.getChat(userOne, userTwo, porukeDAO);

		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(poruke)));
	}

}
