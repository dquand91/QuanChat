package luongduongquan.com.quanchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.Utils.Common;

public class SettingsActivity extends AppCompatActivity {

	private CircleImageView imgUser;
	private TextView tvUserName;
	private TextView tvUserStatus;
	private Button btnChangeImage, btnChangeStatus;

	private DatabaseReference userDataPreference;
	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		imgUser = findViewById(R.id.imgUser_Settings);
		tvUserName = findViewById(R.id.tvUserName_Settings);
		tvUserStatus = findViewById(R.id.tvUserStatus_Settings);
		btnChangeImage = findViewById(R.id.btnChangeImage_Settings);
		btnChangeStatus = findViewById(R.id.btnChangeStatus_Settings);


		mAuth = FirebaseAuth.getInstance();
		String current_user_id = mAuth.getCurrentUser().getUid();
		userDataPreference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(current_user_id);

		userDataPreference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null){
					String name = dataSnapshot.child(Common.USER_NAME_TAG).getValue().toString();
					String status = dataSnapshot.child(Common.USER_STATUS_TAG).getValue().toString();
					String userImage = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();
					String userThumb = dataSnapshot.child(Common.USER_THUMB_IMAGE_TAG).getValue().toString();

					tvUserName.setText(name);
					tvUserStatus.setText(status);

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

	}
}
