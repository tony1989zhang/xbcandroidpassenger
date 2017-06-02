package com.lvchehui.www.xiangbc.utils.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.freeride.RideDetailsActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.photoselector.view.ZoomImageView;

import java.util.ArrayList;

public class GalleryActivity extends Activity {
	private TextView txt_num;
	private ViewPagerAdapter adapter;
	private ViewPager mViewPager;
	private ArrayList<String> pathList;
	private ArrayList<View> views;
	private int index;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		String sourse = getIntent().getStringExtra(Constants.SOURCE);
		if (StringUtils.isEmpty(sourse)){
			findViewById(R.id.btn_enter).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_del).setVisibility(View.VISIBLE);
		}else if (sourse.equals(RideDetailsActivity.class.getSimpleName())){
			findViewById(R.id.btn_enter).setVisibility(View.GONE);
			findViewById(R.id.btn_del).setVisibility(View.GONE);
		}
		pathList = getIntent().getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
		index = getIntent().getIntExtra("position", 1);
		if (pathList == null || pathList.size() == 0) {
			Toast.makeText(this, "无可预览图片", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		txt_num = (TextView) findViewById(R.id.txt_num);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		findViewById(R.id.btn_enter).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
	            data.putStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT, pathList);
	            setResult(RESULT_OK, data);
	            finish();
			}
		});
		findViewById(R.id.btn_del).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (views.size() == 1) {
					pathList.clear();
					Intent data = new Intent();
		            data.putStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT, pathList);
		            setResult(RESULT_OK, data);
		            finish();
				} else {
					pathList.remove(index);
					views.remove(index);
					mViewPager.removeAllViews();
					adapter.notifyDataSetChanged();
					txt_num.setText((index + 1) + "/" + views.size());
				}
			}
		});
		
		views = new ArrayList<View>();
		for (int i = 0; i < pathList.size(); i++) {
			View view = View.inflate(getApplicationContext(), R.layout.activity_preview, null);
			ZoomImageView imageView = (ZoomImageView) view.findViewById(R.id.img_zoom);
			Glide.with(getApplicationContext())
            .load(pathList.get(i))
            .into(imageView);
			views.add(view);
		}
		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnViewPagerChangeListener());
		mViewPager.setCurrentItem(index);
		txt_num.setText((index + 1) + "/" + views.size());
	}
	
	class ViewPagerAdapter extends PagerAdapter{
		
		private int size;
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position % size));
			return views.get(position % size);
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			container.removeView(views.get(position % size));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public int getItemPosition(Object object) {
		    return POSITION_NONE;
		}

		@Override
		public int getCount() {
			size = views == null ? 0 : views.size();
			return size;
		}
	}
	
	class OnViewPagerChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			index = arg0;
			txt_num.setText((index + 1) + "/" + views.size());
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	    	Intent data = new Intent();
            data.putStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT, pathList);
            setResult(RESULT_OK, data);
            finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
