package com.slickdoozer.cartridges.custom;

import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.cartridge.RecordDetails;
import com.endeca.infront.cartridge.RecordDetailsConfig;
import com.endeca.infront.cartridge.RecordDetailsHandler;

public class RecordDetailsWithInventoryHandler extends RecordDetailsHandler {

	public RecordDetails process(RecordDetailsConfig cartridgeConfig) {
		RecordDetails rd = null;
		
		try {
			rd = super.process(cartridgeConfig);
		} catch (CartridgeHandlerException e) {
			System.out.println("Assembler : CUSTOM RecordDetailsWithInventoryHandler : EXCEPTION e="+e);
			e.printStackTrace();
		}
		
		// Add inventory status
		String sku = (String)rd.get("product.sku");
		String inventoryStatus = getInventoryForItem(sku);
		System.out.println("Assembler : CUSTOM RecordDetailsWithInventoryHandler : Inventory lookup - SKU="+sku+" InventoryStatus="+inventoryStatus+"!");
		rd.put("availability", inventoryStatus);
		
		return rd;
	}
	
	private String getInventoryForItem(String sku) {
		String invStatus = "";
		double rand = Math.random();
		
		if (rand < 0.25) {
			invStatus = "Out of Stock";
		} else if (rand < 0.5) {
			invStatus = "Low Stock";
		} else {
			invStatus = "In Stock";
		}
		
		return invStatus;
	}
	
}
