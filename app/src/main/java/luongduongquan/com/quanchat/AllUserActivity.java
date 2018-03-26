package luongduongquan.com.quanchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import luongduongquan.com.quanchat.Adapter.AllUserAdapter;
import luongduongquan.com.quanchat.Model.User;
import luongduongquan.com.quanchat.Utils.Common;

public class AllUserActivity extends AppCompatActivity {

	private Toolbar mToolBar;
	private RecyclerView mRecyclerUserList;

	private FirebaseAuth mAuth;
	private FirebaseDatabase firebaseDatabase;
	private DatabaseReference databaseReference;
	private AllUserAdapter mAdapter;
	private List<User> mListUser;


	private ProgressDialog loadingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_user);

		mAuth = FirebaseAuth.getInstance();


		loadingBar = new ProgressDialog(this);
		loadingBar.setTitle("Getting User List");
		loadingBar.setMessage("Please wait...");
		loadingBar.show();

		//init FireBase
		firebaseDatabase = FirebaseDatabase.getInstance();
		// Get all data in table "Users"
		firebaseDatabase.getReference().child(Common.USERS_TAG);

		mListUser = new ArrayList<>();

		mRecyclerUserList = findViewById(R.id.listAllUser_allUserList);
		mRecyclerUserList.setHasFixedSize(true);
		mRecyclerUserList.setLayoutManager(new LinearLayoutManager(this));



		firebaseDatabase.getReference().addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot != null){
					mListUser.clear();
					Log.d("QuanTest", "aaaaaaaaaa = " + dataSnapshot.toString());
					for (DataSnapshot data: dataSnapshot.child(Common.USERS_TAG).getChildren()) {
						Log.d("QuanTest", "data_before = " + data.toString());
						String userID = data.getKey().toString();
						String userName = data.child(Common.USER_NAME_TAG).getValue().toString();
						String userStatus = "Nothing";
						String userImage = "Nothing";
						if(data.hasChild(Common.USER_STATUS_TAG)){
							userStatus = data.child(Common.USER_STATUS_TAG).getValue().toString();
						}
						if (data.hasChild(Common.USER_IMAGE_TAG)){
							userImage = data.child(Common.USER_IMAGE_TAG).getValue().toString();
						}
						User user = new User(userID, userName, userStatus, userImage);
						if (user.getUserID().equals(mAuth.getCurrentUser().getUid())){
							continue;
						}
						mListUser.add(user);

						Log.d("QuanTest", "userID = " + userID
								+ " --- " + "userName = " + userName
								+ " --- " + "userStatus = " + userStatus
								+  " --- " + "userImage = " + userImage);
					}
				}
				Log.d("QuanTest", "Size = " + mListUser.size());
				mAdapter = new AllUserAdapter(getApplicationContext(), mListUser);
				mAdapter.setCustomOnClick(new AllUserAdapter.CustomOnClick() {
					@Override
					public void OnClickListenerCustom(int position) {
						String chosen_userID = mListUser.get(position).getUserID();
						Intent intentToProfile = new Intent(AllUserActivity.this, ProfileActivity.class);
						intentToProfile.putExtra(Common.USERS_ID_TAG, chosen_userID);
						startActivity(intentToProfile);
					}
				});
				mRecyclerUserList.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				loadingBar.dismiss();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});





		mToolBar = findViewById(R.id.appBar_allUserList);
		setSupportActionBar(mToolBar);
		getSupportActionBar().setTitle("All User List");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


	}
}
