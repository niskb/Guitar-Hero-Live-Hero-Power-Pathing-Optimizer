package test;

import java.util.Scanner;

import model.Chart;
import utils.ChartUtils;

public class Test {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter .chart file location (INCLUDING FILE NAME): ");
		String fileLocation = scanner.nextLine();
		Chart chart = new Chart(fileLocation);
		System.out.println("Create path for GHL or GHTV?");
		String gameMode = scanner.nextLine();
		scanner.close();
		System.out.println(chart);
		ChartUtils.retrieveChartData(chart, fileLocation, gameMode);
	}

}
