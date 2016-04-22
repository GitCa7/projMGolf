package com.mygdx.map;

import java.util.ArrayList;

public class pointNode {
	
	ArrayList<pointNode> connected;
	int ID;
	float x,y,z;
	
	public pointNode(int ID, float x, float y, float z)	{
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public ArrayList<pointNode> getConnected()	{
		return connected;
	}
	
	public void addConnected(pointNode newNode)	{
		connected.add(newNode);
	}

}
