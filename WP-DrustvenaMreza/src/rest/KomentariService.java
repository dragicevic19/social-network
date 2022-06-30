package rest;

import beans.Komentar;
import dao.KomentariDAO;

public class KomentariService {
	
	public KomentariService(){
		
	}
	
	public static Komentar updateKomentar(Komentar k, KomentariDAO komentariDAO)
	{
		Komentar komentar = komentariDAO.pronadjiKomentar(k.getId());
		komentar.setDatumIzmene(k.getDatumIzmene());
		komentar.setDatumKomentara(k.getDatumKomentara());
		komentar.setKorisnik(k.getKorisnik());
		komentar.setObrisan(k.isObrisan());
		komentar.setTekst(k.getTekst());
		return k;
		
	}
	
}
