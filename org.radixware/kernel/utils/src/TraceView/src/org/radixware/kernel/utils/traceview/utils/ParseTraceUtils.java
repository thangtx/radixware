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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EFileFormat;
import org.radixware.kernel.utils.traceview.TraceViewSettings.ESeverity;

public class ParseTraceUtils {

    private static final Logger logger = Logger.getLogger(ParseTraceUtils.class.getName());

    private static boolean isContext(String str) {
        return str.startsWith("[") && str.endsWith("]") && !str.contains(" ");
    }

    private static boolean isSource(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!(Character.isLetter(str.charAt(i)) || Character.isDigit(str.charAt(i)) || str.charAt(i) == '.')) {
                return false;
            }
        }
        return true;
    }

    private static TraceEvent getEvent(ESeverity severity, Date date, String source, String context, String message, int eventIndex, ContextSet contextSet) {
        TraceEvent event = new TraceEvent();
        event.setSeverity(severity);
        event.setIndexedDate(new TraceEvent.IndexedDate(date, eventIndex));
        event.setSource(source);
        event.setContext(context);
        event.setMessage(message);
        event.setContextSet(contextSet);
        return event;
    }

    private static TraceEvent getEventWithContextFilter(ESeverity severity, Date date, String source, String context, String message, int eventIndex) {
        TraceEvent event = new TraceEvent();
        event.setSeverity(severity);
        event.setIndexedDate(new TraceEvent.IndexedDate(date, eventIndex));
        event.setSource(source);
        if (context.startsWith("[") && context.endsWith("]")) {
            event.setContext(context.substring(1, context.length() - 1));
        } else {
            event.setContext(context);
        }
        event.setMessage(message);
        return event;
    }
    
    private static TraceEvent getEventWithContextFilter(ESeverity severity, Date date, String source, String context, String message, int eventIndex, ContextSet contextSet) {
        
        TraceEvent event = getEventWithContextFilter(severity,date,source, context, message, eventIndex);
        event.setContextSet(contextSet);
        return event;
    }

    private static TraceEvent isNewEvent(String line, int index) {
        ESeverity severity = null;
        Date date = null;

        try {
            if (line.startsWith("@ ") || line.startsWith("# ") || line.startsWith("! ") || line.startsWith("^ ") || line.startsWith("  ")) {
                severity = ESeverity.getSeverity(line.charAt(0) + " ");
                String el[] = line.substring(2).split(" : ");
                date = TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().parse(el[0]);
                if (el.length == 2) {
                    return getEventWithContextFilter(severity, date, "", "", el[1], index);
                } else if (el.length == 3) {
                    if (isSource(el[1])) {
                        return getEventWithContextFilter(severity, date, el[1], "", el[2], index);
                    } else if (isContext(el[1])) {
                        return getEventWithContextFilter(severity, date, "", el[1], el[2], index);
                    } else {
                        return getEventWithContextFilter(severity, date, "", "", el[1] + el[2], index);
                    }
                } else if (el.length > 3) {
                    int i;
                    String source = isSource(el[1]) ? el[1] : "";
                    String context;
                    if (source.isEmpty()) {
                        i = isContext(el[1]) ? 2 : 1;
                        context = i == 2 ? el[1] : "";
                    } else {
                        i = isContext(el[2]) ? 3 : 2;
                        context = i == 2 ? "" : el[2];
                    }
                    StringBuilder message = new StringBuilder();
                    for (; i < el.length; i++) {
                        message.append(el[i]);
                    }
                    return getEventWithContextFilter(severity, date, source, context, message.toString(), index);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    private static void maxSizeElementCompare(TraceEvent traceEvent, TraceEvent maxSizeEvent) {
        if (maxSizeEvent != null) {
            if (traceEvent.getSource().length() > maxSizeEvent.getSource().length()) {
                maxSizeEvent.setSource(traceEvent.getSource());
            }
            if (traceEvent.getContext().length() > maxSizeEvent.getContext().length()) {
                maxSizeEvent.setContext(traceEvent.getContext());
            }
            if (traceEvent.getMessage().length() > maxSizeEvent.getMessage().length()) {
                maxSizeEvent.setMessage(traceEvent.getMessage());
            }
        }
    }

    private static void addEvent(TraceEvent event, boolean splitByContext, List<TraceEvent> events, TraceEvent maxSizeEvent, File file, Set<String> uniqueSources, ContextSet contextSet) {
        if (event != null) {
            String[] contextArray = event.getContext().split(",");
            String source = event.getSource();
            if (splitByContext && contextArray.length > 1) {
                for (String con : contextArray) {
                    TraceEvent ev = getEventWithContextFilter(event.getSeverity(), event.getIndexedDate().getDate(), source, con, event.getMessage(), events.size(), contextSet);
                    ev.setFile(file);
                    events.add(ev);
                }
            } else {
                maxSizeElementCompare(event, maxSizeEvent);
                event.setFile(file);
                event.setContextSet(contextSet);
                events.add(event);

            }

            if (!uniqueSources.contains(source)) {
                uniqueSources.add(source);
            }
            
        }
    }

    public static void parseTraceFile(ContextForParsing ctx) {
        List<TraceEvent> events = ctx.getEvents();
        TraceEvent maxSizeEvent = ctx.getMaxSizeEvent();
        Set<String> uniqueSources = ctx.getSortedSetOfUniqueSources();
        File file = ctx.getFile();

        try (ProgressInputStream pis = new ProgressInputStream(new FileInputStream(file), file.length());
                BufferedReader br = new BufferedReader(new InputStreamReader(pis, TraceViewUtils.detectEncoding(file)))) {
            String line;
            TraceEvent event = null;
            ArrayList<String> namesContexts = new ArrayList<>();
            ContextSet current = new ContextSet();
            TraceEvent newEvent;
            boolean splitByContext = ctx.splitByContext();
            ContextSet cs = new ContextSet();
            while ((line = br.readLine()) != null) {

                
                cs = new ContextSet(current);
                if ((newEvent = isNewEvent(line, events.size())) != null) {           
                    addEvent(event, splitByContext, events, maxSizeEvent, file, uniqueSources, cs);
                    ctx.setProgress(pis.getProgressInPercentage());
                    event = newEvent;
                    String contexts[] = newEvent.getContext().split(",");
                    for (int i = 0; i < contexts.length; i++){
                        int ind = namesContexts.indexOf(contexts[i]);
                        if (ind == -1) {
                            namesContexts.add(contexts[i]);
                            current.add();
                            cs.add();
                            cs.set(cs.size - 1, true);
                        } else cs.set(ind, true);
                        
                    }
                } else {
                    if (event != null) {
                        event.addMessage("\n").addMessage(line);
                    } else {
                        logger.log(Level.SEVERE, "Violation of syntax: {0}", line);
                    }
                }
            }
            addEvent(event, splitByContext, events, maxSizeEvent, file, uniqueSources, cs);
            ctx.setProgress(pis.getProgressInPercentage());
            ctx.setArrayListName(namesContexts);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public static void parseTraceFileXml(ContextForParsing ctx) {
        List<TraceEvent> events = ctx.getEvents();
        TraceEvent maxSizeEvent = ctx.getMaxSizeEvent();
        Set<String> uniqueSources = ctx.getSortedSetOfUniqueSources();

        try (ProgressInputStream pis = new ProgressInputStream(new FileInputStream(ctx.getFile()), ctx.getFile().length())) {
            XMLStreamReader xmlr = XMLInputFactory.newInstance().createXMLStreamReader(pis);

            StringBuilder context = new StringBuilder();
            StringBuilder message = new StringBuilder();
            ESeverity severity = null;
            String source = null;
            Date date = null;
            ArrayList<String> namesContexts = new ArrayList<>();
            ContextSet current = new ContextSet();
            ContextSet cs = new ContextSet();

            boolean isMessage = false;

            while (xmlr.hasNext()) {
                xmlr.next();
                if (xmlr.isStartElement()) {
                    switch (xmlr.getLocalName()) {
                        case "Event":
                            cs = new ContextSet(current);
                            for (int i = 0; i < xmlr.getAttributeCount(); i++) {
                                switch (xmlr.getAttributeLocalName(i)) {
                                    case "Severity":
                                        severity = ESeverity.getSeverity(Integer.parseInt(xmlr.getAttributeValue(i)));
                                        break;
                                    case "RaiseTime":
                                        try {
                                            date = TraceViewSettings.DATE_FORMAT_XML.get().parse(xmlr.getAttributeValue(i));
                                        } catch (ParseException e) {
                                            date = TraceViewSettings.DATE_FORMAT_XML_NO_MS.get().parse(xmlr.getAttributeValue(i));
                                        }
                                        break;
                                    case "Component":
                                        source = xmlr.getAttributeValue(i);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case "Message":
                            isMessage = true;
                            break;
                        case "EventContext":
                            StringBuilder tmp = new StringBuilder();
                            String str ="";
                            for (int i = 0; i < xmlr.getAttributeCount(); i++) {  
                                switch (xmlr.getAttributeLocalName(i)) {
                                    case "Type":
                                        tmp.append(xmlr.getAttributeValue(i));
                                        str += xmlr.getAttributeValue(i);
                                        break;
                                    case "ID":
                                        tmp.append("~");
                                        tmp.append(xmlr.getAttributeValue(i));
                                        str += '~' + xmlr.getAttributeValue(i);
                                        break;
                                }
                            }
                            int index = namesContexts.indexOf(str);
                            if (index == -1) {
                                namesContexts.add(str);
                                current.add();
                                cs.add();
                                cs.set(cs.size - 1, true);
                            } else cs.set(index, true);
                            if (context.length() > 0) {
                                context.append(",");
                            }
                            context.append(tmp);
                            break;
                    }
                } else if (xmlr.isEndElement()) {
                    switch (xmlr.getLocalName()) {
                        case "Event":
                            String mes = parseSafeXmlString(message.toString());

                            if (source == null) {
                                source = "";
                            }

                            String[] contextArray = context.toString().split(",");

                            if (contextArray.length > 1 && ctx.splitByContext()) {
                                for (String ctxt : contextArray) {
                                    events.add(getEvent(severity, date, source, ctxt, mes, events.size(),cs));
                                }
                            } else {
                                TraceEvent event = getEvent(severity, date, source, context.toString(), mes, events.size(),cs);
                                events.add(event);
                                maxSizeElementCompare(event, maxSizeEvent);
                            }

                            if (!uniqueSources.contains(source)) {
                                uniqueSources.add(source);
                            }

                            ctx.setProgress(pis.getProgressInPercentage());
                            context.setLength(0);
                            message.setLength(0);
                            severity = null;
                            source = null;
                            date = null;
                            break;
                        case "Message":
                            isMessage = false;
                            break;
                    }
                } else if (xmlr.hasText() && isMessage && xmlr.getText().trim().length() > 0) {
                    message.append(xmlr.getText());
                }
            }
            ctx.setArrayListName(namesContexts);
            xmlr.close();
        } catch (IOException | XMLStreamException | ParseException | NumberFormatException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public static EFileFormat determineTheFileFormat(File traceFile) {
        byte[] buff = new byte[200];
        try (FileInputStream fis = new FileInputStream(traceFile)) {
            fis.read(buff, 0, 200);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        if (new String(buff).replaceAll(" ", "").contains(TraceViewSettings.PREFIX_XML_SCHEMA)) {
            return EFileFormat.XML;
        } else {
            try {
                TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().parse((new String(buff)).substring(2).split(" : ")[0]);
                return EFileFormat.BINARY;
            } catch (ParseException ex) {
                return EFileFormat.UNIDENTIFIED;
            }
        }
    }

    /**
     * Convert string with escaped characters into source string. Backslash
     * character and followed character code in UTF-8 (for hexadecimal digits)
     * will be replaced with single character. Double backslash characters will
     * be replaced with single backslash.
     * http://en.wikipedia.org/wiki/Valid_characters_in_XML
     *
     * @param s string from XML document
     * @return decoded string with unescaped characters
     */
    public static String parseSafeXmlString(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        final StringBuilder sourceStringBuilder = new StringBuilder();
        final char[] chars = s.toCharArray();
        final char[] hexNumber = new char[4];
        int digitIndex = 0;
        boolean escape = false;
        for (int i = 0, count = chars.length; i < count; i++) {
            final char c = chars[i];
            if (escape) {
                if (Character.digit(c, 16) > -1) {
                    hexNumber[digitIndex] = c;
                    digitIndex++;
                    if (digitIndex == 4) {//build character from code point
                        final int charCode = Integer.parseInt(String.valueOf(hexNumber), 16);
                        final char[] parsedChars = Character.toChars(charCode);
                        if (parsedChars.length != 1) {//malformed sequence - save as is
                            sourceStringBuilder.append('\\');
                            sourceStringBuilder.append(hexNumber, 0, 4);
                        } else {
                            sourceStringBuilder.append(parsedChars[0]);
                        }
                        digitIndex = 0;
                        escape = false;
                    }
                } else if (c == '\\' && digitIndex == 0) {
                    sourceStringBuilder.append('\\');
                    escape = false;
                } else {//malformed sequence - save as is
                    sourceStringBuilder.append('\\');
                    if (digitIndex > 0) {
                        sourceStringBuilder.append(hexNumber, 0, digitIndex);
                        digitIndex = 0;
                    }
                    sourceStringBuilder.append(c);
                    escape = false;
                }
            } else if (c == '\\') {
                escape = true;
            } else {
                sourceStringBuilder.append(c);
            }
        }
        if (escape) {//malformed sequence - save as is
            sourceStringBuilder.append('\\');
            if (digitIndex > 0) {
                sourceStringBuilder.append(hexNumber, 0, digitIndex);
            }
        }
        return sourceStringBuilder.toString();
    }
}
