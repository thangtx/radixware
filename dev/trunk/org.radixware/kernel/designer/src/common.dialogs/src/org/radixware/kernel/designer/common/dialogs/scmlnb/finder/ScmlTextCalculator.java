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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.scml.Scml;


public class ScmlTextCalculator {

    private final TagTextFactory tagTextFactory;

    public ScmlTextCalculator(final TagTextFactory tagTextFactory) {
        this.tagTextFactory = tagTextFactory;
    }

    public ScmlTextInfo calculate(final Scml scml) {
        final StringBuilder textSb = new StringBuilder();
        final List<Integer> scmlOffsets = new LinkedList<Integer>();
        int currentOffset = 0;
        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Text) {
                final String itemText = ((Scml.Text) item).getText();
                textSb.append(itemText);
                for (int i = 0; i < itemText.length(); i++) {
                    scmlOffsets.add(currentOffset++);
                }
            } else if (item instanceof Scml.Tag) {
                final String tagText = tagTextFactory.getText((Scml.Tag) item);
                textSb.append(tagText);
                for (int i = 0; i < tagText.length(); i++) {
                    scmlOffsets.add(currentOffset);
                }
                currentOffset++;
            } else {
                throw new IllegalStateException("Unknown item type");
            }
        }

        int idx = 0;
        int[] offsetsArray = new int[scmlOffsets.size()];
        for (Integer i : scmlOffsets) {
            offsetsArray[idx++] = i;
        }

        return new ScmlTextInfo(textSb.toString(), offsetsArray);
    }
}
