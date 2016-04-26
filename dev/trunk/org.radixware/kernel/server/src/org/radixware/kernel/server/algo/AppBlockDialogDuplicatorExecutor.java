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
import java.sql.CallableStatement;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.enums.EDwfFormClerkAutoSelect;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadUserPropDef;
import org.radixware.kernel.server.types.Algorithm;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.common.utils.Utils;
import static org.radixware.kernel.server.algo.AppBlockFormCreatorExecutor.DWF_FORM_PROP_EDITORPRESENTATIONIDS_ID;
import org.radixware.kernel.server.exceptions.DatabaseError;

public class AppBlockDialogDuplicatorExecutor extends AppBlockFormCreatorExecutor {

        static private Set<String> getOverrideTokens(final Algorithm algo) {
            final Set<String> tokens =  new HashSet<String>();
            final StringTokenizer t = new StringTokenizer((String)Utils.nvl(algo.getProperty("overrideMask"), ""), "\n");
            while (t.hasMoreTokens())
                tokens.add(t.nextToken());
            return tokens;
        }

	static Entity initForm(final Algorithm algo, final Id classId, final Entity src) throws Exception {
                AlgorithmExecutor executor = algo.getExecutor();
		if (executor.syncExecution)
			throw new AppError("Form generator blocks can not be used in synchronous mode");

		if (src == null)
			throw new AppError("Source dialog not defined");

		final Arte arte = algo.getArte();
		final Id algoId = Id.Factory.loadFrom(algo.getClass().getSimpleName());

		Entity form = (Entity)arte.newObject(classId);		
		form.init(null, src, EEntityInitializationPhase.PROGRAMMED_CREATION);

		final Set<String> tokens = getOverrideTokens(algo);
                if (tokens.contains("title")) {
                    String title = (String)executor.getProperty("title");
                    Id titleStrId = Id.Factory.loadFrom((String)executor.getProperty("titleStrId"));
                    form.setProp(DWF_FORM_PROP_TITLE_ID, title == null ? MultilingualString.get(arte, algoId, titleStrId) : title);
                }
                if (tokens.contains("headerTitle")) {
                    String headerTitle = (String)executor.getProperty("headerTitle");
                    Id headerTitleStrId = Id.Factory.loadFrom((String)executor.getProperty("headerTitleStrId"));
                    form.setProp(DWF_FORM_PROP_HEADERTITLE_ID, headerTitle == null ? MultilingualString.get(arte, algoId, headerTitleStrId) : headerTitle);
                }
                if (tokens.contains("footerTitle")) {
                    String footerTitle = (String)executor.getProperty("footerTitle");
                    Id footerTitleStrId = Id.Factory.loadFrom((String)executor.getProperty("footerTitleStrId"));
                    form.setProp(DWF_FORM_PROP_FOOTERTITLE_ID, footerTitle == null ? MultilingualString.get(arte, algoId, footerTitleStrId) : footerTitle);
                }
                if (tokens.contains("priority"))
                    form.setProp(DWF_FORM_PROP_PRIORITY_ID, executor.getProperty("priority"));
                if (tokens.contains("presentationIds"))
                    form.setProp(DWF_FORM_PROP_EDITORPRESENTATIONIDS_ID, Utils.toString(executor.getProperty("presentationIds")));
                
                if (tokens.contains("adminPresentationId"))
                    form.setProp(DWF_FORM_PROP_ADMINPRESENTATIONID_ID, Utils.toString(executor.getProperty("adminPresentationId")));
                
                if (tokens.contains("submitVariants")) {
                    form.setProp(DWF_FORM_PROP_SUBMITVARIANTS_ID, executor.getProperty("submitVariants"));
                    loadSubmitVariants(algo, form);
                }
                if (tokens.contains("contentSaving"))
                    form.setProp(DWF_FORM_PROP_CONTENTSAVING_ID, executor.getProperty("contentSaving"));
                if (tokens.contains("clerk")) {
                }
                if (tokens.contains("clerkRoleGuids"))
                    form.setProp(DWF_FORM_PROP_CLERKROLEGUIDS_ID, executor.getProperty("clerkRoleGuids"));
                if (tokens.contains("adminRoleGuids"))
                    form.setProp(DWF_FORM_PROP_ADMINROLEGUIDS_ID, executor.getProperty("adminRoleGuids"));
                if (tokens.contains("accessArea")) {
                }

		Entity process = algo.getProcess(); 
		form.setProp(DWF_FORM_PROP_PROCESSID_ID, process.getPid().getPkVals().get(0));
		form.setProp(DWF_FORM_PROP_OPENTIME_ID, executor.getCurrentTime());

		BigDecimal dueDelay = (BigDecimal)executor.getProperty("dueDelay");
		form.setProp(DWF_FORM_PROP_DUETIME_ID, executor.getCurrentTime(dueDelay == null ? 0 : Math.round(dueDelay.doubleValue())));
		BigDecimal overDueDelay = (BigDecimal)executor.getProperty("overDueDelay");
		form.setProp(DWF_FORM_PROP_OVERDUETIME_ID, overDueDelay == null ? null : executor.getCurrentTime(Math.round(overDueDelay.doubleValue())));
		BigDecimal timeout = (BigDecimal)algo.getProperty("timeout");
		form.setProp(DWF_FORM_PROP_EXPIRATIONTIME_ID, timeout == null ? null : executor.getCurrentTime(Math.round(timeout.doubleValue())));

                form.setProp(DWF_FORM_PROP_CLOSETIME_ID, null);
		form.setProp(DWF_FORM_PROP_SUBMITVARIANT_ID, null);
		form.setProp(DWF_FORM_PROP_WAITID_ID, null);

		executor.setProperty("form", form);
		return form;
	}

