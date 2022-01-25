package rest;

import com.google.gson.Gson;

import beans.Korisnik;
import beans.Uloga;
import dao.KorisnikDAO;
import spark.Request;
import spark.Response;
import spark.Session;

public class KorisniciApi {

	private static Gson g = new Gson();

	public static Object getCurrentUser(Request req, Response res) {
		res.type("application/json");
		Session ss = req.session(true);
		Korisnik k = ss.attribute("currentUser");
		if (k == null) {
			res.status(401);
			return g.toJson(new StandardResponse(StatusResponse.ERROR));
		}
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(k)));
	}

	public static Object login(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
		Session ss = req.session(true);
		if (k.getUloga() == Uloga.GOST) {
			ss.attribute("currentUser", k);
			res.redirect("./static/pocetnaGost.html");
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS,"pocetnaGost.html", g.toJsonTree(k)));
		}
		k = KorisniciService.login(k.getKorisnickoIme(), k.getLozinka(), korisniciDAO);
		if (k != null) {
			ss.attribute("currentUser", k);
			if (k.getUloga() == Uloga.KORISNIK) {
				return g.toJson(new StandardResponse(StatusResponse.SUCCESS, "pocetnaKorisnik.html", g.toJsonTree(k)));
			}
			else {
				return g.toJson(new StandardResponse(StatusResponse.SUCCESS, "pocetnaAdmin.html", g.toJsonTree(k)));
			}
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR));
		}
	}

	public static Object register(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
		Session ss = req.session(true);
		k = KorisniciService.register(k, korisniciDAO);
		if (k != null) {
			ss.attribute("currentUser", k);
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(k)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Username already exists!"));
		}
	}

	public static Object changePass(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
		k = KorisniciService.changePassword(k, korisniciDAO);
		if (k != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(k)));
		}
		else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Error while changing password!"));
		}
	}

}
