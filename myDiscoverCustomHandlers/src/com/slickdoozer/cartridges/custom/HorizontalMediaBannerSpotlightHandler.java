package com.slickdoozer.cartridges.custom;

import java.util.List;

import com.endeca.infront.assembler.AbstractCartridgeHandler;
import com.endeca.infront.assembler.CartridgeHandlerException;
import com.endeca.infront.assembler.ContentItem;
import com.endeca.infront.cartridge.MediaBanner;
import com.endeca.infront.cartridge.MediaBannerConfig;
import com.endeca.infront.cartridge.MediaBannerHandler;
import com.endeca.infront.cartridge.model.LinkBuilder;
import com.endeca.infront.cartridge.model.MediaObject;
import com.endeca.infront.cartridge.model.MediaSourceConfig;
import com.endeca.infront.content.source.ContentSource;
import com.endeca.infront.navigation.NavigationStateBuilder;

public class HorizontalMediaBannerSpotlightHandler extends AbstractCartridgeHandler {
	private List<MediaSourceConfig> mediaSourceList;
    private NavigationStateBuilder navigationStateBuilder;
    private ContentSource contentSource;
    private static final String MEDIA_OBJECT_1 = "media1";
    private static final String MEDIA_OBJECT_2 = "media2";
    private static final String MEDIA_OBJECT_3 = "media3";
    private static final String LINK_BUILDER_LINK_1 = "link1";
    private static final String LINK_BUILDER_LINK_2 = "link2";
    private static final String LINK_BUILDER_LINK_3 = "link3";
    
	@Override
	public ContentItem process(ContentItem config) throws CartridgeHandlerException {
		
		// Each media/link pair specified in the HorizontalMediaBannerSpotlight cartridge
		// is modeled after an out-of-box MediaBanner cartridge, so we are going to leverage
		// existing MediaBanner cartridge processing functionality to help provide final
		// content for the HorizontalMediaBannerSpotlight cartridge.
		if (config.containsKey(MEDIA_OBJECT_1)) {
			replaceContentFromMediaBanner(config, MEDIA_OBJECT_1, LINK_BUILDER_LINK_1);
		}
		if (config.containsKey(MEDIA_OBJECT_2)) {
			replaceContentFromMediaBanner(config, MEDIA_OBJECT_2, LINK_BUILDER_LINK_2);
		}
		if (config.containsKey(MEDIA_OBJECT_3)) {
			replaceContentFromMediaBanner(config, MEDIA_OBJECT_3, LINK_BUILDER_LINK_3);
		}
				
		return config;
	}
	
	private void replaceContentFromMediaBanner(ContentItem config, String media, String link) throws CartridgeHandlerException {
		
		// Retrieve objects from the existing configuration
		MediaObject mediaObject = (MediaObject)config.get(media);
		LinkBuilder linkBuilder = (LinkBuilder)config.get(link);
		
		// Create a MediaBannerConfig object using existing configuration
		MediaBannerConfig mediaBannerConfig = new MediaBannerConfig();
		mediaBannerConfig.setMedia(mediaObject);
		mediaBannerConfig.setLink(linkBuilder);
		
		// Create a MediaBannerHandler object using new configuration
		MediaBannerHandler mediaBannerHandler = new MediaBannerHandler();
		mediaBannerHandler.setMediaSourceList(this.mediaSourceList);
		mediaBannerHandler.setNavigationStateBuilder(this.navigationStateBuilder);
		mediaBannerHandler.setContentSource(this.contentSource);
		
		// Validate new configuration
		mediaBannerHandler.preprocess(mediaBannerConfig);
		
		// Generate content using new configuration
		MediaBanner mediaBanner = mediaBannerHandler.process(mediaBannerConfig);
		
		// Replace existing configuration with new MediaBanner configuration
		config.replace(media, mediaBanner.getMedia());
		config.replace(link, mediaBanner.getLink());
		
	}
	
	public NavigationStateBuilder getNavigationStateBuilder() {
        return this.navigationStateBuilder;
    }

    public void setNavigationStateBuilder(NavigationStateBuilder navigationStateBuilder) {
        this.navigationStateBuilder = navigationStateBuilder;
    }

    public List<MediaSourceConfig> getMediaSourceList() {
        return this.mediaSourceList;
    }

    public void setMediaSourceList(List<MediaSourceConfig> mediaSrcList) {
        this.mediaSourceList = mediaSrcList;
    }

    public ContentSource getContentSource() {
        return this.contentSource;
    }

    public void setContentSource(ContentSource contentSource) {
        this.contentSource = contentSource;
    }

}
