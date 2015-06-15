package com.sundbaum.degreeproject.controlchart;

import hudson.util.Graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.servlet.ServletException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;
import org.kohsuke.stapler.ForwardToView;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.sundbaum.degreeproject.controlchart.TestCaseData.DataPoint;

public class ReportRenderer {
	private ControlChartBuildAction action;
	private TestCase testCase;

	public ReportRenderer(ControlChartBuildAction action, TestCase testCase) {
		this.action = action;
		this.testCase = testCase;
	}
	
	public void doIndex(StaplerRequest request, StaplerResponse response) throws IOException, ServletException {
		ForwardToView forward = new ForwardToView(action, "report.jelly")
			.with("testCase", testCase);
		
    forward.generateResponse(request, response, action);
  }
	
	public void doGraph(StaplerRequest request, StaplerResponse response) throws IOException, ServletException {
		Graph graph = new Graph(System.currentTimeMillis(), 800, 600) {
			@Override
			protected JFreeChart createGraph() {
				TestCaseData testCaseData = testCase.getData(action.getBuild());
				
				XYSeries series = new XYSeries("Response time");
				for(DataPoint dataPoint : testCaseData.getDataPoints()) {
					series.add(dataPoint.getX(), dataPoint.getY());
				}
		        XYDataset data = new XYSeriesCollection(series);
		        JFreeChart chart = ChartFactory.createScatterPlot("Control Chart - " + testCase.getType(),
		                                                          "Time", "Response time", data, PlotOrientation.VERTICAL,
		                                                          true,            // include legend
		                                                          false,            // tooltips
		                                                          false         );
		        
		        NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
		        domainAxis.setAutoRangeIncludesZero(false);
		        XYPlot plot = (XYPlot) chart.getPlot();
		        
		        XYItemRenderer renderer = plot.getRenderer();
		        renderer.setSeriesPaint(0, Color.blue);
		        double size = 4.0;
		        double delta = size / 2.0;
		        Shape shape = new Ellipse2D.Double(-delta, -delta, size, size);
		        renderer.setSeriesShape(0, shape);
		        
		        Baseline baseline = testCase.getBaseline();
		        if(!baseline.getUpperControlLimit().equals(Double.NaN)) {
		        	NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		        	rangeAxis.setUpperBound(1.5 * baseline.getUpperControlLimit());
		        	
		        	ValueMarker ucl = new ValueMarker(baseline.getUpperControlLimit());  // position is the value on the axis
			        ucl.setPaint(Color.red);
			        ucl.setStroke(new BasicStroke(2));
			        ucl.setLabel("     UCL"); // see JavaDoc for labels, colors, strokes
			        
			        plot.addRangeMarker(ucl);
		        }
		        
		        if(!baseline.getLowerControlLimit().equals(Double.NaN)) {
		        	ValueMarker lcl = new ValueMarker(baseline.getLowerControlLimit());  // position is the value on the axis
			        lcl.setPaint(Color.red);
			        lcl.setLabel("     LCL"); // see JavaDoc for labels, colors, strokes
			        
			        plot.addRangeMarker(lcl);
		        }
		        
		        if(!baseline.getLowerControlLimit().equals(Double.NaN)) {
		        	ValueMarker mean = new ValueMarker(baseline.getMean());  // position is the value on the axis
			        mean.setPaint(Color.black);
			        mean.setStroke(new BasicStroke(2));
			        mean.setLabel("     Mean"); // see JavaDoc for labels, colors, strokes
			        
			        plot.addRangeMarker(mean);
		        }
		        
				chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				return chart;
			}
		};
		
		graph.doPng(request, response);
  }
}
