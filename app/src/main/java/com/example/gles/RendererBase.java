package com.example.gles;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RendererBase implements GLSurfaceView.Renderer {
    protected View mView;

    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    public RendererBase(View view) {
        mView = view;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
    }

    public static int compileShaders(String vertexShaderCode, String fragmentShaderCode, String[] defines){
        StringBuilder sbDefines = new StringBuilder();
        //sbDefines.append("#version 300 es\n");
        for (int i = 0; i < defines.length / 2; ++i) {
            sbDefines.append(String.format("#define %s %s\n", defines[i * 2], defines[i * 2 + 1]));
        }
        sbDefines.append("#line 1\n");

        int vertexShader = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);
        GLES30.glShaderSource(vertexShader, sbDefines.toString() + vertexShaderCode);
        GLES30.glCompileShader(vertexShader);

        int fragmentShader = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);
        GLES30.glShaderSource(fragmentShader, sbDefines.toString() + fragmentShaderCode);
        GLES30.glCompileShader(fragmentShader);

        // create empty OpenGL ES Program
        int program = GLES30.glCreateProgram();
        // add the vertex shader to program
        GLES30.glAttachShader(program, vertexShader);
        // add the fragment shader to program
        GLES30.glAttachShader(program, fragmentShader);
        // creates OpenGL ES program executables
        GLES30.glLinkProgram(program);

        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);

        return program;
    }

    public static ByteBuffer convertToByteBuffer(int[] ints) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
               ints.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        IntBuffer ib = bb.asIntBuffer();
        ib.put(ints);
        ib.position(0);

        return bb;
    }

    public static ByteBuffer convertToByteBuffer(float[] floats) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                floats.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(floats);
        fb.position(0);

        return bb;
    }

    public static int createBuffer(int type, ByteBuffer buffer) {
        // allocate buffer
        int[] IdBuffer = new int[1];
        GLES30.glGenBuffers(1, IdBuffer, 0);

        // fill buffer with data
        GLES30.glBindBuffer(type, IdBuffer[0]);
        GLES30.glBufferData(type, buffer.capacity(), buffer, GLES30.GL_STATIC_DRAW);

        return IdBuffer[0];
    }

    public static int createVertexStream(int indexBuffer, int[] layout /* location, type, size, buffer_id */) {
        int[] IdBuffer = new int[1];
        GLES30.glGenVertexArrays(1, IdBuffer, 0);
        GLES30.glBindVertexArray(IdBuffer[0]);

        for (int i = 0; i < layout.length / 4; ++i) {
            int location = layout[i * 4 + 0];
            int type = layout[i * 4 + 1];
            int size = layout[i * 4 + 2];
            int bufferId = layout[i * 4 + 3];

            GLES30.glEnableVertexAttribArray(location);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferId);
            GLES30.glVertexAttribPointer(location, size, type, false, 0, 0);
        }

        if (indexBuffer != 0) {
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        }

        GLES30.glBindVertexArray(0);
        return IdBuffer[0];
    }
}
