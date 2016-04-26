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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.resources.RadixWareIcons;


final class LinkPanel extends JPanel {

    private final ExtendableTextField linkEditor = new ExtendableTextField();
    private JButton btnClear;
    private Link presentationLink;

    public LinkPanel() {
        setupUI();
    }

    private void open(Link presentationLink) {
        this.presentationLink = presentationLink;

        if (presentationLink == null) {
            linkEditor.setEnabled(false);
            btnClear.setVisible(false);
            linkEditor.setValue("<Not Defined>");
            return;
        }

        if (!presentationLink.getPresenter().isChangeable()) {
            linkEditor.setEnabled(false);
            btnClear.setVisible(false);
        } else {
            linkEditor.setEnabled(true);
            btnClear.setVisible(presentationLink.getLinkPresenter() != null);
        }
        updateEditorValue();
        linkEditor.requestFocusInWindow();
    }

    public void openForEdit(Link presentationLink) {
        open(presentationLink);
    }

    public void openForRender(Link presentationLink) {
        open(presentationLink);
    }

    private void updateEditorValue() {
        ClassMemberPresenter presenter = presentationLink.getLinkPresenter();

        if (presenter == null) {
            presenter = presentationLink.getPresenter();
        }

        if (presenter != null) {
            linkEditor.setValue(presenter.getName());
        } else {
            linkEditor.setValue("<Not Defined>");
        }
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        final JButton btnChoose = linkEditor.addButton();
        btnChoose.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        btnChoose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                assert presentationLink != null;

                if (presentationLink != null) {

                    final ClassMemberPresenter presenter = presentationLink.getPresenter();
                    final PublishTableModel currentModel = presentationLink.getTableModel();
                    final ClassMemberPresenter linkPresenter = presentationLink.getLinkPresenter();

                    final ClassMemberPresenter replacement = PublishedClassSynchronizer.chooseMemberForReplace(currentModel.getNotPublished(), presenter);

                    if (replacement != null) {
                        if (linkPresenter != null) {
                            linkPresenter.setState(null);
                        }

                        replacement.setState(EPresenterState.REPLACE);
                        presenter.setState(EPresenterState.REPLACE);

                        presentationLink.setLinkPresenter(replacement);
                        replacement.addChangeListener(presentationLink);
                    }
                }
            }
        });

        btnClear = linkEditor.addButton();
        btnClear.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(13, 13));
        btnClear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                assert presentationLink != null;

                if (presentationLink != null) {
                    presentationLink.clearLink();
                    btnClear.setVisible(false);
                }
            }
        });

        linkEditor.setEditable(false);

        add(linkEditor, BorderLayout.CENTER);
    }

    public Link getLink() {
        return presentationLink;
    }
}