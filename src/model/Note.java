package model;

public class Note {

	private long timeSignature;
	private Type type;
	private long ticks;

	public Note(long timeSignature, Type type, long ticks) {
		this.timeSignature = timeSignature;
		this.type = type;
		this.ticks = ticks;
	}

	public Note(Note note) {
		this.timeSignature = note.getTimeSignature();
		this.type = note.getType();
		this.ticks = note.getTicks();
	}

	public long getTimeSignature() {
		return timeSignature;
	}

	public Type getType() {
		return type;
	}

	public long getTicks() {
		return ticks;
	}

	@Override
	public String toString() {
		return "Note [timeSignature=" + timeSignature + ", type=" + type + ", ticks=" + ticks + "]";
	}

}
