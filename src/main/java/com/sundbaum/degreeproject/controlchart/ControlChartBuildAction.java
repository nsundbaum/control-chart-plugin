package com.sundbaum.degreeproject.controlchart;

import hudson.model.Action;
import hudson.model.AbstractBuild;

public class ControlChartBuildAction implements Action {
	private final AbstractBuild<?, ?> build;
	private TestRun testRun;
	
	public ControlChartBuildAction(AbstractBuild<?, ?> build, TestRun testRun) {
		this.build = build;
		this.testRun = testRun;
	}

	@Override
	public String getIconFileName() {
		return "/plugin/control-chart/img/logo.png";
	}

	@Override
	public String getDisplayName() {
		return "Control Chart";
	}

	@Override
	public String getUrlName() {
		return "control-chart";
	}
	
	public TestRun getTestRun() {
		return testRun;
	}
	
	public AbstractBuild<?, ?> getBuild() {
		return build;
	}
	
	public String getReportURL(String testCaseType) {
		return new StringBuilder().append(getUrlName()).append("/report/").append(testCaseType).toString();
	}
	
	public ReportRenderer getReport(String testCaseType) {
		return new ReportRenderer(this, testRun.getTestCase(testCaseType));
	}
}
