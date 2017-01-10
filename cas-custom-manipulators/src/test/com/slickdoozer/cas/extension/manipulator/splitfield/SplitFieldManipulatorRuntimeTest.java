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

import com.endeca.cas.extension.ExecutionException;
import com.slickdoozer.cas.extension.ExtensionTestCase;
import com.slickdoozer.cas.extension.MockOutputChannel;
import com.slickdoozer.cas.extension.MockPipelineComponentRuntimeContext;
import com.endeca.itl.record.PropertyValue;
import com.endeca.itl.record.Record;

/**
 * Unit test of the SplitFieldManipulatorRuntime.
 */
public class SplitFieldManipulatorRuntimeTest extends ExtensionTestCase {

	/**
	 * A MockPipelineComponentRuntimeContext that will be used to inspect
	 * the output of the manipulator.
	 */
	private MockPipelineComponentRuntimeContext mMockRuntimeContext;
	
	public SplitFieldManipulatorRuntimeTest(String name) {
		super(name);
	}

	/**
	 * Called before each test method to setup the MockPipelineComponentRuntimeContext.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mMockRuntimeContext = new MockPipelineComponentRuntimeContext();
	}

	/**
	 * Called after each test method to tear down the MockPipelineComponentRuntimeContext.
	 */
	@Override
	protected void tearDown() throws Exception {
		mMockRuntimeContext = null;
		super.tearDown();
	}
	
	/**
	 * Tests the split field manipulation on a record with the source property and some 
	 * delimited data.
	 */
	public void testBasic() throws ExecutionException {
		Record record = new Record();
		record.addPropertyValue(new PropertyValue("Property1","This is value1|of property1."));
		record.addPropertyValue(new PropertyValue("Property1","This is|value2 of property1."));
		record.addPropertyValue(new PropertyValue("Property2","This is value1 of property2."));
		record.addPropertyValue(new PropertyValue("Property3","This is value1 of property3."));
		
		Record expectedOutput = new Record(record);
		expectedOutput.addPropertyValue(new PropertyValue("Target","This is value1"));
		expectedOutput.addPropertyValue(new PropertyValue("Target","of property1."));
		expectedOutput.addPropertyValue(new PropertyValue("Target","This is"));
		expectedOutput.addPropertyValue(new PropertyValue("Target","value2 of property1."));
		
		SplitFieldManipulatorConfig config = new SplitFieldManipulatorConfig();
		config.setSourceProperty("Property1");
		config.setTargetProperty("Target");
		config.setDelimiter("|");
		config.setRemoveProperty("FALSE");
		
		SplitFieldManipulatorRuntime runtime = new SplitFieldManipulatorRuntime(mMockRuntimeContext, 
				config);
		runtime.processRecord(record);
		
		assertEquals(expectedOutput, getRecordOutput());
	}
	
	/**
	 * Test removal of source property on a record if required.
	 */
	public void testRemoveProperty() throws ExecutionException {
		Record record = new Record();
		record.addPropertyValue(new PropertyValue("Property1","This is value1|of property1."));
		record.addPropertyValue(new PropertyValue("Property1","This is|value2 of property1."));
		record.addPropertyValue(new PropertyValue("Property2","This is value1 of property2."));
		record.addPropertyValue(new PropertyValue("Property3","This is value1 of property3."));
		
		Record expectedOutput = new Record();
		expectedOutput.addPropertyValue(new PropertyValue("Target","This is value1"));
		expectedOutput.addPropertyValue(new PropertyValue("Target","of property1."));
		expectedOutput.addPropertyValue(new PropertyValue("Target","This is"));
		expectedOutput.addPropertyValue(new PropertyValue("Target","value2 of property1."));
		expectedOutput.addPropertyValue(new PropertyValue("Property2","This is value1 of property2."));
		expectedOutput.addPropertyValue(new PropertyValue("Property3","This is value1 of property3."));
		
		SplitFieldManipulatorConfig config = new SplitFieldManipulatorConfig();
		config.setSourceProperty("Property1");
		config.setTargetProperty("Target");
		config.setDelimiter("|");
		config.setRemoveProperty("TRUE");
		
		SplitFieldManipulatorRuntime runtime = new SplitFieldManipulatorRuntime(mMockRuntimeContext, 
				config);
		runtime.processRecord(record);
		
		assertEquals(expectedOutput, getRecordOutput());
	}
	
	/**
	 * Verifies that one record has been output to the MockOutputter and returns that record.
	 * 
	 * @return the record output to the MockOutputter
	 */
	private Record getRecordOutput() {
		MockOutputChannel mockOutputter = mMockRuntimeContext.getOutputChannel();
		assertEquals(1, mockOutputter.getRecordsOutput().size());
		return mockOutputter.getRecordsOutput().get(0);
	}
}
