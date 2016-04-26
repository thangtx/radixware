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

package org.radixware.kernel.designer.common.general.refactoring;

import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;

/**
 * Refactoring provider interface
 * Add an instance of this interface implementation
 * to lookup of {@linkplain RadixObjectNode} to get menu item "Refactor"
 * with popup containing actions listed in result
 * of method {@linkplain IRefactoringProvider#allowedActions() }
 */
public abstract class RefactoringProvider implements Node.Cookie {

    protected final RadixObject context;

    protected RefactoringProvider(RadixObject context) {
        this.context = context;
    }

    public RadixObject getRadixObject() {
        return context;
    }

    /**
     * List refactoring actions allowed for current context
     */
    public abstract Class<? extends SystemAction>[] allowedActions();
}
