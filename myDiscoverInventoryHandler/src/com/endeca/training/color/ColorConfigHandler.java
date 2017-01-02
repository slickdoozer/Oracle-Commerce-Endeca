package com.endeca.training.color;
import com.endeca.infront.assembler.AbstractCartridgeHandler;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.assembler.BasicContentItem;
import com.endeca.infront.assembler.ContentItemInitializer;
import com.endeca.infront.navigation.NavigationState;
import com.endeca.infront.navigation.url.ActionPathProvider;
import com.endeca.training.color.ColorConfig;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ColorConfigHandler extends AbstractCartridgeHandler {

	private ContentItemInitializer mInitializer;
	private Map<String, String> mColorOptions;
	
	private ActionPathProvider mActionPathProvider;
	private NavigationState mNavigationState;
	
	public void setActionPathProvider(ActionPathProvider mActionPath) {
		this.mActionPathProvider = mActionPath;
	}
	
	public ActionPathProvider getActionPathProvider() {
		return mActionPathProvider;
	}
	
	public void setNavigationState(NavigationState navigationState) {
		mNavigationState = navigationState;
	}
	
	public NavigationState getNavigationState() {
		return mNavigationState;
	}

	public void setContentItemInitializer(ContentItemInitializer initializer) {
		mInitializer = initializer;
	}

	public void setColorOptions(Map<String, String> colorOptions) {
		mColorOptions = colorOptions;
	}

	/**
	 * Returns the merged configuration based on Spring defaults, Experience
	 * Manager configuration, and request parameters
	 */
	@Override
	public ContentItem initialize(ContentItem pContentItem) {
		
		try{
			   super.initialize(pContentItem);
			}catch(CartridgeHandlerException e){
				e.printStackTrace();
			}
			
			for (String key : pContentItem.keySet()) {
				// note: not all objects returned are strings e.g. AuditInfo
				if (pContentItem.get(key) instanceof String &&  ((String)pContentItem.get(key)).isEmpty()) { 
						pContentItem.remove(key);
				}	
			}
			
			if (mInitializer == null){
				pContentItem=new ColorConfig(pContentItem);
			}else{
		        pContentItem=mInitializer.initialize(pContentItem);
			} 
			
			return pContentItem;
	}

	/**
	 * Returns the merged configuration and information about the color options
	 * available to the site visitor.
	 */
	@Override
	public ContentItem process(ContentItem pContentItem)
			throws CartridgeHandlerException {
		
		// Append the color options from the Assembler defaults
		int numColors = mColorOptions.size();
		ArrayList<ContentItem> colors = new ArrayList<ContentItem>(numColors);
		if (mColorOptions != null && !mColorOptions.isEmpty()) {
			for (String key : mColorOptions.keySet()) {
				ContentItem color = new BasicContentItem("colorOption");
				color.put("label", key);
				color.put("hexCode", mColorOptions.get(key));
				colors.add(color);
			}
			pContentItem.put("colorOptions", colors);
		}
		
		// Append relevant navigation and action properties
		String navPathValue = "";
		String siteRootPathValue = "";
		String fullNavStateValue = "";
		
		fullNavStateValue = getNavigationState().toString();
		navPathValue = getActionPathProvider().getDefaultNavigationActionContentPath();
		siteRootPathValue = getActionPathProvider().getDefaultNavigationActionSiteRootPath();
		
		pContentItem.put("fullNavState", fullNavStateValue);
		pContentItem.put("navActionPath", navPathValue);
		pContentItem.put("siteActionRootPath", siteRootPathValue);
		
		return pContentItem;
	}

}
