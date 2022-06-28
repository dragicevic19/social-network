package rest;

import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.File;
import java.io.IOException;

import dao.KomentariDAO;
import dao.KorisnikDAO;
import dao.ObjaveDAO;
import dao.PorukeDAO;
import dao.SlikeDAO;
import dao.ZahteviDAO;
import ws.WsHandler;

public class SparkMain {
	static String dataPath = "./static/data";
	private static SlikeDAO slikeDAO = new SlikeDAO(dataPath);
	private static KorisnikDAO korisniciDAO = new KorisnikDAO(dataPath, slikeDAO);
	private static KomentariDAO komentariDAO = new KomentariDAO(dataPath, korisniciDAO);
	private static ObjaveDAO objaveDAO = new ObjaveDAO(dataPath, korisniciDAO, komentariDAO);
	private static ZahteviDAO zahteviDAO = new ZahteviDAO(dataPath, korisniciDAO);
	private static PorukeDAO porukeDAO = new PorukeDAO(dataPath, korisniciDAO);

	public static void main(String[] args) throws IOException {
		port(8080);
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		webSocket("/ws", WsHandler.class);

		post("/rest/korisnici/register", (req, res) -> KorisniciApi.register(req, res, korisniciDAO));

		get("/rest/korisnici/loggedIn", (req, res) -> KorisniciApi.getCurrentUser(req, res, zahteviDAO, porukeDAO));

		post("/rest/korisnici/login", (req, res) -> KorisniciApi.login(req, res, korisniciDAO));
		
		put("/rest/korisnici/changePass", (req, res) -> KorisniciApi.changePass(req, res, korisniciDAO));
		
		put("/rest/korisnici/update", (req,res) -> KorisniciApi.update(req, res, korisniciDAO));
		
		get("/rest/korisnici/logout", (req,res) -> KorisniciApi.logout(req, res));
		
		get("rest/korisnici/friendRequests", (req,res) -> KorisniciApi.getFriendRequestsForUser(req, res, zahteviDAO));
		
		get("rest/korisnici/acceptFriendRequest", (req,res) -> KorisniciApi.acceptFriendRequest(req,res,zahteviDAO, korisniciDAO));
		
		get("rest/korisnici/declineFriendRequest", (req,res) -> KorisniciApi.declineFriendRequest(req,res,zahteviDAO, korisniciDAO));
		
		get("rest/korisnici/friends", (req,res) -> KorisniciApi.getFriendsForUser(req, res, korisniciDAO));
		
		get("rest/korisnici/user", (req,res) -> KorisniciApi.getUserForUsername(req, res, korisniciDAO));
		
		get("rest/korisnici/mutualFriends", (req,res) -> KorisniciApi.getMutualFriends(req, res, korisniciDAO));
		
		post("rest/zahtevi/", (req,res)-> ZahteviApi.newFriendRequest(req, res, korisniciDAO, zahteviDAO));
		
		get("rest/korisnici/removeFriend", (req,res)-> KorisniciApi.removeFriend(req, res, korisniciDAO));

		get("rest/korisnici/search", (req, res) -> KorisniciApi.search(req, res, korisniciDAO));
		
		
		
		post("rest/poruke" , (req, res) -> PorukeApi.newMessage(req, res, porukeDAO, korisniciDAO));
		
		get("rest/poruke/razgovor", (req, res) -> PorukeApi.getChat(req, res, porukeDAO, korisniciDAO));
	}
}
