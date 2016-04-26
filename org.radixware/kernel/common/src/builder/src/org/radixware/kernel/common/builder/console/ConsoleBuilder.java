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
package org.radixware.kernel.common.builder.console;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.builder.check.common.Checker;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.FileUtils;

public class ConsoleBuilder {

    public static final String BUILD_QUIET = "console.build.quiet";

    public static enum EBuildTarget {

        CLEAN("clean", EBuildActionType.CLEAN, 1),
        BUILD("build", EBuildActionType.BUILD, 2),
        RELEASE("release", EBuildActionType.RELEASE, 3),
        API_DOC_STAT("api-doc-stat", EBuildActionType.CHECK_API_DOC, 4);
        private String alias;
        private BuildActionExecutor.EBuildActionType actionType;
        private int priority;

        private EBuildTarget(String alias, EBuildActionType actionType, int priority) {
            this.alias = alias;
            this.actionType = actionType;
            this.priority = priority;
        }

        public EBuildActionType getActionType() {
            return actionType;
        }

        public String getAlias() {
            return alias;
        }

        public int getPriority() {
            return priority;
        }
    }

    public static enum EBuildEnv {

        COMMON("common", EnumSet.of(ERuntimeEnvironmentType.COMMON)),
        SERVER("server", EnumSet.of(ERuntimeEnvironmentType.SERVER)),
        EXPLORER("explorer", EnumSet.of(ERuntimeEnvironmentType.EXPLORER)),
        ALL("all", EnumSet.allOf(ERuntimeEnvironmentType.class));
        private String alias;
        private EnumSet<ERuntimeEnvironmentType> env;

        EBuildEnv(String alias, EnumSet<ERuntimeEnvironmentType> env) {
            this.alias = alias;
            this.env = env;
        }

        public String getAlias() {
            return alias;
        }

        public EnumSet<ERuntimeEnvironmentType> getEnv() {
            return env;
        }
    }
    private static List<EBuildTarget> DEFAULT_BUILD_TARGETS = Arrays.asList(EBuildTarget.CLEAN, EBuildTarget.BUILD);
    private static EnumSet<ERuntimeEnvironmentType> DEFAULT_TARGET_ENV = EnumSet.allOf(ERuntimeEnvironmentType.class);
    private EnumSet<ERuntimeEnvironmentType> targetEnv;
    private List<EBuildTarget> buildTargets;
    private Map<String, EBuildTarget> buildTargetMap;
    private Map<String, EBuildEnv> envMap;
    private String layerName;
    private File branchDir;
    private RadixObject radixObject;
    private String[] args;
    private int argPos;
    private boolean performCheck;
    private String dbUrl, dbUser, dbPwd;
    private boolean apiDocCheckOnly;

