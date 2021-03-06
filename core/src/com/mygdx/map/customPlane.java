package com.mygdx.map;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class customPlane {
	
	static float[] vertices;
	
	
    public static Mesh buildPlane(int width, int height, int numberOfChunks, boolean withHeight) {
    	
        int offset = 0;
        
        
        float width_half = width / 2;
        float height_half = height / 2;
        
        
        int gridX = numberOfChunks;
        int gridY = numberOfChunks;
        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        float segment_width = width / gridX;
        float segment_height = height / gridY;

        vertices = new float[gridX1 * gridY1 * 3];
        short[] indices = new short[gridX * gridY * 6];

        for (int iy = 0; iy < gridY1; iy++) {
            float y = iy * segment_height - height_half;
            for (int ix = 0; ix < gridX1; ix++) {

                float x = ix * segment_width - width_half;

                vertices[offset] = x;
                vertices[offset + 1] = -y;
                vertices[offset + 2] = 0;

                offset += 3;
            }
        }

        offset = 0;
        for (int iy = 0; iy < gridY; iy++) {
            for (int ix = 0; ix < gridX; ix++) {
                short a = (short) (ix + gridX1 * iy);
                short b = (short) (ix + gridX1 * (iy + 1));
                short c = (short) ((ix + 1) + gridX1 * (iy + 1));
                short d = (short) ((ix + 1) + gridX1 * iy);

                indices[offset] = a;
                indices[offset + 1] = b;
                indices[offset + 2] = d;

                indices[offset + 3] = b;
                indices[offset + 4] = c;
                indices[offset + 5] = d;

                offset += 6;

            }
        }
        
        if (withHeight) {
            float[] d = ImprovedNoise.heightData(numberOfChunks);
            offset = 2;
            for (int f = 0; f < vertices.length / 3; f++) {
                vertices[offset] = d[f];
                offset += 3;
            }
        }
		
        //Show meshes
        for(int i = 0; i < 3; i++)	{
        	System.out.print(i + vertices[i] + "\t");
        }
        
        System.out.println("\n");
        //Show indices
        for(int i = 0; i < 6; i++)	{
        	System.out.print(i + indices[i]);
        }
        
        
        
        
        
        Mesh mesh = new Mesh(true,vertices.length / 3,indices.length,new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        return mesh;
    }
    
    


}

