package com.sundbaum.degreeproject.controlchart.gatling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.sundbaum.degreeproject.controlchart.DataCollector;
import com.sundbaum.degreeproject.controlchart.LoadTestParser;
import com.sundbaum.degreeproject.controlchart.TestCaseData.DataPoint;

public class GatlingLoadTestParser implements LoadTestParser {
	private File simulationLog;
	
	public GatlingLoadTestParser(File simulationLog) {
		this.simulationLog = simulationLog;
	}

	@Override
	public void parse(DataCollector dataCollector) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(simulationLog)); 
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] elements = line.split("\t+");
		    	if("REQUEST".equals(elements[2])) {
		    		String status = elements[8];
		    		if("ok".equalsIgnoreCase(status)) {
		    			String type = elements[3];
    		    		long start = Long.parseLong(elements[4]);
    		    		long end = Long.parseLong(elements[7]);
    		    		long responseTime = end - start;
    		    		
    		    		dataCollector.addDataPoint(type, new DataPoint(start,  responseTime));
		    		}
		    	}
		    }
		}
		finally {
			br.close();
		}
	}
}
