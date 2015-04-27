package pl.grm.ex.entities;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex2f;
import pl.grm.ex.entities.core.Entity2D;

public class RedSquare extends Entity2D {
	private int height;
	private int width;

	public RedSquare() {
		super(222);
		this.height = 10;
		this.width = 10;
		setVisible();
	}

	@Override
	public void render() {
		glPushMatrix();
		glColor3f(1f, 0f, 0f);
		glBegin(GL_QUADS);
		glVertex2f(position.getX(), position.getY());
		glVertex2f(position.getX(), position.getY() + height);
		glVertex2f(position.getX() + width, position.getY() + height);
		glVertex2f(position.getX() + width, position.getY());
		glEnd();
		glPopMatrix();
	}

	@Override
	public void update() {
		if (position.getX() < 500) {
			position.move(5, 0);
		}
	}

	public void setSize(int size) {
		if (size >= 0) {
			this.height = size;
			this.width = size;
		}
	}
}
