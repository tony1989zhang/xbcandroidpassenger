package com.lvchehui.www.xiangbc.utils.photoselector;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.photoselector.adapter.DemoAdapter;

/**
 * 多图片上传 评论可以加入
 */
public class DemoActivity extends Activity {

    private DemoAdapter adapter;
    private ArrayList<String> path = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        RecyclerView recycler = (RecyclerView) super.findViewById(R.id.recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(gridLayoutManager);
        adapter = new DemoAdapter(this, path);
        recycler.setAdapter(adapter);

    }

    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            path.clear();
            path.addAll(pathList);
            adapter.notifyDataSetChanged();
        }
    }
}
