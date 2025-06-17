package communication;

import java.io.Serializable;

public class Response implements Serializable {
	private Object data;
	private Exception exception;
	public Response(Object data, Exception exception) {
		super();
		this.data = data;
		this.exception = exception;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}