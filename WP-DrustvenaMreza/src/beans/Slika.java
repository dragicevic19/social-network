package beans;

public class Slika {
	private String id;
	private String putanja;
	private boolean obrisana;

	public Slika() {
	}

	public Slika(String id) {
		this.id = id;
	}

	public Slika(String id, String putanja, boolean obrisana) {
		super();
		this.id = id;
		this.putanja = putanja;
		this.obrisana = obrisana;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPutanja() {
		return putanja;
	}

	public void setPutanja(String putanja) {
		this.putanja = putanja;
	}

	public boolean isObrisana() {
		return obrisana;
	}

	public void setObrisana(boolean obrisana) {
		this.obrisana = obrisana;
	}

}
