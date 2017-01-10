package com.slickdoozer.cas.extension.manipulator.splitfield;

import com.endeca.cas.extension.Manipulator;
import com.endeca.cas.extension.PipelineComponentRuntimeContext;
import com.endeca.cas.extension.annotation.CasManipulator;

/**
 * A CAS manipulator that adds a new property that is a substring of another property.
 */
@CasManipulator(
		supportsIncrementals=true, 
		deleteRecordsBypassManipulator = true,
		displayName="Split Field Manipulator", 
		description="Generates a new property representing another property value split using a delimiter")
public class SplitFieldManipulator extends Manipulator<SplitFieldManipulatorRuntime,SplitFieldManipulatorConfig>{

	/**
	 * Returns the configuration class used to configure this manipulator.
	 */
	@Override
	public Class<SplitFieldManipulatorConfig> getConfigurationClass() {
		return SplitFieldManipulatorConfig.class;
	}

	/**
	 * Returns the runtime class for this manipulator
	 */
	@Override
	public Class<SplitFieldManipulatorRuntime> getRuntimeClass() {
		return SplitFieldManipulatorRuntime.class;
	}

	/**
	 * Returns the runtime that applies the manipulation to records during a
	 * pipeline execution.
	 * 
	 * @param configuration the configuration of the manipulator
	 * @param context the runtime context of the manipulator
	 */
	@Override
	public SplitFieldManipulatorRuntime createManipulatorRuntime(
			SplitFieldManipulatorConfig configuration, PipelineComponentRuntimeContext context) {
		return new SplitFieldManipulatorRuntime(context, configuration);
	}

}
