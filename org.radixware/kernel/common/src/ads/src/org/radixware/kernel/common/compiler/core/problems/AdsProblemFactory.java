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
package org.radixware.kernel.common.compiler.core.problems;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.util.HashtableOfInt;

public class AdsProblemFactory extends DefaultProblemFactory {

    static boolean defailtTemplatesInited = false;
    private static final Object messageLoadLock = new Object();
    private final Map<String, String> messageRenameMap = new HashMap<>();

    public AdsProblemFactory() {
        loadMessageTemplates(Locale.getDefault(), messageTemplates);
    }

    public void addRenameRule(String from, String to) {
        messageRenameMap.put(from, to);
    }

    public static class MutableProblem extends DefaultProblem {

        private String message;

        public MutableProblem(char[] originatingFileName, String message, int id, String[] stringArguments, int severity, int startPosition, int endPosition, int line, int column) {
            super(originatingFileName, message, id, stringArguments, severity, startPosition, endPosition, line, column);
            this.message = super.getMessage();
        }

        public void setMessage(String newMessage) {
            this.message = newMessage;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

    }

    public CategorizedProblem createProblem(
            char[] originatingFileName,
            int problemId,
            String[] problemArguments,
            String[] messageArguments,
            int severity,
            int startPosition,
            int endPosition,
            int lineNumber,
            int columnNumber) {

        if (problemId == IProblem.MissingEnumConstantCase) {
            return new MutableProblem(
                    originatingFileName,
                    this.getLocalizedMessage(problemId, processMessageArguments(messageArguments)),
                    problemId,
                    processMessageArguments(problemArguments),
                    severity,
                    startPosition,
                    endPosition,
                    lineNumber,
                    columnNumber);
        }

        return new DefaultProblem(
                originatingFileName,
                this.getLocalizedMessage(problemId, processMessageArguments(messageArguments)),
                problemId,
                processMessageArguments(problemArguments),
                severity,
                startPosition,
                endPosition,
                lineNumber,
                columnNumber);
    }

    public CategorizedProblem createProblem(
            char[] originatingFileName,
            int problemId,
            String[] problemArguments,
            int elaborationId,
            String[] messageArguments,
            int severity,
            int startPosition,
            int endPosition,
            int lineNumber,
            int columnNumber) {
        if (problemId == IProblem.MissingEnumConstantCase) {
            return new MutableProblem(
                    originatingFileName,
                    this.getLocalizedMessage(problemId, elaborationId, processMessageArguments(messageArguments)),
                    problemId,
                    processMessageArguments(problemArguments),
                    severity,
                    startPosition,
                    endPosition,
                    lineNumber,
                    columnNumber);
        }
        return new DefaultProblem(
                originatingFileName,
                this.getLocalizedMessage(problemId, elaborationId, processMessageArguments(messageArguments)),
                problemId,
                processMessageArguments(problemArguments),
                severity,
                startPosition,
                endPosition,
                lineNumber,
                columnNumber);

    }

    private String[] processMessageArguments(String[] messageArguments) {
        if (messageArguments == null) {
            return null;
        }
        if (messageRenameMap.isEmpty()) {
            return messageArguments;
        }
        for (int i = 0; i < messageArguments.length; i++) {
            for (Map.Entry<String, String> e : messageRenameMap.entrySet()) {
                messageArguments[i] = messageArguments[i].replace(e.getKey(), e.getValue());
            }
        }
        messageRenameMap.clear();
        return messageArguments;
    }

    public static void loadMessageTemplates(Locale loc, HashtableOfInt templates) {
        synchronized (messageLoadLock) {
            if (!defailtTemplatesInited) {
                defailtTemplatesInited = true;
                ResourceBundle bundle = null;
                final String bundleName = "org.radixware.kernel.common.compiler.core.problems.problems"; //$NON-NLS-1$
                try {
                    bundle = ResourceBundle.getBundle(bundleName, loc);
                } catch (MissingResourceException e) {
                    Logger.getLogger(AdsProblemFactory.class.getName()).log(Level.SEVERE, "Missing resource : {0}.properties for locale {1}", new Object[]{bundleName.replace('.', '/'), loc});
                    throw e;
                }
                final Enumeration keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    try {

                        int messageID = Integer.parseInt(key);
                        int keyValue = keyFromID(messageID);
                        if (templates.containsKey(keyValue)) {
                            throw new IllegalArgumentException("Message with id " + messageID + "is already registered");
                        }
                        templates.put(keyValue, bundle.getString(key));
                    } catch (NumberFormatException e) {
                        // key ill-formed
                    } catch (MissingResourceException e) {
                        // available ID
                    }
                }
            }
        }
    }

    private final static int keyFromID(int id) {
        return id + 1; // keys are offsetted by one in table, since it cannot handle 0 key
    }
}
