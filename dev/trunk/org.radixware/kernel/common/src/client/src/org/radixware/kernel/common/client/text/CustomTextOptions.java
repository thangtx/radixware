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

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.enums.ETextOptionsMarkerGroup;
import org.radixware.kernel.common.enums.ESelectorRowStyle;


public final class CustomTextOptions implements ITextOptionsProvider{
    
    private Map<ESelectorRowStyle, TextOptionsContainer> optionsByRowStyle = new HashMap<>(16);
    private ITextOptions defaultOptions;
    
    private static final class TextOptionsContainer{
        
        private final Map<String,ITextOptions> optionsByMarkers = new HashMap<>();
        
        public TextOptionsContainer(){
        }        
        
        private TextOptionsContainer(final TextOptionsContainer source){
            optionsByMarkers.putAll(source.optionsByMarkers);
        }
        
        public final void putOptions(final EnumSet<ETextOptionsMarker> markers, final ITextOptions options){
            if (markers==null){
                throw new NullPointerException("markers cannot be null");
            }
            EnumSet<ETextOptionsMarker> markersForGroup;
            
            Collection<String> keys = new LinkedList<>();
            Collection<String> processedKeys = new LinkedList<>();
            for(ETextOptionsMarkerGroup markerGroup: EnumSet.allOf(ETextOptionsMarkerGroup.class)){
                markersForGroup = ETextOptionsMarker.selectMarkersForGroup(markers, markerGroup);
                if (markersForGroup.isEmpty()){
                    keys = generateKeys(keys, ETextOptionsMarker.getAllMarkersInGroup(markerGroup));
                }else {
                    keys = generateKeys(keys, markers);
                    for (String key: keys){
                        if (!processedKeys.contains(key)){
                            if (options==null){
                                optionsByMarkers.remove(key);
                            }else{
                                optionsByMarkers.put(key, options);
                            }
                            processedKeys.add(key);
                        }
                    }
                }
            }
        }
        
        public final ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers){
            EnumSet<ETextOptionsMarker> normalizedMarkers = ETextOptionsMarker.getNormalizedMarkersSet(markers);
            final StringBuilder markersKeyBuilder = new StringBuilder();
            for(ETextOptionsMarker marker: normalizedMarkers){
                if (markersKeyBuilder.length()>0){
                    markersKeyBuilder.append(',');
                }
                markersKeyBuilder.append(marker.name());
            }
            return optionsByMarkers.get(markersKeyBuilder.toString());
        }
        
        private static Collection<String> generateKeys(final Collection<String> currentKeys, final EnumSet<ETextOptionsMarker> markers){
            final Collection<String> result = new LinkedList<>();
            if (currentKeys.isEmpty()){
                for (ETextOptionsMarker marker: markers){
                    result.add(marker.name());
                }
            }else{
                if (markers.size()==1 && markers.iterator().next()==ETextOptionsMarker.CHOOSEN_OBJECT){
                    result.addAll(currentKeys);
                }
                for (String key: currentKeys){
                    for (ETextOptionsMarker marker: markers){
                        result.add(key+","+marker.name());
                    }
                }
            }
            return result;
        }
        
        public TextOptionsContainer copy(){
            return new TextOptionsContainer(this);
        }
    }
    
    public CustomTextOptions(){
    }
    
    private CustomTextOptions(final CustomTextOptions source){
        defaultOptions = source.getDefaultOptions();
        for(Map.Entry<ESelectorRowStyle, TextOptionsContainer> entry: source.optionsByRowStyle.entrySet()){
            optionsByRowStyle.put(entry.getKey(), entry.getValue().copy());
        }
    }

    @Override
    public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
        if (defaultOptions!=null){
            final EnumSet<ETextOptionsMarker> valueMarkers = 
                ETextOptionsMarker.selectMarkersForGroup(markers, ETextOptionsMarkerGroup.VALUE_STATUS);
            if (valueMarkers.isEmpty() || 
                valueMarkers.iterator().next().getPriority()<ETextOptionsMarker.UNDEFINED_VALUE.getPriority()){
                return defaultOptions;
            }else{
                final TextOptionsContainer optionsContainer = optionsByRowStyle.get(style);
                final ITextOptions options = optionsContainer==null ? null : optionsContainer.getOptions(markers);
                return defaultOptions.changeForegroundColor(options==null ? null : options.getForegroundColor());
            }
        }
        final TextOptionsContainer optionsContainer = optionsByRowStyle.get(style);
        return optionsContainer==null ? null : optionsContainer.getOptions(markers);
    }
    
    public void putOptions(final ITextOptions options, final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style){
        if (markers==null){
            throw new NullPointerException("markers cannot be null");
        }
        if (markers.isEmpty()){
            throw new IllegalArgumentException("At least one ETextOptionsMarker must be defined");
        }
        TextOptionsContainer optionsContainer = optionsByRowStyle.get(style);
        if (optionsContainer==null){
            optionsContainer = new TextOptionsContainer();
            optionsByRowStyle.put(style, optionsContainer);
        }
        optionsContainer.putOptions(markers, options);
    }
    
    
    public void putOptions(final ITextOptions options, final EnumSet<ETextOptionsMarker> markers){
        putOptions(options, markers, null);
    }    
    
    public void putOptions(final ITextOptions options, final ETextOptionsMarker...markers){
        final EnumSet<ETextOptionsMarker> markersSet = EnumSet.noneOf(ETextOptionsMarker.class);
        markersSet.addAll(Arrays.<ETextOptionsMarker>asList(markers));
        putOptions(options, markersSet, null);
    }    
    
    public void putOptions(final ITextOptions options, final ESelectorRowStyle style, final ETextOptionsMarker...markers){
        final EnumSet<ETextOptionsMarker> markersSet = EnumSet.noneOf(ETextOptionsMarker.class);
        markersSet.addAll(Arrays.<ETextOptionsMarker>asList(markers));
        putOptions(options, markersSet, style);
    }
    
    public ITextOptions getDefaultOptions(){
        return defaultOptions;
    }
    
    public void setDefaultOptions(final ITextOptions textOptions){
        defaultOptions = textOptions;
    }
    
    public CustomTextOptions copy(){
        return new CustomTextOptions(this);
    }
}
