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

package org.radixware.kernel.common.check;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.FileUtils;

/**
 * Check problem.
 */
public class RadixProblem {

    public interface ProblemFixSupport {

        public interface Fix {

            public void fix();

            public String getDescription();
        }

        public abstract boolean canFix(int code, List<Fix> fixes);
    }

    public static abstract class WarningSuppressionSupport {

        private int[] suppressedWarnings = null;
        protected final RadixObject owner;

        protected WarningSuppressionSupport(RadixObject owner) {
            this.owner = owner;
        }

        public boolean suppressWarnings(int code, boolean suppress) {
            synchronized (this) {
                if (suppress) {
                    if (suppressedWarnings == null) {
                        suppressedWarnings = new int[]{code};
                        if (owner != null) {
                            owner.setEditState(RadixObject.EEditState.MODIFIED);
                        }
                        return true;
                    } else {
                        for (int i = 0; i < suppressedWarnings.length; i++) {
                            if (suppressedWarnings[i] == code) {
                                return false;
                            }
                        }
                        int[] arr = new int[suppressedWarnings.length + 1];
                        System.arraycopy(suppressedWarnings, 0, arr, 0, suppressedWarnings.length);
                        arr[suppressedWarnings.length] = code;
                        if (owner != null) {
                            owner.setEditState(RadixObject.EEditState.MODIFIED);
                        }
                        suppressedWarnings = arr;
                        return true;
                    }
                } else {
                    if (suppressedWarnings == null) {
                        return false;
                    } else {
                        for (int i = 0; i < suppressedWarnings.length; i++) {
                            if (suppressedWarnings[i] == code) {
                                if (suppressedWarnings.length == 1) {
                                    suppressedWarnings = null;
                                } else {
                                    int[] arr = new int[suppressedWarnings.length - 1];
                                    System.arraycopy(suppressedWarnings, 0, arr, 0, i);
                                    if (i + 1 < suppressedWarnings.length) {
                                        System.arraycopy(suppressedWarnings, i + 1, arr, i, suppressedWarnings.length - (i + 1));
                                    }
                                    suppressedWarnings = arr;
                                }
                                if (owner != null) {
                                    owner.setEditState(RadixObject.EEditState.MODIFIED);
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                }
            }
        }

        public boolean isWarningSuppressed(int code) {
            synchronized (this) {
                if (suppressedWarnings == null) {
                    return false;
                }
                for (int i = 0; i < suppressedWarnings.length; i++) {
                    if (suppressedWarnings[i] == code) {
                        return true;
                    }
                }
                return false;
            }
        }

        public abstract boolean canSuppressWarning(int code);

        public int[] getSuppressedWarnings() {
            if (suppressedWarnings == null) {
                return new int[0];
            } else {
                return Arrays.copyOf(suppressedWarnings, suppressedWarnings.length);
            }
        }

        protected void setSuppressedWarnings(int[] warnings) {
            synchronized (this) {
                if (warnings == null || warnings.length == 0) {
                    if (suppressedWarnings != null) {
                        suppressedWarnings = null;
                        if (owner != null) {
                            owner.setEditState(RadixObject.EEditState.MODIFIED);
                        }
                    }
                } else {
                    suppressedWarnings = Arrays.copyOf(warnings, warnings.length);
                    if (owner != null) {
                        owner.setEditState(RadixObject.EEditState.MODIFIED);
                    }
                }
            }
        }

        public boolean isEmpty() {
            return suppressedWarnings == null || suppressedWarnings.length == 0;
        }
    }

    public static class ProblemDescription {

        private final String message;
        private final String description;
        private final int code;

        public ProblemDescription(String message, String description, int code) {
            this.message = message;
            this.description = description == null ? message : description;
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final class Problems {

        private static final Map<Class<? extends RadixObject>, Problems> problemDescriptions = new HashMap<Class<? extends RadixObject>, Problems>();
        private final List<ProblemDescription> problems = new LinkedList<ProblemDescription>();
        private final Class clazz;

        private Problems(Class clazz, InputStream stream) {
            this.clazz = clazz;
            if (stream != null) {
                try {
                    String text = FileUtils.readTextStream(stream, FileUtils.XML_ENCODING);
                    String[] lines = text.split("\n");
                    for (int i = 0; i < lines.length; i++) {
                        int index = lines[i].indexOf('=');
                        if (index > 0) {
                            try {
                                int code = Integer.parseInt(lines[i].substring(0, index));
                                String messageString = lines[i].substring(index + 1).replace("\\^", "^");
                                int sep = messageString.indexOf("^");
                                String desc;
                                String msg;
                                if (sep > 0) {
                                    msg = messageString.substring(0, sep);
                                    desc = messageString.substring(sep + 1);
                                } else {
                                    msg = messageString;
                                    desc = null;
                                }
                                problems.add(new ProblemDescription(msg, desc, code));
                            } catch (NumberFormatException ex) {
                                Logger.getLogger(RadixProblem.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(RadixProblem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private static Problems create(Class<? extends RadixObject> c) {
            InputStream stream = c.getResourceAsStream(c.getSimpleName() + "_problems.properties");
            if (stream == null) {
                return null;
            } else {
                return new Problems(c, stream);
            }
        }

        private Problems findBase() {
            Class sc = clazz.getSuperclass();
            if (RadixObject.class.isAssignableFrom(sc)) {
                return find(sc);
            } else {
                return null;
            }
        }

        private static Problems find(Class<? extends RadixObject> c) {
            synchronized (problemDescriptions) {
                LinkedList<Class> unfound = null;
                for (Class cc = c; cc != null && RadixObject.class.isAssignableFrom(cc); cc = cc.getSuperclass()) {
                    Problems instance = problemDescriptions.get(cc);
                    if (instance != null) {
                        return instance;
                    } else {
                        instance = create(cc);
                        if (instance != null) {
                            problemDescriptions.put(cc, instance);
                            if (unfound != null) {
                                for (Class cc2 : unfound) {
                                    problemDescriptions.put(cc2, instance);
                                }
                            }
                            return instance;
                        } else {
                            if (unfound == null) {
                                unfound = new LinkedList<Class>();
                            }
                            unfound.add(cc);
                        }
                    }
                }


                return null;
            }
        }

        public final ProblemDescription get(int code) {
            for (ProblemDescription d : problems) {
                if (d.code == code) {
                    return d;
                }
            }
            Problems base = findBase();
            if (base != null) {
                return base.get(code);
            } else {
                return null;
            }
        }
    }

    public static final String getProblemDescription(RadixObject object, int code) {
        Problems problemSet = Problems.find(object.getClass());
        if (problemSet == null) {
            return "<No problem description found>";
        } else {
            ProblemDescription desc = problemSet.get(code);
            if (desc == null) {
                return "<No problem description>";
            } else {
                return desc.getDescription();
            }
        }
    }

    public static final String getProblemMessage(RadixObject object, int code) {
        Problems problemSet = Problems.find(object.getClass());
        if (problemSet == null) {
            return "<No problem description found>";
        } else {
            ProblemDescription desc = problemSet.get(code);
            if (desc == null) {
                return "<No problem description>";
            } else {
                return desc.getMessage();
            }
        }
    }

    private static final String getProblemMessage(RadixObject object, int code, Object[] arguments) {
        String message = getProblemMessage(object, code);
        if (arguments != null && arguments.length > 0) {
            return MessageFormat.format(message, arguments);
        } else {
            return message;
        }
    }

    public static final class Codes {

        public static final int UNKNOWN = -11;
    }

    /**
     * Problem annotation.
     * Used, for example, to specify exact position in SCML.
     */
    public interface IAnnotation {
    }

    public enum ESeverity {

        WARNING,
        ERROR
    }

    public enum EProblemKind {
        SPELLING, OTHER
    }

    private final RadixObject source;
    private final String message;
    private ESeverity severity;
    private IAnnotation annotation;
    private final int code;
    private int nativeCode = -1;

    protected RadixProblem(ESeverity severity, RadixObject source, String message, IAnnotation annotation) {
        this(Codes.UNKNOWN, severity, source, message, annotation);
    }

    protected RadixProblem(int code, ESeverity severity, RadixObject source, IAnnotation annotation, Object[] messageArguments) {
        this(code, severity, source, getProblemMessage(source, code, messageArguments), annotation);
    }

    protected RadixProblem(int code, ESeverity severity, RadixObject source, String message, IAnnotation annotation) {
        this.source = source;
        this.message = message;
        this.severity = severity;
        this.annotation = annotation;
        this.code = code;
    }

    public int getNativeCode() {
        return nativeCode;
    }

    public void setNativeCode(int nativeCode) {
        this.nativeCode = nativeCode;
    }

    public boolean isSuppressed() {
        if (code == Codes.UNKNOWN) {
            return false;
        }
        WarningSuppressionSupport support = source.getWarningSuppressionSupport(false);
        if (support == null) {
            return false;
        }
        return support.isWarningSuppressed(code);
    }

    public String getMessage() {
        return message;
    }

    public IAnnotation getAnnotation() {
        return annotation;
    }

    /**
     * Get problem source. Can be Scml.Tag, so, do not use getSource().getQualifiedName()
     */
    public RadixObject getSource() {
        return source;
    }

    public int getCode() {
        return code;
    }

    public EProblemKind getKind() {
        return EProblemKind.OTHER;
    }

    public ESeverity getSeverity() {
        return severity;
    }

    public static final class Factory {

        private Factory() {
        }

        public static RadixProblem newWarning(RadixObject source, String message) {
            final IAnnotation emptyAnnotation = ProblemAnnotationFactory.newEmptyAnnotation();
            return new RadixProblem(ESeverity.WARNING, source, message, emptyAnnotation);
        }

        public static RadixProblem newWarning(RadixObject source, int code, String... args) {
            final IAnnotation emptyAnnotation = ProblemAnnotationFactory.newEmptyAnnotation();
            return new RadixProblem(code, ESeverity.WARNING, source, emptyAnnotation, args);
        }

//        public static RadixProblem newWarning(int code, RadixObject source, String message) {
//            final IAnnotation emptyAnnotation = ProblemAnnotationFactory.newEmptyAnnotation();
//            return new RadixProblem(code, ESeverity.WARNING, source, message, emptyAnnotation);
//        }
        public static RadixProblem newWarning(int code, RadixObject source, String... messageArgs) {
            final IAnnotation emptyAnnotation = ProblemAnnotationFactory.newEmptyAnnotation();
            return new RadixProblem(code, ESeverity.WARNING, source, emptyAnnotation, messageArgs);
        }

        public static RadixProblem newError(RadixObject source, String message) {
            final IAnnotation emptyAnnotation = ProblemAnnotationFactory.newEmptyAnnotation();
            return new RadixProblem(ESeverity.ERROR, source, message, emptyAnnotation);
        }

        public static RadixProblem newSpellingError(RadixObject source, String message, String word, EIsoLanguage language) {
            final IAnnotation annotation = ProblemAnnotationFactory.newSpellingAnnotation(word, language);
            return new SpellcheckProblem(Codes.UNKNOWN, ESeverity.ERROR, source, message, annotation);
        }

        public static RadixProblem newWarning(RadixObject source, String message, IAnnotation annotation) {
            return new RadixProblem(ESeverity.WARNING, source, message, annotation);
        }

        public static RadixProblem newWarning(int code, RadixObject source, String message, IAnnotation annotation) {
            return new RadixProblem(code, ESeverity.WARNING, source, message, annotation);
        }

        public static RadixProblem newWarning(int code, RadixObject source, IAnnotation annotation, String... messageArgs) {
            return new RadixProblem(code, ESeverity.WARNING, source, annotation, messageArgs);
        }

        public static RadixProblem newError(RadixObject source, String message, IAnnotation annotation) {
            return new RadixProblem(ESeverity.ERROR, source, message, annotation);
        }

        public static RadixProblem newError(int code, RadixObject source, String... messageArgs) {
            final IAnnotation emptyAnnotation = ProblemAnnotationFactory.newEmptyAnnotation();
            return new RadixProblem(code, ESeverity.ERROR, source, emptyAnnotation, messageArgs);
        }
    }
}
