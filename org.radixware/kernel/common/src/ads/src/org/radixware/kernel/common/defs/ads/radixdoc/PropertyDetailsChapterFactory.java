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
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEventCodePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ColumnProperty;
import org.radixware.kernel.common.defs.ads.clazz.members.DbPropertyValue;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.members.ValueInheritanceRules;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation.ParentSelect;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation.ParentTitle;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassRadixdoc.DetailMembersChapterFactory;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.enums.EAccess;
import static org.radixware.kernel.common.enums.EPropNature.DYNAMIC;
import static org.radixware.kernel.common.enums.EPropNature.EVENT_CODE;
import static org.radixware.kernel.common.enums.EPropNature.EXPRESSION;
import static org.radixware.kernel.common.enums.EPropNature.INNATE;
import static org.radixware.kernel.common.enums.EPropNature.PARENT_PROP;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import static org.radixware.kernel.common.enums.EPropNature.USER;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Table;

public class PropertyDetailsChapterFactory extends DetailMembersChapterFactory<AdsPropertyDef> {

    private final ClassRadixdoc classDoc;
    private final Block container;
    private final ClassRadixdoc.ClassWriter clWriter;

    public PropertyDetailsChapterFactory(ClassRadixdoc classDoc, Block container) {
        this.classDoc = classDoc;
        this.container = container;
        clWriter = classDoc.getClassWriter();
    }

    public ClassRadixdoc getClassDoc() {
        return classDoc;
    }

