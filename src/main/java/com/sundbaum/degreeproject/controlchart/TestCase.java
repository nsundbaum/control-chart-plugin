package com.sundbaum.degreeproject.controlchart;

import hudson.model.AbstractBuild;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundbaum.degreeproject.controlchart.TestCaseData.DataPoint;

public class TestCase {
	private Baseline baseline;
	private long numViolations;
	private OnlineStatsCalculator calculator;
	private WeakReference<TestCaseData> testCaseData;
	
	public TestCase(Baseline baseline) {
		this.baseline = baseline;
		this.calculator = new OnlineStatsCalculator();
	}
	
	public long getNumViolations() {
		return numViolations;
	}
	
	public double getViolationRate() {
		if(calculator.getNumDataPoints() == 0) {
			return 0;
		}
		return numViolations / (double)calculator.getNumDataPoints();
	}

	public String getType() {
		return baseline.getType();
	}
	
	public double getMean() {
		return calculator.getMean();
	}
	
	public double getVariance() {
		return calculator.getVariance();
	}
	
	public double getStandardDeviation() {
		return calculator.getStandardDeviation();
	}
	
	public long getNumDataPoints() {
		return calculator.getNumDataPoints();
	}
	
	public Baseline getBaseline() {
		return baseline;
	}
	
	public TestCaseData getData(AbstractBuild<?, ?> build) {
		TestCaseData data = null;
	    WeakReference<TestCaseData> wr = this.testCaseData;
	    if (wr != null) {
	      data = wr.get();
	      if (data != null) 
	        return data;
	    }
	 
	    File testCaseFile = getTestCaseFile(build);
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	    	data = mapper.readValue(testCaseFile, TestCaseData.class);
	    } 
	    catch (IOException e) {
	      throw new RuntimeException("Failed to read test case data from " + testCaseFile, e);
	    }
	    this.testCaseData = new WeakReference<TestCaseData>(data);
	    return data;
	}
	
	private void add(DataPoint dataPoint) {
		long responseTime = dataPoint.getY();
		
		calculator.add(responseTime);
		if(baseline.isViolation(responseTime)) {
			numViolations++;
		}
	}
	
	public void setData(AbstractBuild<?, ?> build, TestCaseData data) {
		numViolations = 0;
		calculator.reset();
		for(DataPoint dataPoint : data.getDataPoints()) {
			add(dataPoint);
		}
		
		this.testCaseData = new WeakReference<TestCaseData>(data);
		File testCaseFile = getTestCaseFile(build);
		testCaseFile.getParentFile().mkdirs();
		
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	    	mapper.writeValue(testCaseFile, data);
		} 
	    catch (IOException e) {
	      throw new RuntimeException("Failed to write test case data to " + testCaseFile, e);
	    }
	}
	
	private File getTestCaseFile(AbstractBuild<?, ?> build) {
		String typeAsFileName = getType().replaceAll("\\s+","-");
		return new File(build.getRootDir(), "/control-chart/" + typeAsFileName + "-data.json");
	}
}
