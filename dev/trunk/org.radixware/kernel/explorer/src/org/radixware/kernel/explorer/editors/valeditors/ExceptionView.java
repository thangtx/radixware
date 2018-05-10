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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


public class ExceptionView extends ValEditor<Throwable>{
    
    private String stringToShow;
    private boolean isShowExceptionBtnVisible;
    private final QToolButton showExceptionBtn;
    
    public ExceptionView(final IClientEnvironment environment, final QWidget parent){
        super(environment, parent, new EditMaskNone(), true, false);
        getLineEdit().setReadOnly(true);
        stringToShow = environment.getMessageProvider().translate("Value", "<Error!!!>");
        {
            final QAction action = new QAction(this);
            action.triggered.connect(this, "showException()");
            action.setToolTip(environment.getMessageProvider().translate("ExceptionDialog", "Show Details"));
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.WARNING));
            action.setObjectName("show_details");
            showExceptionBtn = addButton(null, action);
            showExceptionBtn.setVisible(false);
        }
    }

    @Override
    public void setPredefinedValues(final List<Throwable> predefValues) {
    }

    @Override
    public void setEditingHistory(final IEditingHistory history) {
    }

    @Override
    protected ExplorerTextOptions calculateTextOptions(final EnumSet<ETextOptionsMarker> markers) {
        markers.add(ETextOptionsMarker.INVALID_VALUE);
        return super.calculateTextOptions(markers);
    }

    @Override
    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        return false;
    }

    @Override
    public void setValue(final Throwable value) {
        super.setValue(value);
        showExceptionBtn.setVisible(isExceptionDetailsBtnVisible() && value!=null);
    }        

    @Override
    protected String getStringToShow(final Object value) {
        return getValue()==null ? super.getStringToShow(value) : stringToShow;
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
    
    @SuppressWarnings("unused")
    private void showException(){
        if (getValue()!=null){
            final ExceptionMessage message = new ExceptionMessage(getEnvironment(), getValue());
            message.display(this);
        }
    }
}
