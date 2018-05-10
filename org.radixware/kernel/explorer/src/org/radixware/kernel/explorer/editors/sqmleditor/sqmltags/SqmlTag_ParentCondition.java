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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QTextCursor;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.ESqlConditionOperator;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlProcessor;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.Condition_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;


public class SqmlTag_ParentCondition extends SqmlTag {

    private ParentCondition parentCondition;
    private ISqmlTableDef presentationClassDef;
    private Reference entityReference;
    private ISqmlColumnDef prop;
    private Id propId;
    private String objTitle;
    private String tableAlias;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PARENT_CONDITION";

    public SqmlTag_ParentCondition(final IClientEnvironment environment,
                                                     final ISqmlColumnDef prop, 
                                                     final ESqlConditionOperator operator, 
                                                     final Reference ref,
                                                     final long pos,
                                                     final EDefinitionDisplayMode mode,
                                                     final String tableAlias) {
        super(environment, pos, prop==null?false :prop.isDeprecated());

        this.prop = prop;
        this.tableAlias = tableAlias;
        if (prop != null) {
            presentationClassDef = prop.getOwnerTable();
        }
        this.entityReference = ref;
        setParentConditionInfo(operator, entityReference == null ? null : entityReference.getPid(), mode);
    }

    public SqmlTag_ParentCondition(final IClientEnvironment environment,
                                                     final ParentCondition parentCondition,
                                                     final long pos,
                                                     final EDefinitionDisplayMode showMode) {
        super(environment, pos);
        final Id tableId = parentCondition.getTableId();
        propId = parentCondition.getPropId();
        setPropSqlName(parentCondition);
        presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
        if (presentationClassDef == null) {
            final String mess = Application.translate("SqmlEditor", "table or entity #%s not found");
            environment.getTracer().warning(String.format(mess, tableId));
            setNotValid();
        } else {
            prop = presentationClassDef.getColumns().getColumnById(propId);
            if (prop == null) {
                final String mess = Application.translate("SqmlEditor", "property #%s was not found in entity %s #%s");
                environment.getTracer().warning(String.format(mess, propId, presentationClassDef.getFullName(), presentationClassDef.getId()));
                setNotValid();
            } else {
                setIsDeprecated(prop.isDeprecated());
                if (prop.getType() == EValType.PARENT_REF) {
                    objTitle = getPid(prop);
                    setDisplayedInfo(showMode);
                    return;
                }
                setNotValid();
            }
        }
    }

    public final void setParentConditionInfo(final ESqlConditionOperator operator, final Pid pid, final EDefinitionDisplayMode showMode) {
        if ((presentationClassDef != null) && (prop != null) && (prop.getType() == EValType.PARENT_REF)) {
            final String str_pid = pid == null ? null : pid.toString();
            setPropSqlName(operator, str_pid, prop.getId(), presentationClassDef.getId());
            objTitle = entityReference == null ? "null" : entityReference.toString();
            setDisplayedInfo(showMode);

        } else {
            setNotValid(operator);
        }
    }

    private void setNotValid(final ESqlConditionOperator operator) {
        setValid(false);
        final String str_operator = operator==null ? "" : operator.getText();
        final String propName = prop == null ? String.valueOf(propId) : prop.getId().toString();
        final String defName = presentationClassDef != null ? presentationClassDef.getId().toString() : "null";
        final String pidStr = (entityReference == null) || (entityReference.getPid() == null) ? "" : " " + entityReference.getPid();
        final String s = "???" + defName + "-" + propName + " " + str_operator + pidStr + "???";//"???<TableId>-<PropertyId> <operator> <ParentPid>???"
        setDisplayedInfo(s, s);
    }
    
    private ESqlConditionOperator getOperator(){
        return ESqlConditionOperator.findByParentConditionOperator(parentCondition.getOperator());
    }

