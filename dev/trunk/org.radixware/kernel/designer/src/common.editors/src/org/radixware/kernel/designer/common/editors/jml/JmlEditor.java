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

package org.radixware.kernel.designer.common.editors.jml;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.jml.*;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.ErrorVTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.TagEditorFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.TagPopupFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTagFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertCheckLicenseTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertDataTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertDbNameAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertEventCodeTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertIdTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertInternalPropertyAccessTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertInvocationTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertLibUserFuncTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertNlsTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertReadLicenseTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertTaskTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertTypeDeclarationTagAction;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlTopLevelActions.InsertWidgetTagAction;
import org.radixware.kernel.designer.common.editors.jml.editors.EnumerationItemTagEditor;
import org.radixware.kernel.designer.common.editors.jml.editors.EventCodeTagEditor;
import org.radixware.kernel.designer.common.editors.jml.editors.LocalizedStringTagEditor;
import org.radixware.kernel.designer.common.editors.jml.editors.TaskEditor;
import org.radixware.kernel.designer.common.editors.jml.vtag.*;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class JmlEditor extends ScmlEditor<Jml> {

    private Definition context;

    /**
     * Open jml in editor. In this case, JmlEditor will find first owner of Jml
     * which is instance of AdsDefinition and use it as context.
     *
     * @param jml
     */
    public void open(final Jml jml) {
        open(jml, null, null);
    }

    /**
     * Open jml in editor with given context and OpenInfo. OpenInfo can contain
     * information about required position in editor.
     */
    public void open(final Jml jml, Definition context, final OpenInfo openInfo) {
        super.open(jml, openInfo);
        if (context == null) {
            RadixObject owner = jml.getContainer();
            while (owner != null && !(owner instanceof Definition)) {
                owner = owner.getContainer();
            }
            context = (Definition) owner;
        }
        this.context = context;
        reloadToolbarActions();
    }

    /**
     * Open jml in editor. OpenInfo can contain information about required
     * position in editor. Context will be found automatically - it would be the
     * first owner of type AdsDefinition.
     */
    @Override
    public void open(final Jml source, final OpenInfo openInfo) {
        open(source, null, openInfo);
    }

    public JmlEditor() {
        super(JML_MIME_TYPE);
        setupActions();
    }

    private void setupActions() {
        getPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), CompileJmlAction.COMPILE_JML_ACTION);
        getPane().getActionMap().put(CompileJmlAction.COMPILE_JML_ACTION, new CompileJmlAction(this));
        getPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK), GoToBodyAction.GO_TO_BODY_ACTION);
        getPane().getActionMap().put(GoToBodyAction.GO_TO_BODY_ACTION, new GoToBodyAction(this));
    }

    @Override
    protected ScmlToolBarAction[] createScmlToolBarActions() {
        final ScmlToolBarAction[] actions = new ScmlToolBarAction[]{
            new InsertIdTagAction(this),
            new InsertDbNameAction(this),
            new InsertTypeDeclarationTagAction(this),
            new InsertNlsTagAction(this),
            new InsertEventCodeTagAction(this),
            new InsertInvocationTagAction(this),
            new InsertTaskTagAction(this),
            new InsertReadLicenseTagAction(this),
            new InsertCheckLicenseTagAction(this),
            new InsertDataTagAction(this),
            new InsertWidgetTagAction(this),
            new InsertInternalPropertyAccessTagAction(this),
            new InsertLibUserFuncTagAction(this)};
        return actions;
    }

    public Definition getContext() {
        return context;
    }

    @Override
    protected TagEditorFactory createTagEditorFactory() {
        return new JmlTagEditorFactory(this);
    }

    @Override
    protected TagPopupFactory createTagPopupFactory() {
        return super.createTagPopupFactory();
    }

    @Override
    protected VTagFactory createVTagFactory() {
        return new JmlVTagFactory();
    }

    public static class JmlVTagFactory implements VTagFactory {

        @Override
        public VTag createVTag(final Tag tag) {
            if (tag instanceof JmlTagInvocation) {
                return new VJmlTagInvocation((JmlTagInvocation) tag);
            }
            if (tag instanceof JmlTagId) {
                return new VJmlTagId((JmlTagId) tag);
            }
            if (tag instanceof JmlTagEventCode) {
                return new VJmlTagEventCode((JmlTagEventCode) tag);
            }
            if (tag instanceof JmlTagLocalizedString) {
                return new VJmlTagLocalizedString((JmlTagLocalizedString) tag);
            }
            if (tag instanceof JmlTagLiteral) {
                return new VJmlTagLiteral((JmlTagLiteral) tag);
            }
            if (tag instanceof JmlTagTask) {
                return new VJmlTagTask((JmlTagTask) tag);
            }
            if (tag instanceof JmlTagTypeDeclaration) {
                return new VJmlTagTypeDeclaration((JmlTagTypeDeclaration) tag);
            }
            if (tag instanceof JmlTagPin) {
                return new VJmlTagPin((JmlTagPin) tag);
            }
            if (tag instanceof JmlTagData) {
                return new VJmlTagData((JmlTagData) tag);
            }
            if (tag instanceof JmlTagDbEntity) {
                return new VJmlTagDbEntity((JmlTagDbEntity) tag);
            }
            if (tag instanceof JmlTagReadLicense) {
                return new VJmlTagReadLicense((JmlTagReadLicense) tag);
            }
            if (tag instanceof JmlTagCheckLicense) {
                return new VJmlTagCheckLicense((JmlTagCheckLicense) tag);
            }
            return new ErrorVTag(tag);
        }
    }

    protected static class JmlTagEditorFactory implements TagEditorFactory {

        private final JmlEditor jmlEditor;

        public JmlTagEditorFactory(final JmlEditor jmlEditor) {
            this.jmlEditor = jmlEditor;
        }

        @Override
        public TagEditor createTagEditor(final Tag tag) {
            if (tag instanceof JmlTagInvocation) {
                Definition def = ((JmlTagInvocation) tag).resolve(jmlEditor.getContext());
                if (def instanceof AdsEnumItemDef) {
                    return new EnumerationItemTagEditor();
                }
            }
            if (tag instanceof JmlTagTask) {
                return new TaskEditor();
            } else if (tag instanceof JmlTagEventCode) {
                return new EventCodeTagEditor();
            } else if (tag instanceof JmlTagLocalizedString) {
                return new LocalizedStringTagEditor();
            } else {
                return null;
            }
        }

        @Override
        public EditorOpenInfo createOpenInfo(final Tag tag) {
            return new EditorOpenInfo(jmlEditor.getPane().isReadOnly(), Lookups.fixed(new ContextProviderImpl(jmlEditor.getContext()), jmlEditor, jmlEditor.getSource()));
        }
    }

    public static interface ContextProvider {

        Definition getContext();
    }

    public static class ContextProviderImpl implements ContextProvider {

        private final Definition context;

        public ContextProviderImpl(final Definition context) {
            this.context = context;
        }

        @Override
        public Definition getContext() {
            return context;
        }
    }

    /*protected static class JmlTagPopupFactory implements TagPopupFactory {

        @Override
        public TagPopup createTagPopup(ScmlEditorPane editor) {
            return new JmlTagPopup(editor);
        }
    }

    protected static class JmlTagPopup extends TagPopup {

        private Jml.Tag tag;

        public JmlTagPopup(ScmlEditorPane editor) {
            super(editor);
        }

        @Override
        public boolean open(Tag tag) {
            if (tag instanceof Jml.Tag) {
                this.tag = (Jml.Tag) tag;
                return true;
            } else {
                return false;
            }

        }

        @Override
        public void show(Component c, Point popupLocation) {
            try {
                editor.putClientProperty(PopupActionCodeGeneratorFactory.RADIX_OBJECT_FOR_TAG_REPLACEMENT, tag);
                editor.putClientProperty(ScmlEditorPane.DISABLE_STANDART_GENERATORS, Boolean.TRUE);

                Action generateAction = editor.getActionMap().get("generate-code");

                if (generateAction != null) {
                    generateAction.actionPerformed(new ActionEvent(editor, -1, null));
                }

            } finally {
                editor.putClientProperty(PopupActionCodeGeneratorFactory.RADIX_OBJECT_FOR_TAG_REPLACEMENT, null);
                editor.putClientProperty(ScmlEditorPane.DISABLE_STANDART_GENERATORS, Boolean.FALSE);

            }
        }
    }*/
}
