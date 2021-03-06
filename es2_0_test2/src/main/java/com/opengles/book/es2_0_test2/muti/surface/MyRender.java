package com.opengles.book.es2_0_test2.muti.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.opengles.book.es2_0_test2.R;
import com.opengles.book.es2_0_test2.eglUtils.EglSurfaceView;
import com.opengles.book.es2_0_test2.utils.BufferUtils;
import com.opengles.book.es2_0_test2.utils.EasyGlUtils;
import com.opengles.book.es2_0_test2.utils.MatrixUtils;
import com.opengles.book.es2_0_test2.utils.ShaderUtils;
import com.opengles.book.es2_0_test2.utils.TextureUtils;

import java.nio.FloatBuffer;

/**
 * author: ycl
 * date: 2019-01-04 10:57
 * desc:
 */
public class MyRender implements EglSurfaceView.EglRenderer {
    private static final String TAG = "MyRender";
    private Context context;
    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f,

            -0.5f, -0.5f,
            0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f

    };
    private FloatBuffer vertexBuffer;

    private float[] fragmentData = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f

    };
    private FloatBuffer fragmentBuffer;


    private int program;
    private int vPosition;
    private int fPosition;
    private int textureid;
    private int sampler;

    private int umatrix;
    private float[] matrix = new float[16];

    private int vboId;
    private int fboId;

    private int imgTextureId;
    private int imgTextureId2;

    private int width;
    private int height;

    private int imgWidth;
    private int imgHeight;

    private MyTextureRender mTextureRender;
    private OnRenderCreateListener onRenderCreateListener;


    public MyRender(Context context) {
        this.context = context;
        mTextureRender = new MyTextureRender(context);

        vertexBuffer = BufferUtils.arr2FloatBuffer(vertexData);
        fragmentBuffer = BufferUtils.arr2FloatBuffer(fragmentData);
    }

    @Override
    public void onSurfaceCreated() {
        mTextureRender.onCreate();

        program = ShaderUtils.createProgram(ShaderUtils.readRawTextFile(context, R.raw.vertex_shader_m),
                ShaderUtils.readRawTextFile(context, R.raw.fragment_shader));

        vPosition = GLES20.glGetAttribLocation(program, "v_Position");
        fPosition = GLES20.glGetAttribLocation(program, "f_Position");
        sampler = GLES20.glGetUniformLocation(program, "sTexture");
        umatrix = GLES20.glGetUniformLocation(program, "u_Matrix");

        vboId = EasyGlUtils.getVboId(vertexData, fragmentData, vertexBuffer, fragmentBuffer);
        // 缓冲区
        textureid = TextureUtils.genTexturesWithParameter(1, 0, GLES20.GL_RGBA, 720, 1280)[0];

        // 激活一个通道，绘制图片即可
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(sampler, 0);


        fboId = EasyGlUtils.getFboId(textureid);


        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.muti);
        imgWidth = b.getWidth();
        imgHeight = b.getHeight();

        imgTextureId = TextureUtils.genTexturesWithParameter(1, 0, b)[0];
        imgTextureId2 = TextureUtils.genTexturesWithParameter(1, 0, b)[0];

        if (onRenderCreateListener != null) {
            onRenderCreateListener.onCreate(textureid);
        }
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        this.width = width;
        this.height = height;

        Log.d(TAG, "onSurfaceChanged:width " + width + " height " + height + " imgWidth " + imgWidth + " imgHeight " + imgHeight);
        MatrixUtils.getCenterMatrix(matrix, imgWidth, imgHeight, 720f, 1280f);
        Matrix.rotateM(matrix, 0, 180, 1, 0, 0);
    }

    @Override
    public void onDrawFrame() {

        GLES20.glViewport(0, 0, 720, 1280);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f, 0f, 0f, 1f);

        GLES20.glUseProgram(program);
        GLES20.glUniformMatrix4fv(umatrix, 1, false, matrix, 0);

        // 顶点缓冲
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);

        //绘制第一张图片
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imgTextureId);

        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                0);
        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //绘制第二张图片
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imgTextureId2);
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length / 2 * 4);
        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);


        GLES20.glDisableVertexAttribArray(vPosition);
        GLES20.glDisableVertexAttribArray(fPosition);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0); // 解绑texture

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);


        // 真实图片绘制
        mTextureRender.onChange(width, height);
        mTextureRender.onDraw(textureid);
    }


    public void setOnRenderCreateListener(OnRenderCreateListener onRenderCreateListener) {
        this.onRenderCreateListener = onRenderCreateListener;
    }

    public interface OnRenderCreateListener {
        void onCreate(int textureId);
    }
}
