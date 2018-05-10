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

package org.radixware.kernel.designer.dds.script;

import org.radixware.kernel.common.defs.dds.DdsDefinition;


public class DefinitionPair {
    private final DdsDefinition oldDefinition;
    private final DdsDefinition newDefinition;
    private boolean needToDrop = false, needToAlter = false, needToCreate = false;

    public DefinitionPair(DdsDefinition oldDefinition, DdsDefinition newDefinition) {
        if (oldDefinition == null && newDefinition == null) {
            throw new IllegalArgumentException("Both oldDefinition and newDefinition are null");
        }
        else if (oldDefinition != null && newDefinition != null && !(oldDefinition.getClass().isAssignableFrom(newDefinition.getClass()) || newDefinition.getClass().isAssignableFrom(oldDefinition.getClass()))) {
            throw new IllegalArgumentException("Uncompatible types of oldDefinition ["+oldDefinition.getClass()+"] and newDefinition ["+newDefinition.getClass()+"]");
        }
        else {
            this.oldDefinition = oldDefinition;
            this.newDefinition = newDefinition;
            if (oldDefinition == null) {
                this.needToCreate = true;
            } else if (newDefinition == null) {
                this.needToDrop = true;
            } else {
                this.needToAlter = true;
            }
        }
    }

    public DdsDefinition getOldDefinition() {
        return oldDefinition;
    }

    public DdsDefinition getAnyDefinition() {
        if (oldDefinition != null) {
            return oldDefinition;
        } else {
            return newDefinition;
        }
    }

    public DdsDefinition getNewDefinition() {
        return newDefinition;
    }

    public boolean isNeedToAlter() {
        return needToAlter;
    }

    public boolean isNeedToCreate() {
        return needToCreate;
    }

    public boolean isNeedToDrop() {
        return needToDrop;
    }

    public void setNeedToAlter(boolean needToAlter) {
        this.needToAlter = needToAlter;
    }

    public void setNeedToCreate(boolean needToCreate) {
        this.needToCreate = needToCreate;
    }

    public void setNeedToDrop(boolean needToDrop) {
        this.needToDrop = needToDrop;
    }

    @Override
    public String toString() {
        return "DefinitionPair{" + "oldDefinition=" + oldDefinition + ", newDefinition=" + newDefinition + ", needToDrop=" + needToDrop + ", needToAlter=" + needToAlter + ", needToCreate=" + needToCreate + '}';
    }
}
