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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.types.Id;


public class SqmlTagParameterTranslator  extends  SqmlTagTranslator{
    
    private final ISqmlParameter parameter;
    private final ISqmlTableDef contextTable;
    private final ISqmlParameters parameters;
    private final Id  paramId;
    private final IClientEnvironment environment;
    
    public SqmlTagParameterTranslator(final ISqmlParameter parameter,final ISqmlTableDef contextTable,
           final ISqmlParameters parameters,final Id  paramId,final IClientEnvironment environment,final boolean isVaid){
        super(isVaid);
        this.parameter=parameter;
        this.contextTable=contextTable;
        this.parameters=parameters;
        this.paramId=paramId;
        this.environment=environment;
    }
    
    @Override
    public String getDisplayString() {
         if (parameter != null && parameters != null) {
              if (!isValid) {
                    final String idAsStr = parameter.getId() == null ? "" : parameter.getId().toString();
                   return  "::???" + idAsStr + "???";
              }
              final String prefix;
              if (parameter.getPersistentValue() != null) {
                        final Object obj = parameter.getPersistentValue().getValObject();
                        prefix = obj == null ? "::null" : "::" + parameter.getEditMask().toStr(environment,obj);
              } else {
                        prefix = "::" + parameter.getDisplayableText(displayMode);
              }
              return prefix;
          }
        return "::???" + paramId + "???";
    }          

    @Override
    public String getToolTip() {
         if (parameter != null && parameters != null) {
            /*if (parameters.getParameterById(parameter.getId()) == null) {
                isValid = false;
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
            setDisplayedInfo(calculateToolTip(showMode), prefix);*/
            return calculateToolTip();
        }
        return "";
    }
    
    private String calculateToolTip() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final StringBuilder strBuilder = new StringBuilder(96);
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
        strBuilder.append("<br><b>");
        strBuilder.append(valueTypeStr);
        strBuilder.append(":</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        strBuilder.append(parameter.getType().getName());
        strBuilder.append("</br>");
        return strBuilder.toString();
    }
}
