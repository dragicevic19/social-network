package beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
	
	public ZahtevZaPrijateljstvo(String id, Korisnik posiljalac, Korisnik primalac, Status status) {
		super();
		this.id = id;
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.status = status;
		try {
			this.datum = new SimpleDateFormat("dd/MM/yyyy").parse("12/12/2022");
		} catch (ParseException e) {
			e.printStackTrace();
		}
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

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	@Override
	public String toString() {
		return "ZahtevZaPrijateljstvo [id=" + id + ", posiljalac=" + posiljalac + ", primalac=" + primalac + ", status="
				+ status + ", datum=" + datum + "]";
	}

}
