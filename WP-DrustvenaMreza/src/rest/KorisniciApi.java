package rest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Korisnik;
import beans.Status;
import beans.Uloga;
import beans.ZahtevZaPrijateljstvo;
import dao.KorisnikDAO;
import dao.PorukeDAO;
import dao.ZahteviDAO;
import dto.GrupisanePorukeDTO;
import dto.RegistracijaKorisnikDTO;
import dto.UlogovaniKorisnikDTO;
import dto.ZahtevDTO;
import spark.Request;
import spark.Response;
import spark.Session;

public class KorisniciApi {

	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public static Object getCurrentUser(Request req, Response res, ZahteviDAO zahteviDAO, PorukeDAO porukeDAO) {
		res.type("application/json");
		Session ss = req.session(true);
		Korisnik k = ss.attribute("currentUser");
		
		if (k == null) {
			res.status(401);
			return g.toJson(new StandardResponse(StatusResponse.ERROR));
		}
		
		List<ZahtevDTO> zahtevi = zahteviDAO.getZahteviNaCekanjuZaKorisnika(k.getKorisnickoIme());
		List<ZahtevDTO> poslatiZahtevi = zahteviDAO.getPoslatiZahteviNaCekanjuZaKorisnika(k.getKorisnickoIme());
		List<List<ZahtevDTO>> list = new ArrayList<List<ZahtevDTO>>();
		list.add(zahtevi);
		list.add(poslatiZahtevi);
		
		List<GrupisanePorukeDTO> poruke = porukeDAO.getGrupisanePorukeZaKorisnika(k.getKorisnickoIme());
		UlogovaniKorisnikDTO korisnik = new  UlogovaniKorisnikDTO(k, list, poruke);
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(korisnik)));
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
		RegistracijaKorisnikDTO k = g.fromJson(req.body(), RegistracijaKorisnikDTO.class);
		Session ss = req.session(true);
		Korisnik newUser = KorisniciService.register(k, korisniciDAO);
		if (newUser != null) {
			ss.attribute("currentUser", newUser);
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(newUser)));
		} else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Username or Email already exists!"));
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
		List<ZahtevDTO> poslatiZahtevi = zahteviDAO.getPoslatiZahteviNaCekanjuZaKorisnika(username);
		List<List<ZahtevDTO>> list = new ArrayList<List<ZahtevDTO>>();
		list.add(zahtevi);
		list.add(poslatiZahtevi);
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(list)));
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

	public static Object removeFriend(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String userOne = req.queryParams("userOne");
		String userTwo = req.queryParams("userTwo");
		
		if (KorisniciService.removeFriend(userOne, userTwo, korisniciDAO)) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS));
		}
		else {
			res.status(400);
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Bad request!"));
		}
		
		
	}

	public static Object search(Request req, Response res, KorisnikDAO korisniciDAO) {
		res.type("application/json");
		String query = req.queryParams("query");
		String options = req.queryParams("options");
		try {
			List<Korisnik> searchRes = KorisniciService.search(query, options, korisniciDAO);
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(searchRes)));

		}
		catch(Exception e) {
			res.status(500);
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Internal server error!"));
		}
		
	}
}
