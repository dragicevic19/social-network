package rest;

import static spark.Spark.staticFiles;
import static spark.Spark.port;
import static spark.Spark.path;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import beans.Korisnik;
import dao.KomentariDAO;
import dao.KorisnikDAO;
import dao.ObjaveDAO;
import dao.SlikeDAO;
import dao.ZahteviDAO;
import spark.Session;

public class SparkMain {
	static String dataPath = "./static/data";
	private static SlikeDAO slikeDAO = new SlikeDAO(dataPath);
	private static KorisnikDAO korisniciDAO = new KorisnikDAO(dataPath, slikeDAO);
	private static KomentariDAO komentariDAO = new KomentariDAO(dataPath, korisniciDAO);
	private static ObjaveDAO objaveDAO = new ObjaveDAO(dataPath, korisniciDAO, komentariDAO);
	private static ZahteviDAO zahteviDAO = new ZahteviDAO(dataPath, korisniciDAO);

	public static void main(String[] args) throws IOException {
		port(8080);
		staticFiles.externalLocation(new File("./static").getCanonicalPath());

		post("/rest/korisnici/register", (req, res) -> KorisniciApi.register(req, res, korisniciDAO));

		get("/rest/korisnici/loggedIn", (req, res) -> KorisniciApi.getCurrentUser(req, res));

		post("/rest/korisnici/login", (req, res) -> KorisniciApi.login(req, res, korisniciDAO));

//		get("/rest/test", (req, res) -> {
//		return "radi";
//	});
	}
}
