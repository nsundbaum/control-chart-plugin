package com.sundbaum.degreeproject.controlchart;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestRun {
	private Map<String, TestCase> testCases = new HashMap<String, TestCase>();
	private Baselines baselines;
	
	public TestRun(Baselines baselines) {
		this.baselines = baselines;
	}
	
	public void addDataPoint(String type, Date timestamp, long responseTime) {
		TestCase testCase = testCases.get(type);
		if(testCase == null) {
			Baseline baseline = baselines.get(type); 
			testCase = new TestCase(baseline);
			testCases.put(type, testCase);
		}
		testCase.addDataPoint(timestamp, responseTime);
	}
	
	public Collection<TestCase> getTestCases() {
		return testCases.values();
	}
	
	public TestCase getTestCase(String type) {
		TestCase testCase = testCases.get(type);
		return testCase;
	}
}
