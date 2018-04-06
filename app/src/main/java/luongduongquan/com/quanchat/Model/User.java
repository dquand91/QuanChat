package luongduongquan.com.quanchat.Model;

/**
 * Created by luong.duong.quan on 3/23/2018.
 */

public class User {

	private String UserName;

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	private String UserID;
	private String UserStatus;
	private String UserEmail;
	private String UserImage;
	private String UserThumb;
	private boolean IsOnline;

	public User(String userID,String userName, String userStatus, String userImage, boolean isOnline) {
		UserID = userID;
		UserName = userName;
		UserStatus = userStatus;
		UserImage = userImage;
		IsOnline = isOnline;

	}

	public User(String userID,String userName, String userStatus, String userImage) {
		UserID = userID;
		UserName = userName;
		UserStatus = userStatus;
		UserImage = userImage;
		IsOnline = false;
	}

	public User(){

	}

	public boolean isOnline() {
		return IsOnline;
	}

	public void setOnline(boolean online) {
		IsOnline = online;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserStatus() {
		return UserStatus;
	}

	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}

	public String getUserEmail() {
		return UserEmail;
	}

	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}

	public String getUserImage() {
		return UserImage;
	}

	public void setUserImage(String userImage) {
		UserImage = userImage;
	}

	public String getUserThumb() {
		return UserThumb;
	}

	public void setUserThumb(String userThumb) {
		UserThumb = userThumb;
	}
}
