package dto;

public class PorukaPayload {
	
	private String posiljalac;
	private String primalac;
	private String poruka;
	
	public PorukaPayload() {}
	
	public PorukaPayload(String posiljalac, String primalac, String poruka) {
		super();
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.poruka = poruka;
	}
	
	public String getPosiljalac() {
		return posiljalac;
	}
	public void setPosiljalac(String posiljalac) {
		this.posiljalac = posiljalac;
	}
	public String getPrimalac() {
		return primalac;
	}
	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}
	public String getPoruka() {
		return poruka;
	}
	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}
	
	


}
