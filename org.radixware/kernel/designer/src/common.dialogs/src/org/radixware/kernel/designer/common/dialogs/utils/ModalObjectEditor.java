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

package org.radixware.kernel.designer.common.dialogs.utils;

import javax.swing.JPanel;

import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public abstract class ModalObjectEditor<T extends Object> extends JPanel {

    private boolean correct = true;
    private ModalDisplayer md;
    private EditorOpenInfo openInfo;
    private T object;

    public ModalObjectEditor() {
        super();
    }

    /**
     * Set edited object to the editor.
     *
     * @param object
     * @param openInfo - additional information about context, etc.
     */
    public void open(T object, EditorOpenInfo openInfo) {
        this.object = object;
        this.openInfo = openInfo;
        afterOpen();
        if (openInfo != null) {
            setReadOnly(openInfo.isReadOnly());
        }
    }

    /**
     * Get edited object
     *
     * @return
     */
    public T getObject() {
        return object;
    }

    public EditorOpenInfo getOpenInfo() {
        return openInfo;
    }

    /**
     * Shows this panel in modal mode.
     *
     * @return true if OK was pressed.
     */
    public boolean showModal() {
        if (!beforeShowModal()) {
            return false;
        }

        if (md == null) {
            md = new ModalDisplayer(this, getTitle()) {

                @Override
                protected void apply() {
                    applyChanges();
                }
            };
            md.getDialogDescriptor().setValid(correct);
//            md.setValid(correct);
        }

        return md.showModal();
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        if (this.correct != correct) {
            this.correct = correct;
            if (md != null) {
                md.getDialogDescriptor().setValid(correct);
//                md.setValid(correct);
            }
        }
    }

    /**
     * Called actually before displaying editor. If this method returns false,
     * then editor will not be shown.
     *
     * @return
     */
    protected boolean beforeShowModal() {
        return true;
    }

    /**
     * Title of the model window
     *
     * @return
     */
    protected abstract String getTitle();

    /**
     * Called after OK was pressed. This method should apply all changes made in
     * editor to the edited object.
     */
    protected abstract void applyChanges();

    protected abstract void afterOpen();

    protected abstract void setReadOnly(boolean readOnly);
}
