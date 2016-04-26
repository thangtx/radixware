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

package org.radixware.kernel.common.html;


public class CssConstants {

    public enum VerticalAlign {

        MIDDLE("middle"),
        BASELINE("baseline"),
        SUB("sub"),
        SUPER("super"),
        TOP("top"),
        TEXT_TOP("text-top"),
        BOTTOM("bottom"),
        TEXT_BOTTOM("text-bottom"),
        INHERIT("inherit");
        public final String value;

        private VerticalAlign(String value) {
            this.value = value;
        }
    }

    public enum TextAlign {

        CENTER("center"),
        INHERIT("inherit"),
        JUSTIFY("justify"),
        LEFT("left"),
        RIGHT("right");
        public final String value;

        private TextAlign(String value) {
            this.value = value;
        }
    }
}
