package beans;

import java.util.Date;

public class ZahtevZaPrijateljstvo {
	private String id;
	private Korisnik posiljalac;
	private Korisnik primalac;
	private Status status;
	private Date datum;

	public ZahtevZaPrijateljstvo() {
	}

	public ZahtevZaPrijateljstvo(String id) {
		this.id = id;
		this.posiljalac = null;
		this.primalac = null;
		this.status = null;
		this.datum = null;
	}

	public ZahtevZaPrijateljstvo(String id, Korisnik posiljalac, Korisnik primalac, Status status, Date datum) {
		super();
		this.id = id;
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.status = status;
		this.datum = datum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "ZahtevZaPrijateljstvo [id=" + id + ", posiljalac=" + posiljalac + ", primalac=" + primalac + ", status="
				+ status + ", datum=" + datum + "]";
	}

}
