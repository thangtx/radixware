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
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.Parameter_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.Parameter;


public class SqmlTag_Parameter extends SqmlTag {

    private final Parameter parameterItem;
    private final ISqmlParameter parameter;
    private final ISqmlTableDef contextTable;
    private final ISqmlParameters parameters;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PARAMETER";

    public SqmlTag_Parameter(final IClientEnvironment environment, final ISqmlParameter parameter, final long pos, final ISqmlTableDef contextTable, final ISqmlParameters params, final EDefinitionDisplayMode displayMode) {
        super(environment, pos,parameter==null?false :parameter.isDeprecated());
        parameters = params;
        this.parameter = parameter;
        parameterItem = Parameter.Factory.newInstance();
        if(parameter!=null)
            parameterItem.setParamId(parameter.getId());
        this.contextTable = contextTable;
        setDisplayedInfo(displayMode);
    }

    public SqmlTag_Parameter(final IClientEnvironment environment, final Parameter xmlParameter, final ISqmlParameters parameters, final long pos, final ISqmlTableDef contextTable, final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        this.parameters = parameters;
        this.contextTable = contextTable;
        parameterItem = (Parameter) xmlParameter.copy();
        parameter = parameters.getParameterById(xmlParameter.getParamId());
        if (parameter == null || parameter.getType()==null) {
            valid = false;
            setDisplayedInfo(null, "::???" + xmlParameter.getParamId() + "???");
        } else {
            setIsDeprecated(parameter.isDeprecated());
            setDisplayedInfo(displayMode);
        }
    }

    private SqmlTag_Parameter(final IClientEnvironment environment, final SqmlTag_Parameter source) {
        super(environment, source);
        parameterItem = (Parameter) source.parameterItem.copy();
        parameter = source.parameter;
        parameters = source.parameters;
        contextTable = source.contextTable;
    }

    private String calculateToolTip(final EDefinitionDisplayMode displayMode) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final StringBuilder strBuilder = new StringBuilder();
        final String parameterStr = msgProvider.translate("SqmlEditor", "Parameter");
        final String valueTypeStr = msgProvider.translate("SqmlEditor", "Value type");
        final String basePropertyStr = msgProvider.translate("SqmlEditor", "Based on property");
        strBuilder.append("<b>");
        strBuilder.append(parameterStr);
        strBuilder.append(":</b><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        strBuilder.append(parameter.getTitle());
        strBuilder.append("</br>");
        if (parameter.getBasePropertyId() != null && contextTable!=null) {
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
        if (!valid){
            strBuilder.append("<br><b>");
            strBuilder.append(valueTypeStr);
            strBuilder.append(":</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
            strBuilder.append(parameter.getType().getName());
            strBuilder.append("</br>");
        }
        return strBuilder.toString();
    }

    @Override
    public SqmlTag_Parameter copy() {
        return new SqmlTag_Parameter(environment, this);
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
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (parameter != null && parameters != null) {
            if (parameters.getParameterById(parameter.getId()) == null) {
                valid = false;
                final String idAsStr = parameter.getId() == null ? "" : parameter.getId().toString();
                setDisplayedInfo(null, "::???" + idAsStr + "???");
                return true;
            }
            final String prefix;
            if (parameter.getPersistentValue() != null) {
                final Object obj = parameter.getPersistentValue().getValObject();
                prefix = obj == null ? "::null" : "::" + parameter.getEditMask().toStr(environment,obj);
            } else {
                prefix = "::" + parameter.getDisplayableText(showMode);
            }
            setDisplayedInfo(calculateToolTip(showMode), prefix);
            return true;
        }
        return false;
    }

    @Override
    public void addTagToSqml(XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setParameter(parameterItem);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }
}
