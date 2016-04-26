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

package org.radixware.kernel.common.client.eas;

import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.schemas.eas.SelectMess;
import org.radixware.schemas.eas.SelectRq;


public class SelectRequestHandle extends RequestHandle {

    private boolean searchForCurrentEntity = false;
    private Pid pidToSearch;
    private long startIndex;
    private final int rowCount;

    protected SelectRequestHandle(final GroupModel group, final int startIndex, final int rowCount, final boolean withSelectorAddons) {
        super(group.getEnvironment(), RequestCreator.select(group, startIndex, rowCount, withSelectorAddons, false), SelectMess.class);
        this.rowCount = rowCount;
    }

    public void setStartIndex(final int index) {
        final SelectRq rq = (SelectRq) getRequest();
        rq.setStartIndex(index);
        startIndex = index;
    }

    public Pid getPidToSearch() {
        return pidToSearch;
    }

    public void setPidToSearch(Pid pidToSearch) {
        this.pidToSearch = pidToSearch;
    }

    public boolean isSearchForCurrentEntity() {
        return searchForCurrentEntity;
    }

    public void setSearchForCurrentEntity(boolean searchForCurrentEntity) {
        this.searchForCurrentEntity = searchForCurrentEntity;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public int getRowCount() {
        return rowCount;
    }        
}
