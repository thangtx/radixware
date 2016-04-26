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

package org.radixware.kernel.common.sqml.tags;

import java.text.MessageFormat;
import java.util.Date;
import org.radixware.kernel.common.enums.ETaskTagBehavior;
import org.radixware.kernel.common.enums.ETaskTagPriority;
import org.radixware.kernel.common.enums.ETaskTagType;
import org.radixware.kernel.common.scml.ITaskTag;
import org.radixware.kernel.common.scml.TaskTagUtils;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;


public class TaskTag extends Sqml.Tag implements ITaskTag {

    public static class Factory {

        public static TaskTag newNotes(String notes) {
            TaskTag task = new TaskTag(ETaskTagType.NOTE);
            task.notes = notes;
            return task;
        }

        public static TaskTag newToDo(String toDo) {
            TaskTag task = new TaskTag(ETaskTagType.TODO);
            task.notes = toDo;
            return task;
        }

        public static TaskTag newFixMe(String problem) {
            TaskTag task = new TaskTag(ETaskTagType.FIXME);
            task.notes = problem;
            return task;
        }

        public static TaskTag loadFrom(org.radixware.schemas.xscml.TaskTagType xTask) {
            TaskTag task = new TaskTag(xTask);
            return task;
        }
    }
    private String assigne;
    private Date controlDate = null;
    private Date createDate = null;
    private Date dueDate = null;
    private ETaskTagType type;
    private ETaskTagPriority priority;
    private ETaskTagBehavior behavior;
    private String notes;

    private TaskTag(ETaskTagType type) {
        this.type = type;
        this.behavior = ETaskTagBehavior.DO_NOTHING;
        this.priority = ETaskTagPriority.LOW;
        this.createDate = new Date(System.currentTimeMillis());
    }

    private TaskTag(org.radixware.schemas.xscml.TaskTagType xTask) {
        TaskTagUtils.loadFrom(this, xTask);
    }

    @Override
    public void setBehavior(ETaskTagBehavior behavior) {
        if (!Utils.equals(this.behavior, behavior)) {
            this.behavior = behavior;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setAssigne(String assigne) {
        if (!Utils.equals(this.assigne, assigne)) {
            this.assigne = assigne;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setControlDate(Date controlDate) {
        if (!Utils.equals(this.controlDate, controlDate)) {
            this.controlDate = controlDate;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setCreateDate(Date createDate) {
        if (!Utils.equals(this.createDate, createDate)) {
            this.createDate = createDate;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setDueDate(Date dueDate) {
        if (!Utils.equals(this.dueDate, dueDate)) {
            this.dueDate = dueDate;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setNotes(String notes) {
        if (!Utils.equals(this.notes, notes)) {
            this.notes = notes;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setPriority(ETaskTagPriority priority) {
        if (!Utils.equals(this.priority, priority)) {
            this.priority = priority;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void setType(ETaskTagType type) {
        if (!Utils.equals(this.type, type)) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public String getAssigne() {
        return assigne;
    }

    @Override
    public ETaskTagBehavior getBehavior() {
        return behavior;
    }

    @Override
    public Date getControlDate() {
        return controlDate;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public ETaskTagPriority getPriority() {
        return priority;
    }

    @Override
    public ETaskTagType getType() {
        return type;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag task={0}]", notes);
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        TaskTagUtils.appendAdditionalToolTip(this, sb);
    }

    @Override
    public String getTypeTitle() {
        return "Task";
    }

    @Override
    public String getTypesTitle() {
        return "Tasks";
    }
}
