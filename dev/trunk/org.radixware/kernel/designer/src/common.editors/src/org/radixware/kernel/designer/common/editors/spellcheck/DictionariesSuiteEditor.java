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

/*
 * DictionaryEditor.java
 *
 * Created on Dec 15, 2011, 10:44:14 AM
 */
package org.radixware.kernel.designer.common.editors.spellcheck;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.utils.spellchecker.DictionariesSuite;


public class DictionariesSuiteEditor extends RadixObjectEditor<DictionariesSuite> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DictionariesSuite> {

        @Override
        public IRadixObjectEditor<DictionariesSuite> newInstance(DictionariesSuite container) {
            return new DictionariesSuiteEditor(container);
        }
    }

    DictionariesSuiteEditor() {
        this(null);
    }

    DictionariesSuiteEditor(DictionariesSuite dictionaries) {
        super(dictionaries);

        setLayout(new java.awt.BorderLayout());

        JPanel mainPanel = new DictionaryEditorMainPanel(dictionaries);
        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean open(OpenInfo openInfo) {
        return super.open(openInfo);
    }

    @Override
    public void onClosed() {
        DictionariesSuite dictionariesSuite = getRadixObject();
        dictionariesSuite.releaseDictionaries();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
