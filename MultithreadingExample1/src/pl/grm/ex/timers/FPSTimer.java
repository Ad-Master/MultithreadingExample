package pl.grm.ex.timers;

public class FPSTimer {
	/** time at last frame */
	private long	lastFrame;
	/** current frames per second ration (not in full second!) */
	private int		fpsT;
	/** last fps time */
	private long	lastFPSTime;
	/** last fps ration */
	private long	lastFPS;
	
	public void initTime() {
		getDelta(); // call once before loop to initialise lastFrame
		lastFPSTime = getTime(); // call before loop to initialise fps timer
	}
	
	/**
	 * Calculate the FPS and set it
	 */
	public void updateFPS() {
		if (getTime() - lastFPSTime > 1000) {
			lastFPS = fpsT;
			fpsT = 0;
			lastFPSTime += 1000;
		}
		fpsT++;
	}
	
	/**
	 * Calculate how many milliseconds have passed since last frame.
	 *
	 * @return milliseconds passed since last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	/**
	 * Get the accurate system time
	 *
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	/**
	 * Get the current fps count (fake)
	 * 
	 * @return fps Counter
	 */
	public int getFPS() {
		return fpsT;
	}
	
	/**
	 * Gets the real FPS of display renderer
	 * 
	 * @return frames per second
	 */
	public long getLastFps() {
		return lastFPS;
	}
}