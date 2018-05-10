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
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ParameterCondition_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;


public class SqmlTag_IfParam extends SqmlTag {
    
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PREPROCESSOR";
            
    private final ISqmlParameters parameters;
    private final Sqml.Item.IfParam ifParamItem;
    
    public SqmlTag_IfParam(final IClientEnvironment environment, 
                               final ISqmlParameter parameter, 
                               final EIfParamTagOperator operator,
                               final ValAsStr value,
                               final long pos, 
                               final ISqmlParameters params, 
                               final EDefinitionDisplayMode displayMode) {
        super(environment, pos, parameter==null ? false :parameter.isDeprecated());
        this.parameters = params;
        ifParamItem = Sqml.Item.IfParam.Factory.newInstance();
        if (operator!=null){
            ifParamItem.setOperation(operator);
        }
        if (value!=null){
            ifParamItem.setValue(value.toString());
        }
        if (parameter!=null){
            ifParamItem.setParamId(parameter.getId());
        }
        setDisplayedInfo(displayMode);//NOPMD
    }
    
    public SqmlTag_IfParam(final IClientEnvironment environment, 
                               final Sqml.Item.IfParam xmlItem, 
                               final long pos,
                               final ISqmlParameters params, 
                               final EDefinitionDisplayMode displayMode) {
        super(environment, pos);
        this.parameters = params;
        ifParamItem = (Sqml.Item.IfParam)xmlItem.copy();
        final Id paramId = xmlItem.getParamId();
        final ISqmlParameter parameter;
        if (parameters==null || paramId==null){
            parameter=null;
        }else{
            parameter = parameters.getParameterById(paramId);
        }
        if (parameter!=null && parameter.getType()!=null){
            setIsDeprecated(parameter.isDeprecated());
        }
        setDisplayedInfo(displayMode);//NOPMD
    }
    
    private SqmlTag_IfParam(final IClientEnvironment environment, final SqmlTag_IfParam source){
        super(environment, source);
        ifParamItem = (Sqml.Item.IfParam)source.ifParamItem.copy();
        parameters = source.parameters;
    }

