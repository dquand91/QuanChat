package luongduongquan.com.quanchat.Adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class MessageAdapter<Message, MessageViewHolder> extends FirebaseRecyclerAdapter<Message, luongduongquan.com.quanchat.ViewHolder.MessageViewHolder> {


	public MessageAdapter(Class<Message> modelClass, int modelLayout, Class<luongduongquan.com.quanchat.ViewHolder.MessageViewHolder> viewHolderClass, Query query) {
		super(modelClass, modelLayout, viewHolderClass, query);
	}

	@Override
	protected void populateViewHolder(luongduongquan.com.quanchat.ViewHolder.MessageViewHolder viewHolder, Message model, int position) {

	}
}
