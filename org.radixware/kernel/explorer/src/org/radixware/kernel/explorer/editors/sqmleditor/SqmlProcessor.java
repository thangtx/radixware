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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCharFormat.UnderlineStyle;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QWidget;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.ChooseSqmlDefinitionDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.*;//NOPMD
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.TagProcessor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;
import org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner;


public class SqmlProcessor extends TagProcessor {

    private ISqmlTableDef contextClass;
    private ISqmlParameters parameters;
    private org.radixware.schemas.xscml.Sqml xSqml;
    private SqmlTagInsertion tagInsertion;
    //private IClientEnvironment environment;

    public SqmlProcessor(final IClientEnvironment environment,final ISqmlTableDef contextClass, final ISqmlParameters parameters) {
        super(environment);
        //this.environment = environment;
        this.contextClass = contextClass;
        this.parameters = parameters;
    }

    public void setSqmlTagInsertion(final SqmlTagInsertion tagInsertion) {
        this.tagInsertion = tagInsertion;
    }

    public SqmlTagInsertion getSqmlTagInsertion() {
        return tagInsertion;
    }

    public void setParameters(final ISqmlParameters parameters) {
        this.parameters = parameters;
    }

    public void setContextClass(final ISqmlTableDef contextClass) {
        this.contextClass = contextClass;
        hystoryIndex = 0;
        tagListHystory.clear();
    }

    public void setSqml(final org.radixware.schemas.xscml.Sqml javaSrc) {
        this.xSqml = javaSrc;
    }

    public TagInfo createTag(final Object obj, final long pos, final List<ISqmlTableReference> path, final String tableAlias) {
        TagInfo tag = null;
        if (obj instanceof ISqmlTableDef) {
            final ISqmlTableDef classDef = (ISqmlTableDef) obj;
            if ((classDef.equals(contextClass)) && (tableAlias == null)) {
                tag = new SqmlTag_ThisTableSqlName(environment, classDef, pos);
            } else {
                tag = new SqmlTag_TableSqlName(environment, classDef, pos, showMode);
            }
        } else if (obj instanceof ISqmlTableReference) {
            final ISqmlTableReference classDef = (ISqmlTableReference) obj;
            tag = new SqmlTag_TableSqlName(environment, classDef, pos, showMode);
        } else if (obj instanceof ISqmlEnumItem) {
            final ISqmlEnumItem constSetItem = (ISqmlEnumItem) obj;
            tag = new SqmlTag_ConstValue(environment, constSetItem, pos, showMode);
        } else if (obj instanceof ISqmlColumnDef) {
            final ISqmlColumnDef prop = (ISqmlColumnDef) obj;
            final ISqmlTableDef owner = prop.getOwnerTable();
            if ((tableAlias == null) && (path == null)) {
                if (owner.equals(contextClass)) {
                    tag = new SqmlTag_PropSqlName(environment, prop, pos, Owner.THIS, showMode, tableAlias);
                } else {
                    tag = new SqmlTag_PropSqlName(environment, prop, pos, Owner.TABLE, showMode, tableAlias);
                }
            } else {
                if ((tableAlias == null) && (contextClass != null)) {
                    tag = new SqmlTag_ParentRefPropSqlName(environment, prop, owner, contextClass, path, pos, showMode);
                } else {
                    tag = new SqmlTag_PropSqlName(environment, prop, pos, Owner.TABLE, showMode, tableAlias);
                }
            }
        } else if (obj instanceof ISqmlParameter) {//add by yremizov
            final ISqmlParameter parameter = (ISqmlParameter) obj;
            if (parameter.getType() == EValType.PARENT_REF || parameter.getType() == EValType.ARR_REF) {
                tag = new SqmlTag_EntityRefParameter(environment, parameter, pos, contextClass, parameters, showMode);
            } else {
                tag = new SqmlTag_Parameter(environment, parameter, pos, contextClass, parameters, showMode);
            }
        }
        return tag;
    }

    public SqmlTag_PropSqlName createPropSqlName(final ISqmlColumnDef prop, final long pos) {
        return new SqmlTag_PropSqlName(environment, prop, pos, Owner.TABLE, showMode, null);
    }

    public SqmlTag_EventCode createEventCode(final ISqmlEventCodeDef eventCodeDef, final long pos) {
        return new SqmlTag_EventCode(environment, pos, eventCodeDef, showMode);
    }
    
    public SqmlTag_ThisTableSqlName createThisTableSqlName(final ISqmlTableDef classDef, final long pos) {
        return new SqmlTag_ThisTableSqlName(environment, classDef, pos);
    }

    public SqmlTag_EntityRefValue createEntityRefValue(final Pid pid, final long pos, final boolean isPrimaryKey, final Id secondaryKeyId) {
        return new SqmlTag_EntityRefValue(environment, pid, pos, showMode, isPrimaryKey, secondaryKeyId);
    }

