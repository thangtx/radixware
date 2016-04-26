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
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.enums.ETextOptionsMarkerGroup;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;

public class DefaultTextOptionsProvider implements ITextOptionsProvider {

    private WpsEnvironment environment;

    public DefaultTextOptionsProvider(final WpsEnvironment environment) {
        this.environment = environment;
    }

    public WpsTextOptions getDefault() {
        return TextOptionsManager.getInstance().getOptionsForStyle(ECssTextOptionsStyle.REGULAR_VALUE);
    }

    @Override
    public WpsTextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
        final ETextOptionsMarker componentMarker;
        {
            final EnumSet<ETextOptionsMarker> componentMarkers
                    = ETextOptionsMarker.selectMarkersForGroup(markers, ETextOptionsMarkerGroup.GUI_COMPONENT);
            if (componentMarkers.isEmpty()) {
                componentMarker
                        = ETextOptionsMarker.getDefaultMarkerForGroup(ETextOptionsMarkerGroup.GUI_COMPONENT);
            } else {
                componentMarker = componentMarkers.iterator().next();
            }
        }
        final ETextOptionsMarker editingMarker;
        {
            final EnumSet<ETextOptionsMarker> editingMarkers
                    = ETextOptionsMarker.selectMarkersForGroup(markers, ETextOptionsMarkerGroup.EDITING_POLICY);
            if (editingMarkers.isEmpty()) {
                editingMarker = ETextOptionsMarker.getDefaultMarkerForGroup(ETextOptionsMarkerGroup.EDITING_POLICY);
            } else {
                editingMarker = editingMarkers.iterator().next();
            }
        }
        final TextOptionsManager manager = TextOptionsManager.getInstance();
        final WpsTextOptions baseOptions;

        final List<String> cfgGroups = new LinkedList<>();
        cfgGroups.add(SettingNames.SYSTEM);

