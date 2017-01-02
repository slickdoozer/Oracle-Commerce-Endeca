package com.endeca.training.color;

import com.endeca.infront.assembler.BasicContentItem;
import com.endeca.infront.assembler.ContentItem;

public class ColorConfig extends BasicContentItem {

	public ColorConfig() {
		super();
		}
		public ColorConfig(final String pType) {
		super(pType);
		}
		public ColorConfig(ContentItem pContentItem) {
		super(pContentItem);
		}
		public String getMessageColor() {
		return getTypedProperty("messageColor");
		}
		public void setMessageColor(String color) {
		this.put("messageColor", color);
		}
	
}
