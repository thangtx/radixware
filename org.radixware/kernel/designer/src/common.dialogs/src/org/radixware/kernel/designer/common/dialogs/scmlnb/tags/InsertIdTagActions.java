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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomParagEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomSelectorDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.choosedomain.ChooseDomain;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence.ChooseDefinitionCfgs;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class InsertIdTagActions {

    // DDS
    public static final String INSERT_TABLE_COLUMN_ID_TAG = "insert-table-column-id-tag";
    public static final String INSERT_TABLE_ID_TAG = "insert-table-id-tag";
    // ADS
    public static final String INSERT_CLASS_PROPERTY_ID_TAG = "insert-class-property-id-tag";
    public static final String INSERT_ADS_CLASS_ID_TAG_ACTION = "insert-ads-class-id-tag-action";
    public static final String INSERT_ADS_CONTEXTLESS_COMMAND_ID_TAG_ACTION = "insert-ads-contextless-command-id-tag-action";
    public static final String INSERT_ADS_ROLE_ID_TAG_ACTION = "insert-ads-role-id-tag-action";
    public static final String INSERT_ADS_DOMAIN_ID_TAG_ACTION = "insert-ads-domain-id-tag-action";
    public static final String INSERT_ADS_EDITOR_PRESENTATION_ID_TAG_ACTION = "insert-ads-editor-presentation-id-tag-action";
    public static final String INSERT_ADS_SELECTOR_PRESENTATION_ID_TAG_ACTION = "insert-ads-selector-presentation-id-tag-action";
    public static final String INSERT_ADS_EDITOR_PAGE_ID_TAG_ACTION = "insert-ads-editor-page-id-tag-action";
    public static final String INSERT_ADS_FILTER_ID_TAG_ACTION = "insert-ads-filter-id-tag-action";
    public static final String INSERT_ADS_SORTING_ID_TAG_ACTION = "insert-ads-sorting-id-tag-action";
    public static final String INSERT_ADS_COLOR_SCHEME_ID_TAG_ACTION = "insert-ads-color-scheme-id-tag-action";
    public static final String INSERT_ADS_PARAGRAPH_ID_TAG_ACTION = "insert-ads-paragraph-id-tag-action";
    public static final String INSERT_ADS_EXPLORER_ITEM_ID_TAG_ACTION = "insert-ads-explorer-item-id-tag-action";
    public static final String INSERT_ADS_CUSTOM_DIALOG_ID_TAG_ACTION = "insert-ads-custom-dialog-id-tag-action";
    public static final String INSERT_ADS_CUSTOM_PROP_EDITOR_ID_TAG_ACTION = "insert-ads-custom-prop-editor-id-tag-acton";
    public static final String INSERT_ADS_CUSTOM_PARAG_EDITOR_ID_TAG_ACTION = "insert-ads-custom-parag-editor-id-tag-action";
    public static final String INSERT_ADS_CUSTOM_SELECTOR_ID_TAG_ACTION = "insert-ads-custom-selector-id-tag-action";
    public static final String INSERT_ADS_SCOPE_COMMAND_ID_TAG_ACTION = "insert-ads-scope-command-id-tag-action";
    public static final String INSERT_ADS_CUSTOM_EDITOR_ID_TAG_ACTION = "insert-ads-custom-editor-id-tag-action";
    public static final String INSERT_ADS_ENUM_ID_TAG_ACTION = "insert-ads-enum-id-tag-action";
    public static final String INSERT_ADS_XML_SCHEME_ID_TAG_ACTION = "insert-ads-xml-scheme-id-tag-action";
    public static final String INSERT_MSDL_SCHEME_ID_TAG_ACTION = "insert-msdl-scheme-id-tag-action";
    public static final String INSERT_ACCESS_PARTITION_FAMILY_ID_TAG_ACTION = "insert-access-partition-family-id-tag-action";
    public static final String INSERT_ADS_METHOD_ID_TAG_ACTION = "insert-method-id-tag-action";

    // Abstract Insert Id classes
    public static abstract class InsertIdTagAction extends InsertTagByDefinitionAction {

        public InsertIdTagAction(String name, ScmlEditor editor) {
            super(name, editor);
        }

        @Override
        protected Tag createTag(Definition definition) {
            return IdTagFactory.createIdTag(definition, getPane());
        }
    }

    protected static abstract class InsertIdTagActionImpl extends InsertIdTagAction {

        public InsertIdTagActionImpl(String name, ScmlEditor editor) {
            super(name, editor);
        }

        protected abstract ChooseDefinitionCfg createCfg();

        @Override
        protected List<Definition> chooseDefinitions() {
            return ChooseDefinition.chooseDefinitions(createCfg());
        }

        @Override
        protected Tag createTag(Definition definition) {
            return IdTagFactory.createIdTag(definition, getPane());
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        protected Class getClassForBundle() {
            return InsertIdTagActions.class;
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
        public boolean isAvailable(Scml scml) {
            return scml != null;
        }
    }

    // Specific ID Classes
    public static class InsertTableColumnIdTagAction extends InsertIdTagActionImpl {

        public InsertTableColumnIdTagAction(ScmlEditor pane) {
            super(INSERT_TABLE_COLUMN_ID_TAG, pane);
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                    getPane().getScml(),
                    SqmlVisitorProviderFactory.newColumnForIdTagProvider());
            cfg.setStepCount(2);
            return cfg;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isSqml(scml);
        }
    }

    public static class InsertClassPropertyIdTagAction extends InsertIdTagActionImpl {

        public InsertClassPropertyIdTagAction(ScmlEditor pane) {
            super(INSERT_CLASS_PROPERTY_ID_TAG, pane);
        }

//        @Override
//        public boolean isAvailable(Scml scml) {
//            return isAdsSqml(scml) || isJml(scml);
//        }
        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.COLUMN.getIcon(16);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsPropertyDef.class, 2, new IFilter<RadixObject>() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsPropertyDef && radixObject.getOwnerDefinition() instanceof AdsPropertyDef) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        }
    }

    public static class InsertTableIdTagAction extends InsertIdTagActionImpl {

        public InsertTableIdTagAction(ScmlEditor pane) {
            super(INSERT_TABLE_ID_TAG, pane);
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.TABLE.getIcon(16);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(getPane().getScml(),
                    SqmlVisitorProviderFactory.newTableForIdTagProvider());
            return cfg;
        }
    }

    // Insert ID Tag Actions for ADS
    public static abstract class InsertAdsIdTagAction extends InsertIdTagActionImpl {

        public InsertAdsIdTagAction(String name, ScmlEditor editor) {
            super(name, editor);
        }
//        @Override
//        public void updateState() {
//            super.updateState();
//        }
//
//        @Override
//        public boolean isAvailable(Scml scml) {
//            return isAdsSqml(scml) || isJml(scml);
//        }
    }

    public static class InsertAdsClassIdAction extends InsertAdsIdTagAction {

        public InsertAdsClassIdAction(ScmlEditor editor) {
            super(INSERT_ADS_CLASS_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsClassDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CLASS_DYNAMIC.getIcon(16);
        }
    }

    public static class InsertAdsMethodIdAction extends InsertAdsIdTagAction {

        public InsertAdsMethodIdAction(ScmlEditor editor) {
            super(INSERT_ADS_METHOD_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsMethodDef.class, 2);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.Method.METHOD.getIcon();
        }
    }

    public static class InsertAdsContextlessCommandIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsContextlessCommandIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_CONTEXTLESS_COMMAND_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsContextlessCommandDef.class, 2);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CONTEXTLESS_COMMAND.getIcon(16);
        }
    }

    public static class InsertAdsScopeCommandIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsScopeCommandIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_SCOPE_COMMAND_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsScopeCommandDef.class, 2);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.COMMAND.getIcon(16);
        }
    }

    public static class InsertAdsEnumIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsEnumIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_ENUM_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsEnumDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return RadixObjectIcon.ENUM.getIcon(16);
        }
    }

    public static class InsertAdsRoleIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsRoleIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_ROLE_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsRoleDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.ROLE.getIcon(16);
        }
    }

    public static class InsertAdsDomainIdTagAction extends ScmlInsertTagAction {

        public InsertAdsDomainIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_DOMAIN_ID_TAG_ACTION, editor);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.DOMAIN.getIcon(16);
        }

        @Override
        protected List<Tag> createTags() {
            AdsDomainDef domainDef = ChooseDomain.chooseDomain(getContextDefinition());
            if (domainDef == null) {
                return null;
            }
            Scml.Tag tag = IdTagFactory.createIdTag(domainDef, getPane());
            if (tag != null) {
                return Collections.singletonList(tag);
            }
            return null;
        }

        private RadixObject getContextDefinition() {
            return getPane().getScml();
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isJml(scml) || isAdsSqml(scml) || isSqml(scml);
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
            return InsertIdTagActions.class;
        }
    }

    public static class InsertAdsEditorPresentationIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsEditorPresentationIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_EDITOR_PRESENTATION_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsEditorPresentationDef.class, 2);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.EDITOR_PRESENTATION.getIcon(16);
        }
    }

    public static class InsertAdsSelectorPresentationIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsSelectorPresentationIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_SELECTOR_PRESENTATION_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsSelectorPresentationDef.class, 2);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.SELECTOR_PRESENTATION.getIcon(16);
        }
    }

    public static class InsertAdsEditorPageIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsEditorPageIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_EDITOR_PAGE_ID_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {

            List<Tag> tags = new LinkedList<Tag>();
            DefinitionChooser.IDefinitionPathCreator pathCreator = new DefinitionChooser.IDefinitionPathCreator() {
                @Override
                public List<Definition> createDefinitionPath(Definition definition) {
                    if (definition instanceof AdsClassDef) {
                        return new LinkedList<Definition>(Collections.singleton(definition));
                    }
                    List<Definition> ownerPath = createDefinitionPath(definition.getOwnerDefinition());
                    ownerPath.add(definition);
                    return ownerPath;
                }
            };

            VisitorProvider provider = new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return AdsEditorPageDef.class.isInstance(radixObject);
                }
            };

            Definition def = DefinitionChooser.choose(getPane().getScml(), pathCreator, provider);
            if (def != null) {
                tags.add(createTag(def));
            }
            return tags;
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.EDITOR_PAGE.getIcon(16);
        }
    }

    public static class InsertAdsFilterIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsFilterIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_FILTER_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsFilterDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.FILTER.getIcon(16);
        }
    }

    public static class InsertAdsSortingIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsSortingIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_SORTING_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsSortingDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.SORTING.getIcon(16);
        }
    }

    public static class InsertAdsExplorerItemIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsExplorerItemIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_EXPLORER_ITEM_ID_TAG_ACTION, editor);
        }

        @Override
        public List<Tag> createTags() {
            VisitorProvider vp = new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (AdsExplorerItemDef.class.isInstance(radixObject)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            DefinitionChooser.IDefinitionPathCreator pathCreator = new DefinitionChooser.IDefinitionPathCreator() {
                @Override
                public List<Definition> createDefinitionPath(Definition definition) {
                    assert definition != null : "Can not create path for null definition";
                    if (definition instanceof AdsModule) {
                        return new LinkedList<Definition>();
                    }
                    List<Definition> ownerPath = createDefinitionPath(definition.getOwnerDefinition());
                    ownerPath.add(definition);
                    return ownerPath;
                }
            };

            LinkedList<Tag> tags = new LinkedList<Tag>();

            Definition def = DefinitionChooser.choose(getPane().getScml(), pathCreator, vp);
            if (def != null) {
                tags.add(createTag(def));
            }
            return tags;
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.ENTITY_EXPLORER_ITEM.getIcon(16);
        }
    }

    public static class InsertAdsCustomDialogIdTag extends InsertAdsIdTagAction {

        public InsertAdsCustomDialogIdTag(ScmlEditor editor) {
            super(INSERT_ADS_CUSTOM_DIALOG_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsCustomDialogDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CUSTOM_DIALOG.getIcon(16);
        }
    }

    public static class InsertAdsCustomPropEditorIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsCustomPropEditorIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_CUSTOM_PROP_EDITOR_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsCustomPropEditorDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CUSTOM_PROP_EDITOR.getIcon(16);
        }
    }

    public static class InsertAdsCustomParagEditorIdtagAction extends InsertAdsIdTagAction {

        public InsertAdsCustomParagEditorIdtagAction(ScmlEditor editor) {
            super(INSERT_ADS_CUSTOM_PARAG_EDITOR_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsCustomParagEditorDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CUSTOM_PARAG_EDITOR.getIcon(16);
        }
    }

    public static class InsertAdsCustomEditorIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsCustomEditorIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_CUSTOM_EDITOR_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsCustomEditorDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CUSTOM_EDITOR.getIcon(16);
        }
    }

    public static class InsertAdsCustomSelectorIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsCustomSelectorIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_CUSTOM_SELECTOR_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsCustomSelectorDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CUSTOM_SELECTOR.getIcon(16);
        }
    }

    public static class InsertAdsXmlSchemeIdTagAction extends InsertAdsIdTagAction {

        public InsertAdsXmlSchemeIdTagAction(ScmlEditor editor) {
            super(INSERT_ADS_XML_SCHEME_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsXmlSchemeDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.XML_SCHEME.getIcon(16);
        }
    }

    public static class InsertMsdlSchemeIdTagAction extends InsertAdsIdTagAction {

        public InsertMsdlSchemeIdTagAction(ScmlEditor editor) {
            super(INSERT_MSDL_SCHEME_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), AdsMsdlSchemeDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.MSDL_SCHEME.getIcon(16);
        }
    }

    public static class InsertAccessPartitionFamilyTagAction extends InsertAdsIdTagAction {

        public InsertAccessPartitionFamilyTagAction(ScmlEditor editor) {
            super(INSERT_ACCESS_PARTITION_FAMILY_ID_TAG_ACTION, editor);
        }

        @Override
        protected ChooseDefinitionCfg createCfg() {
            return ChooseDefinitionCfgFactory.createCfgForIdTag(getPane().getScml(), DdsAccessPartitionFamilyDef.class, 1);
        }

        @Override
        public Icon getIcon() {
            return DdsDefinitionIcon.ACCESS_PARTITION_FAMILY.getIcon(16);
        }
    }

    public static class DefinitionChooser {

        private static class Node {

            private final Definition definition;
            private final Map<Definition, Node> def2child = new HashMap<Definition, Node>();

            public Node(Definition definition) {
                this.definition = definition;
            }

            public void addChildNode(Node node) {
                def2child.put(node.definition, node);
            }

            public Collection<Definition> getChildDefinitions() {
                return def2child.keySet();
            }

            public boolean hasChildDefinition(Definition childDef) {
                return def2child.containsKey(childDef);
            }

            public Node getChildNode(Definition childDef) {
                return def2child.get(childDef);
            }
        }

        /**
         * ChooseObject with non-constant count of steps. Created to choose
         * definitions from tree structure. It takes context and VisitorProvider
         * and collect all target definitions via
         * {@code DefinitionsUtils.collectTopAround(context, targetVisitorProvider)}.
         * After that, it asks pathCreator to create sequence of definitions
         * which user must select to choose target definition. For example, if
         * target definition is targetDef, and definiton sequence is
         * (ownerOwnerDef, ownerDef, targetDef), then user have to select
         * ownerOwnerDef on first step, ownerDef on second step, and on third
         * step he can select targetDef. The key point is that pathCreator can
         * return arbitrary long path for any target definition, so user can
         * select one definition in one step, but another in three steps.
         *
         * @return
         */
        public static Definition choose(RadixObject context, IDefinitionPathCreator pathCreator, VisitorProvider targetVisitorProvider) {
            Collection<Definition> targets = DefinitionsUtils.collectAllAround(context, targetVisitorProvider);
            List<Definition> path;
            HashMap<Definition, Node> def2node = new HashMap<Definition, Node>();
            Node rootNode = new Node(null);
            for (Definition target : targets) {
                path = pathCreator.createDefinitionPath(target);
                Node node = rootNode;
                for (Definition pathItem : path) {
                    Node childNode;
                    if (node.hasChildDefinition(pathItem)) {
                        childNode = node.getChildNode(pathItem);
                    } else {
                        childNode = new Node(pathItem);
                        node.addChildNode(childNode);
                        def2node.put(pathItem, childNode);
                    }
                    node = childNode;
                }
            }

            ChooseDefinitionCfgs cfgs = new ChooseDefinitionCfgsImpl(ChooseDefinitionCfg.Factory.newInstance(rootNode.getChildDefinitions()), def2node, targetVisitorProvider);
            List<Definition> definitions = ChooseDefinitionSequence.chooseDefinitionSequence(cfgs);
            if (definitions != null && !definitions.isEmpty()) {
                return definitions.get(definitions.size() - 1);
            }
            return null;
        }

        private static class ChooseDefinitionCfgsImpl extends ChooseDefinitionCfgs {

            private final Map<Definition, Node> def2node;
            private final VisitorProvider targetVisitorProvider;

            public ChooseDefinitionCfgsImpl(ChooseDefinitionCfg initialConfig, HashMap<Definition, Node> def2node, VisitorProvider targetVisitorProvider) {
                super(initialConfig);
                this.def2node = def2node;
                this.targetVisitorProvider = targetVisitorProvider;
            }

            @Override
            protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
                Node node = def2node.get(choosenDef);
                assert node != null;
                if (node.getChildDefinitions() == null || node.getChildDefinitions().size() == 0) {
                    return null;
                }
                return ChooseDefinitionCfg.Factory.newInstance(node.getChildDefinitions());
            }

            @Override
            protected boolean hasNextConfig(Definition choosenDef) {
                Node node = def2node.get(choosenDef);
                if (node.getChildDefinitions() == null || node.getChildDefinitions().size() == 0) {
                    return false;
                }
                return true;
            }

            @Override
            protected boolean isFinalTarget(Definition choosenDef) {
                return targetVisitorProvider.isTarget(choosenDef);
            }

            @Override
            public String getDisplayName() {
                return NbBundle.getMessage(InsertAdsEditorPageIdTagAction.class, "choose-definition-title");
            }
        }

        public static interface IDefinitionPathCreator {

            List<Definition> createDefinitionPath(Definition definition);
        }
    }
}
