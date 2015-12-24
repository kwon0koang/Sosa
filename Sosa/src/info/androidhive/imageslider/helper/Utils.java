package info.androidhive.imageslider.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import com.app.sosa.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Utils {
	private String TAG = "Utils";
	
	private Context _context;

	// constructor
	public Utils(Context context) {
		this._context = context;
	}

	/**
	 * Reading file paths from SDCard
	 */
	public ArrayList<String> getFilePaths() {
		ArrayList<String> filePaths = new ArrayList<String>();

/*		File directory = new File(
				android.os.Environment.getExternalStorageDirectory()
						+ AppConstant.PHOTO_ALBUM);
*/
		File directory = new File("/storage/sdcard0"+ AppConstant.PHOTO_ALBUM);
		
		// check for directory
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				//for (int i = listFiles.length-1; i >= 0; i--) {
				for (int i = 0; i < listFiles.length; i++) {
					// get file path
					String filePath = listFiles[i].getAbsolutePath();
					
					// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						filePaths.add(filePath);
					}
				}
			} else {
				// image directory is empty
				AlertDialog.Builder alert = new AlertDialog.Builder(_context);
				alert.setTitle("Info");
				alert.setMessage("사진이 없습니다.");
				alert.setPositiveButton("OK", null);
				alert.show();
			}

		} 
		/*else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Info");
			alert.setMessage(android.os.Environment.getExternalStorageDirectory()
					+ AppConstant.PHOTO_ALBUM
					+ " directory path is not valid! Please set the image directory name AppConstant.java class");
			alert.setPositiveButton("OK", null);
			alert.show();
		}*/

		return filePaths;
	}
	
	
	/**
	 * Reading sound paths from SDCard
	 */
	public ArrayList<String> getSoundPaths() {
		ArrayList<String> soundPaths = new ArrayList<String>();

		/*File directory = new File(
				android.os.Environment.getExternalStorageDirectory()
						+ AppConstant.SOUND_FOLDER);
		*/
		
		File directory = new File("/storage/sdcard0"+ AppConstant.SOUND_FOLDER);

		// check for directory
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {
				// loop through all files
				//for (int i = listFiles.length-1; i >= 0; i--) {
				for (int i = 0; i < listFiles.length; i++) {
					// get file path
					String filePath = listFiles[i].getAbsolutePath();
					
					//이게 안먹히더라
					//if 체크 빼보자
					/*// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						soundPaths.add(filePath);
					}*/
					soundPaths.add(filePath);
				}
			} else {
				// sound directory is empty
				
			}

		} 
		/*else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Error!");
			alert.setMessage(android.os.Environment.getExternalStorageDirectory()
					+ AppConstant.SOUND_FOLDER
					+ " directory path is not valid! Please set the image directory name AppConstant.java class");
			alert.setPositiveButton("OK", null);
			alert.show();
		}*/

		return soundPaths;
	}
	
	

	/*
	 * Check supported file extensions
	 * 
	 * @returns boolean
	 */
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (AppConstant.FILE_EXTN
				.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
}
