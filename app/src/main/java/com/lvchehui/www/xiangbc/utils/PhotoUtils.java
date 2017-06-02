package com.lvchehui.www.xiangbc.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Display;

import com.lvchehui.www.xiangbc.view.dialog.ActionSheetDialog;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：V先生 on 2016/8/2 10:13
 * 作用：
 */
public class PhotoUtils {
    private GetPhotoResultListener mGetPhotoResultListener;
    private static  PhotoUtils photoUtils;
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private String picname;
    private int width;
    private int height;

    private String filePath;
    private File file;
    private PhotoUtils()
    {

    }

    public PhotoUtils init(Activity acy, GetPhotoResultListener getPRListener){
        mGetPhotoResultListener = getPRListener;
        picname = "IMG_" + System.currentTimeMillis() / 1000 + ".png";
        file = new File(Environment.getExternalStorageDirectory(), picname);
        Display display = acy.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        return this;
    }
    public static PhotoUtils getInstance(){
        if (null == photoUtils)
        {
            photoUtils = new PhotoUtils();
        }
        return photoUtils;
    }
    public void showDialog(final Activity act)
    {
        /*final ArrayList<String > picArr =  new ArrayList<>();
        picArr.add("拍照");
        picArr.add("相册");

        final CWayDlg cwDlg = new CWayDlg(act);
        cwDlg.settitle("选择上传照片");
        cwDlg.setData(picArr.get(0),picArr.get(1),null);//picArr.get(1)
        cwDlg.setWayBack(new CWayDlg.ChooseBack() {
            @Override
            public void wayback(int i) {
                if (i == 0) startCamera(act);
                else startPick(act);

            }
        });
        cwDlg.show();*/

        ActionSheetDialog dialog = new ActionSheetDialog(act);
        dialog.builder().setCancelable(false).setCanceledOnTouchOutside(false)
                .addSheetItem("拍摄", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startCamera(act);
                    }
                })
                .addSheetItem("从手机相册中选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        startPick(act);
                    }
                }).show();
    }
    private void startCamera(Activity act){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, Uri.fromFile(file));
        act.startActivityForResult(intent,PHOTO_CARMERA);
    }
    private void startPick(Activity act){
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        act.startActivityForResult(intent,PHOTO_PICK);
    }
    public void getActivityResult(Activity act,int requestCode,int resultCode,Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        return;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNKNOWN))
        {
            ToastManager.getManager().show("SD卡不可用");
            return;
        }
        switch(requestCode)
        {
            case PHOTO_CARMERA:
                getBitmap();
                break;
            case PHOTO_PICK:
                if (null == data)
                    return;
                setPicToView(data.getData(),act);
                break;
        }
    }
    private void setPicToView(Uri data,Activity act) {
        Uri uri = data;
        Cursor c = act.getContentResolver().query(uri, null, null, null, null);
        if (null != c) {
            if (c.moveToNext()) {
                String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                file = new File(path);
                getBitmap();

            }
        }else{
            String path = getImageAbsolutePath(act, uri);
            file = new File(path);
            getBitmap();
        }
    }
//    private void setCarmeraToView() {
//        Bitmap bitmap = getBitmap();
//        if (null != mGetPhotoResultListener){
//            mGetPhotoResultListener.onPotoResult(bitmap);
//        }
//
//    }
    private Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        int widthRadio = (int)Math.ceil(options.outWidth/width);
        int heightRadio = (int) Math.ceil(options.outHeight/height);

        if (widthRadio >1 || heightRadio > 1)    //如果大于1，进行压缩
        {
            if (widthRadio >= heightRadio)
            {
                options.inSampleSize = widthRadio;
            }else{
                options.inSampleSize = heightRadio;
            }
        }
        options.inJustDecodeBounds = false;//设置为真正的解码图片
        bitmap = BitmapFactory.decodeFile(file.getPath(),options);

        filePath= saveBitmap(bitmap);
        //传递给activity
        if (null !=  mGetPhotoResultListener)
            mGetPhotoResultListener.onPotoResult(bitmap);

        return bitmap;
    }
    public interface GetPhotoResultListener{
        void onPotoResult(Bitmap ib);
    }

    public String getFilePath(){
        return filePath;
    }

    // 将剪切后的图片保存到本地图片上！
    public String saveBitmap(Bitmap bitmap) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMddHHmmss");
        String cutnameString = dateFormat.format(date);
        String filename = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera" + "/" + cutnameString + ".jpg";
        File f = new File(filename);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }
    public interface PhotoResultIml{
        void OnPotoResult(Bitmap ib);
    }

    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
