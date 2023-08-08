package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import model.BPM;
import model.BPMBag;
import model.Note;

public class BPMUtils {

	private static final String REGEX = "[\t ]";

	public static BPMBag getBPMData(String fileLocation, Note[] notes) {
		return scanSyncTrack(fileLocation);
	}

	private static BPMBag scanSyncTrack(String fileLocation) {
		long numberOfLines = readChart(fileLocation);
		long startLine = findSyncTrack(fileLocation, numberOfLines);
		long endLine = findEndLine(fileLocation, startLine);
		int numberOfBPMChanges = calculateNumberOfBPMChanges(fileLocation, startLine, endLine);
		return storeBPMs(fileLocation, numberOfBPMChanges, startLine, endLine);
	}

	private static long readChart(String fileLocation) {
		try {
			FileReader fr = new FileReader(fileLocation);
			BufferedReader br = new BufferedReader(fr);
			long lineNumber = 1;
			for (String x = br.readLine(); x != null; x = br.readLine()) {
				lineNumber++;
			}
			br.close();
			return lineNumber;
		} catch (FileNotFoundException e) {
			System.out.println("Failed to load file.");
		} catch (IOException e) {
			System.out.println("Failed to read line.");
		}
		return -1;
	}

	private static long findSyncTrack(String fileLocation, long numberOfLines) {
		if (numberOfLines != -1) {
			try {
				FileReader fr = new FileReader(fileLocation);
				BufferedReader br = new BufferedReader(fr);
				long lineNumber = 1;
				for (String x = br.readLine(); lineNumber < numberOfLines; x = br.readLine()) {
					if (x.contains("[SyncTrack]")) {
						break;
					}
					lineNumber++;
				}
				br.close();
				return lineNumber;
			} catch (FileNotFoundException e) {
				System.out.println("Failed to load file.");
			} catch (IOException e) {
				System.out.println("Failed to read line.");
			}
		}
		return -1;
	}

	private static long findEndLine(String fileLocation, long startLine) {
		if (startLine != -1) {
			try {
				FileReader fr = new FileReader(fileLocation);
				BufferedReader br = new BufferedReader(fr);
				long lineNumber = 1;
				for (@SuppressWarnings("unused")
				String x = br.readLine(); lineNumber < startLine; x = br.readLine()) {
					lineNumber++;
				}
				for (String x = br.readLine(); !x.contains("}"); x = br.readLine()) {
					lineNumber++;
				}
				lineNumber++;
				br.close();
				return lineNumber;
			} catch (FileNotFoundException e) {
				System.out.println("Failed to load file.");
			} catch (IOException e) {
				System.out.println("Failed to read line.");
			}
		}
		return -1;
	}

	private static int calculateNumberOfBPMChanges(String fileLocation, long startLine, long endLine) {
		int numberOfBPMChanges = 0;
		if (endLine != -1) {
			try {
				FileReader fr = new FileReader(fileLocation);
				BufferedReader br = new BufferedReader(fr);
				long lineNumber = 1;
				String previousLine = "";
				for (String x = br.readLine(); lineNumber < startLine; x = br.readLine()) {
					previousLine = x;
					lineNumber++;
				}
				for (String x = br.readLine(); lineNumber < endLine; x = br.readLine()) {
					if (x.contains("{") || previousLine.contains("{")) {
						previousLine = x;
						lineNumber++;
						continue;
					}
					try {
						String[] tokens = x.split(REGEX);
						if (tokens[4].equals("B")) {
							if (!tokens[5].equals("B") && (isSorted(Integer.valueOf(previousLine.split(REGEX)[2]),
									Integer.valueOf(tokens[2])))) {
								numberOfBPMChanges++;
							}
						}
					} catch (Exception e) {
						continue;
					}
					lineNumber++;
				}
				br.close();
			} catch (FileNotFoundException e) {
				System.out.println("Failed to load file.");
			} catch (IOException e) {
				System.out.println("Failed to read line.");
			}
			return numberOfBPMChanges;
		}
		if (numberOfBPMChanges == 0) {
			numberOfBPMChanges = -1;
		}
		return numberOfBPMChanges;
	}

	private static BPMBag storeBPMs(String fileLocation, int numberOfBPMChanges, long startLine, long endLine) {
		BPMBag bpmsBag = new BPMBag(numberOfBPMChanges);
		try {
			FileReader fr = new FileReader(fileLocation);
			BufferedReader br = new BufferedReader(fr);
			long lineNumber = 1;
			String previousLine = "";
			for (String x = br.readLine(); lineNumber < startLine; x = br.readLine()) {
				previousLine = x;
				lineNumber++;
			}
			for (String x = br.readLine(); lineNumber < endLine; x = br.readLine()) {
				if (x.contains("{") || previousLine.contains("{")) {
					previousLine = x;
					lineNumber++;
					continue;
				}
				try {
					String[] tokens = x.split(REGEX);
					if (tokens[4].equals("B")) {
						bpmsBag.insert(new BPM(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[5])));

					}
				} catch (Exception e) {
					continue;
				}
				lineNumber++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to load file.");
		} catch (IOException e) {
			System.out.println("Failed to read line.");
		}
		return bpmsBag;
	}

	private static boolean isSorted(long previousTimeToken, long timeToken) {
		if (previousTimeToken <= timeToken) {
			return true;
		} else {
			return false;
		}
	}

	public static long calculateHeroPowerEndTime(Note[] notes, int index, BPMBag bpmsBag, String gameMode) {
		BigDecimal endTime = BigDecimal.valueOf(notes[index].getTimeSignature());
		int numberOfBeats = 32;
		if (gameMode.equals("GHTV")) {
			numberOfBeats = 64;
		}
		int currentBeatNumber = 0;
		long currentBPMValue = getBPMFromNoteTime(notes[index], bpmsBag);
		for (int i = index; i < notes.length; i++) {
			if (currentBeatNumber < numberOfBeats) {
				if (currentBeatNumber > 0) {
					if (currentBPMValue != getBPMFromNoteTime(notes[i - 1], bpmsBag)) {
						currentBPMValue = getBPMFromNoteTime(notes[i], bpmsBag);
					}
				}
				BigDecimal beatsPerSecond = BigDecimal.valueOf(currentBPMValue).divide(BigDecimal.valueOf(6000),
						MathContext.DECIMAL128);
				BigDecimal halfBeatsPerSecond = BigDecimal.valueOf(currentBPMValue)
						.divide(BigDecimal.valueOf(12000), MathContext.DECIMAL128)
						.add(BigDecimal.valueOf(Math.pow(1.0, -34))); // squeezing (need to refer to engine hit window)
				endTime = endTime.add(beatsPerSecond).add(halfBeatsPerSecond);
				currentBeatNumber++;
			}
		}
		return (endTime.longValue() + 1);
	}

	private static long getBPMFromNoteTime(Note note, BPMBag bpmsBag) {
		long bpm = 0;
		for (int i = 0; i < bpmsBag.obtainBPMInformation().length; i++) {
			if (bpmsBag.obtainBPMInformation()[i].getTimeSignature() <= note.getTimeSignature()) {
				bpm = bpmsBag.obtainBPMInformation()[i].getBpmValue();
			}
		}
		return bpm;
	}

}
