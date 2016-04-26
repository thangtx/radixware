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
package org.radixware.kernel.common.trace;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;

public class TraceProfile {

    protected EEventSeverity defSeverity;
    protected Map<String, EEventSeverity> severityByEventSource = new TreeMap<>(); //TreeMap is used for string presentation normalization
    protected long minSeverity;
    private final RadixEventSource changeHandler = new RadixEventSource();
    private final Map<String, EventSourceOptions> optionsByEventSource;

    public TraceProfile(final EEventSeverity defaultSeverity, final Map<String, EEventSeverity> severityByEventSource) {
        this.defSeverity = defaultSeverity;
        long ms = defaultSeverity.getValue().longValue();
        final java.util.Iterator<Entry<String, EEventSeverity>> iter = severityByEventSource.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, EEventSeverity> entry = iter.next();
            this.severityByEventSource.put(entry.getKey(), entry.getValue());
            long s = Integer.valueOf(entry.getKey()).longValue();
            if (ms > s) {
                ms = s;
            }
        }
        minSeverity = ms;
        optionsByEventSource = new HashMap<>();
    }

    public TraceProfile(final String str) {
        optionsByEventSource = new HashMap<>();
        if (str == null || str.length() == 0) {
            defSeverity = EEventSeverity.NONE;
            minSeverity = defSeverity.getValue().longValue();
        } else {
            //<defaultSeverity>;<source>=<severity>, например "Error;Arte.DefManager=Debug".
            final StringTokenizer tokens = new StringTokenizer(str, ";");
            if (!tokens.hasMoreTokens()) {
                throw new WrongFormatError("Wrong format of trace profile's string. String is \"" + str + "\"", null);
            }
            final String defStr = tokens.nextToken();
            final Long defVal = EEventSeverity.getForName(defStr).getValue();
            try {
                this.defSeverity = EEventSeverity.getForValue(defVal);
            } catch (NoConstItemWithSuchValueError e) {
                throw new WrongFormatError("Wrong format of trace profile's string. Unknown trace level: \"" + defStr + "\"", e);
            }
            long ms = defSeverity.getValue().longValue();
            while (tokens.hasMoreTokens()) {
                final String token = tokens.nextToken();
                if (token.length() == 0) {
                    break;
                }
                final int i = token.indexOf('=');
                if (i <= 0) {
                    throw new WrongFormatError("Wrong format of trace profile's item. Event source string and '=' are required. Item is \"" + str + "\"", null);
                }
                final String source = token.substring(0, i);
                final String severityAndOptsStr = token.substring(i + 1);
                int indexOfOptsStart = severityAndOptsStr.indexOf("[");
                final String severityStr;
                final String optsStr;
                if (indexOfOptsStart > 0) {
                    severityStr = severityAndOptsStr.substring(0, indexOfOptsStart);
                    optsStr = severityAndOptsStr.substring(indexOfOptsStart + 1, severityAndOptsStr.length() - 1);
                } else {
                    severityStr = severityAndOptsStr;
                    optsStr = "";
                }
                final Long severity = EEventSeverity.getForName(severityStr).getValue();
                if (ms > severity.longValue()) {
                    ms = severity.intValue();
                }
                severityByEventSource.put(source, EEventSeverity.getForValue(severity));
                if (!optsStr.isEmpty()) {
                    optionsByEventSource.put(source, new EventSourceOptions(optsStr));
                }
            }
            minSeverity = ms;
        }
    }

    public TraceProfile(final TraceProfile source) {
        this.defSeverity = source.defSeverity;
        this.minSeverity = source.minSeverity;
        this.severityByEventSource = new TreeMap<>(source.severityByEventSource);
        this.optionsByEventSource = source.optionsByEventSource;
    }

    public synchronized void removeEventListener(IRadixEventListener listener) {
        changeHandler.removeEventListener(listener);
    }

    public synchronized void addEventListener(IRadixEventListener listener) {
        changeHandler.addEventListener(listener);
    }

    protected void fireProfileChange() {
        changeHandler.fireEvent(new RadixEvent());
    }

    public long getMinSeverity() {
        return minSeverity;
    }

    public long getMinSeverity(EEventSource eventSource) {
        return getMinSeverity(eventSource == null ? (String) null : eventSource.getValue());
    }

    public long getMinSeverity(final String eventSource) {
        if (eventSource == null) {
            return getMinSeverity();
        }
        long ms = getEventSourceSeverity(eventSource).getValue().longValue();
        final String childEventSourcePrefix = eventSource + ".";
        for (Entry<String, EEventSeverity> e : severityByEventSource.entrySet()) {
            final long sev = e.getValue().getValue().longValue();
            if (ms > sev && e.getKey().startsWith(childEventSourcePrefix)) {
                ms = sev;
            }
        }
        return ms;
    }

    public boolean itemMatch(final TraceItem item) {
        return itemMatch(item.severity, item.source);
    }
    
    public boolean itemMatch(final EEventSeverity severity, final String source){
        final long s = severity != null ? severity.getValue().longValue() : EEventSeverity.DEBUG.getValue();
        if (//RADIX-4397
                s > 0
                && source != null
                && source.startsWith(EEventSource.APP_AUDIT.getValue())) { //APP_AUDIT's events can't be switched off
            return true;
        }
        if (s < minSeverity) {
            return false;
        }
        return s >= getEventSourceSeverity(source).getValue().longValue();
    }

    private String calcStringRepresentation() {
        final StringBuilder res = new StringBuilder();
        if (defSeverity != null) {
            res.append(defSeverity.getName());
            res.append(';');
        }
        for (Map.Entry<String, EEventSeverity> k : severityByEventSource.entrySet()) {
            res.append(k.getKey());
            res.append('=');
            res.append(k.getValue().getName());
            final EventSourceOptions options = optionsByEventSource.get(k.getKey());
            if (options != null && !options.toString().isEmpty()) {
                res.append("[");
                res.append(options.toString());
                res.append("]");
            }
            res.append(';');
        }
        return res.toString();
    }

    @Override
    public String toString() {
        return calcStringRepresentation();
    }

    public boolean getUseInheritedSeverity(final String source) {
        return !severityByEventSource.containsKey(source);
    }

    public EEventSeverity getEventSourceSeverity(String source) {
        if (source != null) {
            int i;
            for (;;) {
                EEventSeverity tmp = severityByEventSource.get(source);
                if (tmp != null) {
                    return tmp;
                }
                i = source.lastIndexOf('.');
                if (i < 0) {
                    break;
                }
                source = source.substring(0, i);
            }
        }
        return defSeverity;
    }

    public void add(final TraceProfile p) {
        if (p.minSeverity < minSeverity) {
            minSeverity = p.minSeverity;
        }
        if (defSeverity == null || (p.defSeverity != null && p.defSeverity.getValue().longValue() < defSeverity.getValue().longValue())) {
            defSeverity = p.defSeverity;
        }
        for (Map.Entry<String, EEventSeverity> k : p.severityByEventSource.entrySet()) {
            EEventSeverity s = severityByEventSource.get(k.getKey());
            if (s == null || (k.getValue() != null && k.getValue().getValue().longValue() < s.getValue().longValue())) {
                severityByEventSource.put(k.getKey(), k.getValue());
            }
        }
        for (Map.Entry<String, EventSourceOptions> entry : p.optionsByEventSource.entrySet()) {
            final Map<String, Object> optionsMap = new HashMap<>();
            final EventSourceOptions thisOptions = optionsByEventSource.get(entry.getKey());
            if (thisOptions != null) {
                optionsMap.putAll(thisOptions.options);
            }
            if (entry.getValue() != null) {
                optionsMap.putAll(entry.getValue().options);
            }
        }
    }

    public void set(final String source, final EEventSeverity severity) {
        set(source, severity, "");
    }

    public void set(final String source, final EEventSeverity severity, final String options) {
        severityByEventSource.put(source, severity);
        optionsByEventSource.put(source, new EventSourceOptions(options == null ? "" : options));
        calcMinSeverity();
    }

    public void setDefaultSeverity(final EEventSeverity severity) {
        defSeverity = severity;
        if (severity.getValue().longValue() < minSeverity) {
            minSeverity = severity.getValue().longValue();
        }
    }

    public EEventSeverity getDefaultSeverity() {
        return defSeverity;
    }

    public void setUseInheritedSeverity(final String source) {
        severityByEventSource.remove(source);
        calcMinSeverity();
    }

    protected void calcMinSeverity() {
        long ms = defSeverity.getValue().longValue();
        for (EEventSeverity severity : severityByEventSource.values()) {
            if (ms > severity.getValue().longValue()) {
                ms = severity.getValue().longValue();
            }
        }
        minSeverity = ms;
    }

    public Object getOption(final String eventSource, final String optionName) {
        final EventSourceOptions container = optionsByEventSource.get(eventSource);
        if (container != null) {
            return container.getOption(optionName);
        } else {
            return null;
        }
    }
    
    public boolean hasOption(final String eventSource, final String optionName) {
        final EventSourceOptions container = optionsByEventSource.get(eventSource);
        if (container != null) {
            return container.hasOption(optionName);
        } else {
            return false;
        }
    }

    public static class EventSourceOptions {

        private final Map<String, Object> options = new TreeMap<>();

        public EventSourceOptions(final String optionsStr) {
            if (optionsStr != null) {
                final String[] optsAndVals = optionsStr.split(",");
                for (String optAndValStr : optsAndVals) {
                    final String[] optAndVal = optAndValStr.split("=", 2);
                    final Object val;
                    if (optAndVal.length == 2) {

                        switch (optAndVal[1]) {
                            case "true":
                                val = true;
                                break;
                            case "false":
                                val = false;
                                break;
                            default:
                                Object tmpVal;
                                try {
                                    tmpVal = Long.parseLong(optAndVal[1]);
                                } catch (NumberFormatException ex) {
                                    tmpVal = optAndVal[1];
                                }
                                val = tmpVal;
                        }
                    } else {
                        val = null;
                    }
                    options.put(optAndVal[0], val);
                }
            }
        }

        public EventSourceOptions(final Map<String, Object> options) {
            if (options != null) {
                this.options.putAll(options);
            }
        }

        public Object getOption(final String name) {
            return options.get(name);
        }
        
        public boolean hasOption(final String name) {
            return options.containsKey(name);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            if (options.isEmpty()) {
                return sb.toString();
            } else {
                for (Map.Entry<String, Object> entry : options.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(entry.getKey());
                    if (entry.getValue() != null) {
                        sb.append("=");
                        sb.append(entry.getValue());
                    }
                }
                return sb.toString();
            }

        }
    }
}
