package com.sundbaum.degreeproject.controlchart;

import org.kohsuke.stapler.DataBoundConstructor;

public class BasicBaseline implements Baseline {
	private String type;
	private double mean;
	private double standardDeviation;
	private double upperLimit;
	private double lowerLimit;
	
	// TODO: Criteria for test failures
	@DataBoundConstructor
	public BasicBaseline(String type, double mean, double standardDeviation) {
		this.type = type;
		this.mean = mean;
		this.standardDeviation = standardDeviation;
		this.lowerLimit = Math.max(0, mean - (3 * standardDeviation));
		this.upperLimit = mean + (3 * standardDeviation);
	}
	
	@Override
	public boolean isViolation(long responseTime) {
		return responseTime > upperLimit;
	}

	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public Double getMean() {
		return mean;
	}
	
	@Override
	public Double getStandardDeviation() {
		return standardDeviation;
	}
	
	@Override
	public Double getUpperControlLimit() {
		return upperLimit;
	}

	@Override
	public Double getLowerControlLimit() {
		return lowerLimit;
	}

	@Override
	public String toString() {
		return "Baseline [type=" + type + ", mean=" + mean
				+ ", standardDeviation=" + standardDeviation + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicBaseline other = (BasicBaseline) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
