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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.designer.ads.editors.clazz.sql.AdsSqlClassCodeEditorActions.InsertCursorFieldTagToEditorAction;
import org.radixware.kernel.designer.ads.editors.clazz.sql.panels.IfTagEditor;
import org.radixware.kernel.designer.ads.editors.clazz.sql.panels.ParameterTagEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.TagEditorFactory;

import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.InsertDbFuncCallAcion;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.InsertDbNameAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.InsertEnumerationItemValueTag;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.InsertIdAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.InsertPreprocessorAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.InsertXPathTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlTopLevelActions.PreviewSqlAction;


public class AdsSqlClassCodeEditor extends SqmlEditorPanel {

    private ISqlDef sqmlSorceDef;
    DocumentListener listener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            actualize();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            actualize();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    };

    private void actualize() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // sqlClass.synchronizeFieldsAndParams();
            }
        });
    }

    public AdsSqlClassCodeEditor() {
        super();
        getPane().addPropertyChangeListener("tag-content", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                actualize();
            }
        });
    }

    @Override
    public void update() {
        super.update();
        actualize();
    }

    public ISqlDef getSqlClass() {
        return sqmlSorceDef;
    }

    @Override
    protected ScmlToolBarAction[] createScmlToolBarActions() {
        return new ScmlToolBarAction[]{
            new InsertIdAction(this),
            new InsertEnumerationItemValueTag(this),
            new InsertDbNameAction(this),
            new InsertDbFuncCallAcion(this),
            new InsertXPathTagAction(this),
            new InsertCursorFieldTagToEditorAction(this),
            new PreviewSqlAction(this),
            new InsertPreprocessorAction(this),
            new SqmlTopLevelActions.ToggleCheckSqlAction(this)};
    }

    public void open(ISqlDef sqmlSorceDef, EditorOpenInfo info) {
        ISqlDef oldValue = this.sqmlSorceDef;
        this.sqmlSorceDef = sqmlSorceDef;
        getPane().getDocument().removeDocumentListener(listener);
        open(this.sqmlSorceDef.getSqml(), info);
        getPane().getDocument().addDocumentListener(listener);
        firePropertyChange("sqlClass", oldValue, this.sqmlSorceDef);
    }

    @Override
    protected TagEditorFactory createTagEditorFactory() {
        return new AdsSqlClassTagEditorFactory(this);
    }

    protected static class AdsSqlClassTagEditorFactory extends SqmlEditorPanel.SqmlTagEditorFactory {

        AdsSqlClassCodeEditor editor;

        public AdsSqlClassTagEditorFactory(AdsSqlClassCodeEditor editor) {
            super(editor);
            this.editor = editor;
        }

        @Override
        public TagEditor createTagEditor(Tag tag) {
            if (tag instanceof ParameterTag) {
                return new ParameterTagEditor();
            } else if (tag instanceof IfParamTag) {
                return new IfTagEditor();
            }
            return super.createTagEditor(tag);
        }
    }
}
