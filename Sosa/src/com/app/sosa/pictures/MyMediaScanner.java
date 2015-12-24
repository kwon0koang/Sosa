package com.app.sosa.pictures;

import java.io.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MyMediaScanner implements MediaScannerConnectionClient{

	private MediaScannerConnection mMs;
	private File mFile;
	
	public MyMediaScanner(Context context, File f) {
		mFile = f;
		mMs = new MediaScannerConnection(context, this);
		mMs.connect();		//onMediaScannerConnected() 호출
	}
	
	@Override
	public void onMediaScannerConnected() {
		mMs.scanFile(mFile.getAbsolutePath(), null);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		mMs.disconnect();		//onMediaScannerConnected() 수행이 끝난 후 연결 해제
	}
	
}
