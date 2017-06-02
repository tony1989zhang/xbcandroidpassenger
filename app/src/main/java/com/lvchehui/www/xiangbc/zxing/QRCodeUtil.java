package com.lvchehui.www.xiangbc.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lvchehui.www.xiangbc.zxing.android.CaptureActivity;
import com.lvchehui.www.xiangbc.zxing.encode.CodeCreator;

import java.util.Hashtable;

/**
 * Created by tony on 2016/4/29.
 */
public class QRCodeUtil {

    private QRCodeUtil() {

    }

    private static QRCodeUtil qr;

    public static QRCodeUtil getInstance() {
        if (null == qr) {
            qr = new QRCodeUtil();
        }
        return qr;
    }

    private static final int REQUEST_CODE_SCAN = 0x0000; // 扫描二维码返回值
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int IMAGE_HALFWIDTH = 40;// 二维码 宽度值，影响中间图片大小

    // 生成二维码
    public   Bitmap createQRCode(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = CodeCreator.createQRCode(url);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    // 生成二维码 带logo 形式的
    public Bitmap createQRCode(String string, Bitmap mBitmap, BarcodeFormat format) throws WriterException {
        string = "http://www.baidu.com";
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);// 设置缩放信息
        // 将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable hst = new Hashtable();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);// 生成二维码矩阵信息
        int width = matrix.getWidth();// 矩阵高度
        int height = matrix.getHeight();// 矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];// 定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {// 从行开始迭代矩阵
            for (int x = 0; x < width; x++) {// 迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {// 该位置用于存放图片信息
                    // 记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {// 如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;// 记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    // 扫描二维码,调用
    public void codeScan(Activity activity) {
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    // 生成二维码文字链接 在OnActivityResult 直接调用便可以了
    public String codeScanStringResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (null != data) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                // qrCoded.setText("解码结果： \n" + content);
                // qrCodeImage.setImageBitmap(bitmap);
                return content;
            }

        }
        return null;
    }

    // 生成二维码图片链接在OnActivityResult 直接调用便可以了
    public Bitmap codeScanBitmapResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (null != data) {
                // String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                // qrCoded.setText("解码结果： \n" + content);
                // qrCodeImage.setImageBitmap(bitmap);
                return bitmap;
            }

        }
        return null;
    }

}
