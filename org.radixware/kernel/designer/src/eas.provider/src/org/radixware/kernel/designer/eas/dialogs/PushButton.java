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

package org.radixware.kernel.designer.eas.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.client.widgets.actions.Action;


public class PushButton extends JButton implements IPushButton {
    
    private String objectName;
    private List<ClickHandler> handlers = new LinkedList<>();
    
    public static class ButtonIcon extends ImageIcon implements Icon {
        
        @Override
        public long cacheKey() {
            return 0;
        }
    }
    
    public PushButton() {
        this.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ClickHandler h : handlers) {
                    h.onClick(PushButton.this);
                }
            }
        });
    }

    @Override
    public void click() {
        doClick();
    }
    
    
    
    @Override
    public boolean isDefault() {
        return false;
    }
    
    @Override
    public void setDefault(boolean bln) {
        //   throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getTitle() {
        return getText();
    }
    
    @Override
    public void setTitle(String string) {
        setText(string);
    }
    
    @Override
    public void setIcon(Icon icon) {
    }
    
    @Override
    public ButtonIcon getIcon() {
        return null;
    }
    
    @Override
    public void addClickHandler(ClickHandler ch) {
        handlers.add(ch);
    }

    @Override
    public void removeClickHandler(ClickHandler ch) {
        handlers.remove(ch);
    }

    @Override
    public void clearClickHandlers() {
        handlers.clear();
    }
        
    
    @Override
    public void addAction(Action action) {
    }
    
    @Override
    public Object findChild(Class<?> type, String string) {
        return null;
    }
    
    @Override
    public int width() {
        return getWidth();
    }
    
    @Override
    public int height() {
        return getHeight();
    }
    
    @Override
    public void setToolTip(String string) {
    }
    
    @Override
    public boolean isDisposed() {
        return false;
    }
    
    @Override
    public String getObjectName() {
        return objectName;
    }
    
    @Override
    public void setObjectName(String string) {
        objectName = string;
    }    

    @Override
    public IPeriodicalTask startTimer(TimerEventHandler teh) {
        throw new UnsupportedOperationException("startTimer is not supported in desinger environment.");
    }

    @Override
    public void killTimer(IPeriodicalTask ipt) {
        throw new UnsupportedOperationException("killTimer is not supported in desinger environment.");
    }        
}
