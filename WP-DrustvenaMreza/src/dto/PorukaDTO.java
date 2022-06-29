package dto;

import beans.DirektnaPoruka;

public class PorukaDTO {

	private String posiljalac;
	private String primalac;
	private String poruka;

	public PorukaDTO(DirektnaPoruka direktnaPoruka) {
		this.posiljalac = direktnaPoruka.getPosiljalac().getKorisnickoIme();
		this.primalac = direktnaPoruka.getPrimalac().getKorisnickoIme();
		this.poruka = direktnaPoruka.getSadrzajPoruke();
	}

	public PorukaDTO(String posiljalac, String poruka) {
		super();
		this.posiljalac = posiljalac;
		this.poruka = poruka;
	}

	public String getPosiljalac() {
		return posiljalac;
	}
	

	public String getPrimalac() {
		return primalac;
	}

	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}

	public void setPosiljalac(String posiljalac) {
		this.posiljalac = posiljalac;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

}
