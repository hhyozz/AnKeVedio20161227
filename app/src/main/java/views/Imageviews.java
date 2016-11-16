package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/*  
 * 重写CircleImage，继承ImageView  
 */   
public class Imageviews extends ImageView {   
private Paint paint;
    
    public Imageviews(Context context) {  
        this(context,null);  
    }  
  
    public Imageviews(Context context, AttributeSet attrs) {  
        this(context, attrs,0);  
    }  
  
    public Imageviews(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle); 
        paint  = new Paint();
    }  
  
    /**
     * 绘制圆角矩形图片
     * @author caizhiming
     */
    @Override  
    protected void onDraw(Canvas canvas) {  
  
        Drawable drawable = getDrawable();  
        if (null != drawable) {  
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  
            Bitmap b = getRoundBitmap(bitmap, 8);  
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());  
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            paint.reset();  
            canvas.drawBitmap(b, rectSrc, rectDest, paint);  
  
        } else {  
            super.onDraw(canvas);  
        }  
    }  
  
    /**
     * 获取圆角矩形图片方法
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
                bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
          
        final int color = 0xffFFFFFF;
       
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        int x = bitmap.getWidth(); 
        
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
        
        
    }  
       
}   