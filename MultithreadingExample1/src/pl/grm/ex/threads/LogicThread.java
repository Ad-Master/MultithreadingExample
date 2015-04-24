package pl.grm.ex.threads;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import pl.grm.ex.Example1;
import pl.grm.ex.GameLoadStage;
import pl.grm.ex.entities.Entity;
import pl.grm.ex.timers.TPSTimer;

public class LogicThread extends Thread {
	private TPSTimer timer;

	public LogicThread() {
		timer = Example1.instance.getTPSTimer();
	}

	@Override
	public void run() {
		timer.initTime(Example1.TPS);
		boolean isClosing = false;
		while (Example1.instance.isRunning()) {
			GameLoadStage loadState = Example1.instance.getGameStage();
			if (loadState != GameLoadStage.CLOSING) {
				System.out.println(loadState.toString());
			}
			switch (Example1.instance.getGameStage()) {
			case LOADING:
				introIterate();
				break;
			case MAIN:
				mainIterator();
				break;
			case CLOSING:
				if (!isClosing) {
					isClosing = true;
					System.out.println("Closing ...");
				}
				break;
			default:
				break;
			}
		}
	}

	private void introIterate() {
		Thread.currentThread().setName("Loading Logic Thread");
		while (Example1.instance.getGameStage() == GameLoadStage.LOADING) {
			baseLoop();
		}
	}

	/**
	 * Iterates over every event
	 */
	private void mainIterator() {
		Thread.currentThread().setName("Game Logic Thread");
		while (Example1.instance.getGameStage() == GameLoadStage.MAIN) {
			updateEntities();
			if (timer.isFullCycle())
				System.out.println("Entities count: "
						+ Example1.getEntities().size());
			baseLoop();
		}
	}

	private void baseLoop() {
		if (timer.isFullCycle()) {
			long lastFps = Example1.instance.getFPSTimer().getLastFps();
			long lastTps = Example1.instance.getTPSTimer().getLastTps();
			if (lastFps != 0 && lastTps != 0) {
				System.out.println("FPS: " + lastFps + " | TPS: " + lastTps);
			} else {
				System.out.println("Please wait. Program is loading ... ");
			}
		}
		timer.sync();
	}

	private void updateEntities() {
		Set<Integer> keySet = Example1.getEntities().keySet();
		Iterator<Integer> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			Collection<Entity> entityCollection = Example1.getEntities().get(
					key);
			for (Entity entity : entityCollection) {
				entity.update();
			}
		}
	}
}
