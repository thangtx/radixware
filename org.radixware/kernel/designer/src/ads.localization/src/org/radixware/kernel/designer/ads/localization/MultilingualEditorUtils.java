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
package org.radixware.kernel.designer.ads.localization;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

public class MultilingualEditorUtils {
    //Events
    public static final String LOADING_STRINGS = "LoadingStrings";
    public static final String CLOSED = "MultilingualEditorClosed";
    public static final String STRINGS_UP_TO_DATE = "StringsUpToDate";
    public static final String PROGRESS_HANDLE = "ProgressHandleEvent";
    public static final String FILTER_REFRESH = "FilterRefresh";
    public static final String CLEAR_STRINGS = "ClearStrings";
    
    public static final String GO_TO_NEXT = "GoToNext";
    public static final String GO_TO_PREV = "GoToPrev";
    public static final String GO_TO_NEXT_UNCHECKED = "GoToNextUnchecked";
    public static final String GO_TO_PREV_UNCHECKED = "GoToPrevUnchecked";
    public static final String GO_TO_NEXT_ROW = "GoToNextRow";
    public static final String GO_TO_PREVIOUS_ROW = "GoToPreviousRow";
    public static final String GO_TO_NEXT_UNCHECKED_ROW = "GoToNextUncheckedRow";
    public static final String GO_TO_PREVIOUS_UNCHECKED_ROW = "GoToPreviousUncheckedRow";
    public static final String GO_TO_NEXT_EDITABLE = "GoToNextEditable";
    public static final String GO_TO_PREV_EDITABLE = "GoToPrevEditable";
    public static final String GO_TO_NEXT_EDITABLE_ROW = "GoToNextEditableRow";
    public static final String GO_TO_PREV_EDITABLE_ROW = "GoToPrevEditableRow";
    
    public static final String FOCUS_TEXT = "FocusText";
    public static final String SELECT_LAST_TEXT = "SelectLast";
    public static final String SELECT_LAST_UNCHECKED_TEXT = "SelectLastUnchecked";
    public static final String SELECT_FIRST_UNCHECKED_TEXT = "SelectFirstUnchecked";
    public static final String SELECT_LAST_EDITABLE_TEXT = "SelectLastEditable";
    public static final String SELECT_FIRST_EDITABLE_TEXT = "SelectFirstEditable";
    
    public static final String CHECK_ALL_AND_GO     = "CheckAllStringsAndGo";
    public static final String CHECK_STRING_AND_GO  = "CheckStringAndGo";
    public static final String CHECK_STRING         = "CheckString";
    
    public static final String PREFERENCES_KEY = "RadixWareDesigner";
    public static final String EDITOR_KEY = "MlStringEditor";
    public static final String SOURCE_LANGUAGE = "SourceLangs";
    public static final String TRANSLATE_LANGUAGE = "TranslateLangs";
    public static final String SELECTED_LAYERS = "SelectedLayers";
    public static final String OPENED_PHRASEBOOK = "OpenedPhraseBook";
    public static final String OUTPUT_LOCATIONS = "ExportLocation";
    public static final String EXPORT_AUTHOR = "ExportAuthor";
    public static final String EXPORT_TYPES = "ExportTypes";
    public static final String MERGE_AUTHOR = "MergeAuthor";
    public static final String MERGE_BRANCH = "MergeBranch";
    
    //Key Map
    public static final String RELEASE_BUTTON = "RELEASE";    
    
    public static String getPreferensesValue(String key, String defaultValue) {
        try {
            if (Preferences.userRoot().nodeExists(MultilingualEditorUtils.PREFERENCES_KEY)) {
                Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
                if (designerPreferences.nodeExists(MultilingualEditorUtils.EDITOR_KEY)) {
                    Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
                    return editor.get(key, defaultValue);
                }
            }
        } catch (BackingStoreException ex) {
        }
        return defaultValue;
    }
    
    public static void setPreferensesValue(String key, String value) {
        try {
            if (Preferences.userRoot().nodeExists(MultilingualEditorUtils.PREFERENCES_KEY)) {
                Preferences designerPreferences = Preferences.userRoot().node(MultilingualEditorUtils.PREFERENCES_KEY);
                if (designerPreferences.nodeExists(MultilingualEditorUtils.EDITOR_KEY)) {
                    Preferences editor = designerPreferences.node(MultilingualEditorUtils.EDITOR_KEY);
                    if (value != null){
                        editor.put(key, value);
                    } else {
                        editor.remove(key);
                    }
                }
            }
        } catch (BackingStoreException ex) {
        }
    }

    public static Map<Layer, List<Module>> getLayersAndModules(Map<Layer, List<Module>> selectedLayers){
        if (selectedLayers == null) {
            Map<Layer, List<Module>> allLayers = new HashMap<>();
            Collection<Branch> openBranches = RadixFileUtil.getOpenedBranches();
            for (Branch branch : openBranches) {
                for (Layer layer : branch.getLayers().list()) {
                    if (!layer.isLocalizing()) {
                        allLayers.put(layer, null);//addAll(branch.getLayers().list());
                    }
                }
            }
            return allLayers;
        }
        return selectedLayers;
    }
    
    
    public enum SelectionInfo{
        NONE,
        FOCUS,
        PREV,
        UNCHECK_PREV,
        UNCHECK_NEXT,
        EDITABLE_PREV,
        EDITABLE_NEXT
    }
}
