package com.lidiwo.wechat_style_qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class MainActivity extends AppCompatActivity {


    //二维码样式背景图
    private int[] qrBgs = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r, R.drawable.s};

    //二维码背景图
    private static int[] qrBgIds = {R.drawable.aa, R.drawable.kk,
            R.drawable.jj, R.drawable.dd, R.drawable.ee, R.drawable.ff,
            R.drawable.gg, R.drawable.hh, R.drawable.mm, R.drawable.jj,
            R.drawable.kk, R.drawable.ll, R.drawable.mm, R.drawable.aa,
            R.drawable.ll, R.drawable.kk, R.drawable.dd, R.drawable.ee,
            R.drawable.ff, R.drawable.gg};

    //二维码在背景图的位置信息 （和上面二维码的背景图要一一对应）
    private static String[] assertJsons = {"aa.json", "kk.json",
            "jj.json", "dd.json", "ee.json", "ff.json",
            "gg.json", "hh.json", "mm.json", "jj.json",
            "kk.json", "ll.json", "mm.json", "aa.json",
            "ll.json", "kk.json", "dd.json", "ee.json",
            "ff.json", "gg.json"};


    private Random mRandom = new Random();
    private ImageView iv_content;
    private Bitmap originalQrBitmap;
    private Bitmap lastBitmap; //最终的花式二维码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_content=(ImageView)findViewById(R.id.iv_content);
        String url = "https://www.baidu.com/";


        //二维码中间的图片
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //生成需要的二维码，将二维码颜色设置成透明，背景设置成白色
        originalQrBitmap = QRCodeEncoder.syncEncodeQRCode(url, BGAQRCodeUtil.dp2px(this, 250), ContextCompat.getColor(this, R.color.transparent), ContextCompat.getColor(this, R.color.white), logoBitmap);

        creatQRcode();

    }


    public void save(View view) {
        if (lastBitmap != null) {
            MyUtils.saveImage(this, lastBitmap);
        }
    }

    public void change(View view) {
        creatQRcode();
    }

    /**
     * 生成最终的花式二维码
     */
    private void creatQRcode(){
        int result1 = mRandom.nextInt(qrBgs.length);
        Bitmap qrbgBitmap = BitmapFactory.decodeResource(getResources(), qrBgs[result1]);
        int result2 = mRandom.nextInt(qrBgIds.length);
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), qrBgIds[result2]);
        String assertFileName = assertJsons[result2];

        lastBitmap = MyUtils.drawStyleQRcode(this, bgBitmap, qrbgBitmap, originalQrBitmap, assertFileName);
        iv_content.setImageBitmap(lastBitmap);
    }
}
