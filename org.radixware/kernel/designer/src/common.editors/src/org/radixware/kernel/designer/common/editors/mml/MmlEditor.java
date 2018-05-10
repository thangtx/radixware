package org.radixware.kernel.designer.common.editors.mml;

import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.mml.MmlTagId;
import org.radixware.kernel.common.mml.MmlTagMarkdownImage;
import org.radixware.kernel.common.mml.MmlTagMarkdownRef;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.radixware.kernel.designer.common.editors.mml.editors.MarkdownRefTagEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class MmlEditor extends ScmlEditor<Mml> {

    private Definition context;

    public MmlEditor() {
        super(MML_MIME_TYPE);
    }

    public void open(final Mml mml, Definition context, final OpenInfo openInfo) {
        super.open(mml, openInfo);
        if (context == null) {
            RadixObject owner = mml.getContainer();
            while (owner != null && !(owner instanceof Definition)) {
                owner = owner.getContainer();
            }
            context = (Definition) owner;
        }
        this.context = context;
        reloadToolbarActions();
    }

    @Override
    public void open(final Mml source, final OpenInfo openInfo) {
        open(source, null, openInfo);
    }

    @Override
    protected ScmlEditorPane.TagEditorFactory createTagEditorFactory() {
        return new MmlTagEditorFactory(this);
    }

    @Override
    protected ScmlEditorPane.TagPopupFactory createTagPopupFactory() {
        return super.createTagPopupFactory();
    }

    @Override
    protected ScmlEditorPane.VTagFactory createVTagFactory() {
        return new MmlVTagFactory();
    }

    @Override
    protected ScmlToolBarAction[] createScmlToolBarActions() {
        final ScmlToolBarAction[] actions = new ScmlToolBarAction[]{
            new MmlTopLevelActions.InsertIdTagAction(this),
            new MmlTopLevelActions.InsertMarkdownRefTagAction(this),
            new MmlTopLevelActions.InsertMarkdownImageTagAction(this)};
        return actions;
    }

    @Override
    public Definition getContext() {
        return context;
    }

    public static class MmlVTagFactory implements ScmlEditorPane.VTagFactory {

        @Override
        public ScmlEditorPane.VTag createVTag(final Scml.Tag tag) {
            if (tag instanceof MmlTagId) {
                return new VMmlTagId((MmlTagId) tag);
            }
            if (tag instanceof MmlTagMarkdownRef) {
                return new VMmlTagMarkdownRef((MmlTagMarkdownRef) tag);
            }
            if (tag instanceof MmlTagMarkdownImage) {
                return new VMmlTagMarkdownImage((MmlTagMarkdownImage) tag);
            }
            return null;
        }
    }

    protected static class MmlTagEditorFactory implements ScmlEditorPane.TagEditorFactory {

        private final MmlEditor mmlEditor;

        public MmlTagEditorFactory(final MmlEditor mmlEditor) {
            this.mmlEditor = mmlEditor;
        }

        @Override
        public TagEditor createTagEditor(final Scml.Tag tag) {
            if (tag instanceof MmlTagMarkdownRef) {
                return new MarkdownRefTagEditor();
            } else if (tag instanceof MmlTagMarkdownImage) {
                return new MarkdownRefTagEditor();
            } else {
                return null;
            }
        }

        @Override
        public EditorOpenInfo createOpenInfo(final Scml.Tag tag) {
            return new EditorOpenInfo(mmlEditor.getPane().isReadOnly(), Lookups.fixed(new ContextProviderImpl(mmlEditor.getContext()), mmlEditor, mmlEditor.getSource()));
        }
    }

    public static interface ContextProvider {

        Definition getContext();
    }

    public static class ContextProviderImpl implements MmlEditor.ContextProvider {

        private final Definition context;

        public ContextProviderImpl(final Definition context) {
            this.context = context;
        }

        @Override
        public Definition getContext() {
            return context;
        }
    }

}
