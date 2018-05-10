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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author dlastochkin
 */
public final class SelectableObjectsPanel extends BaseSelectableObjectsPanel {

    private static final String FILTER_LABEL_TEXT = " Name (prefix, camel case: \"AA\" or \"AbcAb\", wildcards: \"?\" \"*\", exact match: end with space):";
    private static final String RESULT_LABEL_TEXT = "s Found:";

    private final String objectsTypeTitle;

    private final JLabel filterLabel = new JLabel();
    private final JTextField filterEditor = new JTextField();
    private final JLabel resultLabel = new JLabel();
    private final JCheckBox caseSensitiveChb = new JCheckBox("Case Sensitive");

    public SelectableObjectsPanel(Collection<ISelectableObject> objects, String objectsTypeTitle) {
        super(objects);
        
        this.objectsTypeTitle = objectsTypeTitle == null ? "Object" : objectsTypeTitle;
        initComponents();
    }

    @Override
    protected void initComponents() {
        filterLabel.setText(objectsTypeTitle + FILTER_LABEL_TEXT);
        resultLabel.setText(objectsTypeTitle + RESULT_LABEL_TEXT);

        JScrollPane objectsTableHolder = initSelectableObjectsTable();

        filterEditor.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                selectableObjectsTable.applyFilter(filterEditor.getText(), caseSensitiveChb.isSelected());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                selectableObjectsTable.applyFilter(filterEditor.getText(), caseSensitiveChb.isSelected());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                selectableObjectsTable.applyFilter(filterEditor.getText(), caseSensitiveChb.isSelected());
            }
        });
        
        selectableObjectsTable.applyFilter(filterEditor.getText(), caseSensitiveChb.isSelected());

        this.setLayout(new MigLayout("fill", "[grow][shrink]", "[shrink][shrink][shrink][grow]"));
        this.add(filterLabel, "span, growx, wrap");
        this.add(filterEditor, "span, growx, wrap");
        this.add(resultLabel, "growx");
        this.add(caseSensitiveChb, "shrinkx, wrap");
        this.add(objectsTableHolder, "span, grow");
    }   

    @Override
    public Collection<ISelectableObject> getAllObjects() {
        List<ISelectableObject> result = new ArrayList<>();
        for (SelectableObjectDelegate object : selectableObjectsTable.getFiltered()) {
            result.add(object.getObject());
        }
        return result;
    }

    @Override
    public void removeAllObjects() {
        this.objects.removeAll(selectableObjectsTable.getFiltered());
    }        
        
}
