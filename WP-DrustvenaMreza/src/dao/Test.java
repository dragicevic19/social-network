package dao;

public class Test {

	public static void main(String[] args) {
		String dataPath = "./static/data";
		SlikeDAO slikeDAO = new SlikeDAO(dataPath);
		KorisnikDAO korisniciDAO = new KorisnikDAO(dataPath, slikeDAO);
		KomentariDAO komentariDAO = new KomentariDAO(dataPath, korisniciDAO);
		ObjaveDAO objaveDAO = new ObjaveDAO(dataPath, korisniciDAO, komentariDAO);
		ZahteviDAO zahteviDAO = new ZahteviDAO(dataPath, korisniciDAO);

		slikeDAO.ispisiSve();
		korisniciDAO.ispisiSve();
		komentariDAO.ispisiSve();
		objaveDAO.ispisiSve();
		zahteviDAO.ispisiSve();
	}

}
