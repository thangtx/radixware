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
package org.radixware.kernel.designer.common.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.NetbeansActions;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;

/**
 * Panel that displays Radix object type, name and location. Displayed in top
 * part of non-modal editors.
 *
 */
public class EditorTitlePanel extends javax.swing.JPanel {

    private static final String SHOW_ID_KEY = "rdx.editor.showid";
    //
    private final JPanel toolbar;
    private final JButton btGoToPrevDocument;
    private final JButton btSelectInProjects;
    private JButton btShowSuppresedWarnings;
    private final JLabel lbLocation;
    private final JLabel lbName;
    private final JTextArea tfId;
    private RadixObject radixObject = null;
    private final RadixObject.RenameListener renameListener = new RadixObject.RenameListener() {
        @Override
        public void onEvent(RenameEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    EditorTitlePanel.this.update();
                }
            });

        }
    };
    private final Action copyIdAction = new AbstractAction("Copy ID") {
        @Override
        public void actionPerformed(ActionEvent e) {
            final Definition definition = (Definition) radixObject;
            ClipboardUtils.copyToClipboard(definition.getId().toString());
        }
    };
    private final Action showIdAction = new ShowIdAction();
    private final Action copyNameAction = new AbstractAction("Copy Name") {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClipboardUtils.copyToClipboard(radixObject.getName());
        }
    };
    private final Action copyQualifiedNameAction = new AbstractAction("Copy Qualified Name") {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClipboardUtils.copyToClipboard(radixObject.getQualifiedName());
        }
    };
    private final Action showSuppressedWarningsAction = new AbstractAction("Suppressed Warnings...") {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SuppressedWarningsPanel panel = new SuppressedWarningsPanel();
                    panel.open(radixObject);
                    ModalDisplayer displayer = new ModalDisplayer(panel, "Suppressed Warnings");
                    if (displayer.showModal()) {
                        panel.apply();
                    }
                    updateSuppressedWarningsButton();
                }
            });
        }
    };
    private final Action[] actions = new Action[]{
        copyIdAction,
        showIdAction,
        copyNameAction,
        copyQualifiedNameAction
    };
    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                copyIdAction.setEnabled(radixObject instanceof Definition);
                showIdAction.setEnabled(copyIdAction.isEnabled());
                final JPopupMenu popupMenu = Utilities.actionsToPopup(actions, EditorTitlePanel.this);
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    };

    private JButton newButton() {
        final Dimension buttonSize = new Dimension(22, 20);
        final JButton result = new JButton();
        result.setFocusable(false);
        result.setMaximumSize(buttonSize);
        result.setMinimumSize(buttonSize);
        result.setPreferredSize(buttonSize);
        result.setOpaque(false);
        return result;
    }

    public EditorTitlePanel() {
        toolbar = new JPanel();
        lbName = new javax.swing.JLabel();
        tfId = new JTextArea();
        lbLocation = new javax.swing.JLabel();
        btSelectInProjects = newButton();
        btGoToPrevDocument = newButton();

        Color background = UIManager.getDefaults().getColor("Button.shadow");
        if (background == null) {
            background = Color.darkGray;
        }
        final BoxLayout boxLayout = new BoxLayout(toolbar, BoxLayout.X_AXIS);
        toolbar.setLayout(boxLayout);
        toolbar.setBackground(background);

//        final int MAX_WIDTH = 19;
//        toolbar.setMaximumSize(new Dimension(32767, MAX_WIDTH));
//        toolbar.setMinimumSize(new Dimension(0, MAX_WIDTH));
//        toolbar.setPreferredSize(new Dimension(400, MAX_WIDTH));
        setBackground(background);
//        setMaximumSize(new Dimension(32767, MAX_WIDTH));
//        setMinimumSize(new Dimension(0, MAX_WIDTH));
//        setPreferredSize(new Dimension(400, MAX_WIDTH));

//        lbName.setFont(new Font("Tahoma", Font.BOLD, 11));
        final Border labelBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        lbName.setForeground(Color.WHITE);
        lbName.setBorder(labelBorder);
        lbName.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        tfId.setBackground(new Color(background.getRGB()));//TextArea background should not be instanceof UIResource
        tfId.setForeground(Color.WHITE);
        tfId.setEditable(false);
        tfId.setBorder(labelBorder);
        tfId.addMouseListener(mouseListener);

        lbLocation.setForeground(Color.WHITE);
        lbLocation.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbLocation.setBorder(labelBorder);

        btSelectInProjects.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon(14));
        btSelectInProjects.setToolTipText("Select in Projects");
        btSelectInProjects.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                NodesManager.selectInProjects(radixObject);
            }
        });

        final AbstractAction action = NetbeansActions.getGoToPrevDocumentAction();
        btGoToPrevDocument.setAction(action);
        btGoToPrevDocument.setIcon(RadixWareIcons.ARROW.LEFT.getIcon(14));
        btGoToPrevDocument.setFocusable(false);
        btGoToPrevDocument.setText("");
        btGoToPrevDocument.setToolTipText("Go To Previous Document");

        toolbar.add(btGoToPrevDocument);
        toolbar.add(btSelectInProjects);
        toolbar.add(lbName);
        toolbar.add(tfId);

        this.setLayout(new BorderLayout());
        this.add(toolbar, BorderLayout.CENTER);
        this.add(lbLocation, BorderLayout.EAST);

        lbName.addMouseListener(mouseListener);
        this.addMouseListener(mouseListener);
    }

    public void open(RadixObject radixObject) {
        synchronized (this) {
            if (this.radixObject != null) {
                this.radixObject.removeRenameListener(renameListener);
            }
            this.radixObject = radixObject;
            this.radixObject.addRenameListener(renameListener);
            update();
        }
    }

    public void update() {
        synchronized (this) {
            if (radixObject != null) {
                this.lbName.setText(radixObject.getTypeTitle() + " '" + radixObject.getName() + "'");
                this.lbName.setToolTipText(radixObject.getToolTip());

                final Id id = radixObject instanceof Definition ? ((Definition) radixObject).getId() : null;
                //This should be controlled via designer setting, but it requires investigation on how to do it. Forgive me.
                if (id != null && NbPreferences.root().getBoolean(SHOW_ID_KEY, false)) {
                    tfId.setVisible(true);
                    lbName.setText(lbName.getText() + ",  ID: ");
                    tfId.setText(id.toString());
                } else {
                    tfId.setText("");
                    tfId.setVisible(false);
                }

                final RadixObject ownerForQualifedName = radixObject.getOwnerForQualifedName();
                if (ownerForQualifedName != null) {
                    this.lbLocation.setText(ownerForQualifedName.getQualifiedName());
                    this.lbLocation.setToolTipText(ownerForQualifedName.getToolTip());
                } else {
                    this.lbLocation.setText("");
                }
            } else {
                this.lbName.setText(String.valueOf(radixObject));
                this.lbLocation.setText(String.valueOf(radixObject));
                this.tfId.setText(String.valueOf(radixObject));
            }
            updateSuppressedWarningsButton();
        }
    }

    private void updateSuppressedWarningsButton() {

        WarningsList wl = new WarningsList() {
            @Override
            public void acceptSuppressers(final List<RadixObject> objects) {
                if (objects.isEmpty()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            if (btShowSuppresedWarnings != null) {
                                toolbar.remove(btShowSuppresedWarnings);
                                btShowSuppresedWarnings = null;
                                toolbar.revalidate();
                            }
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (btShowSuppresedWarnings == null) {
                                btShowSuppresedWarnings = newButton();
                                btShowSuppresedWarnings.setAction(showSuppressedWarningsAction);
                                btShowSuppresedWarnings.setIcon(RadixWareIcons.EVENT_LOG.getForSeverity(EEventSeverity.WARNING).getIcon(13, 13));
                                btShowSuppresedWarnings.setText("");
                                btShowSuppresedWarnings.setToolTipText("Suppressed Warnings...");
                                toolbar.add(btShowSuppresedWarnings, 2);
                                toolbar.revalidate();
                            }
                        }
                    });
                }
            }
        };
        wl.explore(radixObject);
    }

    private class ShowIdAction extends AbstractAction implements Presenter.Popup {

        private JCheckBoxMenuItem presenter;

        public ShowIdAction() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NbPreferences.root().putBoolean(SHOW_ID_KEY, presenter.isSelected());
            update();
        }

        @Override
        public JMenuItem getPopupPresenter() {
            if (presenter == null) {
                presenter = new JCheckBoxMenuItem("Show ID", NbPreferences.root().getBoolean(SHOW_ID_KEY, false));
                presenter.addActionListener(this);
            }
            return presenter;
        }
    }
}
