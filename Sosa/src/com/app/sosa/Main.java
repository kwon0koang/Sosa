package com.app.sosa;

import java.io.File;
import java.util.ArrayList;

import info.androidhive.imageslider.GridViewActivity;
import info.androidhive.imageslider.helper.AppConstant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.app.saso.R;
import com.app.sosa.pictures.MyCamera;

public class Main extends Activity {

	private String TAG="Main";
	
	private Button btnCamera;
	private Button btnPhotoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		btnCamera = (Button)findViewById(R.id.btnCamera);
		btnPhotoList = (Button)findViewById(R.id.btnPhotoList);
		btnCamera.setOnClickListener(mListener);
		btnPhotoList.setOnClickListener(mListener);
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
	private View.OnClickListener mListener =  new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			if(v == btnCamera){
				Intent intent = new Intent(Main.this, MyCamera.class);
				startActivity(intent);
				
			}
			else if(v == btnPhotoList){
				File directory = new File(
						android.os.Environment.getExternalStorageDirectory()
								+ AppConstant.PHOTO_ALBUM);

				//사진 폴더가 존재하면
				if (directory.isDirectory()) {
					// getting list of file paths
					File[] listFiles = directory.listFiles();

					//사진이 있다면
					if (listFiles.length > 0) {
						Intent intent = new Intent(Main.this, GridViewActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade, R.anim.hold);
					} 
					else{
						dialogShow();
					}
				} 
				//사진 폴더가 존재하지 않으면
				else {
					dialogShow();
				}
			}
		}
	};
	
	public void dialogShow(){
		AlertDialog.Builder alert = new AlertDialog.Builder(Main.this);
		alert.setTitle("Info");
		alert.setMessage("사진이 없습니다.");
		alert.setPositiveButton("OK", null);
		alert.show();
	}
}
