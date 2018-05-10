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
package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import java.util.Date;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETaskTagBehavior;
import org.radixware.kernel.common.enums.ETaskTagPriority;
import org.radixware.kernel.common.enums.ETaskTagType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.ITaskTag;
import org.radixware.kernel.common.scml.TaskTagUtils;

public class JmlTagTask extends Jml.Tag implements ITaskTag {

    public static class Factory {

        public static JmlTagTask newNotes(String notes) {
            JmlTagTask task = new JmlTagTask(ETaskTagType.NOTE);
            task.notes = notes;
            return task;
        }

        public static JmlTagTask newToDo(String toDo) {
            JmlTagTask task = new JmlTagTask(ETaskTagType.TODO);
            task.notes = toDo;
            return task;
        }

        public static JmlTagTask newFixMe(String problem) {
            JmlTagTask task = new JmlTagTask(ETaskTagType.FIXME);
            task.notes = problem;
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

    private JmlTagTask(ETaskTagType type) {
        super(null);
        this.type = type;
        this.behavior = ETaskTagBehavior.DO_NOTHING;
        this.priority = ETaskTagPriority.LOW;
        this.createDate = new Date(System.currentTimeMillis());
        this.assigne = System.getProperty("user.name");
    }

    @Override
    public void setBehavior(ETaskTagBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public void setAssigne(String assigne) {
        this.assigne = assigne;
    }

    @Override
    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    @Override
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setPriority(ETaskTagPriority priority) {
        this.priority = priority;
    }

    @Override
    public void setType(ETaskTagType type) {
        this.type = type;
    }

    JmlTagTask(org.radixware.schemas.xscml.TaskTagType xTask) {
        super(null);
        TaskTagUtils.loadFrom(this, xTask);
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
    public void appendTo(org.radixware.schemas.xscml.JmlType.Item item) {
        org.radixware.schemas.xscml.TaskTagType xTask = item.addNewTask();
        TaskTagUtils.appendTo(this, xTask);
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

    @Override
    public String getDisplayName() {
        return TaskTagUtils.getDisplayName(this);
    }
    private static final String TRACE_VAR_NAME = "$task_trace_access$";

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new JmlTagWriter(this, purpose, JmlTagTask.this) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        switch (getBehavior()) {
                            case DO_NOTHING:
                                printer.print("/**/");
                                break;
                            case LOG_MESSGAGE:
                                printer.enterBlock();
                                printer.println('{');
                                WriterUtils.writeTraceAccessCode(printer, TRACE_VAR_NAME, getOwnerJml().getOwnerDef(), usagePurpose);
                                final String logMessage = TaskTagUtils.getMessage(JmlTagTask.this);
                                final EEventSeverity severity = TaskTagUtils.getSeverity(JmlTagTask.this);
                                WriterUtils.writeTracePutCode(printer, TRACE_VAR_NAME, EEventSource.TODO, severity, logMessage);
                                printer.leaveBlock();
                                printer.println('}');
                                break;
                            case THROW_EXCEPTION:
                                printer.print(CODE_EXCEPTION_LEAD);
                                final String excMessage = TaskTagUtils.getMessage(JmlTagTask.this);
                                printer.printStringLiteral(excMessage);
                                printer.print(CODE_EXCEPTION_TAIL);
                                break;
                        }
                        WriterUtils.leaveHumanUnreadableBlock(printer);
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }
//    private static final char[] CODE_STR_DEBUG = "DEBUG".toCharArray();
//    private static final char[] CODE_STR_EVENT = "EVENT".toCharArray();
//    private static final char[] CODE_STR_ALARM = "ALARM".toCharArray();
//    private static final char[] CODE_STR_ERROR = "ERROR".toCharArray();
//    private static final char[] CODE_LOGGER_LEAD = "{TRACE_ACCESS.put(EventSeverity.".toCharArray();
//    private static final char[] CODE_LOGGER_TAIL = ");}".toCharArray();
    private static final char[] CODE_EXCEPTION_LEAD = "{throw new UnsupportedOperationException(".toCharArray();
    private static final char[] CODE_EXCEPTION_TAIL = ");}".toCharArray();

//    private char[] getCodeLoggerLead() {
//        return CODE_LOGGER_LEAD;
//    }
//
//    private char[] getCodeLoggerTail() {
//        return CODE_LOGGER_TAIL;
//    }
//    private char[] getCodeExceptionLead() {
//        return CODE_EXCEPTION_LEAD;
//    }
//
//    private char[] getCodeExceptionTail() {
//        return CODE_EXCEPTION_TAIL;
//    }
//    private void printPriority(CodePrinter source) {
//        switch (priority) {
//            case LOW:
//                source.print(CODE_STR_DEBUG);
//                break;
//            case ORDINAL:
//                source.print(CODE_STR_EVENT);
//                break;
//            case HIGH:
//                source.print(CODE_STR_ALARM);
//                break;
//            case CRITICAL:
//                source.print(CODE_STR_ERROR);
//                break;
//            default:
//                source.print(CODE_STR_ERROR);
//        }
//    }
    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
// TODO: move in task window
//        Date today = new Date(System.currentTimeMillis());
//
//        if (getAssigne() == null || getAssigne().isEmpty()) {
//            warning(problemHandler, "Task assignee is not specified");
//        }
//        if (getControlDate() == null) {
//            warning(problemHandler, "Task control date is not specified");
//        } else {
//            if (today.after(getControlDate())) {
//                warning(problemHandler, "Task control date is expired");
//            }
//        }
//        if (getCreateDate() == null) {
//            warning(problemHandler, "Task create date is not specified");
//        }
//        if (getDueDate() == null) {
//            warning(problemHandler, "Task due date is not specified");
//        } else {
//            if (today.after(getDueDate())) {
//                warning(problemHandler, "Task due date is expired");
//            }
//        }
    }
}
