package com.opengles.book.es2_0_test.glsv;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.opengles.book.es2_0_test.base.BaseGLSurfaceView;
import com.opengles.book.es2_0_test.shape.triangle.CameraTriangle;
import com.opengles.book.es2_0_test.shape.triangle.ColorfulTriangle;
import com.opengles.book.es2_0_test.shape.triangle.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 *  绘制各类三角形的GLSurfaceView
 */
public class TriangleGLSurfaceView extends BaseGLSurfaceView {

    public TriangleGLSurfaceView(Context context) {
        super(context);

        // setRenderer(new TriangleRenderer()); // 绘制三角形
        // setRenderer(new CameraTriangleRenderer()); // 绘制摄像机下的三角形
        setRenderer(new ColorfulTriangleRenderer()); // 绘制摄像机下的彩色三角形
    }

    class TriangleRenderer implements GLSurfaceView.Renderer {

        Triangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new Triangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            triangle.draw();
        }
    }


    class CameraTriangleRenderer implements GLSurfaceView.Renderer {

        CameraTriangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new CameraTriangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            triangle.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            triangle.draw();
        }
    }

    class ColorfulTriangleRenderer implements GLSurfaceView.Renderer {

        ColorfulTriangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new ColorfulTriangle();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            triangle.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            triangle.draw();
        }
    }

}
