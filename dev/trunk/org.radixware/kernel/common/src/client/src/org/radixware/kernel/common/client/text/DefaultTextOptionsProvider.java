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

import java.awt.Color;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.enums.ESelectorRowStyle;


public abstract class DefaultTextOptionsProvider implements ITextOptionsProvider{
           
    private Color readForegroundColor(final List<String> cfgGroups, final String value){
        final StringBuilder cfgPath = new StringBuilder();
        for (String cfgGroup: cfgGroups){
            if (cfgPath.length()>0){
                cfgPath.append("/");
            }
            cfgPath.append(cfgGroup);
        }
        cfgPath.append("/");
        cfgPath.append(SettingNames.TextOptions.FCOLOR);
        cfgPath.append("/");
        cfgPath.append(value);
        final String colorAsStr = readString(cfgPath.toString());
        if (colorAsStr==null){
            if (SettingNames.Properties.INVALID.equals(value) || SettingNames.Properties.BROKEN.equals(value)){
                return Color.RED;
            }
            return null;
        }
        try{
            return Color.decode(colorAsStr);
        }catch(IllegalArgumentException exception){
            final String message = 
                getMessageProvider().translate("ExplorerError", "Failed to parse color from string \'%1$s\'");
            getTracer().error(String.format(message, colorAsStr), exception);
            return null;
        }
    }
    
    private Color readSelectedRowColor(){
        final StringBuilder cfgPath = new StringBuilder();
        cfgPath.append(SettingNames.SYSTEM);
        cfgPath.append("/");
        cfgPath.append(SettingNames.SELECTOR_GROUP);
        cfgPath.append("/");
        cfgPath.append(SettingNames.Selector.COMMON_GROUP);
        cfgPath.append("/");
        cfgPath.append(SettingNames.Selector.Common.SELECTED_ROW_COLOR);
        final String colorAsStr = readString(cfgPath.toString());
        if (colorAsStr!=null){
            try{
                return Color.decode(colorAsStr);
            }catch(IllegalArgumentException exception){
                final String message = 
                    getMessageProvider().translate("ExplorerError", "Failed to parse color from string \'%1$s\'");
                getTracer().error(String.format(message, colorAsStr), exception);                
            }            
        }
        return null;
    }
    
    private ITextOptions readTextOptions(final List<String> cfgGroups){
        final StringBuilder cfgPath = new StringBuilder();
        for (String cfgGroup: cfgGroups){
            if (cfgPath.length()>0){
                cfgPath.append('/');
            }
            cfgPath.append(cfgGroup);
        }
        final ClientSettings settings = getSettings();
        settings.pushGroup();
        try{
            settings.endAllGroups();
            return settings.readTextOptions(cfgPath.toString());
        }finally{
            settings.popGroup();
        }
    }
    
    private String readString(final String cfgPath){
        final ClientSettings settings = getSettings();
        settings.pushGroup();
        try{
            settings.endAllGroups();
            return settings.readString(cfgPath);
        }finally{
            settings.popGroup();
        }        
    }
            
    public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers) {
        return getOptions(markers,(ESelectorRowStyle)null);
    }
    
    public ITextOptions getOptions(final ETextOptionsMarker... markers) {
        return getOptions(null,markers);
    }
        
    public ITextOptions getOptions(final ESelectorRowStyle style, final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> markersSet = EnumSet.noneOf(ETextOptionsMarker.class);
        markersSet.addAll(Arrays.asList(markers));
        return getOptions(markersSet,style);
    }    
        
    public ITextOptions getDefaultOptions() {
        return getOptions(EnumSet.of(ETextOptionsMarker.EDITOR));
    }
    
    @Override
    public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style){
        final List<String> cfgGroups = new LinkedList<>();
        cfgGroups.add(SettingNames.SYSTEM);
        {//first look for component marker            
            if (markers.contains(ETextOptionsMarker.SELECTOR_ROW)){
                cfgGroups.add(SettingNames.SELECTOR_GROUP);
                cfgGroups.add(SettingNames.Selector.STYLES_GROUP);
                cfgGroups.add(style==null ? ESelectorRowStyle.NORMAL.getValue() : style.getValue());
            }else if (markers.contains(ETextOptionsMarker.LABEL)){
                cfgGroups.add(SettingNames.EDITOR_GROUP);
                cfgGroups.add(SettingNames.Editor.PROPERTY_TITLES_GROUP);
            }else{
                cfgGroups.add(SettingNames.EDITOR_GROUP);
                cfgGroups.add(SettingNames.Editor.PROPERTY_VALUES_GROUP);
            }
        }
        {//second look for editing policy marker
            if (markers.contains(ETextOptionsMarker.READONLY_VALUE)){
                cfgGroups.add(SettingNames.Properties.READONLY_PROPERTY);
            }else if (markers.contains(ETextOptionsMarker.MANDATORY_VALUE)){
                cfgGroups.add(SettingNames.Properties.MANDATORY_PROPERTY);
            }else{
                cfgGroups.add(SettingNames.Properties.OTHER_PROPERTY);                
            }
        }
        ITextOptions options = readTextOptions(cfgGroups);
        {//third look for component state markers
            final Color backgroundColor;
            if (markers.contains(ETextOptionsMarker.CHOOSEN_OBJECT)){
                backgroundColor = readSelectedRowColor();
            }else{
                backgroundColor = null;
            }
            if (backgroundColor!=null){
                options = options.changeBackgroundColor(backgroundColor);
            }
        }
        {//forth look for value state markers            
            final String foregroundColorSettingName;            
            if (markers.contains(ETextOptionsMarker.INVALID_VALUE) && !markers.contains(ETextOptionsMarker.LABEL)){
                foregroundColorSettingName = SettingNames.Properties.INVALID;
            }else if (markers.contains(ETextOptionsMarker.BROKEN_REFERENCE) && !markers.contains(ETextOptionsMarker.LABEL)){
                foregroundColorSettingName = SettingNames.Properties.BROKEN;
            }else if (markers.contains(ETextOptionsMarker.INHERITED_VALUE)){
                foregroundColorSettingName = SettingNames.Properties.INHERITED;
            }else if (markers.contains(ETextOptionsMarker.OVERRIDDEN_VALUE)){
                foregroundColorSettingName = SettingNames.Properties.OVERRIDED;
            }else if (markers.contains(ETextOptionsMarker.UNDEFINED_VALUE) && !markers.contains(ETextOptionsMarker.LABEL)){
                foregroundColorSettingName = SettingNames.Properties.UNDEFINED;
            }else{
                foregroundColorSettingName = null;
            }
            if (foregroundColorSettingName==null){
                return options;
            }else{
                final Color foregroundColor = readForegroundColor(cfgGroups, foregroundColorSettingName);
                return foregroundColor==null ? options : options.changeForegroundColor(foregroundColor);
            }
        }        
    }
    
    protected abstract ClientSettings getSettings();    
    protected abstract MessageProvider getMessageProvider();
    protected abstract ClientTracer getTracer();
}