package com.example.gles;

import android.opengl.GLES30;
import android.view.View;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SquareRenderer extends RendererBase {
    // resources
    private int program = 0;
    private int vertexPositionBuffer = 0;
    private int vertexUVBuffer = 0;
    private int indexBuffer = 0;
    private int vertexStream = 0;
    private int texture = 0;

    // bindings
    final int POSITION_LOCATION = 0;
    final int UV_LOCATION = 1;
    final int COLOR_BINDING = 0;
    final int TEXTURE_BINDING = 1;

    final int squareCoordsDim = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right
    final int uvCoordsDim = 2;
    final float uvCoords[] = {
            0, 1,
            0, 0,
            1, 0,
            1, 1,
    };
    final int textureData[] = {
            0xff0000ff, 0xff00ff00, 0xffff0000,
            0xff00ff00, 0xffff0000, 0xff0000ff,
            0xffffffff, 0xff000000, 0xffffffff,
    };

    private int indices[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    public SquareRenderer(View view) {
        super(view);
    }

    public static int createTexture(int width, int height, ByteBuffer buffer)
    {
        int[] IdBuffer = new int[1];
        GLES30.glGenTextures(1, IdBuffer, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, IdBuffer[0]);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA8, width, height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, buffer);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return IdBuffer[0];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        program = compileShaders(
                mView.getResources().getString(R.string.textured_vert),
                mView.getResources().getString(R.string.textured_frag),
                //mView.getResources().getString(R.string.triangle_frag),
                new String[] {
                    "POSITION_LOCATION",    Integer.toString(POSITION_LOCATION),
                    "UV_LOCATION",          Integer.toString(UV_LOCATION),
                    "COLOR_BINDING",        Integer.toString(COLOR_BINDING),
                    "TEXTURE_BINDING",      Integer.toString(TEXTURE_BINDING),
                });

        vertexPositionBuffer = createBuffer(GLES30.GL_ARRAY_BUFFER, convertToByteBuffer(squareCoords));
        vertexUVBuffer = createBuffer(GLES30.GL_ARRAY_BUFFER, convertToByteBuffer(uvCoords));
        indexBuffer = createBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, convertToByteBuffer(indices));
        texture = createTexture(3, 3, convertToByteBuffer(textureData));

        vertexStream = createVertexStream(indexBuffer, new int[]{
            POSITION_LOCATION, GLES30.GL_FLOAT, squareCoordsDim, vertexPositionBuffer,
            UV_LOCATION, GLES30.GL_FLOAT, uvCoordsDim, vertexUVBuffer,
        });
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES30.glUseProgram(program);

        GLES30.glUniform4fv(COLOR_BINDING, 1, new float[] { 1, 0, 0, 1 }, 0);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture);
        GLES30.glUniform1ui(TEXTURE_BINDING, 0);

        GLES30.glBindVertexArray(vertexStream);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length, GLES30.GL_UNSIGNED_INT, 0);
    }
}