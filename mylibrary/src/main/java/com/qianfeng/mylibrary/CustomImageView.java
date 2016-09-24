package com.qianfeng.mylibrary;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 1.在values文件夹中创建一个attrs.xml文件用来存放自定义属性
 */
public class CustomImageView extends ImageView {
    private int shape;
    private final static int CIRCLE = 0;
    private final static int ROUNDRECT = 1;
    private final static int RECT = 2;
    private final static int OVAL = 3;
    private final static int RHOMBUS=4;
    private final static int HEXAGON=5;
    private final static int STAR=6;
    private Paint paint;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        //获取用户设置的shape属性的值
        shape = ta.getInt(R.styleable.CustomImageView_shape, RECT);
        //回收资源
        ta.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //获取用户设置的图片
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        //重置Paint的属性
        paint.reset();
        //获取用户所设置图片对应的Bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        //获取控件的宽高
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        //创建一个和控件宽高大小一致的空白的Bitmap
        Bitmap blankBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        //canvas所绘制的所有图像都将出现在blankBitmap
        Canvas mCanvas = new Canvas(blankBitmap);
        switch (shape) {
            case CIRCLE:
                mCanvas.drawCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2, paint);
                break;
            case ROUNDRECT:
                mCanvas.drawRoundRect(new RectF(0, 0, measuredWidth, measuredHeight), 10, 10, paint);
                break;
            case RECT:
                mCanvas.drawRect(0, 0, measuredWidth, measuredHeight, paint);
                break;
            case OVAL:
                if (Build.VERSION.SDK_INT > 20) {
                    mCanvas.drawOval(0, 0, measuredWidth, measuredHeight, paint);
                } else {
                    mCanvas.drawOval(new RectF(0, 0, measuredWidth, measuredHeight), paint);
                }
                break;
            case RHOMBUS:
                Path path=new Path();
                path.moveTo(measuredWidth/2,0);
                path.lineTo(measuredWidth,measuredHeight/2);
                path.lineTo(measuredWidth/2,measuredHeight);
                path.lineTo(0,measuredHeight/2);
                path.close();
                mCanvas.drawPath(path,paint);
                break;
            case HEXAGON:
                Path path1=new Path();
                path1.moveTo(measuredWidth/2,0);
                path1.lineTo(measuredWidth,measuredHeight/4);
                path1.lineTo(measuredWidth,measuredHeight*0.75f);
                path1.lineTo(measuredWidth/2,measuredHeight);
                path1.lineTo(0,measuredHeight*0.75f);
                path1.lineTo(0,measuredHeight/4);
                path1.close();
                mCanvas.drawPath(path1,paint);
                break;
            case STAR:
                Path path2=new Path();
                path2.moveTo((float) (Math.cos(18)*measuredWidth),
                        0);
                path2.lineTo((float) ((Math.cos(54)+(Math.cos(54)))*measuredWidth),
                        (float) ((-1-(Math.sin(54)))*measuredHeight));
                path2.lineTo(0,
                        (float) ((-1+(Math.sin(18)))*measuredHeight));
                path2.lineTo((float) (2*Math.cos(18)*measuredWidth),
                        (float) ((-1+Math.sin(18))*measuredHeight));
                path2.lineTo((float)((Math.cos(18)-Math.cos(54))*measuredWidth),
                        (float)((-1-Math.sin(54))*measuredHeight));
                path2.close();
                mCanvas.drawPath(path2,paint);
                break;

        }
        //设置paint，使之取图像1和图像2的交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制一个Bitmap图像
        mCanvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawBitmap(blankBitmap, 0, 0, null);
        if (blankBitmap != null && !blankBitmap.isRecycled()) {
            blankBitmap.recycle();
        }
    }
}

