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

package org.radixware.kernel.designer.common.editors.sqml.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsIncludeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsVarObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityObjectType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.common.sqml.tags.DataTag;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.EntityRefParameterTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.IndexDbNameTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.sqml.tags.TaskTag;
import org.radixware.kernel.common.sqml.tags.ThisTableRefTag;
import org.radixware.kernel.common.sqml.tags.XPathTag;
import org.radixware.kernel.designer.ads.common.sql.AdsSqlClassVisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence.ChooseDefinitionCfgs;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.PopupButton;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.ScmlToolBarDropdownButton;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertIndexDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertPackageDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertReferenceDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertSqmlSequenceDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertTriggerDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertDbNameTagActions.InsertTypeDbNameTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions;
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
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsRoleIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsScopeCommandIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsSelectorPresentationIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsSortingIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertAdsXmlSchemeIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertClassPropertyIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertTableColumnIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.InsertIdTagActions.InsertTableIdTagAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertChildPropertySqlNameTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertParentPropertySqlNameTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertPropertySqlNameTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertTablePropertySqlNameTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertTableSqlNameTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertThisPropertySqlNameTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertThisTableIdTagAction;
import org.radixware.kernel.designer.common.editors.sqml.actions.SqmlPopupActions.InsertThisTableSqlNameTagAction;

public class SqmlTopLevelActions {

    public static final int REGULAR_ACTIONS = 1000;
    public static final int PREVIEW_ACTIONS = 2000;
    public static final String INSERT_ID_ACTION = "insert-id-action";
    public static final String INSERT_DB_NAME_ACTION = "insert-db-name-action";
    public static final String INSERT_TABLE_SQL_NAME_ACTION = "insert-table-sql-name-action";
    public static final String INSERT_PROPERTY_SQL_NAME_ACTION = "insert-property-sql-name-action";
    public static final String INSERT_TABLE_COLUMN_SQL_NAME_ACTION = "insert-table-column-sql-name-action";
    public static final String INSERT_CLASS_PROPERTY_SQL_NAME_ACTION = "insert-class-property-sql-name-action";
    public static final String INSERT_INDEX_DB_NAME_ACTION = "insert-index-db-name-action";
    public static final String INSERT_DB_FUNC_CALL_ACTION = "insert-db-func-call-action";
    public static final String INSERT_ENUMERATION_ITEM_VALUE_ACTION = "insert-enumeration-item-value-action";
    public static final String PREVIEW_SQL_ACTION = "preview-sql-action";
    public static final String TOGGLE_CHECK_SQL_ACTION = "toggle-check-sql-action";
    public static final String INSERT_INPUT_PARAMETER = "insert-input-parameter-action";
    public static final String INSERT_THIS_TABLE_REF_ACTION = "insert-this-table-ref-action";
    public static final String INSERT_TASK_TAG_ACTION = "insert-task-tag-action";
    public static final String INSERT_XPATH_TAG_ACTION = "insert-xpath-tag-action";
    public static final String INSERT_DATA_TAG_ACTION = "insert-data-tag-action";
    public static final String INSERT_TARGET_DB_PREPROCESSOR_TAG_ACTION = "insert-db-preprocessor-action";
    public static final String INSERT_PREPROCESSOR_ACTION = "insert-preprocessor-action";
    public static final String INSERT_ELSEIF_TAG_TO_EDITOR = "insert-elseif-tag-to-editor";
    public static final String INSERT_ENDIF_TAG_TO_EDITOR = "insert-endif-tag-to-editor";

    public static abstract class SqmlInsertTagDropdownButton extends ScmlToolBarDropdownButton {

        public SqmlInsertTagDropdownButton(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlTopLevelActions.class;
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return ScmlInsertTagAction.isSqml(scml);
        }
    }

    public static abstract class SqmlInsertTagButton extends ScmlInsertTagAction {

        public SqmlInsertTagButton(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isSqml(scml);
        }
    }

    public static class InsertPreprocessorAction extends ScmlToolBarAction {

        private JComponent presenter;

        public InsertPreprocessorAction(final ScmlEditor editor) {
            super(INSERT_PREPROCESSOR_ACTION, editor);
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.DEBUG.IF.getIcon();
        }

        @Override
        public void updateState() {
            setEnabled(getEditor().getPane().isCurrentPositionEditable());
        }

