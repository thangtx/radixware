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
package org.radixware.kernel.designer.dds.editors.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseDocument;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.sqlscript.ISQLScriptProvider;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SQLScriptExecutionSession;
import org.radixware.kernel.designer.common.dialogs.sqlscript.SelectedConnectionsManager;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;

public class DdsFunctionSqmlEditor extends SqmlEditorPanel {

    public DdsFunctionSqmlEditor() {
        super(DdsFunctionEditorKit.DDS_FUNCTION_MIME_TYPE);
    }

    @Override
    protected Collection<Object> getObjectsForLookup() {
        final List<Object> result = new ArrayList<Object>();
        result.addAll(Arrays.asList(new Object[]{new SQLScriptExecutionSession(), new SelectedConnectionsManager(), new FunctionScriptProviderFactory()}));
        result.addAll(super.getObjectsForLookup());
        return result;
    }

    private static class FunctionScriptProvider implements ISQLScriptProvider {

        private final String script;
        private final GenerationHandler handler;
        private final SqmlEditorPanel editor;

        public FunctionScriptProvider(DdsPlSqlObjectDef plSqlObject, SqmlEditorPanel editor) {
            handler = new GenerationHandler();
            script = DdsScriptGeneratorUtils.getPlSqlObjectScript(plSqlObject, handler);
            this.editor = editor;
        }

        @Override
        public String getScript() {
            return script;
        }

        @Override
        public Object getObjectAtLine(int line) {
            return handler.getObject(line);
        }

        @Override
        public void activateObject(final Object object) {
            if (object instanceof Scml) {
                if (SwingUtilities.isEventDispatchThread()) {
                    doActivateEditor((Scml) object);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doActivateEditor((Scml) object);
                        }
                    });
                }
            }
        }

        private void doActivateEditor(final Scml scml) {
            ScmlEditor scmlEditor = ScmlEditor.findScmlEditor(scml);
            if (scmlEditor == null) {
                EditorsManager.getDefault().open(scml);
                scmlEditor = ScmlEditor.findScmlEditor(scml);
            }
            final TopComponent tc = DialogUtils.findTopComponent(scmlEditor);
            if (tc != null && (!tc.isVisible() || !tc.isOpened())) {
                EditorsManager.getDefault().open(scml);
            }
            scmlEditor.getPane().requestFocusInWindow();
        }

        @Override
        public int convertScriptLineToObjectLine(Object object, int line) {
            return line - handler.getObjectStartLine(object);
        }

        @Override
        public JTextComponent getObjectTextComponent(Object object) {
            if (object instanceof Scml) {
                final ScmlEditor editor = ScmlEditor.findScmlEditor((Scml) object);
                if (editor != null) {
                    return editor.getPane();
                }
            }
            return null;
        }

        @Override
        public void setPosition(Object object, int absLine, int absColumn) {
            if (object instanceof Scml) {
                final ScmlEditor scmlEditor = ScmlEditor.findScmlEditor((Scml) object);
                if (scmlEditor != null) {
                    int offset = org.netbeans.editor.Utilities.getRowStartFromLineOffset(scmlEditor.getPane().getScmlDocument(), absLine - handler.getObjectStartLine(object));
                    offset += absColumn - 1;//1 is dirty trich to accaount for one tab inserted by codegenerator 
                    if (offset >= 0 && offset <= scmlEditor.getPane().getDocument().getLength()) {
                        scmlEditor.getPane().setCaretPosition(offset);
                    }
                }
            }
        }

        private static class GenerationHandler implements IScriptGenerationHandler {

            private final List<Integer> lineNumbers = new ArrayList<>();
            private final List<Object> objects = new ArrayList<>();

            @Override
            public void onGenerationStarted(Object object, CodePrinter cp) {
                lineNumbers.add(cp.getLineNumber(cp.length()));
                objects.add(object);
            }

            private int getIndex(final int absLine) {
                int index = Collections.binarySearch(lineNumbers, absLine);
                if (index < 0) {
                    index = (-index) - 2;
                    if (index < 0) {
                        index = 0;
                    }
                }
                return index;
            }

            public int getObjectStartLine(final Object object) {
                final int index = objects.indexOf(object);
                if (index >= 0 && index < objects.size()) {
                    return lineNumbers.get(index);
                }
                return -1;
            }

            public Object getObject(final int absLine) {
                final int index = getIndex(absLine);
                if (objects.size() > index) {
                    return objects.get(index);
                }
                return null;
            }

        }
    }

    private static class FunctionScriptProviderFactory implements ISQLScriptProvider.Factory {

        @Override
        public ISQLScriptProvider create(Lookup context) {
            SqmlEditorPanel sqmlEditor = context.lookup(SqmlEditorPanel.class);
            if (sqmlEditor == null) {
                throw new NullPointerException("No SqmlEditor in lookup");
            }
            DdsPlSqlObjectDef plSqlObject = getPlSqlObject(sqmlEditor);
            return new FunctionScriptProvider(plSqlObject, sqmlEditor);
        }

        private DdsPlSqlObjectDef getPlSqlObject(SqmlEditorPanel sqmlEditor) {
            Sqml sqml = sqmlEditor.getSource();
            Definition parent = sqml.getOwnerDefinition();
            while (parent != null) {
                if (parent instanceof DdsPlSqlObjectDef) {
                    return (DdsPlSqlObjectDef) parent;
                }
                parent = parent.getOwnerDefinition();
            }
            throw new IllegalStateException("Can't find parent Pl/Sql object");
        }
    }
}