	static void postInitForm(final Algorithm algo) throws Exception {
                final Arte arte = algo.getArte();
		final Entity process = algo.getProcess();

		Entity form = (Entity)algo.getProperty("form");
                if (process == null || form == null)
                    return;

		final Set<String> tokens = getOverrideTokens(algo);
                if (tokens.contains("clerk")) {
                    CallableStatement stmt = null;
                    String clerkName = null;
                    Entity clerk = (Entity)algo.getProperty("clerk");
                    EDwfFormClerkAutoSelect clerkAutoSelect = Utils.toEnum(algo.getProperty("clerkAutoSelect"), EDwfFormClerkAutoSelect.class);
                    switch (clerkAutoSelect) {
                        case NONE:
                            clerkName = (clerk == null) ? null : (String)clerk.getPid().getPkVals().get(0);
                            break;
                        case ACTIVE_SUITABLE:
                            try {
                                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                                try {
                                    stmt = arte.getDbConnection().get().prepareCall(
                                        "begin ?:= RDX_WF.getActiveSuitableClerk(?); end;"
                                        );
                                } finally {
                                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                                }    
                                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                                try {
                                    stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                                    stmt.setLong(2, (Long)form.getPid().getPkVals().get(0));
                                    stmt.execute();
                                    clerkName = stmt.getString(1);
                                } finally {
                                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                                }     
                            } finally {
                                if (stmt != null)
                                    stmt.close();
                            }    
                            break;
                        case SUITABLE:
                            try {
                                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                                try {
                                    stmt = arte.getDbConnection().get().prepareCall(
                                        "begin ?:= RDX_WF.getSuitableClerk(?); end;"
                                        );
                                } finally {
                                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                                }   
                                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                                try {
                                    stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                                    stmt.setLong(2, (Long)form.getPid().getPkVals().get(0));
                                    stmt.execute();
                                    clerkName = stmt.getString(1);
                                } finally {
                                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                                }    
                            } finally {    
                                if (stmt != null)
                                    stmt.close();
                            }
                            break;
                        case PROCESS_OWNER:
                            clerkName = (String)process.getProp(DWF_PROCESS_PROP_OWNER_ID);
                            break;
                    }
                    form.setProp(DWF_FORM_PROP_CLERKNAME_ID, clerkName);
                }

                if (tokens.contains("accessArea")) {
                    Object accessArea = algo.getProperty("accessArea");
                    if (accessArea != null) {
                        java.lang.reflect.Method mth = accessArea.getClass().getMethod("mthTFOMPUCWGTOBDGANABIFNQAABA", new Class<?>[] { Long.class });
			mth.invoke(accessArea, new Object[]{ form.getPid().getPkVals().get(0) });
                    }
                }

                form.update();
	}

	static private void setProps(final Algorithm algo, final Entity form) {
		final Arte arte = algo.getArte();
                final AlgorithmExecutor executor = algo.getExecutor();
		final RadClassDef classDef = arte.getDefManager().getClassDef(Id.Factory.loadFrom((String)executor.getProperty("class")));
		final Set<String> tokens = getOverrideTokens(algo);
                final StringTokenizer t = new StringTokenizer((String)Utils.nvl(executor.getProperty("userPropIds"), ""), "\n");
		while (t.hasMoreTokens()) {
			String token = t.nextToken();
                        try {                                                    
                            RadPropDef prop = classDef.getPropById(Id.Factory.loadFrom(token));
                            if (!(prop instanceof RadUserPropDef) || !tokens.contains(token))
    				continue;
                            ((RadUserPropDef)prop).setValue(form, executor.getProperty(prop.getId().toString()));
			} catch (Exception e) {
                            executor.trace(EEventSeverity.WARNING, "User property " + token + " setting error: " + e.toString());
			}
                }
	}

