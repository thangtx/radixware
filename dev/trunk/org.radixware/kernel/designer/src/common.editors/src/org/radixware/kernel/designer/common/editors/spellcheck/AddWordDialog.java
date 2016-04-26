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

package org.radixware.kernel.designer.common.editors.spellcheck;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class AddWordDialog extends ModalDisplayer {

    private AddWordDialog(AddWordPanel panel, String title, boolean valid) {
        super(panel, title);

        getComponent().addPropertyChangeListener(AddWordPanel.PROP_NAME_WORD_VALIDITY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                getDialogDescriptor().setValid((Boolean) evt.getNewValue());
            }
        });
        
        getDialogDescriptor().setValid(valid);
    }

    public AddWordDialog(Word word, String description, String title) {
        this(new AddWordPanel(word, description), title, word != null);
    }

    public AddWordDialog(String title) {
        this(new AddWordPanel(), title, false);
    }

    @Override
    public AddWordPanel getComponent() {
        return (AddWordPanel) super.getComponent();
    }

    @Override
    protected boolean canClose() {
        return true;
    }

    @Override
    protected void showClosingProblems() {
    }

    Word getWord() {
        return Word.Factory.newInstance(getComponent().getWord());
    }

    String getDescription() {
        return getComponent().getDescription();
    }
}
