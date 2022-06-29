package rest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.Korisnik;
import beans.Objava;
import dao.KorisnikDAO;
import dao.ObjaveDAO;
import spark.Request;
import spark.Response;

public class ObjaveApi {
	private static Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	
	public static Object getObjaveForUser(Request req, Response res, KorisnikDAO korisniciDAO, ObjaveDAO objaveDAO)
	{
		res.type("application/json");
		String username = req.queryParams("username");
		Korisnik k = korisniciDAO.pronadjiKorisnika(username);
		List<Objava> objave = new ArrayList<Objava>();
		
		for (String id : k.getObjave())
		{
			Objava objava = objaveDAO.pronadjiObjavu(id);
			objave.add(objava);
			
		}
		
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(objave)));
	}
	
	public static Object getObjava(Request req, Response res, ObjaveDAO objaveDAO)
	{
		res.type("application/json");
		String objavaID = req.queryParams("objavaID");
		Objava objava = objaveDAO.pronadjiObjavu(objavaID);
		
		return g.toJson(new StandardResponse(StatusResponse.SUCCESS, g.toJsonTree(objava)));
	}
}
