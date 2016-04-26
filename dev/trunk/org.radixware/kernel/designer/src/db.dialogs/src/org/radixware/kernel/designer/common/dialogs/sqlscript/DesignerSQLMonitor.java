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
package org.radixware.kernel.designer.common.dialogs.sqlscript;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;
import org.openide.text.Annotation;
import org.openide.text.NbDocument.Annotatable;
import org.openide.util.Exceptions;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.radixware.kernel.common.sqlscript.parser.DefaultPauseObject;
import org.radixware.kernel.common.sqlscript.parser.IPauseObject;
import org.radixware.kernel.common.sqlscript.parser.SQLParseStatement;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLMonitor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class DesignerSQLMonitor implements SQLMonitor {

    private final IPauseObject pauseObject = new DefaultPauseObject();
//    private static final ConcurrentMap<String, AtomicInteger> SCRIPT_EXECUTION_COUNTER = new ConcurrentHashMap<String, AtomicInteger>();
    private volatile JTextComponent currentTextComponent;
    private volatile InputOutput inputOutput;
    private volatile Annotation currentLineAnnotation = null;
    private final Pattern locationPattern = Pattern.compile("\\d+\\:\\d+");
    private volatile AtomicBoolean cleared = new AtomicBoolean(false);
    private final ISQLScriptProvider scriptProvider;
    private volatile boolean annotateLines;

    public DesignerSQLMonitor(ISQLScriptProvider scriptProvider, String scriptName, String dbUrl, boolean annotateLines) {
        this.scriptProvider = scriptProvider;
        final String firstPart = "Execution of \"" + scriptName + "\"";
        final String lastPart = dbUrl != null ? (" on " + dbUrl) : "";
        this.inputOutput = IOProvider.getDefault().getIO(firstPart + lastPart, false);
        this.annotateLines = annotateLines;
        try {
            inputOutput.getOut().reset();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        inputOutput.select();
    }

    @Override
    public void setCurrentLine(int line) {
        if (annotateLines) {
            setCurrentLine(line, false);
        }
    }

    private void setCurrentLine(int line, boolean annotate) {
        removeCurrentAnnotation();
        final Object newCurrentObject = scriptProvider.getObjectAtLine(line);
        JTextComponent newCurrentComponent = scriptProvider.getObjectTextComponent(newCurrentObject);
        if (newCurrentComponent != currentTextComponent) {
            currentTextComponent = newCurrentComponent;
            scriptProvider.activateObject(newCurrentObject);
        }
        line = scriptProvider.convertScriptLineToObjectLine(newCurrentComponent, line);
        if (currentTextComponent != null && currentTextComponent.getDocument() instanceof BaseDocument) {
            final int offset = Utilities.getRowStartFromLineOffset((BaseDocument) currentTextComponent.getDocument(), line);
            if (offset == -1) {//invalid line number
                return;
            }
            currentTextComponent.setCaretPosition(offset);
            if (annotate && currentTextComponent.getDocument() instanceof Annotatable) {
                final Annotatable annotableDoc = (Annotatable) currentTextComponent.getDocument();
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        //sometimes this method is invoked after
                        //clear() invocation and add new annotation that will not be removed later.
                        //so we should not annotate the line if clear() has been called already.
                        if (cleared.get()) {
                            return;
                        }
                        removeCurrentAnnotation();
                        try {
                            currentLineAnnotation = new CurrentLineAnnotation();
                            annotableDoc.addAnnotation(annotableDoc.createPosition(offset), -1, currentLineAnnotation);
                        } catch (BadLocationException ex) {
                            DialogUtils.messageError(ex);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void printDbmsOutput(List<String> dbmsOutput) {
        inputOutput.select();
        for (String output : dbmsOutput) {
            inputOutput.getOut().println(output);
        }
    }

    @Override
    public boolean wishToHandleErrors() {
        return true;
    }

    @Override
    public void printErrors(List<String> errors) {
        inputOutput.select();
        for (String error : errors) {
            String[] errorAndLocation = splitError(error);
            if (errorAndLocation != null) {
                try {
                    OutputListener errorNavigator = createErrorNavigator(errorAndLocation[0]);
                    inputOutput.getErr().println("Error at " + errorAndLocation[0] + ":", errorNavigator);
                    inputOutput.getErr().println(errorAndLocation[1]);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                inputOutput.getErr().println(error);
            }
        }
    }

    /**
     * Tries to split error text to error location and error description
     *
     * @return
     */
    private String[] splitError(String error) {
        Matcher matcher = locationPattern.matcher(error);
        if (matcher.lookingAt()) {
            String[] locationAndError = new String[2];
            locationAndError[0] = matcher.group();
            locationAndError[1] = error.substring(locationAndError[0].length());
            return locationAndError;
        }
        return null;
    }

    private OutputListener createErrorNavigator(final String errorLocation) {

        Matcher matcher = locationPattern.matcher(errorLocation);
        if (matcher.lookingAt()) {
            String numbers[] = matcher.group().split(":");
            if (numbers.length != 2) {
                return null;
            }
            final int line = Integer.parseInt(numbers[0]) - 1;
            final int column = Integer.parseInt(numbers[1]) - 1;
            return new OutputListener() {

                @Override
                public void outputLineSelected(OutputEvent ev) {
                    //do nothing
                }

                @Override
                public void outputLineAction(OutputEvent ev) {
                    final Object object = scriptProvider.getObjectAtLine(line);
                    scriptProvider.activateObject(object);
                    scriptProvider.setPosition(object, line, column);
                }

                @Override
                public void outputLineCleared(OutputEvent ev) {
                    removeCurrentAnnotation();
                }
            };
        }
        return null;
    }

    @Override
    public boolean wishToHandleDbmsOutput() {
        return true;
    }

    @Override
    public IPauseObject getPauseObject() {
        return pauseObject;
    }

    /**
     * Removes any visualization created by this monitor
     */
    public void clear() {
        cleared.set(true);
        inputOutput.getOut().close();
        inputOutput.getErr().close();
        removeCurrentAnnotation();
    }

    private void removeCurrentAnnotation() {
        if (currentLineAnnotation != null) {
            if (SwingUtilities.isEventDispatchThread()) {
                currentLineAnnotation.detach();
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        removeCurrentAnnotation();
                    }
                });
            }
        }
    }

    @Override
    public void completed() {
        final String[] lines = scriptProvider.getScript().split("\\n");
        setCurrentLine(lines.length > 0 ? lines.length - 1 : 0);
    }

    @Override
    public void beforeStatement(SQLParseStatement statement) {
        if (annotateLines) {
            if (statement.getPosition() != null) {
                setCurrentLine(statement.getPosition().getSourceLine() - 1, true);
            }
        }
    }

    private static class CurrentLineAnnotation extends Annotation {

        @Override
        public String getAnnotationType() {
            return "CurrentExpressionLine";
        }

        @Override
        public String getShortDescription() {
            return "Next statement";
        }
    }
}
