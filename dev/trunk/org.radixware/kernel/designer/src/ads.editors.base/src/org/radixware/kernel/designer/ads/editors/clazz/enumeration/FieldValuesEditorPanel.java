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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;


final class FieldValuesEditorPanel extends FilteredTablePanel {

    private AdsEnumClassFieldDef field;

    public FieldValuesEditorPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    protected AdvanceTable<?> createTable() {
        return new AdsEnumClassFieldValuesTable();
    }

    public void open(AdsEnumClassFieldDef field) {
        this.field = field;

        update();
    }

    public void update() {
        getValuesTable().setModel(new AdsEnumClassFieldValuesModel(field.getOwnerEnumClass(), field));

        setSorter();
    }
}
