package com.mygdx.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;




//Only useful if you wanna crate your own terrain
//what we could do, would be saving the vertices and the nodes of a changed terrain and then create a new mesh from that

public class terrainGraph {
	ArrayList<pointNode> leafs;
	
	private static float[] vertices;
	private static short[] indices;
	int length,width,rectSideLength; 
	
	
	
	
	public terrainGraph(int width, int length, int rectSideLength)	{
		leafs = new ArrayList();
		
		this.width = width;
		this.length = length;
		this.rectSideLength = rectSideLength;
		
		vertices = new float[(width+1)*(length+1)*3];
		indices = new short[width*length*6];
		
		createPoints();
		createConnections();
		//printGraph();
		
	}
	
	
	
	
	public void updateGraph(float[] newVertices)	{
		vertices = newVertices;
		for(int i = 0; i < vertices.length; i+=3)	{
        	addNode(vertices[i], vertices[i+1], vertices[i+2]);
        }

		
	}
	
	public void addNode(float x, float y, float z)	{
		//System.out.println("Creating new Node");
		pointNode newNode = new pointNode(x,y,z);
		leafs.add(newNode);
	}
	
	public void addConnectionToNode(int nodeOne, int nodeTwo)	{
		//System.out.println("Adding Connection\nNode: " + nodeOne + " to " + nodeTwo);
		pointNode newNode = leafs.get(nodeTwo);
		
		leafs.get(nodeOne).addConnected(newNode);
	}
	
	//Probably not necessary if we have the points in the vertices array
	public float[] getPointsAsCoord()	{
		float[] returnVals = new float[leafs.size() * 3];
		float[] tempTable;
		int offset = 0;
		for(int i = 0; i < returnVals.length; i+= 3)	{
			tempTable = leafs.get(offset).getCoordinates();
			returnVals[i] = tempTable[i];
			returnVals[i+1] = tempTable[i+1];
			returnVals[i+2] = tempTable[i+2];
			offset++;
		}

		return returnVals;
	}
	
	public void createPoints()	{
		int gridY1 = length + 1;
		int gridX1 = width + 1;
		
		int offset = 0;
		for (int iy = 0; iy < gridY1; iy++) {
            float y = iy * rectSideLength;
            for (int ix = 0; ix < gridX1; ix++) {
            	//System.out.println("Offset: " + offset);
                float x = ix * rectSideLength;

                vertices[offset] = x;
                vertices[offset + 1] = -y;
                vertices[offset + 2] = 0;

                offset += 3;
            }
        }
		
		//Add points to graph
		for(int i = 0; i < vertices.length; i+=3)	{
        	addNode(vertices[i], vertices[i+1], vertices[i+2]);
        }
	}
	
	public void createConnections()	{
		int offset = 0;
		int gridX1 = width + 1;
		int gridY1 = length + 1;
		
        for (int iy = 0; iy < length; iy++) {
            for (int ix = 0; ix < width; ix++) {
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
        
        //Adding connections to Graph
        //Useful for increasing height and such, but not really vertex connection
        for(int i = 0; i < indices.length; i+=3)	{
        	addConnectionToNode(indices[i], indices[i+1]);
        	addConnectionToNode(indices[i], indices[i+2]);
        	
        	addConnectionToNode(indices[i+1], indices[i]);
        	addConnectionToNode(indices[i+1], indices[i+2]);
        	
        	addConnectionToNode(indices[i+2], indices[i]);
        	addConnectionToNode(indices[i+2], indices[i+1]);
        }
        
	}
	
	public void printGraph()	{
		for(int i = 0; i < leafs.size(); i++)	{
			System.out.println("Node: " + i);
			leafs.get(i).printCoord();
		}
	}
	
	
	public void increaseHeightSimple()	{
		
	}
	
	//get closets faces to a certain position
	//Thbis method only returns the positions in the vertioces array, would propbly be more useful tpo return an array of point nodes;
	public int[] getClosest(int x, int y)	{
		return new int[3];
	}
	
	public void getClosestPoints(float x, float y)	{
		pointNode[] closest = {leafs.get(0), leafs.get(1), leafs.get(2), leafs.get(3)};
		float[] absDistances = new float[4];
		

		
		int j=0;
		float absDist = 0;
		float[] tempCoords; 
		
		//Calculate current closest points
		while(j < closest.length)	{
			tempCoords = closest[j].getCoordinates();
			absDist = (float) (Math.pow(x-tempCoords[0],2) - Math.pow(y-tempCoords[1],2));
			absDistances[j] = absDist;
			j++;
		}
		

		
	}
	

	
	public float absolDist(pointNode node, float xTar, float yTar)	{
		float[] nodeCor = node.getCoordinates();
		float absDist =  (float) Math.sqrt(((float) Math.pow(xTar-nodeCor[0],2)) - ((float)Math.pow(yTar-nodeCor[1],2)));
		return absDist;
	}
	
	public static Mesh getMesh()	{
		
		Mesh finalTerrain = new Mesh(true,
                vertices.length / 3,
                indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
		finalTerrain.setVertices(vertices);
		finalTerrain.setIndices(indices);
        
		if(finalTerrain != null){
			return finalTerrain;
		}
		else
			return null;
	}

public void raiseInRange(float xStart, float xLimit, float yStart, float yLimit, float amount)	{
		
		float[] tempCoord;
		int number = 0;
		pointNode temp;
		
		for(int i = 0; i < leafs.size(); i++)	{
			tempCoord = leafs.get(i).getCoordinates();
			if(tempCoord[0] >= xStart &&  tempCoord[0] <= xLimit && -tempCoord[1] >= yStart &&  -tempCoord[1] <= yLimit)	{
				number++;
				temp = leafs.get(i);
				temp.changeHeight(amount);
				leafs.set(i, temp);
			}
		}
		
		System.out.println(number + " points were in the given range");
		updateVertices();
	}
	
	public void updateVertices()	{
		float[] newVerticeArray = new float[(width+1)*(length+1)*3];
		float[] temp;
		int offset = 0;
		for(int i = 0; i < leafs.size(); i++)	{
			temp = leafs.get(i).getCoordinates();
			newVerticeArray[offset] = temp[0];
			newVerticeArray[offset+1] = temp[1];
			newVerticeArray[offset+2] = temp[2];
			offset += 3;
		}
		vertices = newVerticeArray;
	}
	
	public static void main(String args[])	{
		terrainGraph terrain = new terrainGraph(20, 20, 10);
		terrain.printGraph();
	}
}