	static private void getProps(final Algorithm algo, final Entity form) {
		final Arte arte = algo.getArte();
                final AlgorithmExecutor executor = algo.getExecutor();
		final RadClassDef classDef = arte.getDefManager().getClassDef(Id.Factory.loadFrom((String)executor.getProperty("class")));
		final StringTokenizer t = new StringTokenizer((String)Utils.nvl(executor.getProperty("userPropIds"), ""), "\n");
		while (t.hasMoreTokens()) {
			String token = t.nextToken();
			try {
        			RadPropDef prop = classDef.getPropById(Id.Factory.loadFrom(token));
        			if (!(prop instanceof RadUserPropDef))
        				continue;
				RadUserPropDef userProp = (RadUserPropDef)prop;
				if (userProp.isValueExist(form.getPid()))
				{
					Object val = userProp.getValue(form);
					if (val == null)
						executor.setProperty(prop.getId().toString(), null);
					else
						executor.setProperty(prop.getId().toString(), val);
				}
				else
					executor.setProperty(prop.getId().toString(), null);
			} catch (Exception e) {
				executor.trace(EEventSeverity.WARNING, "User property " + token + " getting error: " + e.toString());
			}
		}
	}

	// return 0 - wait, 1 - timeout если задан	
	static public int invoke(final Algorithm algo) throws Exception {
                AlgorithmExecutor executor = algo.getExecutor();
		BigDecimal timeout = (BigDecimal)executor.getProperty("timeout");
		BigDecimal overdueDelay = (BigDecimal)executor.getProperty("overDueDelay");
		Boolean processOverDue = (Boolean)executor.getProperty("processOverDue");

		// генерируем диалог
                Object dialogClass = executor.getProperty("class");
                if (dialogClass == null)
			throw new AppError("Dialog class is not defined");

                Entity sourceDialog = (Entity)executor.getProperty("sourceDialog");
                if (sourceDialog == null) {
                    try {
                        Id sourceBlockId = Id.Factory.loadFrom((String)executor.getProperty("sourceBlockId"));                    
                        if (sourceBlockId != null)
                            sourceDialog = (Entity)executor.getData(algo.getBlockPropId(sourceBlockId, "form"));
                    } catch (Exception e) {
                        sourceDialog = null;
                    }
                }
                if (sourceDialog == null)
			throw new AppError("Source dialog is not defined");

		Entity form = initForm(algo, Id.Factory.loadFrom((String)dialogClass), sourceDialog);
		form.setProp(DWF_FORM_PROP_CLASSID_ID, Utils.toString(dialogClass));
		if (executor.syncExecution || (timeout != null && timeout.compareTo(new BigDecimal(AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY)) < 0)) {
			// выполняем с синхронным ожиданием
			if (timeout.compareTo(BigDecimal.valueOf(0)) <= 0) {
                            
			} else {
                            java.lang.Thread.sleep(Math.round(timeout.doubleValue()*1000));
			}
			form.create(null);
			postInitForm(algo);
			setProps(algo, form);
        		StringTokenizer t = new StringTokenizer((String)Utils.nvl(executor.getProperty("submitVariants"), ""), ";");
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
			form.setProp(DWF_FORM_PROP_WAITID_ID, executor.getProperty("submitWID"));
			form.create(null);
			postInitForm(algo);
			setProps(algo, form);
			return -1;
		}
	}

	public static int resume(final Algorithm algo) throws Exception {
		// таймаут или редактирование завершено
		final Arte arte = algo.getArte();
                final AlgorithmExecutor executor = algo.getExecutor();

		Long waitId = executor.getCurrentThread().resumedWaitId;
		String submitVariant = (String)executor.getCurrentThread().clientData;

		Long timeoutWID = (Long)executor.getProperty("timeoutWID");
		Long overdueWID = (Long)executor.getProperty("overdueWID");
		Long submitWID = (Long)executor.getProperty("submitWID");
		Entity form = (Entity)executor.getProperty("form");
		if (form == null || !form.isInDatabase(true))
			throw new AppError("Form in resume method doesn't exists");
                
		done(algo);
		String clerk = (String)form.getProp(DWF_FORM_PROP_CLERKNAME_ID);
		if (clerk != null)
			try {
                            executor.setProperty("clerk", arte.getEntityObject(new Pid(arte, DWF_CLERK_ENTITY_ID, clerk)));
			} catch(Exception ex) {
                            // do nothing
                            Logger.getLogger(AppBlockDialogDuplicatorExecutor.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
		getProps(algo, form);
		StringTokenizer t = new StringTokenizer((String)Utils.nvl(executor.getProperty("submitVariants"), ""), ";");
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
			if (timeoutWID != null)
                            try {
				arte.getJobQueue().unsheduleJob(timeoutWID);
                            } catch (DatabaseError e) {
                                executor.trace(EEventSeverity.ERROR, e.getMessage());
                            }
			if (overdueWID != null)
                            try {
				arte.getJobQueue().unsheduleJob(overdueWID);
                            } catch (DatabaseError e) {
                                executor.trace(EEventSeverity.ERROR, e.getMessage());
                            }
			form.setProp(DWF_FORM_PROP_CLOSETIME_ID, executor.getCurrentTime());
			form.update();
			int idx = -1;
			while (t.hasMoreTokens()) {
				String token = t.nextToken(); idx++;
				if (token.equals(submitVariant))
					return idx;
			}
			throw new AppError("Unknown submit variant " + submitVariant); 
		}
		throw new AppError("Unknown waitId " + waitId); 
	}
}