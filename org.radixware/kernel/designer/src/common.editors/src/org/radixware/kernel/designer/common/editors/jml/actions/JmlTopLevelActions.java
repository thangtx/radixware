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
package org.radixware.kernel.designer.common.editors.jml.actions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AbstractFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.IModelClassOwner;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsCatchObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsIncludeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPage;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsScopeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsVarObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsFilterModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsDialogModelClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.*;
import org.radixware.kernel.common.repository.Layer.License;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarDropdownButton;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertColumnDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertFunctionDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertIndexDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertJmlSequenceDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertReferenceDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertTableDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertTypeDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAccessPartitionFamilyTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsClassIdAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsContextlessCommandIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsCustomDialogIdTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsCustomEditorIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsCustomParagEditorIdtagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsCustomPropEditorIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsCustomSelectorIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsDomainIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsEditorPageIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsEditorPresentationIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsEnumIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsExplorerItemIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsFilterIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsMethodIdAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsRoleIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsScopeCommandIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsSelectorPresentationIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsSortingIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsXmlSchemeIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertClassPropertyIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertMsdlSchemeIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertTableIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;
import org.radixware.kernel.designer.common.editors.jml.actions.JmlPopupActions.InsertAdsIconIdTagAction;
import org.radixware.kernel.designer.common.editors.layer.LicensesPanel;

public class JmlTopLevelActions {

    public static final int REGULAR_ACTIONS = 1000;
    public static final int ALGO_ACTIONS = 2000;
    public static final int INTERNAL_ACTIONS = 3000;
    public static final String INSERT_ID_TAG_ACTION = "insert-id-tag-action";
    public static final String INSERT_DB_NAME_TAG_ACTION = "insert-db-name-tag-action";
    public static final String INSERT_TASK_TAG_ACTION = "insert-task-tag-action";
    public static final String INSERT_NLS_TAG_ACTION = "insert-nls-tag-action";
    public static final String INSERT_EVENT_CODE_TAG_ACTION = "insert-event-code-tag-action";
    public static final String INSERT_TYPE_TAG_ACTION = "insert-type-tag-action";
    public static final String INSERT_INVOCATION_TAG_ACTION = "insert-invocation-tag-action";
    public static final String INSERT_DATA_TAG_ACTION = "insert-data-tag-action";
    public static final String INSERT_INTERNAL_PROPERTY_ACCESS_TAG_ACTION = "insert-internal-property-access-tag-action";
    public static final String INSERT_WIDGET_TAG_ACTION = "insert-widget-tag-action";
    public static final String INSERT_READ_LICENSE_TAG_ACTION = "insert-read-license-tag-action";
    public static final String INSERT_CHECK_LICENSE_TAG_ACTION = "insert-check-license-tag-action";

    public static abstract class JmlInsertTagDropdownButton extends ScmlToolBarDropdownButton {

        public JmlInsertTagDropdownButton(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Class getClassForBundle() {
            return JmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return ScmlInsertTagAction.isJml(scml);
        }
    }

    public static abstract class JmlInsertTagButton extends ScmlInsertTagAction {

        public JmlInsertTagButton(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Class getClassForBundle() {
            return JmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isJml(scml);
        }
    }

    public static class InsertIdTagAction extends JmlInsertTagDropdownButton {

        ScmlToolBarAction[] actions;

        public InsertIdTagAction(ScmlEditor editor) {
            super(INSERT_ID_TAG_ACTION, editor);
            actions = new ScmlToolBarAction[]{
                //Dds ID
                new InsertTableIdTagAction(editor),
                //Ads ID
                new InsertClassPropertyIdTagAction(editor),
                new InsertAdsClassIdAction(editor),
                new InsertAdsMethodIdAction(editor),
                new InsertAdsContextlessCommandIdTagAction(editor),
                new InsertAdsScopeCommandIdTagAction(editor),
                new InsertAdsEnumIdTagAction(editor),
                new InsertAdsRoleIdTagAction(editor),
                new InsertAdsDomainIdTagAction(editor),
                new InsertAdsEditorPresentationIdTagAction(editor),
                new InsertAdsSelectorPresentationIdTagAction(editor),
                new InsertAdsEditorPageIdTagAction(editor),
                new InsertAdsFilterIdTagAction(editor),
                new InsertAdsSortingIdTagAction(editor),
                new InsertAdsExplorerItemIdTagAction(editor),
                new InsertAdsCustomDialogIdTag(editor),
                new InsertAdsCustomPropEditorIdTagAction(editor),
                new InsertAdsCustomParagEditorIdtagAction(editor),
                new InsertAdsCustomEditorIdTagAction(editor),
                new InsertAdsCustomSelectorIdTagAction(editor),
                new InsertAdsIconIdTagAction(editor),
                new InsertAdsXmlSchemeIdTagAction(editor),
                new InsertMsdlSchemeIdTagAction(editor),
                new InsertAccessPartitionFamilyTagAction(editor)};
        }

