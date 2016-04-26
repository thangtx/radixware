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

import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;


public abstract class AbstractCompletionItem implements ScmlCompletionProvider.CompletionItem {

    private int relevance;
    protected String sortText;
    protected String leadText;
    protected String tailText;
    protected String enclosingSuffix;
    protected int replaceStart, replaceEnd;

    public AbstractCompletionItem(int relevance, int replaceStart, int replaceEnd) {
        this.relevance = relevance;
        this.replaceStart = replaceStart;
        this.replaceEnd = replaceEnd;
    }

    @Override
    public String getSortText() {
        return sortText;
    }

    @Override
    public int getRelevance() {
        return relevance;
    }

    @Override
    public String getLeadDisplayText() {
        return leadText;
    }

    @Override
    public String getTailDisplayText() {
        return tailText;
    }

    @Override
    public String getEnclosingSuffix() {
        return enclosingSuffix;
    }

    @Override
    public int getReplaceStartOffset() {
        return replaceStart;
    }

    @Override
    public int getReplaceEndOffset() {
        return replaceEnd;
    }

    @Override
    public boolean removePrevious(Scml.Item prevItem) {
        return false;
    }

    @Override
    public RadixObject getRadixObject() {
        return null;
    }
}
