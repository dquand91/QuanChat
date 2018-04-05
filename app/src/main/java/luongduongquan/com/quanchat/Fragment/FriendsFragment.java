package luongduongquan.com.quanchat.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
		View mainView = inflater.inflate(R.layout.fragment_friends, container, false);
		mRecyclerFriendList = mainView.findViewById(R.id.rvListFriend_fragment);
		mRecyclerFriendList.setHasFixedSize(true);
		mRecyclerFriendList.setLayoutManager(new LinearLayoutManager(mainView.getContext()));

		mAuth = FirebaseAuth.getInstance();

		friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Common.FRIENDS_TAG).child(mAuth.getCurrentUser().getUid());
		friendsDatabaseReference.keepSynced(true);

		FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
				Friends.class,
				R.layout.item_user_list,
				FriendsViewHolder.class,
				friendsDatabaseReference
		) {
			@Override
			protected void populateViewHolder(FriendsViewHolder viewHolder, Friends model, int position) {
				viewHolder.setTvDate(model.getDate());
				Log.d(TAG, "Key = " + friendsDatabaseReference.getKey());

			}
		};
		mRecyclerFriendList.setAdapter(friendsAdapter);
		friendsAdapter.notifyDataSetChanged();


		return mainView;
	}


}
