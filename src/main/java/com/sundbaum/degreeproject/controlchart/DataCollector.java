package com.sundbaum.degreeproject.controlchart;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sundbaum.degreeproject.controlchart.TestCaseData.DataPoint;

public class DataCollector {
	private Map<String, TestCaseData> nameToTestCaseData;
	
	public DataCollector() {
		nameToTestCaseData = new HashMap<String, TestCaseData>();
	}
	
	public void addDataPoint(String name, DataPoint dataPoint) {
    	TestCaseData data = nameToTestCaseData.get(name);
    	if(data == null) {
    		data = new TestCaseData();
    		nameToTestCaseData.put(name, data);
    	}
    	
    	data.add(dataPoint);
    }
	
	protected Set<String> getTestCaseNames() {
		return nameToTestCaseData.keySet();
	}
	
	protected TestCaseData getTestCaseData(String name) {
		return nameToTestCaseData.get(name);
	}
}
