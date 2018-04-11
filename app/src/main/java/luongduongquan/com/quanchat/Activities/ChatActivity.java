package luongduongquan.com.quanchat.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import luongduongquan.com.quanchat.LastSeenTime;
import luongduongquan.com.quanchat.Model.Message;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;
import luongduongquan.com.quanchat.ViewHolder.MessageViewHolder;

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
	FirebaseRecyclerAdapter<Message, MessageViewHolder> adapterFireBase;

	private static final int GALLERY_PICK = 1;

	private ImageView imgPreview, btnDeletePreview;
	private TextView tvPreviewImgName;
	private RelativeLayout layoutPreviewImage;
	private Uri imageSendURI;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mAuth = FirebaseAuth.getInstance();
		localUserID = mAuth.getCurrentUser().getUid();

		remote_userID = getIntent().getExtras().get(Common.USERS_ID_TAG).toString();
		remote_userName = getIntent().getExtras().get(Common.USER_NAME_TAG).toString();

		userReference = FirebaseDatabase.getInstance().getReference();
		messageReference = FirebaseDatabase.getInstance().getReference().child(Common.MESSAGES_CHAT_TAG).child(localUserID).child(remote_userID);

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
		recyclerViewMessage.setHasFixedSize(true);
		recyclerViewMessage.setLayoutManager(new LinearLayoutManager(getBaseContext()));

		btnAddPhoto = findViewById(R.id.img_Photo_Chat);
		btnSend = findViewById(R.id.btn_Send_Chat);
		edtInput = findViewById(R.id.edt_InputMessage_Chat);

		imgPreview = findViewById(R.id.imgPreviewImage_Chat);
		btnDeletePreview = findViewById(R.id.btnDeletePreviewImage_Chat);
		tvPreviewImgName = findViewById(R.id.tvPreviewImageName_Chat);
		layoutPreviewImage = findViewById(R.id.layoutPreviewImage_Chat);

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
				Toast.makeText(ChatActivity.this, "Sending message...", Toast.LENGTH_LONG).show();
				if(layoutPreviewImage.getVisibility() == View.VISIBLE && imageSendURI == null){
					// send message with attached image
					sendImageMessage(false, null);
				} else {
					// send only message
					sendImageMessage(true, imageSendURI);
				}

			}
		});

		btnAddPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentToGallery = new Intent();
				intentToGallery.setAction(Intent.ACTION_GET_CONTENT);
				intentToGallery.setType("image/*");
				startActivityForResult(intentToGallery, GALLERY_PICK);
			}
		});

		btnDeletePreview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imgPreview.setImageResource(R.drawable.user);
				tvPreviewImgName.setText("");
				layoutPreviewImage.setVisibility(View.GONE);
				imageSendURI = null;
			}
		});


		adapterFireBase = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(Message.class,
				R.layout.item_message_right,
				MessageViewHolder.class,
				messageReference) {
			@Override
			protected void populateViewHolder(final MessageViewHolder viewHolder, final Message messageModel, int position) {
				final String message_from = messageModel.getFrom();
//				Log.d(TAG, "message_from = " + message_from);

				viewHolder.initView();
				viewHolder.setContent(messageModel.getBody()); // set message body


				viewHolder.setCustomOnClick(new MessageViewHolder.CustomOnClick() {
					@Override
					public void OnClickListenerCustom(View view, int position) {
						Toast.makeText(ChatActivity.this, "position = " + position, Toast.LENGTH_SHORT).show();
					}
				});

				if(message_from.equals(localUserID)){
//					Log.d(TAG,"LOCAL " + "id = " + position + " --- " + "from = " + message_from + " --- "
//							+ "body =" + messageModel.getBody());


					// set message image.
					userReference.child(Common.USERS_TAG).child(localUserID).addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							if(dataSnapshot != null){
//								Log.d(TAG, "dataSnapshot = " + dataSnapshot.toString());
								viewHolder.setAvatar(ChatActivity.this, dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString());
								viewHolder.setLocalMessage();
							}
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {

						}
					});

				} else {
//					Log.d(TAG,"REMOTE " + "id = " + position + " --- " + "from = " + message_from + " --- "
//							+ "body =" + messageModel.getBody());

//					final String remote_user_id = getRef(position).getKey();

					// set message image.
					userReference.child(Common.USERS_TAG).child(remote_userID).addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							if(dataSnapshot != null){
//								Log.d(TAG, "dataSnapshot = " + dataSnapshot.toString());
								viewHolder.setAvatar(ChatActivity.this, dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString());
								viewHolder.setRemoteMessage();
							}
						}

						@Override
						public void onCancelled(DatabaseError databaseError) {

						}
					});

				}

			}
		};








