package xmk;

import java.util.List;

public class XMKHeader {

	private long version;
	private long hash;
	private long eventCount;
	private long blobLength;
	private long unknown;
	private List<Tempo> tempoChunks;
	private List<TimeSig> timeSigs;

	public XMKHeader(long version, long hash, long eventCount, long blobLength, long unknown, List<Tempo> tempoChunks,
			List<TimeSig> timeSigs) {
		this.version = version;
		this.hash = hash;
		this.eventCount = eventCount;
		this.blobLength = blobLength;
		this.unknown = unknown;
		this.tempoChunks = tempoChunks;
		this.timeSigs = timeSigs;
	}

	public long getVersion() {
		return version;
	}

	public long getHash() {
		return hash;
	}

	public long getEventCount() {
		return eventCount;
	}

	public long getBlobLength() {
		return blobLength;
	}

	public long getUnknown() {
		return unknown;
	}

	public List<Tempo> getTempoChunks() {
		return tempoChunks;
	}

	public List<TimeSig> getTimeSigs() {
		return timeSigs;
	}
	
	@Override
	public String toString() {
		return "XMKHeader [Version: " + version + ", Hash: " + hash + ", Event Count: " + eventCount + ", Blob Length: "
				+ blobLength + ", Unknown: " + unknown + ", Tempo Chunks: " + tempoChunks + ", Time Sigs: " + timeSigs + "]";
	}

}
