package com.slickdoozer.cas.extension.manipulator.splitfield;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.endeca.cas.extension.ExecutionException;
import com.endeca.cas.extension.ManipulatorRuntime;
import com.endeca.cas.extension.OutputChannel;
import com.endeca.cas.extension.PipelineComponentRuntimeContext;
import com.endeca.itl.record.PropertyValue;
import com.endeca.itl.record.Record;

/**
 * Runtime of the split field manipulator that will execute in the context of a pipeline.
 */
public class SplitFieldManipulatorRuntime extends ManipulatorRuntime {

	/**
	 * The OutputChannel this manipulator will write to.
	 */
	private final OutputChannel mOutputChannel;
	
	/**
	 * The configuration the manipulator will use.
	 */
	private final SplitFieldManipulatorConfig mConfig;
	
	/**
	 * A Logger for log messages.
	 */
	private final Logger mLogger = LoggerFactory.getLogger(getClass());
	
	public SplitFieldManipulatorRuntime(PipelineComponentRuntimeContext context, 
			SplitFieldManipulatorConfig config) {
		super(context);
		mOutputChannel = context.getOutputChannel();
		mConfig = config;
		mLogger.debug("Created runtime with config {}", config);
	}
	
	/**
	 * Applies the manipulation to the given record and outputs the modified record.
	 * 
	 * @param record The record that the manipulation should be applied to.
	 * @throws ExecutionException If there is an error while processing the record.
	 */
	@Override
	public void processRecord(Record record) throws ExecutionException {
		mLogger.debug("Started processing record {}", record);
		
		// Loop over all values of the source property
		for (PropertyValue propertyValue : record.getPropertyValues(mConfig.getSourceProperty())) {
			
			/**
			 * Split the matching property based on the specified delimiter and
			 * save results to one or more new target properties on the record.
			 * --------------------------------------------------------------------------------
			 * NOTE: Favored using an older backwards-compatible 'java.util.StringTokenizer' to
			 * split the string instead of the recommended 'java.lang.String split()' method, in
			 * order to avoid needing to escape all the following javaRegexMetaCharacters
			 * "( ) [ ] { { \ ^ $ | ? * + . < > - = !" which can not be used as a delimiter
			 * in the 'split()' function unless escaped.
			 * --------------------------------------------------------------------------------
			 */
			if ( propertyValue.getValue().length() > 0 && propertyValue.getValue().indexOf(mConfig.getDelimiter()) > 0 ) {
				// Generate a target property for each tokenized value
				StringTokenizer st = new StringTokenizer(propertyValue.getValue(), mConfig.getDelimiter());
				while (st.hasMoreTokens()) {
			         record.addPropertyValue(new PropertyValue(mConfig.getTargetProperty(), st.nextToken()));
			     }
				// Remove the source property if required
				if (mConfig.getRemoveProperty().equalsIgnoreCase("TRUE")) {
					record.removePropertyValue(propertyValue);
				}
			}
		}
		
		mLogger.debug("Finished processing record {}", record);
		
		// Output the modified record
		mOutputChannel.output(record);
	}
}
