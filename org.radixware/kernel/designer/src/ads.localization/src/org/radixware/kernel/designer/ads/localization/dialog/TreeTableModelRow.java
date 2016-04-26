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

package org.radixware.kernel.designer.ads.localization.dialog;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public  class TreeTableModelRow {
        private final RadixObject object;
        private boolean isChecked; 


        public TreeTableModelRow(final RadixObject object,final boolean isChecked) {
            this.object=object;
            this.isChecked=isChecked;
        }

        public Boolean isCheched() {
            return isChecked;
        }

        public void setChecked(final boolean isChecked) {
            this.isChecked=isChecked;
        }

        public  RadixObject getValue() {
            return object;
        }

        public RadixIcon getIcon() {
            return object.getIcon();
        }

        public String getName() {
            return object.getName();
        }

        public String getQualifiedName() {
            return object.getQualifiedName();
        }
}