    public static void main(String[] args) throws IOException {
        try {
            new ConsoleBuilder(args).build();
        } catch (IllegalConsoleArgumentException ex) {
            System.err.println(ex.getMessage());
            printUsage();
            System.exit(1);
        } catch (BuildError ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public ConsoleBuilder(String[] args) throws IllegalConsoleArgumentException {

        buildTargetMap = createActionTypeMap();
        envMap = createEnvMap();
        this.args = args;
        argPos = 0;

        apiDocCheckOnly = matchApiDocCheck();
        if (apiDocCheckOnly) {
            buildTargets = Collections.singletonList(EBuildTarget.API_DOC_STAT);
        }
        performCheck = matchCheck();

        buildTargets = new LinkedList<EBuildTarget>();

        EBuildTarget target = matchBuildTarget();
        while (target != null) {
            buildTargets.add(target);
            target = matchBuildTarget();
        }
        if (buildTargets.isEmpty() && !performCheck) {
            buildTargets = DEFAULT_BUILD_TARGETS;
        }
        Collections.sort(buildTargets);

        targetEnv = EnumSet.noneOf(ERuntimeEnvironmentType.class);
        if (argPos < args.length && args[argPos].equals("-env")) {
            argPos++;
            EBuildEnv buildEnv = matchEnv();
            while (buildEnv != null) {
                targetEnv.addAll(buildEnv.getEnv());
                buildEnv = matchEnv();
            }
        }
        if (targetEnv.isEmpty()) {
            targetEnv = DEFAULT_TARGET_ENV;
        }

        if (argPos < args.length && args[argPos].equals("-layer")) {
            argPos++;
            layerName = matchLayerName();
        }

        branchDir = matchBranchDir();
        if (branchDir == null) {
            throw new IllegalConsoleArgumentException("Branch directory not specified or invalid.");
        }

        Branch branch;
        try {
            branch = Branch.Factory.loadFromDir(branchDir);
            radixObject = branch;
            if (layerName != null) {
                Layer layer = branch.getLayers().findByURI(layerName);
                if (layer != null) {
                    radixObject = layer;
                } else {
                    throw new IllegalArgumentException("Layer " + layerName + " does not exist.");
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Can not load branch from " + branchDir.getAbsolutePath(), ex);
        }

        boolean quiet = matchQuiet();
        if (quiet) {
            System.setProperty(BUILD_QUIET, "");
        }

        if (argPos < args.length && args[argPos].equals("-dbUrl")) {
            argPos++;
            if (argPos < args.length) {
                dbUrl = args[argPos];
                argPos++;
            } else {
                throw new IllegalConsoleArgumentException("Db URL expected at argument " + (argPos + 1));
            }
        }

        if (argPos < args.length && args[argPos].equals("-dbUser")) {
            argPos++;
            if (argPos < args.length) {
                dbUser = args[argPos];
                argPos++;
            } else {
                throw new IllegalConsoleArgumentException("Db user name expected at argument " + (argPos + 1));
            }
        }
        if (argPos < args.length && args[argPos].equals("-dbPwd")) {
            argPos++;
            if (argPos < args.length) {
                dbPwd = args[argPos];
                argPos++;
            } else {
                throw new IllegalConsoleArgumentException("Db password expected at argument " + (argPos + 1));
            }
        }

        if (argPos != args.length) {
            throw new IllegalConsoleArgumentException("Non-expected parameters after " + (argPos + 1) + " argument");
        }
    }

    public ConsoleBuilder(RadixObject radixObject, List<EBuildTarget> buildTargets, EnumSet<ERuntimeEnvironmentType> targetEnv) {
        this.radixObject = radixObject;
        if (buildTargets == null) {
            buildTargets = DEFAULT_BUILD_TARGETS;
        }
        this.buildTargets = buildTargets;
        if (targetEnv == null) {
            targetEnv = DEFAULT_TARGET_ENV;
        }
        this.targetEnv = targetEnv;
    }

    private EBuildTarget matchBuildTarget() {
        if (argPos >= args.length) {
            return null;
        }
        String alias = args[argPos];
        EBuildTarget ret = buildTargetMap.get(alias);
        if (ret != null) {
            argPos++;
        }
        return ret;
    }

    private EBuildEnv matchEnv() {
        if (argPos >= args.length) {
            return null;
        }
        String alias = args[argPos];
        EBuildEnv ret = envMap.get(alias);
        if (ret != null) {
            argPos++;
        }
        return ret;
    }

    private String matchLayerName() {
        if (argPos >= args.length) {
            return null;
        }
        String ret = args[argPos];
        argPos++;
        return ret;
    }

    private File matchBranchDir() {
        if (argPos >= args.length) {
            return null;
        }
        String path = args[argPos];
        File dir = new File(path);
        if (!Branch.isBranchDir(dir)) {
            return null;
        }
        argPos++;
        return dir.getAbsoluteFile();
    }

    private boolean matchQuiet() {
        if (argPos >= args.length) {
            return false;
        }
        if ("-quiet".equals(args[argPos])) {
            argPos++;
            return true;
        }
        return false;
    }

    private boolean matchCheck() {
        if (argPos >= args.length) {
            return false;
        }
        if ("check".equals(args[argPos])) {
            argPos++;
            return true;
        }
        return false;
    }

    private boolean matchApiDocCheck() {
        if (argPos >= args.length) {
            return false;
        }
        if ("api-doc-stat".equals(args[argPos])) {
            argPos++;
            return true;
        }
        return false;
    }

    private void visitRadixObject(RadixObject radixObject) {
        radixObject.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                //doNothing
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());
    }
    private Connection dbConnection;

    private Connection getDbConnection() {
        return dbConnection;
    }

    public Connection newConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPwd);
    }

    private void closeDbConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                Logger.getLogger(ConsoleBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void build() throws BuildError {
        assert radixObject != null : "RadixObject for build can not be null";

        //load all child objects before build.
        //fix of #RADIX-2531 (bug with lazy loading)
        visitRadixObject(radixObject);

        RadixObjectChecker.DocumentationStatusReport docReport = null;

        int totalWarnings = 0;
        int totalErrors = 0;
        try {
            if (dbUrl != null && dbUser != null && dbPwd != null) {
                try {
                    dbConnection = newConnection();
                } catch (SQLException ex) {
                    new ConsoleFlowLogger().error("Exception on opening db connection");
                }
            }

            ConsoleEnvironment buildEnv = new ConsoleEnvironment(targetEnv, getDbConnection(), apiDocCheckOnly ? new ConsoleFlowLogger() {

                @Override
                public void problem(RadixProblem problem) {
                    //ignore
                }

            } : null);
            BuildActionExecutor executor = new BuildActionExecutor(buildEnv);

            if (performCheck || apiDocCheckOnly) {
                long time = System.currentTimeMillis();
                buildEnv.getBuildDisplayer().getDialogUtils().messageInformation("Check started...");
                CheckOptions options = buildEnv.getCheckOptions();
                if (apiDocCheckOnly) {
                    options.setCheckDocumentation(true);
                }
                final IProblemHandler ph = apiDocCheckOnly ? new IProblemHandler() {

                    @Override
                    public void accept(RadixProblem problem) {
                        //Do not report any issue
                    }
                } : buildEnv.getBuildProblemHandler();

                Checker checker = new Checker(ph, buildEnv.getCheckOptions());
                if (apiDocCheckOnly) {
                    checker.checkDoc(Collections.singleton(radixObject));
                } else {
                    checker.check(Collections.singleton(radixObject));
                }

                docReport = checker.getCheckHistory().findItemByClass(RadixObjectChecker.DocumentationStatusReport.class);

                if (buildEnv.getBuildProblemHandler().wasErrors()) {
                    time = System.currentTimeMillis() - time;
                    String timeString = MessageFormat.format("{0,time,mm:ss}", time);
                    throw new BuildError("Check failed after " + timeString + ". Total errors: " + buildEnv.getBuildProblemHandler().getErrorsCount() + ", total warnings: " + buildEnv.getBuildProblemHandler().getWarningsCount());
                }
                time = System.currentTimeMillis() - time;
                buildEnv.getBuildDisplayer().getDialogUtils().messageInformation(MessageFormat.format("Check completed in {0,time,mm:ss}", time) + ". Total warnings: " + buildEnv.getBuildProblemHandler().getWarningsCount());
                buildEnv.getBuildProblemHandler().clear();
            }

            if (!apiDocCheckOnly) {
                for (EBuildTarget buildTarget : buildTargets) {
                    EBuildActionType actionType = buildTarget.getActionType();
                    System.out.println(actionType);
                    executor.execute(radixObject, actionType);
                    totalErrors += buildEnv.getBuildProblemHandler().getErrorsCount();
                    totalWarnings += buildEnv.getBuildProblemHandler().getWarningsCount();
                    if (executor.wasErrors()) {
                        throw new BuildError("Build failed. Total errors: " + totalErrors + ", total warnings: " + totalWarnings);
                    }
                }
            }
        } catch (BuildException ex) {
            throw new BuildError(ex.getMessage());
        } finally {
            closeDbConnection();
            if (docReport != null) {
                String report = docReport.textualReport();
                System.out.println(report);
                //try to save result to api-check-status.txt
                final String workspace = System.getenv("WORKSPACE");
                if (workspace != null && !workspace.isEmpty()) {
                    File statusFile = new File(new File(workspace), "api-check-status.txt");
                    try {
                        FileUtils.writeToFile(statusFile, report);
                    } catch (IOException ex) {
                        Logger.getLogger(ConsoleBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
        System.out.println("No errors found during the build. Total warnings: " + totalWarnings);

    }

    private Map<String, EBuildTarget> createActionTypeMap() {
        Map<String, EBuildTarget> ret = new HashMap<String, EBuildTarget>();
        for (EBuildTarget buildTarget : EBuildTarget.values()) {
            ret.put(buildTarget.getAlias(), buildTarget);
        }
        return ret;
    }

    private Map<String, EBuildEnv> createEnvMap() {
        Map<String, EBuildEnv> ret = new HashMap<String, EBuildEnv>();
        for (EBuildEnv buildEnv : EBuildEnv.values()) {
            ret.put(buildEnv.getAlias(), buildEnv);
        }
        return ret;
    }

    public static void printUsage() {
        System.out.println("Usage: [check] [TARGET1 [TARGET2 ..]] [-env ENV1 [ENV2 ..]] [-layer LAYER] BRANCH_DIRECTORY [-quiet]");
        System.out.println("Supported targets:");
        for (EBuildTarget buildTarget : EBuildTarget.values()) {
            System.out.println("\t" + buildTarget.getAlias());
        }
        System.out.println("Supported environments:");
        for (EBuildEnv buildEnv : EBuildEnv.values()) {
            System.out.println("\t" + buildEnv.getAlias());
        }

    }

    public static class BuildError extends Throwable {

        private static final long serialVersionUID = 0;

        public BuildError(String message, Throwable cause) {
            super(message, cause);
        }

        public BuildError(String message) {
            super(message);
        }

        public BuildError() {
        }
    }

    public static class BuildException extends RuntimeException {

        private static final long serialVersionUID = 0;

        public BuildException(String message, Throwable cause) {
            super(message, cause);
        }

        public BuildException(String message) {
            super(message);
        }

        public BuildException() {
        }
    }

    public static class IllegalConsoleArgumentException extends RuntimeException {

        private static final long serialVersionUID = 0;

        public IllegalConsoleArgumentException(String s) {
            super(s);
        }

        public IllegalConsoleArgumentException() {
        }
    }
}
