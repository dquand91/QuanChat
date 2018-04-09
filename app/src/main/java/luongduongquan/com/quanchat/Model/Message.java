package luongduongquan.com.quanchat.Model;

public class Message {

	private String id;
	private String body;
	private String type;
	private boolean seen;
	private long time;

	public Message(String id, String body, String type, boolean seen, long time) {
		this.id = id;
		this.body = body;
		this.type = type;
		this.seen = seen;
		this.time = time;
	}

	public Message() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
