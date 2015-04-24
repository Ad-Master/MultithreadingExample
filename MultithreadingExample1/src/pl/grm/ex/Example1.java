package pl.grm.ex;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import pl.grm.ex.entities.Entity;
import pl.grm.ex.threads.LWJGLEventMulticaster;
import pl.grm.ex.threads.LogicThread;
import pl.grm.ex.threads.RenderThread;
import pl.grm.ex.timers.FPSTimer;
import pl.grm.ex.timers.TPSTimer;

import com.google.common.collect.HashMultimap;

public class Example1 {
	/** Ticks per second */
	public static final int TPS = 20;
	/** Frames per second */
	public static final int FPS = 60;
	/** Our controller instance object */
	public static Example1 instance;
	/** Render lwjgl thread */
	private RenderThread renderThread;
	/** Logic Thread */
	private LogicThread logicThread;
	/** Specifies that game is running or not */
	private boolean running = false;
	/** Timer to count game FPS and Delta */
	private FPSTimer fpsTimer;
	/** Timer to count game logic tps */
	private TPSTimer tpsTimer;
	/** Stage of game rendering */
	private GameLoadStage gameLoadStage = GameLoadStage.LOADING;
	/** Collection of ingame entities */
	HashMultimap<Integer, Entity> entityMap = HashMultimap.create();

	public static void main(String[] args) {
		instance = new Example1();
		instance.prepare();
		instance.start();
	}

	private Example1() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Example1");
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private void prepare() {
		setFPSTimer(new FPSTimer());
		setTPSTimer(new TPSTimer());
		setLogicThread(new LogicThread());
		setRenderThread(new RenderThread());
	}

	private void start() {
		setRunning(true);
		setGameStage(GameLoadStage.MAIN);
		LWJGLEventMulticaster.init();
		renderThread.start();
		logicThread.start();
		LWJGLEventMulticaster.startMulitCaster();
	}

	public static void stop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName("Closing Thread");
				Example1.instance.setGameStage(GameLoadStage.CLOSING);
				LWJGLEventMulticaster.discharge();
				instance.setRunning(false);
				long initTime = System.currentTimeMillis();
				while (instance.getLogicThread().isAlive()
						|| instance.getRenderThread().isAlive()) {
					try {
						Thread.sleep(100l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					long timeDelay = System.currentTimeMillis() - initTime;
					if (timeDelay > 5 * 1000) {
						System.out.println("Thread rage quit: "
								+ Thread.currentThread().getName());
						System.exit(0);
						break;
					}
				}
			}
		}).start();
	}

	public static void addEntity(Entity entity) {
		instance.entityMap.put(entity.getID(), entity);
	}

	public static HashMultimap<Integer, Entity> getEntities() {
		return instance.entityMap;
	}

	public RenderThread getRenderThread() {
		return renderThread;
	}

	public void setRenderThread(RenderThread renderThread) {
		this.renderThread = renderThread;
	}

	public LogicThread getLogicThread() {
		return logicThread;
	}

	public void setLogicThread(LogicThread logicThread) {
		this.logicThread = logicThread;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public GameLoadStage getGameStage() {
		return gameLoadStage;
	}

	public void setGameStage(GameLoadStage gameLoadStage) {
		this.gameLoadStage = gameLoadStage;
	}

	public FPSTimer getFPSTimer() {
		return fpsTimer;
	}

	public void setFPSTimer(FPSTimer fpsTimer) {
		this.fpsTimer = fpsTimer;
	}

	public TPSTimer getTPSTimer() {
		return tpsTimer;
	}

	public void setTPSTimer(TPSTimer tpsTimer) {
		this.tpsTimer = tpsTimer;
	}
}
