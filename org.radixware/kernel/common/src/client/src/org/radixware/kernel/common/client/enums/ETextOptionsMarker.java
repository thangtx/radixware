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

package org.radixware.kernel.common.client.enums;

import java.util.EnumSet;


public enum ETextOptionsMarker {    
    SELECTOR_ROW(ETextOptionsMarkerGroup.GUI_COMPONENT,2),
    LABEL(ETextOptionsMarkerGroup.GUI_COMPONENT,1),    
    EDITOR(ETextOptionsMarkerGroup.GUI_COMPONENT,0),
    READONLY_VALUE(ETextOptionsMarkerGroup.EDITING_POLICY,2),
    MANDATORY_VALUE(ETextOptionsMarkerGroup.EDITING_POLICY,1),
    EDITABLE_VALUE(ETextOptionsMarkerGroup.EDITING_POLICY,0),
    CHOOSEN_OBJECT(ETextOptionsMarkerGroup.COMPONENT_STATUS,1/*this marker is not default*/),
    INVALID_VALUE(ETextOptionsMarkerGroup.VALUE_STATUS,5),
    BROKEN_REFERENCE(ETextOptionsMarkerGroup.VALUE_STATUS,4),
    INHERITED_VALUE(ETextOptionsMarkerGroup.VALUE_STATUS,3),    
    UNDEFINED_VALUE(ETextOptionsMarkerGroup.VALUE_STATUS,2),
    OVERRIDDEN_VALUE(ETextOptionsMarkerGroup.VALUE_STATUS,1),
    REGULAR_VALUE(ETextOptionsMarkerGroup.VALUE_STATUS,0);
    
    private final ETextOptionsMarkerGroup group;
    private final int priority;
    
    private ETextOptionsMarker(final ETextOptionsMarkerGroup group, final int priority){
        this.group = group;
        this.priority = priority;
    }  
    
    public ETextOptionsMarkerGroup getGroup(){
        return group;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public static EnumSet<ETextOptionsMarker> getAllMarkersInGroup(final ETextOptionsMarkerGroup group){
        final EnumSet<ETextOptionsMarker> markers = EnumSet.noneOf(ETextOptionsMarker.class);
        for (ETextOptionsMarker marker: ETextOptionsMarker.values()){
            if (group==marker.getGroup()){
                markers.add(marker);
            }
        }
        return markers;
    }
    
    public static EnumSet<ETextOptionsMarker> selectMarkersForGroup(final EnumSet<ETextOptionsMarker> markers, final ETextOptionsMarkerGroup group){
        final EnumSet<ETextOptionsMarker> result = EnumSet.noneOf(ETextOptionsMarker.class);
        for (ETextOptionsMarker marker: markers){
            if (group==marker.getGroup()){
                result.add(marker);
            }
        }
        return result;
    }
    
    public static ETextOptionsMarker getDefaultMarkerForGroup(final ETextOptionsMarkerGroup group){
        final EnumSet<ETextOptionsMarker> markers = getAllMarkersInGroup(group);
        for (ETextOptionsMarker marker: markers){
            if (marker.getPriority()==0){
                return marker;
            }
        }
        return null;//no default marker
    }
    
    public static EnumSet<ETextOptionsMarker> getNormalizedMarkersSet(final EnumSet<ETextOptionsMarker> markers){
        final EnumSet<ETextOptionsMarker> normalizedMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        final EnumSet<ETextOptionsMarker> defaultMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        EnumSet<ETextOptionsMarker> markersForGroup;
        for (ETextOptionsMarkerGroup markerGroup: EnumSet.allOf(ETextOptionsMarkerGroup.class)){
            markersForGroup = ETextOptionsMarker.selectMarkersForGroup(markers, markerGroup);
            if (markersForGroup.isEmpty()){
                final ETextOptionsMarker marker = ETextOptionsMarker.getDefaultMarkerForGroup(markerGroup);
                if (marker!=null){
                    defaultMarkers.add(marker);
                }
            }else{
                normalizedMarkers.addAll(defaultMarkers);
                normalizedMarkers.add(markersForGroup.iterator().next());
                defaultMarkers.clear();
            }
        }
        return normalizedMarkers;
    }
}