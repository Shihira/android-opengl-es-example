package com.example.gles;

import android.opengl.GLES30;
import android.view.View;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer extends RendererBase {
    // resources
    private int program = 0;
    private int vertexPositionBuffer = 0;
    private int vertexStream = 0;

    // bindings
    final int POSITION_LOCATION = 0;
    final int COLOR_BINDING = 0;

    final int triangleCoordDim = 3; // vec3
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    private FloatBuffer vertexBufferData;

    public TriangleRenderer(View view) { super(view); }

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

        vertexPositionBuffer = createBuffer(GLES30.GL_ARRAY_BUFFER, convertToByteBuffer(triangleCoords));

        vertexStream = createVertexStream(0, new int[]{
            POSITION_LOCATION, GLES30.GL_FLOAT, triangleCoordDim, vertexPositionBuffer,
        });
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES30.glUseProgram(program);

        GLES30.glUniform4fv(COLOR_BINDING, 1, new float[] { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f }, 0);

        GLES30.glBindVertexArray(vertexStream);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, triangleCoords.length / triangleCoordDim);
    }
}