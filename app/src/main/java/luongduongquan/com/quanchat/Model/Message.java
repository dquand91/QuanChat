package luongduongquan.com.quanchat.Model;

public class Message {

	private String body;
	private String type;
	private boolean seen;
	private long time;
	private String from;

	public Message( String body, String type, boolean seen, long time, String from) {

		this.body = body;
		this.type = type;
		this.seen = seen;
		this.time = time;
		this.from = from;
	}

	public Message() {
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}


	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