        {
            if (componentMarker == ETextOptionsMarker.SELECTOR_ROW) {//selector
                baseOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.REGULAR_VALUE);
                cfgGroups.add(SettingNames.SELECTOR_GROUP);
                cfgGroups.add(SettingNames.Selector.STYLES_GROUP);
                cfgGroups.add(style == null ? ESelectorRowStyle.NORMAL.getValue() : style.getValue());

            } else if (componentMarker == ETextOptionsMarker.LABEL) {//editor labels
                cfgGroups.add(SettingNames.EDITOR_GROUP);
                cfgGroups.add(SettingNames.Editor.PROPERTY_TITLES_GROUP);
                if (editingMarker == ETextOptionsMarker.MANDATORY_VALUE) {
                    baseOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.REQUIRED_VALUE_TITLE);
                } else {
                    baseOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.REGULAR_TITLE);
                }

            } else {//editors
                cfgGroups.add(SettingNames.EDITOR_GROUP);
                cfgGroups.add(SettingNames.Editor.PROPERTY_VALUES_GROUP);
                if (editingMarker == ETextOptionsMarker.READONLY_VALUE) {
                    baseOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.READONLY_VALUE);
                } else {
                    baseOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.REGULAR_VALUE);
                }
            }

            if (editingMarker == ETextOptionsMarker.READONLY_VALUE) {
                cfgGroups.add(SettingNames.Properties.READONLY_PROPERTY);
            } else if (editingMarker == ETextOptionsMarker.MANDATORY_VALUE) {
                cfgGroups.add(SettingNames.Properties.MANDATORY_PROPERTY);
            } else {
                cfgGroups.add(SettingNames.Properties.OTHER_PROPERTY);
            }
        }

        WpsTextOptions configOptions = readTextOptions(cfgGroups);
        final ETextOptionsMarker valueMarker;
        {
            final EnumSet<ETextOptionsMarker> valueStatusMarkers
                    = ETextOptionsMarker.selectMarkersForGroup(markers, ETextOptionsMarkerGroup.VALUE_STATUS);
            if (valueStatusMarkers.isEmpty()) {
                valueMarker = ETextOptionsMarker.getDefaultMarkerForGroup(ETextOptionsMarkerGroup.VALUE_STATUS);
            } else {
                valueMarker = valueStatusMarkers.iterator().next();
            }
        }
        final WpsTextOptions valueOptions;

        final ECssTextOptionsStyle cssStyle;        
        if (valueMarker==ETextOptionsMarker.INVALID_VALUE || valueMarker==ETextOptionsMarker.BROKEN_REFERENCE){
            if (componentMarker==ETextOptionsMarker.LABEL){
                valueOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.INVALID_VALUE_TITLE);                
            }else{
                valueOptions = manager.getOptionsForStyle(ECssTextOptionsStyle.INVALID_VALUE);
            }
            
        }else{
            final String foregroundColorSettingName;
            if (valueMarker==null){
                cssStyle = null;
                foregroundColorSettingName = null;
            }else{
                switch (valueMarker) {
                    case UNDEFINED_VALUE: {
                        cssStyle
                                = componentMarker == ETextOptionsMarker.LABEL ? null : ECssTextOptionsStyle.UNDEFINED_VALUE;
                        foregroundColorSettingName = componentMarker == ETextOptionsMarker.LABEL ? null : SettingNames.Properties.UNDEFINED;
                        break;
                    }
                    case INHERITED_VALUE: {
                        cssStyle
                                = componentMarker == ETextOptionsMarker.LABEL ? null : ECssTextOptionsStyle.INHERITED_VALUE;
                        foregroundColorSettingName = componentMarker == ETextOptionsMarker.LABEL ? null : SettingNames.Properties.INHERITED;
                        break;
                    }
                    case OVERRIDDEN_VALUE: {
                        cssStyle
                                = componentMarker == ETextOptionsMarker.LABEL ? null : ECssTextOptionsStyle.OVERRIDED_VALUE;
                        foregroundColorSettingName  = componentMarker == ETextOptionsMarker.LABEL ? null : SettingNames.Properties.OVERRIDED;
                        break;
                    }
                    default: {
                        cssStyle = null;
                        foregroundColorSettingName = null;
                        break;
                    }
                }
            }
            valueOptions = cssStyle == null ? null : manager.getOptionsForStyle(cssStyle);
            final String foreground = readColor(cfgGroups, foregroundColorSettingName, SettingNames.TextOptions.FCOLOR);        
            if (foreground!=null){
                final Color foregroundColor = Color.decode(foreground);
                if (configOptions==null){
                    configOptions = WpsTextOptions.Factory.getOptions(foregroundColor);
                }else{
                    configOptions = configOptions.changeForegroundColor(foregroundColor);
                }
            }            
        }
        if (componentMarker != ETextOptionsMarker.LABEL) {
            final String background = readColor(cfgGroups, null, SettingNames.TextOptions.BCOLOR);
            if (background!=null){
                final Color backgroundColor = Color.decode(background);
                if (configOptions==null){
                    configOptions = WpsTextOptions.Factory.getOptions((Color)null, backgroundColor);
                }else{
                    configOptions = configOptions.changeBackgroundColor(backgroundColor);
                }
            }
        }        
        WpsTextOptions finalOptions = valueOptions == null ? baseOptions : manager.merge(baseOptions, valueOptions);
        return configOptions == null ? finalOptions : manager.merge(finalOptions, configOptions);
    }

    public WpsTextOptions getOptions(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> markersSet = EnumSet.noneOf(ETextOptionsMarker.class);
        markersSet.addAll(Arrays.asList(markers));
        return getOptions(markersSet, null);
    }

    private WpsTextOptions readTextOptions(List<String> cfgGroups) {
        final StringBuilder cfgPath = new StringBuilder();
        for (String cfgGroup : cfgGroups) {
            if (cfgPath.length() > 0) {
                cfgPath.append("/");
            }
            cfgPath.append(cfgGroup);
        }

        WpsTextOptions options = getSettings().readTextOptions(cfgPath.toString());
        return options;
    }

    private String readColor(final List<String> cfgGroups, final String value, String opt) {
        if (getSettings() == null) {
            return null;
        }
        final StringBuilder cfgPath = new StringBuilder();
        for (String cfgGroup : cfgGroups) {
            if (cfgPath.length() > 0) {
                cfgPath.append("/");
            }
            cfgPath.append(cfgGroup);
        }
        cfgPath.append("/");

        cfgPath.append(opt);
        if (value != null) {
            cfgPath.append("/");
            cfgPath.append(value);
        }
        final String colorAsStr = getSettings().readString(cfgPath.toString());
        return colorAsStr;
    }

    protected WpsSettings getSettings() {
        return environment.getConfigStore();
    }
}
