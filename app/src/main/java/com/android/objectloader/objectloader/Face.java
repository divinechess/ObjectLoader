package com.android.objectloader.objectloader;

import java.util.ArrayList;

class Face extends Model {

    public ArrayList<Model.Vector3D> getVertices() {

        return vertices;
    }

    public ArrayList<Model.Vector3D> getUvws() {

        return vertexTexture;
    }

    public ArrayList<Model.Vector3D> getNormals() {

        return vertexNormal;
    }

}