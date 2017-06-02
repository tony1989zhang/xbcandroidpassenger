package com.lvchehui.www.xiangbc.utils.photoselector;

import com.bumptech.glide.Glide;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.photoselector.view.ZoomImageView;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class PreviewActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		String img = getIntent().getStringExtra("data");
		if (TextUtils.isEmpty(img)) {
			Toast.makeText(this, "路径出错", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		ZoomImageView img_zoom = (ZoomImageView) findViewById(R.id.img_zoom);
		Glide.with(this)
        .load(img)
        .into(img_zoom);
	}
}
