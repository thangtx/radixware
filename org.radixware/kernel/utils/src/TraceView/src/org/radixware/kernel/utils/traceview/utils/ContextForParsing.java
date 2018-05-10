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
package org.radixware.kernel.utils.traceview.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EFileFormat;
import static org.radixware.kernel.utils.traceview.utils.ParseTraceUtils.determineTheFileFormat;
import static org.radixware.kernel.utils.traceview.utils.ParseTraceUtils.parseTraceFile;
import static org.radixware.kernel.utils.traceview.utils.ParseTraceUtils.parseTraceFileXml;

public class ContextForParsing {

    private final IEventParserListener eventParserListener;
    private final List<TraceEvent> events = new ArrayList<>();
    private final TraceEvent maxSizeEvent;
    private final File file;
    private final EFileFormat fileFormat;
    private final boolean splitByContext;
    private ArrayList<String> arrayListName = new ArrayList<>();
    private final Set<String> sortedSetOfUniqueSources = new TreeSet<>();

    public ContextForParsing(IEventParserListener eventParserListener, File file, boolean splitByContext, boolean getMaxSizeEvent) {
        this.eventParserListener = eventParserListener;
        this.file = file;
        this.splitByContext = splitByContext;
        maxSizeEvent = getMaxSizeEvent ? createMaxSizeEvent() : null;
        fileFormat = determineTheFileFormat(file);
        if (fileFormat == EFileFormat.UNIDENTIFIED) {
            throw new IllegalArgumentException(new StringBuilder("File [").append(file.getAbsolutePath()).append("] format UNIDENTIFIED!").toString());
        }
    }

    private static TraceEvent createMaxSizeEvent() {
        TraceEvent maxSizeEvent = new TraceEvent();

        maxSizeEvent.setContext("Context");
        maxSizeEvent.setSource("Source");
        maxSizeEvent.setMessage("Message");
        maxSizeEvent.setSeverity(TraceViewSettings.ESeverity.getMaxSizeSeverity());

        return maxSizeEvent;
    }

    public void parseFile() {
        if (fileFormat == EFileFormat.XML) {
            parseTraceFileXml(this);
        } else {
            parseTraceFile(this);
        }
    }

    public EFileFormat getFileFormat() {
        return fileFormat;
    }

    public boolean splitByContext() {
        return splitByContext;
    }

    public boolean isGetMaxSizeEvent() {
        return maxSizeEvent != null;
    }

    public boolean trackProgress() {
        return eventParserListener != null;
    }

    public List<TraceEvent> getEvents() {
        return events;
    }

    public TraceEvent getMaxSizeEvent() {
        return maxSizeEvent;
    }

    public IEventParserListener getEventParserListener() {
        return eventParserListener;
    }

    public ArrayList getArrayListName() {
        return arrayListName;
    }

    public void setArrayListName(ArrayList arrayListName) {
        this.arrayListName = arrayListName;
    }

    public HashMap<String, Object> getData() {
        if (maxSizeEvent != null) {
            maxSizeEvent.setIndexedDate(new TraceEvent.IndexedDate(new Date(), -1));
            maxSizeEvent.setContext(maxSizeEvent.getContext() + "__");
            maxSizeEvent.setSource(maxSizeEvent.getSource() + "__");
            maxSizeEvent.addMessage("__");
        }
        HashMap<String, Object> data = new HashMap<>();

        data.put(TraceViewSettings.DATA, events);
        data.put(TraceViewSettings.FICTIVE_ELEMENT, maxSizeEvent);
        data.put(TraceViewSettings.UNIQUE_SOURCES, new ArrayList<>(sortedSetOfUniqueSources));
        data.put(TraceViewSettings.CONTEXT_ARRAY, arrayListName);

        return data;
    }

    public File getFile() {
        return file;
    }

    public void setProgress(int progressInPercentage) {
        if (trackProgress()) {
            eventParserListener.setProgress(progressInPercentage, events.size());
        }
    }

    public Set<String> getSortedSetOfUniqueSources() {
        return sortedSetOfUniqueSources;
    }
}
