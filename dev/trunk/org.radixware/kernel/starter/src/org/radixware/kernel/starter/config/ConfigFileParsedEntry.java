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

package org.radixware.kernel.starter.config;


public class ConfigFileParsedEntry {

    private final int startOffset;
    private final int endOffset;
    private final String key;
    private final String value;

    public ConfigFileParsedEntry(int startOffset, int endOffset, String key, String value) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.key = key;
        this.value = value;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public String getKey() {
        return key;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public String getValue() {
        return value;
    }
}
