package pl.grm.ex.entities.core;

public class Position {
	private int dimensions;
	private int x, y, z;

	public Position(int x, int y) {
		this.dimensions = 2;
		this.x = x;
		this.y = y;
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public void move(int i, int j) {
		this.x += i;
		this.y += j;
	}

}
