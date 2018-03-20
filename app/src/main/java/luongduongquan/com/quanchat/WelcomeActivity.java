package luongduongquan.com.quanchat;

import android.content.Intent;
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
					Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
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
}
