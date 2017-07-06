<font size=4 face="微软雅黑" color=#000000>今天产品突然让我做一个像微信一个可以换样式的二维码功能，在网上找了一下，没有发现有类似的功能，于是决定自己写一个，在此记录一下，希望对有这种需求的开发人员有帮助。</font>

## **1.样式展示** ##

![](/screenshot/Wechat_Style_QRcode.gif)

## **2.原理说明** ##

  a.先生成一张背景色是白色 ，二维码颜色为透明的正式二维码。

  b.将生成的二维码绘制到准备好的二维码背景图片上，让透明的部分渗透出来，这样就形成了一个花式的二维码(二维码上面会有不同的颜色)。
 
  c.将花式二维码绘制到定制的背景图上面，就形成类似微信二维码效果。

## **3.使用到的第三方库** ##

    compile 'com.google.zxing:core:3.2.1'
    compile 'cn.bingoogolapple:bga-qrcodecore:1.1.7@aar'
    compile 'cn.bingoogolapple:bga-zxing:1.1.7@aar'
    compile 'com.alibaba:fastjson:1.2.34'

## **4.核心代码** ##

步骤一：生成二维码



     //二维码中间的图片
     Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
     //生成需要的二维码，将二维码颜色设置成透明，背景设置成白色
     originalQrBitmap = QRCodeEncoder.syncEncodeQRCode(url, BGAQRCodeUtil.dp2px(this, 250), ContextCompat.getColor(this, R.color.transparent), ContextCompat.getColor(this, R.color.white), logoBitmap);




步骤二：绘制花式二维码

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


步骤三：绘制带有背景的花式二维码

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

## **5.总结** ##

  这个就是我个人实现微信换二维码样式的功能，代码中可能有不足的地方，欢迎在留言中支持，如果您有更好的实现方式，可以留言告知，谢谢。
csdn地址：http://blog.csdn.net/lidiwo/article/details/74554934




	
	
