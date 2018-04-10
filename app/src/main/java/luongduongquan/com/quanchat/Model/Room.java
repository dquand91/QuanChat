package luongduongquan.com.quanchat.Model;

public class Room {

	private String user_status;

	public Room(String user_status) {
		this.user_status = user_status;
	}

	public Room() {
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
}
