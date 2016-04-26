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

package org.radixware.kernel.common.defs.localization;

import java.util.Collection;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EMultilingualStringKind;


public interface ILocalizedDef {

    public abstract class MultilingualStringInfo {

        private final ILocalizedDef owner;

        protected MultilingualStringInfo(ILocalizedDef owner) {
            this.owner = owner;
        }

        public abstract String getContextDescription();

        public IMultilingualStringDef findString() {
            return owner.findLocalizedString(getId());
        }

        public boolean setString(IMultilingualStringDef string) {
            if (string != null) {
                updateId(string.getId());
                return true;
            } else {
                return false;
            }
        }

        public ILocalizedDef getOwner() {
            return owner;
        }

        public boolean canChangeId(){
            return true;
        }
        
        public abstract Id getId();

        public abstract EAccess getAccess();

        public abstract void updateId(Id newId);

        public abstract boolean isPublished();

        public void setId(Id id) {
            updateId(id);
        }

        public boolean isInComment() {
            return false;
        }
        
        public EMultilingualStringKind getKind(){
            return EMultilingualStringKind.TITLE;
        }
    }

    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids);

    public IMultilingualStringDef findLocalizedString(Id stringId);
}
