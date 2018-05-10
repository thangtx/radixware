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
package org.radixware.kernel.common.dialogs.chooseobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author dlastochkin
 */
public final class SelectedObjectsPanel extends BaseSelectableObjectsPanel {

    private List<SelectableObjectDelegate> objects = null;
    private final String objectsTypeTitle;

    public SelectedObjectsPanel(String objectsTypeTitle) {
        super(null);
        
        this.objects = new ArrayList<>();
        this.objectsTypeTitle = objectsTypeTitle == null ? "Object" : objectsTypeTitle;
        initComponents();
    }

    @Override
    protected void initComponents() {
        JScrollPane objectsTableHolder = initSelectableObjectsTable();                
        JLabel selectedLabel = new JLabel("Selected " + objectsTypeTitle + "s: ");
        
        this.setLayout(new MigLayout("fill", "[grow]", "[shrink][grow]"));
        this.add(selectedLabel, "shrinky, growx, wrap");
        this.add(objectsTableHolder, "span, grow");
    }   
    
}
