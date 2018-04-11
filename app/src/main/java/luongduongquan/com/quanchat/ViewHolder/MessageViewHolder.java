package luongduongquan.com.quanchat.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

	private CircleImageView imgUserMessage_holder;
	private TextView tvContentMessage_holder;
	private MessageViewHolder.CustomOnClick mCustomOnClick;
	public View mView;
	private ImageView imgPreviewImage;
	private TextView tvFileName;
	private LinearLayout layoutBodyMessage;

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
		tvContentMessage_holder.setText(content);
	}

	public void initView(){
		tvContentMessage_holder = mView.findViewById(R.id.tvMessageUserItem_Chat);
		imgUserMessage_holder = mView.findViewById(R.id.imgUserItem_Chat);
		imgPreviewImage = mView.findViewById(R.id.imgPreviewImageItem_Chat);
		tvFileName = mView.findViewById(R.id.tvPreviewNameItem_Chat);
		layoutBodyMessage = mView.findViewById(R.id.layoutBodyMessageItem_Chat);
	}

	public void setImagePreview(Context context, String urlImage){
		Picasso.with(context).load(urlImage).into(imgPreviewImage);
	}

	public void setImageName(String name){
		tvFileName.setText(name);
	}

	public void setAvatar(Context context, String urlImage){
		Picasso.with(context).load(urlImage).into(imgUserMessage_holder);
	}

	public void setCustomOnClick(MessageViewHolder.CustomOnClick customOnClick){
		mCustomOnClick = customOnClick;
	}

	public void setLocalMessage(){

		//imgUserMessage_holder ALIGN_PARENT_LEFT
		// De lay ra cai layout cha cua thang imgUserMessage_holder nay
		RelativeLayout.LayoutParams layoutParamsImg =(RelativeLayout.LayoutParams)imgUserMessage_holder.getLayoutParams();

		layoutParamsImg.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParamsImg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		imgUserMessage_holder.setLayoutParams(layoutParamsImg);


		// De lay ra cai layout cha cua thang layoutBodyMessage nay
		RelativeLayout.LayoutParams layoutParamsBody =(RelativeLayout.LayoutParams)layoutBodyMessage.getLayoutParams();

		layoutParamsBody.addRule(RelativeLayout.LEFT_OF, R.id.imgUserItem_Chat);
		layoutParamsBody.addRule(RelativeLayout.RIGHT_OF, 0);
		layoutParamsBody.setMargins(0,0,5,0);

		layoutBodyMessage.setLayoutParams(layoutParamsBody);
//		layoutBodyMessage.setGravity(Gravity.END);

		Drawable mDrawable = ContextCompat.getDrawable(mView.getContext(), R.drawable.message_local_background);
		tvContentMessage_holder.setBackground(mDrawable);

		tvContentMessage_holder.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

		//
//		RelativeLayout.LayoutParams layoutParamsText =(RelativeLayout.LayoutParams)tvContentMessage_holder.getLayoutParams();
//		layoutParamsText.addRule(RelativeLayout.LEFT_OF, R.id.imgUserItem_Chat);
//		layoutParamsText.addRule(RelativeLayout.RIGHT_OF, 0);
//		layoutParamsText.setMargins(0,0,5,0);
//
//		tvContentMessage_holder.setLayoutParams(layoutParamsText);
//		tvContentMessage_holder.setGravity(Gravity.END);
//
//		Drawable mDrawable = ContextCompat.getDrawable(mView.getContext(), R.drawable.message_local_background);
//		tvContentMessage_holder.setBackground(mDrawable);
	}

	public void setRemoteMessage(){

		//imgUserMessage_holder ALIGN_PARENT_LEFT
		RelativeLayout.LayoutParams layoutParamsImg =(RelativeLayout.LayoutParams)imgUserMessage_holder.getLayoutParams();
		layoutParamsImg.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParamsImg.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		imgUserMessage_holder.setLayoutParams(layoutParamsImg);

		RelativeLayout.LayoutParams layoutParamsBody =(RelativeLayout.LayoutParams)layoutBodyMessage.getLayoutParams();
		layoutParamsBody.addRule(RelativeLayout.LEFT_OF, 0);
		layoutParamsBody.addRule(RelativeLayout.RIGHT_OF,  R.id.imgUserItem_Chat);
		layoutParamsBody.setMargins(5,0,0,0);

		layoutBodyMessage.setLayoutParams(layoutParamsBody);
//		layoutBodyMessage.setGravity(Gravity.END);

		Drawable mDrawable = ContextCompat.getDrawable(mView.getContext(), R.drawable.message_remote_background);
		tvContentMessage_holder.setBackground(mDrawable);

		tvContentMessage_holder.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

		//
//		RelativeLayout.LayoutParams layoutParamsText =(RelativeLayout.LayoutParams)tvContentMessage_holder.getLayoutParams();
//		layoutParamsText.addRule(RelativeLayout.RIGHT_OF, R.id.imgUserItem_Chat);
//		layoutParamsText.addRule(RelativeLayout.LEFT_OF, 0);
//		layoutParamsText.setMargins(5,0,0,0);
//
//		tvContentMessage_holder.setLayoutParams(layoutParamsText);
//		tvContentMessage_holder.setGravity(Gravity.START);
//
//		Drawable mDrawable = ContextCompat.getDrawable(mView.getContext(), R.drawable.message_remote_background);
//		tvContentMessage_holder.setBackground(mDrawable);
	}
}
