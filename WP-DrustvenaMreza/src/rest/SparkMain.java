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

	private static Gson g = new Gson();

	public static void main(String[] args) throws IOException {
		port(8080);
		staticFiles.externalLocation(new File("./static").getCanonicalPath());

		get("/rest/test", (req, res) -> {
			return "radi";
		});

		get("/rest/korisnici/loggedIn", (req, res) -> {	// vraca trenutno registrovanog korisnika
			res.type("application/json");
			Session ss = req.session(true);
			Korisnik k = ss.attribute("ulogovaniKorisnik");
			if (k == null) {
				return false;
			}
			return g.toJson(k);
		});

		post("rest/korisnici/login", (req, res) -> {
			res.type("application/json");
			String kIme = req.queryParams("logKorisnicko");
			String lozinka = req.queryParams("logPass");
			Session ss = req.session(true);
			Korisnik k = KorisniciService.login(kIme, lozinka, korisniciDAO);
			if (k != null) {
				ss.attribute("ulogovaniKorisnik", k);
				return g.toJson(k);
			} else {
				return false;
			}
		});

		post("/rest/korisnici/register", (req, res) -> {
			res.type("application/json");
			Korisnik k = g.fromJson(req.body(), Korisnik.class);
			Session ss = req.session(true);
			k = KorisniciService.register(k, korisniciDAO);
			if (k != null) {
				ss.attribute("ulogovaniKorisnik", k);
				return g.toJson(k);
			} else {
				return null;
			}
		});
	}
}
