package model;

public class NoteBank {

	private Note[] noteBank;
	private int nElems;

	public NoteBank(int maxSize) {
		noteBank = new Note[maxSize];
		nElems = 0;
	}

	public void deposit(Note note) {
		try {
			noteBank[nElems++] = note;
		} catch (Exception e) {
			System.out.println("The array is full.");
		}
	}

	public Note withdrawByElement(int i) {
		Note note = noteBank[i];
		noteBank[i] = new Note(0, Type.NULL, 0);
		return note;
	}
	
	public Note[] obtainNoteInformation() {
		return noteBank;
	}
	
	public void displayAll() {
		for (int i = 0; i < nElems; i++) {
			if (noteBank[i].getType() != Type.HERO_POWER_PHRASE_START) {
				System.out.println("Note " + (i + 1) + ": Time: " + noteBank[i].getTimeSignature() + "\tType: "
						+ noteBank[i].getType() + "\tTicks: " + noteBank[i].getTicks());
			} else {
				System.out.println("Note " + (i + 1) + ": Time: " + noteBank[i].getTimeSignature() + "\tLength: "
						+ noteBank[i].getTicks());
			}
		}
	}

}
