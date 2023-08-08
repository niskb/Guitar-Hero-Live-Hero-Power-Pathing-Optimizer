package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.Chart;
import model.Note;
import model.NoteBank;
import model.Type;

public class ChartUtils {

	private static final String REGEX = "[\t ]";

	public static void retrieveChartData(Chart chart, String fileLocation, String gameMode) {
		int numberOfHeroPowerPhrases = calculateNumberOfHeroPowerPhrases(chart, fileLocation);
		findOptimalPathing(chart, fileLocation, numberOfHeroPowerPhrases, gameMode);
	}

	private static int calculateNumberOfHeroPowerPhrases(Chart chart, String fileLocation) {
		int numberOfHeroPowerPhrases = 0;
		if (chart.getEndLine() != -1) {
			long startLine = chart.getStartLine();
			long endLine = chart.getEndLine();
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
						if (tokens[4].equals("S")) {
							if (!tokens[5].equals("5") && (isSorted(Integer.valueOf(previousLine.split(REGEX)[2]),
									Integer.valueOf(tokens[2])))) {
								numberOfHeroPowerPhrases++;
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
			return numberOfHeroPowerPhrases;
		}
		if (numberOfHeroPowerPhrases == 0) {
			numberOfHeroPowerPhrases = -1;
		}
		return numberOfHeroPowerPhrases;
	}

	private static int calculateTotalNumberOfNotes(Chart chart, String fileLocation) {
		int numberOfTotalNotes = 0;
		long startLine = chart.getStartLine();
		long endLine = chart.getEndLine();
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
					if (tokens[4].equals("N")) {
						if (!tokens[5].equals("5") && (isSorted(Integer.valueOf(previousLine.split(REGEX)[2]),
								Integer.valueOf(tokens[2])))) {
							numberOfTotalNotes++;
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
		return numberOfTotalNotes;
	}

	private static NoteBank storeTotalNumberOfNotes(Chart chart, String fileLocation, int totalNumberOfNotes) {
		NoteBank notesBank = new NoteBank(totalNumberOfNotes);
		long startLine = chart.getStartLine();
		long endLine = chart.getEndLine();
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
					if (tokens[4].equals("N")) {
						if (!tokens[5].equals("5") && (isSorted(Integer.valueOf(previousLine.split(REGEX)[2]),
								Integer.valueOf(tokens[2])))) {
							notesBank.deposit(new Note(Integer.parseInt(tokens[2]), parseNoteType(tokens[5]),
									Integer.parseInt(tokens[6])));
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
		return notesBank;
	}

	private static NoteBank storeNumberOfHeroPowerPhrases(Chart chart, String fileLocation,
			int numberOfHeroPowerPhrases) {
		NoteBank heroPowerBank = new NoteBank(numberOfHeroPowerPhrases);
		long startLine = chart.getStartLine();
		long endLine = chart.getEndLine();
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
					if (tokens[4].equals("S")) {
						if (!tokens[5].equals("5") && (isSorted(Integer.valueOf(previousLine.split(REGEX)[2]),
								Integer.valueOf(tokens[2])))) {
							heroPowerBank.deposit(new Note(Integer.parseInt(tokens[2]), Type.HERO_POWER_PHRASE_START,
									Integer.parseInt(tokens[6])));
						}
						numberOfHeroPowerPhrases++;
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
		return heroPowerBank;
	}

	private static Type parseNoteType(String token) {
		switch (token) {
		case "3":
			return Type.BLACK_1;
		case "4":
			return Type.BLACK_2;
		case "8":
			return Type.BLACK_3;
		case "0":
			return Type.WHITE_1;
		case "1":
			return Type.WHITE_2;
		case "2":
			return Type.WHITE_3;
		case "7":
			return Type.OPEN_NOTE;
		case "5":
			return Type.FORCED;
		default:
			break;
		}
		return Type.NULL;
	}

	private static boolean isSorted(long previousTimeToken, long timeToken) {
		if (previousTimeToken <= timeToken) {
			return true;
		} else {
			return false;
		}
	}

	private static void findOptimalPathing(Chart chart, String fileLocation, int numberOfHeroPowerPhrases,
			String gameMode) {
		NoteBank[] theNoteBank = fillNotes(chart, fileLocation, numberOfHeroPowerPhrases);
		chooseGameMode(theNoteBank, gameMode, fileLocation);
	}

	private static NoteBank[] fillNotes(Chart chart, String fileLocation, int numberOfHeroPowerPhrases) {
		NoteBank[] theNoteBank = new NoteBank[2];
		theNoteBank[0] = storeTotalNumberOfNotes(chart, fileLocation, calculateTotalNumberOfNotes(chart, fileLocation));
		theNoteBank[1] = storeNumberOfHeroPowerPhrases(chart, fileLocation, numberOfHeroPowerPhrases);
		return theNoteBank;
	}

	private static void chooseGameMode(NoteBank[] theNoteBank, String gameMode, String fileLocation) {
		if (gameMode.equals("GHL")) {
			GHLPathBuilder.optimizeForGHLMode(theNoteBank, fileLocation, gameMode);
		} /*
			 * else if (gameMode.equals("GHTV")) {
			 * GHTVPathBuilder.optimizeForGHTVMode(theNoteBank); }
			 */
	}

}
