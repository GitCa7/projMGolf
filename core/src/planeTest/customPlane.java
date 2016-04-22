package planeTest;

public class customPlane {
	
	private int width, length, rectSideLength;
	private nodeGraph terrain;
	private float[] vertices;
	
	public customPlane(int width, int length, int rectSideLength)	{
		this.width = width;
		this.length = length;
		this.rectSideLength = rectSideLength;
		vertices = new float[width*length*3];
	}
	
	
	//neesds to be called before rendering
	public void createVertices()	{
        int gridX1 = width + 1;
        int gridY1 = length + 1;

        //float segment_width = width / gridX;
        //float segment_height = height / gridY;

        short[] indices = new short[width * length * 6];
        int offset = 0;
        for (int iy = 0; iy < gridY1; iy++) {
            float y = iy * rectSideLength;
            for (int ix = 0; ix < gridX1; ix++) {

                float x = ix * rectSideLength;

                vertices[offset] = x;
                vertices[offset + 1] = -y;
                vertices[offset + 2] = 0;

                offset += 3;
            }
        }
		
	}
	
	public void changeTerrain()	{
		
	}
	
	
	


}
