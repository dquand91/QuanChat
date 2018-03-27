package luongduongquan.com.quanchat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WelcomeActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		Thread thread = new Thread(){
			@Override
			public void run() {
				try{
					sleep(3000);
				}

				catch (Exception e){
					Log.e("ERROR", ""+ e.getMessage());
				}
				finally {
					if (isNetworkConnected()){
						Intent intent = new Intent(WelcomeActivity.this, StartPageActivity.class);
						startActivity(intent);
					} else {
						Intent intentToMain = new Intent(WelcomeActivity.this, MainActivity.class);
						startActivity(intentToMain);
					}

				}
			}
		};
		thread.start();

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null;
	}
}
