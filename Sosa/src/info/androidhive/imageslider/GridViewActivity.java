package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.FullScreenImageAdapter;
import info.androidhive.imageslider.adapter.GridViewImageAdapter;
import info.androidhive.imageslider.helper.AppConstant;
import info.androidhive.imageslider.helper.Utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.app.saso.R;

public class GridViewActivity extends Activity {

	private String TAG = "GridViewActivity";
	
	private Utils utils;
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private ArrayList<String> soundPaths = new ArrayList<String>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private int columnWidth;

	public static ProgressBar mProgressBar;		//이미지 프로그레스 바
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_grid_view);

		gridView = (GridView) findViewById(R.id.grid_view);

		utils = new Utils(this);

		// Initilizing Grid View
		InitilizeGridLayout();
		
		// loading all image paths and sound paths from SD card
		imagePaths = utils.getFilePaths();
		soundPaths = utils.getSoundPaths();

		// Gridview adapter
		adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths, soundPaths, columnWidth);
		gridView.setAdapter(adapter);		// setting grid view adapter
		
		mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
		mProgressBar.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		mProgressBar.setVisibility(View.INVISIBLE);
	}


	
	
	
	//######################################################################################




	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppConstant.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

}
