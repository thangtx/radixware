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

package org.radixware.kernel.common.client.text;

import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextDecoration;


public abstract class Font implements IFont{
    
    protected final static class Attributes{
        
        public final String family;
        public final int pointSize;
        public final EFontWeight weigth;
        public final EFontStyle style;
        public final EnumSet<ETextDecoration> decoration;        

        public Attributes(final String family, final int size, final EFontWeight weigth, final EFontStyle style, final EnumSet<ETextDecoration> decoration){
            this.family = family;
            this.pointSize = size;
            this.weigth = weigth;
            this.style = style;
            this.decoration = decoration==null ? EnumSet.noneOf(ETextDecoration.class) : decoration.clone();            
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.family);
            hash = 59 * hash + this.pointSize;
            hash = 59 * hash + Objects.hashCode(this.weigth);
            hash = 59 * hash + Objects.hashCode(this.style);
            hash = 59 * hash + Objects.hashCode(this.decoration);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Attributes other = (Attributes) obj;
            if (!Objects.equals(this.family, other.family)) {
                return false;
            }
            if (this.pointSize != other.pointSize) {
                return false;
            }
            if (this.weigth != other.weigth) {
                return false;
            }
            if (this.style != other.style) {
                return false;
            }
            if (!Objects.equals(this.decoration, other.decoration)) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString(){
            final StringBuilder stringBuilder = new StringBuilder();
            if (family!=null){
                stringBuilder.append(family);
            }
            stringBuilder.append(',');
            if (pointSize>=0){
                stringBuilder.append(String.valueOf(pointSize));
            }
            stringBuilder.append(",-1,5,");//pixelSize and styleHint
            if (weigth!=null){
                stringBuilder.append(String.valueOf(weigth.getQtValue()));
            }
            stringBuilder.append(',');
            if (style!=null){
                stringBuilder.append(String.valueOf(style.ordinal()));
            }
            stringBuilder.append(',');
            stringBuilder.append(decoration.contains(ETextDecoration.UNDERLINE) ? '1' : '0');
            stringBuilder.append(',');
            stringBuilder.append(decoration.contains(ETextDecoration.STRIKE_OUT) ? '1' : '0');
            stringBuilder.append(",0,0");//fixedPitch, rawMode
            return stringBuilder.toString();               
        }
        
        public static Attributes fromString(final String fontAsStr){
            String family = null;
            int pointSize = -1;
            EFontWeight fontWeigth = null;
            EFontStyle fontStyle = null;
            EnumSet<ETextDecoration> decoration = null;
            final String[] parts = fontAsStr.split(",");
            if (parts.length>0 && !parts[0].isEmpty()){
                family = parts[0];
            }
            if (parts.length>1 && !parts[1].isEmpty()){
                try{
                    pointSize = Integer.parseInt(parts[1]);
                }catch(NumberFormatException exception){
                    throw new IllegalArgumentException("Failed to parse font point size from string \'"+fontAsStr+"\'",exception);
                }
            }
            if (parts.length>4 && !parts[4].isEmpty()){
                try{
                    final int weigth = Integer.parseInt(parts[4]);
                    if (weigth>=0){
                        fontWeigth = EFontWeight.forValue(weigth);
                    }
                }catch(IllegalArgumentException exception){
                    throw new IllegalArgumentException("Failed to parse font weigth from string \'"+fontAsStr+"\'",exception);
                }
            }
            if (parts.length>5 && !parts[5].isEmpty()){
                try{
                    final int style = Integer.parseInt(parts[5]);
                    fontStyle = EFontStyle.forIntValue(style);
                }catch(IllegalArgumentException exception){
                    throw new IllegalArgumentException("Failed to parse font style from string \'"+fontAsStr+"\'",exception);
                }
            }
            if (parts.length>6 && !parts[6].isEmpty()){
                try{
                    final int underline = Integer.parseInt(parts[6]);
                    if (underline!=0){
                        decoration = EnumSet.of(ETextDecoration.UNDERLINE);
                    }
                }catch(IllegalArgumentException exception){
                    throw new IllegalArgumentException("Failed to parse text decoration from string \'"+fontAsStr+"\'",exception);
                }
            }
            if (parts.length>7 && !parts[7].isEmpty()){
                try{
                    final int strikeOut = Integer.parseInt(parts[7]);
                    if (strikeOut!=0){
                        if (decoration==null){
                            decoration = EnumSet.of(ETextDecoration.STRIKE_OUT);                                
                        }else{
                            decoration.add(ETextDecoration.STRIKE_OUT);
                        }
                    }
                }catch(IllegalArgumentException exception){
                    throw new IllegalArgumentException("Failed to parse text decoration from string \'"+fontAsStr+"\'",exception);
                }
            }
            return new Attributes(family, pointSize, fontWeigth, fontStyle, decoration);
        }
        
        public boolean fullDefined(){
            return family!=null && pointSize>-1 && weigth!=null && style!=null && decoration!=null;
        }
    }
    
    private final Attributes attributes;
        
    protected Font(final String family, final int size, final EFontWeight weigth, final EFontStyle style, final EnumSet<ETextDecoration> decoration){
        attributes = new Attributes(family, size, weigth, style, decoration);
    }
    
    protected Font(Attributes attributes){
        this.attributes = attributes;
    }

    @Override
    public String getFamily() {
        return attributes.family;
    }

    @Override
    public int getSize() {
        return attributes.pointSize;
    }

    @Override
    public EFontWeight getWeight() {
        return attributes.weigth;
    }

    @Override
    public EFontStyle getStyle() {
        return attributes.style;
    }

    @Override
    public EnumSet<ETextDecoration> getDecoration() {
        return attributes.decoration.clone();
    }

    @Override
    public boolean isItalic() {
        return attributes.style==EFontStyle.ITALIC;
    }

    @Override
    public boolean isBold() {
        return attributes.weigth==EFontWeight.BOLD || attributes.weigth==EFontWeight.BOLDER;
    }
        
    @Override
    public String asStr() {
        return attributes.toString();
    }

    @Override
    public int hashCode() {
        return attributes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Font other = (Font) obj;
        return Objects.equals(this.attributes, other.attributes);
    }
}