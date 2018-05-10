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
package org.radixware.kernel.common.builder.check.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchEngine;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.repository.Layer;

public class Checker {

    private boolean wasErrors = false;
    private final IProblemHandler problemHandler;
    private static final CheckersManager checkersManager = new CheckersManager();
    private final CheckHistory checkHistory = new CheckHistory();
    private final CheckOptions checkOptions = new CheckOptions();
//    private final CheckCache checkCache = new CheckCache();
    private final IProblemHandler proxy;
    private final Object lock = new Object();
    private final Set<RadixObject> suppressWarnings = new HashSet<>();

    private boolean isRadixObjectWithinDeprecatedDefinition(RadixObject obj) {
        for (RadixObject object = obj; object != null; object = object.getContainer()) {
            if (object instanceof Definition) {
                if (((Definition) object).isDeprecated()) {
                    return true;
                }
                if (object instanceof Module) {
                    return false;
                }
            }
        }
        return false;
    }

    public Checker(final IProblemHandler problemHandler, CheckOptions options) {
        this.problemHandler = problemHandler;
        this.checkOptions.accept(options);
        proxy = new IProblemHandler() {
            @Override
            public void accept(final RadixProblem problem) {
                synchronized (this) {

                    if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                        wasErrors = true;
                    } else {
                        //RADIX-4967
                        synchronized (lock) {
                            if (suppressWarnings.contains(problem.getSource())) {
                                return;
                            }
                            if (AdsDefinitionProblems.EXPIRED_DEPRECATED_DEFINITION != problem.getCode() && isRadixObjectWithinDeprecatedDefinition(problem.getSource())) {
                                return;
                            }
                        }
                    }
                    Checker.this.problemHandler.accept(problem);
                }

            }
        };
    }

    public Checker(CheckOptions options) {
        this(RadixProblemRegistry.getDefault(), options);
    }

    /**
     * Check specified radix objects and its children. Return true if check
     * completed without errors, false otherwise.
     */
    public boolean check(final Collection<? extends RadixObject> contexts) {
        wasErrors = false;

        RadixProblemRegistry.getDefault().clear(contexts);
//        checkCache.clear();
//        

        for (RadixObject context : contexts) {
            context.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    checkRadixObject(radixObject);
                }
            }, VisitorProviderFactory.createCheckVisitorProvider());
        }

        return !wasErrors;
    }

    /**
     * Check specified radix object (without children).
     */
    public void checkRadixObject(final RadixObject radixObject) {

        final RadixObjectChecker checker = checkersManager.find(radixObject);
        if (checker != null) {
            checker.setHistory(checkHistory);
            checker.setCheckOptions(checkOptions);
//            checker.setCheckCache(checkCache);
            boolean as = false;
            try {
                //RADIX-4967
                final Layer l = radixObject.getLayer();

                if (l != null && l.isReadOnly()) {
                    synchronized (lock) {
                        suppressWarnings.add(radixObject);
                        as = true;
                    }
                }
                try {
                    if (!checkOptions.isCheckAllOvrPathes()) {
                        SearchEngine.disableMultipleResolution();
                    }
                    checker.check(radixObject, proxy);
                } finally {
                    SearchEngine.enableMultipleResolution();
                }
            } finally {
                if (as) {
                    synchronized (lock) {
                        suppressWarnings.remove(radixObject);
                    }
                }
            }
        } else if (radixObject instanceof Definition) {
            final String message = "Checker not found for " + radixObject.getClass().getName();
            RadixProblem problem = RadixProblem.Factory.newWarning(radixObject, message);
            proxy.accept(problem);
        }
    }

    public CheckHistory getCheckHistory() {
        return checkHistory;
    }

    public void checkRadixObjectDoc(final RadixObject radixObject) {

        final RadixObjectChecker checker = checkersManager.find(radixObject);
        if (checker != null) {
            checker.setHistory(checkHistory);
            checker.setCheckOptions(checkOptions);
            boolean as = false;
            try {
                //RADIX-4967
                final Layer l = radixObject.getLayer();

                if (l != null && l.isReadOnly()) {
                    synchronized (lock) {
                        suppressWarnings.add(radixObject);
                        as = true;
                    }
                }
                try {
                    if (!checkOptions.isCheckAllOvrPathes()) {
                        SearchEngine.disableMultipleResolution();
                    }
                    checker.checkDocumentation(radixObject, proxy);
                    if (radixObject instanceof AdsMethodDef) {
                        AdsMethodDef mthDef = (AdsMethodDef) radixObject;
                        if (mthDef.getProfile().getReturnValue() != null) {
                            checker.checkDocumentation(mthDef.getProfile().getReturnValue(), problemHandler);
                        }
                        for (MethodParameter p : mthDef.getProfile().getParametersList()) {
                            checker.checkDocumentation(p, problemHandler);
                        }
                    }
                } finally {
                    SearchEngine.enableMultipleResolution();
                }
            } finally {
                if (as) {
                    synchronized (lock) {
                        suppressWarnings.remove(radixObject);
                    }
                }
            }
        } else if (radixObject instanceof Definition) {
            final String message = "Checker not found for " + radixObject.getClass().getName();
            RadixProblem problem = RadixProblem.Factory.newWarning(radixObject, message);
            proxy.accept(problem);
        }
    }

    public boolean checkDoc(final Collection<? extends RadixObject> contexts) {
        wasErrors = false;

        RadixProblemRegistry.getDefault().clear(contexts);
//        

        for (RadixObject context : contexts) {
            context.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    checkRadixObjectDoc(radixObject);
                }
            }, VisitorProviderFactory.createCheckVisitorProvider());
        }

        return !wasErrors;
    }
}
