package dto;


import java.util.Date;

import beans.Korisnik;

public class GrupisanePorukeDTO {
	
	private Korisnik posiljalac;
	private Korisnik primalac;
	private String poslednjaPoruka;
	private boolean withAdmin;
	
	public GrupisanePorukeDTO() {}
	
	public GrupisanePorukeDTO(Korisnik p, Korisnik prim, String poruka, boolean withAdmin) {
		this.posiljalac = p;
		this.primalac = prim;
		this.poslednjaPoruka = poruka;
		this.withAdmin = withAdmin;
	}
	
	

	public boolean isWithAdmin() {
		return withAdmin;
	}

	public void setWithAdmin(boolean withAdmin) {
		this.withAdmin = withAdmin;
	}

	public Korisnik getPosiljalac() {
		return posiljalac;
	}

	public void setPosiljalac(Korisnik posiljalac) {
		this.posiljalac = posiljalac;
	}

	public Korisnik getPrimalac() {
		return primalac;
	}

	public void setPrimalac(Korisnik primalac) {
		this.primalac = primalac;
	}

	public String getPoslednjaPoruka() {
		return poslednjaPoruka;
	}

	public void setPoslednjaPoruka(String poslednjaPoruka) {
		this.poslednjaPoruka = poslednjaPoruka;
	}
	
	
}
