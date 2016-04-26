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
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.DbFuncCall_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.DbFuncCall;


public class SqmlTag_DbFuncCall extends SqmlTag {

    private DbFuncCall xmlFuncCall;
    private ISqmlFunctionDef dbFunction;
    private Map<Id, String> funcParameters;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_DB_FUNC_CALL";

    public SqmlTag_DbFuncCall(final IClientEnvironment environment, final ISqmlFunctionDef function, final Map<Id, String> paramValues, final long pos, final EDefinitionDisplayMode displayMode) {
        super(environment, pos, function == null ? false : function.isDeprecated());
        setData(function, paramValues);
        setDisplayedInfo(displayMode);
    }

    public SqmlTag_DbFuncCall(final IClientEnvironment environment, final DbFuncCall funcCall, final long pos, final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        final MessageProvider msgProvider = environment.getMessageProvider();
        final ISqmlDefinitions definitions = environment.getSqmlDefinitions();
        dbFunction = definitions.findFunctionById(funcCall.getFuncId());
        if (dbFunction == null) {
            final String message = msgProvider.translate("TraceMessage", "Data base function #%s was not found");
            environment.getTracer().error(String.format(message, funcCall.getFuncId()));
            setDisplayedInfo(null, "???" + funcCall.getFuncId() + "()???");
            valid = false;
            xmlFuncCall = null;
        } else {
            setIsDeprecated(dbFunction.isDeprecated());
            final DbFuncCall.Params params = funcCall.getParams();
            if (params != null && params.getParamList() != null) {
                for (DbFuncCall.Params.Param param : params.getParamList()) {
                    if (dbFunction.getParameterById(param.getParamId()) == null) {
                        final String message = msgProvider.translate("TraceMessage", "Function parameter #%s was not found in '%s'"); 
                        environment.getTracer().error(String.format(message, param.getParamId(), dbFunction.getTitle()));
                        setDisplayedInfo(null, dbFunction.getFullName() + "(???" + param.getParamId() + "???)");
                        valid = false;
                        xmlFuncCall = null;
                        return;
                    } else {
                        if (funcParameters == null) {
                            funcParameters = new HashMap<>();
                        }
                        funcParameters.put(param.getParamId(), param.getValue());
                    }
                }
            }
            xmlFuncCall = (DbFuncCall) funcCall.copy();
            setDisplayedInfo(displayMode);
        }
    }

    private void setData(final ISqmlFunctionDef function, final Map<Id, String> paramValues) {
        xmlFuncCall = DbFuncCall.Factory.newInstance();
        xmlFuncCall.setFuncId(function.getId());
        if (paramValues != null && !paramValues.isEmpty()) {
            final DbFuncCall.Params params = xmlFuncCall.addNewParams();
            funcParameters = new HashMap<>();
            for (Map.Entry<Id, String> paramValue : paramValues.entrySet()) {
                if (function.getParameterById(paramValue.getKey()) == null) {
                    final MessageProvider msgProvider = environment.getMessageProvider();
                    final String message = msgProvider.translate("TraceMessage", "Function parameter #%s was not found in '%s'");
                    environment.getTracer().error(String.format(message, paramValue.getKey(), function.getTitle()));
                } else {
                    final DbFuncCall.Params.Param param = params.addNewParam();
                    param.setParamId(paramValue.getKey());
                    param.setValue(String.valueOf(paramValue.getValue()));
                    funcParameters.put(paramValue.getKey(), paramValue.getValue());
                }
            }
        } else {
            funcParameters = null;
        }
        this.dbFunction = function;
    }

    private SqmlTag_DbFuncCall(final IClientEnvironment environment, final SqmlTag_DbFuncCall source) {
        super(environment, source);
        if (source != null) {
            if (source.xmlFuncCall != null) {
                xmlFuncCall = (DbFuncCall) source.xmlFuncCall.copy();
            } else {
                xmlFuncCall = null;
            }
            dbFunction = source.dbFunction;
            if (source.funcParameters != null) {
                funcParameters = new HashMap<>();
                funcParameters.putAll(source.funcParameters);
            }
        }
    }

    @Override
    public SqmlTag_DbFuncCall copy() {
        return new SqmlTag_DbFuncCall(environment, this);
    }

    private String calculateToolTip() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final String functionStr = msgProvider.translate("SqmlEditor", "Function '%s'");
        final String locationStr = msgProvider.translate("SqmlEditor", "Location: %s");
        final StringBuilder tooltipBuilder = new StringBuilder();
        tooltipBuilder.append("<b>");
        tooltipBuilder.append(String.format(functionStr, dbFunction.getShortName()));
        tooltipBuilder.append("</b>");
        final ISqmlPackageDef packageDef = dbFunction.getPackage();
        if (packageDef != null) {
            tooltipBuilder.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
            tooltipBuilder.append(String.format(locationStr, packageDef.getFullName()));
            tooltipBuilder.append("</br>");
        }
        return tooltipBuilder.toString();
    }

    private String calculateTagTitle(final EDefinitionDisplayMode displayMode) {
        final StringBuilder titleBuilder = new StringBuilder();
        final ISqmlPackageDef packageDef = dbFunction.getPackage();
        if (packageDef != null) {
            titleBuilder.append(packageDef.getDisplayableText(displayMode));
            titleBuilder.append('.');
        }
        titleBuilder.append(dbFunction.getShortName());
        if (funcParameters != null && !funcParameters.isEmpty()) {
            titleBuilder.append('(');
            boolean firstParameter = true;
            for (ISqmlFunctionParameter parameter : dbFunction.getAllParameters()) {
                if (firstParameter) {
                    firstParameter = false;
                } else {
                    titleBuilder.append(", ");
                }
                titleBuilder.append(parameter.getShortName());
                titleBuilder.append(" => ");
                titleBuilder.append(String.valueOf(funcParameters.get(parameter.getId())));
            }
            titleBuilder.append(')');
        } else if (dbFunction.getParametersCount() == 0) {
            titleBuilder.append("()");
        }
        return titleBuilder.toString();
    }

    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode displayMode) {
        if (isValid()) {
            if (dbFunction == null) {
                return false;
            } else {
                setDisplayedInfo(calculateToolTip(), calculateTagTitle(displayMode));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        final DbFuncCall_Dialog dialog =
                new DbFuncCall_Dialog(environment, dbFunction, funcParameters, showMode, editText.isReadOnly(), editText);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            setData(dialog.getSqmlFunction(), dialog.getParameters());
            setDisplayedInfo(showMode);
            return true;
        }
        return false;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setDbFuncCall(xmlFuncCall);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }
}
