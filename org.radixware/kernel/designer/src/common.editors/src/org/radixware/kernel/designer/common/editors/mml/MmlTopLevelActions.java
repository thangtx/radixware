package org.radixware.kernel.designer.common.editors.mml;

import java.util.*;
import javax.swing.*;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorFactory;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.doc.DocTopicBody;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.mml.MmlTagId;
import org.radixware.kernel.common.mml.MmlTagMarkdownImage;
import org.radixware.kernel.common.mml.MmlTagMarkdownRef;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction;
import static org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction.isMml;
import static org.radixware.kernel.designer.common.editors.mml.MmlTopLevelActions.INSERT_MARKDOWN_IMAGE_TAG_ACTION;
import static org.radixware.kernel.designer.common.editors.mml.MmlTopLevelActions.INSERT_MARKDOWN_REF_TAG_ACTION;
import org.radixware.kernel.designer.common.editors.mml.MmlTopLevelActions.MmlInsertTagButton;

public class MmlTopLevelActions {

    public static final int REGULAR_ACTIONS = 1000;
    public static final String INSERT_ID_CODE_TAG_ACTION = "insert-id-tag-action";
    public static final String INSERT_MARKDOWN_REF_TAG_ACTION = "insert-markdown-ref-tag-action";
    public static final String INSERT_MARKDOWN_IMAGE_TAG_ACTION = "insert-markdown-image-tag-action";

    public static abstract class MmlInsertTagButton extends ScmlInsertTagAction {

        public MmlInsertTagButton(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Class getClassForBundle() {
            return MmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isMml(scml);
        }
    }

    private static class ChooseNeedDocDef {

        public static List<Definition> get(List<Definition> modules) {
            ChooseNeedDocDefCfgs cfgIterator = new ChooseNeedDocDefCfgs(modules);
            return ChooseDefinitionSequence.chooseDefinitionSequence(cfgIterator);
        }

        private static boolean isHaveNeedDocChildren(Definition choosenDef) {
            List<RadixObject> children = new ArrayList<RadixObject>();
            choosenDef.visitChildren(VisitorFactory.newAppedingVisiter(children), new ChildrenNeedDocumentationDefinitionVisitorProvider(choosenDef));
            return !children.isEmpty();
        }

        // DefinitionNeedDocumentationVisitorProvider
        private static class ChildrenNeedDocumentationDefinitionVisitorProvider extends VisitorProvider {

            private Definition ownerDef;

            public ChildrenNeedDocumentationDefinitionVisitorProvider(Definition ownerDef) {
                super();
                this.ownerDef = ownerDef;
            }

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof Definition) {
                    Definition def = (Definition) radixObject;
                    boolean isChild = (def.getOwnerDefinition() == ownerDef);
                    return isChild && ((def instanceof IRadixdocProvider) || (isHaveNeedDocChildren(def))); //def.needsDocumentation()
                }

                return false;
            }
        }

        // ChooseParentPropCfg
        private static class ChooseNeedDocDefCfg extends ChooseDefinitionCfg {

            ChooseNeedDocDefCfg(final Definition currentDef) {
                super(currentDef, new ChildrenNeedDocumentationDefinitionVisitorProvider(currentDef));
                setTypesTitle("Choose Final Definition or Next Reference in Definition "); //+ currentDef.getQualifiedName(contextDef)
            }

            ChooseNeedDocDefCfg(List<Definition> modules) {
                super(modules);
                setTypeTitle("Choose Parent Reference in Definition "); //+ contextDef.getName()
            }

        }

        // ChooseParentPropCfgs
        private static class ChooseNeedDocDefCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {

            public ChooseNeedDocDefCfgs(List<Definition> modules) {
                super(new ChooseNeedDocDefCfg(modules));
            }

            @Override
            public String getDisplayName() {
                return "Choose Parent Definition";
            }

            @Override
            protected boolean hasNextConfig(Definition choosenDef) {
                return isHaveNeedDocChildren(choosenDef);
            }

