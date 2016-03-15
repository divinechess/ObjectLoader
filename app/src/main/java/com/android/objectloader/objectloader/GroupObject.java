package com.android.objectloader.objectloader;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

class GroupObject  extends Model {

    String objectName;

    public void setObjectName(String string) {
        this.objectName = string;
    }

    public String getObjectName() {
        return objectName;
    }



// Android Stuff!
private Context context;
private int modelID;

    public Model(int modelID, Context activity) {
        this.vertices = new ArrayList<Model.Vector3D>();
        this.vertexTexture = new ArrayList<Model.Vector3D>();
        this.vertexNormal = new ArrayList<Model.Vector3D>();
        this.faces = new ArrayList<Face>();

        this.groupObjects = new ArrayList<GroupObject>();

        this.modelID = modelID;
        this.context = activity;

        loadFile();
    }

    private int loadFile() {
        InputStream inputStream = context.getResources().openRawResource(
                modelID);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                inputStream));

        try {
            loadOBJ(in);
            Log.d("LOADING FILE", "FILE LOADED SUCESSFULLY====================");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 1;
    }

    private void loadOBJ(BufferedReader in) throws IOException {
        Log.d("LOADING FILE", "STARTING!====================");
        GroupObject defaultObject = new GroupObject();
        GroupObject currentObject = defaultObject;

        this.groupObjects.add(defaultObject);

        String Line; // Stores ever line we read!
        String[] Blocks; // Stores string fragments after the split!!
        String CommandBlock; // Stores Command Blocks such as: v, vt, vn, g,
        // etc...

        while ((Line = in.readLine()) != null) {
            Blocks = Line.split(" ");
            CommandBlock = Blocks[0];

            // Log.d("COMMAND BLOCK" , "---------- " + CommandBlock +
            // " ----------");

            if (CommandBlock.equals("g")) {
                if (Blocks[1] == "default")
                    currentObject = defaultObject;
                else {
                    GroupObject groupObject = new GroupObject();
                    groupObject.setObjectName(Blocks[1]);
                    currentObject = groupObject;
                    groupObjects.add(groupObject);
                }
            }

            if (CommandBlock.equals("v")) {
                Model.Vector3D vertex = new Model.Vector3D(Float.parseFloat(Blocks[1]),
                        Float.parseFloat(Blocks[2]),
                        Float.parseFloat(Blocks[3]));
                this.vertices.add(vertex);
                // Log.d("VERTEX DATA", " " + vertex.getX() + ", " +
                // vertex.getY() + ", " + vertex.getZ());
            }

            if (CommandBlock.equals("vt")) {
                Model.Vector3D vertexTex = new Model.Vector3D(Float.parseFloat(Blocks[1]),
                        Float.parseFloat(Blocks[2]), 0.0f);
                this.vertexTexture.add(vertexTex);
                // Log.d("TEXTURE DATA", " " + vertexTex.getX() + ", " +
                // vertexTex.getY() + ", " + vertexTex.getZ());
            }

            if (CommandBlock.equals("vn")) {
                Model.Vector3D vertexNorm = new Model.Vector3D(Float.parseFloat(Blocks[1]),
                        Float.parseFloat(Blocks[2]),
                        Float.parseFloat(Blocks[3]));
                this.vertexNormal.add(vertexNorm);
                // Log.d("NORMAL DATA", " " + vertexNorm.getX() + ", " +
                // vertexNorm.getY() + ", " + vertexNorm.getZ());
            }

            if (CommandBlock.equals("f")) {
                Face face = new Face();
                faces.add(face);

                String[] faceParams;

                for (int i = 1; i < Blocks.length; i++) {
                    faceParams = Blocks[i].split("/");

                    face.getVertices()
                            .add(this.vertices.get(Integer
                                    .parseInt(faceParams[0]) - 1));

                    if (faceParams[1] == "") {
                    } else {
                        face.getUvws().add(
                                this.vertexTexture.get(Integer
                                        .parseInt(faceParams[1]) - 1));
                        face.getNormals().add(
                                this.vertexNormal.get(Integer
                                        .parseInt(faceParams[2]) - 1));
                    }
                }
            }
        }

        // fillInBuffers();
        fillInBuffersWithNormals();

        Log.d("OBJ OBJECT DATA", "V = " + vertices.size() + " VN = "
                + vertexTexture.size() + " VT = " + vertexNormal.size());

    }

    private void fillInBuffers() {

        int facesSize = faces.size();

        vertexCount = facesSize * 3;

        tempV = new float[facesSize * 3 * 3];
        tempVt = new float[facesSize * 2 * 3];
        indices = new short[facesSize * 3];

        for (int i = 0; i < facesSize; i++) {
            Face face = faces.get(i);
            tempV[i * 9] = face.getVertices().get(0).getX();
            tempV[i * 9 + 1] = face.getVertices().get(0).getY();
            tempV[i * 9 + 2] = face.getVertices().get(0).getZ();
            tempV[i * 9 + 3] = face.getVertices().get(1).getX();
            tempV[i * 9 + 4] = face.getVertices().get(1).getY();
            tempV[i * 9 + 5] = face.getVertices().get(1).getZ();
            tempV[i * 9 + 6] = face.getVertices().get(2).getX();
            tempV[i * 9 + 7] = face.getVertices().get(2).getY();
            tempV[i * 9 + 8] = face.getVertices().get(2).getZ();
            tempVt[i * 6] = face.getUvws().get(0).getX();
            tempVt[i * 6 + 1] = face.getUvws().get(0).getY();
            tempVt[i * 6 + 2] = face.getUvws().get(1).getX();
            tempVt[i * 6 + 3] = face.getUvws().get(1).getY();
            tempVt[i * 6 + 4] = face.getUvws().get(2).getX();
            tempVt[i * 6 + 5] = face.getUvws().get(2).getY();
            indices[i * 3] = (short) (i * 3);
            indices[i * 3 + 1] = (short) (i * 3 + 1);
            indices[i * 3 + 2] = (short) (i * 3 + 2);
        }

        _vb = ByteBuffer.allocateDirect(tempV.length * Model.FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        _vb.put(tempV);
        _vb.position(0);

        _tcb = ByteBuffer.allocateDirect(tempVt.length * Model.FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        _tcb.put(tempVt);
        _tcb.position(0);

        _ib = ByteBuffer.allocateDirect(indices.length * Model.SHORT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        _ib.put(indices);
        _ib.position(0);
    }

    private void fillInBuffersWithNormals() {

        int facesSize = faces.size();

        vertexCount = facesSize * 3;

        tempV = new float[facesSize * 3 * 3];
        tempVt = new float[facesSize * 2 * 3];
        tempVn = new float[facesSize * 3 * 3];
        indices = new short[facesSize * 3];

        for (int i = 0; i < facesSize; i++) {
            Face face = faces.get(i);
            tempV[i * 9] = face.getVertices().get(0).getX();
            tempV[i * 9 + 1] = face.getVertices().get(0).getY();
            tempV[i * 9 + 2] = face.getVertices().get(0).getZ();
            tempV[i * 9 + 3] = face.getVertices().get(1).getX();
            tempV[i * 9 + 4] = face.getVertices().get(1).getY();
            tempV[i * 9 + 5] = face.getVertices().get(1).getZ();
            tempV[i * 9 + 6] = face.getVertices().get(2).getX();
            tempV[i * 9 + 7] = face.getVertices().get(2).getY();
            tempV[i * 9 + 8] = face.getVertices().get(2).getZ();

            tempVn[i * 9] = face.getNormals().get(0).getX();
            tempVn[i * 9 + 1] = face.getNormals().get(0).getY();
            tempVn[i * 9 + 2] = face.getNormals().get(0).getZ();
            tempVn[i * 9 + 3] = face.getNormals().get(1).getX();
            tempVn[i * 9 + 4] = face.getNormals().get(1).getY();
            tempVn[i * 9 + 5] = face.getNormals().get(1).getZ();
            tempVn[i * 9 + 6] = face.getNormals().get(2).getX();
            tempVn[i * 9 + 7] = face.getNormals().get(2).getY();
            tempVn[i * 9 + 8] = face.getNormals().get(2).getZ();

            tempVt[i * 6] = face.getUvws().get(0).getX();
            tempVt[i * 6 + 1] = face.getUvws().get(0).getY();
            tempVt[i * 6 + 2] = face.getUvws().get(1).getX();
            tempVt[i * 6 + 3] = face.getUvws().get(1).getY();
            tempVt[i * 6 + 4] = face.getUvws().get(2).getX();
            tempVt[i * 6 + 5] = face.getUvws().get(2).getY();

            indices[i * 3] = (short) (i * 3);
            indices[i * 3 + 1] = (short) (i * 3 + 1);
            indices[i * 3 + 2] = (short) (i * 3 + 2);
        }

        _vb = ByteBuffer.allocateDirect(tempV.length * Model.FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        _vb.put(tempV);
        _vb.position(0);

        _tcb = ByteBuffer.allocateDirect(tempVt.length * Model.FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        _tcb.put(tempVt);
        _tcb.position(0);

        _nb = ByteBuffer.allocateDirect(tempVn.length * Model.FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        _nb.put(tempVn);
        _nb.position(0);

        _ib = ByteBuffer.allocateDirect(indices.length * Model.SHORT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        _ib.put(indices);
        _ib.position(0);
    }

    public  FloatBuffer getVertices() {
        return _vb;
    }

    public FloatBuffer getTexCoords() {
        return _tcb;
    }

    public ShortBuffer getIndices() {
        return _ib;
    }

    public FloatBuffer getNormals() {
        return _nb;
    }





}