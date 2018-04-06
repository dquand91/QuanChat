package luongduongquan.com.quanchat.Utils;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by luong.duong.quan on 3/27/2018.
 */

public class OfflineData extends Application {

	private DatabaseReference UserReference;
	private FirebaseAuth mAuth;
	private FirebaseUser current_User;

	@Override
	public void onCreate() {
		super.onCreate();

		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		// Picasso load picture offline
		Picasso.Builder builder = new Picasso.Builder(this);
		builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
		Picasso imageOfflineBuilt = builder.build();

		imageOfflineBuilt.setIndicatorsEnabled(true);
		imageOfflineBuilt.setLoggingEnabled(true);
		Picasso.setSingletonInstance(imageOfflineBuilt);

		mAuth = FirebaseAuth.getInstance();
		current_User = mAuth.getCurrentUser();

		if(current_User != null){
			String online_user_id = mAuth.getCurrentUser().getUid();
			UserReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(online_user_id);

			UserReference.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					UserReference.child(Common.ONLINE_TAG).onDisconnect().setValue(System.currentTimeMillis());
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
		}

	}
}
