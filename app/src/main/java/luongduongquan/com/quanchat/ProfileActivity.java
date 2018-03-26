package luongduongquan.com.quanchat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import luongduongquan.com.quanchat.Utils.Common;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

	ImageView imgUserProfile;
	TextView tvUserNameProfile, tvUserStatusProfile;
	Button btnAcceptSentRequest, btnDeclineRequest;

	private DatabaseReference userDataPreference;
	private FirebaseAuth mAuth;

	private DatabaseReference friendDataRegerence;

	private String currentUserID, receiverID;
	// currentUserID : ID of current login user
	// receiverID  : ID of chosen user from User list, app is showing the profile screen of this user.


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

		currentUserID = mAuth.getCurrentUser().getUid();
		if(getIntent().getExtras() != null){
			receiverID = getIntent().getStringExtra(Common.USERS_ID_TAG);
		}

		// For Friend Request
		friendDataRegerence = FirebaseDatabase.getInstance().getReference().child(Common.FRIEND_REQUEST_TAG);

		friendDataRegerence.child(currentUserID)
				.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot.hasChild(receiverID)){
							String request_type = dataSnapshot.child(receiverID).child(Common.FRIEND_REQUEST_TYPE_TAG).getValue().toString();

							if(request_type.equals(Common.REQUEST_SEND_TAG)){
								btnAcceptSentRequest.setText("Cancel Friend Request");
							}
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});


		userDataPreference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(receiverID);

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
				friendDataRegerence.child(currentUserID).child(receiverID)
						.child(Common.FRIEND_REQUEST_TYPE_TAG).setValue(Common.REQUEST_SEND_TAG)
						.addOnCompleteListener(new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								// Send friend request
								if(task.isSuccessful()){
									// After send request success, send other API to update
									friendDataRegerence.child(receiverID).child(currentUserID)
											.child(Common.FRIEND_REQUEST_TYPE_TAG).setValue(Common.FRIEND_REQUEST_RECEIVER_TAG)
											.addOnCompleteListener(new OnCompleteListener<Void>() {
												@Override
												public void onComplete(@NonNull Task<Void> task) {

													if (task.isSuccessful()){
														btnAcceptSentRequest.setEnabled(true);
														btnAcceptSentRequest.setText("Cancel Friend Request");
													}else {
														Toast.makeText(ProfileActivity.this, "Error, Can not receive response from friend request", Toast.LENGTH_SHORT).show();
													}
												}
											});
								} else {
									Toast.makeText(ProfileActivity.this, "Error, Can not sent friend request", Toast.LENGTH_SHORT).show();
								}
							}
						});
				break;
			case R.id.btnDecline_profile:

				break;
		}
	}
}
