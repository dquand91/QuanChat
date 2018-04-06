package luongduongquan.com.quanchat.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import luongduongquan.com.quanchat.LastSeenTime;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;

public class ChatActivity extends AppCompatActivity {

	private String opposite_userID ;
	private String opposite_userName;
	private android.support.v7.widget.Toolbar toolbarChat;

	private TextView userNameTitle;
	private TextView userLastSendTitle;
	private ImageView userImageView;

	private DatabaseReference userReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		opposite_userID = getIntent().getExtras().get(Common.USERS_ID_TAG).toString();
		opposite_userName = getIntent().getExtras().get(Common.USER_NAME_TAG).toString();

		userReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(opposite_userID);

		toolbarChat = (Toolbar) findViewById(R.id.appBar_ChatActivity);
		setSupportActionBar(toolbarChat);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View actionBar_view = inflater.inflate(R.layout.chat_custom_bar, null);
		actionBar.setCustomView(actionBar_view);

		userImageView = findViewById(R.id.imgUser_chatAppBar);
		userNameTitle = findViewById(R.id.tvUserName_chatAppBar);
		userLastSendTitle = findViewById(R.id.tvLastSeen_chatAppBar);

		userNameTitle.setText(opposite_userName);
		userReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot.exists()){
					if(dataSnapshot.hasChild(Common.ONLINE_TAG)){
						String status = dataSnapshot.child(Common.ONLINE_TAG).getValue().toString();
						if(!status.isEmpty()){
							if(status.equals("true")){
								userLastSendTitle.setText("Active");
							} else if(status.equals("false")){
								userLastSendTitle.setText("Offline");
							} else{
								Long time_online = Long.parseLong(status);
								LastSeenTime getSeenTime = new LastSeenTime();
								status = getSeenTime.getTimeAgo(time_online, getBaseContext());
								userLastSendTitle.setText(status);
							}
						} else {
							Toast.makeText(ChatActivity.this, "Error...", Toast.LENGTH_SHORT).show();
						}

					}

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});





	}
}
