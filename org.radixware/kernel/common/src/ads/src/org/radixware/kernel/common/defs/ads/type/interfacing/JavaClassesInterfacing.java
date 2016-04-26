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

package org.radixware.kernel.common.defs.ads.type.interfacing;

import java.util.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;


public class JavaClassesInterfacing {

    public static final String OBJECT_CLASS_NAME = "java.lang.Object";
    private final Layer context;
    private final Set<String> alien = new HashSet<>();
    private final Set<String> childs = new HashSet<>();
    private final IFilter filter;
    private final String baseTypeName;
    private final ReferenceBinding baseTypeClass;
    private final boolean isInterface;
    private final boolean isObject;
    private final ERuntimeEnvironmentType env;

    public JavaClassesInterfacing(String javaClassName, Layer context, ERuntimeEnvironmentType env) {
        this(javaClassName, context, env, new DefaultFilter());
    }

    public JavaClassesInterfacing(String javaClassName, Layer context, ERuntimeEnvironmentType env, IFilter filter) {
        this.context = context;
        this.filter = filter;
        this.env = env;

        if (javaClassName != null && !javaClassName.isEmpty() && context != null) {
            this.baseTypeName = javaClassName;
            this.baseTypeClass = getClass(baseTypeName);
            this.isInterface = baseTypeClass != null && baseTypeClass.isInterface();
            this.isObject = isJavaLangObject(javaClassName);
        } else {
            this.baseTypeName = null;
            this.baseTypeClass = null;
            this.isInterface = false;
            this.isObject = false;
        }
    }

    private boolean isJavaLangObject(String className) {
        return OBJECT_CLASS_NAME.equals(className);
    }

    public List<String> getChildList() {
        if (baseTypeName == null) {
            return Collections.<String>emptyList();
        }

        if (isObject) {
            childs.clear();
            childs.addAll(LookupUtils.collectLibraryClasses(context, env));
        } else {
            collectChilds();
        }

        List<String> classes = new LinkedList<>();

        for (String child : childs) {
            if (accept(child)) {
                classes.add(child);
            }
        }
        return classes;
    }

    public boolean isSuperFor(String className) {
        if (accept(className)) {
            if (isObject) {
                return getClass(className) != null;
            } else if (isUsed(className)) {
                return childs.contains(className);
            } else {
                ReferenceBinding subClass = getClass(className);
                if (subClass != null && (!subClass.isInterface() || isInterface)) {
                    return checkSubClass(subClass);
                }
            }
        }
        return false;
    }

    private void collectChilds() {
        addToChildList(baseTypeName);

        if (baseTypeClass == null || baseTypeClass.isFinal()) {
            return;
        }

        List<String> knownClasses = LookupUtils.collectLibraryClasses(context, env);

        for (String subClassName : knownClasses) {
            isSuperFor(subClassName);
        }
    }

    private boolean isUsed(String name) {
        return alien.contains(name) || childs.contains(name);
    }

    private boolean checkSubClass(ReferenceBinding rootClass) {
        if (rootClass == null) {
            return false;
        }
        String name = String.valueOf(CharOperation.concatWith(rootClass.compoundName, '.'));

        if (alien.contains(name)) {
            return false;
        } else if (childs.contains(name)) {
            return true;
        } else if (isSubClass(name)) {
            addToChildList(name);
            return true;
        } else {

            ReferenceBinding rootSuperClassType = rootClass.superclass();
            boolean accept = checkSubClass(rootSuperClassType);

            if (isInterface) {
                ReferenceBinding[] superInterfaces = rootClass.superInterfaces();
                for (ReferenceBinding superInteface : superInterfaces) {
                    accept = accept || checkSubClass(superInteface);
                }
            }
            if (accept) {
                addToChildList(name);
            } else {
                alien.add(name);
            }
            return accept;
        }
    }

    private boolean isSubClass(String name) {
        return baseTypeName.equals(name);
    }

    private ReferenceBinding getClass(String name) {
        return LookupUtils.findPlatformClass(name, context, env);
    }

    private void addToChildList(String className) {
        childs.add(className);
    }

    private boolean accept(String className) {
        return filter == null || filter.accept(className);
    }

    public interface IFilter {

        boolean accept(String name);
    }

    private static final class DefaultFilter implements IFilter {

        @Override
        public boolean accept(String name) {
            return true;
        }
    }
}
