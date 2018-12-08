package com.opengles.book.es2_0.utils;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * author:  ycl
 * date:  2018/11/30 18:54
 * desc:
 */
public class EasyGlUtils {
    EasyGlUtils() {
    }

    public static void useTexParameter() {
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    public static void useTexParameter(int gl_wrap_s, int gl_wrap_t, int gl_min_filter,
                                       int gl_mag_filter) {
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, gl_wrap_s);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, gl_wrap_t);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, gl_min_filter);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, gl_mag_filter);
    }

    public static void genTexturesWithParameter(int size, int[] textures, int start,
                                                int gl_format, int width, int height) {
        GLES20.glGenTextures(size, textures, start);
        for (int i = 0; i < size; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, gl_format, width, height,
                    0, gl_format, GLES20.GL_UNSIGNED_BYTE, null);
            useTexParameter();
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public static void genTexturesWithParameter(int size, int[] textures, int start, Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            GLES20.glGenTextures(size, textures, start);
            for (int i = 0; i < size; i++) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
                useTexParameter();
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    public static int[] genTexturesWithParameter(int size, int start, Bitmap... bitmap) {
        int[] texture = new int[size];
        GLES20.glGenTextures(size, texture, start);
            for (int i = 0; i < size; i++) {
                if (bitmap[i] != null && !bitmap[i].isRecycled()) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[i]);
                useTexParameter();
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap[i], 0);
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
        return texture;
    }


    public static void bindFrameTexture(int frameBufferId, int textureId) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, textureId, 0);
    }

    public static void unBindFrameBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

}