//		adapter = new MessageAdapter<Message, MessageViewHolder>(Message.class, R.layout.item_message_right, MessageViewHolder.class,
//				messageReference, userReference, localUserID, this.getBaseContext());

		recyclerViewMessage.setAdapter(adapterFireBase);
		adapterFireBase.notifyDataSetChanged();


	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
			imageSendURI = data.getData();

			String filePath = getRealPathFromURI(imageSendURI);
//			Log.d(TAG, "data = " + data.toString() + " --- " + "imageURI = " + imageURI + " --- " + "path = " + mime);

			if(layoutPreviewImage.getVisibility() == View.GONE){
				layoutPreviewImage.setVisibility(View.VISIBLE);
			}
			Picasso.with(ChatActivity.this).load(imageSendURI).into(imgPreview);
			tvPreviewImgName.setText(filePath);





			// Tới đây sẽ gọi tới Activity để Crop Image.
			// Crop xong image thì nó sẽ quay lại app và sẽ quay lại callBack onActivityResult() này tiếp.
			// Nó sẽ xuống chỗ "CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE"

		}
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void sendMessage(String linkFile, String message) {


		if(TextUtils.isEmpty(message)){
			Toast.makeText(ChatActivity.this, "Please input message.", Toast.LENGTH_SHORT).show();
		} else {
			String message_sender_ref = "Messages/" + localUserID + "/" + remote_userID;

			String message_receiver_ref = "Messages/" + remote_userID + "/" + localUserID;

			DatabaseReference message_key = userReference.child(Common.MESSAGES_CHAT_TAG).child(localUserID).child(remote_userID).push();

			String message_push_id = message_key.getKey();

			Map messageTextBody = new HashMap();
			messageTextBody.put(Common.BODY_CHAT_TAG, message);
			messageTextBody.put(Common.SEEN_CHAT_TAG, false);
			messageTextBody.put(Common.TYPE_CHAT_TAG, "text");
			messageTextBody.put(Common.TIME_CHAT_TAG, ServerValue.TIMESTAMP);
			messageTextBody.put(Common.FROM_CHAT_TAG, localUserID);
			messageTextBody.put(Common.FILE_CHAT_TAG, linkFile);

			Map messageBodyDetail = new HashMap();
			messageBodyDetail.put(message_sender_ref + "/" + message_push_id, messageTextBody);
			messageBodyDetail.put(message_receiver_ref + "/" + message_push_id, messageTextBody);

			userReference.updateChildren(messageBodyDetail, new DatabaseReference.CompletionListener() {
				@Override
				public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
					if(databaseError != null){
						Log.d(TAG, databaseError.getMessage().toString());
					}
					adapterFireBase.notifyDataSetChanged();
//					imageSendURI = null;
//					layoutPreviewImage.setVisibility(View.GONE);
//					imgPreview.setImageResource(R.drawable.user);
//					tvPreviewImgName.setText("");

				}
			});
		}

	}

	private void sendImageMessage(boolean isSendFile, Uri fileURI){
		final String message =  edtInput.getText().toString();
		if(isSendFile){
			StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Message_Image"); // Profile_Image là tên do mình đặt để tạo thư mục tên "Profile_Image" trên FireBase storage.

			StorageReference filePath = storageReference.child(localUserID +System.currentTimeMillis() + ".jpg");

			filePath.putFile(fileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
					if(task.isSuccessful()){
						Toast.makeText(ChatActivity.this, "Sending your image to FireBase Storage...", Toast.LENGTH_LONG).show();
						String downloadURI = task.getResult().getDownloadUrl().toString();
						// After save file
						sendMessage(downloadURI, message);

					} else {
						Toast.makeText(ChatActivity.this, "Error occured, can not send file!!!", Toast.LENGTH_SHORT).show();
						sendMessage("", message);
					}
				}
			});
			btnDeletePreview.performClick();
		} else {
			sendMessage("", message);
		}
		edtInput.setText("");

	}
}
