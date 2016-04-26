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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

/**
 * Class representing occurrence of some pattern in scml
 */
public class ScmlOccurrence implements IOccurrence {

    private final ScmlLocation scmlLocation;
    private final ScmlOccurenceInfo occurrenceInfo;
    private final String text;

    public ScmlOccurrence(final ScmlLocation scmlLocation, final ScmlOccurenceInfo occuranceInfo, final String text) {
        this.scmlLocation = scmlLocation;
        this.occurrenceInfo = occuranceInfo;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Occurance of \"" + text + "\"; Info: " + occurrenceInfo;
    }

    @Override
    public String getDisplayText() {
        if (occurrenceInfo != null) {
            StringBuilder boldedStringSb = new StringBuilder();
            boldedStringSb.append("<html>");
            boldedStringSb.append(occurrenceInfo.getContainingString().substring(0, occurrenceInfo.getStartOffset()));
            boldedStringSb.append("<b>");
            boldedStringSb.append(text);
            boldedStringSb.append("</b>");
            if (occurrenceInfo.getEndOffset() < occurrenceInfo.getContainingString().length() - 1) {
                boldedStringSb.append(occurrenceInfo.getContainingString().substring(occurrenceInfo.getEndOffset()));
            }
            boldedStringSb.append("</html>");
            return boldedStringSb.toString();
        } else {
            return toString();
        }
    }

    @Override
    public RadixObject getOwnerObject() {
        return scmlLocation.getScml().getDefinition();
    }

    @Override
    public void goToObject() {
        EditorsManager.getDefault().open(scmlLocation.getScml(), new OpenInfo(scmlLocation.getScml(), Lookups.fixed(scmlLocation)));
    }

    /**
     * Addition information about occurrence.
     * Main purpose - visual representation of occurrence
     */
    public static class ScmlOccurenceInfo {

        private final String containingString;
        private final int startOffset;
        private final int endOffset;

        public ScmlOccurenceInfo(final String containingString, final int startOffset, final int endOffset) {
            this.containingString = containingString;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }

        public String getContainingString() {
            return containingString;
        }

        public int getEndOffset() {
            return endOffset;
        }

        public int getStartOffset() {
            return startOffset;
        }
    }
}
