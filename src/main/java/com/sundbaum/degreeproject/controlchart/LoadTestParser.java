package com.sundbaum.degreeproject.controlchart;

import java.io.IOException;


public interface LoadTestParser {
	void parse(DataCollector dataCollector) throws IOException;
}
