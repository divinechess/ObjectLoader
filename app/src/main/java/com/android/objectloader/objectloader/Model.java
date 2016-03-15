package com.android.objectloader.objectloader;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class Model {

    // Constants
    // Note all private variable changed from private
    public static final int FLOAT_SIZE_BYTES = 4;
    public static final int SHORT_SIZE_BYTES = 2;

    public FloatBuffer _vb;
    public FloatBuffer _nb;
    public ShortBuffer _ib;
    public FloatBuffer _tcb;

    public short[] indices;

    public float[] tempV;
    public float[] tempVt;
    public float[] tempVn;

    public ArrayList<Vector3D> vertices;
    public ArrayList<Vector3D> vertexTexture;
    public ArrayList<Vector3D> vertexNormal;
    public ArrayList<Face> faces;
    public int vertexCount;

    public ArrayList<GroupObject> groupObjects;

    class Vector3D {

        float x, y, z;

        public Vector3D(float parseFloat, float parseFloat2, float parseFloat3) {
            x = parseFloat;
            y = parseFloat2;
            z = parseFloat3;
        }

        public float getX() {

            return x;
        }

        public float getY() {

            return y;
        }

        public float getZ() {

            return z;
        }


    }
}