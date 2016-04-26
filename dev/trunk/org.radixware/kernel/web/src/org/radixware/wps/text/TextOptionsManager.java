/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.wps.text;

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.text.IFont;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.TextOptions;


public class TextOptionsManager extends org.radixware.kernel.common.client.text.TextOptionsManager{
    
    private final static TextOptionsManager INSTANCE = new TextOptionsManager();
    private final static Character PWD_CHAR = Character.valueOf('*');
    
    private final Map<String,WpsTextOptions> cache = new HashMap<>();
    private final Map<String,WpsTextOptions> styledOptionsCache = new HashMap<>();
    private final Map<ECssTextOptionsStyle,WpsTextOptions> predefinedOptions = new HashMap<>();
    
    private final Object semaphore = new Object();
    
    private TextOptionsManager(){
        super(WpsFontManager.getInstance());
        final WpsFontManager fntMgr = WpsFontManager.getInstance();
        predefinedOptions.put(ECssTextOptionsStyle.REGULAR_VALUE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.REGULAR_VALUE), fntMgr.getFontForCssStyle(ECssTextOptionsStyle.REGULAR_VALUE), null, Color.BLACK, Color.WHITE));
        predefinedOptions.put(ECssTextOptionsStyle.REGULAR_TITLE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.REGULAR_TITLE), fntMgr.getFontForCssStyle(ECssTextOptionsStyle.REGULAR_TITLE), null, Color.BLACK, null));
        predefinedOptions.put(ECssTextOptionsStyle.REQUIRED_VALUE_TITLE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.REGULAR_TITLE,ECssTextOptionsStyle.REQUIRED_VALUE_TITLE), fntMgr.getFontForCssStyle(ECssTextOptionsStyle.REQUIRED_VALUE_TITLE), null, Color.BLACK, null));
        predefinedOptions.put(ECssTextOptionsStyle.READONLY_VALUE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.REGULAR_VALUE,ECssTextOptionsStyle.READONLY_VALUE), fntMgr.getFontForCssStyle(ECssTextOptionsStyle.READONLY_VALUE), null, Color.BLACK, Color.decode("#EEEEFF")));
        predefinedOptions.put(ECssTextOptionsStyle.INHERITED_VALUE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.INHERITED_VALUE), fntMgr.getFontForCssStyle(ECssTextOptionsStyle.INHERITED_VALUE), null, null, null));
        predefinedOptions.put(ECssTextOptionsStyle.OVERRIDED_VALUE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.OVERRIDED_VALUE), null, null, Color.decode("#1C94C4"), null));
        predefinedOptions.put(ECssTextOptionsStyle.INVALID_VALUE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.INVALID_VALUE), null, null, Color.decode("#FF0000"), null));
        predefinedOptions.put(ECssTextOptionsStyle.UNDEFINED_VALUE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.UNDEFINED_VALUE), null, null, Color.decode("#b3b2b1"), null));
        predefinedOptions.put(ECssTextOptionsStyle.INVALID_VALUE_TITLE, new WpsTextOptions(EnumSet.of(ECssTextOptionsStyle.INVALID_VALUE_TITLE), fntMgr.getFontForCssStyle(ECssTextOptionsStyle.INVALID_VALUE_TITLE), null, Color.decode("#FF0000"), null));
    }
    
    @Override
    public WpsTextOptions getOptions(final IFont font, final ETextAlignment alignment, final Color foreground, final Color background) {
        return getOptions(font, alignment, foreground, background, null);
    }
    
    WpsTextOptions getOptionsForStyle(final ECssTextOptionsStyle style){
        final WpsTextOptions options = predefinedOptions.get(style);
        return options==null ? predefinedOptions.get(ECssTextOptionsStyle.REGULAR_VALUE) : options;
    }

    WpsTextOptions getOptions(final IFont font, 
                              final ETextAlignment alignment, 
                              final Color foreground, 
                              final Color background, 
                              final EnumSet<ECssTextOptionsStyle> cssStyles) {
        if (cssStyles==null || cssStyles.isEmpty()){
            final StringBuilder keyBuilder = 
                    new StringBuilder(TextOptions.getTextOptionsAsStr(font, alignment, foreground, background));
            final EnumSet<ECssTextOptionsStyle> fontStyles;
            if (font==null){
                fontStyles = EnumSet.noneOf(ECssTextOptionsStyle.class);
            }else{
                fontStyles = ((WpsFont)font).getPredefinedCssStyles();
            }
            boolean firstStyle=true;
            for (ECssTextOptionsStyle style: fontStyles){
                if (firstStyle){
                    keyBuilder.append(';');
                    firstStyle = false;
                }else{
                    keyBuilder.append(',');
                }
                keyBuilder.append(style.getStyleName());
            }
            final String key = keyBuilder.toString();
            synchronized(semaphore){
                WpsTextOptions options = cache.get(key);
                if (options==null){
                    options = 
                        new WpsTextOptions(cssStyles, (WpsFont)font, alignment, foreground, background);
                    cache.put(key, options);
                }
                return options;
            }
        }else{
            final StringBuilder key = new StringBuilder();
            for (ECssTextOptionsStyle style: cssStyles){
                if (key.length()==0){
                    key.append(',');                    
                }
                key.append(style.name());
            }
            WpsTextOptions options = styledOptionsCache.get(key.toString());
            if (options==null){
                options = new WpsTextOptions(cssStyles, (WpsFont)font, alignment, foreground, background);
                styledOptionsCache.put(key.toString(), options);
            }
            return options;
        }
    }
    
    @Override
    public WpsTextOptions merge(final ITextOptions initial, final ITextOptions addon) {
        if (addon==null || isEmptyOptions(addon)){
            return (WpsTextOptions)initial;
        }
        if (initial==null || isEmptyOptions(initial) || initial==addon){
            return (WpsTextOptions)addon;
        }
        final boolean wholeOverride = 
                (addon.getFont()!=null || initial.getFont()==null) &&
                (addon.getAlignment()!=null || initial.getAlignment()==null) &&
                (addon.getForegroundColor()!=null || initial.getForegroundColor()==null) &&
                (addon.getBackgroundColor()!=null || initial.getBackgroundColor()==null) &&
                (((WpsTextOptions)addon).getPredefinedCssStyles().isEmpty() || ((WpsTextOptions)initial).getPredefinedCssStyles().isEmpty());
        if (wholeOverride){
            return (WpsTextOptions)addon;
        }
        final IFont font = 
               addon.getFont()==null ? initial.getFont() : addon.getFont();
        final ETextAlignment alignment = 
            addon.getAlignment()==null ? initial.getAlignment() : addon.getAlignment();
        final Color fg = 
            addon.getForegroundColor()==null ? initial.getForegroundColor() : addon.getForegroundColor();
        final Color bg = 
            addon.getBackgroundColor()==null ? initial.getBackgroundColor() : addon.getBackgroundColor();
        final EnumSet<ECssTextOptionsStyle> styles;
        if (!((WpsTextOptions)addon).getPredefinedCssStyles().isEmpty() && !((WpsTextOptions)initial).getPredefinedCssStyles().isEmpty()){
            styles = ((WpsTextOptions)initial).getPredefinedCssStyles();
            final EnumSet<ECssTextOptionsStyle> newStyles = ((WpsTextOptions)addon).getPredefinedCssStyles();
            for (ECssTextOptionsStyle style: newStyles){
                if (!styles.contains(style)){
                    for (ECssTextOptionsStyle oldStyle: styles){
                        if (oldStyle.ordinal()>style.ordinal()){
                            //new style cant override some existing style
                            return getOptions(font, alignment, fg, bg, null);
                        }
                    }
                }
            }
            styles.addAll( newStyles );
        }else{
            styles = null;
        }
        return getOptions(font, alignment, fg, bg, styles);
    }

    @Override
    public Character getPasswordCharacter() {
        return PWD_CHAR;
    }        
    
    public static TextOptionsManager getInstance(){
        return INSTANCE;
    }
    
    
}