package com.lvchehui.www.xiangbc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;

import com.lvchehui.www.xiangbc.view.dialog.ActionSheetDialog;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 张灿能 on 2016/6/22.
 * 作用：直接拍照、选择相册不裁剪
 */
public final class UtilsPhotoNoZoom {
    private OnPhotoNoZoomListener onPhotoNoZoomListener;
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private  File file;
    private  String picname;
    private  SharedPreferences spf;
    private  int width;
    private  int height;
    private static UtilsPhotoNoZoom utilsPhotoNoZoom;
    private UtilsPhotoNoZoom() {
    }

    public static UtilsPhotoNoZoom getInstance(){
        if (null == utilsPhotoNoZoom) {
            utilsPhotoNoZoom = new UtilsPhotoNoZoom();
        }

        return utilsPhotoNoZoom;
    }

    public  void showDialog(final Activity activity){

        /*ChooseWayDialog chooseWayDialog = new ChooseWayDialog(activity);
        chooseWayDialog.setData("从手机相册中获取", "拍摄", null);
        // chooseWayDialog.setData("相册", "拍照", null);
        chooseWayDialog.setWayBack(new ChooseWayDialog.ChooseBack() {
            @Override
            public void wayback(int i) {
                switch (i) {
                    case 0:
                        // 相册获取
                        startPick(activity);
                        break;
                    case 1:
                        // 拍照获取
                        startCamera(activity);
                        break;
                }
            }
        });
        chooseWayDialog.show();*/
        ActionSheetDialog dialog = new ActionSheetDialog(activity);
        dialog.builder().setCancelable(false).setCanceledOnTouchOutside(false)
                .addSheetItem("拍摄", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startCamera(activity);
                    }
                })
                .addSheetItem("从手机相册中选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startPick(activity);
                    }
                }).show();
    }
    public  UtilsPhotoNoZoom init(Activity activity,OnPhotoNoZoomListener onPhotoNoZoomListener) {
        this.onPhotoNoZoomListener = onPhotoNoZoomListener;
        spf = activity.getSharedPreferences("info_user", Context.MODE_PRIVATE);
        picname = "f_" + spf.getString("_id", "") + "_" + System.currentTimeMillis() / 1000 + ".png";
        file = new File(Environment.getExternalStorageDirectory(), picname);

        Display display = activity.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        return this;
    }

    // 调用系统相机
    protected  void startCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, PHOTO_CARMERA);
    }

    // 调用系统相册
    protected  void startPick(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, PHOTO_PICK);
    }

    public  void getActivityResult(int requestCode, int resultCode, Intent data, Activity activity) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNKNOWN)) {
            ToastManager.getManager().show("SD卡不可用");
            return;
        }

        switch (requestCode) {
            case PHOTO_CARMERA:
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;//设置解码只是为了获取图片的width和height值,而不是真正获取图片
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);//解码后可以options.outWidth和options.outHeight来获取图片的尺寸

                int widthRatio = (int) Math.ceil(options.outWidth / width);//获取宽度的压缩比率
                int heightRatio = (int) Math.ceil(options.outHeight / height);//获取高度的压缩比率

                if (widthRatio > 1 || heightRatio > 1) {//只要其中一个的比率大于1,说明需要压缩
                    if (widthRatio >= heightRatio) {//取options.inSampleSize为宽高比率中的最大值
                        options.inSampleSize = widthRatio;
                    } else {
                        options.inSampleSize = heightRatio;
                    }
                }

                options.inJustDecodeBounds = false;//设置为真正的解码图片
                bitmap = BitmapFactory.decodeFile(file.getPath(), options);//解码图片
                if (null != onPhotoNoZoomListener) {
                    onPhotoNoZoomListener.OnPotoResult(bitmap);
                }
                break;
            case PHOTO_PICK:
                if (null != data) {
                    setPicToView(data.getData(), activity);
                }
                break;
        }

    }


    private  void setPicToView(Uri data, Activity activity) {

        /**
         * 当选择的图片不为空的话，在获取到图片的途径
         */
        Uri uri = data;
       /* Cursor c = activity.getContentResolver().query(uri, null, null, null, null);*/
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor c = activity.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        //XgoLog.e(" c:" +  c.toString());
        if (c!=null &&c.moveToNext()) {
            String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));


            file = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//设置解码只是为了获取图片的width和height值,而不是真正获取图片
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);//解码后可以options.outWidth和options.outHeight来获取图片的尺寸

            int widthRatio = (int) Math.ceil(options.outWidth / width);//获取宽度的压缩比率
            int heightRatio = (int) Math.ceil(options.outHeight / height);//获取高度的压缩比率

            if (widthRatio > 1 || heightRatio > 1) {//只要其中一个的比率大于1,说明需要压缩
                if (widthRatio >= heightRatio) {//取options.inSampleSize为宽高比率中的最大值
                    options.inSampleSize = widthRatio;
                } else {
                    options.inSampleSize = heightRatio;
                }
            }

            options.inJustDecodeBounds = false;//设置为真正的解码图片
            bitmap = BitmapFactory.decodeFile(file.getPath(), options);//解码图片
            saveMyBitmap(bitmap, file.getName());
            if (null != onPhotoNoZoomListener) {
                onPhotoNoZoomListener.OnPotoResult(bitmap);
            }
        }
    }

    public  void saveMyBitmap(Bitmap mBitmap, String bitName) {
        file = new File(Environment.getExternalStorageDirectory(),
                picname);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        XgoLog.e("name=" + file.getName() + "\n\n path=" + file.getPath());
    }

    public interface OnPhotoNoZoomListener {
        void OnPotoResult(Bitmap ib);
    }
}
