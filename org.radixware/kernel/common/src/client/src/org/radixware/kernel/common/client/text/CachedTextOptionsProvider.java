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
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.enums.ESelectorRowStyle;


public final class CachedTextOptionsProvider implements ITextOptionsProvider{
    
    private final ITextOptionsProvider sourceProvider;    
    private final Map<String,ITextOptions> cache = new HashMap<>();
    
    public CachedTextOptionsProvider(final ITextOptionsProvider sourceProvider){
        this.sourceProvider = sourceProvider;
    }

    @Override
    public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
        final EnumSet<ETextOptionsMarker> normalizedMarkers = ETextOptionsMarker.getNormalizedMarkersSet(markers);        
        final StringBuilder cacheKeyBuilder = new StringBuilder();
        if (style!=null){
            cacheKeyBuilder.append("style=");
            cacheKeyBuilder.append(style.name());
        }
        for (ETextOptionsMarker marker: normalizedMarkers){
            if (cacheKeyBuilder.length()>0){
                cacheKeyBuilder.append(',');
            }
            cacheKeyBuilder.append(marker.name());
        }
        final String cacheKey = cacheKeyBuilder.toString();
        ITextOptions options = cache.get(cacheKey);
        if (options==null){
            options = sourceProvider.getOptions(normalizedMarkers, style);
            cache.put(cacheKey, options);
        }
        return options;
    }
    
    public void clearCache(){
        cache.clear();
    }
}