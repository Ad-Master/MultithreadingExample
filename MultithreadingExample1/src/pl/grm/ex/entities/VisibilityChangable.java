package pl.grm.ex.entities;

public interface VisibilityChangable {

	public void setVisible();

	public void setInvisible();

	public boolean isVisible();

	public void toggle();

	public void setSwitchable(boolean switchable);

	public boolean isSwitchable();
}
