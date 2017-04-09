package com.sumit.kaushik.HBTUFacemash;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by sumit on 12/6/16.
 */
public class ScalingUtility {
    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight) {
        Rect srcRect=new Rect(0,0,unscaledBitmap.getWidth(),unscaledBitmap.getHeight());
        Rect dstRect=calculateDstRect(unscaledBitmap.getWidth(),unscaledBitmap.getHeight(),1200,720);
        Bitmap scaledBitmap=Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap,srcRect,dstRect,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    private static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {

        final float srcAspect=(float)srcWidth/(float)srcHeight;
        final float dstAspect=(float)dstWidth/(float)dstHeight;
        if(srcAspect>dstAspect){
            return new Rect(0,0,dstWidth,(int)(dstWidth/srcAspect));
        }else
        {
            return new Rect(0,0,(int)(dstHeight*srcAspect),dstHeight);
        }
    }


}
