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
import org.radixware.kernel.common.enums.EDwfFormSubmitVariant;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.Algorithm;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

public class AppBlockSelectorFormCreatorExecutor extends AppBlockFormCreatorExecutor {

    static final Id DWF_SELECTOR_FORM_CLASS_ID = Id.Factory.loadFrom("aclO2T5FNHYXTNRDF5NABIFNQAABA");

    // return 0 - wait, 1 - timeout если задан	
    static public int invoke(final Algorithm algo) throws Exception {
        AlgorithmExecutor executor = algo.getExecutor();
        BigDecimal timeout = (BigDecimal) algo.getProperty("timeout");
        BigDecimal overdueDelay = (BigDecimal) executor.getProperty("overDueDelay");
        Boolean processOverDue = (Boolean) executor.getProperty("processOverDue");

        // генерируем форму
        Entity form = initForm(algo, DWF_SELECTOR_FORM_CLASS_ID);

        Object objectClass = executor.getProperty("objClassId");
        if (objectClass == null) {
            throw new AppError("Object class is not defined");
        }

        form.setProp(DWF_FORM_PROP_CLASSID_ID, Utils.toString(objectClass));

        Object presentationId = executor.getProperty("presentationId");
        if (presentationId == null) {
            throw new AppError("Presentation is not defined");
        }

        form.setProp(DWF_FORM_PROP_SELECTORPRESENTATIONID_ID, Utils.toString(presentationId));
        form.setProp(DWF_FORM_PROP_SELECTCONDITION_ID, AppBlockFormCreatorExecutor.prepareCondition(algo, (String) algo.getProperty("condition")));

        form.setProp(DWF_FORM_PROP_RESTRICTIONS_ID, algo.getProperty("restrictions"));
        if (executor.syncExecution || (timeout != null && timeout.compareTo(new BigDecimal(AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY)) < 0)) {
            // выполняем с синхронным ожиданием
            if (timeout.compareTo(BigDecimal.valueOf(0)) <= 0) {
            } else {
                java.lang.Thread.sleep(Math.round(timeout.doubleValue() * 1000));
            }
            form.create(null);
            postInitForm(algo);
            StringTokenizer t = executor.getSubmitVariantsTokens(algo);
            return t.countTokens();
        } else {
            algo.scheduleEvent(executor.getPropertyId("submitWID")); // подписка на синхронный onEvent
            if (overdueDelay != null && Boolean.TRUE == processOverDue) {
                algo.scheduleTimeoutJob(overdueDelay.doubleValue(), executor.getPropertyId("overdueWID"), false); // подписка на onOverdue
            } else {
                executor.setProperty("overdueWID", null);
            }
            if (timeout != null) {
                algo.scheduleTimeoutJob(timeout.doubleValue(), executor.getPropertyId("timeoutWID")); // подписка на onTimeout
            } else {
                executor.setProperty("timeoutWID", null);
            }
            form.setProp(DWF_FORM_PROP_WAITID_ID, algo.getProperty("submitWID"));
            form.create(null);
            postInitForm(algo);
            return -1;
        }
    }

    public static int resume(final Algorithm algo) throws Exception {
        // таймаут или редактирование завершено
        final Arte arte = algo.getArte();
        final AlgorithmExecutor executor = algo.getExecutor();

        Long waitId = executor.getCurrentThread().resumedWaitId;
        EDwfFormSubmitVariant submitVariant = algo.getCurrentThread().clientData == null
                ? null : EDwfFormSubmitVariant.getForValue((String) algo.getCurrentThread().clientData);

        Long timeoutWID = (Long) algo.getProperty("timeoutWID");
        Long overdueWID = (Long) executor.getProperty("overdueWID");
        Long submitWID = (Long) algo.getProperty("submitWID");
        Entity form = (Entity) algo.getProperty("form");
        if (form == null || !form.isInDatabase(true)) {
            throw new AppError("Form in resume method doesn't exists");
        }

        done(algo);
        StringTokenizer t = executor.getSubmitVariantsTokens(algo);
        if (waitId.equals(timeoutWID)) {
            algo.setProperty("submitVariant", null);
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
            algo.setProperty("submitVariant", submitVariant);
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
            String clerk = (String) form.getProp(DWF_FORM_PROP_CLERKNAME_ID);
            if (clerk != null) {
                try {
                    algo.setProperty("clerk", arte.getEntityObject(new Pid(arte, DWF_CLERK_ENTITY_ID, clerk)));
                } catch (Exception ex) {
                    // do nothing   
                    Logger.getLogger(AppBlockSelectorFormCreatorExecutor.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
            ArrBool updates = (ArrBool) form.getProp(DWF_FORM_PROP_SUBMITVARIANTSNEEDUPDATE_ID);
            form.setProp(DWF_FORM_PROP_CLOSETIME_ID, executor.getCurrentTime());
            form.update();
            int idx = -1;
            while (t.hasMoreTokens()) {
                String token = t.nextToken();
                idx++;
                if (token.equals(submitVariant.getValue())) {
                    if (updates != null && idx < updates.size() && updates.get(idx) == Boolean.TRUE) {
                        Entity object = null;
                        try {
                            Id entityId = arte.getDefManager().getClassEntityId(Id.Factory.loadFrom((String) form.getProp(DWF_FORM_PROP_CLASSID_ID)));
                            String pid = (String) form.getProp(DWF_FORM_PROP_SELECTEDPID_ID);
                            if (pid != null) {
                                object = arte.getEntityObject(new Pid(arte, entityId, pid));
                            }
                        } catch (Exception e) {
                            throw new AppError("Unhandled exception: " + e.getMessage(), e);
                        }
                        algo.setProperty("object", object);
                    }
                    return idx;
                }
            }
            throw new AppError("Unknown submit variant " + submitVariant);
        }
        throw new AppError("Unknown waitId " + waitId);
    }
}