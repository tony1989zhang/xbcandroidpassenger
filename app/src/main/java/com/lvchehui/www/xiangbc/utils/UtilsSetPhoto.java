package com.lvchehui.www.xiangbc.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.view.dialog.ActionSheetDialog;
import com.lvchehui.www.xiangbc.view.dialog.ActionSheetDialog.SheetItemColor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lvchehui.www.xiangbc.view.dialog.ActionSheetDialog.OnSheetItemClickListener;

/**
 * 作用：图片选择框，相册or拍照
 *  只需要 调用 showTop 与photoResult
 *  showTop 弹出框 ，photoResult 结果
 */
public class UtilsSetPhoto {

	private static final int PHOTO_PICKED_WITH_DATA = 1881;
	private static final int CAMERA_WITH_DATA = 1882;
	private static final int CAMERA_CROP_RESULT = 1883;
	private static final int PHOTO_CROP_RESOULT = 1884;
	private static final int ICON_SIZE = 300;

    private static final String TAKE_PHOTO = "拍摄";
    private static final String SELECT_FROM_GALLERY = "从手机相册中选择";

	private PopupWindow mSetPhotoPop;
	private File mCurrentPhotoFile;
	private Bitmap imageBitmap;

	private static UtilsSetPhoto u;

	private UtilsSetPhoto() {

	}

	public static UtilsSetPhoto getInstance() {
		if (null == u)
			u = new UtilsSetPhoto();

		return u;
	}

	public void showDialog(final Activity activity,View v){
		/*ChooseWayDialog chooseWayDialog = new ChooseWayDialog(activity);
		chooseWayDialog.setData(TAKE_PHOTO, SELECT_FROM_GALLERY, null);
		chooseWayDialog.setWayBack(new ChooseWayDialog.ChooseBack() {
			@Override
			public void wayback(int i) {
				switch (i) {
					case 0:
						// 相册获取
						doPickPhotoFromGallery(activity);
						break;
					case 1:
						// 拍照获取
						doTakePhoto(activity);
						break;
				}
			}
		});
		chooseWayDialog.show();*/

        ActionSheetDialog dialog = new ActionSheetDialog(activity);
        dialog.builder().setCancelable(false).setCanceledOnTouchOutside(false)
                .addSheetItem("拍摄", SheetItemColor.Blue, new OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        doTakePhoto(activity);
                    }
                })
                .addSheetItem("从手机相册中选择", SheetItemColor.Blue, new OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        doPickPhotoFromGallery(activity);
                    }
                }).show();

	}
	/**
	 * 弹出 popupwindow 底下弹框的方式
	 */
	public void showPop(final Activity activity, View v) {

		View mainView = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.alert_setphoto_menu_layout,
				null);
		Button btnTakePhoto = (Button) mainView.findViewById(R.id.btn_take_photo);
		btnTakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSetPhotoPop.dismiss();
				// 拍照获取
				doTakePhoto(activity);
			}
		});
		Button btnCheckFromGallery = (Button) mainView.findViewById(R.id.btn_check_from_gallery);
		btnCheckFromGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSetPhotoPop.dismiss();
				// 相册获取
				doPickPhotoFromGallery(activity);
			}
		});
		Button btnCancle = (Button) mainView.findViewById(R.id.btn_cancel);
		btnCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSetPhotoPop.dismiss();
			}
		});
		mSetPhotoPop = new PopupWindow(activity);
		mSetPhotoPop.setBackgroundDrawable(new BitmapDrawable());
		mSetPhotoPop.setFocusable(true);
		mSetPhotoPop.setTouchable(true);
		mSetPhotoPop.setOutsideTouchable(true);
		mSetPhotoPop.setContentView(mainView);
		mSetPhotoPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		mSetPhotoPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mSetPhotoPop.setAnimationStyle(R.style.bottomStyle);

		mSetPhotoPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// popupwindow消失的时候恢复成原来的透明度
				Utils.backgroundAlpha(activity, 1f);
			}
		});

		mSetPhotoPop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		mSetPhotoPop.update();
		Utils.backgroundAlpha(activity, 0.5f);
	}

	/**
	 * 调用系统相机拍照
	 */
	private void doTakePhoto(Activity activity) {
		try {
			// Launch camera to take photo for selected contact
			File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
			if (!file.exists()) {
				file.mkdirs();
			}
			mCurrentPhotoFile = new File(file, PhotoUtil.getRandomFileName());
			Log.e("mCurrentPhotoFile", "mCurrentPhotoFile:" + mCurrentPhotoFile);
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			activity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity.getBaseContext(), R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
		}
	}

	private  Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * 从相册选择图片
	 */
	private void doPickPhotoFromGallery(Activity activity) {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			activity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity.getBaseContext(), R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 获取调用相册的Intent
	 */
	private  Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		return intent;
	}

	/**
	 * 相册裁剪图片
	 *
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri, Activity activity) {

		Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 进行修剪
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", ICON_SIZE);
		intent.putExtra("outputY", ICON_SIZE);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, PHOTO_CROP_RESOULT);
	}

	/**
	 * 获取系统剪裁图片的Intent.
	 */
	private  Intent getCropImageIntent(Uri photoUri) {
		Log.e("photoUri", "photoUri:" + photoUri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", ICON_SIZE);
		intent.putExtra("outputY", ICON_SIZE);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 相机剪切图片
	 */
	private void doCropPhoto(File f, Activity activity) {
		try {
			// Add the image to the media store
			MediaScannerConnection.scanFile(activity.getBaseContext(), new String[] { f.getAbsolutePath() },
					new String[] { null }, null);

			// Launch gallery to crop the photo
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			activity.startActivityForResult(intent, CAMERA_CROP_RESULT);
		} catch (Exception e) {
			Toast.makeText(activity.getBaseContext(), R.string.photoPickerNotFoundText, Toast.LENGTH_LONG).show();
		}
	}

	// 当resultCode == RESULT_OK
	public void photoResult(int requestCode, Intent data, Activity activity,PhotoResultIml pr) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:
			// 相册选择图片后裁剪图片
			startPhotoZoom(data.getData(), activity);
			break;
		case PHOTO_CROP_RESOULT:
			Bundle extras = data.getExtras();
			if (extras != null) {
				imageBitmap = extras.getParcelable("data");
//				 imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				pr.OnPotoResult(imageBitmap);
			}
			break;
		case CAMERA_WITH_DATA:
			// 相机拍照后裁剪图片
			doCropPhoto(mCurrentPhotoFile, activity);
			break;
		case CAMERA_CROP_RESULT:
			imageBitmap = data.getParcelableExtra("data");
		//	 imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			//Uri urii = data.getData();
			pr.OnPotoResult(imageBitmap);
			break;
		}

	}


	// 将剪切后的图片保存到本地图片上！
	public String savaBitmap(Bitmap bitmap) {
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
}
