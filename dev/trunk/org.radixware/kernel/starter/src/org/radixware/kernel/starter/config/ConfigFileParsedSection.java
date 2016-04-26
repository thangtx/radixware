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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ConfigFileParsedSection {

    private final String name;
    private final List<ConfigFileParsedEntry> parsedEntries;
    private final int startOffset;
    private final int sectionLineLength;

    public ConfigFileParsedSection(String name, int startOffset, int sectionLineLength, List<ConfigFileParsedEntry> parsedEntries) {
        this.name = name;
        this.sectionLineLength = sectionLineLength;
        this.parsedEntries = Collections.unmodifiableList(new ArrayList<ConfigFileParsedEntry>(parsedEntries));
        this.startOffset = startOffset;
    }

    public String getName() {
        return name;
    }

    public List<ConfigFileParsedEntry> getParsedEntries() {
        return Collections.unmodifiableList(parsedEntries);
    }

    public int getSectionDefinitionLength() {
        return sectionLineLength;
    }

    public int getStartOffset() {
        return startOffset;
    }

    @Override
    public String toString() {
        return "ConfigFileParsedSection{" + "name=" + name + ", startOffset=" + startOffset + ", sectionLineLength=" + sectionLineLength + ", parsedEntries=" + parsedEntries + '}';
    }
}
