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

package org.radixware.wps.views.editors.valeditors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.ToolButton;


public class ExceptionViewController  extends InputBoxController<Throwable, EditMaskNone> {
    
    private final ToolButton showExceptionBtn = new ToolButton();
    private String stringToShow;
    private boolean isShowExceptionBtnVisible;    
    
    public ExceptionViewController(final IClientEnvironment env){
        this(env,null);
    }
    
    public ExceptionViewController(final IClientEnvironment env, final LabelFactory labelFactory){
        super(env,labelFactory);
        setEditMask(new EditMaskNone());
        setMandatory(true);
        stringToShow = env.getMessageProvider().translate("Value", "<Error!!!>");
        showExceptionBtn.setToolTip(env.getMessageProvider().translate("ExceptionDialog", "Show Details"));
        final Icon warningIcon =
                env.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.WARNING);
        showExceptionBtn.setIcon(warningIcon);
        showExceptionBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(IButton source) {
                    showException();
            }
        });
        addButton(showExceptionBtn);
        addTextOptionsMarkers(ETextOptionsMarker.INVALID_VALUE);
    }

    @Override
    protected InputBox.ValueController<Throwable> createValueController() {
        return null;//keyboard input is not allowed
    }

    private void showException(){
        if (getValue()!=null){
            final ExceptionMessage message = new ExceptionMessage(getEnvironment(), getValue());
            message.display((IWidget)this.getValEditor());
        }
    }    

    @Override
    protected InputBox.DisplayController<Throwable> createDisplayController() {
        return new InputBox.DisplayController<Throwable>(){
            @Override
            public String getDisplayValue(Throwable value, boolean isFocused, boolean isReadOnly) {
                final EditMask mask = getEditMask();
                if (value==null){
                    if (mask==null){
                        return "";
                    }else{
                        return mask.toStr(getEnvironment(), null);
                    }
                }else{
                    return stringToShow;
                }
            }
        };
    }    
    

    @Override
    public void setValue(final Throwable value) {
        super.setValue(value);
        showExceptionBtn.setVisible(isExceptionDetailsBtnVisible() && value!=null);
    }        
    
    public void setStringToShow(final String string){
        stringToShow = string;
        if (getValue()!=null){
            refresh();
        }
    }
    
    public String getStringToShow(){
        return stringToShow;
    }        
    
    public final void setExceptionDetailsBtnVisible(final boolean isVisible){
        isShowExceptionBtnVisible = isVisible;
    }
    
    public boolean isExceptionDetailsBtnVisible(){
        return isShowExceptionBtnVisible;
    }    

    @Override
    public void setEditHistory(final IEditingHistory history, final EValType type) {
    }

    
}
