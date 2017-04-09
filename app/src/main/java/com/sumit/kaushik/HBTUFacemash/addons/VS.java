package com.sumit.kaushik.HBTUFacemash.addons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Pranav on 3/16/2016.
 */
public class VS extends View {
    public VS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public VS(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public VS(Context context) {
        super(context);
    }

    Paint r=new Paint();
    //Paint p=new Paint();
float y=0;
   /// int a=100%255;
   // int g=100%255;
   // int b=100%255;
    int flag=0;
    //float x;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width=this.getMeasuredWidth();
        float height=this.getMeasuredHeight();
        //r.setColor(Color.RED);
       // r.setARGB(100,a,g,b);
        r.setColor(Color.argb(255,63,81,181));
       r.setStrokeWidth(10);
       // canvas.drawLine((right-left)/2,up,(right-left)/2,(up-down)/2,p);
        canvas.drawLine(width/2,0,width/2,y,r);
        canvas.drawLine(width / 2, height, width / 2, height - y, r);
        if(y<(height/2)&&flag==0){
          y++;
            if(y==(height/2)) {
                flag = 1;
                y++;
            }
        }else if(y>=(height/2)||flag==1){
            canvas.save();
            y--;
            if(y==0){
                flag=0;
            }
        }

        invalidate();
    }
}
