package planeTest;

import java.util.ArrayList;

public class nodeGraph {
	
	ArrayList<pointNode> leafs;
	public nodeGraph()	{
		leafs = new ArrayList();
	}
	
	public void addNode(float x, float y, float z)	{
		pointNode newNode = new pointNode(x,y,z);
	}
	
	public void addConnectionToNode(int nodeOne, int nodeTwo)	{
		leafs.get(nodeOne).addConnected(leafs.get(nodeTwo));
	}
	
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
	
	
}
