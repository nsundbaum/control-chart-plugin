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
		private long x;
		private long y;
		
		public DataPoint() {}
		
		public DataPoint(long x, long y) {
			this.x = x;
			this.y = y;
		}

		public long getX() {
			return x;
		}

		public void setX(long x) {
			this.x = x;
		}

		public long getY() {
			return y;
		}

		public void setY(long y) {
			this.y = y;
		}
	}
}