        @Override
        protected ScmlToolBarAction[] getActions() {
            return actions;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.ID.getIcon(16);
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 100;
        }
    }

    public static class InsertDbNameAction extends JmlInsertTagDropdownButton {

        ScmlToolBarAction actions[];

        public InsertDbNameAction(ScmlEditor editor) {
            super(INSERT_DB_NAME_TAG_ACTION, editor);
        }

        @Override
        protected ScmlToolBarAction[] getActions() {
            if (actions == null) {
                actions = new ScmlToolBarAction[]{
                    new InsertTableDbNameTagAction(getEditor()),
                    new InsertColumnDbNameTagAction(getEditor()),
                    new InsertIndexDbNameTagAction(getEditor()),
                    new InsertJmlSequenceDbNameTagAction(getEditor()),
                    new InsertFunctionDbNameTagAction(getEditor()),
                    new InsertReferenceDbNameTagAction(getEditor()),
                    new InsertTypeDbNameTagAction(getEditor()),};
            }
            return actions;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.DATABASE.DB_NAME.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 150;
        }
    }

    public static class InsertTaskTagAction extends JmlInsertTagButton {

        public InsertTaskTagAction(ScmlEditor editor) {
            super(INSERT_TASK_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            JmlTagTask tag = JmlTagTask.Factory.newToDo("");
            List<Tag> tags = new LinkedList<Tag>();
            if (getPane().editTag(tag)) {
                tags.add(tag);
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.TASK.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 600;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt T";
        }
    }

    public static class InsertReadLicenseTagAction extends JmlInsertTagButton {

        public InsertReadLicenseTagAction(ScmlEditor editor) {
            super(INSERT_READ_LICENSE_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            JmlTagReadLicense tag = createTag();
            if (tag != null) {
                return Collections.singletonList((Tag) tag);
            }
            return Collections.emptyList();
        }

        private JmlTagReadLicense createTag() {
            final License license = LicensesPanel.selectLicense(Collections.singletonList(getEditor().getSource().getLayer()), false);
            if (license != null) {
                JmlTagReadLicense tag = new JmlTagReadLicense();
                tag.setLicense(license.getFullName());
                int readId = 1;
                for (Scml.Item scmlItem : getEditor().getSource().getItems()) {
                    if (scmlItem instanceof JmlTagReadLicense) {
                        if (((JmlTagReadLicense) scmlItem).getLicense().equals(tag.getLicense())) {
                            readId++;
                        }
                    }
                }
                tag.setId(readId);
                return tag;
            }
            return null;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.READ_LICENSE.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 800;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            if (((Jml) scml).getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                return super.isAvailable(scml);
            }
            return false;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt R";
        }
    }

    public static class InsertCheckLicenseTagAction extends JmlInsertTagButton {

        private static class CheckLicenseModalPanel extends JPanel {

            public CheckLicenseModalPanel(LayoutManager layout) {
                super(layout);
            }
        }

        public InsertCheckLicenseTagAction(ScmlEditor editor) {
            super(INSERT_CHECK_LICENSE_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            final JmlTagCheckLicense tag = createTag();
            if (tag != null) {
                return Collections.singletonList((Tag) tag);
            }
            return Collections.emptyList();
        }

        private JmlTagCheckLicense createTag() {
            final List<JmlTagReadLicense> readTags = new ArrayList<>();
            int itemIndexUnderCursor[] = getEditor().getPane().getScmlDocument().itemIndexAndOffset(getEditor().getPane().getCaretPosition());
            final TokenSequence ts = TokenHierarchy.get(getEditor().getPane().getScmlDocument()).tokenSequence();
            for (int i = 0; i < itemIndexUnderCursor[0]; i++) {
                if (getEditor().getSource().getItems().get(i) instanceof JmlTagReadLicense) {
                    if (ts != null) {
                        ts.move(getEditor().getPane().getScmlDocument().itemOffsetAndLength(i)[0]);
                        if (ts.moveNext()) {
                            if (ts.token().id().primaryCategory() != null && ts.token().id().primaryCategory().contains("comment")) {
                                continue;//do not count tags in comments
                            }
                        }
                    }
                    readTags.add((JmlTagReadLicense) getEditor().getSource().getItems().get(i));
                }
            }
            if (readTags.isEmpty()) {
                DialogUtils.messageInformation("There are no read license tags in the previous lines");
                return null;
            }
            if (readTags.size() == 1) {
                return createCheckTag(readTags.get(0));
            }
            final JPanel contentPane = new CheckLicenseModalPanel(new MigLayout("fill"));//custom panel class for correct dialog size saving
            final JComboBox cbReadLicenseTag = new JComboBox(readTags.toArray());
            cbReadLicenseTag.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    final JmlTagReadLicense readTag = (JmlTagReadLicense) value;
                    setText(readTag.getLicense() + " [" + readTag.getId() + "]");
                    return this;
                }
            });
            cbReadLicenseTag.setMaximumSize(new Dimension(Integer.MAX_VALUE, cbReadLicenseTag.getPreferredSize().height));
            cbReadLicenseTag.setMinimumSize(new Dimension(280, cbReadLicenseTag.getPreferredSize().height));
            contentPane.add(cbReadLicenseTag, "growx, wrap");
            final ModalDisplayer md = new ModalDisplayer(contentPane, "Check License");
            if (md.showModal()) {
                return createCheckTag((JmlTagReadLicense) cbReadLicenseTag.getSelectedItem());
            }
            return null;
        }

        private JmlTagCheckLicense createCheckTag(final JmlTagReadLicense readTag) {
            final JmlTagCheckLicense tag = new JmlTagCheckLicense();
            tag.setLicense(readTag.getLicense());
            tag.setId(readTag.getId());
            return tag;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.CHECK_LICENSE.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 900;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            if (((Jml) scml).getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                return super.isAvailable(scml);
            }
            return false;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt C";
        }
    }

