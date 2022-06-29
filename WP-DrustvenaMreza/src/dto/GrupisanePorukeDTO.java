package dto;


import java.util.Date;

import beans.Korisnik;

public class GrupisanePorukeDTO {
	
	private Korisnik posiljalac;
	private Korisnik primalac;
	private String poslednjaPoruka;

	public GrupisanePorukeDTO() {}
	
	public GrupisanePorukeDTO(Korisnik p, Korisnik prim, String poruka) {
		this.posiljalac = p;
		this.primalac = prim;
		this.poslednjaPoruka = poruka;
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
