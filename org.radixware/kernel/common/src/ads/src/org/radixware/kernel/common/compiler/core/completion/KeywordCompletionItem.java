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


public class KeywordCompletionItem extends AbstractCompletionItem {

    char[] keyword;

    public KeywordCompletionItem(char[] keyword, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.keyword = keyword;
        this.leadText = String.valueOf(keyword);
        this.sortText = this.leadText;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.UNKNOWN;
    }

    @Override
    public Scml.Item[] getNewItems() {
        return new Scml.Item[]{
            Scml.Text.Factory.newInstance(String.valueOf(keyword))
        };
    }
}
