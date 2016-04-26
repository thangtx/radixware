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

package org.radixware.kernel.common.scml;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * Scml completion provider interface
 */
public interface ScmlCompletionProvider {

    /**
     * Complietion item descriptor
     * Two important restrictions:
     * <ul>
     * <li>if cursor is currently inside of tag, the will be replaced with action result</li>
     * <li>if cursor is currently inside of text item, only region inside of text item will be replaced</li>
     * </ul>
     */
    public interface CompletionItem {

        public String getSortText();

        public int getRelevance();

        public String getLeadDisplayText();

        public String getTailDisplayText();

        /**
         * Returns string that shoud be removed if any after completion end
         */
        public String getEnclosingSuffix();

        public RadixIcon getIcon();

        /**
         * Returns an array of items to be inserted into scml
         */
        public Scml.Item[] getNewItems();

        /**
         * Returns start of replacement relative offset from caret position
         */
        public int getReplaceStartOffset();

        /**
         * Returns end of replacement relative offset from caret position
         */
        public int getReplaceEndOffset();

        public boolean removePrevious(Scml.Item prevItem);

        public RadixObject getRadixObject();
    }

    public interface CompletionRequestor {

        public void accept(CompletionItem item);

        /**
         * If true, all possible items should be added,
         * otherwise CompletionProvider can add only significant items
         * for current context.
         * @return
         */
        public boolean isAll();        
    }

    /**
     * Completion calculator (nillable)
     */
    public void complete(int offset, CompletionRequestor requestor);
}
