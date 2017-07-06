package com.lidiwo.wechat_style_qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * *****************************************************
 *
 * @author：lidi
 * @date：2017/7/5 15:37
 * @Company：智能程序员
 * @Description： *****************************************************
 */
public class MyUtils {

    /**
     * @param context
     * @param bgBitmap 背景图片
     * @param qrbgBitmap  二维码背景
     * @param originalQrBitmap 原始二维码
     * @param assertFileName  尺寸资源名字
     * @return
     */
    public static Bitmap drawStyleQRcode(Context context, Bitmap bgBitmap,Bitmap qrbgBitmap,Bitmap originalQrBitmap,String assertFileName) {

        Bitmap qrBitmap= drawQRcode(qrbgBitmap,originalQrBitmap);//花式二维码
        String json = readAssertResource(context, assertFileName);//尺寸json

        DrawBean bean = json2Bean(json, DrawBean.class);
        int size = bean.getSize();
        int x = bean.getX();
        int y = bean.getY();

        Bitmap bitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        //绘制背景
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bgBitmap, null, rect, null);

        //绘制二维码
        Rect mRectDst1 = new Rect(x, y, x + size, y + size);//绘制头像的位置
        canvas.drawBitmap(qrBitmap, null, mRectDst1, null);
        return bitmap;
    }


    /**
     * @param qrbgBitmap 二维码背景图片
     * @param originalQrBitmap 原始二维码图片
     * @return 花式二维码
     */
    private static Bitmap drawQRcode(Bitmap qrbgBitmap, Bitmap originalQrBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(originalQrBitmap.getWidth(), originalQrBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(qrbgBitmap, null, rect, null);
        canvas.drawBitmap(originalQrBitmap, null, rect, null);
        return bitmap;

    }



    public static <T> T json2Bean(String json, Class<T> beanClass) {
        T bean = null;
        try {
            bean = JSON.parseObject(json, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


    //把资产目录下的文本文件转变成字符串
    private static String readAssertResource(Context context, String strAssertFileName) {
        AssetManager assetManager = context.getAssets();
        String strResponse = "";
        try {
            InputStream ims = assetManager.open(strAssertFileName);
            strResponse = getStringFromInputStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    private static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }



    //保存二维码

    public static void saveImage(Context context, Bitmap bmp) {
        // 首先保存图片
        String local_file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lidiwo/";
        File appDir = new File(local_file);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String fileName = appDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(fileName);

        try {
            if (file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), "lidiwo");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        Toast.makeText(context, "图片已保存至" + appDir.getAbsolutePath() + "文件夹", Toast.LENGTH_SHORT).show();

    }

}
