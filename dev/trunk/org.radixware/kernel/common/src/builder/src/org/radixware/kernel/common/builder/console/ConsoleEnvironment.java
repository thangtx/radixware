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

import java.sql.Connection;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IBuildProblemHandler;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.builder.api.ILifecycleManager;
import org.radixware.kernel.common.builder.api.IMutex;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;

 public class ConsoleEnvironment implements IBuildEnvironment {

    private BuildOptions buildOptions;
    private ConsoleProblemHandler problemHandler;
    private IMutex mutex = new ConsoleMutex();
    private IBuildDisplayer buildDisplayer = new ConsoleBuildDisplayer();
    private ILifecycleManager lfcManager = new ConsoleLifecycleManager();
    private final IFlowLogger flowLogger;
    private CheckOptions checkOptions = new CheckOptions();
    private long time;

    public ConsoleEnvironment(EnumSet<ERuntimeEnvironmentType> env, Connection dbConnection) {
        this(env, dbConnection, null);
    }

    public ConsoleEnvironment(EnumSet<ERuntimeEnvironmentType> env, Connection dbConnection, IFlowLogger flowLogger) {
        buildOptions = BuildOptions.Factory.newInstance();
        if (env == null) {
            env = EnumSet.allOf(ERuntimeEnvironmentType.class);
        }
        this.flowLogger = flowLogger == null ? new ConsoleFlowLogger() : flowLogger;
        buildOptions.setEnvironment(env);
        buildOptions.setMultythread(false);
        buildOptions.setVerifyClassLinkage(true);
        problemHandler = new ConsoleProblemHandler(this.flowLogger);
        checkOptions.setCheckAllOvrPathes(true);
        checkOptions.setDbConnection(dbConnection);
        checkOptions.setCheckSqlClassQuerySyntax(true);
        checkOptions.setMaxSqlQueryVariants(16);
        checkOptions.setCheckDocumentation(true);
    }

    @Override
    public void prepare() {
        time = System.currentTimeMillis();
        System.out.println("Started");
    }

    @Override
    public CheckOptions getCheckOptions() {
        return checkOptions;
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(ConsoleEnvironment.class.getName());
    }

    @Override
    public ConsoleProblemHandler getBuildProblemHandler() {
        return problemHandler;
    }

    @Override
    public BuildOptions getBuildOptions() {
        return buildOptions;
    }

    @Override
    public IMutex getMutex() {
        return mutex;
    }

    @Override
    public void complete() {
        time = System.currentTimeMillis() - time;
        getBuildDisplayer().getStatusDisplayer().setStatusText(MessageFormat.format("Completed {0,time,mm:ss}", time));
    }

    @Override
    public IBuildDisplayer getBuildDisplayer() {
        return buildDisplayer;
    }

    @Override
    public ILifecycleManager getLifecycleManager() {
        return lfcManager;
    }

    @Override
    public void displayResults() {
    }

    @Override
    public void targetsDetermined(Set<Definition> sucseedCheckDefinititons, List<Definition> failedCheckDefinitions) {
        if (failedCheckDefinitions != null && !failedCheckDefinitions.isEmpty()) {
            throw new ConsoleBuilder.BuildException("Check failed");
        }
    }

    @Override
    public IFlowLogger getFlowLogger() {
        return flowLogger;
    }

    public class ConsoleProblemHandler implements IBuildProblemHandler {

        private int errorsCount = 0;
        private int warningsCount = 0;
        private final IFlowLogger flowLogger;

        public ConsoleProblemHandler(IFlowLogger flowLogger) {
            this.flowLogger = flowLogger;
        }

        @Override
        public void accept(RadixProblem problem) {

            final RadixObject source = problem.getSource();
            Layer srcLayer = source.getLayer();
            if (srcLayer != null && srcLayer.isReadOnly()) {
                return;
            }
            if (problem.getSeverity().equals(RadixProblem.ESeverity.ERROR)) {
                errorsCount++;
            } else if (problem.getSeverity() == RadixProblem.ESeverity.WARNING) {
                warningsCount++;
            }

//            StringBuilder sb = new StringBuilder();
//            sb.append("[");
//            sb.append(problem.getSeverity().toString());
//            sb.append("] ");
//            sb.append("Problem in ");
//            sb.append(problem.getSource().getQualifiedName());
////            if (!(problem.getSource() instanceof Definition) && problem.getSource().getDefinition() != null) {
////                sb.append(" [");
////                sb.append(problem.getSource().getDefinition().toString());
////                sb.append("]");
////            }
//            sb.append(":\n");
//            sb.append(problem.getMessage());
//            sb.append("\n");
//            if (problem.getSeverity().equals(RadixProblem.ESeverity.ERROR)) {
//                System.err.print(sb.toString());
//            } else {
//                System.out.print(sb.toString());
//            }
            flowLogger.problem(problem);
        }

        @Override
        public void clear() {
            errorsCount = 0;
            warningsCount = 0;
        }

        @Override
        public boolean wasErrors() {
            return errorsCount > 0;
        }

        public int getErrorsCount() {
            return errorsCount;
        }

        public int getWarningsCount() {
            return warningsCount;
        }
    }

    private class ConsoleMutex implements IMutex {

        Lock l = new ReentrantLock();

        //Console build doesn't write anything to objects, so there is no any protection.
        @Override
        public void readAccess(Runnable r) {
            r.run();
        }

        @Override
        public Lock getLongProcessLock() {
            return l;
        }
    }

    private class ConsoleLifecycleManager implements ILifecycleManager {

        @Override
        public void saveAll() {
            return;
        }

        @Override
        public void exit() {
            System.exit(0);
        }
    }

    @Override
    public EBuildActionType getActionType() {
        return EBuildActionType.CLEAN_AND_BUILD;
    }
}
