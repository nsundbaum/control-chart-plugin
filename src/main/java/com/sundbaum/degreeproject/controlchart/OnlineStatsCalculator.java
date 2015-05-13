package com.sundbaum.degreeproject.controlchart;

public class OnlineStatsCalculator {
	private long n = 0;
	private double mean = 0;
	private double m2 = 0;
	
	// See http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
	public void add(double x) {
		n = n + 1;
		double delta = x - mean;
		mean = mean + delta / n;
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
	
	public static void main(String[] args) {
		OnlineStatsCalculator calc = new OnlineStatsCalculator();
		
		calc.add(2.0);
		calc.add(1.0);
		calc.add(7.0);
		calc.add(2.0);
		calc.add(4.0);
		
		System.out.println(calc.getVariance());
		System.out.println(calc.getStandardDeviation());
		System.out.println(calc.getMean());
	}
}