    @Override
    void documentMemberDetail(AdsPropertyDef member, ContentContainer contentBlock) {
        contentBlock.setStyle(DefaultStyle.NAMED);
        
        final Block title = clWriter.addBlockTitle(contentBlock);
        final TypeDocument typeDoc = new TypeDocument();
        
        typeDoc.addString(member.getTypeTitle()).addString(" ").addType(member.getValue().getType(), classDoc.getSource());
        clWriter.documentType(title, typeDoc, classDoc.getSource());

        final RadixIconResource resource = new RadixIconResource(member.getIcon());
        title.addNewResource().setSource(resource.getKey());
        clWriter.addText(title, member.getName()).setStyle(DefaultStyle.IDENTIFIER);
        classDoc.addResource(resource);
        
        final Block descriptionBlock = contentBlock.addNewBlock();
        descriptionBlock.setStyle(DefaultStyle.DESCRIPTION);
        
        clWriter.setAttribute(contentBlock, DefaultAttributes.ANCHOR, member.getId().toString());
        clWriter.documentDescription(descriptionBlock, member);

        //Common property options
        Table notPresentAttrTable = clWriter.addGeneralAttrTable(descriptionBlock);
        List<String> modifiers = new ArrayList<>();
        modifiers.add(member.getAccessFlags().getAccessMode().getAsStr());
        if (member.isOverwrite()) {
            modifiers.add("Overwriting");
        }
        if (member.isOverride()) {
            modifiers.add("Overriding");
        }
        if (member.isDeprecated()) {
            modifiers.add("Deprecated");
        }
        if (member.isReadOnly()) {
            modifiers.add("Read Only");
        }
        if (member.isFinal()) {
            modifiers.add("Final");
        }
        if (!modifiers.isEmpty()) {
            clWriter.addAllStrRow(notPresentAttrTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        } else {
            clWriter.addAllStrRow(notPresentAttrTable, "Modifiers", "");
        }

        switch (member.getNature()) {
            case DETAIL_PROP:
                if (member instanceof AdsDetailColumnPropertyDef) {
                    AdsDetailColumnPropertyDef colProp = (AdsDetailColumnPropertyDef) member;
                    generateDetailColumnPropertyDoc(colProp, notPresentAttrTable);
                } else {
                    AdsDetailRefPropertyDef parRefProp = (AdsDetailRefPropertyDef) member;
                    generateDetailRefPropertyDoc(parRefProp, notPresentAttrTable);
                }
                break;
            case DYNAMIC:
                if (member instanceof AdsTransparentPropertyDef) {
                    AdsTransparentPropertyDef transProp = (AdsTransparentPropertyDef) member;
                    generateTransparentPropertyDoc(transProp, notPresentAttrTable);
                } else {
                    AdsDynamicPropertyDef dynProp = (AdsDynamicPropertyDef) member;
                    generateDynamicPropertyDoc(dynProp, notPresentAttrTable);
                }
                break;
            case EVENT_CODE:
                AdsEventCodePropertyDef evProp = (AdsEventCodePropertyDef) member;
                generateEventCodePropertyDoc(evProp, notPresentAttrTable);
                break;
            case EXPRESSION:
                AdsExpressionPropertyDef exprProp = (AdsExpressionPropertyDef) member;
                generateExpressionPropertyDoc(exprProp, notPresentAttrTable);
                break;
            case PARENT_PROP:
                AdsParentPropertyDef parProp = (AdsParentPropertyDef) member;
                generateParentPropertyDoc(parProp, notPresentAttrTable);
                break;
            case PROPERTY_PRESENTATION:
                AdsPropertyPresentationPropertyDef presProp = (AdsPropertyPresentationPropertyDef) member;
                generatePropertyPresentationDoc(presProp, notPresentAttrTable);
                break;
            case INNATE:
                if (member instanceof AdsInnateColumnPropertyDef) {
                    AdsInnateColumnPropertyDef colProp = (AdsInnateColumnPropertyDef) member;
                    generateInnateColumnPropertyDoc(colProp, notPresentAttrTable);
                } else {
                    AdsInnateRefPropertyDef refProp = (AdsInnateRefPropertyDef) member;
                    generateInnateRefPropertyDoc(refProp, notPresentAttrTable);
                }
                break;
            case FIELD_REF:
                AdsFieldRefPropertyDef fieldProp = (AdsFieldRefPropertyDef) member;
                generateFieldRefPropertyDoc(fieldProp, notPresentAttrTable, descriptionBlock);
                break;
            case USER:
                AdsUserPropertyDef userProp = (AdsUserPropertyDef) member;
                generateUserPropertyDoc(userProp, notPresentAttrTable);
                break;

            case GROUP_PROPERTY:
            case FIELD:
            case SQL_CLASS_PARAMETER:
            default:
        }

        generatePresentableInfo(member, descriptionBlock);
    }

    @Override
    ElementList createRootList() {
        final Block rootChapter = container.addNewBlock();
        clWriter.appendStyle(rootChapter, DefaultStyle.CHAPTER);
        clWriter.addStrTitle(rootChapter, "Properties Detail");

        final ElementList rootList = rootChapter.addNewList();
        clWriter.appendStyle(rootList, DefaultStyle.MEMBERS);
        return rootList;
    }

    @Override
    List<AdsPropertyDef> getAllMembers(AdsClassDef c) {
        return classDoc.getSource().getProperties().getLocal().list();
    }

    @Override
    ElementList.Item createItem() {
        return getRootChapter().addNewItem();
    }

    private void generateDynamicPropertyDoc(AdsDynamicPropertyDef dynProp, Table contentTable) {
        clWriter.addStr2BoolRow(contentTable, "Property of class", dynProp.getAccessFlags().isStatic());
    }

    private void generateTransparentPropertyDoc(AdsTransparentPropertyDef transProp, Table contentTable) {
        clWriter.addAllStrRow(contentTable, "Published property name", transProp.getTransparence().getPublishedName());
    }

    private void generateDetailColumnPropertyDoc(AdsDetailColumnPropertyDef colProp, Table contentTable) {
        clWriter.addStr2RefRow(contentTable, "Detail column", colProp.getColumnInfo().findColumn(), getClassDoc().getSource());
        writeInheritanceInfo(colProp, contentTable);
    }

    private void generateDetailRefPropertyDoc(AdsDetailRefPropertyDef refProp, Table contentTable) {
        clWriter.addStr2RefRow(contentTable, "Detail reference", refProp.getParentReferenceInfo().findParentReference(), getClassDoc().getSource());
        writeInheritanceInfo(refProp, contentTable);
    }

    private void generateEventCodePropertyDoc(AdsEventCodePropertyDef eventProp, Table contentTable) {
        Collection<ILocalizedDef.MultilingualStringInfo> messages = new ArrayList<>();
        eventProp.collectUsedMlStringIds(messages);
        int num = 1;
        for (ILocalizedDef.MultilingualStringInfo mes : messages) {
            if (mes.getId() != null) {
                clWriter.addStr2MslIdRow(contentTable, "Message #" + num++, eventProp.getLocalizingBundleId(), mes.getId());
            }
        }
        clWriter.addAllStrRow(contentTable, "Event source", eventProp.findEventCode().getEventSource());

        String severity = "<Not Defined>";
        AdsEventCodeDef evCode = eventProp.findEventCode();
        if (evCode != null && evCode.getEventSeverity() != null) {
            severity = evCode.getEventSeverity().getName();
        }
        clWriter.addAllStrRow(contentTable, "Severity", severity);
    }

    private void generateExpressionPropertyDoc(AdsExpressionPropertyDef exprProp, Table contentTable) {
        DbPropertyValue dbPropVal = (DbPropertyValue) exprProp.getValue();

        clWriter.addStr2BoolRow(contentTable, "Invisible for ARTE", exprProp.isInvisibleForArte());
        clWriter.addAllStrRow(contentTable, "DB Type", dbPropVal.getDbType());
    }

    private void generateInnateColumnPropertyDoc(AdsInnateColumnPropertyDef colProp, Table contentTable) {
        clWriter.addStr2RefRow(contentTable, "Database column", colProp.getColumnInfo().findColumn(), getClassDoc().getSource());
        writeInheritanceInfo(colProp, contentTable);
    }

    private void generateInnateRefPropertyDoc(AdsInnateRefPropertyDef refProp, Table contentTable) {
        clWriter.addStr2RefRow(contentTable, "Database reference", refProp.getParentReferenceInfo().findParentReference(), getClassDoc().getSource());
        writeInheritanceInfo(refProp, contentTable);
    }

    private void generateParentPropertyDoc(AdsParentPropertyDef parentRefProp, Table contentTable) {
        clWriter.addStr2RefRow(contentTable, "Parent property", parentRefProp.getParentInfo().findOriginalProperty(), getClassDoc().getSource());
        writeInheritanceInfo(parentRefProp, contentTable);
    }

    private void generatePropertyPresentationDoc(AdsPropertyPresentationPropertyDef presProp, Table contentTable) {
        AdsClassDef embeddedClass = presProp.findEmbeddedClass(ExtendableDefinitions.EScope.ALL).get();
        if (embeddedClass != null) {
            clWriter.addAllStrRow(contentTable, "Embedded class methods", "");
            for (AdsMethodDef method : embeddedClass.getMethods().getAll(ExtendableDefinitions.EScope.ALL)) {
                if (method.isOverride() || (method.isPublished() && !method.getAccessMode().isLess(EAccess.PRIVATE))) {
                    clWriter.addAllStrRow(contentTable, method.getQualifiedName(), "");
                }
            }
        }
    }

    private void generateFieldRefPropertyDoc(AdsFieldRefPropertyDef fieldProp, Table contentTable, ContentContainer detailsBlock) {
        DdsTableDef refTable = fieldProp.findReferencedTable();
        clWriter.addStr2RefRow(contentTable, "Type", refTable, getClassDoc().getSource());
        clWriter.addAllStrRow(contentTable, "Reference To", fieldProp.findUsedIndex() != null ? fieldProp.findUsedIndex().getName() : "<not defined>");

        Table column2CursorField = clWriter.addNewTable(detailsBlock, "Table Column", "Cursor Field");
        for (AdsFieldRefPropertyDef.RefMapItem item : fieldProp.getFieldToColumnMap()) {
            final DdsColumnDef column = refTable.getColumns().findById(item.getColumnId(), ExtendableDefinitions.EScope.ALL).get();
            final AdsDefinition field = fieldProp.getOwnerClass().findComponentDefinition(item.getFieldId()).get();
            if (column != null && field != null) {
                Table.Row row = column2CursorField.addNewRow();
                clWriter.addRef(row.addNewCell(), column, column.getModule());
                clWriter.addRef(row.addNewCell(), field, field.getOwnerDef());
            }
        }
    }

    private void generateUserPropertyDoc(AdsUserPropertyDef userProp, Table contentTable) {
        clWriter.addStr2BoolRow(contentTable, "Audit update", userProp.isAuditUpdate());
    }

    private void writeInheritanceInfo(AdsPropertyDef member, Table detailsTable) {
        boolean isInheritanceAllowed = member.getValueInheritanceRules().getInheritable();
        clWriter.addStr2BoolRow(detailsTable, "Value inheritance allowed", isInheritanceAllowed);
        if (isInheritanceAllowed) {
            clWriter.addAllStrRow(detailsTable, "Value initialization mode", member.getValueInheritanceRules().getInitializationPolicy().getName());
            for (int i = 0; i < member.getValueInheritanceRules().getPathes().size(); i++) {
                ValueInheritanceRules.InheritancePath path = member.getValueInheritanceRules().getPathes().get(i);

                StringBuilder pathBuilder = new StringBuilder("this->");
                List<Definition> dependences = new ArrayList<>(2);
                path.collectDependences(dependences);
                if (dependences.size() == 2) {
                    if (dependences.get(1) instanceof ColumnProperty) {
                        ColumnProperty parentTableCol = (ColumnProperty) dependences.get(1);
                        pathBuilder.append(parentTableCol.getColumnInfo().findColumnTable().getName());
                    }
                    pathBuilder.append('(');
                    pathBuilder.append(dependences.get(0).getName());
                    pathBuilder.append(").");
                    pathBuilder.append(dependences.get(1).getName());
                } else {
                    pathBuilder.append(dependences.get(0).getName());
                }
                clWriter.addAllStrRow(detailsTable, "Inheritance Path #" + (i + 1), pathBuilder.toString());
            }
        }
    }

    private void generatePresentableInfo(AdsPropertyDef member, ContentContainer detailBlock) {
        if (member instanceof IAdsPresentableProperty) {
            ServerPresentationSupport presentSupport = ((IAdsPresentableProperty) member).getPresentationSupport();
            if (presentSupport != null) {
                PropertyPresentation presentation = presentSupport.getPresentation();
                if (presentation != null && presentation.isPresentable()) {

                    Block titlesBlock = detailBlock.addNewBlock();
                    Table titlesTable = clWriter.setBlockCollapsibleAndAddTable(titlesBlock, "Titles");
                    generateTitlesInfo(presentation, titlesTable);

                    Block presentInfoBlock = detailBlock.addNewBlock();
                    Table presentInfoTable = clWriter.setBlockCollapsibleAndAddTable(presentInfoBlock, "Presentation Attributes");

                    if (presentation instanceof ParentRefPropertyPresentation) {
                        generateEditingInfo(presentation, presentInfoTable, false);

                        ParentRefPropertyPresentation parentRefPropPresentation = (ParentRefPropertyPresentation) presentation;
                        Table parentRefPesentTable = clWriter.setBlockCollapsibleAndAddTable(detailBlock.addNewBlock(), "Parent Ref");
                        generateParentRefPresentationInfo(parentRefPropPresentation, parentRefPesentTable);

                        Table restrictionsTable = clWriter.setBlockCollapsibleAndAddTable(detailBlock.addNewBlock(), "Restrictions");
                        generateRestrictionsPresentationInfo(parentRefPropPresentation, restrictionsTable);
                    } else {
                        generateEditingInfo(presentation, presentInfoTable, true);
                    }
                }
            }
        }
    }

    private void generateTitlesInfo(PropertyPresentation presentation, Table titlesTable) {
        if (presentation.getTitleId() != null) {
            clWriter.addStr2MslIdRow(titlesTable, "Title", presentation.getOwnerProperty().getLocalizingBundleId(), presentation.getTitleId());
        } else {
            clWriter.addAllStrRow(titlesTable, "Title", "");
        }
        if (presentation.getEditOptions().getNullValTitleId() != null) {
            clWriter.addStr2MslIdRow(titlesTable, "Display instead of '<Not Defined>'", presentation.getOwnerProperty().getLocalizingBundleId(), presentation.getEditOptions().getNullValTitleId());
        } else {
            clWriter.addAllStrRow(titlesTable, "Display instead of '<Not Defined>'", "");
        }
        if (presentation.getHintId() != null) {
            clWriter.addStr2MslIdRow(titlesTable, "Tool Tip", presentation.getOwnerProperty().getLocalizingBundleId(), presentation.getHintId());
        } else {
            clWriter.addAllStrRow(titlesTable, "Tool Tip", "");
        }
    }

    private void generateEditingInfo(PropertyPresentation presentation, Table presentInfoTable, boolean isObjectPresentation) {
        PropertyEditOptions editOptions = presentation.getEditOptions();
        if (!ERuntimeEnvironmentType.COMMON_CLIENT.equals(editOptions.getEditEnvironment())) {
            clWriter.addAllStrRow(presentInfoTable, "Available to edit in", editOptions.getEditEnvironment().getName());
        }
        if (isObjectPresentation) {
            String editMaskType = "Default edit mask";
            if (editOptions.getEditMask() != null) {
                editMaskType = editOptions.getEditMask().getType().getAsStr();
            }
            clWriter.addAllStrRow(presentInfoTable, "Input mask type", editMaskType);
            if (!EPropertyValueStorePossibility.NONE.equals(editOptions.getValueStorePossibility())) {
                clWriter.addAllStrRow(presentInfoTable, "Value import/export", editOptions.getValueStorePossibility().getName());
            }
        }

        clWriter.addStr2BoolRow(presentInfoTable, "Use custom dialog", editOptions.isShowDialogButton());
        clWriter.addStr2BoolRow(presentInfoTable, "Custom edit only", editOptions.isCustomEditOnly());

        if (editOptions.findCustomDialog(ERuntimeEnvironmentType.EXPLORER) != null) {
            clWriter.addAllStrRow(presentInfoTable, "Desktop variant", editOptions.findCustomDialog(ERuntimeEnvironmentType.EXPLORER).getDialogDef().getName());
        }
        if (editOptions.findCustomDialog(ERuntimeEnvironmentType.WEB) != null) {
            clWriter.addAllStrRow(presentInfoTable, "Web variant", editOptions.findCustomDialog(ERuntimeEnvironmentType.WEB).getDialogDef().getName());
        }

        clWriter.addStr2BoolRow(presentInfoTable, "Not null", editOptions.isNotNull());
        clWriter.addStr2BoolRow(presentInfoTable, "Store edit history", editOptions.isStoreEditHistory());
        if (isObjectPresentation) {
            clWriter.addStr2BoolRow(presentInfoTable, "Memo", editOptions.isMemo());
            clWriter.addStr2BoolRow(presentInfoTable, "Read separately", editOptions.isReadSeparately());

        }
        clWriter.addAllStrRow(presentInfoTable, "Edit possibility", editOptions.getEditPossibility().getName());
        if (!isObjectPresentation) {
            int num = 1;
            for (AdsEditorPresentationDef editor : editOptions.findObjectEditorPresentations()) {
                clWriter.addAllStrRow(presentInfoTable, "Parent editor presentation #" + num++, editor.getQualifiedName());
            }
        }
    }

    private void generateParentRefPresentationInfo(ParentRefPropertyPresentation presentation, Table contentTable) {
        ParentSelect selectInfo = presentation.getParentSelect();
        if (selectInfo != null) {
            clWriter.addStr2BoolRow(contentTable, "Inherit parent selector", selectInfo.isParentSelectorInherited());
            AdsSelectorPresentationDef parentSelector = selectInfo.findParentSelectorPresentation();
            if (parentSelector != null) {
                clWriter.addStr2RefRow(contentTable, "Parent selector presentation", parentSelector, parentSelector.getOwnerDef());
            } else {
                clWriter.addAllStrRow(contentTable, "Parent selector presentation", "<Not Defined>");
            }
            clWriter.addStr2BoolRow(contentTable, "Inherit parent select condition", selectInfo.isParentSelectConditionInherited());
        }

        ParentTitle parentTitle = presentation.getParentTitle();
        if (parentTitle != null) {
            clWriter.addStr2BoolRow(contentTable, "Inherit format titles", parentTitle.isParentTitleFormatInherited());
            int num = 1;
            for (TitleItem item : parentTitle.getTitleFormat().getItems()) {
                AdsPropertyDef prop = item.findProperty();
                if (prop != null) {
                    Table.Row titleFormatRow = contentTable.addNewRow();
                    titleFormatRow.addNewCell().addNewText().setStringValue("Title format #" + num++);
                    Table.Row.Cell refCell = titleFormatRow.addNewCell();
                    clWriter.addRef(refCell, prop, classDoc.getSource());
                    clWriter.addText(refCell, " = " + item.getPattern());
                }
            }
            if (parentTitle.getTitleFormat().getNullValTitleId() != null) {
                clWriter.addStr2MslIdRow(contentTable, "Display instead of null", parentTitle.getTitleFormat().getLocalizingBundleId(), parentTitle.getTitleFormat().getNullValTitleId());
            } else {
                clWriter.addAllStrRow(contentTable, "Display instead of null", "");
            }
        }
    }

    private void generateRestrictionsPresentationInfo(ParentRefPropertyPresentation presentation, Table contentTable) {
        Restrictions selectorRestrictions = presentation.getParentSelectorRestrictions();
        long selRestrs = ERestriction.RUN_EDITOR.getValue()
                | ERestriction.CREATE.getValue()
                | ERestriction.DELETE.getValue()
                | ERestriction.DELETE_ALL.getValue()
                | ERestriction.MULTIPLE_COPY.getValue()
                | ERestriction.ANY_COMMAND.getValue()
                | ERestriction.UPDATE.getValue();
        clWriter.addAllStrRow(contentTable, "Parent selector prohibided actions:", clWriter.getRestrictionsAsStr(selRestrs & selectorRestrictions.toBitField()));

        Restrictions editorRestrictions = presentation.getParentEditorRestrictions();
        long editorRestrs = ERestriction.UPDATE.getValue()
                | ERestriction.ANY_COMMAND.getValue()
                | ERestriction.VIEW.getValue();
        clWriter.addAllStrRow(contentTable, "Parent editor prohibided actions:", clWriter.getRestrictionsAsStr(editorRestrs & editorRestrictions.toBitField()));
    }
}
