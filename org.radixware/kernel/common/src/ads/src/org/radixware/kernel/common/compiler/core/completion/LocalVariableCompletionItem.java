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
package org.radixware.kernel.common.compiler.core.completion;

import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.resources.icons.RadixIcon;

import org.radixware.kernel.common.scml.Scml;

public class LocalVariableCompletionItem extends AbstractCompletionItem {

    public LocalVariableCompletionItem(char[] token, String typeName, int relevance, int replaceStart, int replaceEnd) {
        super(relevance + 5, replaceStart, replaceEnd);
        this.sortText = this.leadText = String.valueOf(token);
        this.tailText = typeName;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.UNKNOWN;
    }

    @Override
    public Scml.Item[] getNewItems() {
        return new Scml.Item[]{
            Scml.Text.Factory.newInstance(this.sortText)
        };
    }
}