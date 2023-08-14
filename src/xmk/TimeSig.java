package xmk;

public class TimeSig {

	private long ticks;
	private long measure;
	private long numerator;
	private long denominator;

	public TimeSig(long ticks, long measure, long numerator, long denominator) {
		this.ticks = ticks;
		this.measure = measure;
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public long getTicks() {
		return ticks;
	}

	public long getMeasure() {
		return measure;
	}

	public long getNumerator() {
		return numerator;
	}

	public long getDenominator() {
		return denominator;
	}

	@Override
	public String toString() {
		return "TimeSig [Ticks: " + ticks + ", Measure: " + measure + ", Numerator: " + numerator + ", Denominator: "
				+ denominator + "]";
	}

}