    public static class InsertNlsTagAction extends JmlInsertTagButton {

        public InsertNlsTagAction(final ScmlEditor editor) {
            super(INSERT_NLS_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            final List<Tag> tags = new LinkedList<Tag>();
            final JmlEditor editor = (JmlEditor) getEditor();

            final AdsMultilingualStringDef stringDef = AdsMultilingualStringDef.Factory.newInstance();
            final JmlTagLocalizedString tag = JmlTagLocalizedString.Factory.newInstance(stringDef);

            if (getPane().editTag(tag, Lookups.fixed(stringDef))) {
                Definition ctx = editor.getContext();
                if (ctx instanceof AdsDefinition) {
                    ((AdsDefinition) ctx).findExistingLocalizingBundle().getStrings().getLocal().add(stringDef);
                    tags.add(tag);
                }

            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.NLS.getIcon();
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
            return "ctrl alt S";
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return scml.getOwnerDefinition() instanceof AdsDefinition;
        }
    }

    public static class InsertEventCodeTagAction extends JmlInsertTagButton {

        public InsertEventCodeTagAction(ScmlEditor editor) {
            super(INSERT_EVENT_CODE_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            final List<Tag> tags = new LinkedList<Tag>();
            final JmlEditor editor = (JmlEditor) getEditor();

            AdsEventCodeDef ecDef = AdsEventCodeDef.Factory.newEventCodeInstance();
            JmlTagEventCode tag = (JmlTagEventCode) JmlTagEventCode.Factory.newInstance(ecDef);

            if (getPane().editTag(tag, Lookups.fixed(ecDef))) {
                Definition ctx = editor.getContext();
                if (ctx instanceof AdsDefinition) {
                    ((AdsDefinition) ctx).findExistingLocalizingBundle().getStrings().getLocal().add(ecDef);
                    tags.add(tag);
                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.EVENT_CODE.getIcon();
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
            return "ctrl alt E";
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return scml.getOwnerDefinition() instanceof AdsDefinition;
        }
    }

    public static class InsertTypeDeclarationTagAction extends JmlInsertTagButton {

        public InsertTypeDeclarationTagAction(ScmlEditor editor) {
            super(INSERT_TYPE_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            JmlEditor editor = (JmlEditor) getEditor();
            List<Tag> tags = new LinkedList<Tag>();

            AdsTypeDeclaration type = ChooseType.getInstance().chooseType(new ChooseType.DefaultTypeFilter(editor.getContext(), editor.getSource()));
            if (type != null) {
                JmlTagTypeDeclaration tag = new JmlTagTypeDeclaration(type);
                tags.add(tag);
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.CLASS.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 200;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt Y";
        }
    }

    public static class InsertInvocationTagAction extends JmlInsertTagButton {

        public InsertInvocationTagAction(ScmlEditor editor) {
            super(INSERT_INVOCATION_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            JmlEditor editor = (JmlEditor) getEditor();
            VisitorProvider provider = new InvocationVisitorProvider();
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                    editor.getContext(),
                    provider);
            cfg.setStepCount(2);
            Definition def = ChooseDefinition.chooseDefinition(cfg);
            List<Tag> tags = new LinkedList<Tag>();
            if (def instanceof AdsDefinition) {
                JmlTagInvocation tag = JmlTagInvocation.Factory.newInstance((AdsDefinition) def);
                tags.add(tag);
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.METHOD.getIcon();
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

        private static class InvocationVisitorProvider extends VisitorProvider {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof AdsPropertyDef || radixObject instanceof AdsMethodDef || radixObject instanceof IEnumDef.IItem || radixObject instanceof AdsUISignalDef);
            }
        }
    }

    public static class InsertLibUserFuncTagAction extends JmlInsertTagButton {

        public InsertLibUserFuncTagAction(ScmlEditor editor) {
            super(INSERT_INVOCATION_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            JmlEditor editor = (JmlEditor) getEditor();
            VisitorProvider provider = null;
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                    editor.getContext(),
                    provider);
            cfg.setStepCount(1);
            //cfg.setDisplayMode(EChooseDefinitionDisplayMode.QUALIFIED_NAME);
            Definition def = ChooseDefinition.chooseDefinition(cfg);
            List<Tag> tags = new LinkedList<>();
            if (def instanceof AdsDefinition) {
                JmlTagInvocation tag = JmlTagInvocation.Factory.newInstance((AdsDefinition) def);
                tags.add(tag);
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.FUNCTION.getIcon();//RadixWareIcons.JML_EDITOR.METHOD.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 1000;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt I";
        }

        public boolean isAvailable(Scml scml) {
            return (scml instanceof Jml) && (getUserReportClassDef() != null);
        }

        private AdsUserReportClassDef getUserReportClassDef() {
            Definition context = ((JmlEditor) getEditor()).getContext();
            if (context == null) {
                return null;
            }
            Definition def = context;
            do {
                if (def instanceof AdsReportModelClassDef) {
                    return null;
                }
                if (def instanceof AdsUserReportClassDef) {
                    return (AdsUserReportClassDef) def;
                }
            } while ((def = def.getOwnerDefinition()) != null);
            return null;
        }

        private static class InvocationVisitorProvider extends VisitorProvider {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof AdsPropertyDef || radixObject instanceof AdsMethodDef || radixObject instanceof IEnumDef.IItem || radixObject instanceof AdsUISignalDef);
            }
        }
    }

    public static class InsertDataTagAction extends JmlInsertTagButton {

        public InsertDataTagAction(ScmlEditor editor) {
            super(INSERT_DATA_TAG_ACTION, editor);
        }

        private AdsAlgoClassDef getAlgoClassDef() {
            Definition context = ((JmlEditor) getEditor()).getContext();
            if (context == null) {
                return null;
            }
            Definition def = context;
            do {
                if (def instanceof AdsAlgoClassDef) {
                    return (AdsAlgoClassDef) def;
                }
            } while ((def = def.getOwnerDefinition()) != null);
            return null;
        }

        @Override
        public List<Tag> createTags() {
            final List<Tag> tags = new LinkedList<Tag>();
            final JmlEditor editor = (JmlEditor) getEditor();
            final AdsAlgoClassDef algoDef = getAlgoClassDef();
            if (algoDef == null) {
                return tags;
            }

            AdsVisitorProvider provider = new AdsVisitorProvider() {
                @Override
                public boolean isContainer(RadixObject object) {
                    return (object instanceof AdsSegment)
                            || (object instanceof AdsModule)
                            || (object instanceof ModuleDefinitions)
                            || (object instanceof AdsDefinitions)
                            || (object instanceof RadixObjects)
                            || (object instanceof AdsAlgoClassDef && algoDef.equals(object))
                            || (object instanceof AdsPage)
                            || (object instanceof AdsScopeObject)
                            || (object instanceof AdsCatchObject)
                            || (object instanceof AdsAppObject)
                            || (object instanceof AdsIncludeObject);
                }

                @Override
                public boolean isTarget(RadixObject object) {
                    if (object instanceof IAdsTypedObject || object instanceof AdsAppObject || object instanceof AdsIncludeObject) {
                        if (object instanceof AdsAppObject.Prop) {
                            AdsAppObject.Prop p = (AdsAppObject.Prop) object;
                            return (p.getMode() & AdsAppObject.Prop.PUBLIC) != 0;
                        }
                        return true;
                    }
                    return false;
                }
            };
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(editor.getContext(), provider);
            cfg.setForAlgo(true);

            final Definition obj = ChooseDefinition.chooseDefinition(cfg);
            if (obj != null) {
                Tag tag = null;
                if (obj instanceof AdsVarObject) {
                    tag = new JmlTagData((AdsVarObject) obj);
                }
                if (obj instanceof AdsAlgoClassDef.Param) {
                    tag = new JmlTagData((AdsAlgoClassDef.Param) obj);
                }
                if (obj instanceof AdsAlgoClassDef.Var) {
                    tag = new JmlTagData((AdsAlgoClassDef.Var) obj);
                }
                if (obj instanceof AdsIncludeObject.Param) {
                    tag = new JmlTagData((AdsIncludeObject.Param) obj);
                }
                if (obj instanceof AdsAppObject.Prop) {
                    tag = new JmlTagData((AdsAppObject.Prop) obj);
                }
                if (tag != null) {
                    tags.add(tag);
                }
            }

            return tags;
            /*
             * JmlEditor editor = (JmlEditor) getEditor(); List<Tag> tags = new
             * LinkedList<Tag>(); final AdsAlgoClassDef algoDef =
             * getAlgoClassDef(); if (algoDef != null) { AdsVisitorProvider
             * provider = new AdsVisitorProvider() {
             *
             * @Override public boolean isContainer(RadixObject object) { //
             * return object instanceof AdsAlgoClassDef ? algoDef.equals(object)
             * : true; return (object instanceof AdsSegment) || (object
             * instanceof AdsModule) || (object instanceof ModuleDefinitions) ||
             * (object instanceof AdsDefinitions) || (object instanceof
             * RadixObjects) || (object instanceof AdsAlgoClassDef &&
             * algoDef.equals(object)) || (object instanceof AdsPage) || (object
             * instanceof AdsScopeObject) || (object instanceof AdsCatchObject)
             * || (object instanceof AdsAppObject) || (object instanceof
             * AdsIncludeObject); }
             *
             * @Override public boolean isTarget(RadixObject object) { if
             * (object instanceof IAdsTypedObject) { if (object instanceof
             * AdsAppObject.Prop) { AdsAppObject.Prop p = (AdsAppObject.Prop)
             * object; return (p.getMode() & AdsAppObject.Prop.PUBLIC) != 0; }
             * return true; } return false; } };
             *
             * ChooseDefinitionCfg cfg =
             * ChooseDefinitionCfg.Factory.newInstance(editor.getContext(),
             * provider); Definition obj =
             * ChooseDefinition.chooseDefinition(cfg);
             *
             * if (obj != null) { Tag tag = null; if (obj instanceof
             * AdsVarObject) { tag = new JmlTagData((AdsVarObject) obj); } if
             * (obj instanceof AdsAlgoClassDef.Param) { tag = new
             * JmlTagData((AdsAlgoClassDef.Param) obj); } if (obj instanceof
             * AdsAlgoClassDef.Var) { tag = new JmlTagData((AdsAlgoClassDef.Var)
             * obj); } if (obj instanceof AdsIncludeObject.Param) { tag = new
             * JmlTagData((AdsIncludeObject.Param) obj); } if (obj instanceof
             * AdsAppObject.Prop) { tag = new JmlTagData((AdsAppObject.Prop)
             * obj); } if (tag != null) { tags.add(tag); } } } return tags;
             */
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return (scml instanceof Jml) && getAlgoClassDef() != null;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.DATA.getIcon();
        }

        @Override
        public int getGroupType() {
            return ALGO_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt D";
        }
    }

    public static class InsertInternalPropertyAccessTagAction extends JmlInsertTagButton {

        public InsertInternalPropertyAccessTagAction(ScmlEditor editor) {
            super(INSERT_INTERNAL_PROPERTY_ACCESS_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            JmlEditor editor = (JmlEditor) getEditor();
            Definition ownerDef = editor.getSource().getOwnerDef();
            List<Tag> tags = new LinkedList<Tag>();
            if (ownerDef instanceof AdsPropertyDef) {
                AdsPropertyDef propDef = (AdsPropertyDef) ownerDef;
                JmlTagInvocation tag = JmlTagInvocation.Factory.newInternalPropAccessor(propDef);

                tags.add(tag);
            } else {
                List<AdsPropertyDef> props = ((AdsMethodDef) ownerDef).getOwnerClass().getProperties().get(EScope.LOCAL_AND_OVERWRITE);
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(props);
                Definition def = ChooseDefinition.chooseDefinition(cfg);
                if (def instanceof AdsPropertyDef) {
                    AdsPropertyDef propDef = (AdsPropertyDef) def;
                    JmlTagInvocation tag = JmlTagInvocation.Factory.newInternalPropAccessor(propDef);

                    tags.add(tag);
                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.PROPERTY.getIcon();
        }

        @Override
        public int getGroupType() {
            return INTERNAL_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 100;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            if (scml instanceof Jml) {
                Jml jml = (Jml) scml;
                Definition def = jml.getOwnerDef();
                return def instanceof AdsPropertyDef || (def instanceof AdsMethodDef && ((AdsMethodDef) def).isConstructor());
            } else {
                return false;
            }
        }

        @Override
        public void updateState() {
            JmlEditor jmlEditor = (JmlEditor) getEditor();
            Jml jml = jmlEditor.getSource();
            Definition def = jml.getOwnerDef();

            if (def instanceof AdsPropertyDef || (def instanceof AdsMethodDef && ((AdsMethodDef) def).isConstructor())) {
                setEnabled(getPane().isCurrentPositionEditable());
            } else {
                setEnabled(false);
            }
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt P";
        }
    }

    public static class InsertWidgetTagAction extends JmlInsertTagButton {

        public InsertWidgetTagAction(ScmlEditor editor) {
            super(INSERT_WIDGET_TAG_ACTION, editor);
        }

        @Override
        public boolean isAvailable(Scml scml) {
            if (!(scml instanceof Jml)) {
                return false;
            }
            Definition def = scml.getOwnerDefinition();
            while (def != null) {
                if (def instanceof AdsModelClassDef) {
                    return true;
                }
                def = def.getOwnerDefinition();
            }
            return false;
        }

        public AdsClassDef getModelClassDef() {
            Definition context = ((JmlEditor) getEditor()).getContext();
            if (context == null) {
                return null;
            }
            Definition def = context;
            do {
                if (def instanceof AdsModelClassDef) {
                    return (AdsModelClassDef) def;
                }
            } while ((def = def.getOwnerDefinition()) != null);
            return null;
        }

        @Override
        protected List<Tag> createTags() {
            final JmlEditor editor = (JmlEditor) getEditor();
            final List<Tag> tags = new LinkedList<Tag>();
            final AdsClassDef modelDef = getModelClassDef();
            final ERuntimeEnvironmentType env = editor.getSource().getUsageEnvironment();
            if (modelDef != null) {
                final List<AdsAbstractUIDef> uis = new ArrayList<AdsAbstractUIDef>();
                if (modelDef instanceof AdsDialogModelClassDef) {
                    final IModelClassOwner ui = ((AdsDialogModelClassDef) modelDef).getOwnerDialog();
                    if (ui != null) {
                        uis.add((AdsAbstractUIDef) ui);
                    }
                } else if (modelDef instanceof AdsEntityModelClassDef) {
                    AdsAbstractUIDef ui = ((AdsEntityModelClassDef) modelDef).getOwnerEditorPresentation().getCustomViewSupport().getCustomView(env);
                    if (ui != null) {
                        uis.add(ui);
                    }
                    for (AdsEditorPageDef page : ((AdsEntityModelClassDef) modelDef).getOwnerEditorPresentation().getEditorPages().get(EScope.ALL)) {
                        ui = page.getCustomViewSupport().getCustomView(env);
                        if (ui != null) {
                            uis.add(ui);
                        }
                    }
                } else if (modelDef instanceof AdsGroupModelClassDef) {
                    final AdsAbstractUIDef ui = ((AdsGroupModelClassDef) modelDef).getOwnerSelectorPresentation().getCustomViewSupport().getCustomView(env);
                    if (ui != null) {
                        uis.add(ui);
                    }
                } else if (modelDef instanceof AdsParagraphModelClassDef) {
                    final AdsAbstractUIDef ui = ((AdsParagraphModelClassDef) modelDef).getOwnerParagraph().getCustomViewSupport().getCustomView(env);
                    if (ui != null) {
                        uis.add(ui);
                    }
                } else if (modelDef instanceof AbstractFormModelClassDef) {
                    final AbstractFormPresentations prs = ((IAdsFormPresentableClass) ((AbstractFormModelClassDef) modelDef).getOwnerClass()).getPresentations();
                    AdsAbstractUIDef ui = prs.getCustomViewSupport().getCustomView(env);
                    if (ui != null) {
                        uis.add(ui);
                    }
                    for (AdsEditorPageDef page : prs.getEditorPages().get(EScope.ALL)) {
                        ui = page.getCustomViewSupport().getCustomView(env);
                        if (ui != null) {
                            uis.add(ui);
                        }
                    }
                } else if (modelDef instanceof AdsFilterModelClassDef) {
                    final AdsFilterDef filter = ((AdsFilterModelClassDef) modelDef).getOwnerFilterDef();
                    AdsAbstractUIDef ui = filter.getCustomViewSupport().getCustomView(env);
                    if (ui != null) {
                        uis.add(ui);
                    }
                    for (AdsEditorPageDef page : filter.getEditorPages().get(EScope.ALL)) {
                        ui = page.getCustomViewSupport().getCustomView(env);
                        if (ui != null) {
                            uis.add(ui);
                        }
                    }
                }
                if (uis.isEmpty()) {
                    DialogUtils.messageError("No widgets found");
                    return tags;
                }
                final AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isContainer(RadixObject object) {
                        return true;
                    }

                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (env == ERuntimeEnvironmentType.EXPLORER) {
                            return object instanceof AdsWidgetDef/*
                                     * || object instanceof AdsUIDef
                                     */;
                        } else if (env == ERuntimeEnvironmentType.WEB) {
                            return object instanceof AdsRwtWidgetDef;
                        } else {
                            return false;
                        }

                    }
                };

                final ArrayList<Definition> defs = new ArrayList<Definition>();

                for (AdsAbstractUIDef ui : uis) {

                    AdsAbstractUIDef o = ui;
                    while (o != null) {
                        o.visit(new IVisitor() {
                            @Override
                            public void accept(RadixObject radixObject) {
                                defs.add((Definition) radixObject);
                            }
                        }, provider);
                        o = (AdsAbstractUIDef) o.getHierarchy().findOverwritten().get();
                    }
                }
                final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(defs);
                final Definition obj = ChooseDefinition.chooseDefinition(cfg);

                if (obj != null) {
                    Tag tag = null;
                    if (obj instanceof AdsDefinition) {
                        tag = JmlTagInvocation.Factory.newInstance((AdsDefinition) obj);
                    }
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.WIDGETS.BUTTON_BOX.getIcon();
        }

        @Override
        public int getGroupType() {
            return INTERNAL_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 100;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt W";
        }
    }
}
