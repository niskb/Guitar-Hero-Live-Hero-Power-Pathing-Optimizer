package xmk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

public class Demo {

	public static void main(String[] args) {
		try (RandomAccessFile raf = new RandomAccessFile("guitar_3x2.xmk", "r")) {
			// XMK Header
			long xmkhVersion = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			long xmkhHash = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			long xmkhEventCount = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			long xmkhBlobLength = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			long xmkhUnknown = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			long xmkhTempoCount = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			List<Tempo> tempoChunks = new LinkedList<>();
			long xmkhTSCount = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
			List<TimeSig> timeSigs = new LinkedList<>();
			
			// Fill Tempo Map
			for (long i = 0; i < xmkhTempoCount; i++) {
				long tcTicks = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				float tcStartTime = advanceInFloat(raf, 4);
				long tcMicroPerQuarter = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				Tempo tempo = new Tempo(tcTicks, tcStartTime, tcMicroPerQuarter);
				tempoChunks.add(tempo);
			}

			// Fill Time Signatures
			for (long i = 0; i < xmkhTSCount; i++) {
				long tsTicks = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				long tsMeasure = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				long tsNumerator = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				long tsDenominator = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				TimeSig timeSig = new TimeSig(tsTicks, tsMeasure, tsNumerator, tsDenominator);
				timeSigs.add(timeSig);
			}

			Event[] events = new Event[(int) xmkhEventCount];
			// Fill Events
			for (int i = 0; i < events.length; i++) {
				long eGroupIndex = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				long eChord = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 2));
				long eEvent = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 1));
				long eNote = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 1));
				float eStartTime = advanceInFloat(raf, 4);
				float eEndTime = advanceInFloat(raf, 4);
				long eUnknown = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				long eStringOffset = ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 4));
				events[i] = new Event(eGroupIndex, eChord, eEvent, eNote, eStartTime, eEndTime, eUnknown,
						eStringOffset);
			}

			char[] charTable = new char[(int) xmkhBlobLength];
			// Fill Char Table
			for (int i = 0; i < charTable.length; i++) {
				char blobString = (char) ByteFactory.parseBytesToDecimal(advanceInBytes(raf, 1));
				charTable[i] = blobString;
			}
			XMKHeader header = new XMKHeader(xmkhVersion, xmkhHash, xmkhEventCount, xmkhBlobLength, xmkhUnknown,
					tempoChunks, timeSigs);
			System.out.println(header);
			System.out.println("Number of Tempos: " + xmkhTempoCount);
			System.out.println("Number of Time Sigs: " + xmkhTSCount);
			System.out.println("Events:");
			for (int i = 0; i < events.length; i++) {
				System.out.println("Event Number: " + (i + 1) + ": " + events[i]);
			}
			System.out.println("Section Names: ");
			for (int i = 0; i < charTable.length; i++) {
				System.out.print(charTable[i]);
			}

			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static String advanceInBytes(RandomAccessFile raf, int numberOfBytes) {
		String text = "";
		for (int i = 0; i < numberOfBytes; i++) {
			try {
				text = text + raf.readByte() + " ";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	@SuppressWarnings("null")
	private static float advanceInFloat(RandomAccessFile raf, int numberOfBytes) {
		byte[] bytes = new byte[numberOfBytes];
		try {
			raf.readFully(bytes);
			return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getFloat();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (Float) null;
	}

}
