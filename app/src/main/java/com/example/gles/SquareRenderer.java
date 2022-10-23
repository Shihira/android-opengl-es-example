package com.example.gles;

import android.opengl.GLES30;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SquareRenderer extends RendererBase {
    // resources
    private int program = 0;
    private int vertexPositionBuffer = 0;
    private int indexBuffer = 0;
    private int vertexStream = 0;

    // bindings
    final int POSITION_LOCATION = 0;
    final int COLOR_BINDING = 0;

    final int squareCoordsDim = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private int indices[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    public SquareRenderer(View view) { super(view); }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);

        program = compileShaders(
                mView.getResources().getString(R.string.triangle_vert),
                mView.getResources().getString(R.string.triangle_frag),
                new String[] {
                    "POSITION_LOCATION",    Integer.toString(POSITION_LOCATION),
                    "COLOR_BINDING",        Integer.toString(COLOR_BINDING),
                });

        vertexPositionBuffer = createBuffer(GLES30.GL_ARRAY_BUFFER, convertToByteBuffer(squareCoords));
        indexBuffer = createBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, convertToByteBuffer(indices));

        vertexStream = createVertexStream(indexBuffer, new int[]{
            POSITION_LOCATION, GLES30.GL_FLOAT, squareCoordsDim, vertexPositionBuffer,
        });
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES30.glUseProgram(program);

        GLES30.glUniform4fv(COLOR_BINDING, 1, new float[] { 1, 0, 0, 1 }, 0);

        GLES30.glBindVertexArray(vertexStream);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length, GLES30.GL_UNSIGNED_INT, 0);
    }
}