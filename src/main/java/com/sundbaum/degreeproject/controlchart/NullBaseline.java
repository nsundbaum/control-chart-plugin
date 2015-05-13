package com.sundbaum.degreeproject.controlchart;

public class NullBaseline implements Baseline {
	private String type;
	
	public NullBaseline(String type) {
		this.type = type;
	}

	@Override
	public boolean isViolation(long responseTime) {
		return false;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Double getMean() {
		return Double.NaN;
	}

	@Override
	public Double getStandardDeviation() {
		return Double.NaN;
	}

	@Override
	public Double getUpperControlLimit() {
		return Double.NaN;
	}

	@Override
	public Double getLowerControlLimit() {
		return Double.NaN;
	}
}
