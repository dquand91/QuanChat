package luongduongquan.com.quanchat.Adapter;


import android.content.Context;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import luongduongquan.com.quanchat.Model.Message;
import luongduongquan.com.quanchat.Utils.Common;
import luongduongquan.com.quanchat.ViewHolder.MessageViewHolder;

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder>{

	private static final String TAG = "MessageAdapter";
	private DatabaseReference mUserReference;
	private String mUserID;
	private Context mContext;


	public MessageAdapter(Class<Message> modelClass,
						  int modelLayout,
						  Class<MessageViewHolder> viewHolderClass,
						  Query query,
						  DatabaseReference userReference,
						  String localUserID,
						  Context context) {
		super(modelClass, modelLayout, viewHolderClass, query);
		Log.d(TAG, "constructor");
		mUserReference = userReference;
		mUserID = localUserID;
		mContext = context;
	}





	@Override
	protected void populateViewHolder(final MessageViewHolder viewHolder, final Message messageModel, int position) {
		final String message_from = messageModel.getFrom();
		Log.d(TAG, "message_from = " + message_from);
			if(message_from.equals(mUserID)){
				Log.d(TAG,"LOCAL " + "id = " + position + " --- " + "from = " + message_from + " --- "
						+ "body =" + messageModel.getBody());

				viewHolder.setContent(messageModel.getBody()); // set message body

				// set message image.
				mUserReference.child(mUserID).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot != null){
							viewHolder.setAvatar(mContext, dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString());
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});

			} else {
				Log.d(TAG,"REMOTE " + "id = " + position + " --- " + "from = " + message_from + " --- "
						+ "body =" + messageModel.getBody());
				final String remote_user_id = getRef(position).getKey();

				viewHolder.setContent(messageModel.getBody()); // set message body
				viewHolder.setRemoteMessage();

				// set message image.
				mUserReference.child(remote_user_id).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot != null){
							viewHolder.setAvatar(mContext, dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString());
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});

			}

		}

}
