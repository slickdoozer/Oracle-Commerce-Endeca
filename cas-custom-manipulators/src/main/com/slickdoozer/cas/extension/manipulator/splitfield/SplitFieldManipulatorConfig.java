package com.slickdoozer.cas.extension.manipulator.splitfield;

import java.util.List;

import com.endeca.cas.extension.PipelineComponentConfiguration;
import com.endeca.cas.extension.ValidationFailure;
import com.endeca.cas.extension.annotation.ConfigurationGroup;
import com.endeca.cas.extension.annotation.ConfigurationGroupOrder;
import com.endeca.cas.extension.annotation.StringProperty;
import com.google.common.collect.Lists;

/**
 * Configuration of a split field manipulator. The configuration consists of:
 * 
 * <ul>
 * <li>Source Property - the source property to evaluate the split operationg</li>
 * <li>Target Property - the target property for the result of the split operation</li>
 * <li>Delimiter String - one or more consecutive characters representing a split delimiter</li>
 * <li>Remove Source Property - boolean determining if source property will be removed</li>
 * </ul>
 */
// Use the @ConfigurationGroupOrder/@ConfigurationGroup annotations to control the order and grouping
// of the configuration settings in the UI. This is optional.
@ConfigurationGroupOrder({
	@ConfigurationGroup(propertyOrder={"sourceProperty", "targetProperty", "delimiter", "removeSourceProp"})
})
public class SplitFieldManipulatorConfig extends PipelineComponentConfiguration<SplitFieldManipulatorConfig> {

	public static final String MISSING_DELIMITER_FAILURE_MSG = "Delimiter String has not been provided";
	public static final String INVALID_REMOVE_SOURCE_PROPERTY_FAILURE_MSG = "Remove Source Property may only be set to TRUE or FALSE.";
	
	@StringProperty(isRequired=true, name="sourceProperty", displayName="Source Property",
			description="Enter single-valued Source Property name.")
	private String mSourceProperty;
	
	@StringProperty(isRequired=true, name="targetProperty", displayName="Target Property",
			description="Enter name for generated multi-valued property.")
	private String mTargetProperty;
	
	@StringProperty(isRequired=true, name="delimiter", displayName="Delimiter String", 
			description="Delimiter String. Can be one or more consecutive characters.")
	private String mDelimiter;
	
	@StringProperty(isRequired=false, name="removeSourceProp", displayName="Remove Source Property", 
			description="Enter TRUE to remove source property. Leave FALSE for no action.", defaultValue="FALSE")
	private String mRemoveProperty;
	
	/**
	 * Returns a list of configuration validation failures. 
	 * 
	 * @return a list of configuration validation failures.
	 */
	@Override
	public List<ValidationFailure> validate() {
		List<ValidationFailure> failures = Lists.newLinkedList();
		
		// Check for an empty delimiter
		if (mDelimiter == null || mDelimiter.isEmpty()) {
			failures.add(new ValidationFailure(MISSING_DELIMITER_FAILURE_MSG));
		}
		
		// Check for an invalid remove source property value. Default is FALSE.
		if (!mRemoveProperty.equalsIgnoreCase("TRUE") && !mRemoveProperty.equalsIgnoreCase("FALSE")) {
			failures.add(new ValidationFailure(INVALID_REMOVE_SOURCE_PROPERTY_FAILURE_MSG));
		}
		
		return failures;
	}
	
	public String getSourceProperty() {
		return mSourceProperty;
	}

	public void setSourceProperty(String sourceProperty) {
		mSourceProperty = sourceProperty;
	}

	public String getTargetProperty() {
		return mTargetProperty;
	}

	public void setTargetProperty(String targetProperty) {
		mTargetProperty = targetProperty;
	}

	public String getDelimiter() {
		return mDelimiter;
	}

	public void setDelimiter(String delimiter) {
		mDelimiter = delimiter;
	}

	public String getRemoveProperty() {
		return mRemoveProperty;
	}

	public void setRemoveProperty(String removeSourceProp) {
		mRemoveProperty = removeSourceProp;
	}

	@Override
	public String toString() {
		return "SubstringManipulatorConfig(" +
			"source=" + mSourceProperty + ", " +
			"target=" + mTargetProperty + ", " +
			"delimiter=" + mDelimiter + ", " +
			"removeSourceProp=" + mRemoveProperty + ")";
	}
}
