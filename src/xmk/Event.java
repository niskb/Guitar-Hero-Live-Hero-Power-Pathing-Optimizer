package xmk;

public class Event {

	private long groupIndex;
	private long chord;
	private long event;
	private long note;
	private float startTime;
	private float endTime;
	private long unknown;
	private long stringOffset;
	
	public Event(long groupIndex, long chord, long event, long note, float startTime, float endTime, long unknown,
			long stringOffset) {
		this.groupIndex = groupIndex;
		this.chord = chord;
		this.event = event;
		this.note = note;
		this.startTime = startTime;
		this.endTime = endTime;
		this.unknown = unknown;
		this.stringOffset = stringOffset;
	}

	public long getGroupIndex() {
		return groupIndex;
	}

	public long getChord() {
		return chord;
	}

	public long getEvent() {
		return event;
	}

	public long getNote() {
		return note;
	}

	public float getStartTime() {
		return startTime;
	}

	public float getEndTime() {
		return endTime;
	}

	public long getUnknown() {
		return unknown;
	}

	public long getStringOffset() {
		return stringOffset;
	}

	@Override
	public String toString() {
		return "Event [Group Index: " + groupIndex + ", Chord: " + chord + ", Event: " + event + ", Note: " + note
				+ ", Start Time: " + startTime + ", End Time: " + endTime + ", Unknown: " + unknown + ", String Offset: "
				+ stringOffset + "]";
	}
	
}
