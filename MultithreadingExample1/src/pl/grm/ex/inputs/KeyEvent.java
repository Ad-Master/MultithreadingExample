package pl.grm.ex.inputs;

import java.util.List;

import pl.grm.ex.GameEvent;

public class KeyEvent implements GameEvent {
	private int eventKey;
	private boolean eventKeyState;
	private long eventTime;
	private boolean repeatEvent;
	private List<GameListener> listeners;

	public KeyEvent(int eventKey, boolean eventKeyState, long eventTime,
			boolean repeatEvent, List<GameListener> list) {
		this.setEventKey(eventKey);
		this.setEventKeyState(eventKeyState);
		this.setEventTime(eventTime);
		this.setRepeatEvent(repeatEvent);
		this.listeners = list;
	}

	@Override
	public void perform() {
		for (GameListener gameKeyListener : listeners) {
			if (isEventKeyState()) {
				if (isRepeatEvent()) {
					((GameKeyListener) gameKeyListener).keyTyped(this);
				} else {
					((GameKeyListener) gameKeyListener).keyPressed(this);
				}
			} else {
				((GameKeyListener) gameKeyListener).keyReleased(this);
			}
		}
	}

	public void addListener(GameKeyListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public int getEventKey() {
		return eventKey;
	}

	public void setEventKey(int eventKey) {
		this.eventKey = eventKey;
	}

	public boolean isEventKeyState() {
		return eventKeyState;
	}

	public void setEventKeyState(boolean eventKeyState) {
		this.eventKeyState = eventKeyState;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public boolean isRepeatEvent() {
		return repeatEvent;
	}

	public void setRepeatEvent(boolean repeatEvent) {
		this.repeatEvent = repeatEvent;
	}

	public List<GameListener> getListeners() {
		return listeners;
	}
}
