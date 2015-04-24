package pl.grm.ex.entities;

public interface Entity extends Comparable<Entity> {

	public void render();

	public void update();

	public int getID();
}