    public SqmlTag_TableSqlName createTableSqlName(final ISqmlTableDef classDef, final long pos) {
        return new SqmlTag_TableSqlName(environment, classDef, pos, showMode);
    }
    
    public SqmlTag_IdPath createIdPath(final ISqmlDefinition definition, final long pos, final boolean isTable) {
        return new SqmlTag_IdPath(environment, definition, pos, showMode,isTable);
    }    

    public SqmlTag_ThisTableRef createThisTableRef(final ISqmlTableDef classDef, final long pos) {
        return new SqmlTag_ThisTableRef(environment, classDef, pos, showMode);
    }

    public SqmlTag_ParentCondition createParentCondition(final ISqmlColumnDef prop, final Operator.Enum operator,final Reference pid, final long pos, final String tableAlias) {
        return new SqmlTag_ParentCondition(environment, prop, operator, pid, pos, showMode, tableAlias);
    }

    public SqmlTag_TypifiedValue createTypifiedValue(final ISqmlColumnDef prop, final Object value, final long pos) {
        return new SqmlTag_TypifiedValue(environment, prop, value, pos, showMode);
    }

    public SqmlTag_DbFuncCall createDbFuncCall(final ISqmlFunctionDef functionDef, final Map<Id, String> paramValues, final long pos) {
        return new SqmlTag_DbFuncCall(environment, functionDef, paramValues, pos, showMode);
    }        

    @Override
    public void toHtml(final QTextCursor tc, final QTextCharFormat charFormat) {
        toHtml(tc, charFormat, xSqml);
    }

    public void toHtml(final QTextCursor tc, final QTextCharFormat charFormat, final org.radixware.schemas.xscml.Sqml javaSrc1) {
        final List<TagInfo> tagList = new ArrayList<>();
        if ((javaSrc1 != null) && (javaSrc1.getItemList() != null)) {
            final List<org.radixware.schemas.xscml.Sqml.Item> itemList = javaSrc1.getItemList();
            if ((tagListHystory != null) && (tagListHystory.size() > 0) && getCurrentTagList()!=null) {
                for (int i = 0; i < getCurrentTagList().size(); i++) {
                    tagList.add(getCurrentTagList().get(i).copy());
                }
            }
            int insertIndex = getInsertTagIndex(tagList, tc.position() + 1);
            final Map<String, Id> aliases = new HashMap<>();
            for (int j = 0, size = itemList.size(); j < size; j++) {
                final org.radixware.schemas.xscml.Sqml.Item item = itemList.get(j);
                if (item.isSetSql()) {
                    String s = itemList.get(j).getSql();
                    tc.insertText(s, charFormat);
                } else {
                    TagInfo tag;
                    if (item.isSetThisTableId()) {
                        tag = new SqmlTag_ThisTableSqlId(environment, contextClass, tc.position() + 1);
                    } else if (item.isSetThisTableSqlName()) {
                        tag = new SqmlTag_ThisTableSqlName(environment, contextClass, tc.position() + 1);
                    } else if (item.isSetTableSqlName()) {
                        tag = new SqmlTag_TableSqlName(environment, item.getTableSqlName(), tc.position() + 1, showMode);
                        final String tableAlias = item.getTableSqlName().getTableAlias();
                        if(tableAlias != null && !tableAlias.isEmpty()) {
                           aliases.put(tableAlias, item.getTableSqlName().getTableId());
                        }
                    } else if (item.isSetPropSqlName()) {
                        tag = new SqmlTag_PropSqlName(environment, item.getPropSqlName(), tc.position() + 1, showMode);
                        final String tableAlias = item.getPropSqlName().getTableAlias();
                        if(tableAlias != null && !tableAlias.isEmpty()) {
                           aliases.put(tableAlias, item.getPropSqlName().getTableId());
                        }
                    } else if (item.isSetParentRefPropSqlName()) {
                        tag = new SqmlTag_ParentRefPropSqlName(environment, item.getParentRefPropSqlName(), contextClass, tc.position() + 1, showMode);
                    } else if (item.isSetTypifiedValue()) {
                        tag = new SqmlTag_TypifiedValue(environment, item.getTypifiedValue(), tc.position() + 1, showMode);
                    } else if (item.isSetConstValue()) {
                        tag = new SqmlTag_ConstValue(environment, item.getConstValue(), tc.position() + 1, showMode);
                    } else if (item.isSetParentCondition()) {
                        tag = new SqmlTag_ParentCondition(environment, item.getParentCondition(), tc.position() + 1, showMode);
                    } else if (parameters != null && item.isSetParameter()) {
                        tag = new SqmlTag_Parameter(environment, item.getParameter(), parameters, tc.position() + 1, contextClass, showMode);
                    } else if (parameters != null && item.isSetEntityRefParameter()) {
                        tag = new SqmlTag_EntityRefParameter(environment, item.getEntityRefParameter(), parameters, tc.position() + 1, contextClass, showMode);
                    } else if (item.isSetDbFuncCall()) {
                        tag = new SqmlTag_DbFuncCall(environment, item.getDbFuncCall(), tc.position() + 1, showMode);
                    } else if (item.isSetThisTableRef()) {
                        tag = new SqmlTag_ThisTableRef(environment, item.getThisTableRef(), tc.position() + 1, contextClass, showMode);
                    } else if (item.isSetEntityRefValue()) {
                        tag = new SqmlTag_EntityRefValue(environment, item.getEntityRefValue(), tc.position() + 1, showMode);
                    } else if (item.isSetId()){
                        tag = new SqmlTag_IdPath(environment, item.getId(), tc.position() + 1, showMode);
                    } else if (item.isSetEventCode()){
                        tag = new SqmlTag_EventCode(environment, tc.position() + 1, item.getEventCode(), showMode);
                    } else if (item.isSetDbName()) {
                        tag = new SqmlTag_DbName(environment, tc.position() + 1, item.getDbName(), showMode);
                    } else if (item.isSetTargetDbPreprocessor()){
                        tag = new SqmlTag_TargetDbPreprocessorTag(environment, tc.position() + 1, item.getTargetDbPreprocessor());
                    } else if (item.isSetElseIf()){
                        tag = new SqmlTag_ElseIf(environment, tc.position() + 1);
                    } else if (item.isSetEndIf()){
                        tag = new SqmlTag_EndIf(environment, tc.position() + 1);
                    } else{
                        tag = new SqmlTag_UnknownTag(environment, tc.position() + 1, item);
                    }
                    //if (tag != null) {
                        tagList.add(insertIndex, tag);
                        insertIndex = insertIndex + 1;
                        String s = tag.createHtmlTag("");
                        tc.insertHtml(s);
                    //}
                }
            }
            if(!aliases.isEmpty()) 
                tagInsertion.getEditor().setAliases(aliases);
            addHystory(false);
            this.tagListHystory.add(tagList);
            hystoryIndex = this.tagListHystory.size() - 1;
        } else {
            this.tagListHystory.add(tagList);
            tc.insertText("");
        }
    }

