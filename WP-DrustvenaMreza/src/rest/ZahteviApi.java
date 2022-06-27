package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.ZahtevZaPrijateljstvo;
import dao.KorisnikDAO;
import dao.ZahteviDAO;
import spark.Request;
import spark.Response;

public class ZahteviApi {
	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public static Object newFriendRequest(Request req, Response res, KorisnikDAO korisniciDAO, ZahteviDAO zahteviDAO) {
		res.type("application/json");
		ZahtevZaPrijateljstvo z = g.fromJson(req.body(), ZahtevZaPrijateljstvo.class);
		z.setPosiljalac(korisniciDAO.pronadjiKorisnika(z.getPosiljalac().getKorisnickoIme()));
		z.setPrimalac(korisniciDAO.pronadjiKorisnika(z.getPrimalac().getKorisnickoIme()));
		
		z = zahteviDAO.sacuvaj(z);
		if (z != null) {
			return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(z)));
		}
		else {
			return g.toJson(new StandardResponse(StatusResponse.ERROR, "Error while saving request!"));
		}
	}

}
