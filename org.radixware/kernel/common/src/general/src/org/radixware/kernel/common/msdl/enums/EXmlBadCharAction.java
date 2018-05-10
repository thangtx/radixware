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
package org.radixware.kernel.common.msdl.enums;

public enum EXmlBadCharAction {

    NONE("None", "<Not defined>"),
    REPLACE_WITH_QUESTION_MARK("ReplaceWithQuestionMark", "Replace with question mark"),
    THROW_EXCEPTION("ThrowException", "Throw exception");

    private final String title, value;

    private EXmlBadCharAction(String value, String title) {
        this.value = value;
        this.title = title;
    }

    public static EXmlBadCharAction getInstance(String value) {
        if (value == null) {
            return NONE;
        }
        for (EXmlBadCharAction field : EXmlBadCharAction.values()) {
            if (field.value.equals(value)) {
                return field;
            }
        }
        return NONE;
    }
    
    public String getValue() {
        return this == NONE ? null : value;
    }
    
    @Override
    public String toString() {
        return title;
    }
}
