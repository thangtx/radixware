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
package org.radixware.kernel.common.svn.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

public abstract class UserFuncVerifier extends UpgradeCompatibilityVerifier {

    private final Connection con;
    private boolean stopOnFirstError = false;
    private final ERuntimeEnvironmentType env;
    private final String extJarsPath;
    private final UserFuncNoizyVerifierHelper verifyHelper = new UserFuncNoizyVerifierHelper();
    private final CheckResults results = new CheckResults(verifyHelper);    
    

    public UserFuncVerifier(BranchHolderParams branchParams, Connection con, ERuntimeEnvironmentType env) {
        this(branchParams, con, env, null);
    }
    
    public UserFuncVerifier(BranchHolderParams branchParams, Connection con, ERuntimeEnvironmentType env, String extJarsPath) {
        super(branchParams);
        this.con = con;
        this.env = env;
        this.extJarsPath = extJarsPath;
    }
    
    public void setStopOnFirstError(boolean stopOnFirstError) {
        this.stopOnFirstError = stopOnFirstError;
    }

    @Override
    public boolean verify() {
        try {
            message("Verifing user functions at " + con.getMetaData().getURL());
        } catch (SQLException ex) {
            //ignore
        }
        BranchHolder holder = new BranchHolder(this, branchParams.isSkipDevelopmentLayers());
        try {
            if (!holder.initialize(branchParams)) {
                return false;
            }
            message("Checking Usages-API compatibility...");
            results.start();
            Usages2APIVerifier verifier = createUsages2APIVerifier();
            verifyHelper.setResolver(verifier);
            verifier.setStopOnFirstError(stopOnFirstError);
            if (!verifier.verifyUserFunctions(con, holder)) {
                return false;
            }
            
            verifyHelper.clearContext();
            message("Checking binary compatibility...");
            PatchClassFileLinkageVerifier cfVerifier = createCFVerifier();
            boolean res = cfVerifier.verifyUserFunctions(con, holder, env, extJarsPath);
            results.finish();
            
            results.printReport();
            return res;
        } finally {
            holder.finish();
        }
    }

    protected Usages2APIVerifier createUsages2APIVerifier() {        
        return new Usages2APIVerifier(branchParams) {

            @Override
            public boolean cancel() {
                if (cancellableHandler != null) {
                    return cancellableHandler.cancel();
                }
                return super.cancel();
            }

            @Override
            public boolean wasCancelled() {
                if (cancellableHandler != null) {
                    return cancellableHandler.wasCancelled();
                }
                return super.wasCancelled();
            }

            @Override
            protected void processMessage(EEventSeverity severity, String message) {
                verifyHelper.message(getContext(), severity, message);
            }

            @Override
            public void error(Exception e) {
                verifyHelper.error(getContext(), e);
            }

            @Override
            public void error(String message) {
                verifyHelper.error(getContext(), message);
            }

            @Override
            public void message(String message) {
                verifyHelper.message(getContext(), message);
            }
        };
    }

    protected PatchClassFileLinkageVerifier createCFVerifier() {
        return new PatchClassFileLinkageVerifier(branchParams, false) {
            
            @Override
            protected boolean canIgnoreProblem(final Class c, final String problem) {
                if (classLinkageProblemHandler != null) {
                    return classLinkageProblemHandler.canIgnoreProblem(c, problem);
                }
                return super.canIgnoreProblem(c, problem);
            }

            @Override
            public boolean cancel() {
                if (cancellableHandler != null) {
                    return cancellableHandler.cancel();
                }
                return super.cancel();
            }

            @Override
            public boolean wasCancelled() {
                if (cancellableHandler != null) {
                    return cancellableHandler.wasCancelled();
                }
                return super.wasCancelled();
            }

            @Override
            public void message(EEventSeverity sev, String message) {
                verifyHelper.message(getContext(), sev, message);
            }
            
            @Override
            public void error(Exception e) {
                verifyHelper.error(getContext(), e);
            }

            @Override
            public void error(String message) {
                verifyHelper.error(getContext(), message);
            }

            @Override
            public void message(String message) {
                verifyHelper.message(getContext(), message);
            }
        };
    }
    
    public String getResults() {
        return results.getReport();
    }
    
    private class CheckResults {
        
        private final Map<Integer, IVerifyContext> contexts = new LinkedHashMap<>();
        private final Map<Integer, List<CheckMessage>> results = new LinkedHashMap<>();
        private final UserFuncNoizyVerifierHelper helper;
        private long startTime;
        private long totalTime;
        

        public CheckResults(UserFuncNoizyVerifierHelper helper) {
            this.helper = helper;
        }
        
        public void addMessage(IVerifyContext ctx, CheckMessage mess) {
            if (ctx == null) {
                return;
            }
            if (!contexts.containsKey(ctx.getKey())) {
                contexts.put(ctx.getKey(), ctx);
                results.put(ctx.getKey(), new LinkedList<CheckMessage>());
            }
            results.get(ctx.getKey()).add(mess);
        }
        
