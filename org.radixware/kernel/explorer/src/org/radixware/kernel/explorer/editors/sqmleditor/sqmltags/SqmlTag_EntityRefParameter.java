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
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.Parameter_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.EntityRefParameter;


public final class SqmlTag_EntityRefParameter extends SqmlTag_AbstractReference {

    private final EntityRefParameter parameterItem;
    private final ISqmlParameter parameter;
    private final ISqmlParameters parameters;
    private final ISqmlTableDef contextTable;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_ENTITY_REF_PARAMETER";

    public SqmlTag_EntityRefParameter(final IClientEnvironment environment, final ISqmlParameter parameter, final long pos, final ISqmlTableDef contextTable, final ISqmlParameters params, final EDefinitionDisplayMode displayMode) {
        super(environment, pos,parameter==null ? false : parameter.isDeprecated());
        parameters = params;
        this.parameter = parameter;
        parameterItem = EntityRefParameter.Factory.newInstance();
        if (parameter!=null){
            parameterItem.setParamId(parameter.getId());
            parameterItem.setReferencedTableId(parameter.getReferencedTableId());
        }
        parameterItem.setPidTranslationMode(EPidTranslationMode.AS_STR);
        this.contextTable = contextTable;
        setDisplayedInfo(displayMode);
    }

    public SqmlTag_EntityRefParameter(final IClientEnvironment environment, final EntityRefParameter xmlParameter, final ISqmlParameters parameters, final long pos, final ISqmlTableDef contextTable, final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        this.parameters = parameters;
        this.contextTable = contextTable;
        parameterItem = (EntityRefParameter) xmlParameter.copy();
        parameter = parameters.getParameterById(xmlParameter.getParamId());
        if (parameter == null
                || (parameterItem.getPidTranslationMode() != EPidTranslationMode.AS_STR && getIndexDef() == null)) {
            valid = false;
            setDisplayedInfo(null, "::???" + xmlParameter.getParamId() + "???");
        } else {
            setIsDeprecated(parameter.isDeprecated());
            xmlParameter.setReferencedTableId(parameterItem.getReferencedTableId());
            setDisplayedInfo(displayMode);
        }
    }

    private SqmlTag_EntityRefParameter(final IClientEnvironment environment, final SqmlTag_EntityRefParameter source) {
        super(environment, source);
        parameters = source.parameters;
        parameterItem = (EntityRefParameter) source.parameterItem.copy();
        parameter = source.parameter;
        contextTable = source.contextTable;
    }

    @Override
    protected ISqmlTableIndexDef getIndexDef() {
        final ISqmlTableDef referencedTable = getReferencedTable();
        if (referencedTable != null) {
            final ISqmlTableIndices indices = referencedTable.getIndices();
            if (parameterItem.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS) {
                return indices.getPrimaryIndex();
            } else {
                return indices.getIndexById(parameterItem.getPidTranslationSecondaryKeyId());
            }
        }
        return null;
    }

    @Override
    protected ISqmlTableDef getReferencedTable() {
        final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
        return definitions.findTableById(parameterItem.getReferencedTableId());
    }

    @Override
    protected EPidTranslationMode getPidTranslationMode() {
        return parameterItem.getPidTranslationMode();
    }

    private String calculateToolTip(final EDefinitionDisplayMode displayMode) {
        final StringBuilder strBuilder = new StringBuilder();
        final MessageProvider msgProvider = environment.getMessageProvider();
        final String parameterStr = msgProvider.translate("SqmlEditor", "Parameter");
        final String basePropertyStr = msgProvider.translate("SqmlEditor", "Based on property");
        strBuilder.append("<b>");
        strBuilder.append(parameterStr);
        strBuilder.append(":</b><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        strBuilder.append(parameter.getTitle());
        strBuilder.append("</br>");
        if (parameter.getBasePropertyId() != null) {
            final ISqmlColumnDef column = contextTable.getColumns().getColumnById(parameter.getBasePropertyId());
            if (column != null) {
                strBuilder.append("<br><b>");
                strBuilder.append(basePropertyStr);
                strBuilder.append(":</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
                if (displayMode == EDefinitionDisplayMode.SHOW_TITLES) {
                    strBuilder.append(column.getTitle());
                } else {
                    strBuilder.append(column.getShortName());
                }
                strBuilder.append("</br>");
            }
        }

        strBuilder.append(getReferenceToolTip(displayMode));
        return strBuilder.toString();
    }

    @Override
    public SqmlTag_EntityRefParameter copy() {
        return new SqmlTag_EntityRefParameter(environment, this);
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        final Parameter_Dialog dialog = new Parameter_Dialog(environment, parameter, contextTable, editText);
        dialog.readFromTag(parameterItem);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            dialog.writeToTag(parameterItem);
            editText.scheduleUpdateTagNames();
            return true;
        }
        return false;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid() && parameter!=null && parameters!=null){
            if (parameters.getParameterById(parameter.getId()) == null) {
                valid = false;
                final String idAsStr = parameter.getId() == null ? "" : parameter.getId().toString();
                setDisplayedInfo(null, "::???" + idAsStr + "???");
                return true;
            }
            if (getReferencedTable()==null){
                    valid = false;
                    final String traceMessage =
                        environment.getMessageProvider().translate("SqmlEditor", "Can't parse EntityRefParameter tag: table  #%s was not found");
                    environment.getTracer().warning(String.format(traceMessage,parameterItem.getReferencedTableId()));
                    final String idAsStr = parameter.getId()==null ? "" : parameter.getId().toString();
                    setDisplayedInfo(null, "::???"+idAsStr+"???");
                    return true;
            }
            final String prefix;
            if (parameter.getPersistentValue() != null && parameter.getPersistentValue().isValid(environment)) {
                final Object obj = parameter.getPersistentValue().getValObject();
                prefix = obj == null ? "::null" : "::" + obj.toString();
            } else {
                prefix = "::" + parameter.getDisplayableText(showMode);
            }
            setDisplayedInfo(calculateToolTip(showMode), getReferenceDisplayedInfo(prefix, showMode));
            return true;
        }
        return false;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setEntityRefParameter(parameterItem);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }
}
