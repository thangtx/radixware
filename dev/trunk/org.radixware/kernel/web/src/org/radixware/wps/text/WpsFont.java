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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextDecoration;
import org.radixware.kernel.common.client.text.Font;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.ICssStyledItem;



public class WpsFont extends Font{
    
    private final EnumSet<ECssTextOptionsStyle> EMPTY_STYLES = EnumSet.noneOf(ECssTextOptionsStyle.class);
    
    private final EnumSet<ECssTextOptionsStyle> cssStyles;
    
    public final static class Factory{
                
        private final static Map<String,WpsFont> CACHE = new HashMap<>();
        private final static Object SEMAPHORE = new Object();
        private final static String DEFAULT_FONT_FAMILY = "arial";
        private final static WpsFont DEFAULT_UI_FONT = 
                getFont(DEFAULT_FONT_FAMILY,12,EFontWeight.NORMAL,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.DEFAULT_UI));
        private final static Map<ECssTextOptionsStyle,WpsFont> PREDEFINED_FONTS = new HashMap<>();
        private final static Object PREDEFINED_FONTS_SEMAPHORE = new Object();
        
        
        private static void fillPredefined(){//изменять вместе с defaults.css
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.DEFAULT_LABEL, getFont(DEFAULT_FONT_FAMILY,10,EFontWeight.NORMAL,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.DEFAULT_LABEL)));
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.REGULAR_VALUE, getFont(DEFAULT_FONT_FAMILY,12,EFontWeight.NORMAL,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.REGULAR_VALUE)));
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.REGULAR_TITLE, getFont(DEFAULT_FONT_FAMILY,11,EFontWeight.NORMAL,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.REGULAR_TITLE)));
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.REQUIRED_VALUE_TITLE, getFont(DEFAULT_FONT_FAMILY,11,EFontWeight.BOLD,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.REGULAR_TITLE, ECssTextOptionsStyle.REQUIRED_VALUE_TITLE)));
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.INHERITED_VALUE, getFont(DEFAULT_FONT_FAMILY,12,EFontWeight.NORMAL,EFontStyle.ITALIC,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.REGULAR_VALUE, ECssTextOptionsStyle.INHERITED_VALUE)));
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.INVALID_VALUE, getFont(DEFAULT_FONT_FAMILY,12,EFontWeight.BOLD,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.REGULAR_VALUE, ECssTextOptionsStyle.INVALID_VALUE)));
            PREDEFINED_FONTS.put(ECssTextOptionsStyle.INVALID_VALUE_TITLE, getFont(DEFAULT_FONT_FAMILY,11,EFontWeight.BOLD,EFontStyle.NORMAL,EnumSet.noneOf(ETextDecoration.class), EnumSet.of(ECssTextOptionsStyle.REGULAR_TITLE, ECssTextOptionsStyle.INVALID_VALUE_TITLE)));
        }
        
        private Factory(){
        }
        
        public static WpsFont getDefault(){
            return DEFAULT_UI_FONT;
        }
                
        public static WpsFont getFont(final String family, final int pointSize, final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration){
            return Factory.getFont(family, pointSize, weight, style, decoration, null);
        }
        
        static WpsFont getFont(final WpsFont font){
            if (font==null){
                return null;
            }else if (font.cssStyles==null || font.cssStyles.isEmpty()){
                return font;
            }else{                
                return Factory.getFont(font.getFamily(), font.getSize(), font.getWeight(), font.getStyle(), font.getDecoration(), null);
            }
        }
        
        static WpsFont getCssStyleFont(final ECssTextOptionsStyle style){
            synchronized(PREDEFINED_FONTS_SEMAPHORE){
                if (PREDEFINED_FONTS.isEmpty()){
                    fillPredefined();
                }
            }
            final WpsFont font = PREDEFINED_FONTS.get(style);
            return font==null ? PREDEFINED_FONTS.get(ECssTextOptionsStyle.REGULAR_VALUE) : font;
        }
        
        static WpsFont getFont(final String family, final int pointSize, final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration, final EnumSet<ECssTextOptionsStyle> styles){
            final String finalFamily;
            final int finalPointSize;
            final EFontWeight finalFontWeight;
            final EFontStyle finalFontStyle;
            final EnumSet<ETextDecoration> finalDecoration;
            if (family==null || pointSize<0 || weight==null || style==null || decoration==null){
                final WpsFont defaultFont = getDefault();
                finalFamily = family==null || family.isEmpty() ? defaultFont.getFamily() : family;
                finalPointSize = pointSize<0 ? defaultFont.getSize() : pointSize;
                finalFontWeight = weight==null ? defaultFont.getWeight() : weight;
                finalFontStyle = style==null ? defaultFont.getStyle() : style;
                finalDecoration = decoration==null ? defaultFont.getDecoration() : decoration;
            }else{
                finalFamily = family;
                finalPointSize = pointSize;
                finalFontWeight = weight;
                finalFontStyle = style;
                finalDecoration = decoration;
            }
            final Font.Attributes attributes = 
                new Font.Attributes(finalFamily, finalPointSize, finalFontWeight, finalFontStyle, finalDecoration);            
            if (styles==null || styles.isEmpty()){
                final String key = attributes.toString();
                synchronized(SEMAPHORE){
                    WpsFont font = CACHE.get(key);
                    if (font==null){
                        font = new WpsFont(attributes,styles);
                        CACHE.put(key, font);
                    }
                    return font;
                }
            }else{
                return new WpsFont(attributes,styles);
            }
        }        
        
        public static WpsFont getFont(final String family, final int pointSize, final EFontWeight weight, final EFontStyle style) {
            return getFont(family, pointSize, weight, style, null);
        }

        public static WpsFont getFont(final  String family, final int pointSize, final EFontWeight weight) {
            return getFont(family, pointSize, weight, null, null);
        }

        public static WpsFont getFont(final String family, final int pointSize) {
            return getFont(family, pointSize, null, null, null);
        }

        public static WpsFont getFont(final String family) {
            return getFont(family, -1, null, null, null);
        }

        public static WpsFont getFont(final int pointSize, final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
            return getFont(null, pointSize, weight, style, decoration);
        }
        
        public static WpsFont getFont(final int pointSize, final EFontWeight weight, final EFontStyle style) {
            return getFont(null, pointSize, weight, style, null);
        }
        
        public static WpsFont getFont(final int pointSize, final EFontWeight weight) {
            return getFont(null, pointSize, weight, null, null);
        }
        
        public static WpsFont getFont(final int pointSize) {
            return getFont(null, pointSize, null, null, null);
        }
        
        public static WpsFont getFont(final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
            return getFont(null, -1, weight, style, decoration);
        }
        
        public static WpsFont getFont(final EFontWeight weight, final EFontStyle style) {
            return getFont(null, -1, weight, style, null);
        }
        
        public static WpsFont getFont(final EFontWeight weight) {
            return getFont(null, -1, weight, null, null);
        }
        
        public static WpsFont getFont(final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
            return getFont(null, -1, null, style, decoration);
        }
        
        public static WpsFont getFont(final EFontStyle style) {
            return getFont(null, -1, null, style, null);
        }
        
        public static WpsFont fromStr(final String fontAsStr){
            if (fontAsStr==null || fontAsStr.isEmpty()){
                return getDefault();
            }
            synchronized(SEMAPHORE){
                final WpsFont font = CACHE.get(fontAsStr);
                if (font!=null){
                    return font;
                }
            }
            final Font.Attributes attributes = Font.Attributes.fromString(fontAsStr);
            if (attributes.fullDefined()){
                final WpsFont font = new WpsFont(attributes,null);
                synchronized(SEMAPHORE){
                    if (!CACHE.containsKey(attributes.toString())){
                        CACHE.put(attributes.toString(), font);
                    }
                }
                return font;
            }
            return getFont(attributes.family,attributes.pointSize,attributes.weigth,attributes.style,attributes.decoration);
        }
        
        public static WpsFont fromHtml(final ICssStyledItem html){
            final ECssTextOptionsStyle style = ECssTextOptionsStyle.findActualStyle(html.getClasses());
            WpsFont font = style==null ? null : WpsFont.Factory.getCssStyleFont(style);
            String family = null;
            int size = -1;
            EFontWeight weight = null;
            EFontStyle  fontStyle = null;
            EnumSet<ETextDecoration> decoration = null;
            String value;
            for (ECssPropertyName propName: ECssPropertyName.FONT_PROPERTIES){
                value = html.getCss(propName.getPropertyName());
                if (value!=null && !value.isEmpty()){
                    switch(propName){
                        case FONT_FAMILY:
                            family = value;
                            break;
                        case FONT_SIZE:{
                            if (value.endsWith("px")){
                                try{
                                    size = Integer.parseInt(value.substring(0, value.length()-2));
                                }catch(NumberFormatException exception){
                                    throw new IllegalArgumentException("Failed to parse font size from string \'"+value+"\'",exception);
                                }                                
                            }
                            break;
                        }
                        case FONT_WEIGHT:{
                            weight  = EFontWeight.forCssValue(value);
                            break;
                        }
                        case FONT_STYLE:{
                            fontStyle = EFontStyle.valueOf(value);
                            break;
                        }
                        case TEXT_DECORATION:{
                            decoration = EnumSet.of(ETextDecoration.valueOf(value));                            
                        }
                    }
                }
            }
            if (family==null && size<0 && weight==null && fontStyle==null && decoration==null){
                return font;
            }else if (font==null){
                return getFont(family, size, weight, fontStyle, decoration);
            }else{
                if (family==null){
                    family = font.getFamily();
                }
                if (size<0){
                    size = font.getSize();
                }
                if (weight==null){
                    weight = font.getWeight();
                }
                if (fontStyle==null){
                    fontStyle = font.getStyle();
                }
                if (decoration==null){
                    decoration = font.getDecoration();
                }
                return getFont(family, size, weight, fontStyle, decoration);
            }
        }
    }
        
    WpsFont(final String family, final int size, final EFontWeight weigth, final EFontStyle style, final EnumSet<ETextDecoration> decoration){
        super(family,size,weigth,style,decoration);
        cssStyles = null;
    }
    
    private WpsFont(final Font.Attributes attributes, final EnumSet<ECssTextOptionsStyle> cssStyles){
        super(attributes);
        if (cssStyles!=null && !cssStyles.isEmpty()){
            this.cssStyles = cssStyles.clone();
        }else{
            this.cssStyles = null;
        }        
    }
        

    @Override
    public WpsFont changeFamily(String newFamily) {
        return Factory.getFont(newFamily, getSize(), getWeight(), getStyle(), getDecoration());
    }

    @Override
    public WpsFont changeSize(int size) {
        return Factory.getFont(getFamily(), size, getWeight(), getStyle(), getDecoration());
    }

    @Override
    public WpsFont changeWeight(EFontWeight weight) {
        return Factory.getFont(getFamily(), getSize(), weight, getStyle(), getDecoration());
    }

    @Override
    public WpsFont changeStyle(EFontStyle style) {
        return Factory.getFont(getFamily(), getSize(), getWeight(), style, getDecoration());
    }

    @Override
    public WpsFont changeDecoration(EnumSet<ETextDecoration> decoration) {
        return Factory.getFont(getFamily(), getSize(), getWeight(), getStyle(), decoration);
    }

    @Override
    public WpsFont increaseSize(int size) {
        return Factory.getFont(getFamily(), getSize()+size, getWeight(), getStyle(), getDecoration());
    }

    @Override
    public WpsFont decreaseSize(int size) {
        return Factory.getFont(getFamily(), getSize()-size, getWeight(), getStyle(), getDecoration());
    }

    @Override
    public WpsFont getBold() {
        return Factory.getFont(getFamily(), getSize(), EFontWeight.BOLD, getStyle(), getDecoration());
    }

    @Override
    public WpsFont getItalic() {
        return Factory.getFont(getFamily(), getSize(), getWeight(), EFontStyle.ITALIC, getDecoration());
    }
    
    public Map<ECssPropertyName,String> getCssPropertyValues(){
        final Map<ECssPropertyName,String> map = new HashMap<>();
        if (getFamily()!=null){
            map.put(ECssPropertyName.FONT_FAMILY, getFamily());
        }
        if (getSize()>-1){
            map.put(ECssPropertyName.FONT_SIZE, String.valueOf(getSize())+"px");
        }
        if (getWeight()!=null){
            map.put(ECssPropertyName.FONT_WEIGHT, getWeight().getCssPropertyValue());
        }
        if (getStyle()!=null){
            map.put(ECssPropertyName.FONT_STYLE, getStyle().getCssPropertyValue());
        }        
        if (getDecoration()!=null){
            if (getDecoration().contains(ETextDecoration.UNDERLINE)){
                map.put(ECssPropertyName.TEXT_DECORATION, ETextDecoration.UNDERLINE.getCssPropertyValue());
            }else if (getDecoration().contains(ETextDecoration.STRIKE_OUT)){
                map.put(ECssPropertyName.TEXT_DECORATION, ETextDecoration.STRIKE_OUT.getCssPropertyValue());
            }
        }
        return map;
    }
    
    public EnumSet<ECssTextOptionsStyle> getPredefinedCssStyles(){
        return cssStyles==null ? EMPTY_STYLES : cssStyles.clone();
    }    
    
    public void applyTo(final ICssStyledItem html, final boolean applySize){
        for (ECssPropertyName property: ECssPropertyName.FONT_PROPERTIES){
            if (property!=ECssPropertyName.FONT_SIZE || applySize){
                html.setCss(property.getPropertyName(), null);
            }
        }
        if (getPredefinedCssStyles().isEmpty()){
            final Map<ECssPropertyName, String> cssProperties = getCssPropertyValues();
            for(Map.Entry<ECssPropertyName, String> entry: cssProperties.entrySet()){
                if (entry.getKey()!=ECssPropertyName.FONT_SIZE || applySize){
                    html.setCss(entry.getKey().getPropertyName(), entry.getValue());
                }
            }            
        }else{
            for (ECssTextOptionsStyle style: getPredefinedCssStyles()){
               html.addClass(style.getStyleName());
            }            
        }
    }        
}