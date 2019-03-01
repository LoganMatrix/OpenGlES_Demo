package com.ycl.es2_0_native.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * author:  ycl
 * date:  2019/3/1 18:51
 * desc:
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private NativeOpengl mNativeOpengl;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getHolder().addCallback(this);

    }

    public void setNativeOpengl(NativeOpengl nativeOpengl) {
        mNativeOpengl = nativeOpengl;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mNativeOpengl != null) {
            mNativeOpengl.surfaceCreate(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}