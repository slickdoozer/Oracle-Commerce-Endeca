package com.endeca.training.handlers.inventory;

import com.endeca.infront.cartridge.RecordDetailsHandler;

import java.util.Map;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.RecordDetails;
import com.endeca.infront.cartridge.RecordDetailsConfig;
import com.endeca.infront.cartridge.model.Record;

public class InventoryHandler extends RecordDetailsHandler {
	
	public RecordDetails process(RecordDetailsConfig cartridgeConfig) {
		RecordDetails rd = null;
		
		try {
			// Process cartridge normally
			rd = super.process(cartridgeConfig);
			
			// Lookup inventory data and append to RecordDetails
			Record rec = rd.getRecord();
			Map attribs = rec.getAttributes();
			String avail = this.checkInventory(attribs.get("product.id").toString());
			attribs.put("availability", avail);
			
		} catch (CartridgeHandlerException e) {
			e.printStackTrace();
		}
		
		return rd;
	}
	
	private String checkInventory(String id)
	{
		/* This is where you would connect to an inventory system and determine if an item is in stock or not
		 * for this course we will just return a generated string.
		 */
		
		double num = Math.random();
		
		if (num <= 0.5)
			return "In Stock";
		else if (num <= 0.8)
			return "On BACKORDER";
		else
			return "UNAVAILABLE";
		
	}

}
