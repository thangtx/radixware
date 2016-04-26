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
package org.radixware.kernel.designer.ads.editors.common;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.designer.common.dialogs.components.RadixObjectsListModel;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.components.IRadixObjectChooserLeftComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserCommonComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel;

class EditorPresentationsList extends RadixObjectChooserCommonComponent
        implements IRadixObjectChooserLeftComponent {

    public EditorPresentationsList() {
        super();
        list.setCellRenderer(new AbstractItemRenderer(list) {

            @Override
            public String getObjectName(Object object) {
                if (object != null && allEditors != null) {
                    Id asId = (Id) object;
                    Definition def = allEditors.findById(asId, ExtendableDefinitions.EScope.ALL).get();
                    if (def != null) {
                        return def.getQualifiedName();
                    } else {
                        return asId.toString();
                    }
                }
                return "<Not Defined>";
            }

            @Override
            public String getObjectLocation(Object object) {
                return "";
            }

            @Override
            public RadixIcon getObjectIcon(Object object) {
                if (object != null) {
                    Id asId = (Id) object;
                    Definition def = allEditors.findById(asId, ExtendableDefinitions.EScope.ALL).get();
                    if (def != null) {
                        return def.getIcon();
                    }
                }
                return RadixObjectIcon.UNKNOWN;
            }

            @Override
            public RadixIcon getObjectLocationIcon(Object object) {
                return null;
            }
        });

        final String deleteAction = "delete";

        list.getActionMap().put(deleteAction, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RadixObject rdx = getSelectedRadixObject();
                if (rdx != null) {
                    getDoubleClickSupport().fireEvent(new RadixObjectChooserPanel.DoubleClickChooserEvent(getSelectedIndex()));
                } else {
                    Object[] selection = getSelectedItems();
                    if (selection != null && selection.length > 0) {
                        if (selection[0] instanceof Id) {
                            if (isLeft) {
                                presentation.remove((Id) selection[0]);
                                updateContent();
                            }
                        }
                    }
                }
            }
        });

        KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(deleteKey, null);
        list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(deleteKey, deleteAction);
    }
    private PresentationListCfgPanel.PresentationsListAdapter presentation;
    private ExtendableDefinitions<AdsEditorPresentationDef> allEditors;
    private boolean isLeft = false;

    public void open(final PresentationListCfgPanel.PresentationsListAdapter presentation, boolean isLeft) {
        this.presentation = presentation;
        this.isLeft = isLeft;
        update();
    }

    public void update() {
        setReadonly(isReadonly() || presentation.isReadOnly());
        updateContent();
    }

    @Override
    public void updateContent() {
        allEditors = presentation.availablePresentations();
        if (allEditors == null) {
            list.setModel(new AbstractListModel() {

                @Override
                public int getSize() {
                    return 0;
                }

                @Override
                public Object getElementAt(int index) {
                    return null;
                }
            });
            return;
        }
        List<Id> editors = presentation.currentlySelectedIds();

        if (isLeft) {
            EditorPresentationsListModel dm = new EditorPresentationsListModel(presentation);
            list.setModel(dm);
        } else {
            RadixObjectsListModel dm = new RadixObjectsListModel(allEditors);
            for (AdsEditorPresentationDef p : allEditors.get(ExtendableDefinitions.EScope.ALL)) {
                if (!editors.contains(p.getId())) {
                    dm.addElement(p);
                }
            }
            list.setModel(dm);
        }
    }

    @Override
    public boolean isPopupMenuAvailable() {
        Object[] selection = getSelectedItems();
        if (selection != null && selection.length > 0) {
            if (selection[0] instanceof Definition) {
                return true;
            } else if (selection[0] instanceof Id) {
                Definition def = allEditors.findById((Id) selection[0], ExtendableDefinitions.EScope.ALL).get();
                return def != null;
            }
        }
        return false;
    }

    @Override
    public RadixObject getSelectedRadixObject() {
        Object[] selection = getSelectedItems();
        if (selection != null && selection.length > 0) {
            if (selection[0] instanceof Definition) {
                return (Definition) selection[0];
            } else if (selection[0] instanceof Id) {
                Definition def = allEditors.findById((Id) selection[0], ExtendableDefinitions.EScope.ALL).get();
                return def;
            }
        }
        return null;
    }

    @Override
    public void addAllItems(Object[] objects) {
        if (isLeft) {
            EditorPresentationsListModel pm = (EditorPresentationsListModel) list.getModel();
            for (Object r : objects) {
                if (r instanceof Definition) {
                    pm.addElement((Definition) r);
                    presentation.add(((Definition) r).getId());
                } else {
                    pm.addElement((Id) r);
                    presentation.add((Id) r);
                }
            }
            list.setModel(pm);
        } else {
            RadixObjectsListModel rm = (RadixObjectsListModel) list.getModel();
            for (Object r : objects) {
                if (r instanceof Definition) {
                    rm.addElement((Definition) r);
                } else {
                    Definition def = allEditors.findById((Id) r, ExtendableDefinitions.EScope.ALL).get();
                    if (def != null) {
                        rm.addElement((Id) r);
                    }
                }
            }
            list.setModel(rm);
        }

    }

    @Override
    public void removeAll(Object[] objects) {
        if (isLeft) {
            EditorPresentationsListModel pm = (EditorPresentationsListModel) list.getModel();
            for (Object r : objects) {
                if (r instanceof Definition) {
                    pm.removeElement(((Definition) r).getId());
                    presentation.remove(((Definition) r).getId());
                } else {
                    pm.removeElement((Id) r);
                    presentation.remove((Id) r);
                }
            }
            list.setModel(pm);
        } else {
            RadixObjectsListModel rm = (RadixObjectsListModel) list.getModel();
            for (Object r : objects) {
                if (r instanceof Definition) {
                    rm.removeElement(((Definition) r).getId());
                } else {
                    rm.removeElement((Id) r);
                }
            }
            list.setModel(rm);
        }
    }

    @Override
    public JComponent getLabelComponent() {
        if (isLeft) {
            return new javax.swing.JLabel(NbBundle.getMessage(EditorPresentationsList.class, "EditorPresentations-Usable"));
        }
        return new javax.swing.JLabel(NbBundle.getMessage(EditorPresentationsList.class, "EditorPresentations-NotUsable"));
    }

    @Override
    public boolean isOrderDependant() {
        return isLeft;
    }

    @Override
    public void moveDown() {
        if (isLeft) {
            RadixObject selected = getSelectedRadixObject();
            final int index = getSelectedIndex();
            presentation.moveDn(((Definition) selected).getId());
            update();
            list.getSelectionModel().setSelectionInterval(index + 1, index + 1);
        }
    }

    @Override
    public void moveUp() {
        if (isLeft) {
            final int index = getSelectedIndex();
            RadixObject selected = getSelectedRadixObject();
            presentation.moveUp(((Definition) selected).getId());
            update();
            list.getSelectionModel().setSelectionInterval(index - 1, index - 1);
        }
    }
}
