package communication;

public class Request {
	private Operation op;
	private Object arg;
	public Operation getOp() {
		return op;
	}
	public void setOp(Operation op) {
		this.op = op;
	}
	public Object getArg() {
		return arg;
	}
	public void setArg(Object arg) {
		this.arg = arg;
	}
	public Request(Operation op, Object arg) {
		super();
		this.op = op;
		this.arg = arg;
	}
	
}
