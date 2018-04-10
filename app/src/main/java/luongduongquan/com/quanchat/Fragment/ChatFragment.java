package luongduongquan.com.quanchat.Fragment;

import android.content.Intent;
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

import luongduongquan.com.quanchat.Activities.ChatActivity;
import luongduongquan.com.quanchat.Model.Friends;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;
import luongduongquan.com.quanchat.ViewHolder.FriendsViewHolder;

public class ChatFragment extends Fragment {

	private static final String TAG = "ChatFragment";

	private View mMainView;
	private RecyclerView mlistRoom;
	FirebaseAuth mAuth;
	private DatabaseReference friendsDatabaseReference;
	private DatabaseReference userListDatabaseReference;



	public ChatFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		// Inflate the layout for this fragment

		mMainView = inflater.inflate(R.layout.fragment_chat, container,false);

		mlistRoom = mMainView.findViewById(R.id.chatroomlist_chatfragment);
		mlistRoom.setHasFixedSize(true);
		mlistRoom.setLayoutManager(new LinearLayoutManager(mMainView.getContext()));

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

				Log.d(TAG, "Key = " + friendsDatabaseReference.getKey());
				Log.d(TAG, "position = " + position + " --- Key = " + getRef(position).getKey());
				final String opposite_user_id = getRef(position).getKey();
				userListDatabaseReference.child(opposite_user_id).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot.exists()){
							final String userName = dataSnapshot.child(Common.USER_NAME_TAG).getValue().toString();
							String imageURL = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();
							String lastOnline = dataSnapshot.child(Common.ONLINE_TAG).getValue().toString();
							final String status  = dataSnapshot.child(Common.USER_STATUS_TAG).getValue().toString();

							viewHolder.setTvDate(status);
							viewHolder.setUserName(userName);
							viewHolder.setAvatar(mMainView.getContext(),imageURL);
							if(!lastOnline.isEmpty() && lastOnline.equals("true")){
								viewHolder.setStatus(true);
							} else {
								viewHolder.setStatus(false);
							}

							viewHolder.setCustomOnClick(new FriendsViewHolder.CustomOnClick() {
								@Override
								public void OnClickListenerCustom(View view, int position) {
									Intent intentToChat = new Intent(getContext(), ChatActivity.class);
									intentToChat.putExtra(Common.USERS_ID_TAG, opposite_user_id);
									intentToChat.putExtra(Common.USER_NAME_TAG, userName);
									startActivity(intentToChat);

								}
							});
						}else {
							Toast.makeText(mMainView.getContext(), "Error....", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});




			}
		};
		mlistRoom.setAdapter(friendsAdapter);
		friendsAdapter.notifyDataSetChanged();



		return mMainView;
	}



}