    private void setNotValid() {
        setNotValid(getOperator());
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        final Condition_Dialog dialog = new Condition_Dialog(environment, 
                                                                                        prop,
                                                                                        getOperator(),
                                                                                        entityReference,
                                                                                        editText.getTagConverter().getShowMode(), 
                                                                                        editText);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final List<Object> values = dialog.getValues();
            if (dialog.isParentCondition()) {
                prop = dialog.getProperty();
                presentationClassDef = prop.getOwnerTable();                
                if (values!=null && !values.isEmpty()){
                    final Object value = values.get(0);
                    if (value instanceof Pid){
                        final Reference ref = new Reference((Pid)value);
                        entityReference = ref;
                    }else if (value instanceof Reference){
                        entityReference = (Reference)value;
                    }
                }else{
                    entityReference = null;
                }                
                objTitle = entityReference == null ? "null" : entityReference.toString();
                setParentConditionInfo(dialog.getOperator(), entityReference == null ? null : entityReference.getPid(), showMode);
                return true;
            } else {
                final QTextCursor tc = editText.textCursor();
                try {
                    editText.blockSignals(true);
                    tc.beginEditBlock();
                    editText.deleteTag(tc, this);
                } finally {
                    tc.endEditBlock();
                    editText.blockSignals(false);
                    editText.textChanged.emit();
                    //editText.endEditBlock(tc);
                }
                ((SqmlProcessor) editText.getTagConverter()).getSqmlTagInsertion().insertParentCondition(false, values, prop, dialog.getOperator(), tableAlias, tc);
                return false;
            }
        }
        return false;
    }

    private String getPid(final ISqmlColumnDef prop) {
        String s = null;
        if (parentCondition.isSetParentPid()) {
            final Pid pid = new Pid(prop.getReferencedTableId(), parentCondition.getParentPid());
            try {
                s = pid.getDefaultEntityTitle(environment.getEasSession());
            } catch (ServiceClientException ex) {
                s = "???" + pid + "???";
                environment.getTracer().error(ex);
            } catch (InterruptedException ex) {
                s = "???" + pid + "???";                
            }
        }
        return s;
    }

    private void setPropSqlName(final ParentCondition parentCondition) {
        this.parentCondition = ParentCondition.Factory.newInstance();
        this.parentCondition.setOperator(parentCondition.getOperator());
        if (parentCondition.isSetParentPid()) {
            this.parentCondition.setParentPid(parentCondition.getParentPid());
        }
        if (parentCondition.isSetParentTitle()) {
            this.parentCondition.setParentTitle(parentCondition.getParentTitle());
        }
        this.parentCondition.setPropId(parentCondition.getPropId());
        this.parentCondition.setTableId(parentCondition.getTableId());
    }

    private void setPropSqlName(final ESqlConditionOperator operator, final String parentPid, final Id propId, final Id tableId) {
        parentCondition = ParentCondition.Factory.newInstance();
        this.parentCondition.setOperator(operator.getParentConditionOperator());
        if (parentPid != null) {
            this.parentCondition.setParentPid(parentPid);
        }
        this.parentCondition.setPropId(propId);
        this.parentCondition.setTableId(tableId);
    }

    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            String name, s;
            final ESqlConditionOperator operator = getOperator();
            final String str_operator = operator==null ? "" : operator.getText();
            final String propName = prop.getShortName();
            String defName = tableAlias == null ? presentationClassDef.getFullName() : tableAlias;
            final String defTitle = tableAlias == null ? presentationClassDef.getTitle() : tableAlias;
            final String propTitle = prop.getTitle();
            String str_objTitle = ((objTitle == null) || (objTitle.equals("null"))) ? "" : " " + objTitle;
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                name = defTitle + "." + propTitle + " " + str_operator + str_objTitle;
                s = defName + "." + propName + " " + str_operator + str_objTitle;
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) {
                s = presentationClassDef.getTitle() + "." + prop.getTitle() + " " + str_operator + str_objTitle;
                defName = getNameWithoutModule(defName);
                str_objTitle = getNameWithoutModule(str_objTitle);
                fullName = defName + "." + propName + " " + str_operator + str_objTitle;
                name = fullName;
            } else {
                s = presentationClassDef.getTitle() + "." + prop.getTitle() + " " + str_operator + str_objTitle;
                fullName = defName + "." + propName + " " + str_operator + str_objTitle;
                name = fullName;
            }
            setDisplayedInfo(Html.string2HtmlString(s), name);
            return true;
        }
        return false;
    }

    private SqmlTag_ParentCondition(final IClientEnvironment environment,final SqmlTag_ParentCondition source) {
        super(environment, source);
    }

    @Override
    public SqmlTag_ParentCondition copy() {
        final SqmlTag_ParentCondition res = new SqmlTag_ParentCondition(this.environment, this);
        res.parentCondition = parentCondition;
        res.prop = prop;
        res.entityReference = entityReference;
        res.objTitle = objTitle;
        res.presentationClassDef = presentationClassDef;
        res.tableAlias = tableAlias;
        return res;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setParentCondition(this.parentCondition);
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}