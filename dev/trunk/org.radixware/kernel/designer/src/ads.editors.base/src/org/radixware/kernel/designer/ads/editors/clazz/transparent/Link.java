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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.openide.util.ChangeSupport;


final class Link implements ChangeListener {

    static class CorrectLinkEditor extends AbstractCellEditor implements TableCellEditor {

        private final LinkPanel editorPanel = new LinkPanel();

        public CorrectLinkEditor() {
        }

        @Override
        public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, final int row, final int column) {
            if (value instanceof Link) {
                final Link link = (Link) value;
                editorPanel.openForEdit(link);
                link.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        CorrectLinkEditor.this.stopCellEditing();
                        table.repaint();
                        link.removeChangeListener(this);
                    }
                });
            }
            return editorPanel;
        }

        @Override
        public Object getCellEditorValue() {
            return editorPanel.getLink();
        }
    }

    static class CorrectLinkRenderer implements TableCellRenderer {

        private final LinkPanel renderPanel = new LinkPanel();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (value instanceof Link || value == null) {
                renderPanel.openForRender((Link) value);
            }
            return renderPanel;
        }
    }

    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final PublishTableModel model;
    private final ClassMemberPresenter presenter;
    private ClassMemberPresenter linkPresenter;

    public Link(ClassMemberPresenter presenter, PublishTableModel currentModel) {
        super();

        this.model = currentModel;
        this.presenter = presenter;
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        if (e.getSource() == linkPresenter) {
            if (linkPresenter.getState() == null) {
                linkPresenter.removeChangeListener(this);
                clearLink();
            }
        }
    }

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    private void fireChange() {
        model.fireTableDataChanged();
        changeSupport.fireChange();
    }

    PublishTableModel getTableModel() {
        return model;
    }

    public void clearLink() {
        presenter.setState(null);
        linkPresenter.setState(null);
        linkPresenter = null;
        fireChange();
    }

    public void setLinkPresenter(ClassMemberPresenter linkPresenter) {
        this.linkPresenter = linkPresenter;
        fireChange();
    }

    public ClassMemberPresenter getLinkPresenter() {
        return linkPresenter;
    }

    public ClassMemberPresenter getPresenter() {
        return presenter;
    }
}