        public void start() {
            startTime = System.currentTimeMillis();
            verifyHelper.clearContext();
        }
        
        public void finish() {
            totalTime = System.currentTimeMillis() - startTime;
        }
                
        private String getReport() {
            final StringBuilder reportBuilder = new StringBuilder();
            reportBuilder.append("****************************\n");
            reportBuilder.append("USER FUNCTIONS CHECK RESULTS\n");
            reportBuilder.append("****************************\n");
            final List<Integer> keys = new ArrayList<>(results.keySet());
            Collections.sort(keys);
            Collections.reverse(keys);
            boolean thereAreErrors, thereAreWarnings;
            int nFuncsWithErrors = 0;
            int nFuncsWithWarnings = 0;
            for (Integer key : keys) {
                thereAreWarnings = thereAreErrors = false;
                reportBuilder.append(helper.getCtxName(contexts.get(key))).append('\n');
                for (CheckMessage m : results.get(key)) {
                    if (m.severity.getValue() > EEventSeverity.WARNING.getValue()) {
                        thereAreErrors = true;
                    } else if (m.severity.getValue() == EEventSeverity.WARNING.getValue().longValue()) {
                        thereAreWarnings = true;
                    }
                    reportBuilder.append("   ").append(m.toString()).append('\n');
                }
                reportBuilder.append('\n');
                if (thereAreErrors) {
                    nFuncsWithErrors++;
                }
                if (thereAreWarnings) {
                    nFuncsWithWarnings++;
                }
            }
            
            reportBuilder.append('\n');
            
            reportBuilder.append("STATISTICS:").append('\n');
            reportBuilder.append("Operation: User Functions Binary and API Check").append('\n');
            final SimpleDateFormat startTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            final Calendar tTime = Calendar.getInstance();
            tTime.setTimeInMillis(startTime);
            reportBuilder.append("Date: ").append(startTimeFormat.format(tTime.getTime())).append('\n');
            reportBuilder.append("Functions with errors: ").append(nFuncsWithErrors).append('\n');
            reportBuilder.append("Functions with warnings: ").append(nFuncsWithWarnings).append('\n');
            
            tTime.setTimeInMillis(totalTime);
            final String pattern = tTime.getTimeInMillis() >= 60000 ? "mm:ss" : "ss";
            final SimpleDateFormat timeFormat = new SimpleDateFormat(pattern);
            reportBuilder.append(String.format("Total time: %s", timeFormat.format(tTime.getTime())));
            
            return reportBuilder.toString();
        }
        
        public void printReport() {
            message(getReport());
        }
    }
    
    
    private class UserFuncNoizyVerifierHelper {

        private IDefinitionNameResolver resolver;
        private IVerifyContext curCtx;
        
        public void setResolver(IDefinitionNameResolver resolver) {
            this.resolver = resolver;
        }

        public void message(IVerifyContext ctx, EEventSeverity severity, String message) {
            message(ctx, severity, message, null);
        }
        
        public void error(IVerifyContext ctx, Exception e) {
            message(ctx, EEventSeverity.ERROR, null, e);
        }

        public void error(IVerifyContext ctx, String message) {
            message(ctx, EEventSeverity.ERROR, message, null);
        }

        public void message(IVerifyContext ctx, String message) {
            message(ctx, EEventSeverity.EVENT, message, null);
        }
        
        private void message(IVerifyContext ctx, EEventSeverity severity, String message, Exception ex) {
            printContext(ctx);
            if (ex != null || severity.getValue() >= EEventSeverity.WARNING.getValue()) {
                if (ex == null) {
                    UserFuncVerifier.this.error(message);
                } else {
                    UserFuncVerifier.this.error(ex);
                }
            } else {
                UserFuncVerifier.this.message(message);
            }
            final CheckMessage m = (ex != null) ? new CheckMessage(severity, ex) : new CheckMessage(severity, message);
            results.addMessage(ctx, m);
        }

        private void printContext(IVerifyContext ctx) {
            if (ctx != null && (curCtx == null || curCtx.getKey() != ctx.getKey())) {
                curCtx = ctx;
                UserFuncVerifier.this.message(getCtxName(curCtx));
            }
        }
        
        public String getCtxName (IVerifyContext ctx) {
            return ctx.getName(resolver);
        }
        
        public void clearContext() {
            curCtx = null;
        }
    }

    private static class CheckMessage {

        private final EEventSeverity severity;
        private final String message;
        private final Exception ex;

        public CheckMessage(EEventSeverity severity, String message) {
            this.severity = severity;
            this.message = message;
            this.ex = null;
        }

        public CheckMessage(EEventSeverity severity, Exception ex) {
            this.severity = severity;
            this.message = null;
            this.ex = ex;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('[').append(severity.getName()).append(']').append(" ");
            if (message != null) {
                sb.append(message);
            } else {
                sb.append(ex.getMessage());
            }
            return sb.toString();
        }
    }
}
