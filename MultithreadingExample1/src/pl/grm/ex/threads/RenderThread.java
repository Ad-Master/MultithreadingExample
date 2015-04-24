package pl.grm.ex.threads;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;

import pl.grm.ex.Example1;
import pl.grm.ex.entities.Entity;
import pl.grm.ex.timers.FPSTimer;

public class RenderThread extends Thread {
	private FPSTimer timer;

	public RenderThread() {
		super("Game-Render-Thread");
		this.timer = Example1.instance.getFPSTimer();
	}

	@Override
	public void run() {
		initRenderer();
		while (Example1.instance.isRunning()) {
			if (Display.isCloseRequested()) {
				Example1.stop();
			}
			loop();
		}
		Display.destroy();
	}

	/**
	 * Called before loop
	 */
	private void initRenderer() {
		this.timer.initTime();
		try {
			Display.create();
			if (!GLContext.getCapabilities().OpenGL11) {
				System.err
						.println("Your OpenGL version doesn't support the required functionality.");
				Example1.stop();
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glScalef(1.0f, 1.0f, 1.0f);
		glOrtho(0, 800, 600, 0, 1, -1);
		glFrustum(-1, 1, -1, 1, 0.0, 40.0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glShadeModel(GL_SMOOTH);
	}

	private void loop() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		switch (Example1.instance.getGameStage()) {
		case LOADING:
			break;
		case MAIN:
			renderEntities();
			break;
		default:
			break;
		}
		Display.update();
		timer.updateFPS();
		Display.sync(Example1.FPS);
	}

	private void renderEntities() {
		Set<Integer> keySet = Example1.getEntities().keySet();
		Iterator<Integer> keySetIterator = keySet.iterator();
		while (keySetIterator.hasNext()) {
			int ID = keySetIterator.next();
			LinkedBlockingQueue<Entity> entityCollection = new LinkedBlockingQueue<>(
					Example1.getEntities(ID));
			while (!entityCollection.isEmpty()) {
				Entity entity = entityCollection.poll();
				entity.render();
			}
		}
	}
}
