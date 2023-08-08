package utils;

import model.BPMBag;
import model.Note;
import model.NoteBank;
import model.Type;

public class GHLPathBuilder {

	public static void optimizeForGHLMode(NoteBank[] theNoteBank, String fileLocation, String gameMode) {
		Note[] notes = theNoteBank[0].obtainNoteInformation();
		Note[] heroPowerPositions = theNoteBank[1].obtainNoteInformation();
		BPMBag bpmsBag = BPMUtils.getBPMData(fileLocation, notes);
		System.out.println("Creating path for GHL mode (Base game).\n");
		Note[] remainingNotes = deepCopyNotes(notes);
		int[] startTimeIndexes = getStartTimeIndexes(remainingNotes, getHeroPowerPhraseEndTimes(heroPowerPositions));
		int[] bestIndexes = new int[startTimeIndexes.length];
		for (int i = 0; i < startTimeIndexes.length; i++) {
			long bestNoteCount = 0;
			long bestTickCount = 0;
			int bestIndex = 0;
			for (int j = startTimeIndexes[i]; j < remainingNotes.length; j++) {
				long heroPowerEndTime = BPMUtils.calculateHeroPowerEndTime(notes, j, bpmsBag, gameMode);
				long indexFromHeroPowerEndTime = getIndexFromHeroPowerEndTime(remainingNotes, heroPowerEndTime);
				long noteCount = indexFromHeroPowerEndTime - j
						- getNumberOfForcedNotes(remainingNotes, j, indexFromHeroPowerEndTime);
				long tickCount = calculateTicks(remainingNotes, startTimeIndexes[i], indexFromHeroPowerEndTime,
						heroPowerEndTime);
				if (noteCount > bestNoteCount) {
					bestNoteCount = noteCount;
					bestTickCount = tickCount;
					bestIndex = j;
				}
				if (noteCount == bestNoteCount) {
					if (tickCount > bestTickCount) {
						bestTickCount = tickCount;
						bestIndex = j;
					}
				}
			}
			bestIndexes[i] = bestIndex;
			remainingNotes = rebuildNotes(remainingNotes, bestIndex);
		}
		displayPath(notes, bestIndexes);
	}

	private static long[] getHeroPowerPhraseEndTimes(Note[] heroPowerPositions) {
		long[] heroPowerPhraseEndTimes = new long[heroPowerPositions.length];
		int i = 0;
		for (int j = heroPowerPositions.length - 1; j >= 0; j--) {
			heroPowerPhraseEndTimes[i] = heroPowerPositions[j].getTimeSignature() + heroPowerPositions[j].getTicks();
			i++;
		}
		return heroPowerPhraseEndTimes;
	}

	private static int getNextNoteAfterHeroPowerPhrase(Note[] notes, long heroPowerPhraseEndTime) {
		for (int i = 0; i < notes.length; i++) {
			if (notes[i].getTimeSignature() > heroPowerPhraseEndTime) {
				return i;
			}
		}
		return -1;
	}

	private static int[] getStartTimeIndexes(Note[] notes, long[] heroPowerPhraseEndTimes) {
		int[] startTimeIndexes = new int[heroPowerPhraseEndTimes.length];
		for (int i = 0; i < startTimeIndexes.length; i++) {
			startTimeIndexes[i] = getNextNoteAfterHeroPowerPhrase(notes, heroPowerPhraseEndTimes[i]);
		}
		return startTimeIndexes;
	}

	private static long getIndexFromHeroPowerEndTime(Note[] notes, long heroPowerEndTime) {
		for (int i = 0; i < notes.length; i++) {
			if (notes[i].getTimeSignature() > heroPowerEndTime) {
				return i - 1;
			}
		}
		return notes.length - 1;
	}

	private static long calculateTicks(Note[] notes, int startTimeIndex, long endTimeIndex, long heroPowerEndTime) {
		long tickCount = 0;
		int i = startTimeIndex;
		for (; i <= endTimeIndex; i++) {
			tickCount = tickCount + notes[i].getTicks();
		}
		int lastIndex = i - 1;
		if ((notes[lastIndex].getTicks() > 0)) {
			tickCount = subtractTickTimeIfIsChord(tickCount, heroPowerEndTime, notes, lastIndex);
		}
		return tickCount;
	}

