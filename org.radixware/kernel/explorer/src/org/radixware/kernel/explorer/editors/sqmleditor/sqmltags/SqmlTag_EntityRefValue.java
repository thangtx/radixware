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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.EntityRefValue_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.EntityRefValue;


public class SqmlTag_EntityRefValue extends SqmlTag_AbstractReference {

    private final EntityRefValue entityRefValue;
    private String refObjectTitle;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_ENTITY_REF_PARAMETER";

    public SqmlTag_EntityRefValue(IClientEnvironment environment, final Pid pid, final long pos, final EDefinitionDisplayMode displayMode, final boolean isPrimaryKey, final Id secondaryKeyId) {
        super(environment, pos);
        entityRefValue = EntityRefValue.Factory.newInstance();
        entityRefValue.setReferencedObjectPidAsStr(pid.toString());
        entityRefValue.setReferencedTableId(pid.getTableId());
        if (isPrimaryKey) {
            entityRefValue.setPidTranslationMode(EPidTranslationMode.PRIMARY_KEY_PROPS);
        } else if (secondaryKeyId != null) {
            entityRefValue.setPidTranslationMode(EPidTranslationMode.SECONDARY_KEY_PROPS);
            entityRefValue.setPidTranslationSecondaryKeyId(secondaryKeyId);
        } else {
            entityRefValue.setPidTranslationMode(EPidTranslationMode.AS_STR);
        }
        try {
            this.refObjectTitle = pid.getDefaultEntityTitle(environment.getEasSession());
        } catch (ServiceClientException ex) {
            Logger.getLogger(SqmlTag_EntityRefValue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {          
        }
        entityRefValue.setLiteral(true);
        setDisplayedInfo(displayMode);
    }

    public SqmlTag_EntityRefValue(final IClientEnvironment environment, final EntityRefValue entityRefValue, final long pos, final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        this.entityRefValue = (EntityRefValue) entityRefValue.copy();
        String strPid = entityRefValue.getReferencedObjectPidAsStr();
        Id tableId = entityRefValue.getReferencedTableId();
        if (getReferencedTable() == null) {
            final String mess = Application.translate("SqmlEditor", "table or entity #%s not found");
            environment.getTracer().warning(String.format(mess, tableId));
            setDisplayedInfo(null, "???" + strPid + "???");
        } else {
            try {
                Pid pid = new Pid(tableId, strPid);
                this.refObjectTitle = pid.getDefaultEntityTitle(environment.getEasSession());
            } catch (ServiceClientException | InterruptedException ex) {
                final String mess = Application.translate("SqmlEditor", "can not actualize entity title for #%s");
                environment.getTracer().warning(String.format(mess, strPid));
                setDisplayedInfo(null, "???" + strPid + "???");
            }
            if (entityRefValue.getPidTranslationMode() != EPidTranslationMode.AS_STR && getIndexDef() == null) {
                valid = false;
                setDisplayedInfo(null, "???" + strPid + "???");
            } else {
                setDisplayedInfo(displayMode);
            }
        }
    }

    private SqmlTag_EntityRefValue(final IClientEnvironment environment, final SqmlTag_EntityRefValue source) {
        super(environment, source);
        entityRefValue = (EntityRefValue) source.entityRefValue.copy();
        refObjectTitle = source.refObjectTitle;
    }

    @Override
    protected final ISqmlTableIndexDef getIndexDef() {
        final ISqmlTableDef referencedTable = getReferencedTable();
        if (referencedTable != null) {
            final ISqmlTableIndices indices = referencedTable.getIndices();
            if (entityRefValue.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS) {
                return indices.getPrimaryIndex();
            } else {
                return indices.getIndexById(entityRefValue.getPidTranslationSecondaryKeyId());
            }
        }
        return null;
    }

    @Override
    protected final ISqmlTableDef getReferencedTable() {
        final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
        return definitions.findTableById(entityRefValue.getReferencedTableId());
    }

    @Override
    protected EPidTranslationMode getPidTranslationMode() {
        return entityRefValue.getPidTranslationMode();
    }

    @Override
    public SqmlTag_EntityRefValue copy() {
        return new SqmlTag_EntityRefValue(environment, this);
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        ISqmlTableDef table = getReferencedTable();
        if (table != null) {
            final EntityRefValue_Dialog dialog = new EntityRefValue_Dialog(environment, table, refObjectTitle, editText);
            dialog.readFromTag(entityRefValue);
            if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
                dialog.writeToTag(entityRefValue);
                refObjectTitle = dialog.getRefObjectTitle();
                setDisplayedInfo(showMode);
                return true;
            }
        }
        return false;
    }

    private String calculateToolTip(final EDefinitionDisplayMode displayMode) {
        final StringBuilder strBuilder = new StringBuilder();
        final String lbTableObl = Application.translate("SqmlEditor", "Table Object");
        strBuilder.append("<b>");
        strBuilder.append(lbTableObl);
        strBuilder.append(":</b><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        strBuilder.append(refObjectTitle);
        strBuilder.append("</br>");
        strBuilder.append(getReferenceToolTip(displayMode));
        return strBuilder.toString();
    }

    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            String displayValue = entityRefValue.getDisplayValue();
            String prefix = (displayValue == null || displayValue.isEmpty()) ? refObjectTitle : displayValue;
            setDisplayedInfo(calculateToolTip(showMode), getReferenceDisplayedInfo(prefix, showMode));
            return true;
        }
        return false;
    }

    @Override
    public void addTagToSqml(XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setEntityRefValue(entityRefValue);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }
}
