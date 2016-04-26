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


public class DefaultMeta {

    private DefaultMeta() {
    }

    public static final class List extends DefaultMeta {

        private List() {
        }
        public static final String HIERARCHY = "hierarchy";
        public static final String SINGLE_LINE = "single-line";
    }

    public static final class Text extends DefaultMeta {

        private Text() {
        }
        public static final String DESCRIPTION = "description";
        public static final String TITLE = "title";
        public static final String PARAGRAPH = "paragraph";
        public static final String CODE = "code";
        public static final String MLSID = "mlsid";
        public static final String LOCALIZED = "localized";
    }

    public static final class Reference extends DefaultMeta {

        private Reference() {
        }
        public static final String ANCHOR = "anchor";
    }

    public static final class Block extends DefaultMeta {

        private Block() {
        }

        public static final String COLLAPSIBLE = "collapsible";
    }

    public static final String CONTENT = "content";
}
