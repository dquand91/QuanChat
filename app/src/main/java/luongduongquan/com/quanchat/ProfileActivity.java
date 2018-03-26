package luongduongquan.com.quanchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import luongduongquan.com.quanchat.Utils.Common;

public class ProfileActivity extends AppCompatActivity {

	ImageView imgUserProfile;
	TextView tvUserNameProfile, tvUserStatusProfile;
	Button btnAcceptSentRequest, btnDeclineRequest;

	private DatabaseReference userDataPreference;
	private FirebaseAuth mAuth;

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

		String userID_profile = mAuth.getCurrentUser().getUid();
		if(getIntent().getExtras() != null){
			userID_profile = getIntent().getStringExtra(Common.USERS_ID_TAG);
		}



		userDataPreference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(userID_profile);

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


}
