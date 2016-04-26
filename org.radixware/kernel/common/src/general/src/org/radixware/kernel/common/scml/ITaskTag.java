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

package org.radixware.kernel.common.scml;

import java.util.Date;
import org.radixware.kernel.common.enums.ETaskTagBehavior;
import org.radixware.kernel.common.enums.ETaskTagPriority;
import org.radixware.kernel.common.enums.ETaskTagType;


public interface ITaskTag {

    public ETaskTagType getType();

    public void setType(ETaskTagType type);

    public ETaskTagBehavior getBehavior();

    public void setBehavior(ETaskTagBehavior behavior);

    public String getAssigne();

    public void setAssigne(String assigne);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getControlDate();

    public void setControlDate(Date controlDate);

    public Date getDueDate();

    public void setDueDate(Date dueDate);

    public String getNotes();

    public void setNotes(String notes);

    public ETaskTagPriority getPriority();

    public void setPriority(ETaskTagPriority priority);
}
