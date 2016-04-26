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

package org.radixware.kernel.server.algo;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import static org.radixware.kernel.server.algo.AppBlockFormCreatorExecutor.DWF_FORM_PROP_CLOSETIME_ID;
import static org.radixware.kernel.server.algo.AppBlockFormCreatorExecutor.DWF_FORM_PROP_EDITORPRESENTATIONIDS_ID;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadUserPropDef;
import org.radixware.kernel.server.types.Algorithm;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

public class AppBlockDialogCreatorExecutor extends AppBlockFormCreatorExecutor {

    static private void setProps(final Algorithm algo, final Entity form) {
        final Arte arte = algo.getArte();
        AlgorithmExecutor executor = algo.getExecutor();
        RadClassDef classDef = arte.getDefManager().getClassDef(Id.Factory.loadFrom((String) executor.getProperty("class")));
        StringTokenizer t = new StringTokenizer((String) Utils.nvl(executor.getProperty("userPropIds"), ""), "\n");
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            try {
                RadPropDef prop = classDef.getPropById(Id.Factory.loadFrom(token));
                if (!(prop instanceof RadUserPropDef)) {
                    continue;
                }
                RadUserPropDef userProp = (RadUserPropDef) prop;
                userProp.setValue(form, executor.getProperty(prop.getId().toString()));
            } catch (Exception e) {
                executor.trace(EEventSeverity.WARNING, "User property " + token + " setting error: " + ExceptionTextFormatter.exceptionStackToString(e));
            }
        }
    }

    static private void getProps(final Algorithm algo, final Entity form) {
        final Arte arte = algo.getArte();
        AlgorithmExecutor executor = algo.getExecutor();
        RadClassDef classDef = arte.getDefManager().getClassDef(Id.Factory.loadFrom((String) executor.getProperty("class")));
        StringTokenizer t = new StringTokenizer((String) Utils.nvl(executor.getProperty("userPropIds"), ""), "\n");
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            try {
                RadPropDef prop = classDef.getPropById(Id.Factory.loadFrom(token));
                if (!(prop instanceof RadUserPropDef)) {
                    continue;
                }
                RadUserPropDef userProp = (RadUserPropDef) prop;
                if (userProp.isValueExist(form.getPid())) {
                    Object val = userProp.getValue(form);
                    executor.setProperty(prop.getId().toString(), val);
                } else {
                    executor.setProperty(prop.getId().toString(), null);
                }
            } catch (Exception e) {
                executor.trace(EEventSeverity.WARNING, "User property " + token + " getting error: " + ExceptionTextFormatter.exceptionStackToString(e));
            }
        }
    }

    // return 0 - wait, 1 - timeout ???? ?????	
    static public int invoke(final Algorithm algo) throws Exception {
        AlgorithmExecutor executor = algo.getExecutor();
        BigDecimal timeout = (BigDecimal) executor.getProperty("timeout");
        BigDecimal overdueDelay = (BigDecimal) executor.getProperty("overDueDelay");
        Boolean processOverDue = (Boolean) executor.getProperty("processOverDue");

        // ?????????? ??????
        Object dialogClass = executor.getProperty("class");
        if (dialogClass == null) {
            throw new AppError("Dialog class is not defined");
        }

        Entity form = initForm(algo, Id.Factory.loadFrom((String) dialogClass));
        try {
            algo.setProperty("dialog", form);
        } catch (Exception ex) {
            // do nothing
            Logger.getLogger(AppBlockDialogCreatorExecutor.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        form.setProp(DWF_FORM_PROP_CLASSID_ID, Utils.toString(dialogClass));

        Object presentationIds = executor.getProperty("presentationIds");
        if (presentationIds == null) {
            throw new AppError("Dialog presentations are not defined");
        }
        form.setProp(DWF_FORM_PROP_EDITORPRESENTATIONIDS_ID, Utils.toString(presentationIds));

        Object adminPresentationId = executor.getProperty("adminPresentationId");
        form.setProp(DWF_FORM_PROP_ADMINPRESENTATIONID_ID, Utils.toString(adminPresentationId));
        if (executor.syncExecution || (timeout != null && timeout.compareTo(new BigDecimal(AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY)) < 0)) {
            // ????????? ? ?????????? ?????????
            if (timeout.compareTo(BigDecimal.valueOf(0)) <= 0) {
            } else {
                java.lang.Thread.sleep(Math.round(timeout.doubleValue() * 1000));
            }
            form.create(null);
            postInitForm(algo);
            setProps(algo, form);
            StringTokenizer t = new StringTokenizer((String) Utils.nvl(executor.getProperty("submitVariants"), ""), ";");
            return t.countTokens();
        } else {
            algo.scheduleEvent(executor.getPropertyId("submitWID"));
            if (overdueDelay != null && Boolean.TRUE == processOverDue) {
                algo.scheduleTimeoutJob(overdueDelay.doubleValue(), executor.getPropertyId("overdueWID"), false); // onOverdue
            } else {
                executor.setProperty("overdueWID", null);
            }
            if (timeout != null) {
                algo.scheduleTimeoutJob(timeout.doubleValue(), executor.getPropertyId("timeoutWID")); // onTimeout
            } else {
                executor.setProperty("timeoutWID", null);
            }
            form.setProp(DWF_FORM_PROP_WAITID_ID, executor.getProperty("submitWID"));
            form.create(null);
            postInitForm(algo);
            setProps(algo, form);
            return -1;
        }
    }

    public static int resume(final Algorithm algo) throws Exception {
        final Arte arte = algo.getArte();
        final AlgorithmExecutor executor = algo.getExecutor();

        final Long waitId = executor.getCurrentThread().resumedWaitId;
        final String submitVariant = (String) executor.getCurrentThread().clientData;

        final Long timeoutWID = (Long) executor.getProperty("timeoutWID");
        final Long overdueWID = (Long) executor.getProperty("overdueWID");
        final Long submitWID = (Long) executor.getProperty("submitWID");
        final Entity form = (Entity) executor.getProperty("form");
        if (form == null || !form.isInDatabase(true)) {
            throw new AppError("Form in resume method doesn't exists");
        }

        done(algo);
        final String clerk = (String) form.getProp(DWF_FORM_PROP_CLERKNAME_ID);
        if (clerk != null) {
            try {
                executor.setProperty("clerk", arte.getEntityObject(new Pid(arte, DWF_CLERK_ENTITY_ID, clerk)));
            } catch (Exception ex) {
                // do nothing   
                Logger.getLogger(AppBlockDialogCreatorExecutor.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        getProps(algo, form);
        final StringTokenizer t = new StringTokenizer((String) Utils.nvl(executor.getProperty("submitVariants"), ""), ";");
        if (waitId.equals(timeoutWID)) {
            executor.setProperty("submitVariant", null);
            form.setProp(DWF_FORM_PROP_CLOSETIME_ID, executor.getCurrentTime());
            form.update();
            return t.countTokens();
        }
        if (waitId.equals(overdueWID)) {
            executor.setProperty("submitVariant", null);
            executor.setProperty("overdueWID", null);
            return -1;
        }
        if (waitId.equals(submitWID)) {
            executor.setProperty("submitVariant", submitVariant);
            if (timeoutWID != null) {
                try {
                    arte.getJobQueue().unsheduleJob(timeoutWID);
                } catch (DatabaseError e) {
                    executor.trace(EEventSeverity.ERROR, e.getMessage());
                }
            }
            if (overdueWID != null) {
                try {
                    arte.getJobQueue().unsheduleJob(overdueWID);
                } catch (DatabaseError e) {
                    executor.trace(EEventSeverity.ERROR, e.getMessage());
                }
            }
            form.setProp(DWF_FORM_PROP_CLOSETIME_ID, executor.getCurrentTime());
            form.update();
            int idx = -1;
            while (t.hasMoreTokens()) {
                final String token = t.nextToken();
                idx++;
                if (token.equals(submitVariant)) {
                    return idx;
                }
            }
            throw new AppError("Unknown submit variant " + submitVariant);
        }
        throw new AppError("Unknown waitId " + waitId);
    }
}