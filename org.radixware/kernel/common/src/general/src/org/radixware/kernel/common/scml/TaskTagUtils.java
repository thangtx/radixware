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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.EEventSeverity;


public class TaskTagUtils {

    public static String getDisplayName(ITaskTag tag) {
        int maxLen = tag.getNotes().length();
        boolean trim = false;
        if (maxLen > 30) {
            trim = true;
        }
        int index = tag.getNotes().indexOf("\n");
        if (index >= 0 && index < maxLen) {
            maxLen = index;
            trim = true;
        }

        String display = trim ? tag.getNotes().substring(0, maxLen) + "..." : tag.getNotes();
        return ("//:" + tag.getType().name() + ": " + display).trim();
    }

    public static void appendAdditionalToolTip(ITaskTag tag, StringBuilder sb) {
        sb.append("<b>#");
        sb.append(tag.getType().name());
        sb.append(":</b> ");
        sb.append(tag.getNotes());
        sb.append("<hr><br>");
        sb.append("Assignee: ");
        sb.append(tag.getAssigne());
        sb.append("<br>Control Date: ");
        sb.append(tag.getControlDate());
        sb.append("<br><B>Due Date:</B> ");
        sb.append(tag.getDueDate());
    }

    public static void appendTo(ITaskTag tag, org.radixware.schemas.xscml.TaskTagType xTask) {
        xTask.setAssignee(tag.getAssigne());
        xTask.setBehavior(tag.getBehavior());

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (tag.getControlDate() != null) {
            xTask.setControlDate(df.format(tag.getControlDate()));
        }

        if (tag.getCreateDate() != null) {
            xTask.setCreateDate(df.format(tag.getCreateDate()));
        }

        if (tag.getDueDate() != null) {
            xTask.setDueDate(df.format(tag.getDueDate()));
        }

        xTask.setPriority(tag.getPriority());
        xTask.setType(tag.getType());
        xTask.setStringValue(tag.getNotes());
    }

    private static Date toDate(String string) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(string);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            if (c.get(Calendar.YEAR) >= 2008) {
                return date;
            }

            df = new SimpleDateFormat("yyyy-MM-dd+hh:mm");
            date = df.parse(string);
            return date;
        } catch (ParseException e) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date date = df.parse(string);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                if (c.get(Calendar.YEAR) >= 2008) {
                    return date;
                }
            } catch (ParseException ex) {
            }
            return new Date();

        }
    }

    public static void loadFrom(ITaskTag tag, org.radixware.schemas.xscml.TaskTagType xTask) {
        tag.setAssigne(xTask.getAssignee());
        if (xTask.isSetControlDate()) {
            tag.setControlDate(toDate(xTask.getControlDate()));
        }
        if (xTask.isSetCreateDate()) {
            tag.setCreateDate(toDate(xTask.getCreateDate()));
        }
        if (xTask.isSetDueDate()) {
            tag.setDueDate(toDate(xTask.getDueDate()));
        }

        tag.setBehavior(xTask.getBehavior());

        tag.setPriority(xTask.getPriority());
        tag.setType(xTask.getType());
        tag.setNotes(xTask.getStringValue());
    }

    public static String getMessage(ITaskTag task) {
        String message = "Feature isn't realized yet: " + task.getNotes();
        Scml.Tag tag = (Scml.Tag) task;
        final Definition def = tag.getDefinition();
        if (def != null) {
            message += " at " + def.getQualifiedName();
        }
        return message;
    }

    public static EEventSeverity getSeverity(ITaskTag tag) {
        switch (tag.getPriority()) {
            case LOW:
                return EEventSeverity.EVENT;
            case ORDINAL:
                return EEventSeverity.WARNING;
            case HIGH:
                return EEventSeverity.ERROR;
            case CRITICAL:
                return EEventSeverity.ALARM;
            default:
                return EEventSeverity.ERROR;
        }
    }
}