    public org.radixware.schemas.xscml.Sqml toXml(final String plaintText, final QTextCursor tc) {
        List<TagInfo> tagList = getCurrentTagList();
        return toXml(plaintText, tagList, tc, 0, plaintText.length());
    }

    public org.radixware.schemas.xscml.Sqml toXml(final String plaintText, final List<TagInfo> tagList, final QTextCursor tc, final int pos, final int endpos) {
        final org.radixware.schemas.xscml.Sqml sqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        int startPosForTag = pos, endPosForTag = pos;
        if(tagList!=null)
            for (int i = 0; i < tagList.size(); i++) {
                SqmlTag tag = (SqmlTag) tagList.get(i);
                //������� ��� � editText
                QTextCharFormat f;
                int curEndPos = endPosForTag;
                do {
                    startPosForTag = plaintText.indexOf(tag.getDisplayName(), curEndPos);
                    tc.setPosition(startPosForTag + 1);
                    f = tc.charFormat();
                    if (f.underlineStyle() == UnderlineStyle.NoUnderline) {
                        curEndPos = startPosForTag + 1;
                    }
                } while ((f.underlineStyle() == UnderlineStyle.NoUnderline) && ((startPosForTag + tag.getDisplayName().length()) < endpos));
                //������� java item
                if (endPosForTag < startPosForTag) {
                    org.radixware.schemas.xscml.Sqml.Item item = sqml.addNewItem();
                    int n = startPosForTag < endpos ? startPosForTag : endpos;
                    String d = plaintText.substring(endPosForTag, n).replace("\u200B", "");
                    item.setSql(d);
                }
                //tag
                org.radixware.schemas.xscml.Sqml.Item itemTag = sqml.addNewItem();
                tag.addTagToSqml(itemTag);
                endPosForTag = startPosForTag + tag.getDisplayName().length();
            }
        //������� java item
        if (endPosForTag < plaintText.length()) {
            String s = plaintText.substring(endPosForTag, endpos);
            s = s.replace("\u200B", "");
            org.radixware.schemas.xscml.Sqml.Item item = sqml.addNewItem();
            item.setSql(s.replace("\u200B", ""));
        }
        return sqml;
    }

