package luongduongquan.com.quanchat.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

	private CircleImageView imgUserMessage_holder;
	private TextView tvContentMessage_holder;
	private MessageViewHolder.CustomOnClick mCustomOnClick;
	public View mView;

	public interface CustomOnClick{
		public void OnClickListenerCustom(View view, int position);
	}

	public MessageViewHolder(View itemView) {
		super(itemView);
		mView = itemView;
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCustomOnClick.OnClickListenerCustom(v, getAdapterPosition());
			}
		});
	}

	public void setContent(String content){
		tvContentMessage_holder = mView.findViewById(R.id.tvMessageUserItem_Chat);
		tvContentMessage_holder.setText(content);
	}

	public void setAvatar(Context context, String urlImage){
		imgUserMessage_holder = mView.findViewById(R.id.imgUserItem_Chat);
		Picasso.with(context).load(urlImage).into(imgUserMessage_holder);
	}

	public void setCustomOnClick(MessageViewHolder.CustomOnClick customOnClick){
		mCustomOnClick = customOnClick;
	}
}
