package com.app.sosa.sound;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.app.saso.R;
import com.app.sosa.Main;
import com.app.sosa.pictures.MyCamera;



public class MySoundRecorder extends Activity  {
	private String TAG = "MySoundRecorder";
	
	public final static String RECORD_DIR = "/storage/sdcard0/Sosa/Sounds/";

	private MediaPlayer player;
	private MediaRecorder recorder;
	
	
	private Button ViewRecording;
	
	private Button btnRerecord;
	private Button btnPlayAndStop;
	private Button btnRecordOK;
	
	private boolean playFlag = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_sound);

		ViewRecording = (Button) findViewById(R.id.ViewRecording);
		ViewRecording.setOnClickListener(mListener);
		
		btnRerecord = (Button) findViewById(R.id.btnRerecord);
		btnPlayAndStop = (Button) findViewById(R.id.btnPlayAndStop);
		btnRecordOK = (Button) findViewById(R.id.btnRecordOK);
		
		btnRerecord.setOnClickListener(mListener);
		btnPlayAndStop.setOnClickListener(mListener);
		btnRecordOK.setOnClickListener(mListener);
		
		makeSoundFolder();	//사운드 폴더 생성
		
		recordStart();			//바로 레코딩 시작
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
	//mListener#####################################################
	View.OnClickListener mListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//녹음
			if(v == btnRerecord){
				recordStart();
			}
			//녹음 중지
			else if(v == ViewRecording){
				recordStop();
			}
			//재생
			else if(v == btnPlayAndStop){
				playStartAndStop();
			}
			//완료
			else if(v == btnRecordOK){
				Intent intent = new Intent(MySoundRecorder.this, Main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
			
		}
	};
	//mListener end#####################################################


	/**사운드 폴더 생성*/
	private void makeSoundFolder(){
	    String mPath = "/storage/sdcard0/Sosa/";
	    File mediaStorageDir = new File(mPath, "Sounds"); 
	    
	    // 없는 경로라면 따로 생성한다.
	    if (! mediaStorageDir.exists()){
	        mediaStorageDir.mkdirs();
	    }
	}
	
	/**녹음 시작*/
	private void recordStart(){
		ViewRecording.setVisibility(View.VISIBLE);
		btnRerecord.setVisibility(View.INVISIBLE);
		btnPlayAndStop.setVisibility(View.INVISIBLE);
		btnRecordOK.setVisibility(View.INVISIBLE);
		
		//레코딩 뷰가 떠있을 때 뒤의 버튼들에 터치이벤트 무시시키기 위함
		btnRerecord.setEnabled(false);
		btnPlayAndStop.setEnabled(false);
		btnRecordOK.setEnabled(false);
		
		if (recorder != null) {
			recorder.stop();
			recorder.release();
			recorder = null;
		}
		
		recorder = new MediaRecorder();
		
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		
		recorder.setOutputFile(RECORD_DIR + MyCamera.fileName + ".mp3");
		
		try {
			recorder.prepare();
			recorder.start();
		} catch (Exception ex) {
			Log.e("SampleAudioRecorder", "Exception : ", ex);
		}
	}
	/**녹음 중지*/
	private void recordStop(){
		ViewRecording.setVisibility(View.INVISIBLE);
		btnRerecord.setVisibility(View.VISIBLE);
		btnPlayAndStop.setVisibility(View.VISIBLE);
		btnRecordOK.setVisibility(View.VISIBLE);
		
		//레코딩 뷰가 사라지고 버튼들에 터치이벤트 살림
		btnRerecord.setEnabled(true);
		btnPlayAndStop.setEnabled(true);
		btnRecordOK.setEnabled(true);
		
		if (recorder == null)
			return;

		recorder.stop();
		recorder.release();
		recorder = null;
	}
	
	/**재생 시작 중지*/
	private void playStartAndStop(){
		//재생 중 아닐 때, 재생 시작
		if(playFlag == false){
				btnPlayAndStop.setBackgroundDrawable(getResources().getDrawable(R.drawable.record_stop));
				playFlag = true;
				
				if (player != null) {
					player.stop();
					player.release();
					player = null;
				}
	
				try {
					player = new MediaPlayer();
					player.setDataSource(RECORD_DIR + MyCamera.fileName + ".mp3");
					
					player.setOnCompletionListener(new OnCompletionListener() {
						public void onCompletion(MediaPlayer mp) {
							btnPlayAndStop.setBackgroundDrawable(getResources().getDrawable(R.drawable.record_play));
							playFlag = false;
						}
					});
					
					player.prepare();
					player.start();
				} catch (Exception e) {
					Log.e("SampleAudioRecorder", "Audio play failed.", e);
				}
		}
		//재생 중일 때, 재생 중지
		else	{
			btnPlayAndStop.setBackgroundDrawable(getResources().getDrawable(R.drawable.record_play));
			playFlag = false;
			
			if (player == null)	return;

			player.stop();
			player.release();
			player = null;
		}
	}
	
}

