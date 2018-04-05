package luongduongquan.com.quanchat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import luongduongquan.com.quanchat.Utils.Common;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

	ImageView imgUserProfile;
	TextView tvUserNameProfile, tvUserStatusProfile;
	Button btnAcceptSentRequest, btnDeclineRequest;

	private DatabaseReference userDataPreference;
	private FirebaseAuth mAuth;

	private DatabaseReference friendRequestDataReference;

	private DatabaseReference friendsDataReferece;

	// currentUserID : ID of current login user
	// receiverID  : ID of chosen user from User list, app is showing the profile screen of this user.
	private String currentUserID, receiverID;
	private String CURRENT_STATE;

	// For Notification
	private DatabaseReference notificationReference;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		mAuth = FirebaseAuth.getInstance();

		imgUserProfile = findViewById(R.id.imgUser_profile);
		tvUserNameProfile = findViewById(R.id.tvUserName_profile);
		tvUserStatusProfile = findViewById(R.id.tvStatus_profile);

		btnAcceptSentRequest = findViewById(R.id.btnSendOrAccept_profile);
		btnDeclineRequest = findViewById(R.id.btnDecline_profile);

		btnAcceptSentRequest.setOnClickListener(this);
		btnDeclineRequest.setOnClickListener(this);

		btnDeclineRequest.setVisibility(View.INVISIBLE);
		btnDeclineRequest.setEnabled(false);

		CURRENT_STATE = Common.NOT_FRIEND;

		currentUserID = mAuth.getCurrentUser().getUid();
		if(getIntent().getExtras() != null){
			Log.d("ProfileActivity", " " + receiverID + " " );
			receiverID = getIntent().getStringExtra(Common.USERS_ID_TAG);
			Log.d("ProfileActivity", " " + receiverID + " " );
		}


		// For Notification
		notificationReference = FirebaseDatabase.getInstance().getReference().child(Common.NOTIFICATION_TAG);
		notificationReference.keepSynced(true);

		// For Friend Data base
		friendsDataReferece = FirebaseDatabase.getInstance().getReference().child(Common.FRIENDS_TAG);
		friendsDataReferece.keepSynced(true);


		friendsDataReferece.child(currentUserID)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot.exists()){
							Log.d("QUAN123", "dataSnapshot exists ");
							if(dataSnapshot.hasChild(receiverID)){
								CURRENT_STATE = Common.FRIENDS_TAG;
								btnAcceptSentRequest.setText(Common.UN_FRIENDS_TAG);
								btnDeclineRequest.setVisibility(View.INVISIBLE);
								btnDeclineRequest.setEnabled(false);
							}
						} else if (CURRENT_STATE.equals(Common.NOT_FRIEND)){
							btnAcceptSentRequest.setText(Common.SEND_FRIEND_REQUEST);
							btnDeclineRequest.setVisibility(View.INVISIBLE);
							btnDeclineRequest.setEnabled(false);
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});

		// For Friend Request
		friendRequestDataReference = FirebaseDatabase.getInstance().getReference().child(Common.FRIEND_REQUEST_TAG);
		friendsDataReferece.keepSynced(true);

		friendRequestDataReference.child(currentUserID)
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						Log.d("QUAN123", "current_USER_ID = " + currentUserID);

						if(dataSnapshot.exists()){
							Log.d("QUAN123", "data = " + dataSnapshot.toString());
							if(dataSnapshot.hasChild(receiverID)){
								String request_type = dataSnapshot.child(receiverID).child(Common.FRIEND_REQUEST_TYPE_TAG).getValue().toString();
								Log.d("QUAN123", "request_type = " + request_type);
								switch (request_type){
									case Common.REQUEST_SEND_TAG:
										CURRENT_STATE = Common.REQUEST_SEND;
										btnAcceptSentRequest.setText("Cancel Friend Request");

										btnDeclineRequest.setVisibility(View.INVISIBLE);
										btnDeclineRequest.setEnabled(false);

										break;
									case Common.REQUEST_RECEIVE_TAG:
										CURRENT_STATE = Common.REQUEST_RECEIVE;
										btnAcceptSentRequest.post(new Runnable() {
											@Override
											public void run() {
												btnAcceptSentRequest.setText("Accept Friend Request");
												btnDeclineRequest.setVisibility(View.VISIBLE);
												btnDeclineRequest.setEnabled(true);
											}
										});
										break;
								}
							}
						}

					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});


		userDataPreference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(receiverID);
		userDataPreference.keepSynced(true);


		userDataPreference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null){
					String name = dataSnapshot.child(Common.USER_NAME_TAG).getValue().toString();
					String status = dataSnapshot.child(Common.USER_STATUS_TAG).getValue().toString();
					String userImage = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();

					tvUserNameProfile.setText(name);
					tvUserStatusProfile.setText(status);
					Picasso.with(getApplicationContext()).load(userImage).into(imgUserProfile);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnSendOrAccept_profile:
				btnAcceptSentRequest.setEnabled(false);
				switch (CURRENT_STATE){
					case Common.NOT_FRIEND:
						sendFriendRequest();
						break;
					case Common.REQUEST_SEND:
						cancelRequest(Common.NOT_FRIEND, Common.SEND_FRIEND_REQUEST);
						break;
					case Common.REQUEST_RECEIVE:
						acceptRequest();
						break;
					case Common.FRIENDS_TAG:
						unFriend();
						break;
				}

				break;
			case R.id.btnDecline_profile:
				btnDeclineRequest.setEnabled(false);
				if(CURRENT_STATE.equals(Common.REQUEST_RECEIVE)){
					friendRequestDataReference.child(currentUserID).child(receiverID).removeValue()
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									if(task.isSuccessful()){
										friendRequestDataReference.child(receiverID).child(currentUserID).removeValue()
												.addOnCompleteListener(new OnCompleteListener<Void>() {
													@Override
													public void onComplete(@NonNull Task<Void> task) {
														if(task.isSuccessful()){
														updateButtonSentAcceptUI(Common.NOT_FRIEND, Common.SEND_FRIEND_REQUEST, true);
														btnDeclineRequest.setVisibility(View.INVISIBLE);
														btnDeclineRequest.setEnabled(false);
														} else {
															btnAcceptSentRequest.setEnabled(true);
															Toast.makeText(ProfileActivity.this, "Error, ", Toast.LENGTH_SHORT).show();
														}

													}
												});
									} else {
										btnAcceptSentRequest.setEnabled(true);
										Toast.makeText(ProfileActivity.this, "Error, ", Toast.LENGTH_SHORT).show();
									}
								}
							});
				}


				break;
		}
	}

	private void updateButtonSentAcceptUI(String state, String buttonText, boolean isEnable){
		btnAcceptSentRequest.setEnabled(isEnable);
		CURRENT_STATE = state;
		btnAcceptSentRequest.setText(buttonText);
	}

	private void unFriend() {

		friendsDataReferece.child(currentUserID).child(receiverID).removeValue()
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if(task.isSuccessful()){
							friendsDataReferece.child(receiverID).child(currentUserID).removeValue()
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if(task.isSuccessful()){
//												btnAcceptSentRequest.setEnabled(true);
//												CURRENT_STATE = Common.NOT_FRIEND;
//												btnAcceptSentRequest.setText(Common.SEND_FRIEND_REQUEST);
												updateButtonSentAcceptUI(Common.NOT_FRIEND, Common.SEND_FRIEND_REQUEST, true);
												btnDeclineRequest.setVisibility(View.INVISIBLE);
												btnDeclineRequest.setEnabled(false);

											} else {
												btnAcceptSentRequest.setEnabled(true);
												Toast.makeText(ProfileActivity.this, "Error, Can not un friend", Toast.LENGTH_SHORT).show();
											}
										}
									});
						} else {
							btnAcceptSentRequest.setEnabled(true);
							Toast.makeText(ProfileActivity.this, "Error, Can not un friend", Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

	private void acceptRequest() {

		Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss z");
		final String saveCurrentDate = currentDateFormat.format(calendar.getTime());

		friendsDataReferece.child(currentUserID).child(receiverID).child(Common.DATE_TAG).setValue(saveCurrentDate)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if(task.isSuccessful()){
							friendsDataReferece.child(receiverID).child(currentUserID).child(Common.DATE_TAG).setValue(saveCurrentDate)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if(task.isSuccessful()){
											// After make friend success, remove Friend request.
												cancelRequest(Common.FRIENDS_TAG, Common.UN_FRIENDS_TAG);
											}
										}
									});
						} else {
							Toast.makeText(ProfileActivity.this, "Error, ", Toast.LENGTH_SHORT).show();
							btnAcceptSentRequest.setEnabled(true);
						}
					}
				});


	}

	private void cancelRequest(final String current_state, final String buttonText) {

		friendRequestDataReference.child(currentUserID).child(receiverID).removeValue()
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()){
							friendRequestDataReference.child(receiverID).child(currentUserID).removeValue()
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if (task.isSuccessful()){
//													btnAcceptSentRequest.setEnabled(true);
//													CURRENT_STATE = current_state;
//													btnAcceptSentRequest.setText(buttonText);

												updateButtonSentAcceptUI(current_state, buttonText, true);
												btnDeclineRequest.setVisibility(View.INVISIBLE);
												btnDeclineRequest.setEnabled(false);
											} else {
												btnAcceptSentRequest.setEnabled(true);
												Toast.makeText(ProfileActivity.this, "Error while remove request...", Toast.LENGTH_SHORT).show();

											}
										}
									});
						} else {
							btnAcceptSentRequest.setEnabled(true);
							Toast.makeText(ProfileActivity.this, "Error while remove request...", Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

	private void sendFriendRequest() {
		friendRequestDataReference.child(currentUserID).child(receiverID)
				.child(Common.FRIEND_REQUEST_TYPE_TAG).setValue(Common.REQUEST_SEND_TAG)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						// Send friend request
						if(task.isSuccessful()){
							// After send request success, send other API to update
							friendRequestDataReference.child(receiverID).child(currentUserID)
									.child(Common.FRIEND_REQUEST_TYPE_TAG).setValue(Common.REQUEST_RECEIVE_TAG)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {

											if (task.isSuccessful()){

												HashMap<String, String> notificationData = new HashMap<String, String>();
												notificationData.put("from", currentUserID);
												notificationData.put("type", "request");

												// .push ở đây tức là sẽ tạo 1 child ngẫu nhiên.
												notificationReference.child(receiverID).push().setValue(notificationData)
														.addOnCompleteListener(new OnCompleteListener<Void>() {
															@Override
															public void onComplete(@NonNull Task<Void> task) {

																if(task.isSuccessful()){
																	btnAcceptSentRequest.setEnabled(true);
																	btnAcceptSentRequest.setText("Cancel Friend Request");
																	btnDeclineRequest.setVisibility(View.INVISIBLE);
																	btnDeclineRequest.setEnabled(false);
																} else {
																	btnAcceptSentRequest.setEnabled(true);
																	Toast.makeText(ProfileActivity.this, "Error, Can not receive response from friend request", Toast.LENGTH_SHORT).show();
																}

															}
														});


											}else {
												btnAcceptSentRequest.setEnabled(true);
												Toast.makeText(ProfileActivity.this, "Error, Can not receive response from friend request", Toast.LENGTH_SHORT).show();
											}
										}
									});
						} else {
							btnAcceptSentRequest.setEnabled(true);
							Toast.makeText(ProfileActivity.this, "Error, Can not sent friend request", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}
