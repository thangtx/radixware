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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.enums.EDwfFormClerkAutoSelect;
import org.radixware.kernel.common.enums.EDwfFormSubmitVariant;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.meta.RadEnumDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.DataTag;
import org.radixware.kernel.common.types.ArrBool;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.types.Algorithm;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;

public class AppBlockFormCreatorExecutor {

    static final Id DWF_PROCESS_PROP_OWNER_ID = Id.Factory.loadFrom("colXLGX3QEDXLNRDF5JABIFNQAABA");
    static final Id DWF_CLERK_ENTITY_ID = Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4");
    static final Id DWF_FORM_ENTITY_ID = Id.Factory.loadFrom("tblVP66JC4HXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_CLASS_ID = Id.Factory.loadFrom("aecVP66JC4HXLNRDF5JABIFNQAABA");

    static final Id DWF_FORM_PROP_ID_ID = Id.Factory.loadFrom("colJGHFDGEHXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_CLASSGUID_ID = Id.Factory.loadFrom("colSM57J2EHXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_PROCESSID_ID = Id.Factory.loadFrom("colFOZ357EHXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_TITLE_ID = Id.Factory.loadFrom("col34WUQDUIXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_HEADERTITLE_ID = Id.Factory.loadFrom("colPUVYKRSRX7OBDBG7ABIFNQAABA");
    static final Id DWF_FORM_PROP_FOOTERTITLE_ID = Id.Factory.loadFrom("colVNBSW6CRX7OBDBG7ABIFNQAABA");
    static final Id DWF_FORM_PROP_PRIORITY_ID = Id.Factory.loadFrom("colFPUVAH4IXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_OPENTIME_ID = Id.Factory.loadFrom("colFFO5OHMJXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_DUETIME_ID = Id.Factory.loadFrom("colD2WOGKUJXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_OVERDUETIME_ID = Id.Factory.loadFrom("col4MQBMOEJXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_EXPIRATIONTIME_ID = Id.Factory.loadFrom("col7DJHQP4JXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_CLOSETIME_ID = Id.Factory.loadFrom("colO5JOWR4JXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_CLERKNAME_ID = Id.Factory.loadFrom("colPMMP7BUKXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_SUBMITVARIANT_ID = Id.Factory.loadFrom("col4LCFVKUMXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_SUBMITVARIANTS_ID = Id.Factory.loadFrom("colEHLVNESRX7OBDBG7ABIFNQAABA");

    static final Id DWF_FORM_PROP_SUBMITVARIANTSTITLE_ID = Id.Factory.loadFrom("colUZPAT5LEUJDKBIWBKC6GBA3RCA");
    static final Id DWF_FORM_PROP_SUBMITVARIANTSNEEDCONFIRM_ID = Id.Factory.loadFrom("colEM7CCTU6LRDDTP35YMRAT6IIMQ");
    static final Id DWF_FORM_PROP_SUBMITVARIANTSNEEDUPDATE_ID = Id.Factory.loadFrom("colRURA3POSGBEBNMQFDOAMM4LXXA");
    static final Id DWF_FORM_PROP_SUBMITVARIANTSVISIBLE_ID = Id.Factory.loadFrom("colM52BWLMWZJDIDOZ6BMFB3JUZOU");

    static final Id DWF_FORM_PROP_CLASSID_ID = Id.Factory.loadFrom("col5CMSW6MNXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_SELECTORPRESENTATIONID_ID = Id.Factory.loadFrom("colNO2XWSMOXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_EDITORPRESENTATIONIDS_ID = Id.Factory.loadFrom("colEO72AVMOXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_CREATIONPRESENTATIONID_ID = Id.Factory.loadFrom("col76QBKXUOXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_ADMINPRESENTATIONID_ID = Id.Factory.loadFrom("col7CXV3KY5BRGM5BTO3QF2HAZHAI");

    static final Id DWF_FORM_PROP_SELECTEDPID_ID = Id.Factory.loadFrom("colKNQKUZ4PXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_CONTENTSAVING_ID = Id.Factory.loadFrom("col5IUWY4UPXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_OLDCONTENT_ID = Id.Factory.loadFrom("colFDEJ3XUPXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_NEWCONTENT_ID = Id.Factory.loadFrom("colBOKAL24PXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_WAITID_ID = Id.Factory.loadFrom("col56S2R4UPXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_CLERKROLEGUIDS_ID = Id.Factory.loadFrom("colUSDVE5UQXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_ADMINROLEGUIDS_ID = Id.Factory.loadFrom("col7VCUPAEQXLNRDF5JABIFNQAABA");
    static final Id DWF_FORM_PROP_ACCESSAREA_ID = Id.Factory.loadFrom("colDG7VJCMQXLNRDF5JABIFNQAABA");

    static final Id DWF_FORM_PROP_SELECTCONDITION_ID = Id.Factory.loadFrom("colKZYFXOOU6PNRDF6UABIFNQAABA");
    static final Id DWF_FORM_PROP_RESTRICTIONS_ID = Id.Factory.loadFrom("colR35RDQV7BHOBDF7KABIFNQAABA");

    static final Id DWF_FORM_PROP_FORBID_FOR_PROCESS_CREATOR_ID = Id.Factory.loadFrom("colOIPTT7ZYYRAWHJ7PPXNVS3GW5M");
    static final Id DWF_FORM_PROP_FORBID_REDIRECTION = Id.Factory.loadFrom("colRLTHTDATCFEZPELZRCNXILBD34");

    private static final String qryGetActiveSuitableClerkStmtSQL = "begin ?:= RDX_WF.getActiveSuitableClerk(?); end;";
    private static final Stmt qryGetActiveSuitableClerkStmt = new Stmt(new Stmt(qryGetActiveSuitableClerkStmtSQL, Types.VARCHAR, Types.BIGINT), 1);

    private static final String qryGetSuitableClerkStmtSQL = "begin ?:= RDX_WF.getSuitableClerk(?); end;";
    private static final Stmt qryGetSuitableClerkStmt = new Stmt(new Stmt(qryGetSuitableClerkStmtSQL, Types.VARCHAR, Types.BIGINT), 1);

    private final IDbQueries delegate = new DelegateDbQueries(this, null);

    protected AppBlockFormCreatorExecutor() {
    }

    static Entity initForm(final Algorithm algo, final Id classId) throws Exception {
        AlgorithmExecutor executor = algo.getExecutor();
        if (executor.syncExecution) {
            throw new AppError("Form generator blocks can not be used in synchronous mode");
        }

        Arte arte = algo.getArte();
        Entity process = algo.getProcess();

        Entity form = (Entity) arte.newObject(classId);
        form.init(null, null, EEntityInitializationPhase.PROGRAMMED_CREATION);

        form.setProp(DWF_FORM_PROP_PROCESSID_ID, process.getPid().getPkVals().get(0));
        Id algoId = Id.Factory.loadFrom(algo.getClass().getSimpleName());

        String title = (String) algo.getProperty("title");
        if (title == null) {
            Id titleStrId = Id.Factory.loadFrom((String) algo.getProperty("titleStrId"));
            title = MultilingualString.get(arte, algoId, titleStrId);
        }
        if (title != null) {
            title = title.substring(0, Math.min(title.length(), 1000));
        }
        form.setProp(DWF_FORM_PROP_TITLE_ID, title);

        String headerTitle = (String) algo.getProperty("headerTitle");
        if (headerTitle == null) {
            Id headerTitleStrId = Id.Factory.loadFrom((String) algo.getProperty("headerTitleStrId"));
            headerTitle = MultilingualString.get(arte, algoId, headerTitleStrId);
        }
        if (headerTitle != null) {
            headerTitle = headerTitle.substring(0, Math.min(headerTitle.length(), 1000));
        }
        form.setProp(DWF_FORM_PROP_HEADERTITLE_ID, headerTitle);

        String footerTitle = (String) algo.getProperty("footerTitle");
        if (footerTitle == null) {
            Id footerTitleStrId = Id.Factory.loadFrom((String) algo.getProperty("footerTitleStrId"));
            footerTitle = MultilingualString.get(arte, algoId, footerTitleStrId);
        }
        if (footerTitle != null) {
            footerTitle = footerTitle.substring(0, Math.min(footerTitle.length(), 1000));
        }
        form.setProp(DWF_FORM_PROP_FOOTERTITLE_ID, footerTitle);

        form.setProp(DWF_FORM_PROP_SUBMITVARIANTS_ID, executor.getSubmitVariants(algo));
        form.setProp(DWF_FORM_PROP_PRIORITY_ID, algo.getProperty("priority"));
        form.setProp(DWF_FORM_PROP_OPENTIME_ID, executor.getCurrentTime());
        BigDecimal dueDelay = (BigDecimal) algo.getProperty("dueDelay");
        form.setProp(DWF_FORM_PROP_DUETIME_ID, executor.getCurrentTime(dueDelay == null ? 0 : Math.round(dueDelay.doubleValue())));
        BigDecimal overDueDelay = (BigDecimal) algo.getProperty("overDueDelay");
        form.setProp(DWF_FORM_PROP_OVERDUETIME_ID, overDueDelay == null ? null : executor.getCurrentTime(Math.round(overDueDelay.doubleValue())));
        BigDecimal timeout = (BigDecimal) algo.getProperty("timeout");
        form.setProp(DWF_FORM_PROP_EXPIRATIONTIME_ID, timeout == null ? null : executor.getCurrentTime(Math.round(timeout.doubleValue())));

        final Pid pid = new Pid(arte, Algorithm.DWF_PROCESS_TYPE_ENTITY_ID, Algorithm.DWF_PROCESS_TYPE_PROP_ID_ID, algo.getProcess().getProp(Algorithm.DWF_PROCESS_PROP_TYPE_ID));
        final Entity type = arte.getEntityObject(pid);

        ArrStr clerkRoles = (ArrStr) type.getProp(Algorithm.DWF_PROCESS_TYPE_PROP_CLERK_ROLE_IDS_ID);
        ArrStr clerkRoleGuids = (ArrStr) algo.getProperty("clerkRoleGuids");
        if (clerkRoleGuids != null) {
            clerkRoles = clerkRoleGuids;
        }
        form.setProp(DWF_FORM_PROP_CLERKROLEGUIDS_ID, clerkRoles);

        ArrStr adminRoles = (ArrStr) type.getProp(Algorithm.DWF_PROCESS_TYPE_PROP_ADMIN_ROLE_IDS_ID);
        ArrStr adminRoleGuids = (ArrStr) algo.getProperty("adminRoleGuids");
        if (adminRoleGuids != null) {
            adminRoles = adminRoleGuids;
        }
        form.setProp(DWF_FORM_PROP_ADMINROLEGUIDS_ID, adminRoles);

        try {
            final Boolean forbidForCreator = (Boolean) algo.getProperty("forbidForProcessCreator");
            if (forbidForCreator != null) {
                form.setProp(DWF_FORM_PROP_FORBID_FOR_PROCESS_CREATOR_ID, forbidForCreator);
            }
        } catch (Exception ex) {
            //ignore for backward compatibility
        }
        
        try {
            final Boolean forbidRedirection = (Boolean) algo.getProperty("forbidRedirection");
            if (forbidRedirection != null) {
                form.setProp(DWF_FORM_PROP_FORBID_REDIRECTION, forbidRedirection);
            }
        } catch (Exception ex) {
            //ignore for backward compatibility
        }

        form.setProp(DWF_FORM_PROP_CONTENTSAVING_ID, algo.getProperty("contentSaving"));
        form.setProp(DWF_FORM_PROP_WAITID_ID, null);
        loadSubmitVariants(algo, form);

        algo.setProperty("form", form);
        return form;
    }

    static void postInitForm(final Algorithm algo) throws Exception {
        final Arte arte = algo.getArte();
        final Entity process = algo.getProcess();

        Entity form = (Entity) algo.getProperty("form");
        if (process == null || form == null) {
            return;
        }

        CallableStatement stmt = null;
        String clerkName = null;
        Entity clerk = (Entity) algo.getProperty("clerk");
        EDwfFormClerkAutoSelect clerkAutoSelect = Utils.toEnum(algo.getProperty("clerkAutoSelect"), EDwfFormClerkAutoSelect.class);
        switch (clerkAutoSelect) {
            case NONE:
                clerkName = (clerk == null) ? null : (String) clerk.getPid().getPkVals().get(0);
                break;
            case ACTIVE_SUITABLE:
                try {
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    try {
                        stmt = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryGetActiveSuitableClerkStmt);
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    try {
                        stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                        stmt.setLong(2, (Long) form.getPid().getPkVals().get(0));
                        stmt.execute();
                        clerkName = stmt.getString(1);
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
                break;
            case SUITABLE:
                try {
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    try {
                        stmt = ((RadixConnection) arte.getDbConnection().get()).prepareCall(qryGetSuitableClerkStmt);
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                    arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    try {
                        stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                        stmt.setLong(2, (Long) form.getPid().getPkVals().get(0));
                        stmt.execute();
                        clerkName = stmt.getString(1);
                    } finally {
                        arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    }
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
                break;
            case PROCESS_OWNER:
                clerkName = (String) process.getProp(DWF_PROCESS_PROP_OWNER_ID);
                break;
        }

        form.setProp(DWF_FORM_PROP_CLERKNAME_ID, clerkName);

        Object accessArea = algo.getProperty("accessArea");
        if (accessArea != null) {
            java.lang.reflect.Method mth = accessArea.getClass().getMethod("mthTFOMPUCWGTOBDGANABIFNQAABA", new Class<?>[]{Long.class});
            mth.invoke(accessArea, new Object[]{form.getPid().getPkVals().get(0)});
        }

        form.update();
    }

    static void done(final Algorithm algo) throws Exception {
        Entity form = (Entity) algo.getProperty("form");
        Entity process = algo.getProcess();
        Boolean captureProcess = (Boolean) algo.getProperty("captureProcess");
        if (captureProcess.equals(Boolean.TRUE)) {
            process.setProp(DWF_PROCESS_PROP_OWNER_ID, form.getProp(DWF_FORM_PROP_CLERKNAME_ID));
            process.update();
        }
    }

    static public void loadSubmitVariants(final Algorithm algo, final Entity form) {
        final Arte arte = algo.getArte();
        final AlgorithmExecutor executor = algo.getExecutor();
        final Id algoId = Id.Factory.loadFrom(algo.getClass().getSimpleName());

        final List<String> vs = new LinkedList<String>();
        final StringTokenizer tokens = executor.getSubmitVariantsTokens(algo);
        while (tokens.hasMoreTokens()) {
            final String token = tokens.nextToken();
            if (token != null && !token.isEmpty()) {
                vs.add(token);
            }
        }

        final RadEnumDef enumDef;
        try {
            final Class<?> enumClass = arte.getDefManager().getClassLoader().loadClass("org.radixware.ads.mdlYMVABQK4LJFZZBPBASHNUQRZ5U.common.acsQ4A5NN4KXLNRDF5JABIFNQAABA");
            final Method m = enumClass.getMethod("getRadMeta", new Class<?>[]{});
            enumDef = (RadEnumDef) m.invoke(null, new Object[]{});
        } catch (Exception e) {
            return;
        }

        final HashMap v2t;
        final HashMap v2tid;
        final HashMap v2c;
        final HashMap v2u;
        final HashMap v2v;
        try {
            v2t = (HashMap) executor.getProperty("submitVariantsTitle");
            v2tid = (HashMap) executor.getProperty("submitVariantsTitleId");
            v2c = (HashMap) executor.getProperty("submitVariantsNeedConfirm");
            v2u = (HashMap) executor.getProperty("submitVariantsNeedUpdate");
            v2v = (HashMap) executor.getProperty("submitVariantsVisible");
        } catch (Exception e) {
            return;
        }

        final ArrStr titles = new ArrStr();
        final ArrBool confirms = new ArrBool();
        final ArrBool updates = new ArrBool();
        final ArrBool visibles = new ArrBool();

        for (String v : vs) {
            final EDwfFormSubmitVariant key = EDwfFormSubmitVariant.getForValue(v);
            final RadEnumDef.IItem item = enumDef.getItemByVal(key);
            final String title = item.getTitle(arte);

            if (v2t != null && v2t.containsKey(key)) {
                titles.add((String) v2t.get(key));
            } else {
                if (v2tid != null && v2tid.containsKey(key)) {
                    final Id titleId = Id.Factory.loadFrom((String) v2tid.get(key));
                    titles.add(MultilingualString.get(arte, algoId, titleId));
                } else {
                    titles.add(title);
                }
            }

            if (v2c != null && v2c.containsKey(key)) {
                confirms.add((Boolean) v2c.get(key));
            } else {
                confirms.add(Boolean.TRUE);
            }

            if (v2u != null && v2u.containsKey(key)) {
                updates.add((Boolean) v2u.get(key));
            } else {
                boolean needUpdate
                        = key == EDwfFormSubmitVariant.OK
                        || key == EDwfFormSubmitVariant.YES
                        || key == EDwfFormSubmitVariant.COMMIT
                        || key == EDwfFormSubmitVariant.FINISH
                        || key == EDwfFormSubmitVariant.NEXT
                        || key == EDwfFormSubmitVariant.PROCEED;
                updates.add(needUpdate);
            }

            if (v2v != null && v2v.containsKey(key)) {
                visibles.add((Boolean) v2v.get(key));
            } else {
                visibles.add(Boolean.TRUE);
            }
        }

        form.setProp(DWF_FORM_PROP_SUBMITVARIANTSTITLE_ID, titles);
        form.setProp(DWF_FORM_PROP_SUBMITVARIANTSNEEDCONFIRM_ID, confirms);
        form.setProp(DWF_FORM_PROP_SUBMITVARIANTSNEEDUPDATE_ID, updates);
        form.setProp(DWF_FORM_PROP_SUBMITVARIANTSVISIBLE_ID, visibles);
    }

    public static String prepareCondition(final Algorithm algo, final String condition) throws Exception {
        if (condition == null) {
            return null;
        }
        AlgorithmExecutor executor = algo.getExecutor();
        org.radixware.schemas.xscml.Sqml xOldSqml = org.radixware.schemas.xscml.Sqml.Factory.parse(condition);
        Sqml oldSqml = Sqml.Factory.newInstance();
        Sqml newSqml = Sqml.Factory.newInstance();
        oldSqml.loadFrom(xOldSqml);
        while (!oldSqml.getItems().isEmpty()) {
            Sqml.Item item = oldSqml.getItems().get(0);
            oldSqml.getItems().remove(0);
            if (item instanceof DataTag) {
                DataTag tag = (DataTag) item;
                Object o;
                if (AdsAlgoClassDef.TIME_VAR_ID.equals(tag.getId())) {
                    o = executor.getCurrentTime();
                } else {
                    o = executor.getData(tag.getId());
                }
                CodePrinter p = CodePrinter.Factory.newSqlPrinter();
                if (o instanceof String || o instanceof Character) {
                    p.printStringLiteral(String.valueOf(o));
                } else if (o instanceof Long || o instanceof BigDecimal) {
                    p.print(String.valueOf(o));
                } else if (o instanceof Boolean) {
                    p.print(((Boolean) o) == Boolean.TRUE ? "1" : "0");
                } else if (o instanceof Date) {
                    String date = new SimpleDateFormat("dd-MM-yy HH:mm:ss").format((Date) o);
                    p.print("TO_DATE('" + date + "', 'DD-MM-YYYY HH24:MI:SS')");
                } else {
                    p.print(String.valueOf(o));
                }
                Sqml.Text text = Sqml.Text.Factory.newInstance(new String(p.getContents()));
                newSqml.getItems().add(text);
            } else {
                newSqml.getItems().add(item);
            }
        }
        org.radixware.schemas.xscml.Sqml xNewSqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
        newSqml.appendTo(xNewSqml);
        return xNewSqml.xmlText();
    }

    // block entry (return 0 - wait)
    static public int invoke(final Algorithm algo) throws Exception {
        AlgorithmExecutor executor = algo.getExecutor();
        BigDecimal timeout = (BigDecimal) algo.getProperty("timeout");
        if (executor.syncExecution || (timeout != null && timeout.compareTo(new BigDecimal(AlgorithmExecutor.WAIT_TIMEOUT_BOUNDARY)) < 0)) {
            //TODO выполнить с синхронным ожиданием
            if (timeout == null || timeout.compareTo(BigDecimal.valueOf(0)) <= 0) {
            } else {
            }
            return 0;
        } else {
            if (timeout != null) {
                algo.scheduleTimeoutJob(timeout.doubleValue(), null); // подписка на onTimeout
            }			//TODO сгенерировать форму
            return -1;
        }
    }

    // block leave
    public static int resume(final Algorithm algo) throws Exception {
        //TODO таймаут или редактирование завершено
        return 0;
    }
}
