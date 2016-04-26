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

package org.radixware.kernel.designer.common.dialogs.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.nodes.Node;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;


public class NameFilter extends AbstractFiter {

    private final transient JTextField textField = new JTextField();
    private final transient JPanel filterPanel = new JPanel();

    public NameFilter() {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                notifyListeners();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                notifyListeners();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                //do nothing
            }
        });

        filterPanel.setLayout(new MigLayout("fill", "", ""));
        filterPanel.add(new JLabel(" Search: "));
        filterPanel.add(textField, "push, grow");

    }

    @Override
    public IAcceptor<Node> getNodeAcceptor() {
        final String regExpr = transformWildCardsToJavaStyle(textField.getText()) + ".*";

        return new IAcceptor<Node>() {
            private final Pattern namePattern;

            {
                Pattern pattern = null;
                try {
                    pattern = Pattern.compile(regExpr, Pattern.CASE_INSENSITIVE);
                } catch (PatternSyntaxException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }

                namePattern = pattern;
            }

            @Override
            public boolean accept(final Node candidate) {
                if (namePattern == null) {
                    return false;
                }
                return namePattern.matcher(candidate.getName()).matches();
            }
        };
    }

    @Override
    public Component getComponent() {
        return filterPanel;
    }

    @Override
    public Object getComponentPosition() {
        return BorderLayout.NORTH;
    }

    private static String transformWildCardsToJavaStyle(String text) {
        final StringBuilder regexBuilder = new StringBuilder(""); // NOI18N
        int lastWildCardPosition = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '?') { // NOI18N
                regexBuilder.append(text.substring(lastWildCardPosition, i));
                regexBuilder.append('.'); // NOI18N
                lastWildCardPosition = i + 1;
            } else if (text.charAt(i) == '*') { // NOI18N
                regexBuilder.append(text.substring(lastWildCardPosition, i));
                regexBuilder.append(".*"); // NOI18N
                lastWildCardPosition = i + 1;
            }
        }
        regexBuilder.append(text.substring(lastWildCardPosition, text.length()));
        return regexBuilder.toString();
    }

    protected String getSearchPattern() {
        return textField.getText();
    }
}
