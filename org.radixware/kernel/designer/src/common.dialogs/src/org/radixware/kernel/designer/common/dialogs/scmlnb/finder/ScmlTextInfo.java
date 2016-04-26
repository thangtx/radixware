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

/**
 * Class which helps to map each position in the given text to scml offset
 */
public class ScmlTextInfo {

    private String text;
    private int[] scmlOffsets;

    public ScmlTextInfo(String text, int[] scmlOffsets) {
        this.text = text;
        this.scmlOffsets = scmlOffsets;
    }

    public int getScmlOffsetAt(int offset) {
        if(offset < 0 || offset >= scmlOffsets.length) {
            throw new IllegalArgumentException("Invalid offset");
        }
        return scmlOffsets[offset];
    }

    public String getText() {
        return text;
    }
}
