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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.QtPropertyWriter;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class ExplorerDialogButtonBox extends QWidget{
    
    private final QLayout layout = WidgetUtils.createHBoxLayout(this);
    private final QDialogButtonBox internalButtonBox;
    
    private final IClientEnvironment environment;
    private final Map<String, IPushButton> buttonsByType = new HashMap<>();
    private final EnumMap<QDialogButtonBox.StandardButton, String> buttonTypesByStdButton = new EnumMap<>(QDialogButtonBox.StandardButton.class);
    
    public final Signal0 accepted = new Signal0();
    public final Signal1<QAbstractButton> clicked = new Signal1<>();
    public final Signal0 helpRequested = new Signal0();
    public final Signal0 rejected = new Signal0();
    
    public ExplorerDialogButtonBox(final IClientEnvironment environment, final QWidget parent){
        super(parent);
        internalButtonBox = new QDialogButtonBox(this);
        internalButtonBox.accepted.connect(accepted);
        internalButtonBox.clicked.connect(clicked);
        internalButtonBox.helpRequested.connect(helpRequested);
        internalButtonBox.rejected.connect(rejected);
        layout.addWidget(internalButtonBox);
        this.environment = environment;
    }
    
    public IPushButton addButton(final String buttonType){
        if (buttonsByType.containsKey(buttonType)){
            return buttonsByType.get(buttonType);
        }
        final IPushButton button = 
            environment.getApplication().getWidgetFactory().newPushButton();
        addButtonToInternalButtonBox((QAbstractButton) button, QDialogButtonBox.ButtonRole.ActionRole, true);
        button.setObjectName(buttonType);
        buttonsByType.put(buttonType, button);
        return button;
    }
    
    public QAbstractButton addButton(final EDialogButtonType buttonType){
        return addButtonImpl(buttonType, true);
    }
    
    private QAbstractButton addButtonImpl(final EDialogButtonType buttonType, final boolean restoreVisibility){
        if (buttonsByType.containsKey(buttonType.getValue())){
            return (QAbstractButton)buttonsByType.get(buttonType.getValue());
        }
        final IPushButton button = 
            environment.getApplication().getWidgetFactory().newDialogButton(buttonType, environment);        
        button.setObjectName(buttonType.getValue());
        addButtonToInternalButtonBox((QAbstractButton) button, buttonType2buttonRole(buttonType), restoreVisibility);
        buttonsByType.put(buttonType.getValue(), button);
        return (QAbstractButton)button;        
    }
    
    public EnumMap<EDialogButtonType,QAbstractButton> addButtons(final EnumSet<EDialogButtonType> buttonTypes){
        final EnumMap<EDialogButtonType,QAbstractButton>  buttons = new EnumMap<>(EDialogButtonType.class);
        final Map<String,Boolean> visibility = storeButtonsVisibility();
        for (EDialogButtonType buttonType: buttonTypes){
            buttons.put(buttonType,addButtonImpl(buttonType,false));
        }
        restoreButtonsVisibility(visibility);
        return buttons;
    }
        
    public void clearButtons() {
        internalButtonBox.clear();
        buttonsByType.clear();
        buttonTypesByStdButton.clear();
    }

    public IPushButton getButton(final String buttonType) {
        return buttonsByType.get(buttonType);
    }    
    
    public IPushButton getButton(final EDialogButtonType button) {
        return getButton(button.getValue());
    }
    
    public void removeButton(final EDialogButtonType button) {
        removeButton(button.getValue());
    }
    
    public void removeButton(final String buttonType) {
        final IPushButton buttonToRemove = buttonsByType.get(buttonType);
        if (buttonToRemove != null) {
            final Map<String,Boolean> visibility = storeButtonsVisibility();
            internalButtonBox.removeButton((QAbstractButton) buttonToRemove);
            buttonsByType.remove(buttonType);
            restoreButtonsVisibility(visibility);
            QDialogButtonBox.StandardButton stdButton = null;
            for (Map.Entry<QDialogButtonBox.StandardButton, String> entry: buttonTypesByStdButton.entrySet()){
                if (Objects.equals(entry.getValue(), buttonType)){
                    stdButton = entry.getKey();
                    break;
                }
            }
            if (stdButton!=null){
                buttonTypesByStdButton.remove(stdButton);
            }
        }
    }    
    
    private static QDialogButtonBox.ButtonRole buttonType2buttonRole(final EDialogButtonType button) {
        switch (button) {
            case ABORT:
            case IGNORE:
            case SKIP:
            case NO:
            case NO_BUTTON:
            case NO_TO_ALL:
                return QDialogButtonBox.ButtonRole.NoRole;
            case ALL:
            case OK:
            case OPEN:
            case SAVE:
            case SAVE_ALL:
                return QDialogButtonBox.ButtonRole.AcceptRole;
            case APPLY:
                return QDialogButtonBox.ButtonRole.ApplyRole;
            case CANCEL:
            case CLOSE:
            case DISCARD:
                return QDialogButtonBox.ButtonRole.RejectRole;
            case HELP:
                return QDialogButtonBox.ButtonRole.HelpRole;
            case RESET:
            case RESTORE_DEFAULTS:
                return QDialogButtonBox.ButtonRole.ResetRole;
            case RETRY:
                return QDialogButtonBox.ButtonRole.ActionRole;
            case YES:
            case YES_TO_ALL:
                return QDialogButtonBox.ButtonRole.YesRole;
            default:
                throw new IllegalArgumentException("Can't find ButtonRole for " + button.name());
        }
    }
    
    private static EDialogButtonType buttonRole2ButtonType(final QDialogButtonBox.ButtonRole buttonRole){
        switch(buttonRole){
            case AcceptRole:
                return EDialogButtonType.OK;
            case ActionRole:
                return EDialogButtonType.RETRY;
            case ApplyRole:
                return EDialogButtonType.APPLY;
            case DestructiveRole:
            case InvalidRole:
                return EDialogButtonType.NO_BUTTON;
            case HelpRole:
                return EDialogButtonType.HELP;
            case RejectRole:
                return EDialogButtonType.CANCEL;
            case NoRole:
                return EDialogButtonType.NO;
            case ResetRole:
                return EDialogButtonType.RESET;
            case YesRole:
                return EDialogButtonType.YES;
            default:
                throw new IllegalArgumentException("Can't find dialog button type for " + buttonRole.name());
        }
    }
    
    private static EDialogButtonType standardButton2ButtonType(final QDialogButtonBox.StandardButton button){
        switch (button){
            case Abort:
                return EDialogButtonType.ABORT;
            case Apply:
                return EDialogButtonType.APPLY;
            case Cancel:
                return EDialogButtonType.CANCEL;
            case Close:
                return EDialogButtonType.CLOSE;
            case Discard:
                return EDialogButtonType.DISCARD;
            case Help:
                return EDialogButtonType.HELP;
            case Ignore:
                return EDialogButtonType.IGNORE;
            case No:
                return EDialogButtonType.NO;
            case NoButton:
                return EDialogButtonType.NO_BUTTON;
            case NoToAll:
                return EDialogButtonType.NO_TO_ALL;
            case Ok:
                return EDialogButtonType.OK;
            case Open:
                return EDialogButtonType.OPEN;
            case Reset:
                return EDialogButtonType.RESET;
            case RestoreDefaults:
                return EDialogButtonType.RESTORE_DEFAULTS;
            case Retry:
                return EDialogButtonType.RETRY;
            case Save:
                return EDialogButtonType.SAVE;
            case SaveAll:
                return EDialogButtonType.SAVE_ALL;
            case Yes:
                return EDialogButtonType.YES;
            case YesToAll:
                return EDialogButtonType.YES_TO_ALL;
            default:
                throw new IllegalArgumentException("Can't find dialog button type for "+button.name());
        }
    }
        
    //QDialogButtonBox wrapping
    @Deprecated
    public final void addButton(final QAbstractButton button, final QDialogButtonBox.ButtonRole buttonRole) {
        addButtonToInternalButtonBox(button, buttonRole, true);
    }
    
    private void addButtonToInternalButtonBox(final QAbstractButton button, final QDialogButtonBox.ButtonRole buttonRole, final boolean restoreVisibility){
        final Map<String,Boolean> visibility = restoreVisibility ? storeButtonsVisibility() : null;
        internalButtonBox.addButton(button, buttonRole);
        if (visibility!=null){
            restoreButtonsVisibility(visibility);
        }
    }
    
    public final QPushButton addButton(final QDialogButtonBox.StandardButton button) {
        return addButtonImpl(button, true);
    }    
    
    private QPushButton addButtonImpl(final QDialogButtonBox.StandardButton button, final boolean restoreVisibility){
        if (buttonTypesByStdButton.containsKey(button)){
            return (QPushButton)buttonsByType.get(buttonTypesByStdButton.get(button));
        }
        final EDialogButtonType buttonType = standardButton2ButtonType(button);        
        final QPushButton result = (QPushButton)addButtonImpl(buttonType,restoreVisibility);
        buttonTypesByStdButton.put(button, buttonType.getValue());
        return result;        
    }
    
    public final QPushButton addButton(final String title, final QDialogButtonBox.ButtonRole buttonRole) {
        final QPushButton button = (QPushButton)addButton(buttonRole2ButtonType(buttonRole));
        button.setText(title);
        return button;
    }

    public final QPushButton button(final QDialogButtonBox.StandardButton button) {
        final QPushButton qbutton = internalButtonBox.button(button);
        if (qbutton==null){
            return (QPushButton)getButton(standardButton2ButtonType(button));
        }else{
            return qbutton;
        }
    }
    
    public final QDialogButtonBox.ButtonRole buttonRole(final QAbstractButton button) {
        return internalButtonBox.buttonRole(button);
    }

    public final List<QAbstractButton> buttons() {
        return internalButtonBox.buttons();
    }

    public final boolean centerButtons() {
        final Map<String,Boolean> visibility = storeButtonsVisibility();
        final boolean result = internalButtonBox.centerButtons();
        restoreButtonsVisibility(visibility);
        return result;
    }

    public final void clear() {
        clearButtons();
    }

    public final Qt.Orientation orientation() {
        return internalButtonBox.orientation();
    }

    public final void removeButton(final QAbstractButton button) {        
        String type = null;
        for (Map.Entry<String,IPushButton> entry: buttonsByType.entrySet()){
            if (entry.getValue()==button){
                type = entry.getKey();
                break;
            }
        }
        if (type!=null){
            removeButton(type);
        }else{
            final Map<String,Boolean> visibility = storeButtonsVisibility();
            internalButtonBox.removeButton(button);
            restoreButtonsVisibility(visibility);
        }
    }

    public final void setCenterButtons(final boolean center) {
        final Map<String,Boolean> visibility = storeButtonsVisibility();
        internalButtonBox.setCenterButtons(center);
        restoreButtonsVisibility(visibility);
    }

    public final void setOrientation(final Qt.Orientation orientation) {
        final Map<String,Boolean> visibility = storeButtonsVisibility();
        internalButtonBox.setOrientation(orientation);
        restoreButtonsVisibility(visibility);
    }

    @QtPropertyWriter(enabled=false)
    public final void setStandardButtons(final QDialogButtonBox.StandardButton... buttons) {
        final Map<String,Boolean> visibility = storeButtonsVisibility();
        for (QDialogButtonBox.StandardButton button: buttons){
            addButtonImpl(button,false);
        }
        restoreButtonsVisibility(visibility);
    }
    
    public final void setStandardButtons(final QDialogButtonBox.StandardButtons buttons) {        
        final EnumSet<QDialogButtonBox.StandardButton> allButtons = 
                EnumSet.allOf(QDialogButtonBox.StandardButton.class);
        final Map<String,Boolean> visibility = storeButtonsVisibility();
        boolean buttonBoxChanged = false;
        for (QDialogButtonBox.StandardButton button: allButtons){
            if (buttons.isSet(button)){
                addButtonImpl(button,false);
                buttonBoxChanged = true;
            }
        }
        if (buttonBoxChanged){
            restoreButtonsVisibility(visibility);
        }
    }

    public final QDialogButtonBox.StandardButton standardButton(final QAbstractButton button) {
        String type = null;
        for (Map.Entry<String,IPushButton> entry: buttonsByType.entrySet()){
            if (entry.getValue()==button){
                type = entry.getKey();
                break;
            }
        }
        if (type!=null){
            for (Map.Entry<QDialogButtonBox.StandardButton, String> entry: buttonTypesByStdButton.entrySet()){
                if (type.equals(entry.getValue())){
                    return entry.getKey();
                }
            }
        }
        return internalButtonBox.standardButton(button);
    }
    
    public final QDialogButtonBox.StandardButtons standardButtons() {
        final QDialogButtonBox.StandardButton[] buttons = new QDialogButtonBox.StandardButton[buttonTypesByStdButton.size()];
        int i = 0;
        for (QDialogButtonBox.StandardButton button: buttonTypesByStdButton.keySet()){
            buttons[i] = button;
            i++;
        }
        return new QDialogButtonBox.StandardButtons(buttons);
    }
    
    private Map<String,Boolean> storeButtonsVisibility(){
        final Map<String,Boolean> visibility = new HashMap<>();
        QAbstractButton button;
        for (Map.Entry<String,IPushButton> entry: buttonsByType.entrySet()){
            button = (QAbstractButton)entry.getValue();
            //isVisible() returns false if button was not shown yet
            visibility.put(entry.getKey(), Boolean.valueOf(!button.isHidden()));
        }
        return visibility;
    }
    
    private void restoreButtonsVisibility(final Map<String,Boolean> visibility){        
        for (Map.Entry<String,IPushButton> entry: buttonsByType.entrySet()){
            if (visibility.containsKey(entry.getKey())){
                entry.getValue().setVisible(visibility.get(entry.getKey()).booleanValue());
            }
        }    
    }
}