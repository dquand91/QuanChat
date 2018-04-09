package luongduongquan.com.quanchat.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import luongduongquan.com.quanchat.LastSeenTime;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;

public class ChatActivity extends AppCompatActivity {

	private static final String TAG = "ChatActivity";

	private String remote_userID ;
	private String remote_userName;
	private android.support.v7.widget.Toolbar toolbarChat;

	private TextView userNameTitle;
	private TextView userLastSendTitle;
	private ImageView userImageView;

	private ImageView btnAddPhoto;
	private ImageView btnSend;
	private EditText edtInput;

	private DatabaseReference userReference;
	private DatabaseReference messageReference;
	private FirebaseAuth mAuth;
	private String localUserID;
	private RecyclerView recyclerViewMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mAuth = FirebaseAuth.getInstance();
		localUserID = mAuth.getCurrentUser().getUid();

		remote_userID = getIntent().getExtras().get(Common.USERS_ID_TAG).toString();
		remote_userName = getIntent().getExtras().get(Common.USER_NAME_TAG).toString();

		userReference = FirebaseDatabase.getInstance().getReference();

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
		recyclerViewMessage = findViewById(R.id.listMessage_ChatActivity);

		btnAddPhoto = findViewById(R.id.img_Photo_Chat);
		btnSend = findViewById(R.id.btn_Send_Chat);
		edtInput = findViewById(R.id.edt_InputMessage_Chat);

		userNameTitle.setText(remote_userName);
		userReference.child(Common.USERS_TAG).child(remote_userID).addListenerForSingleValueEvent(new ValueEventListener() {
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
					if (dataSnapshot.hasChild(Common.USER_THUMB_IMAGE_TAG)){
						String urlThumb = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();
						if (!urlThumb.isEmpty()){
							Picasso.with(ChatActivity.this).load(urlThumb).into(userImageView);
						}
					}

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});





	}

	private void sendMessage() {

		String messageTextSend = edtInput.getText().toString();
		if(TextUtils.isEmpty(messageTextSend)){
			Toast.makeText(ChatActivity.this, "Please input message.", Toast.LENGTH_SHORT).show();
		} else {
			String message_sender_ref = "Messages/" + localUserID + "/" + remote_userID;

			String message_receiver_ref = "Messages/" + remote_userID + "/" + localUserID;

			DatabaseReference message_key = userReference.child(Common.MESSAGES_CHAT_TAG).child(localUserID).child(remote_userID).push();

			String message_push_id = message_key.getKey();

			Map messageTextBody = new HashMap();
			messageTextBody.put(Common.BODY_CHAT_TAG, messageTextSend);
			messageTextBody.put(Common.SEEN_CHAT_TAG, false);
			messageTextBody.put(Common.TYPE_CHAT_TAG, "text");
			messageTextBody.put(Common.TIME_CHAT_TAG, ServerValue.TIMESTAMP);
			messageTextBody.put(Common.FROM_CHAT_TAG, localUserID);

			Map messageBodyDetail = new HashMap();
			messageBodyDetail.put(message_sender_ref + "/" + message_push_id, messageTextBody);
			messageBodyDetail.put(message_receiver_ref + "/" + message_push_id, messageTextBody);

			userReference.updateChildren(messageBodyDetail, new DatabaseReference.CompletionListener() {
				@Override
				public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
					if(databaseError != null){
						Log.d(TAG, databaseError.getMessage().toString());
					}
					edtInput.setText("");
				}
			});
		}

	}
}
