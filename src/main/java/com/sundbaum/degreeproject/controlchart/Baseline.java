package com.sundbaum.degreeproject.controlchart;

public interface Baseline {
	boolean isViolation(long responseTime);
	String getType();
	Double getMean();
	Double getStandardDeviation();
	Double getUpperControlLimit();
	Double getLowerControlLimit();
}
