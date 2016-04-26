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

package org.radixware.kernel.common.client.views;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IWidget;


public interface IDialog extends IWidget {
    
    public static enum DialogResult {
        ACCEPTED(1),
        REJECTED(0),
        APPLY(2),
        NONE(-1),
        UNKNOWN(null);
        
        private final Integer value;
        
        private DialogResult(Integer value){
            this.value = value;
        }
        
        public static DialogResult getForValue(final int value){
            for (DialogResult item: DialogResult.values()){
                if (item.value!=null && item.value.intValue()==value){
                    return item;
                }
            }
            return UNKNOWN;
        }
    }    

    public interface DialogResultListener {

        public void dialogClosed(IDialog dialog, DialogResult result);
    }

    public static final class EventSupport {

        private Map<DialogResult, List<DialogResultListener>> listnersMap = null;
        private final IDialog dialog;

        public EventSupport(IDialog dialog) {
            this.dialog = dialog;
        }

        public void addResultListener(DialogResult result, DialogResultListener listener) {
            synchronized (this) {
                List<DialogResultListener> listeners = null;
                if (listnersMap == null) {
                    listnersMap = new EnumMap<DialogResult, List<DialogResultListener>>(DialogResult.class);
                    listeners = new LinkedList<DialogResultListener>();
                    listnersMap.put(result, listeners);
                } else {
                    listeners = listnersMap.get(result);
                    if (listeners == null) {
                        listeners = new LinkedList<DialogResultListener>();
                        listnersMap.put(result, listeners);
                    }
                }
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }

        public void removeResultListener(DialogResult result, DialogResultListener listener) {
        }

        public void removeResultListeners(DialogResult result) {
        }
    }

    public String getWidowTitle();

    public void setWindowTitle(String title);

    public Icon getWindowIcon();

    public void setWindowIcon(Icon icon);

    public DialogResult execDialog();

    public DialogResult execDialog(IWidget parentWidget);

    public void acceptDialog();

    public void rejectDialog();

    public DialogResult getDialogResult();

    public EventSupport getEventSupport();    
}
