package photo.my.freeant.photoimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import photo.my.freeant.photoimageview.R;


public class PhotoImageView extends android.support.v7.widget.AppCompatImageView  {


    private int progress = 30;
    private float wHeight;
    private Context mContext;
    private Bitmap mSrcBitmap;
    private String show_text;
    private int show_text_bg_color;
    private Paint paint = new Paint();
    private Paint paintBg = new Paint();

    private int bg_color_border;
    private RectF rectF = new RectF();
    private RectF rectfBg = new RectF();
    private final int default_text_bg_color = Color.argb(10, 0, 0,0);
    public PhotoImageView(Context Context){

        super(Context);
        this.mContext =Context;
    }
    public PhotoImageView(Context mContext, AttributeSet attrs){
        this(mContext, attrs,0);
        this.mContext =mContext;
    }

    public PhotoImageView(Context mContext, AttributeSet attrs, int defStyleAttr){
        super(mContext,attrs,defStyleAttr);
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PhotoImageView,defStyleAttr,0);
        initAttribute(typedArray);
    }
    protected  void initAttribute(TypedArray typedArray){
        if( typedArray.getString(R.styleable.PhotoImageView_headphoto_text) !=null){
           show_text = typedArray.getString(R.styleable.PhotoImageView_headphoto_text);
        }
        show_text_bg_color =typedArray.getColor(R.styleable.PhotoImageView_headphoto_text_bg,default_text_bg_color);
        bg_color_border  =typedArray.getColor(R.styleable.PhotoImageView_headphoto_bg_border,default_text_bg_color);
    }
    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasure){
        rectfBg.set(5,5,MeasureSpec.getSize(widthMeasureSpec)-5,MeasureSpec.getSize(heightMeasure)-5);
        rectF.set(0+5,0+5,MeasureSpec.getSize(widthMeasureSpec)-5,MeasureSpec.getSize(heightMeasure)-5);

        setMeasuredDimension(widthMeasureSpec,heightMeasure);
    }



    @Override
    public void onDraw(Canvas canvas){
         wHeight =getProgress()/(float)100 *getHeight();
        float radius = getWidth()/2;
        float angle = (float) (Math.acos((radius - wHeight) / radius) * 180 / Math.PI);
        float stangle = angle+90;
        float sweetangle = 360 -2*angle;
        paint.setColor(Color.GREEN);
        paintBg.setColor(bg_color_border);
        paintBg.setStyle(Paint.Style.STROKE);
        paintBg.setStrokeCap(Paint.Cap.ROUND);
        paintBg.setStrokeWidth(10);
        canvas.drawArc(rectfBg,0,360,true,paintBg);
        Bitmap image = drawableToBitmap(getDrawable());
        Bitmap reSizeImage = reSizeImage(image, getWidth(), getHeight());
        canvas.drawBitmap(createRoundImage(reSizeImage, getWidth(), getHeight(), stangle,sweetangle),
                getPaddingLeft(), getPaddingTop(), null);




      //  canvas.drawArc(rectF,stangle,sweetangle,false,paint);
        canvas.save();
        canvas.rotate(180, getWidth() / 2, getHeight() / 2);

        paint.setColor(show_text_bg_color);
        paint.setAlpha(100);
        canvas.drawArc(rectF,270-angle,angle*2,false,paint);

       canvas.restore();

        if(show_text !=null){
            paint.setColor(show_text_bg_color);
            paint.setAlpha(0);
            canvas.drawLine(getWidth()/2,getHeight()-wHeight,getWidth()/2,getHeight(),paint);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.BLACK);
            paint.setTextSize(19);
            canvas.drawText(show_text,getWidth()/2, getHeight()-wHeight+wHeight/2,paint);
        }

       // super.onDraw(canvas);



    }


    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算出缩放比
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 矩阵缩放bitmap
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }


    /**
     * 画圆角
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createRoundImage(Bitmap source, int width, int height,float stangle, float sweetangle ) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
      /*  RectF rect = new RectF(0, 0, width, height);*/

        canvas.drawArc(rectF,0,360,false,paint);



        //  canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);





        return target;
    }

    private int getProgress(){
        return  progress;
    }


    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            if (mSrcBitmap != null) {
                return mSrcBitmap;
            } else {
                return null;
            }
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
