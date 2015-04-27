package pl.grm.ex.entities.core;

public interface Entity extends Comparable<Entity> {

	public void render();

	public void update();

	public int getID();
}
