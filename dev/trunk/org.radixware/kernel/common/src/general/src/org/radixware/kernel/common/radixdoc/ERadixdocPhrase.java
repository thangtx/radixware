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

package org.radixware.kernel.common.radixdoc;


public enum ERadixdocPhrase {

    MODULE("module"),

    DEPENDENCIES("Dependencies"),
    DEPENDENCIES_SUMMARY("Dependencies summary"),
    DEPENDENCIES_FROM("Dependencies from"),

    DEFINITIONS("Definitions"),
    DEFINITIONS_SUMMARY("Definitions summary"),
    DEFINITIONS_FROM("Definitions from"),

    LAYER("Layer"),
    ATTRIBUTES("Attributes"),
    TEST_MODULE("Test module"),
    DESCRIPTION("Description"),
    NAME("Name");

    private final String key;

    private ERadixdocPhrase(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
