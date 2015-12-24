package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.FullScreenImageAdapter;
import info.androidhive.imageslider.helper.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

import com.app.saso.R;

public class FullScreenViewActivity extends Activity{

	private String TAG = "FullScreenViewActivity";
	
	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager) findViewById(R.id.pager);

		utils = new Utils(getApplicationContext());

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);		

		Log.d(TAG,utils.getFilePaths()+"    aaaaaaaaaaaaaaaaaaa");
		Log.d(TAG,utils.getSoundPaths()+"   aaaaaaaaaaaaaaaaaaa");
		
		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, utils.getFilePaths(), utils.getSoundPaths(), position);

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
	}

	//사진 터치해서 크게 봤다가
	//액티비티 벗어났을 때, 사운드 재생 중지하기 위함
	@Override
	protected void onStop() {
		super.onStop();
		FullScreenImageAdapter.soundStop();
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
	
}
