package com.sundbaum.degreeproject.controlchart;

public class OnlineStatsCalculator {
	private long n = 0;
	private double mean = 0;
	private double m2 = 0;
	
	public void reset() {
		n = 0;
		mean = 0;
		m2 = 0;
	}
	
	// See http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
	public void add(double x) {
		n = n + 1;
		double delta = x - mean;
		mean = mean + (delta / n);
		m2 = m2 + delta * (x - mean);
	}
	
	public double getVariance() {
		if(n < 2) {
			return 0;
		}
	 
		return m2 / n; // Divide by n to get population variance. n-1 to get sample variance.
	}
	
	public double getMean() {
		return mean;
	}
	
	public double getStandardDeviation() {
		return Math.sqrt(getVariance());
	}
	
	public long getNumDataPoints() {
		return n;
	}
}
