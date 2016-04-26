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
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.enums.ETextOptionsMarkerGroup;
import org.radixware.kernel.common.enums.ESelectorRowStyle;


public final class MergedTextOptionsProvider implements ITextOptionsProvider{
    
    private final ITextOptionsProvider initialOptionsProvider;
    private final ITextOptionsProvider overridedOptionsProvider;
    private final ITextOptionsManager manager;
    
    public MergedTextOptionsProvider(final ITextOptionsProvider initialOptionsProvider, final ITextOptionsProvider overridedOptionsProvider, final ITextOptionsManager manager){
        if ((initialOptionsProvider==null && overridedOptionsProvider==null) || (initialOptionsProvider!=null && overridedOptionsProvider!=null && manager==null)){
            throw new NullPointerException();
        }
        this.initialOptionsProvider = initialOptionsProvider;
        this.overridedOptionsProvider = overridedOptionsProvider;
        this.manager = manager;
    }

    @Override
    public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
        if (initialOptionsProvider==overridedOptionsProvider || overridedOptionsProvider==null){
            return initialOptionsProvider.getOptions(markers, style);
        }
        if (initialOptionsProvider==null){
            return overridedOptionsProvider.getOptions(markers, style);
        }
        final EnumSet<ETextOptionsMarker> normalizedMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        final EnumSet<ETextOptionsMarker> defaultMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        ITextOptions options = null, optionsInCurrentGroup;
        EnumSet<ETextOptionsMarker> markersForGroup;        
        for (ETextOptionsMarkerGroup markerGroup: EnumSet.allOf(ETextOptionsMarkerGroup.class)){
            markersForGroup = ETextOptionsMarker.selectMarkersForGroup(markers, markerGroup);
            if (markersForGroup.isEmpty()){
                final ETextOptionsMarker defaultMarker = ETextOptionsMarker.getDefaultMarkerForGroup(markerGroup);
                if (defaultMarker!=null){
                    defaultMarkers.add(defaultMarker);
                }
            }else if (!markersForGroup.isEmpty()){
                normalizedMarkers.addAll(defaultMarkers);
                normalizedMarkers.add(markersForGroup.iterator().next());
                defaultMarkers.clear();
                optionsInCurrentGroup = 
                    manager.merge(initialOptionsProvider.getOptions(normalizedMarkers, style), overridedOptionsProvider.getOptions(normalizedMarkers, style));
                options = manager.merge(options, optionsInCurrentGroup);                
            }
        }
        if (options==null && !defaultMarkers.isEmpty()){
            options = manager.merge(initialOptionsProvider.getOptions(defaultMarkers, style), overridedOptionsProvider.getOptions(defaultMarkers, style));
        }
        return options;
    }
}