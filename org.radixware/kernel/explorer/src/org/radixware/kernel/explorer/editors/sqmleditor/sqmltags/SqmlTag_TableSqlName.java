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
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.TableOrProperty_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.TableSqlName;


public class SqmlTag_TableSqlName extends SqmlTag {

    private TableSqlName tableSqlName;
    private String tableName;
    // private String tableDisplayTitle;
    private String tableTitle;
    private String tableAlias;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_TABLE_SQL_NAME";

    public SqmlTag_TableSqlName(final IClientEnvironment environment, final ISqmlTableDef presentationClassDef, final long pos, final EDefinitionDisplayMode showMode) {
        super(environment, pos,presentationClassDef==null?false :presentationClassDef.isDeprecated());
        createTableSqlName(presentationClassDef,presentationClassDef==null ? null: presentationClassDef.getAlias(), showMode);
    }

    public SqmlTag_TableSqlName(final IClientEnvironment environment, final ISqmlTableReference presentationClassDef, final long pos,final EDefinitionDisplayMode showMode) {
        super(environment, pos,presentationClassDef.isDeprecated());
        createTableSqlName(presentationClassDef, showMode, null);
    }

    public SqmlTag_TableSqlName(final IClientEnvironment environment, final TableSqlName tableSqlName, final long pos, final EDefinitionDisplayMode showMode) {
        super(environment, pos);
        final Id tableId = tableSqlName.getTableId();
        try {
            final ISqmlTableDef presentationClassDef = environment.getSqmlDefinitions().findTableById(tableId);
            if (presentationClassDef == null) {
                setNotValid(tableId);
            }
            createTableSqlName(presentationClassDef, tableSqlName.getTableAlias(), showMode);
        } catch (DefinitionError ex) {
            setNotValid(tableId);
        }
    }

    private void setNotValid(final Id tableId) {
        setValid(false);
        final String mess = Application.translate("SqmlEditor", "table #%s not found");
        environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, tableId), tableId.toString());
        setDisplayedInfo("", "???" + tableId + "???");
    }

    private void createTableSqlName(final ISqmlTableDef presentationClassDef, final String alias, final EDefinitionDisplayMode showMode) {
        if (presentationClassDef != null) {
            setIsDeprecated(presentationClassDef.isDeprecated());
            tableAlias = alias;
            setTableSqlName(presentationClassDef.getId(), alias);
            setNameAndTitle(presentationClassDef);
            setDisplayedInfo(showMode);
        }
    }

    private void createTableSqlName(final ISqmlTableReference presentationClassDef, final EDefinitionDisplayMode showMode, final String alias) {
        if (presentationClassDef != null) {
            setTableSqlName(presentationClassDef.getReferencedTableId(), alias);
            try {
                final ISqmlTableDef classDef = presentationClassDef.findReferencedTable();
                setNameAndTitle(classDef);
                setDisplayedInfo(showMode);
            } catch (DefinitionError ex) {
                final String mess = Application.translate("SqmlEditor", "table #%s not found");
                environment.getTracer().put(EEventSeverity.WARNING, String.format(mess, presentationClassDef.findReferencedTable()), presentationClassDef.getReferencedTableId().toString());
            }
        }
    }

    private void setNameAndTitle(final ISqmlTableDef presentationClassDef) {
        tableName = presentationClassDef.getFullName();//==null ? ("#"+presentationClassDef.getId()) : presentationClassDef.getName();
        //tableDisplayTitle=presentationClassDef.getTitle();//hasGroupTitle() ? presentationClassDef.getGroupTitle() : tableName ;
        tableTitle = presentationClassDef.getTitle();
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        ISqmlTableDef presentationClassDef = null;
        if (tableSqlName != null) {
            presentationClassDef = environment.getSqmlDefinitions().findTableById(tableSqlName.getTableId());
        }
        final TableOrProperty_Dialog dialog = new TableOrProperty_Dialog(editText.getEnvironment(), 
                                                                                                             presentationClassDef, 
                                                                                                             null,
                                                                                                             tableAlias,
                                                                                                             false,
                                                                                                             showMode,
                                                                                                             editText.isReadOnly(),
                                                                                                             editText);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            presentationClassDef = dialog.getPresentClassDef();
            tableAlias = dialog.getAlias();
            setNameAndTitle(presentationClassDef);
            setTableSqlName(presentationClassDef.getId(), tableAlias);
            setDisplayedInfo(showMode);
            return true;
        }
        return false;
    }

    private void setTableSqlName(final Id tableId, final String alias) {
        tableSqlName = TableSqlName.Factory.newInstance();
        Id id=tableId;
        if (tableId.getPrefix() != EDefinitionIdPrefix.DDS_TABLE) {
            id = Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.DDS_TABLE);
        }
        tableSqlName.setTableId(id);
        if ((alias != null) && (!alias.equals(""))) {
            tableSqlName.setTableAlias(alias);
        }
    }

    private String createTitle(final String s) {
        return isValid() ? "<b>Table:  </b>" + Html.string2HtmlString(s) : "";
    }

    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            fullName = tableName;
            final String str_alias = ((tableAlias == null) || ("".equals(tableAlias))) ? "" : " " + tableAlias;
            //if(tableDisplayTitle==null)tableDisplayTitle=tableName;
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                setDisplayedInfo(createTitle(tableName), tableTitle + str_alias);
            } else if (EDefinitionDisplayMode.SHOW_SHORT_NAMES == showMode) {
                final String name = getNameWithoutModule(fullName);
                setDisplayedInfo(createTitle(tableTitle), name + str_alias);
            } else {
                setDisplayedInfo(createTitle(tableTitle), tableName + str_alias);
            }
            return true;
        }
        return false;
    }

    private SqmlTag_TableSqlName(final IClientEnvironment environment,final SqmlTag_TableSqlName source) {
        super(environment, source);
    }

    @Override
    public SqmlTag_TableSqlName copy() {
        final SqmlTag_TableSqlName res = new SqmlTag_TableSqlName(environment, this);
        res.tableSqlName = tableSqlName;
        res.tableName = tableName;
        res.tableTitle = tableTitle;
        res.tableAlias = tableAlias;
        //res.tableDisplayTitle=tableDisplayTitle;
        return res;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setTableSqlName(this.tableSqlName);
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}