        private AdsSqlClassDef getSqlClass() {
            Definition def = getEditor().getPane().getScml().getOwnerDefinition();
            while (def != null && !(def instanceof AdsSqlClassDef)) {
                def = def.getOwnerDefinition();
            }
            if (def instanceof AdsSqlClassDef) {
                return (AdsSqlClassDef) def;
            }
            return null;

        }

        @Override
        public Component getToolbarPresenter() {
            if (presenter != null) {
                return presenter;
            }
            final JMenu subMenuForParamters = new JMenu() {
                @Override
                public void setPopupMenuVisible(final boolean aFlag) {
                    removeAll();
                    AdsSqlClassDef sqlClass = getSqlClass();
                    IFilter<AdsPropertyDef> paramsFilter = AdsSqlClassVisitorProviderFactory.newPropertyForPreprocessorTag();
                    for (AdsPropertyDef property : sqlClass.getProperties().getLocal().list(paramsFilter)) {
                        JMenuItem presenter = createIfByParameterPresenter(property);
                        this.add(presenter);
                    }
                    super.setPopupMenuVisible(aFlag);
                }
            };
            final JPopupMenu popupMenu = new JPopupMenu() {
                @Override
                public void setVisible(boolean b) {
                    updateParamteresSubMenuState();
                    super.setVisible(b);
                }

                private void updateParamteresSubMenuState() {
                    final AdsSqlClassDef sqlClass = getSqlClass();
                    if (sqlClass == null) {
                        return;
                    }
                    boolean classHasPropertyParams = false;
                    for (AdsPropertyDef property : sqlClass.getProperties().getLocal().list()) {
                        if (property instanceof AdsParameterPropertyDef) {
                            classHasPropertyParams = true;
                            break;
                        }
                    }
                    subMenuForParamters.setEnabled(classHasPropertyParams);
                }
            };

            popupMenu.add(new SqmlTopLevelActions.InsertTargetDbPreprocessorTagAction(getEditor()).getPopupPresenter());

            subMenuForParamters.setText(NbBundle.getMessage(SqmlTopLevelActions.class, "if-by-parameter"));
            subMenuForParamters.setIcon(RadixWareIcons.DEBUG.IF.getIcon());

            if (getSqlClass() != null) {
                popupMenu.add(subMenuForParamters);
            }
            popupMenu.add(new InsertElseIfTagAction(getEditor()).getPopupPresenter());
            popupMenu.add(new InsertEndIfTagAction(getEditor()).getPopupPresenter());

            final JButton menuButton = new PopupButton(popupMenu);
            menuButton.setIcon(RadixWareIcons.DEBUG.IF.getIcon());
            menuButton.setToolTipText(getTooltip());
            final PropertyChangeListener listener = new PropertyChangeListener() {
                private RadixObjects.ContainerChangesListener parametersCountListener = new RadixObjects.ContainerChangesListener() {
                    @Override
                    public void onEvent(final RadixObjects.ContainerChangedEvent e) {
                        updateState();
                    }
                };

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if (evt.getNewValue() instanceof AdsSqlClassDef) {
                        if (evt.getOldValue() instanceof AdsSqlClassDef) {
                            AdsSqlClassDef oldValue = (AdsSqlClassDef) evt.getOldValue();
                            oldValue.getProperties().getLocal().getContainerChangesSupport().removeEventListener(parametersCountListener);
                        }
                        AdsSqlClassDef newValue = (AdsSqlClassDef) evt.getNewValue();
                        newValue.getProperties().getLocal().getContainerChangesSupport().addEventListener(parametersCountListener);
                    }
                    updateState();
                }
            };
            menuButton.putClientProperty("enabled-conditions-listener", listener);
            getPane().addPropertyChangeListener(ScmlEditorPane.CURRENT_POSITION_EDITABLE, listener);
            addPropertyChangeListener(listener);
            listener.propertyChange(new PropertyChangeEvent(this, null, getSqlClass(), getSqlClass()));

            presenter = menuButton;

            return presenter;
        }

