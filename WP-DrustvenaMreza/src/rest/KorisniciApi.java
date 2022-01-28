package rest;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Korisnik;
import beans.Status;
import beans.Uloga;
import beans.ZahtevZaPrijateljstvo;
import dao.KorisnikDAO;
import dao.ZahteviDAO;
import dto.ZahtevDTO;
import spark.Request;
import spark.Response;
import spark.Session;

public class KorisniciApi {

	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

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
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, "pocetnaGost.html", g.toJsonTree(k)));
		}
		k = KorisniciService.login(k.getKorisnickoIme(), k.getLozinka(), korisniciDAO);
		if (k != null) {
			ss.attribute("currentUser", k);
			if (k.getUloga() == Uloga.KORISNIK) {
				return g.toJson(new StandardResponse(StatusResponse.SUCCESS, "pocetnaKorisnik.html", g.toJsonTree(k)));
			} else {
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
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Error while changing password!"));
		}
	}

	public static Object update(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		Korisnik k = g.fromJson(req.body(), Korisnik.class);
		k = KorisniciService.update(k, korisniciDAO);
		if (k != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(k)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Error while updating user!"));
		}
	}

	public static Object logout(Request req, Response res) {
		res.type("application/json");
		Session ss = req.session(true);
		Korisnik korisnik = ss.attribute("currentUser");

		if (korisnik != null) {
			ss.invalidate();
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS));
		}
		return g.toJson(new StandardResponse(StatusResponse.ERROR, "Logout error"));
	}

	public static Object getFriendRequestsForUser(Request req, Response res, ZahteviDAO zahteviDAO) {
		res.type("application/json");
		String username = req.queryParams("username");
		List<ZahtevDTO> zahtevi = zahteviDAO.getZahteviNaCekanjuZaKorisnika(username);

		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(zahtevi)));
	}

	public static Object acceptFriendRequest(Request req, Response res, ZahteviDAO zahteviDAO,
			KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String idZahteva = req.queryParams("id");
		ZahtevZaPrijateljstvo zahtev = zahteviDAO.pronadjiZahtev(idZahteva);
		KorisniciService.acceptRequest(zahtev);

//		List<ZahtevDTO> zahtevi = zahteviDAO.getZahteviNaCekanjuZaKorisnika(zahtev.getPrimalac().getKorisnickoIme());
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS));
	}

	public static Object declineFriendRequest(Request req, Response res, ZahteviDAO zahteviDAO,
			KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String idZahteva = req.queryParams("id");
		ZahtevZaPrijateljstvo zahtev = zahteviDAO.pronadjiZahtev(idZahteva);
		zahtev.setStatus(Status.ODBIJENO);

//		List<ZahtevDTO> zahtevi = zahteviDAO.getZahteviNaCekanjuZaKorisnika(zahtev.getPrimalac().getKorisnickoIme());
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS));
	}

	public static Object getFriendsForUser(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String username = req.queryParams("username");
		List<Korisnik> prijatelji = KorisniciService.getFriendsForUser(username, korisniciDAO);
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(prijatelji)));
	}
	
	public static Object getUserForUsername(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String username = req.queryParams("username");
		Korisnik k = korisniciDAO.pronadjiKorisnika(username);
		if (k != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(k)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "User not found!"));
		}
	}

	public static Object getMutualFriends(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String usernameOne = req.queryParams("userOne");
		String usernameTwo = req.queryParams("userTwo");
		
		List<Korisnik> zajednickiPrijatelji = KorisniciService.getMutualFriends(usernameOne, usernameTwo, korisniciDAO);
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(zajednickiPrijatelji)));
	}
}
