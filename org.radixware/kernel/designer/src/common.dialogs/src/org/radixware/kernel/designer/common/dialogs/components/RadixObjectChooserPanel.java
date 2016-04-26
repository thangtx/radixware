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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public class RadixObjectChooserPanel extends javax.swing.JPanel
        implements ChangeListener {

    private IRadixObjectChooserComponent rightComponent;
    private IRadixObjectChooserLeftComponent leftComponent;
    private javax.swing.JButton addButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton upButton;
    private javax.swing.JButton downButton;

    public RadixObjectChooserPanel() {
        addButton = new FixedSizeSquareButton(RadixWareIcons.ARROW.LEFT.getIcon(16, 16));
        removeButton = new FixedSizeSquareButton(RadixWareIcons.ARROW.RIGHT.getIcon(16, 16));

        ActionListener addListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RadixObjectChooserPanel.this.onAddButtonClick();
            }
        };
        addButton.addActionListener(addListener);

        ActionListener removeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RadixObjectChooserPanel.this.onRemoveButtonClick();
            }
        };
        removeButton.addActionListener(removeListener);
    }

    private void onAddButtonClick() {
        if (leftComponent != null
                && rightComponent != null) {
            Object[] selection = rightComponent.getSelectedItems();
            int selectedIndex = rightComponent.getSelectedIndex();

            if (selection.length > 0) {
                leftComponent.addAllItems(selection);
                leftComponent.setSelectedItems(selection);
                rightComponent.removeAll(selection);

                rightComponent.getVisualComponent().requestFocusInWindow();
                if (selectedIndex > -1) {
                    if (selectedIndex < rightComponent.getItemCount()) {
                        rightComponent.setSelectedItem(selectedIndex);
                    } else {
                        rightComponent.setSelectedItem(rightComponent.getItemCount() - 1);
                    }
                }
                updateButtonState();
                changeSupport.fireChange();
            }
        }
    }

    private void onRemoveButtonClick() {
        if (leftComponent != null
                && rightComponent != null) {
            Object[] selection = leftComponent.getSelectedItems();
            int selectedIndex = leftComponent.getSelectedIndex();

            if (selection.length > 0) {
                rightComponent.addAllItems(selection);
                rightComponent.setSelectedItems(selection);
                leftComponent.removeAll(selection);

                leftComponent.getVisualComponent().requestFocusInWindow();
                if (selectedIndex > -1) {
                    if (selectedIndex < leftComponent.getItemCount()) {
                        leftComponent.setSelectedItem(selectedIndex);
                    } else {
                        leftComponent.setSelectedItem(leftComponent.getItemCount() - 1);
                    }
                }
                updateButtonState();
                changeSupport.fireChange();
            }
        }
    }

    public void updateButtonState() {
        if (rightComponent != null
                && leftComponent != null) {
            addButton.setEnabled(!readonly && rightComponent.hasSelection());
            removeButton.setEnabled(!readonly && leftComponent.hasSelection());
            if (upButton != null) {
                upButton.setEnabled(!readonly && leftComponent.getSelectedIndex() > 0);
            }
            if (downButton != null) {
                downButton.setEnabled(!readonly
                        && leftComponent.getSelectedIndex() < leftComponent.getItemCount() - 1
                        && leftComponent.getSelectedIndex() > -1);
            }
        } else {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            if (upButton != null) {
                upButton.setEnabled(false);
            }
            if (downButton != null) {
                downButton.setEnabled(false);
            }
        }
    }

    private void setupUI() {
        boolean leftHasName = leftComponent.getLabelComponent() != null;
        boolean rightHasName = rightComponent.getLabelComponent() != null;

        JComponent left = leftComponent.getVisualComponent();
        JComponent right = rightComponent.getVisualComponent();

        javax.swing.JComponent leftLabel = null;
        javax.swing.JComponent rightLabel = null;

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        if (leftHasName && rightHasName) {
            leftLabel = leftComponent.getLabelComponent();
            rightLabel = rightComponent.getLabelComponent();
            if (!leftComponent.isOrderDependant()) {
                setupWithLabels(layout, leftLabel, left, rightLabel, right);
            } else {
                setupUpDownButtons();
                setupFullUI(layout, leftLabel, left, rightLabel, right);
            }
        } else {
            if (!leftComponent.isOrderDependant()) {
                setupOnlyComponentsView(layout, left, right);
            } else {
                setupUpDownButtons();
                setupOnlyDependent(layout, left, right);
            }
        }
    }
    private final int defaultHeight = 65;

    private void setupOnlyComponentsView(GroupLayout layout, JComponent left, JComponent right) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(removeButton).addComponent(addButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(addButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeButton).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(right, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE).addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE)).addGap(0, 0, 0)));
    }

    private void setupOnlyDependent(GroupLayout layout, JComponent left, JComponent right) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(addButton).addComponent(removeButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE).addGap(0, 0, 0)) /*.addGroup(layout.createSequentialGroup()
                .addComponent(upButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downButton)
                .addContainerGap())*/);
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(addButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE).addComponent(upButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(downButton)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(right, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE).addComponent(left, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(upButton).addComponent(downButton))));
    }

    private void setupWithLabels(GroupLayout layout, javax.swing.JComponent leftLabel, JComponent left,
            javax.swing.JComponent rightLabel, JComponent right) {
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                       .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                                .addGap(0, 0, 0)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                          .addGroup(layout.createSequentialGroup()
//                                                    .addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
//                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                                              .addComponent(removeButton)
//                                                              .addComponent(addButton)))
//                                         .addComponent(leftLabel))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                          .addGroup(layout.createSequentialGroup()
//                                                    .addComponent(rightLabel)
//                                                    .addContainerGap())
//                                          .addComponent(right, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))));
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                       .addGroup(layout.createSequentialGroup()
//                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                          .addComponent(rightLabel)
//                                          .addComponent(leftLabel))
//                                 .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                           .addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE)
//                                           .addGroup(layout.createSequentialGroup()
//                                                     .addComponent(addButton)
//                                                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                     .addComponent(removeButton))
//                                           .addComponent(left, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE))
//                                 .addGap(0, 0, 0)));
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup() //.addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(leftLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE).addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(addButton).addComponent(removeButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(rightLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE).addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)) /*.addContainerGap()*/));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup() //.addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(leftLabel).addComponent(rightLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(addButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeButton)).addComponent(left).addComponent(right)) /*.addContainerGap()*/));

    }

    private void setupFullUI(GroupLayout layout, javax.swing.JComponent leftLabel, JComponent left,
            javax.swing.JComponent rightLabel, JComponent right) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING) //                                .addGroup(layout.createSequentialGroup()
                //                                          .addComponent(upButton)
                //                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                //                                          .addComponent(downButton))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup().addComponent(left, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(removeButton).addComponent(addButton).addComponent(upButton).addComponent(downButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(rightLabel).addComponent(right, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)))).addGroup(layout.createSequentialGroup().addComponent(leftLabel).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(rightLabel).addComponent(leftLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(right, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(addButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE).addComponent(upButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(downButton)).addComponent(left, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, defaultHeight, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) //                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                //                                                                       .addComponent(upButton)
                //                                                                       .addComponent(downButton))
                ));

    }

    public void open(final IRadixObjectChooserLeftComponent leftComponent, final IRadixObjectChooserComponent rightComponent) {
        removeAll();

        if (leftComponent != null
                && rightComponent != null) {
            this.leftComponent = leftComponent;
            this.rightComponent = rightComponent;
            leftComponent.addSelectionEventListener(this);
            rightComponent.addSelectionEventListener(this);

            DoublieClickChooserSupport leftSupport = leftComponent.getDoubleClickSupport();
            if (leftSupport != null) {
                leftSupport.addEventListener(new DoubleClickChooserListener() {

                    @Override
                    public void onEvent(DoubleClickChooserEvent e) {
                        int selected = leftComponent.getSelectedIndex();
                        if (e.clickedItem != -1
                                && e.clickedItem == selected) {
                            RadixObjectChooserPanel.this.onRemoveButtonClick();
                        }
                    }
                });

            }
            DoublieClickChooserSupport rightSupport = rightComponent.getDoubleClickSupport();
            if (rightSupport != null) {
                rightSupport.addEventListener(new DoubleClickChooserListener() {

                    @Override
                    public void onEvent(DoubleClickChooserEvent e) {
                        int selected = rightComponent.getSelectedIndex();
                        if (e.clickedItem != -1
                                && e.clickedItem == selected) { //temporary ??
                            RadixObjectChooserPanel.this.onAddButtonClick();
                        }
                    }
                });

            }

            setupUI();
            updateButtonState();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        updateButtonState();
        if (leftComponent != null) {
            leftComponent.setReadonly(readonly);
        }
        if (rightComponent != null) {
            rightComponent.setReadonly(readonly);
        }
    }
    private boolean readonly = false;

    @Override
    public void stateChanged(ChangeEvent e) {
        updateButtonState();
    }

    public void update() {
        if (leftComponent != null) {
            leftComponent.updateContent();
        }
        if (rightComponent != null) {
            rightComponent.updateContent();
        }
        updateButtonState();
    }

    private void setupUpDownButtons() {
        upButton = new FixedSizeSquareButton(RadixWareIcons.ARROW.MOVE_UP.getIcon(16, 16));
        ActionListener upListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RadixObjectChooserPanel.this.leftComponent.moveUp();
                changeSupport.fireChange();
            }
        };
        upButton.addActionListener(upListener);

        downButton = new FixedSizeSquareButton(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(16, 16));
        ActionListener downListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RadixObjectChooserPanel.this.leftComponent.moveDown();
                changeSupport.fireChange();
            }
        };
        downButton.addActionListener(downListener);
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    //double-clicking support for list-based components
    public static class DoubleClickChooserEvent extends RadixEvent {

        public int clickedItem = -1;

        public DoubleClickChooserEvent(int index) {
            this.clickedItem = index;
        }
    }

    public interface DoubleClickChooserListener extends IRadixEventListener<DoubleClickChooserEvent> {
    }

    public static class DoublieClickChooserSupport extends RadixEventSource<DoubleClickChooserListener, DoubleClickChooserEvent> {

        private javax.swing.event.EventListenerList listeners = new javax.swing.event.EventListenerList();

        @Override
        public synchronized void addEventListener(DoubleClickChooserListener listener) {
            listeners.add(DoubleClickChooserListener.class, listener);
        }

        @Override
        public synchronized void removeEventListener(DoubleClickChooserListener listener) {
            listeners.remove(DoubleClickChooserListener.class, listener);
        }

        @Override
        public void fireEvent(DoubleClickChooserEvent event) {
            Object[] l = listeners.getListeners(DoubleClickChooserListener.class);
            for (int i = 0; i <= l.length - 1; i++) {
                if (event != null) {
                    ((DoubleClickChooserListener) l[i]).onEvent(event);
                }
            }
        }
    }
}
