package luongduongquan.com.quanchat.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import luongduongquan.com.quanchat.FriendsViewHolder;
import luongduongquan.com.quanchat.Model.Friends;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

	private static final String TAG = "FriendsFragment";
	RecyclerView mRecyclerFriendList;
	FirebaseAuth mAuth;
	private DatabaseReference friendsDatabaseReference;
	private DatabaseReference userListDatabaseReference;

	public FriendsFragment() {
		// Required empty public constructor

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View mainView = inflater.inflate(R.layout.fragment_friends, container, false);
		mRecyclerFriendList = mainView.findViewById(R.id.rvListFriend_fragment);
		mRecyclerFriendList.setHasFixedSize(true);
		mRecyclerFriendList.setLayoutManager(new LinearLayoutManager(mainView.getContext()));

		mAuth = FirebaseAuth.getInstance();

		friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Common.FRIENDS_TAG).child(mAuth.getCurrentUser().getUid());
		friendsDatabaseReference.keepSynced(true);

		userListDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG);
		userListDatabaseReference.keepSynced(true);

		FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
				Friends.class,
				R.layout.item_user_list,
				FriendsViewHolder.class,
				friendsDatabaseReference
		) {
			@Override
			protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends model, int position) {
				viewHolder.setTvDate(model.getDate());

				Log.d(TAG, "Key = " + friendsDatabaseReference.getKey());
				Log.d(TAG, "position = " + position + " --- Key = " + getRef(position).getKey());
				String opposite_user_id = getRef(position).getKey();
				userListDatabaseReference.child(opposite_user_id).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot.exists()){
							String userName = dataSnapshot.child(Common.USER_NAME_TAG).getValue().toString();
							String imageURL = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();
							boolean isOnline = (boolean) dataSnapshot.child(Common.ONLINE_TAG).getValue();

							viewHolder.setUserName(userName);
							viewHolder.setAvatar(mainView.getContext(),imageURL);
							viewHolder.setStatus(isOnline);
						}else {
							Toast.makeText(mainView.getContext(), "Error....", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});


			}
		};
		mRecyclerFriendList.setAdapter(friendsAdapter);
		friendsAdapter.notifyDataSetChanged();


		return mainView;
	}


}
