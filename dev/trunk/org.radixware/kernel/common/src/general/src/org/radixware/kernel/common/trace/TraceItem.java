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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.text.ParseException;
import org.radixware.kernel.common.defs.utils.MlsProcessor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;

public class TraceItem {

    public static enum EFormat {

        WITH_CONTEXT;
    }

    public final EEventSeverity severity;
    public final String code;
    public final List<String> words;
    public final String source;
    public final long time;
    public final boolean isSensitive;
    public final String context;
    protected final MlsProcessor mlsProcessor;
    public String stackTrace;
    private static final ThreadLocal<SimpleDateFormat> traceTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        // SimpleDateFormat is not thread safe

        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(getTimeFormat());
        }
    };
    private static final ThreadLocal<MessageFormat> traceItemFormat = new ThreadLocal<MessageFormat>() {
        // MessageFormat is not thread safe

        @Override
        protected MessageFormat initialValue() {
            return new MessageFormat("{1} {2,date,dd/MM/yy HH:mm:ss.SSS} : {3} : {4}");
        }
    };

    public TraceItem(final MlsProcessor mlsEvCodeProcessor, final EEventSeverity severity, final String code, final List<String> words, final String source) {
        this(mlsEvCodeProcessor, severity, code, words, source, false, System.currentTimeMillis());
    }

    public TraceItem(final MlsProcessor mlsEvCodeProcessor, final EEventSeverity severity, final String code, final List<String> words, final String source, final boolean isSensitive) {
        this(mlsEvCodeProcessor, severity, code, words, source, isSensitive, System.currentTimeMillis());
    }

    public TraceItem(final MlsProcessor mlsEvCodeProcessor, final EEventSeverity severity, final String code, final List<String> words, final String source, final boolean isSensitive, final long timeMillis) {
        this(mlsEvCodeProcessor, severity, code, words, source, null, isSensitive, timeMillis);
    }

    public TraceItem(final MlsProcessor mlsEvCodeProcessor, final EEventSeverity severity, final String code, final List<String> words, final String source, final String context, final boolean isSensitive, final long timeMillis) {
        this.mlsProcessor = mlsEvCodeProcessor;
        this.severity = severity;
        this.code = code;
        this.words = words;
        this.source = source;
        this.time = timeMillis;
        this.isSensitive = isSensitive;
        this.context = context;
    }

    public TraceItem(final EEventSeverity severity, final String asText) throws ParseException {
        final Object[] objs;
        code = null;
        mlsProcessor = null;
        objs = parseMess(asText);
        time = ((Date) objs[2]).getTime();
        source = (String) objs[3];
        words = new ArrStr((String) objs[4]);
        this.severity = severity;
        isSensitive = false;
        context = null;
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(final EIsoLanguage lang) {
        return toFormattedString(lang);
    }

    public String toFormattedString(EIsoLanguage lang, EFormat... format) {
        if (lang == null) {
            lang = mlsProcessor == null ? null : mlsProcessor.getDefLanguage();
        }
        if (isFormatOptionSet(format, EFormat.WITH_CONTEXT)) {
            return formatTraceMessage(severity, time, source, context, getMess(lang));
        } else {
            return formatTraceMessage(severity, time, source, null, getMess(lang));
        }
    }

    /**
     * Returns formatted string of trace item for subsequent writing it to trace file or displaying on screen.
     * limit - maximum length of string to return. When output exceeds limit function truncates it and appends '...' in the end. Negative or zero limit means no limit.
     */
    public static String formatTraceMessageLimited(int limit,
            final EEventSeverity severity,
            final long time,
            final String source,
            final String context,
            final String message) {
        StringWriter sw = new StringWriter();
        try {
            formatTraceMessageToWriter(sw, limit, severity, time, source, context, message);
        }
        catch(IOException ioex) { /*ignore*/ }
        return sw.toString();
    }

    public static String formatTraceMessage(final EEventSeverity severity,
            final long time,
            final String source,
            final String context,
            final String message) {
        StringWriter sw = new StringWriter();
        try {
            formatTraceMessageToWriter(sw, 0/*no limit*/, severity, time, source, context, message);
        }
        catch(IOException ioex) { /*ignore*/ }
        return sw.toString();
//        final StringBuilder s;
//        if (severity == null) {
//            s = new StringBuilder(traceTimeFormat.get().format(new Date(time)));
//        } else {
//            if (severity == EEventSeverity.DEBUG) {
//                s = new StringBuilder("@ ");
//            } else if (severity == EEventSeverity.EVENT) {
//                s = new StringBuilder("  ");
//            } else if (severity == EEventSeverity.WARNING) {
//                s = new StringBuilder("# ");
//            } else if (severity == EEventSeverity.ERROR) {
//                s = new StringBuilder("! ");
//            } else {
//                s = new StringBuilder("^ ");
//            }
//            s.append(traceTimeFormat.get().format(new Date(time)));
//        }
//        s.append(" : ");
//        if (source != null) {
//            s.append(source);
//            s.append(" : ");
//        }
//        if (context != null) {
//            s.append(context);
//            s.append(" : ");
//        }
//        s.append(message);
//        return s.toString();
    }

    /**
     * Writes formatted message to writer in a streamed manner.
     */
    public static int formatTraceMessageToWriter(final Writer writer,
            final EEventSeverity severity,
            final long time,
            final String source,
            final String context,
            final String message) throws IOException {
        return formatTraceMessageToWriter(writer, 0/*no limit*/, severity, time, source, context, message);
    }
    
    /**
     * Writes formatted message to writer in a streamed manner.
     * limit - maximum number of chars to write. When output exceeds limit function truncates it and appends '...' in the end. Negative or zero limit means no limit.
     */
    private static int formatTraceMessageToWriter(final Writer writer,
            final int limit,
            final EEventSeverity severity,
            final long time,
            final String source,
            final String context,
            final String message) throws IOException {
        int nCharsWritten = 0;
        if (severity == null) {
            String s = traceTimeFormat.get().format(new Date(time));
            writer.append(s);
            nCharsWritten += s.length();
        } else {
            if (severity == EEventSeverity.DEBUG) {
                writer.append("@ ");
            } else if (severity == EEventSeverity.EVENT) {
                writer.append("  ");
            } else if (severity == EEventSeverity.WARNING) {
                writer.append("# ");
            } else if (severity == EEventSeverity.ERROR) {
                writer.append("! ");
            } else {
                writer.append("^ ");
            }
            nCharsWritten += 2;
            String s = traceTimeFormat.get().format(new Date(time));
            writer.append(s);
            nCharsWritten += s.length();
        }
        writer.append(" : ");
        nCharsWritten += 3;
        if (source != null) {
            writer.append(source);
            writer.append(" : ");
            nCharsWritten += source.length() + 3;
        }
        if (context != null) {
            writer.append(context);
            writer.append(" : ");
            nCharsWritten += context.length() + 3;
        }
        if(limit <= 0 || nCharsWritten + message.length() < limit){
            writer.append(message);
            nCharsWritten += message.length();
        }
        else {
            writer.write(message, 0, limit - nCharsWritten);
            writer.write("...");
            nCharsWritten += limit - nCharsWritten + 3;
        }
        return nCharsWritten;
    }
    
    private boolean isFormatOptionSet(final EFormat[] options, EFormat target) {
        if (options != null) {
            for (EFormat option : options) {
                if (option == target) {
                    return true;
                }
            }
        }
        return false;
    }

    public final String getMess() {
        return getMess(mlsProcessor != null ? mlsProcessor.getDefLanguage() : EIsoLanguage.ENGLISH);
    }

    public String getMess(final EIsoLanguage lang) {
        return getMess(mlsProcessor, code, lang, words);
    }

    public static Object[] parseMess(final String asText) throws ParseException {
        return traceItemFormat.get().parse(asText);
    }

    static private final String PARAM_SYMBOL = "%";

    public static String getMess(final MlsProcessor mlsProcessor, final String code, final EIsoLanguage lang, final List<String> words) {
        if (code == null) {
            if (words != null && !words.isEmpty()) {
                return words.get(0);
            } else {
                return null;
            }
        }
        final String mess;
        try {
            mess = mlsProcessor != null ? mlsProcessor.getEventTitleByCode(code, lang) : null;
            if (mess == null || mess.isEmpty()) {
                return "??? (code=" + code + ", args = " + words + ")";
            }
        } catch (Throwable e) {
            return "??? (code=" + code + ", args = " + words + "), code interpretation exception: " + ExceptionTextFormatter.exceptionStackToString(e);
        }
        if (words == null || !mess.contains(PARAM_SYMBOL)) {
            return mess;
        }

        final StringBuilder res = new StringBuilder(mess);

        for (int i = res.indexOf(PARAM_SYMBOL); i != -1 && i < res.length() - 1; i = res.indexOf(PARAM_SYMBOL, i + 1)) {
            String replacement = null;
            switch (res.charAt(i + 1)) {
                case '1':
                    if (words.size() >= 1) {
                        replacement = words.get(0);
                    }
                    break;
                case '2':
                    if (words.size() >= 2) {
                        replacement = words.get(1);
                    }
                    break;
                case '3':
                    if (words.size() >= 3) {
                        replacement = words.get(2);
                    }
                    break;
                case '4':
                    if (words.size() >= 4) {
                        replacement = words.get(3);
                    }
                    break;
                case '5':
                    if (words.size() >= 5) {
                        replacement = words.get(4);
                    }
                    break;
                case '6':
                    if (words.size() >= 6) {
                        replacement = words.get(5);
                    }
                    break;
                case '7':
                    if (words.size() >= 7) {
                        replacement = words.get(6);
                    }
                    break;
                case '8':
                    if (words.size() >= 8) {
                        replacement = words.get(7);
                    }
                    break;
                case '9':
                    if (words.size() >= 9) {
                        replacement = words.get(8);
                    }
                    break;
            }
            if (replacement == null) {
                replacement = "null";
            }
            res.replace(i, i + 2, replacement);
            i += replacement.length() - 1;
        }
        return res.toString();
    }

    public boolean match(final TraceProfile profile) {
        if (profile == null) {
            return false;
        }
        return profile.itemMatch(this);
    }
    
    public void fillInStackTrace() {
        stackTrace = Thread.currentThread().getName() + "\n" + ExceptionTextFormatter.getCurrentStackAsStr();
    }
    
    public static String getTimeFormat() {
        return "dd/MM/yy HH:mm:ss.SSS";
    }
}
