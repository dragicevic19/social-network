package beans;

import java.util.Date;

public class DirektnaPoruka {
	private Korisnik posiljalac;
	private Korisnik primalac;
	private String sadrzajPoruke;
	private Date datum;

	public DirektnaPoruka() {
	}

	public DirektnaPoruka(Korisnik posiljalac, Korisnik primalac, String sadrzajPoruke, Date datum) {
		super();
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.sadrzajPoruke = sadrzajPoruke;
		this.datum = datum;
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

	public String getSadrzajPoruke() {
		return sadrzajPoruke;
	}

	public void setSadrzajPoruke(String sadrzajPoruke) {
		this.sadrzajPoruke = sadrzajPoruke;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}
}
