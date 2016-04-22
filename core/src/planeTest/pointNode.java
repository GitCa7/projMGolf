package planeTest;

import java.util.ArrayList;

public class pointNode {
	
	ArrayList<pointNode> connected;
	int ID;
	private float x,y,z;
	
	public pointNode(float x, float y, float z)	{
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float[] getCoordinates()	{
		float[] retVal = {x, y, z};
		return retVal;
	}
	
	
	public void addConnected(pointNode newNode)	{
		connected.add(newNode);
	}
	
	
	
	

}

