/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance.aadc;

/**
 *
 * @author dsafonov
 */
public class AadcMemberBinding {

    private final int aadcMemberId;
    private final boolean confirmed;
    private final boolean changedBeforeExpiration;

    public AadcMemberBinding(int aadcMemberId, boolean confirmed, boolean changedBeforeExpiration) {
        this.aadcMemberId = aadcMemberId;
        this.confirmed = confirmed;
        this.changedBeforeExpiration = changedBeforeExpiration;
    }

    public int getAadcMemberId() {
        return aadcMemberId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public boolean isChangedBeforeExpiration() {
        return changedBeforeExpiration;
    }

    @Override
    public String toString() {
        return "AadcMemberBinding{" + "aadcMemberId=" + aadcMemberId + ", confirmed=" + confirmed + '}';
    }

}
