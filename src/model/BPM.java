package model;

public class BPM {

	private long timeSignature;
	private long bpmValue;

	public BPM(long timeSignature, long bpmValue) {
		this.timeSignature = timeSignature;
		this.bpmValue = bpmValue;
	}

	public long getTimeSignature() {
		return timeSignature;
	}

	public long getBpmValue() {
		return bpmValue;
	}

	@Override
	public String toString() {
		return "BPM [timeSignature=" + timeSignature + ", bpmValue=" + bpmValue + "]";
	}

}
