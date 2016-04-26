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

package org.radixware.kernel.designer.common.dialogs.results;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openide.actions.CopyAction;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesAction;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;


class ResultsPopupMenu extends JPopupMenu {

    public ResultsPopupMenu(final ResultsTree tree) {
        // ===
        final JMenuItem goToObject = new JMenuItem("Go To Object", RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
        goToObject.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tree.goToLastSelectedObject();
            }
        });
        goToObject.setEnabled(tree.getLastSelectedNode() != null);
        add(goToObject);

//        final JMenuItem findUsages = new JMenuItem(SystemAction.get(FindUsagesAction.class));
        if (tree.canFindUsagesForSelectedObject()) {
            final JMenuItem findUsages = new JMenuItem(
                    SystemAction.get(FindUsagesAction.class).getName(),
                    SystemAction.get(FindUsagesAction.class).getIcon());
            findUsages.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    RequestProcessor.getDefault().post(new Runnable() {

                        @Override
                        public void run() {
                            tree.findUsagesOfLastSelectedObject();
                        }
                    }, 0);
                }
            });
//            findUsages.setEnabled(tree.getLastSelectedNode() != null);
            add(findUsages);
        }

        // ===
        final JMenuItem copyToClipboard = new JMenuItem("Copy to Clipboard", SystemAction.get(CopyAction.class).getIcon());
        copyToClipboard.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tree.copyToClipboard();
            }
        });
        addSeparator();
        add(copyToClipboard);

        //===
        final String id = extractId(tree);
        if (id != null && !id.isEmpty()) {
            final JMenuItem copyIdToClipboard = new JMenuItem("Copy ID to Clipboard", SystemAction.get(CopyAction.class).getIcon());
            copyIdToClipboard.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ClipboardUtils.copyToClipboard(id);
                }
            });
            add(copyIdToClipboard);
        }

        Action[] customs = tree.getCustomActions();
        if (customs != null && customs.length > 0) {
            addSeparator();
            for (int i = 0; i < customs.length; i++) {
                if (customs[i] == null) {
                    addSeparator();
                } else {
                    add(customs[i]);
                }
            }
        }
    }
    private static Pattern ID_PATTERN = Pattern.compile(".*#([a-zA-Z0-9]+).*");

    private static String extractId(ResultsTree tree) {
        final Object userObject = tree.getLastSelectedUserObject();
        if (userObject instanceof RadixProblem) {
            final RadixProblem radixProblem = (RadixProblem) userObject;
            final String message = radixProblem.getMessage();
            final Matcher matcher = ID_PATTERN.matcher(message);
            if (matcher.find() && matcher.groupCount() == 1) {
                final String id = matcher.group(1);
                return id;
            }
        }
        return null;
    }
}