    @Override
    public void updateTagsPos(final String plaintText, final QTextCursor tc, final boolean isUndoRedo) {
        if ((tagListHystory != null) && (tagListHystory.size() > 0)) {
            final List<TagInfo> tagList = getCurrentTagList();
            if (tagList == null) {
                return;
            }
            QTextCharFormat f;

            int endPosForTag = 0, startPosForTag;
            for (int i = 0; i < tagList.size(); i++) {
                final TagInfo tag = tagList.get(i);
                do {
                    startPosForTag = plaintText.indexOf(tag.getDisplayName(), endPosForTag);
                    if(tag.getDisplayName().isEmpty()){
                        throw new IllegalStateException("Tag has empty name ");
                    }
                    if (startPosForTag == -1) {
                        return;
                    }
                    tc.setPosition(startPosForTag + 1);
                    f = tc.charFormat();
                    if (f.underlineStyle() == UnderlineStyle.NoUnderline) {
                        endPosForTag = startPosForTag + 1;
                    } else {
                        tag.calcTagPos(startPosForTag);
                        //tag.startPos=startPosForTag+1;
                        //tag.endPos=tag.startPos+tag.getDisplayName().length();
                    }
                } while (f.underlineStyle() == UnderlineStyle.NoUnderline);
                endPosForTag = (int) tag.getEndPos() - 1;
            }
            addHystory(isUndoRedo);
        }
    }

    @Override
    public String getStrFromXml(final String plainText, final List<TagInfo> copyTags, final QTextCursor tc) {
        String res = "";
        try {
            final org.radixware.schemas.xscml.Sqml sqml = toXml(plainText, copyTags, tc, tc.selectionStart(), tc.selectionEnd());
            final StringWriter w = new StringWriter();
            sqml.save(w);
            res = w.toString();
        } catch (IOException ex) {
            Logger.getLogger(SqmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public ISqmlColumnDef chooseThisSqmlColumn(final ISqmlTableDef thisTable, final ISqmlColumnDef currentColumn, final boolean isReadOnly, final QDialog dialog) {
        final List<ISqmlDefinition> columns = new ArrayList<>();
        columns.addAll(thisTable.getColumns().getAll());
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, columns, EnumSet.of(SqmlTreeModel.ItemType.PROPERTY));
        //sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);
        final List<ISqmlDefinition> currentItemPath = Collections.<ISqmlDefinition>singletonList(currentColumn);
        final ChooseSqmlDefinitionDialog choiceObj = new ChooseSqmlDefinitionDialog(environment, sqmlModel, currentItemPath, isReadOnly, dialog);
        final String dialogTitle = isReadOnly ? environment.getMessageProvider().translate("SqmlEditor","Property") :
                                                environment.getMessageProvider().translate("SqmlEditor","Select Property");
        choiceObj.setWindowTitle(dialogTitle);
        choiceObj.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.PROPERTY));
        if (choiceObj.exec() == QDialog.DialogCode.Accepted.value()) {
            return (ISqmlColumnDef) choiceObj.getCurrentItem();
        }
        return null;
    }

    public ISqmlEnumItem chooseEnumItem(final boolean isReadOnly, final QDialog dialog) {
        final List<ISqmlDefinition> enums = new ArrayList<>();
        enums.addAll(environment.getSqmlDefinitions().getEnums());
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(environment, enums, EnumSet.of(SqmlTreeModel.ItemType.ENUIM_ITEM, SqmlTreeModel.ItemType.MODULE_INFO));
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(showMode);
        final ChooseSqmlDefinitionDialog choiceObj = new ChooseSqmlDefinitionDialog(environment, sqmlModel, null, isReadOnly, dialog);
        final String dialogTitle = isReadOnly ? environment.getMessageProvider().translate("SqmlEditor","Enumeration"):
                                                environment.getMessageProvider().translate("SqmlEditor", "Select Enumeration");
        choiceObj.setWindowTitle(dialogTitle);
        choiceObj.setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.CONSTSET));
        if (choiceObj.exec() == QDialog.DialogCode.Accepted.value()) {
            return (ISqmlEnumItem) choiceObj.getCurrentItem();
        }
        return null;
    }

    public EntityModel chooseSqmlTableObject(final ISqmlTableDef currentTable, final boolean isReadOnly, final QWidget parent) {
        final ISqmlTableDef table = chooseSqmlTable(currentTable, isReadOnly, parent, true);
        if (table != null) {
            final RadClassPresentationDef classDef = environment.getApplication().getDefManager().getClassPresentationDef(table.getId());
            final RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
            final GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(environment, selPresDef);

            final SelectEntityDialog chooseObjDialog = new SelectEntityDialog(groupModel, false);
            if (com.trolltech.qt.gui.QDialog.DialogCode.resolve(chooseObjDialog.exec()).equals(com.trolltech.qt.gui.QDialog.DialogCode.Accepted)) {
                return chooseObjDialog.selectedEntity;
            }
        }
        return null;
    }
}