            @Override
            protected boolean isFinalTarget(Definition choosenDef) {
                return choosenDef.needsDocumentation();
            }

            @Override
            protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
                if (isHaveNeedDocChildren(choosenDef)) {
                    return new ChooseNeedDocDefCfg(choosenDef);
                }
                return null;
            }
        }

    }

    public static class InsertIdTagAction extends MmlInsertTagButton {

        public InsertIdTagAction(ScmlEditor editor) {
            super(INSERT_ID_CODE_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            final Definition contextDef = getEditor().getContext().getDefinition();

            // getAllModules
            Layer layer = getEditor().getContext().getLayer();
            List<Layer> layerList = layer.listFinalBaseLayers();
            List<Definition> modules = new ArrayList<Definition>();
            for (Layer l : layerList) {
                l.visitChildren(VisitorFactory.newAppedingDefinitionVisiter(modules), VisitorProviderFactory.createModuleVisitorProvider());
            }

            // choose
            List<Definition> definitionSequence = ChooseNeedDocDef.get(modules);

            // add
            final List<Tag> tags = new LinkedList<Tag>();
            if (!definitionSequence.isEmpty()) {
                Definition def = definitionSequence.get(definitionSequence.size() - 1);
                if (def != null) {
                    MmlTagId tag = new MmlTagId(def);
                    tags.add(tag);
                }
            }
            return tags;

            //<editor-fold defaultstate="collapsed" desc="oldVersion">
//            ChooseRadixObjectCfg cfg = ChooseRadixObjectCfg.Factory.newInstance(branch, new VisitorProvider() {
//                @Override
//                public boolean isTarget(RadixObject radixObject) {
//                    return radixObject instanceof Definition && (((Definition) radixObject).needsDocumentation());
//                }
//            });
//            
//            Definition def = (Definition) ChooseRadixObject.chooseRadixObject(cfg);
//            // add
//            final List<Tag> tags = new LinkedList<Tag>();
//            if (def != null) {
//                MmlTagId tag = new MmlTagId(def);
//                tags.add(tag);
//            }
            //</editor-fold>
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.ID.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 300;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt D";
        }

        @Override
        public boolean isAvailable(Scml scml
        ) {
            return true;
        }
    }

    public static class InsertMarkdownRefTagAction extends MmlInsertTagButton {

        public InsertMarkdownRefTagAction(ScmlEditor editor) {
            super(INSERT_MARKDOWN_REF_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {

            MmlEditor mmlEditor = (MmlEditor) getEditor();
            Mml mml = mmlEditor.getSource();
            DocTopicBody body = (DocTopicBody) mml.getContainer();
            EIsoLanguage lang = body.getLanguage();

            AdsModule module = (AdsModule) mml.getModule();

            MmlTagMarkdownRef tag = new MmlTagMarkdownRef(module, lang);

            final List<Tag> tags = new LinkedList<Tag>();
            if (getPane().editTag(tag, Lookups.fixed(module))) {
                tags.add(tag);
            }

            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.FILE.NEW_DOCUMENT.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 400;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt R";
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return true;
        }
    }

    public static class InsertMarkdownImageTagAction extends MmlInsertTagButton {

        public InsertMarkdownImageTagAction(ScmlEditor editor) {
            super(INSERT_MARKDOWN_IMAGE_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {

            MmlEditor mmlEditor = (MmlEditor) getEditor();
            Mml mml = mmlEditor.getSource();
            DocTopicBody body = (DocTopicBody) mml.getContainer();
            EIsoLanguage lang = body.getLanguage();

            AdsModule module = (AdsModule) mml.getModule();

            MmlTagMarkdownImage tag = new MmlTagMarkdownImage(module, lang);

            final List<Tag> tags = new LinkedList<Tag>();
            if (getPane().editTag(tag, Lookups.fixed(module))) {
                tags.add(tag);
            }

            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.FILE.LOAD_IMAGE.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 500;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt I";
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return true;
        }
    }

}
