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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.util.LinkedList;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm.ChangedEvent;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm.IChangeListener;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public class AdsReportFormUndoRedo {

    private final AdsReportForm form;
    private final static int MAX = 100; // max history size
    private final LinkedList<org.radixware.schemas.adsdef.Report.Form> xForms = new LinkedList<>(); // history
    private final RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent> stateSupport = new RadixEventSource<>(); // support to listen undo/redo possibility
    private int index = -1; // pointed to current form state in xForms
    private long mergetTimeIntervalMs = 500;
    private long lastChangeTime = 0;
    private boolean isBlocked=false;
    private final IChangeListener changeListener = new IChangeListener() { // listened report form changes

        @Override
        public void onEvent(ChangedEvent e) {
            if (SwingUtilities.isEventDispatchThread()) {
                onChanged();
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        onChanged();
                    }
                });
            }
        }
    };

    public AdsReportFormUndoRedo(AdsReportForm form) {
        this.form = form;
        onChanged();
        form.addChangeListener(changeListener);
    }

    private void onChanged() {
        final org.radixware.schemas.adsdef.Report.Form xForm = org.radixware.schemas.adsdef.Report.Form.Factory.newInstance();
        form.appendTo(xForm, ESaveMode.NORMAL);
        final String xFormAsXmlString = xForm.xmlText();

        // ignore identival changes
        if (!xForms.isEmpty()) {
            final org.radixware.schemas.adsdef.Report.Form lastXForm = xForms.get(index);
            final String lastXFormAsXmlString = lastXForm.xmlText();

            if (Utils.equals(lastXFormAsXmlString, xFormAsXmlString)) {
                return; // no changes
            }
        }

        // remove redo
        while (xForms.size() > index + 1) {
            xForms.removeLast();
        }

        // merge offen changes
        final long now = System.currentTimeMillis();
        if (!xForms.isEmpty() && now - lastChangeTime < mergetTimeIntervalMs) {
            xForms.removeLast();
            index--;
        } else {
            lastChangeTime = now;
        }

        // add snapshot
        xForms.add(xForm);
        index++;

        // limit history size
        if (xForms.size() > MAX) {
            xForms.removeFirst();
            index--;
        }

        fireStateChanged();
    }

    private void fireStateChanged() {
        stateSupport.fireEvent(new RadixEvent());
    }

    public synchronized void addStateListener(IRadixEventListener<RadixEvent> listener) {
        stateSupport.addEventListener(listener);
    }

    public synchronized void removeStateListener(IRadixEventListener<RadixEvent> listener) {
        stateSupport.removeEventListener(listener);
    }

    public boolean canUndo() {
        return index > 0;
    }

    public boolean canRedo() {
        return index < xForms.size() - 1;
    }

    // STATE      INDEX   SIZE    CAN UNDO   CAN REDO
    //  initial     0       1        -          -
    //  changed     1       2        +          -
    //  undo        0       2        -          +
    //  redo        1       2        +          -
    private void apply() {
        try {
            form.removeChangeListener(changeListener);
            final org.radixware.schemas.adsdef.Report.Form xForm = xForms.get(index);
            form.loadFrom(xForm);
        } finally {
            form.addChangeListener(changeListener);
        }
        fireStateChanged();
    }
    
    public void block(boolean block){
        if(block!=isBlocked){
            isBlocked=block;
            if(block){
                form.removeChangeListener(changeListener);
            }else{
                form.addChangeListener(changeListener);
            }
        }
    }
    
    public boolean isBlocked(){
        return isBlocked;
    }

    public void undo() {
        index--;
        apply();
    }

    public void redo() {
        index++;
        apply();
    }

    public long getMergetTimeIntervalMs() {
        return mergetTimeIntervalMs;
    }

    public void setMergetTimeIntervalMs(long mergetTimeIntervalMs) {
        this.mergetTimeIntervalMs = mergetTimeIntervalMs;
    }
}
