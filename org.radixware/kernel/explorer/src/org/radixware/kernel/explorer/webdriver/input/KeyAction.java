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

package org.radixware.kernel.explorer.webdriver.input;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.explorer.webdriver.WebDrvKeyEvents;

abstract class KeyAction extends InputAction<KeyInputSource>{
    
    private final Qt.Key key;
    private final char key_char;
    private final String key_symbol;
    
    public KeyAction(final KeyInputSource inputSource, final char key){
        super(inputSource);
        key_char = key;
        this.key = WebDrvKeyEvents.charCode2QKey(key);
        if (WebDrvKeyEvents.isPrintableChar(key)){
            key_symbol = String.valueOf(key);
        }else{
            key_symbol = null;
        }
    }
    
    protected final Qt.Key getQtKey(){
        return key;
    }    

    protected final char getKeyChar() {
        return key_char;
    }

    protected final String getKeySymbol() {
        return key_symbol;
    }
    
    protected static QWidget getTargetWidget(){
        {
            final QWidget widget = QApplication.focusWidget();
            if (widget!=null && widget.nativeId()!=0){
                return widget;
            }
        }
        {
            final QWidget widget = QApplication.activePopupWidget();
            if (widget!=null && widget.nativeId()!=0){
                return widget;
            }
        }        
        {
            final QWidget widget = QApplication.activeModalWidget();
            if (widget!=null && widget.nativeId()!=0){
                return widget;
            }
        }        
        {
            final QWidget widget = QApplication.activeWindow();
            if (widget!=null && widget.nativeId()!=0){
                return widget;
            }
        }        
        {
            final List<QWidget> widgets = QApplication.topLevelWidgets();
            for (QWidget widget: widgets){
                if (widget!=null && widget.nativeId()!=0){
                    return widget;
                }                
            }
        }
        {
            final List<QWidget> widgets = QApplication.allWidgets();
            for (QWidget widget: widgets){
                if (widget!=null && widget.nativeId()!=0){
                    return widget;
                }
            }            
        }
        return null;
    }
}
