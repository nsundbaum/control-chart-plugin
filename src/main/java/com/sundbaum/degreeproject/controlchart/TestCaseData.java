package com.sundbaum.degreeproject.controlchart;

import java.util.ArrayList;
import java.util.List;

public class TestCaseData {
	private List<DataPoint> dataPoints;
	
	public TestCaseData() {
		dataPoints = new ArrayList<TestCaseData.DataPoint>();
	}
	
	public void add(DataPoint dataPoint) {
		dataPoints.add(dataPoint);
	}
	
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public static class DataPoint {
		private double x;
		private double y;
		
		public DataPoint() {}
		
		public DataPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
	}
}
