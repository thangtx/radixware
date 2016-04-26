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

import java.util.Collection;
import javax.swing.AbstractAction;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Allows to search Netbeans actions
 */
public class NetbeansActions {

    private NetbeansActions() {
    }

    private static AbstractAction getAction(final String lookupPath, final String classSimpleName) {
        final Lookup lookup = Lookups.forPath(lookupPath);
        final Collection<? extends AbstractAction> actions = lookup.lookupAll(AbstractAction.class);
        for (AbstractAction action : actions) {
            if (action.getClass().getSimpleName().equals(classSimpleName)) {
                return action;
            }
        }
        throw new IllegalStateException("Action not found: '" + lookupPath + "/" + classSimpleName + "'");
    }

    public static AbstractAction getGoToPrevDocumentAction() {
        return getAction("Actions/Window", "RecentViewListAction");
    }

    public static AbstractAction getSubversionAction(String classSimpleName) {
        return getAction("Actions/Subversion", classSimpleName);
    }
}
