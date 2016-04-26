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

package org.radixware.kernel.explorer.text;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextDecoration;
import org.radixware.kernel.common.client.text.Font;
import org.radixware.kernel.explorer.env.ExplorerSettings;



public final class ExplorerFont extends Font{
    
    private QFont qfont;
    private QFontMetrics qFontMetrics;
    
    public final static class Factory{
        
        private final static Map<String,ExplorerFont> CACHE = new HashMap<>();
        
        private Factory(){
        }
        
        public static ExplorerFont getDefault(){            
            return getFont(QApplication.font());
        }
        
        public static ExplorerFont getFont(final QFont qfont){
            final ExplorerFont font = getFont(qfont.family(),
                                                qfont.pointSize(),
                                                EFontWeight.forValue(qfont.weight()),
                                                Factory.getFontStyle(qfont),
                                                Factory.getTextDecoration(qfont));
            //font.qfont must be created explicitly to init QFont resolve_mask
            return font;
        }
        
        public static ExplorerFont getFont(final String family, final int pointSize, final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration){
            final String finalFamily;
            final int finalPointSize;
            final EFontWeight finalFontWeight;
            final EFontStyle finalFontStyle;
            final EnumSet<ETextDecoration> finalDecoration;
            if (family==null || pointSize<0 || weight==null || style==null || decoration==null){
                final ExplorerFont defaultFont = getDefault();
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
            final String key = attributes.toString();
            ExplorerFont font = CACHE.get(key);
            if (font==null){
                font = new ExplorerFont(attributes);
                CACHE.put(key, font);
            }
            return font;
        }
        
        public static ExplorerFont getFont(final String family, final int pointSize, final EFontWeight weight, final EFontStyle style) {
            return getFont(family, pointSize, weight, style, null);
        }

        public static ExplorerFont getFont(final  String family, final int pointSize, final EFontWeight weight) {
            return getFont(family, pointSize, weight, null, null);
        }

        public static ExplorerFont getFont(final String family, final int pointSize) {
            return getFont(family, pointSize, null, null, null);
        }

        public static ExplorerFont getFont(final String family) {
            return getFont(family, -1, null, null, null);
        }

        public static ExplorerFont getFont(final int pointSize, final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
            return getFont(null, pointSize, weight, style, decoration);
        }
        
        public static ExplorerFont getFont(final int pointSize, final EFontWeight weight, final EFontStyle style) {
            return getFont(null, pointSize, weight, style, null);
        }
        
        public static ExplorerFont getFont(final int pointSize, final EFontWeight weight) {
            return getFont(null, pointSize, weight, null, null);
        }
        
        public static ExplorerFont getFont(final int pointSize) {
            return getFont(null, pointSize, null, null, null);
        }
        
        public static ExplorerFont getFont(final EFontWeight weight, final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
            return getFont(null, -1, weight, style, decoration);
        }
        
        public static ExplorerFont getFont(final EFontWeight weight, final EFontStyle style) {
            return getFont(null, -1, weight, style, null);
        }
        
        public static ExplorerFont getFont(final EFontWeight weight) {
            return getFont(null, -1, weight, null, null);
        }
        
        public static ExplorerFont getFont(final EFontStyle style, final EnumSet<ETextDecoration> decoration) {
            return getFont(null, -1, null, style, decoration);
        }
        
        public static ExplorerFont getFont(final EFontStyle style) {
            return getFont(null, -1, null, style, null);
        }
                
        static EFontStyle getFontStyle(final QFont font){
            switch(font.style()){
                case StyleOblique:
                    return EFontStyle.OBLIQUE;
                case StyleItalic:
                    return EFontStyle.ITALIC;
                default:
                    return EFontStyle.NORMAL;
            }
        }
        
        static EnumSet<ETextDecoration> getTextDecoration(final QFont font){
            final EnumSet<ETextDecoration> decoration = EnumSet.noneOf(ETextDecoration.class);
            if (font.underline()){
                decoration.add(ETextDecoration.UNDERLINE);
            }
            if (font.strikeOut()){
                decoration.add(ETextDecoration.STRIKE_OUT);
            }
            return decoration;
        }
        
        public static ExplorerFont fromStr(final String fontAsStr){
            if (fontAsStr==null || fontAsStr.isEmpty()){
                return getDefault();
            }
            {
                final ExplorerFont font = CACHE.get(fontAsStr);
                if (font!=null){
                    return font;
                }
            }
            final Font.Attributes attributes = Font.Attributes.fromString(fontAsStr);
            if (attributes.fullDefined()){
                final ExplorerFont font = new ExplorerFont(attributes);
                CACHE.put(attributes.toString(), font);
                return font;
            }
            return getFont(attributes.family,attributes.pointSize,attributes.weigth,attributes.style,attributes.decoration);
        }
        
        
    }
        
    ExplorerFont(final QFont font){
        this(font.family(),
             font.pointSize(),
             EFontWeight.forValue(font.pointSize()),
             Factory.getFontStyle(font),
             Factory.getTextDecoration(font));
    }
        
    ExplorerFont(final String family, final int size, final EFontWeight weigth, final EFontStyle style, final EnumSet<ETextDecoration> decoration){
        super(family,size,weigth,style,decoration);
    }
    
    private ExplorerFont(final Font.Attributes attributes){
        super(attributes);
    }
        

    @Override
    public ExplorerFont changeFamily(String newFamily) {
        return Factory.getFont(newFamily, getSize(), getWeight(), getStyle(), getDecoration());
    }

    @Override
    public ExplorerFont changeSize(int size) {
        return Factory.getFont(getFamily(), size, getWeight(), getStyle(), getDecoration());
    }

    @Override
    public ExplorerFont changeWeight(EFontWeight weight) {
        return Factory.getFont(getFamily(), getSize(), weight, getStyle(), getDecoration());
    }

    @Override
    public ExplorerFont changeStyle(EFontStyle style) {
        return Factory.getFont(getFamily(), getSize(), getWeight(), style, getDecoration());
    }

    @Override
    public ExplorerFont changeDecoration(EnumSet<ETextDecoration> decoration) {
        return Factory.getFont(getFamily(), getSize(), getWeight(), getStyle(), decoration);
    }

    @Override
    public ExplorerFont increaseSize(int size) {
        return Factory.getFont(getFamily(), getSize()+size, getWeight(), getStyle(), getDecoration());
    }

    @Override
    public ExplorerFont decreaseSize(int size) {
        return Factory.getFont(getFamily(), getSize()-size, getWeight(), getStyle(), getDecoration());
    }

    @Override
    public ExplorerFont getBold() {
        return Factory.getFont(getFamily(), getSize(), EFontWeight.BOLD, getStyle(), getDecoration());
    }

    @Override
    public ExplorerFont getItalic() {
        return Factory.getFont(getFamily(), getSize(), getWeight(), EFontStyle.ITALIC, getDecoration());
    }
    
    public QFont getQFont(){
        if (qfont==null){
            qfont = ExplorerSettings.getQFont(asStr());
        }
        return qfont;
    }
    
    public QFontMetrics getQFontMetrics(){
        if (qFontMetrics==null){
            qFontMetrics = new QFontMetrics(getQFont());
        }
        return qFontMetrics;
    }
    
}