	private static long subtractTickTimeIfIsChord(long tickCount, long heroPowerEndTime, Note[] notes, int lastIndex) {
		tickCount = heroPowerEndTime - notes[lastIndex].getTimeSignature();
		for (int i = 0; i < Type.values().length; i++) {
			try {
				if ((notes[lastIndex - i].getTimeSignature() + notes[lastIndex - i].getTicks()) > heroPowerEndTime) {
					tickCount = tickCount - (heroPowerEndTime - notes[lastIndex - i].getTimeSignature());
				} else {
					break;
				}
			} catch (Exception e) {
				break;
			}
		}
		return tickCount;
	}

	private static Note[] rebuildNotes(Note[] notes, int bestIndex) {
		Note[] newNotes = new Note[bestIndex - 1];
		for (int i = 0; i < newNotes.length; i++) {
			newNotes[i] = new Note(notes[i]);
		}
		return newNotes;
	}

	private static long getNumberOfForcedNotes(Note[] notes, int currentIndex, long indexFromHeroPowerEndTime) {
		long numberOfForcedNotes = 0;
		for (; currentIndex < indexFromHeroPowerEndTime; currentIndex++) {
			if (notes[currentIndex].getType() == Type.FORCED) {
				numberOfForcedNotes++;
			}
		}
		return numberOfForcedNotes;
	}

	private static void displayPath(Note[] notes, int[] bestIndexes) {
		for (int i = bestIndexes.length - 1; i >= 0; i--) {
			System.out.println(
					"Activation Time " + (bestIndexes.length - i) + ": " + notes[bestIndexes[i]].getTimeSignature());
			printNoteTypeAtTime(notes, notes[bestIndexes[i]].getTimeSignature());
			printNoteNumberFromTime(notes, notes[bestIndexes[i]].getTimeSignature());
		}
	}

	private static void printNoteTypeAtTime(Note[] notes, long time) {
		System.out.print("Type of note: ");
		for (int i = 0; i < notes.length; i++) {
			if (notes[i].getTimeSignature() == time) {
				if ((notes[i].getType() != Type.FORCED) && (notes[i].getType() != Type.NULL)) {
					System.out.print(notes[i].getType() + " ");
				}
			} else if (notes[i].getTimeSignature() > time) {
				break;
			}
		}
		System.out.println();
	}

	private static void printNoteNumberFromTime(Note[] notes, long time) {
		System.out.print("Calculated note number to activate on: ");
		int countOfNotesNotSameTime = 0;
		for (int i = 1; i < notes.length; i++) {
			if (notes[i].getTimeSignature() != notes[i - 1].getTimeSignature()) {
				countOfNotesNotSameTime++;
			} else {
				continue;
			}
		}
		Note[] singleNotesOnly = new Note[countOfNotesNotSameTime];
		int indexOfSingleNotesOnly = 0;
		for (int j = 1; j < notes.length; j++) {
			if (notes[j].getTimeSignature() > notes[j - 1].getTimeSignature()) {
				singleNotesOnly[indexOfSingleNotesOnly] = new Note(notes[j - 1]);
				indexOfSingleNotesOnly++;
			} else {
				continue;
			}
		}
		int indexOfNoteThatMatchesTime = 0;
		for (int k = 0; k < singleNotesOnly.length; k++) {
			if (singleNotesOnly[k].getTimeSignature() < time) {
				indexOfNoteThatMatchesTime = k;
			} else {
				if (singleNotesOnly[k].getTimeSignature() >= time) {
					break;
				}
			}
		}
		indexOfNoteThatMatchesTime++;
		System.out.println(indexOfNoteThatMatchesTime);
		System.out.println();
	}

	private static Note[] deepCopyNotes(Note[] notes) {
		Note[] copyOfNotes = new Note[notes.length];
		for (int i = 0; i < notes.length; i++) {
			copyOfNotes[i] = new Note(notes[i]);
		}
		return copyOfNotes;
	}

}
