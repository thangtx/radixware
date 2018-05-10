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
package org.radixware.kernel.utils.traceview;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.io.File;
import java.util.Date;
import org.radixware.kernel.utils.traceview.TraceViewSettings.ESeverity;
import org.radixware.kernel.utils.traceview.utils.ContextSet;

public class TraceEvent {

    public static class IndexedDate {

        private final Date date;
        private final int index;

        public IndexedDate(Date date, int index) {
            this.date = date;
            this.index = index;
        }

        public Date getDate() {
            return date;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return (TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().format(date) + " " + index);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            return (date.equals(((IndexedDate) obj).getDate()) && index == ((IndexedDate) obj).getIndex());
        }
    }

    private ESeverity severity;
    private IndexedDate indexedDate;
    private String source;
    private String context;
    private StringBuilder message;
    private File file;
    private ContextSet contextSet;

    public ESeverity getSeverity() {
        return severity;
    }
    
    public ContextSet getContextSet(){
        return contextSet;
    }

    public IndexedDate getIndexedDate() {
        return indexedDate;
    }

    public String getSource() {
        return source;
    }

    public String getContext() {
        return context;
    }

    public String getMessage() {
        return message.toString();
    }

    public File getFile() {
        return file;
    }

    public void setSeverity(ESeverity severity) {
        this.severity = severity;
    }
    
    public void setContextSet(ContextSet contextSet){
        this.contextSet = contextSet;
    }

    public void setIndexedDate(IndexedDate indexedDate) {
        this.indexedDate = indexedDate;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setMessage(String message) {
        this.message = new StringBuilder(message);
    }

    public TraceEvent addMessage(String message) {
        this.message.append(message);
        return this;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return new StringBuilder(severity.getShortName())
                .append(TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().format(indexedDate.getDate()))
                .append(" : ").append(source)
                .append(" : ").append(context)
                .append(" : ").append(message).toString();

    }

    public static final Attribute<TraceEvent, String> SOURCE = new SimpleAttribute<TraceEvent, String>("source") {
        @Override
        public String getValue(TraceEvent event, QueryOptions queryOptions) {
            return event.getSource();
        }
    };
    public static final Attribute<TraceEvent, String> CONTEXT = new SimpleAttribute<TraceEvent, String>("context") {
        @Override
        public String getValue(TraceEvent event, QueryOptions queryOptions) {
            return event.getContext();
        }
    };
    public static final Attribute<TraceEvent, String> SEVERITY = new SimpleAttribute<TraceEvent, String>("severity") {
        @Override
        public String getValue(TraceEvent event, QueryOptions queryOptions) {
            return event.getSeverity().getFullNameInUppercase();
        }
    };
    public static final Attribute<TraceEvent, Date> DATE = new SimpleAttribute<TraceEvent, Date>("date") {
        @Override
        public Date getValue(TraceEvent event, QueryOptions queryOptions) {
            return event.getIndexedDate().getDate();
        }
    };
    public static final Attribute<TraceEvent, String> MESSAGE = new SimpleAttribute<TraceEvent, String>("message") {
        @Override
        public String getValue(TraceEvent event, QueryOptions queryOptions) {
            return event.getMessage();
        }
    };
    public static final Attribute<TraceEvent, String> FILE = new SimpleAttribute<TraceEvent, String>("file") {
        @Override
        public String getValue(TraceEvent event, QueryOptions queryOptions) {
            return event.getFile().getAbsolutePath();
        }
    };
}
