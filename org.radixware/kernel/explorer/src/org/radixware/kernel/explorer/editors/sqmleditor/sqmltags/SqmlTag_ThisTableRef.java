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
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ThisTableRef_Dialog;

import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.ThisTableRef;


public final class SqmlTag_ThisTableRef extends SqmlTag_AbstractReference {

    private final ISqmlTableDef contextTable;
    private final ThisTableRef thisTableRef;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_ENTITY_REF_PARAMETER";

    public SqmlTag_ThisTableRef(final IClientEnvironment environment, final ISqmlTableDef contextTable, final long pos, final EDefinitionDisplayMode displayMode) {
        super(environment, pos, contextTable==null?false :contextTable.isDeprecated());
        this.contextTable = contextTable;
        this.thisTableRef = ThisTableRef.Factory.newInstance();
        thisTableRef.setPidTranslationMode(EPidTranslationMode.AS_STR);
        setDisplayedInfo(displayMode);
    }

    public SqmlTag_ThisTableRef(final IClientEnvironment environment, final ThisTableRef thisTableRef, final long pos, final ISqmlTableDef contextTable, final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        this.contextTable = contextTable;        
        this.thisTableRef = thisTableRef;
        if ((contextTable == null) || (thisTableRef.getPidTranslationMode() != EPidTranslationMode.AS_STR && getIndexDef() == null)) {
            setValid(false);
            setDisplayedInfo(null, "???" + "this" + "???");
        } else {
            setIsDeprecated(contextTable.isDeprecated());
            setDisplayedInfo(displayMode);
        }
    }

    private SqmlTag_ThisTableRef(final IClientEnvironment environment, final SqmlTag_ThisTableRef source) {
        super(environment, source);
        thisTableRef = (ThisTableRef) source.thisTableRef.copy();
        contextTable = source.contextTable;
        setIsDeprecated(contextTable.isDeprecated());
    }

    @Override
    protected ISqmlTableIndexDef getIndexDef() {
        final ISqmlTableDef referencedTable = getReferencedTable();
        if (referencedTable != null) {
            final ISqmlTableIndices indices = referencedTable.getIndices();
            if (thisTableRef.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS) {
                return indices.getPrimaryIndex();
            } else {
                return indices.getIndexById(thisTableRef.getPidTranslationSecondaryKeyId());
            }
        }
        return null;
    }

    @Override
    protected ISqmlTableDef getReferencedTable() {
        return contextTable;
    }

    @Override
    protected EPidTranslationMode getPidTranslationMode() {
        return thisTableRef.getPidTranslationMode();
    }

    private String calculateToolTip(final EDefinitionDisplayMode displayMode) {
        final StringBuilder strBuilder = new StringBuilder();
        final String parameterStr = Application.translate("SqmlEditor", "Context Table Reference Tag");
        strBuilder.append("<b>");
        strBuilder.append(parameterStr);
        strBuilder.append(":</b>");
        strBuilder.append(getReferenceToolTip(displayMode));
        return strBuilder.toString();
    }

    @Override
    public SqmlTag_ThisTableRef copy() {
        return new SqmlTag_ThisTableRef(environment, this);
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        if (getReferencedTable() != null) {
            final ThisTableRef_Dialog dialog = new ThisTableRef_Dialog(editText.getEnvironment(), getReferencedTable(), editText);
            dialog.readFromTag(thisTableRef);
            if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                dialog.writeToTag(thisTableRef);
                setDisplayedInfo(showMode);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            final String prefix = "this";
            final EDefinitionDisplayMode newShowMode = EDefinitionDisplayMode.SHOW_FULL_NAMES == showMode ? EDefinitionDisplayMode.SHOW_SHORT_NAMES : showMode;
            setDisplayedInfo(calculateToolTip(newShowMode), getReferenceDisplayedInfo(prefix, newShowMode));
            return true;
        }
        return false;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setThisTableRef(thisTableRef);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }
}
