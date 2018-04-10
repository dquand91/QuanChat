package luongduongquan.com.quanchat.Model;

public class RequestFriend {

	String request_type;

	public RequestFriend() {
	}

	public RequestFriend(String request_type) {
		this.request_type = request_type;
	}

	public String getRequest_type() {
		return request_type;
	}

	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}
}
