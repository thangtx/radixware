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
package org.radixware.kernel.utils.traceview.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EEventColumn;

public class TabSettings {

    private final List<String> severitySelected = new ArrayList<>();
    private final List<String> sourceSelected = new ArrayList<>();
    private final List<String> contextSelect = new ArrayList<>();
    private final List<String> contextList = new ArrayList<>();
    private final List<String> bookmarkList = new ArrayList<>();
    public ArrayList<TraceEvent.IndexedDate> listOfLabels = new ArrayList<>();

    
    private int tableScrollBarValue = 0;
    private int filterPanelScrollBarValue = 0;

    private long lastTime = -1;
    private long firstTime = -1;
    private String findText = "";

    public TabSettings() {}

    public List<String> getSeveritySelectedList() {
        return severitySelected;
    }

    public List<String> getSourceSelectedList() {
        return sourceSelected;
    }

    public List<String> getContextSelectedList() {
        return contextSelect;
    }
    

    public List<String> getContextList() {
        return contextList;
    }

    public List<String> getBookmarkList() {
        return bookmarkList;
    }

    public void setTableScrollBarValue(int value) {
        tableScrollBarValue = value;
    }

    public void setFilterPanelScrollBarValue(int value) {
        filterPanelScrollBarValue = value;
    }

    public int getTableScrollBarValue() {
        return tableScrollBarValue;
    }

    public int getFilterPanelScrollBarValue() {
        return filterPanelScrollBarValue;
    }

    public void clearSeveritySelectedList() {
        severitySelected.clear();
    }

    public void addSeveritySelectedItem(String item) {
        severitySelected.add(item);
    }

    public void clearSourceSelectedList() {
        sourceSelected.clear();
    }

    public void addSourceSelectedItem(String item) {
        sourceSelected.add(item);
    }

    public void clearContextSelectedList() {
        contextSelect.clear();
    }

    public void addContextSelectedItem(String item) {
        contextSelect.add(item);
    }

    public void addNewBookmark(String bm) {
        bookmarkList.add(bm);
    }

    public void removeBookmark(String bm) {
        bookmarkList.remove(bm);
    }

    public void clearBookmarkList() {
        bookmarkList.clear();
    }

    public void addNewContext(String ctx) {
        contextList.add(ctx);
    }

    public void removeContext(String ctx) {
        contextList.remove(ctx);
    }

    public void clearContextList() {
        contextList.clear();
    }

    public void setFindText(String text) {
        findText = text;
    }

    public void setLastTime(long time) {
        lastTime = time;
    }

    public void setFirstTime(long time) {
        firstTime = time;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public String getFindText() {
        return findText;
    }
}
