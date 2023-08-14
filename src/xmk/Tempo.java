package xmk;

public class Tempo {

	private long ticks;
	private float startTime;
	private long microPerQuarter;

	public Tempo(long ticks, float startTime, long microPerQuarter) {
		this.ticks = ticks;
		this.startTime = startTime;
		this.microPerQuarter = microPerQuarter;
	}

	public long getTicks() {
		return ticks;
	}

	public float getStartTime() {
		return startTime;
	}

	public long getMicroPerQuarter() {
		return microPerQuarter;
	}

	@Override
	public String toString() {
		return "Tempo [Ticks: " + ticks + ", Start Time: " + startTime + ", Micro Per Quarter: " + microPerQuarter + "]";
	}

}
