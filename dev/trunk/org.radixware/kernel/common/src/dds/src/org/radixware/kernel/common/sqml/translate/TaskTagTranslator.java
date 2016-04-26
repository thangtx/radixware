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

package org.radixware.kernel.common.sqml.translate;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETaskTagBehavior;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.TaskTagUtils;
import org.radixware.kernel.common.sqml.tags.TaskTag;


class TaskTagTranslator<T extends TaskTag> extends SqmlTagTranslator<T> {

    @Override
    public void translate(T tag, CodePrinter cp) {
        final ETaskTagBehavior behavior = tag.getBehavior();
        switch (behavior) {
            case DO_NOTHING:
                cp.print("/**/");
                break;
            case LOG_MESSGAGE:
                cp.print("RDX_Trace.put(");

                final EEventSeverity severity = TaskTagUtils.getSeverity(tag);
                cp.print(severity.getValue());

                cp.print(", ");
                final String message = TaskTagUtils.getMessage(tag);
                cp.printStringLiteral(message);

                cp.print(", ");
                cp.printStringLiteral(EEventSource.TODO.getValue());
                cp.print(");");
                break;
            case THROW_EXCEPTION:
                cp.print("RAISE_APPLICATION_ERROR(-20000, ");
                final String exceptionMessage = TaskTagUtils.getMessage(tag);
                cp.printStringLiteral(exceptionMessage);
                cp.print(");");
                break;
            default:
                throw new TagTranslateError(tag, "Unknown task tag behavior: " + String.valueOf(behavior));
        }

//        if (behavior != ETaskTagBehavior.DO_NOTHING) {
//            final Definition def = tag.getOwnerDefinition();
//            if (def != null && !(def instanceof DdsPlSqlObjectItemDef) && !(def instanceof DdsTriggerDef)) {
//                if (behavior == ETaskTagBehavior.LOG_MESSGAGE) {
//                    throw new TagTranslateError(tag, "Log message in SQL");
//                } else {
//                    throw new TagTranslateError(tag, "Raise error in SQL");
//                }
//            }
//        }
    }
}
