package pl.grm.ex.entities;

public abstract class Entity2D implements Entity, VisibilityChangable {
	protected boolean visible;
	protected int ID = 0;
	protected boolean switchable;
	protected Position position;

	protected Entity2D(int ID) {
		this.ID = ID;
		position = new Position(0, 0);
	}

	public Entity2D(int ID, int x, int y) {
		this.ID = ID;
		position = new Position(x, y);
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void setVisible() {
		this.visible = true;
	}

	@Override
	public void setInvisible() {
		this.visible = false;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void toggle() {
		if (isSwitchable())
			this.visible = !this.visible;
	}

	@Override
	public void setSwitchable(boolean switchable) {
		this.switchable = switchable;
	}

	@Override
	public boolean isSwitchable() {
		return switchable;
	}

	public Position getPosition() {
		return position;
	}
}
