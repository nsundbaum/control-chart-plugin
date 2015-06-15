package com.sundbaum.degreeproject.controlchart;

import hudson.model.AbstractBuild;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestRun {
	private Map<String, TestCase> testCases = new HashMap<String, TestCase>();
	private Baselines baselines;
	
	public TestRun(Baselines baselines) {
		this.baselines = baselines;
	}
	
	public Collection<TestCase> getTestCases() {
		return testCases.values();
	}
	
	public TestCase getTestCase(String type) {
		return testCases.get(type);
	}

	public void setData(AbstractBuild<?, ?> build, String testCaseName, TestCaseData testCaseData) {
		TestCase testCase = testCases.get(testCaseName);
		if(testCase == null) {
			Baseline baseline = baselines.get(testCaseName); 
			testCase = new TestCase(baseline);
			testCases.put(testCaseName, testCase);
		}
		
		testCase.setData(build, testCaseData);
	}
}
