package com.sundbaum.degreeproject.controlchart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Baselines {
	private Map<String, Baseline> typeToBaselines = new HashMap<String, Baseline>();
	
	public Baselines(Collection<? extends Baseline> baselines) {
		for(Baseline baseline : baselines) {
			typeToBaselines.put(baseline.getType(), baseline);
		}
	}
	
	public Baseline get(String type) {
		Baseline baseline = typeToBaselines.get(type);
		if(baseline == null) {
			baseline = new NullBaseline(type);
		}
		return baseline;
	}
}
