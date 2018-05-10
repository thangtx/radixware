/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import com.trolltech.qt.gui.QDialog;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ParamValCount_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;


public class SqmlTag_ParamValCount extends SqmlTag {

    private final org.radixware.schemas.xscml.Sqml.Item.ParamValCount xTag;
    private ISqmlParameter parameter;
    private final ISqmlParameters parameters;
    private static final String CONFIG_PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PARAMETER";
    
    public SqmlTag_ParamValCount(final IClientEnvironment environment, 
                                 final ISqmlParameter parameter, 
                                 final long pos,
                                 final ISqmlParameters params, 
                                 final EDefinitionDisplayMode displayMode){
        super(environment, pos,parameter==null?false :parameter.isDeprecated());
        parameters = params;
        this.parameter = parameter;
        xTag = org.radixware.schemas.xscml.Sqml.Item.ParamValCount.Factory.newInstance();
        if (parameter!=null){
            xTag.setParamId(parameter.getId());
        }
        setDisplayedInfo(displayMode);        
    }
    
    public SqmlTag_ParamValCount(final IClientEnvironment environment, 
                                 final org.radixware.schemas.xscml.Sqml.Item.ParamValCount xmlTag, 
                                 final ISqmlParameters parameters, 
                                 final long pos,
                                 final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        this.parameters = parameters;
        xTag = (org.radixware.schemas.xscml.Sqml.Item.ParamValCount) xmlTag.copy();
        parameter = parameters.getParameterById(xmlTag.getParamId());
        if (parameter == null || parameter.getType()==null) {
            setValid(false);
            setDisplayedInfo(null, "::???" + xmlTag.getParamId() + "???");
        } else {
            setIsDeprecated(parameter.isDeprecated());
            setDisplayedInfo(displayMode);
        }
    }
    
    public SqmlTag_ParamValCount(final IClientEnvironment environment, final SqmlTag_ParamValCount source){
        super(environment, source);
        xTag = (org.radixware.schemas.xscml.Sqml.Item.ParamValCount) source.xTag.copy();
        parameter = source.parameter;
        parameters = source.parameters;       
    }
    
    private String calculateToolTip() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final StringBuilder strBuilder = new StringBuilder();
        final String toolTip = msgProvider.translate("SqmlEditor", "Count of values in %1$s parameter");
        strBuilder.append("<b>");
        if (parameter==null || !isValid()){
            strBuilder.append(String.format(toolTip, "???"));                        
        }else{
            strBuilder.append(String.format(toolTip, Html.string2HtmlString(parameter.getTitle())));
        }
        strBuilder.append("</b>");
        return strBuilder.toString();
    }
    
    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        final Id parameterId = parameter==null ? null : parameter.getId();
        final ParamValCount_Dialog dialog = new ParamValCount_Dialog(environment, parameterId, parameters, editText);
        if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
            final Id selectedParameterId = dialog.getSelectedId();
            final ISqmlParameter selectedParameter = selectedParameterId==null ? null : parameters.getParameterById(selectedParameterId);
            if (selectedParameter!=null){
                parameter = selectedParameter;
                xTag.setParamId(selectedParameterId);
                setDisplayedInfo(showMode);
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    
    @Override
    public final boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (parameter != null && parameters != null) {
            if (parameters.getParameterById(parameter.getId()) == null) {
                setValid(false);
                final String idAsStr = parameter.getId() == null ? "" : parameter.getId().toString();
                setDisplayedInfo(null, "valCountOf(::???" + idAsStr + "???)");
            }else if (parameter.getPersistentValue() != null) {
                setValid(true);
                final Object obj = parameter.getPersistentValue().getValObject();
                setDisplayedInfo(calculateToolTip(), String.valueOf(calcValCount(obj)));
            } else {
                setValid(true);
                setDisplayedInfo(calculateToolTip(), "valCountOf(::" + parameter.getDisplayableText(showMode) + ")");
            }
            return true;
        }
        return false;
    }    
    
    private static int calcValCount(final Object param){
        if (param instanceof Arr){
            return ((Arr)param).size();
        }else{
            return param==null ? -1 : 1;
        }
    }

    @Override
    public SqmlTag_ParamValCount copy() {
        return new SqmlTag_ParamValCount(environment, this);
    }
    
    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setParamValCount(xTag);
    }

    @Override
    protected String getSettingsPath() {
        return CONFIG_PATH;
    }    
}
