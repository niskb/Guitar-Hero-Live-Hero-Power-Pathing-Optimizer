package model;

public class BPMBag {

	private BPM[] bpms;
	private int nElems;

	public BPMBag(int maxSize) {
		bpms = new BPM[maxSize];
		nElems = 0;
	}

	public void insert(BPM bpm) {
		try {
			bpms[nElems++] = bpm;
		} catch (Exception e) {
			System.out.println("The array is full.");
		}
	}

	public BPM[] obtainBPMInformation() {
		return bpms;
	}

	public void displayAll() {
		for (int i = 0; i < nElems; i++) {
			System.out.println(
					"BPM Change at " + bpms[i].getTimeSignature() + ", New value is " + bpms[i].getBpmValue() + ".");
		}
	}

}
