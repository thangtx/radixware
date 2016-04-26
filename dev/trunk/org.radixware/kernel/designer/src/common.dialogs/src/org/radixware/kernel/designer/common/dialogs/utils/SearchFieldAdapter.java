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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import javax.swing.*;


public class SearchFieldAdapter {

    public static final String[][] LIST_NAVIGATION_COMMANDS = {
        { "selectPreviousRow", "selectPreviousRow" }, // NOI18N
        { "selectNextRow", "selectNextRow" }, // NOI18N
        { "selectFirstRow", "selectFirstRow" }, // NOI18N
        { "selectLastRow", "selectLastRow" }, // NOI18N
        { "scrollUp", "scrollUp" }, // NOI18N
        { "scrollDown", "scrollDown" }, // NOI18N
    };
    public static final String[][] TREE_NAVIGATION_COMMANDS = {
        { "scrollUpChangeSelection", "scrollUpChangeSelection" },
        { "scrollDownChangeSelection", "scrollDownChangeSelection" },
        { "selectPrevious", "selectPrevious" },
        { "selectNext", "selectNext" },
        { "expand", "expand" },
        { "collapse", "collapse" }
    };

    public static void exchangeCommands(String[][] commandsToExchange,
        final JComponent target,
        final JComponent source) {
        InputMap targetBindings = target.getInputMap();
        KeyStroke[] targetBindingKeys = targetBindings.allKeys();
        ActionMap targetActions = target.getActionMap();
        InputMap sourceBindings = source.getInputMap();
        ActionMap sourceActions = source.getActionMap();
        for (int i = 0; i < commandsToExchange.length; i++) {
            String commandFrom = commandsToExchange[i][0];
            String commandTo = commandsToExchange[i][1];
            final Action orig = targetActions.get(commandTo);
            if (orig == null) {
                continue;
            }
            sourceActions.put(commandTo, new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    orig.actionPerformed(new ActionEvent(target, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
                }
            });
            for (int j = 0; j < targetBindingKeys.length; j++) {
                if (targetBindings.get(targetBindingKeys[j]).equals(commandFrom)) {
                    sourceBindings.put(targetBindingKeys[j], commandTo);
                }
            }
        }
    }

    public static boolean isFitingToken(String search, String token) {
        if (!search.contains("*")
            && !search.contains("?")) {
            if (token.startsWith(search)) {
                return true;
            }
        } else {
            String str = "";
            int i = 0;
            while (i <= search.length() - 1) {
                String sub = search.substring(i, i + 1);
                if (sub.equals("*")) {
                    str += ".*";
                    i++;
                } else {
                    if (sub.equals("?")) {
                        str += ".";
                        i++;
                    } else {
                        int max = Math.max(search.indexOf("*", i), search.indexOf("?", i));
                        if (max != -1) {
                            str += search.substring(i, max);
                            i = max;
                        } else {
                            str += search.substring(i, search.length());
                            i = search.length() + 1;
                        }
                    }
                }
            }
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(str);
            boolean res = pattern.matcher(token).matches();
            return res;
        }
        return false;
    }

    public static String getFirstMatchingToken(String search, String token) {
        if (!search.contains("*")
            && !search.contains("?")) {
            if (token.startsWith(search)) {
                return token.substring(0, search.length());
            }
        } else {
            String str = "";
            int i = 0;
            while (i <= search.length() - 1) {
                String sub = search.substring(i, i + 1);
                if (sub.equals("*")) {
                    str += ".*";
                    i++;
                } else {
                    if (sub.equals("?")) {
                        str += ".";
                        i++;
                    } else {
                        int max = Math.max(search.indexOf("*", i), search.indexOf("?", i));
                        if (max != -1) {
                            str += search.substring(i, max);
                            i = max;
                        } else {
                            str += search.substring(i, search.length());
                            i = search.length() + 1;
                        }
                    }
                }
            }
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(str);
            Matcher matcher = pattern.matcher(token);
            if (matcher.matches()) {
                String result = token.substring(matcher.start(), matcher.end());
                return result;
            }
        }
        return "";
    }
}
