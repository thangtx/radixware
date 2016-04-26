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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ThisTableSqmlId_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;


public class SqmlTag_ThisTableSqlId extends SqmlTag {

    private ISqmlTableDef presentationClassDef;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_THIS_TABLE_SQL_NAME"/*SQML_THIS_TABLE_SQL_ID*/;

    public SqmlTag_ThisTableSqlId(final IClientEnvironment environment,final ISqmlTableDef presentationClassDef,final long pos) {
        super(environment, pos);
        this.presentationClassDef = presentationClassDef;
        if (presentationClassDef != null) {
            setIsDeprecated(presentationClassDef.isDeprecated());
            final String tableName = presentationClassDef.getFullName();//==null ? ("#"+presentationClassDef.getId()) : presentationClassDef.getName();
            final String tableTitle = presentationClassDef.getTitle();//getGroupTitle().replaceAll("<", "&#60;");
            final String title = "Table <b>" + tableName + "</b> with title " + tableTitle;
            setDisplayedInfo("ThisId", title);
        } else {
            setDisplayedInfo("???ThisTableId???", "???ThisTableId???");
        }
    }

    @Override
    public final void setDisplayedInfo(final String name,final String title) {
        fullName = name;
        super.setDisplayedInfo(title, name);
    }

    private SqmlTag_ThisTableSqlId(final IClientEnvironment environment,final SqmlTag_ThisTableSqlId source) {
        super(environment, source);
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText,final EDefinitionDisplayMode showMode) {
        final ThisTableSqmlId_Dialog dialog = new ThisTableSqmlId_Dialog(editText.getEnvironment(), editText, presentationClassDef.getFullName(), presentationClassDef.getTitle());
        if (dialog.exec() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public SqmlTag_ThisTableSqlId copy() {
        final SqmlTag_ThisTableSqlId res = new SqmlTag_ThisTableSqlId(environment, this);
        res.presentationClassDef = presentationClassDef;
        return res;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setThisTableId(XmlObject.Factory.newInstance());
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}