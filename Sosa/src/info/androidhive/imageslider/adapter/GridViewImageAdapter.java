package info.androidhive.imageslider.adapter;

import info.androidhive.imageslider.FullScreenViewActivity;
import info.androidhive.imageslider.GridViewActivity;
import info.androidhive.imageslider.helper.AppConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.app.saso.R;
import com.app.sosa.pictures.MyMediaScanner;

public class GridViewImageAdapter extends BaseAdapter {

	private String TAG = "GridViewImageAdapter";
	
	private Activity _activity;
	private ArrayList<String> _photoPaths = new ArrayList<String>();
	private ArrayList<String> _soundPaths = new ArrayList<String>();
	private int imageWidth;
	
	

	public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths, ArrayList<String> soundPaths, int imageWidth) {
		this._activity = activity;
		this._photoPaths = filePaths;
		this._soundPaths = soundPaths;
		this.imageWidth = imageWidth;
	}

	@Override
	public int getCount() {
		return this._photoPaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._photoPaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView aaaaaaaaaaaaaaaaaaaa");
		
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(_activity);
		} else {
			imageView = (ImageView) convertView;
		}

		// get screen dimensions
		Bitmap image = decodeFile(_photoPaths.get(position), imageWidth,	imageWidth);

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(imageWidth, imageWidth));
		imageView.setImageBitmap(image);

		// image view click listener
		imageView.setOnClickListener(new OnImageClickListener(position));
		imageView.setOnLongClickListener(new OnImageLongClickListener(position));

		return imageView;
	}

	/**
	 * 이미지 클릭 리스너
	 */
	class OnImageClickListener implements OnClickListener {
		int _position;

		// constructor
		public OnImageClickListener(int position) {
			this._position = position;
		}

		@Override
		public void onClick(final View v) {
			Log.d(TAG, "click aaaaaaaaaaaaaaaaaaaa");
			
			GridViewActivity.mProgressBar.setVisibility(View.VISIBLE);		//이미지 프로그레스 바 보여짐
			
			//이미지 더블 클릭 방지
			v.setEnabled(false);
			Handler handler = new Handler();
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {
					v.setEnabled(true);
				}
			}, 3000);	//3초 후 이미지 클릭 가능
			
			
			// on selecting grid view image
			// launch full screen activity
			Intent i = new Intent(_activity, FullScreenViewActivity.class);
			i.putExtra("position", _position);
			_activity.startActivity(i);
			_activity.overridePendingTransition(R.anim.fade, R.anim.hold);
		}
	}
	
	/**
	 * 이미지 롱 클릭 리스너
	 */
	class OnImageLongClickListener implements OnLongClickListener {
		int _position;

		// constructor
		public OnImageLongClickListener(int position) {
			this._position = position;
		}

		@Override
		public boolean onLongClick(View v) {
			Log.d(TAG,_photoPaths.get(_position) + " long click aaaaaaaaaaaaa");
			
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(_activity);
		    alt_bld.setMessage("정말 삭제하시겠습니까?").setCancelable(false);
		    alt_bld.setPositiveButton("예",
								        new DialogInterface.OnClickListener() {
									        public void onClick(DialogInterface dialog, int id) {
									            // Action for 'Yes' Button
									        	File photofile = new File(_photoPaths.get(_position));
												File soundfile = new File(_soundPaths.get(_position));
												photofile.delete();	//사진 파일 삭제
												soundfile.delete();	//사운드 파일 삭제
												
												galleryRefresh();		//갤러리 새로고침
												
												_activity.recreate();	//액티비티 새로고침
									        }
								    	});
		    alt_bld.setNegativeButton("아니오",
								        new DialogInterface.OnClickListener() {
									        public void onClick(DialogInterface dialog, int id) {
									            // Action for 'NO' Button
									            dialog.cancel();
									        }
								        });
		    AlertDialog alert = alt_bld.create();
		    // Title for AlertDialog
		    alert.setTitle("삭제");
		    // Icon for AlertDialog
		    alert.show();
		    
			return true;
		}

	}

	/**
	 * 갤러리 강제 갱신*/
    private void galleryRefresh(){
    	Log.d(TAG,"galleryRefresh()aaaaaaaaaaaaaaaaaaaaaaaa");
    	
    	//킷캣 이상
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    		File hFile = new File("file://"+Environment.getExternalStorageDirectory() + AppConstant.PHOTO_ALBUM);
        	MyMediaScanner mScanner = new MyMediaScanner(_activity.getApplicationContext(), hFile);
    	}
    	//킷캣 이하
    	else {
    		_activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://"+Environment.getExternalStorageDirectory() + AppConstant.PHOTO_ALBUM)));
    	}
    }
	
	/**
	 * Resizing image size
	 */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
