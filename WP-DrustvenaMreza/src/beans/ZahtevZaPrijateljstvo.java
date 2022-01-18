package beans;

import java.util.Date;

public class ZahtevZaPrijateljstvo {
	private Korisnik posiljalac;
	private Korisnik primalac;
	private Status status;
	private Date datum;

	public ZahtevZaPrijateljstvo() {
	}

	public ZahtevZaPrijateljstvo(Korisnik posiljalac, Korisnik primalac, Status status, Date datum) {
		super();
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.status = status;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
