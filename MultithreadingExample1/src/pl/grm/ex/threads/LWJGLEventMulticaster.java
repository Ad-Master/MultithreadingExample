package pl.grm.ex.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.lwjgl.input.Keyboard;

import pl.grm.ex.GameEvent;
import pl.grm.ex.inputs.DefaultListeners;
import pl.grm.ex.inputs.GameKeyListener;
import pl.grm.ex.inputs.GameListener;
import pl.grm.ex.inputs.KeyEvent;

public class LWJGLEventMulticaster extends Thread {
	private static ConcurrentHashMap<Integer, ConcurrentLinkedQueue<GameKeyListener>> keyListenersHandler;
	private static LinkedBlockingQueue<GameEvent> gameEventsQueue;
	private static LWJGLEventMulticaster eventCollector;
	private static LWJGLEventMulticaster eventCaster;
	private boolean stopInvoked = false;
	private static boolean initialised = false;
	private boolean finished = false;
	private MCType type;

	private LWJGLEventMulticaster(MCType type) {
		this.type = type;
	}

	public static void init() {
		if (eventCollector == null) {
			eventCollector = new LWJGLEventMulticaster(MCType.COLLECTOR);
		}
		if (eventCaster == null) {
			eventCaster = new LWJGLEventMulticaster(MCType.CASTER);
		}
		if (gameEventsQueue == null) {
			gameEventsQueue = new LinkedBlockingQueue<GameEvent>();
		}
		if (keyListenersHandler == null) {
			keyListenersHandler = new ConcurrentHashMap<Integer, ConcurrentLinkedQueue<GameKeyListener>>();
		}
		keyListenersHandler.putAll(DefaultListeners.getAll());
		setInitialised(true);
	}

	public static boolean startMulitCaster() {
		if (isInitialised()) {
			eventCollector.start();
			eventCaster.start();
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		switch (this.type) {
		case COLLECTOR:
			currentThread().setName("LWJGL Event Collector");
			collectEvents();
			break;
		case CASTER:
			currentThread().setName("LWJGL Event Multicaster");
			castEvents();
			break;
		default:
			break;
		}
	}

	private synchronized void collectEvents() {
		while (!isStopInvoked()) {
			if (!Keyboard.isCreated()) {
				continue;
			}
			while (Keyboard.next()) {
				int eventKey = Keyboard.getEventKey();
				KeyEvent event = new KeyEvent(eventKey,
						Keyboard.getEventKeyState(),
						System.currentTimeMillis(), Keyboard.isRepeatEvent(),
						collectKeyListeners(Keyboard.getEventKey()));
				gameEventsQueue.add(event);
			}
		}
		setFinished(true);
	}

	private List<GameListener> collectKeyListeners(int eventKey) {
		if (keyListenersHandler.containsKey(eventKey)) {
			return new ArrayList<GameListener>(
					keyListenersHandler.get(eventKey));
		}
		return null;
	}

	private synchronized void castEvents() {
		while (!isStopInvoked()) {
			try {
				gameEventsQueue.take().perform();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setFinished(true);
	}

	/**
	 * Stops the event caster
	 */
	public static synchronized void discharge() {
		long initTime = System.currentTimeMillis();
		eventCollector.setStopInvoked(true);
		try {
			while (!(eventCollector.isFinished() && eventCaster.isFinished())) {
				sleep(10l);
				if (eventCollector.isFinished() && gameEventsQueue.isEmpty()) {
					eventCaster.setStopInvoked(true);
					sleep(10l);
					gameEventsQueue.add(new GameEvent() {
						@Override
						public void perform() {
							System.out.println("Last event");
						}
					});
				}
				long timeDelay = System.currentTimeMillis() - initTime;
				if (!eventCaster.isFinished() && timeDelay > 2 * 1000) {
					eventCaster.setStopInvoked(true);
				}
				if (timeDelay > 4 * 1000) {
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		eventCollector = null;
		eventCaster = null;
		setInitialised(false);
	}

	public static void addKeyListener(int key, GameKeyListener keyListener) {
		if (!keyListenersHandler.containsKey(key)) {
			keyListenersHandler.put(key, new ConcurrentLinkedQueue<>());
		}
		ConcurrentLinkedQueue<GameKeyListener> list = keyListenersHandler
				.get(key);
		if (!list.contains(keyListener))
			list.add(keyListener);
		keyListenersHandler.put(key, list);
	}

	public static boolean containsListener(int key) {
		return LWJGLEventMulticaster.keyListenersHandler.containsKey(key);
	}

	public static void removeListeners(int key) {
		LWJGLEventMulticaster.keyListenersHandler.remove(key);
	}

	private enum MCType {
		COLLECTOR, CASTER;
	}

	public static boolean isInitialised() {
		return initialised;
	}

	private static void setInitialised(boolean initialised) {
		LWJGLEventMulticaster.initialised = initialised;
	}

	public boolean isStopInvoked() {
		return stopInvoked;
	}

	public void setStopInvoked(boolean stop) {
		this.stopInvoked = stop;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
