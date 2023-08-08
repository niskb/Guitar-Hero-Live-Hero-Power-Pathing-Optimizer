package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Chart {

	private long numberOfLines;
	private long startLine;
	private long endLine;

	public Chart(String fileLocation) {
		numberOfLines = readChart(fileLocation);
		startLine = findGHLExpert(fileLocation, numberOfLines);
		endLine = findEndLine(fileLocation, startLine);
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

	private static long findGHLExpert(String fileLocation, long numberOfLines) {
		if (numberOfLines != -1) {
			try {
				FileReader fr = new FileReader(fileLocation);
				BufferedReader br = new BufferedReader(fr);
				long lineNumber = 1;
				for (String x = br.readLine(); lineNumber < numberOfLines; x = br.readLine()) {
					if (x.contains("[ExpertGHLGuitar]")) {
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

	public long getNumberOfLines() {
		return numberOfLines;
	}

	public long getStartLine() {
		return startLine;
	}

	public long getEndLine() {
		return endLine;
	}

	@Override
	public String toString() {
		return "Chart [numberOfLines=" + numberOfLines + ", GHL Expert Start Line=" + startLine
				+ ", GHL Expert End Line=" + endLine + "]";
	}

}