        private JMenuItem createIfByParameterPresenter(final AdsPropertyDef property) {
            final JMenuItem item = new JMenuItem();
            item.setText(property.getName());
            item.setIcon(property.getIcon().getIcon());
            item.setToolTipText(property.getToolTip());
            item.putClientProperty(AdsPropertyDef.class, property);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    final IfParamTag tag = IfParamTag.Factory.newInstance();
                    tag.setParameterId(property.getId());
                    if (getPane().editTag(tag, Lookups.fixed(property))) {
                        getPane().insertTag(tag);
                        getPane().requestFocusInWindow();
                    }
                }
            });
            return item;
        }

        @Override
        public int getGroupType() {
            return 1500;
        }

        @Override
        public int getPosition() {
            return 100;
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        protected Class getClassForBundle() {
            return InsertPreprocessorAction.class;
        }

        @Override
        public void actionPerformed(final ActionEvent e, final ScmlEditorPane pane) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isAvailable(final Scml scml) {
            return ScmlInsertTagAction.isSqml(scml);
        }
    }

    public static class InsertElseIfTagAction extends ScmlInsertTagAction {

        public InsertElseIfTagAction(final ScmlEditor editor) {
            super(INSERT_ELSEIF_TAG_TO_EDITOR, editor);
        }

        @Override
        protected List<Tag> createTags() {
            final Scml.Tag tag = ElseIfTag.Factory.newInstance();
            return Collections.singletonList(tag);
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.DEBUG.IF_ELSE.getIcon();
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(final Scml scml) {
            return ScmlInsertTagAction.isSqml(scml);
        }
    }

    public static class InsertEndIfTagAction extends ScmlInsertTagAction {

        public InsertEndIfTagAction(final ScmlEditor editor) {
            super(INSERT_ENDIF_TAG_TO_EDITOR, editor);
        }

        @Override
        protected List<Tag> createTags() {
            final Scml.Tag tag = EndIfTag.Factory.newInstance();
            return Collections.singletonList(tag);
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.DEBUG.IF_END.getIcon();
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(final Scml scml) {
            return ScmlInsertTagAction.isSqml(scml);
        }
    }

    public static class InsertXPathTagAction extends SqmlInsertTagButton {

        public InsertXPathTagAction(ScmlEditor editor) {
            super(INSERT_XPATH_TAG_ACTION, editor);
        }

        @Override
        protected List<Tag> createTags() {
            XPathTag tag = XPathTag.Factory.newInstance();

            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(this.getEditor().getSource(), AdsVisitorProviders.newSchemeVisitorProvider());
            Definition scheme = ChooseDefinition.chooseDefinition(cfg);

            if (scheme != null) {
                XPathTag.Item defaultItem = XPathTag.Item.Factory.newInstance();
                defaultItem.setSchemaId(scheme.getId());
                tag.getItems().add(defaultItem);

                if (getPane().editTag(tag)) {
                    return Collections.singletonList((Tag) tag);
                }

            }

            return null;
        }

        @Override
        protected void insertTag(Tag tag, ScmlEditorPane pane) {
            int caretOfset = pane.getCaretPosition();
            pane.insertString("extractValue(XMLType(), ");
            pane.insertTag(tag);
            pane.insertString(")");
            pane.setCaretPosition(caretOfset + 21);
            pane.requestFocusInWindow();
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.ORACLE.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 320;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt X";
        }
    }

    public static class InsertIdAction extends SqmlInsertTagDropdownButton {

        private final ScmlToolBarAction[] actions;

        public InsertIdAction(ScmlEditor editor) {
            super(INSERT_ID_ACTION, editor);
            actions = new ScmlToolBarAction[]{
                //Dds ID
                new InsertTableIdTagAction(editor),
                new InsertThisTableIdTagAction(editor),
                new InsertTableColumnIdTagAction(editor),
                new InsertClassPropertyIdTagAction(editor),
                //Ads ID
                new InsertAdsClassIdAction(editor),
                new InsertAdsContextlessCommandIdTagAction(editor),
                new InsertAdsScopeCommandIdTagAction(editor),
                new InsertAdsEnumIdTagAction(editor),
                new InsertAdsRoleIdTagAction(editor),
                new InsertIdTagActions.InsertAccessPartitionFamilyTagAction(editor),
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
                new InsertAdsXmlSchemeIdTagAction(editor)};
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

    public static class InsertDbNameAction extends SqmlInsertTagDropdownButton {

        private final ScmlToolBarAction[] actions;

        public InsertDbNameAction(ScmlEditor editor) {
            super(INSERT_DB_NAME_ACTION, editor);
            actions = new ScmlToolBarAction[]{
                new InsertIndexDbNameTagAction(editor),
                new InsertTriggerDbNameTagAction(editor),
                new InsertReferenceDbNameTagAction(editor),
                new InsertSqmlSequenceDbNameTagAction(editor),
                new InsertPackageDbNameTagAction(editor),
                new InsertTypeDbNameTagAction(editor),};
        }

        @Override
        protected ScmlToolBarAction[] getActions() {
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

    public static class InsertTableSqlNameAction extends SqmlInsertTagDropdownButton {

        private final ScmlToolBarAction[] actions;

        public InsertTableSqlNameAction(ScmlEditor pane) {
            super(INSERT_TABLE_SQL_NAME_ACTION, pane);
            actions = new ScmlToolBarAction[]{
                new InsertTableSqlNameTagAction(pane),
                new InsertThisTableSqlNameTagAction(pane),};
        }

        @Override
        protected ScmlToolBarAction[] getActions() {
            return actions;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.TABLE.getIcon(16);
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 200;
        }
    }

    public static class InsertPropertySqlNameAction extends SqmlInsertTagDropdownButton {

        private final ScmlToolBarAction[] actions;

        public InsertPropertySqlNameAction(ScmlEditor editor) {
            super(INSERT_PROPERTY_SQL_NAME_ACTION, editor);
            actions = new ScmlToolBarAction[]{
                new InsertThisPropertySqlNameTagAction(editor),
                new InsertTablePropertySqlNameTagAction(editor),
                new InsertChildPropertySqlNameTagAction(editor),
                new InsertParentPropertySqlNameTagAction(editor),
                new InsertPropertySqlNameTagAction(editor)};
        }

        @Override
        protected ScmlToolBarAction[] getActions() {
            return actions;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 300;
        }
    }

    public static class InsertTargetDbPreprocessorTagAction extends SqmlInsertTagButton {

        public InsertTargetDbPreprocessorTagAction(ScmlEditor editor) {
            super(INSERT_TARGET_DB_PREPROCESSOR_TAG_ACTION, editor);
        }

        @Override
        protected List<Tag> createTags() {
            TargetDbPreprocessorTag tag = TargetDbPreprocessorTag.Factory.newInstance();
            if (getPane().editTag(tag)) {
                return Collections.<Tag>singletonList(tag);
            }
            return null;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.DATABASE_ATTRIBUTES.getIcon();
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
            return "ctrl alt A";
        }
    }

    public static class InsertIndexDbNameAcion extends SqmlInsertTagButton {

        public InsertIndexDbNameAcion(ScmlEditor pane) {
            super(INSERT_INDEX_DB_NAME_ACTION, pane);
        }

        @Override
        public List<Tag> createTags() {
            final ScmlEditorPane pane = getPane();
            List<Tag> tags = new LinkedList<Tag>();
            if (pane.getScml() instanceof Sqml) {
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                        pane.getScml(),
                        SqmlVisitorProviderFactory.newIndexDbNameTagProvider());
                cfg.setStepCount(2);
                List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
                if (definitions != null) {
                    for (Definition definition : definitions) {
                        IndexDbNameTag tag = IndexDbNameTag.Factory.newInstance();
                        tag.setTableId(definition.getOwnerDefinition().getId());
                        if (definition instanceof DdsPrimaryKeyDef) {
                            tag.setIndexId(null);
                        } else {
                            tag.setIndexId(definition.getId());
                        }
                        tags.add(tag);
                    }
                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.INDEX.getIcon(16);
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
            return "ctrl alt I";
        }
    }

    public static class InsertDbFuncCallAcion extends SqmlInsertTagButton {

        public InsertDbFuncCallAcion(ScmlEditor pane) {
            super(INSERT_DB_FUNC_CALL_ACTION, pane);
        }

        @Override
        public List<Tag> createTags() {
            final ScmlEditorPane editor = getPane();
            final Scml scml = editor.getScml();
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                    scml,
                    SqmlVisitorProviderFactory.newDbFuncCallTagProvider(scml));
            final Collection<? extends Definition> allFunctions = cfg.collectAllowedDefinitions();
            final Map<Definition, Collection<Definition>> package2funtions = new HashMap<Definition, Collection<Definition>>();
            for (final Definition funcDef : allFunctions) {
                final Definition packageDef = funcDef.getOwnerDefinition().getOwnerDefinition();
                Collection<Definition> packageFunctions = package2funtions.get(packageDef);
                if (packageFunctions == null) {
                    packageFunctions = new ArrayList<Definition>();
                    package2funtions.put(packageDef, packageFunctions);
                }
                packageFunctions.add(funcDef);
            }
            final ChooseDefinitionCfg initCfg = ChooseDefinitionCfg.Factory.newInstance(package2funtions.keySet());
            initCfg.setTypeTitle("Choose Package");
            initCfg.setTypesTitle("Choose Package");
            final ChooseDefinitionCfgs cfgs = new ChooseDefinitionCfgs(initCfg) {
                @Override
                protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
                    final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(package2funtions.get(choosenDef));
                    cfg.setTypeTitle("Choose Function");
                    cfg.setTypesTitle("Choose Function");
                    return cfg;
                }

                @Override
                protected boolean hasNextConfig(Definition choosenDef) {
                    return !isFinalTarget(choosenDef);
                }

                @Override
                protected boolean isFinalTarget(Definition choosenDef) {
                    return (choosenDef instanceof DdsFunctionDef);
                }

                @Override
                public String getDisplayName() {
                    return "Choose Definition";
                }
            };
            final List<Definition> defSequence = ChooseDefinitionSequence.chooseDefinitionSequence(cfgs);
            List<Tag> tags = new LinkedList<Tag>();
            if (defSequence != null && !defSequence.isEmpty()) {
                final Definition definition = defSequence.get(defSequence.size() - 1);
                if (definition != null) {
                    DbFuncCallTag tag = DbFuncCallTag.Factory.newInstance();
                    tag.setFunctionId(definition.getId());
                    if (editor.editTag(tag)) {
                        tags.add(tag);
                    }
                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.FUNCTION.getIcon(16);
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
        public String getAcceleratorKey() {
            return "ctrl alt F";
        }
    }

    public static class InsertEnumerationItemValueTag extends SqmlInsertTagButton {

        public InsertEnumerationItemValueTag(ScmlEditor editor) {
            super(INSERT_ENUMERATION_ITEM_VALUE_ACTION, editor);

        }

        @Override
        public List<Tag> createTags() {
            final ScmlEditorPane editor = getPane();
            List<Tag> tags = new LinkedList<Tag>();
            if (editor.getScml() instanceof Sqml) {
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                        editor.getScml(),
                        SqmlVisitorProviderFactory.newConstValueTagProvider());
                cfg.setStepCount(2);
                List<Definition> definitions = ChooseDefinition.chooseDefinitions(cfg);
                if (definitions != null) {
                    for (Definition definition : definitions) {
                        ConstValueTag tag = ConstValueTag.Factory.newInstance();
                        tag.setItemId(definition.getId());
                        tag.setEnumId(definition.getOwnerDefinition().getId());
                        tags.add(tag);
                    }

                }
            }
            return tags;
        }

        @Override
        public Icon getIcon() {
            return RadixObjectIcon.ENUM_ITEM.getIcon(16);
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
            return "ctrl alt E";
        }
    }

    public static class InsertInputParameterTag extends SqmlInsertTagButton {

        public InsertInputParameterTag(ScmlEditor editor) {
            super(INSERT_INPUT_PARAMETER, editor);

        }

        @Override
        public List<Tag> createTags() {
            final List<Tag> tags = new LinkedList<Tag>();
            final AdsFilterDef filterDef = getAdsFilterDef(getEditor().getPane().getScml());
            if (filterDef != null) {
                final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(filterDef.getParameters().list());
                final Definition paramDef = ChooseDefinition.chooseDefinition(cfg);
                if (paramDef != null) {
                    if (paramDef instanceof AdsFilterDef.Parameter) {
                        final AdsFilterDef.Parameter filterParamDef = (AdsFilterDef.Parameter) paramDef;
                        if (filterParamDef.getType().getTypeId() == EValType.PARENT_REF || filterParamDef.getType().getTypeId() == EValType.OBJECT) {
                            final EntityRefParameterTag tag = EntityRefParameterTag.Factory.newInstance();
                            final AdsType type = filterParamDef.getType().resolve(filterParamDef).get();
                            if (type instanceof EntityObjectType) {
                                tag.setReferencedTableId(((EntityObjectType) type).getSourceEntityId());
                                tag.setParameterId(filterParamDef.getId());
                                if (getPane().editTag(tag, Lookups.fixed(filterParamDef))) {
                                    tags.add(tag);
                                }
                                return tags;
                            } else {
                                throw new IllegalStateException("Can not obtain Entity id from parameter by Parent Ref.");
                            }
                        }
                    }
                    final ParameterTag tag = ParameterTag.Factory.newInstance();
                    tag.setParameterId(paramDef.getId());
                    tags.add(tag);
                }
            }
            return tags;
        }

        @Override
        public boolean isAvailable(final Scml scml) {
            AdsFilterDef filterDef = getAdsFilterDef(scml);
            if (filterDef != null && filterDef.getCondition() == scml) {
                return true;
            }
            return false;
        }

        private AdsFilterDef getAdsFilterDef(final Scml scml) {
            if (scml.getDefinition() instanceof AdsFilterDef) {
                return (AdsFilterDef) scml.getDefinition();
            }
            return null;

        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.SQL_CLASS_PARAMETERS.getIcon();
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
            return "ctrl alt P";
        }
    }

    public static class InsertThisTableRefAction extends SqmlInsertTagButton {

        public InsertThisTableRefAction(ScmlEditor editor) {
            super(INSERT_THIS_TABLE_REF_ACTION, editor);
        }

        @Override
        protected List<Tag> createTags() {
            if (getEditor().getSource() instanceof Sqml) {
                Sqml sqml = (Sqml) getEditor().getSource();
                final DdsTableDef tableDef = sqml.getEnvironment().findThisTable();
                if (tableDef == null) {
                    throw new IllegalStateException("This table is undefined");
                }

                Scml.Tag tag = ThisTableRefTag.Factory.newInstance();

                if (getEditor().getPane().editTag(tag, Lookups.fixed(tableDef))) {
                    return Collections.singletonList(tag);
                } else {
                    return null;
                }
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public boolean isAvailable(Scml scml) {
            if (scml instanceof Sqml) {
                return ((Sqml) scml).getEnvironment() != null && ((Sqml) scml).getEnvironment().findThisTable() != null;
            }
            return false;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.DATABASE.THIS_TABLE.getIcon();
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
            return "ctrl alt T";
        }
    }

    public static class InsertTaskTagAction extends SqmlInsertTagButton {

        public InsertTaskTagAction(ScmlEditor editor) {
            super(INSERT_TASK_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            TaskTag tag = TaskTag.Factory.newToDo("");
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
            return 1100;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt A";
        }
    }

    public static class PreviewSqlAction extends ScmlToolBarAction {

        private JToggleButton presenter;

        public PreviewSqlAction(ScmlEditor editor) {
            super(PREVIEW_SQL_ACTION, editor);
        }

        @Override
        public void actionPerformed(ActionEvent e, ScmlEditorPane pane) {
            if (presenter.isSelected()) {
                if (!getEditor().setPreviewMode(true)) {
                    presenter.setSelected(false);
                }
            } else {
                getEditor().setPreviewMode(false);
                getEditor().getPane().requestFocusInWindow();
            }
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.EDIT.VIEW_SQL.getIcon(16);
        }

        @Override
        public Component getToolbarPresenter() {
            if (presenter == null) {
                presenter = new JToggleButton(this);
                presenter.setSelected(false);
                presenter.setText("");
                presenter.setToolTipText(getTooltip());
            }
            return presenter;
        }

        @Override
        public void updateState() {
            //do nothing;
        }

        @Override
        public int getGroupType() {
            return PREVIEW_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 100;
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlTopLevelActions.class;
        }

        @Override
        public String getAcceleratorKey() {
            return "ctrl alt R";
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return ScmlInsertTagAction.isSqml(scml);
        }
    }

    public static class ToggleCheckSqlAction extends ScmlToolBarAction {

        private JToggleButton presenter;

        public ToggleCheckSqlAction(ScmlEditor editor) {
            super(TOGGLE_CHECK_SQL_ACTION, editor);
        }

        @Override
        public void actionPerformed(ActionEvent e, ScmlEditorPane pane) {
            AdsSqlClassDef sqlClass = getSqlClassDef(getEditor().getSource());
            if (sqlClass == null || sqlClass.isReadOnly()) {
                return;
            }
            if (presenter.isSelected()) {
                sqlClass.setIgnoreSqlCheck(true);
            } else {
                sqlClass.setIgnoreSqlCheck(false);
            }
        }

        private AdsSqlClassDef getSqlClassDef(RadixObject context) {
            if (context == null) {
                return null;
            }
            Definition def = context.getDefinition();
            while (def != null) {
                if (def instanceof AdsSqlClassDef) {
                    return (AdsSqlClassDef) def;
                }
                def = def.getOwnerDefinition();
            }
            return null;
        }

        @Override
        public Icon getIcon() {
            return RadixWareDesignerIcon.CHECK.STOP.getIcon(16);
        }

        @Override
        public Component getToolbarPresenter() {
            if (presenter == null) {
                presenter = new JToggleButton(this);
                AdsSqlClassDef sqlClass = getSqlClassDef(getEditor().getSource());
                if (sqlClass != null) {
                    presenter.setSelected(sqlClass.isIgnoreSqlCheck());
                }else{
                    presenter.setSelected(false);
                }
                presenter.setText("");
                presenter.setToolTipText(getTooltip());
            }
            return presenter;
        }

        @Override
        public void updateState() {
            if (getEditor().getSource() == null) {
                return;
            }
            if (presenter == null) {
                return;
            }
            AdsSqlClassDef sqlClass = getSqlClassDef(getEditor().getSource());
            if (sqlClass == null) {
                return;
            }
            presenter.setSelected(sqlClass.isIgnoreSqlCheck());
            presenter.setEnabled(!sqlClass.isReadOnly());
        }

        @Override
        public int getGroupType() {
            return PREVIEW_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 100;
        }

        @Override
        protected Class getClassForBundle() {
            return SqmlTopLevelActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            AdsSqlClassDef clazz = getSqlClassDef(scml);
            return clazz != null && !clazz.isReadOnly();
        }

        @Override
        public String getAcceleratorKey() {
            return "";
        }
    }

    public static class InsertDataTagAction extends SqmlInsertTagButton {

        public InsertDataTagAction(ScmlEditor editor) {
            super(INSERT_DATA_TAG_ACTION, editor);
        }

        private AdsAlgoClassDef getAlgoDef(Definition context) {
            Definition def = context;
            while (def != null) {
                if (def instanceof AdsAlgoClassDef) {
                    return (AdsAlgoClassDef) def;
                }
                def = def.getOwnerDefinition();
            }
            return null;
        }

        @Override
        protected List<Tag> createTags() {
            final AdsAlgoClassDef algoDef = getAlgoDef(getEditor().getContext());
            if (algoDef == null) {
                return null;
            }

            final Set<Definition> defs = new HashSet<Definition>();
            final IVisitor visitor = new IVisitor() {
                @Override
                public void accept(RadixObject object) {
                    if (object instanceof AdsVarObject
                            || object instanceof AdsAppObject.Prop
                            || object instanceof AdsIncludeObject.Param
                            || object instanceof AdsAlgoClassDef.Param
                            || object instanceof AdsAlgoClassDef.Var
                            || object instanceof AdsAppObject
                            || object instanceof AdsIncludeObject) {
                        if (object instanceof IAdsTypedObject) {
                            final AdsTypeDeclaration type = ((IAdsTypedObject) object).getType();
                            if (type.getTypeId() != EValType.INT
                                    && type.getTypeId() != EValType.NUM
                                    && type.getTypeId() != EValType.STR
                                    && type.getTypeId() != EValType.DATE_TIME
                                    && type.getTypeId() != EValType.BOOL
                                    && type.getTypeId() != EValType.CHAR) {
                                return;
                            }
                        }
                        if (object instanceof AdsAppObject.Prop) {
                            AdsAppObject.Prop p = (AdsAppObject.Prop) object;
                            if ((p.getMode() & AdsAppObject.Prop.PUBLIC) != 0) {
                                defs.add(p);
                            }
                        } else {
                            defs.add((Definition) object);
                        }
                    }
                }
            };

            algoDef.visit(visitor, VisitorProviderFactory.createDefaultVisitorProvider());
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(defs);
            cfg.setForAlgo(true);

            final Definition obj = ChooseDefinition.chooseDefinition(cfg);
            final List<Tag> tags = new LinkedList<Tag>();
            if (obj != null) {
                final DataTag tag = DataTag.Factory.newInstance(obj.getId());
                tags.add(tag);
            }

            return tags;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            final AdsAlgoClassDef algoDef = getAlgoDef(getEditor().getContext());
            return isSqml(scml) && algoDef != null;
        }

        @Override
        public Icon getIcon() {
            return RadixWareIcons.JML_EDITOR.DATA.getIcon();
        }

        @Override
        public int getGroupType() {
            return REGULAR_ACTIONS;
        }

        @Override
        public int getPosition() {
            return 310;
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }
    }
}