    @Override    
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (parameters!=null){
            final StringBuilder displayTextBuilder = new StringBuilder("#IF (");
            final String paramStr;
            final Id paramId = ifParamItem.getParamId();
            final String value = ifParamItem.getValue();
            final EIfParamTagOperator operator = ifParamItem.getOperation();            
            final EValType valType; 
            final EditMask mask;
            if (paramId==null){
                paramStr = "???";
                setValid(false);
                valType = null;
                mask = null;
            }else{
                final ISqmlParameter parameter = parameters.getParameterById(paramId);
                if (parameter==null || parameter.getType()==null){
                    paramStr = "???"+paramId.toString()+"???";
                    setValid(false);
                    valType = null;
                    mask = null;
                }else{
                    paramStr = parameter.getDisplayableText(showMode);
                    valType = parameter.getType();
                    mask = parameter.getEditMask();
                }
            }
            final boolean isArray = valType==null ? false : valType.isArrayType();
            displayTextBuilder.append(paramStr);
            if (operator==null){
                displayTextBuilder.append(" ??? ");
                setValid(false);
            }else{                
                switch(operator){
                    case EQUAL:{
                        if (isArray){
                            displayTextBuilder.append("!=null && ");
                            displayTextBuilder.append(paramStr);
                            displayTextBuilder.append(".contains(\'");
                            displayTextBuilder.append(getValStr(value, valType, mask));
                            displayTextBuilder.append("\')");
                        }else{
                            if (valType==EValType.STR){
                                displayTextBuilder.append("!=null && ");
                                displayTextBuilder.append(paramStr);
                                displayTextBuilder.append(".equals(\'");
                                displayTextBuilder.append(getValStr(value, valType, mask));
                                displayTextBuilder.append("\')");
                            }else{
                                displayTextBuilder.append("==");
                                displayTextBuilder.append(getValStr(value, valType, mask));
                            }
                        }
                        break;
                    } case NOT_EQUAL:{
                        if (isArray){
                            displayTextBuilder.append("==null || !");
                            displayTextBuilder.append(paramStr);
                            displayTextBuilder.append(".contains(\'");
                            displayTextBuilder.append(getValStr(value, valType, mask));
                            displayTextBuilder.append("\')");
                        }else{
                            if (valType==EValType.STR){
                                displayTextBuilder.append("==null || !");
                                displayTextBuilder.append(paramStr);
                                displayTextBuilder.append(".equals(\'");
                                displayTextBuilder.append(getValStr(value, valType, mask));
                                displayTextBuilder.append("\')");
                            }else{
                                displayTextBuilder.append("!=");
                                displayTextBuilder.append(getValStr(value, valType, mask));
                            }
                        }
                        break;
                    } case NULL:{
                        displayTextBuilder.append("==null");
                        break;
                    } case NOT_NULL:{
                        displayTextBuilder.append("!=null");
                        break;
                    } case EMPTY:
                       case NOT_EMPTY:{
                        displayTextBuilder.append("!=null && ");
                        if (valType!=null && !isArray){
                            setValid(false);
                            displayTextBuilder.append("???");
                        }
                        if (operator==EIfParamTagOperator.NOT_EMPTY){
                            displayTextBuilder.append("!");
                        }
                        displayTextBuilder.append(paramStr);
                        displayTextBuilder.append(".isEmpty()");
                        if (valType!=null && !isArray){
                            displayTextBuilder.append("???");
                        }
                        break;
                    }default:{
                        setValid(false);
                        displayTextBuilder.append(" ???");
                        displayTextBuilder.append(operator.getValue());
                        displayTextBuilder.append("???");
                    }
                }
            }
            displayTextBuilder.append(") THEN");
            setDisplayedInfo(getToolTip(paramStr),displayTextBuilder.toString());
            return true;
        }else{
            return false;
        }
    }    
    
    private String getToolTip(final String paramName) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final StringBuilder strBuilder = new StringBuilder();
        final String template = msgProvider.translate("SqmlEditor", "Filter parameter: '%1$s'");
        strBuilder.append("<b>");
        strBuilder.append(String.format(template, Html.string2HtmlString(paramName)));
        strBuilder.append("</b>");
        return strBuilder.toString();
    }    
    
    private String getValStr(final String value, final EValType valType, final EditMask editMask){
        if (valType==null || editMask==null){
            setValid(false);
            return "???"+value+"???";            
        }
        if (value!=null && !value.isEmpty()){
            try{                
                final Object valObj = ValAsStr.fromStr(value, valType.isArrayType() ? valType.getArrayItemType() : valType);
                return editMask.toStr(environment, valObj);
            }catch(WrongFormatError | IllegalArgumentException error){
                setValid(false);
                return "???"+value+"???";
            }            
        }else{
            switch(valType){//NOPMD
                case STR:
                case ARR_STR:
                    return "";
                case INT:
                case NUM:
                case ARR_INT:
                case ARR_NUM:
                    return "0";
                default:
                    return "null";
            }
        }
    }

    @Override
    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        if (isValid()){
            final ParameterCondition_Dialog dialog = 
                new ParameterCondition_Dialog(parameters, ifParamItem, showMode, editText, environment);
            if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
                final Sqml.Item.IfParam result = dialog.getIfParamTag();
                ifParamItem.setParamId(result.getParamId());
                ifParamItem.setOperation(result.getOperation());
                ifParamItem.setValue(result.getValue());
                editText.scheduleUpdateTagNames();
                return true;
            }else{
                return false;
            }            
        }else{
            return false;
        }
    }
        
    @Override
    public TagInfo copy() {
        return new SqmlTag_IfParam(environment, this);
    }        
    
    @Override
    protected String getSettingsPath() {
        return PATH;
    }    
    
    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.setIfParam((Sqml.Item.IfParam)ifParamItem.copy());
    }  
}
