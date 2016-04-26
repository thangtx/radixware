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

package org.radixware.kernel.common.defs.ads;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.adsdef.Size;

public interface IModalDisplayable {

    class ModialViewSizeInfo {

        private int width, height;
        private RadixObject owner;

        public ModialViewSizeInfo(RadixObject owner) {
            this.owner = owner;
        }

        public void setWidth(int w) {
            if (w != width) {
                width = w;
                if (owner != null) {
                    owner.setEditState(RadixObject.EEditState.MODIFIED);
                }
            }
        }

        public void setHeight(int h) {
            if (h != height) {
                height = h;
                if (owner != null) {
                    owner.setEditState(RadixObject.EEditState.MODIFIED);
                }
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean appendTo(Size xSize) {
            if (this.width > 0 || this.height > 0) {
                xSize.setWidth(width > 0 ? width : 0);
                xSize.setHeight(height > 0 ? height : 0);
                return true;
            } else {
                return false;
            }
        }

        public void loadFrom(Size xSize) {
            if (xSize != null) {
                this.width = xSize.getWidth();
                this.height = xSize.getHeight();
                if (this.width < 0) {
                    this.width = 0;
                }
                if (this.height < 0) {
                    this.height = 0;
                }
            }
        }
    }

    ModialViewSizeInfo getModialViewSizeInfo(ERuntimeEnvironmentType env);

    boolean isReadOnly();

    ERuntimeEnvironmentType getClientEnvironment();
}
