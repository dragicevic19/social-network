package rest;

import com.google.gson.JsonElement;

public class StandardResponse {

	private StatusResponse status;
	private String message;
	private JsonElement data;

	public StandardResponse() {
	}

	public StandardResponse(StatusResponse status, String message, JsonElement data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public StandardResponse(StatusResponse status, JsonElement data) {
		super();
		this.status = status;
		this.data = data;
	}

	public StandardResponse(StatusResponse status) {
		super();
		this.status = status;
	}

	public StandardResponse(StatusResponse status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public StatusResponse getStatus() {
		return status;
	}

	public void setStatus(StatusResponse status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JsonElement getData() {
		return data;
	}

	public void setData(JsonElement data) {
		this.data = data;
	}

}
