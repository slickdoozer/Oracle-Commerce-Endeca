/*
 * Copyright 2001, 2015, Oracle and/or its affiliates. All rights reserved.
 * Oracle and Java are registered trademarks of Oracle and/or its 
 * affiliates. Other names may be trademarks of their respective owners.
 * UNIX is a registered trademark of The Open Group.
 * 
 * This software and related documentation are provided under a license 
 * agreement containing restrictions on use and disclosure and are 
 * protected by intellectual property laws. Except as expressly permitted 
 * in your license agreement or allowed by law, you may not use, copy, 
 * reproduce, translate, broadcast, modify, license, transmit, distribute, 
 * exhibit, perform, publish, or display any part, in any form, or by any 
 * means. Reverse engineering, disassembly, or decompilation of this 
 * software, unless required by law for interoperability, is prohibited.
 * The information contained herein is subject to change without notice 
 * and is not warranted to be error-free. If you find any errors, please 
 * report them to us in writing.
 * U.S. GOVERNMENT END USERS: Oracle programs, including any operating 
 * system, integrated software, any programs installed on the hardware, 
 * and/or documentation, delivered to U.S. Government end users are 
 * "commercial computer software" pursuant to the applicable Federal 
 * Acquisition Regulation and agency-specific supplemental regulations. 
 * As such, use, duplication, disclosure, modification, and adaptation 
 * of the programs, including any operating system, integrated software, 
 * any programs installed on the hardware, and/or documentation, shall be 
 * subject to license terms and license restrictions applicable to the 
 * programs. No other rights are granted to the U.S. Government.
 * This software or hardware is developed for general use in a variety 
 * of information management applications. It is not developed or 
 * intended for use in any inherently dangerous applications, including 
 * applications that may create a risk of personal injury. If you use 
 * this software or hardware in dangerous applications, then you shall 
 * be responsible to take all appropriate fail-safe, backup, redundancy, 
 * and other measures to ensure its safe use. Oracle Corporation and its 
 * affiliates disclaim any liability for any damages caused by use of this 
 * software or hardware in dangerous applications.
 * This software or hardware and documentation may provide access to or 
 * information on content, products, and services from third parties. 
 * Oracle Corporation and its affiliates are not responsible for and 
 * expressly disclaim all warranties of any kind with respect to 
 * third-party content, products, and services. Oracle Corporation and 
 * its affiliates will not be responsible for any loss, costs, or damages 
 * incurred due to your access to or use of third-party content, products, 
 * or services.
 */

package com.slickdoozer.cas.extension.manipulator.splitfield;

import java.util.List;

import com.endeca.cas.extension.ValidationFailure;
import com.slickdoozer.cas.extension.ExtensionTestCase;

/**
 * Unit test of SplitFieldManipulatorConfig.
 */
public class SplitFieldManipulatorConfigTest extends ExtensionTestCase {

	public SplitFieldManipulatorConfigTest(String name) {
		super(name);
	}

	/**
	 * Tests validating a SplitFieldManipulatorConfig with no validation
	 * errors.
	 */
	public void testNoValidationError() {
		SplitFieldManipulatorConfig config = createSplitFieldManipulatorConfig();
		List<ValidationFailure> failures = config.validate();
		assertEquals(0, failures.size());
	}
	
	/**
	 * Tests validating a SplitFieldManipulatorConfig with no delimiter
	 * and an invalid remove source property flag.
	 */
	public void testNegativeNoDelimiterAndInvalidRemoveSourceProperty() {
		SplitFieldManipulatorConfig config = new SplitFieldManipulatorConfig();
		config.setSourceProperty("Source");
		config.setTargetProperty("Target");
		config.setDelimiter(null);
		config.setRemoveProperty("FOO");
		List<ValidationFailure> failures = config.validate();
		assertEquals(2, failures.size());
		assertEquals(SplitFieldManipulatorConfig.MISSING_DELIMITER_FAILURE_MSG,
				failures.get(0).getFailureMessage());
		assertEquals(SplitFieldManipulatorConfig.INVALID_REMOVE_SOURCE_PROPERTY_FAILURE_MSG, 
				failures.get(1).getFailureMessage());
	}
	
	/**
	 * Tests the configuration object's isFullCrawlRequired implementation, 
	 * assuming a change to SourceProperty setting is a property that requires 
	 * a full acquisition.   
	 */
	public void testIsFullAcquisitionRequired() {
		SplitFieldManipulatorConfig oldConfig = createSplitFieldManipulatorConfig();
		SplitFieldManipulatorConfig newConfig = createSplitFieldManipulatorConfig();
		// same configuration should not require full acquisition
		assertFalse(newConfig.isFullAcquisitionRequired(oldConfig));
		
		newConfig.setSourceProperty("AlternateSource");
		assertTrue(newConfig.isFullAcquisitionRequired(oldConfig));
	}

	private SplitFieldManipulatorConfig createSplitFieldManipulatorConfig() {
		SplitFieldManipulatorConfig config = new SplitFieldManipulatorConfig();
		config.setSourceProperty("Source");
		config.setTargetProperty("Target");
		config.setDelimiter("|");
		config.setRemoveProperty("FALSE");
		return config;
	}
}
