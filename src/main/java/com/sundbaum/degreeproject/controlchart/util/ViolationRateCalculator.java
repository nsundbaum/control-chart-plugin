package com.sundbaum.degreeproject.controlchart.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundbaum.degreeproject.controlchart.TestCaseData;
import com.sundbaum.degreeproject.controlchart.TestCaseData.DataPoint;

public class ViolationRateCalculator {
	public static void main(String[] args) {
		String fileName = "/Users/nsundbaum/Documents/kth/exjobb/load-test-reports/baseline" + 
				"/baseline-2/control-chart/Edit-Owner-data.json";
		//String fileName = "/Users/nsundbaum/Documents/kth/exjobb/code/jenkins/jobs/spring-petclinic-load-test/"
		//		+ "builds/53/control-chart/Find-data.json";
		double ucl = 43.38;
		
		TestCaseData data = null;
		ObjectMapper mapper = new ObjectMapper();
	    try {
	    	data = mapper.readValue(new File(fileName), TestCaseData.class);
	    } 
	    catch (IOException e) {
	      throw new RuntimeException("Failed to read test case data from " + fileName, e);
	    }
	    
	    long numViolations = 0;
	    long totalEntries = 0;
	    for(DataPoint dataPoint : data.getDataPoints()) {
	    	totalEntries++;
	    	if(dataPoint.getY() > ucl) {
	    		numViolations++;
	    	}
	    }
	    
	    System.out.println("Total entries:  " + totalEntries);
	    System.out.println("Num violations: " + numViolations);
	    System.out.println("Violation rate: " + (100 * (numViolations / (double)totalEntries)));
	}
}
