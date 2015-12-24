package info.androidhive.imageslider.adapter;

import info.androidhive.imageslider.helper.TouchImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.app.saso.R;
import com.app.sosa.sound.MySoundRecorder;

public class FullScreenImageAdapter extends PagerAdapter {

	private String TAG = "FullScreenImageAdapter";
	
	private Activity _activity;
	private ArrayList<String> _imagePaths;
	private ArrayList<String> _soundPaths;
	private LayoutInflater inflater;
	
	private String selectedPhotoName;
	
	private static MediaPlayer mPlayer;
	
	// constructor
	public FullScreenImageAdapter(Activity activity, ArrayList<String> imagePaths, ArrayList<String> soundPaths, int position) {
		this._activity = activity;
		this._imagePaths = imagePaths;
		this._soundPaths = soundPaths;
		
		selectedPhotoName = _imagePaths.get(position).substring(31, 46);	//31번째자리부터 46까지 사진 이름 , 그 전은 폴더 경로, 그 후는 .jpg
		Log.d(TAG,selectedPhotoName + " aaaaaaaaaaaaaaaaaaaaa");
		
		//사운드 재생
		soundPlay();
	}
	
	
	

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	/**전체 페이지 결정 함수*/
	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	/**
	 * 어느 View가 화면에 나오게 할 것인지 결정할 수 있음
	 * 리턴값이 boolean인데 이 값이 true이면 화면 출력, false이면 화면 출력 X
	 * 
	 * instantiateItem에서 만들어진 ViewGroup에 담긴 View와 리턴된 Object가
	 * 매개변수로 전달되어 개발자가 적절한 판단에 의해 
	 * 그 순간 나와야 할 화면을 동적으로 결정할 수 있도록 하는 구조
	 * */
	@Override
    public boolean isViewFromObject(View view, Object object) {
		//return view == ((RelativeLayout) object);
		return view == object;
    }
	
	/**
	 * ViewPager에서 사용할 View 객체 생성 및 등록
	 * */
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;
 
        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);
 
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);
      
        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_activity.finish();
			}
		}); 

        ((ViewPager) container).addView(viewLayout);
 
        return viewLayout;
	}

	/**
	 * 페이지 변경이 시작될 때 호출 
	 * */
	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}
	
	/**
	 * 페이지 변경이 완료되었을 때 호출
	 * */
	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	/**
	 * 꼭 Override할 필요는 없지만, 화면의 개수가 많고,
	 * 각 화면에서 워낙 많은 View들이 사용되거나
	 * 메모리에 부담이 되는 Resource를 사용한 경우,
	 * 이 함수에서 적절하게 ViewGroup에 담긴 View를 destroy할 수 있음
	 * */
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
	

	/**
	 * 사운드 재생
	 * */
	private void soundPlay(){
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}          

		try {
			mPlayer = new MediaPlayer();

			/*mPlayer.setDataSource(RECORD_DIR + selectedPhotoName + ".mp3");
			mPlayer.prepare();
			*/
			FileInputStream fs = new FileInputStream(new File(MySoundRecorder.RECORD_DIR + selectedPhotoName + ".mp3"));
			FileDescriptor fd = fs.getFD();
			mPlayer.setDataSource(fd);
			mPlayer.prepare();
			mPlayer.start();
		} catch (Exception e) {
			Log.e("SampleAudioRecorder", "Audio play failed.", e);
		}
	}
	/**
	 * 사운드 재생 중지
	 * */
	public static void soundStop(){
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}          
	}
}
