package com.sundbaum.degreeproject.controlchart;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.sundbaum.degreeproject.controlchart.gatling.GatlingLoadTestParser;

public class ControlChartPublisher extends Recorder {
	private static final Logger log = Logger.getLogger(ControlChartPublisher.class.getName());
	
    private final int violationRate;
    private final Set<BasicBaseline> baselines;
    
    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public ControlChartPublisher(int violationRate, Set<BasicBaseline> baselines) {
        this.violationRate = violationRate;
        
        if(baselines == null) {
        	baselines = new HashSet<BasicBaseline>();
        }
        this.baselines = baselines;
    }

    public int getViolationRate() {
        return violationRate;
    }
    
    public Set<BasicBaseline> getBaselines() {
    	return baselines;
    }
    
    @Override
    public BuildStepMonitor getRequiredMonitorService() {
    	return BuildStepMonitor.BUILD;
    }

    private void findSimulationLogs(File current, List<File> files, BuildListener listener) {
    	for(File file : current.listFiles()) {
    		if(file.isDirectory()) {
    			findSimulationLogs(file, files, listener);
    		}
    		else if("simulation.log".equalsIgnoreCase(file.getName())) {
    			files.add(file);
    		}
    	}
    }
    
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
       List<File> simulationLogs = new ArrayList<File>();
    	findSimulationLogs(build.getRootDir(), simulationLogs, listener);
    	if(simulationLogs.isEmpty()) {
    		throw new RuntimeException("No simulation log files found under: " + build.getRootDir());
    	}
    	
    	listener.getLogger().println("Found simulation logs: " + simulationLogs);
    	listener.getLogger().println("Using baselines: " + baselines);
    	
    	TestRun testRun = new TestRun(new Baselines(baselines));
    	for(File simulationLog : simulationLogs) {
    		LoadTestParser parser = new GatlingLoadTestParser(simulationLog);
    		
    		DataCollector dataCollector = new DataCollector();
    		parser.parse(dataCollector);
    		
    		for(String testCaseName : dataCollector.getTestCaseNames()) {
    			TestCaseData testCaseData = dataCollector.getTestCaseData(testCaseName);
    			testRun.setData(build, testCaseName, testCaseData);
        	}
    	}
        
        ControlChartBuildAction ccAction = new ControlChartBuildAction(build, testRun);
        build.addAction(ccAction);
        
        return true;
    }
    
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link ControlChartPublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckViolationRate(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a violation rate.");
            int intValue = -1;
            try {
            	intValue = Integer.parseInt(value);
            }
            catch(NumberFormatException e) {
            	return FormValidation.error("Violation rate must be an integer.");
            }
            
            if(intValue < 0 || intValue > 100) {
            	return FormValidation.error("Violation rate must be between 0 and 100.");
            }
            
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Verify load test with control chart";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            save();
            return super.configure(req,formData);
        }
    }
}

