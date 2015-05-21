package com.facebook.fresco.sample;

import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.Postprocessor;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by sminger on 15-5-21.
 */
public class MyPostprocessor extends BasePostprocessor {
    private int mColor = Color.TRANSPARENT;

    public MyPostprocessor(){
        mColor = Color.RED;
    }
    public void setColor(int color) {
        mColor = color;
        //update();
    }

    @Override
    public String getName() {
        return "meshPostprocessor";
    }

    @Override
    public CloseableReference<Bitmap> process(Bitmap bitmap, PlatformBitmapFactory bitmapFactory) {
        CloseableReference<Bitmap> destBitmapRef = bitmapFactory.createBitmap(bitmap.getWidth(),bitmap.getHeight());
        Bitmap destBitmap = destBitmapRef.get();
        for (int x = 0; x < bitmap.getWidth(); x+=20) {
            for (int y = 0; y < bitmap.getHeight(); y+=20) {
                destBitmap.setPixel(x, y, mColor);
            }
        }
        return CloseableReference.cloneOrNull(destBitmapRef);
    }
}
