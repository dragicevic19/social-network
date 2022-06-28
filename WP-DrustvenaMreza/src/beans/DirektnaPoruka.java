package beans;

import java.time.LocalDateTime;
import java.util.Date;

public class DirektnaPoruka {
	private String id;
	private Korisnik posiljalac;
	private Korisnik primalac;
	private String sadrzajPoruke;
	//private LocalDateTime datum;

	public DirektnaPoruka() {
	}

	public DirektnaPoruka(String id, Korisnik posiljalac, Korisnik primalac, String sadrzajPoruke, LocalDateTime datum) {
		super();
		this.id = id;
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.sadrzajPoruke = sadrzajPoruke;
		//this.datum = datum;
	}
	
	public DirektnaPoruka(Korisnik posiljalac, Korisnik primalac, String sadrzajPoruke, LocalDateTime datum) {
		super();
		this.id = "";
		this.posiljalac = posiljalac;
		this.primalac = primalac;
		this.sadrzajPoruke = sadrzajPoruke;
//		this.datum = datum;
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

	public String getSadrzajPoruke() {
		return sadrzajPoruke;
	}

	public void setSadrzajPoruke(String sadrzajPoruke) {
		this.sadrzajPoruke = sadrzajPoruke;
	}

//	public LocalDateTime getDatum() {
//		return datum;
//	}
//
//	public void setDatum(LocalDateTime datum) {
//		this.datum = datum;
//	}
}
