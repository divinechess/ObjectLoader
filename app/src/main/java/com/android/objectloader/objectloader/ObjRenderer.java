package com.android.objectloader.objectloader;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;


public class ObjRenderer extends GroupObject  implements Renderer {

        private static final String A_POSITION = "a_Position";
        private int aPositionLocation;
        private static final String U_COLOR = "u_Color";
        private int uColorLocation;
        private int program;
        private static final int POSITION_COMPONENT_COUNT = 2;
        private static final int BYTES_PER_FLOAT = 4;
        private static final int BYTES_PER_INT = 4;
        public FloatBuffer vertexData =  null;
        private ShortBuffer indexData = null;
        private Context context = null;
        private int indicesSize;
        private int [ ] indicesRef;

//        public GroupObject GroupObjectReturn; //
        public ObjRenderer(Context context) {
            // this.context = context;

            new Model(R.raw.cube_obj, this.context = context);





//            float[] tableVerticesWithTriangles = {
//                    // Triangle 1
//                    -0.5f, -0.5f,
//                    0.5f,  0.5f,
//                    -0.5f,  0.5f,
//
//                    // Triangle 2
//                    -0.5f, -0.5f,
//                    0.5f, -0.5f,
//                    0.5f,  0.5f,
//
//                    // Line 1
//                    -0.5f, 0f,
//                    0.5f, 0f,
//
//                    // Mallets
//                    0f, -0.25f,
//                    0f,  0.25f
//            };


        }





        @Override
        public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
            // Set the background clear color to red. The first component is
            // red, the second is green, the third is blue, and the last
            // component is alpha, which we don't use in this lesson.
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

            String vertexShaderSource = com.android.objectloader.objectloader.TextResourceReader
                    .readTextFileFromResource(context, R.raw.simple_vertex_shader);
            String fragmentShaderSource = com.android.objectloader.objectloader.TextResourceReader
                    .readTextFileFromResource(context, R.raw.simple_fragment_shader);

            int vertexShader = com.android.objectloader.objectloader.ShaderHelper.compileVertexShader(vertexShaderSource);
            int fragmentShader = com.android.objectloader.objectloader.ShaderHelper.compileFragmentShader(fragmentShaderSource);

            program = com.android.objectloader.objectloader.ShaderHelper.linkProgram(vertexShader, fragmentShader);

            if (LoggerConfig.ON){
                com.android.objectloader.objectloader.ShaderHelper.validateProgram(program);
            }
            glUseProgram(program);

            uColorLocation = glGetUniformLocation(program, U_COLOR);
            aPositionLocation = glGetAttribLocation(program, A_POSITION);
            // Bind our data, specified by the variable vertexData, to the vertex
            // attribute at location A_POSITION_LOCATION.

           // glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
           //       false, 0, vertexData);
            getVertices().position(0);
            GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                    false, 0, getVertices());

            glEnableVertexAttribArray(aPositionLocation);

        }

        @Override
        public void onSurfaceChanged(GL10 glUnused, int width, int height) {
            // Set the OpenGL viewport to fill the entire surface.
            glViewport(0, 0, width, height);
        }
        @Override
        public void onDrawFrame(GL10 glUnused) {
            // Clear the rendering surface.
            glClear(GL_COLOR_BUFFER_BIT);

            glEnableVertexAttribArray(aPositionLocation);
            glUnused.glDrawElements(GL_TRIANGLES, indicesSize, GL_UNSIGNED_INT, );

            //         glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
            glDrawArrays(GL_TRIANGLES, 0, 6);



        }















    }

}
