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

package org.radixware.kernel.designer.debugger.impl;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;


public class NameResolver {

    private Branch branch;
    private static final DefinitionInfo NOT_A_DEF_NAME = new DefinitionInfo(null, null);

    private static class DefinitionInfo {

        private WeakReference<AdsDefinition> ref;
        private String className;

        DefinitionInfo(String className, AdsDefinition def) {
            if (def != null && className != null) {
                this.ref = new WeakReference<AdsDefinition>(def);
                this.className = className;
            }
        }

        private AdsDefinition getDefinition() {
            AdsDefinition def = ref.get();
            if (def == null) {
                throw new RadixError("");
            }
            return def;
        }

        public String getDefinitionName(String defaultName) {
            AdsDefinition def = getDefinition();
            if (def != null) {
                return def.getQualifiedName();
            } else {
                return defaultName;
            }
        }

        public String getMethodName(String javaMethodName) {
            AdsDefinition def = getDefinition();
            if (def instanceof AdsClassDef) {
                AdsClassDef clazz = (AdsClassDef) def;

                int start = 0;
                int end = -1;
                String suffix = null;
                if (javaMethodName.startsWith("get") || javaMethodName.startsWith("set")) {
                    start = 3;
                    if (javaMethodName.endsWith("$$$")) {
                        end = javaMethodName.length() - 3;
                        suffix = "[internal]";
                    }
                }
                String lookup;
                if (start > 0 || end > 0) {
                    if (end > 0) {
                        lookup = javaMethodName.substring(start, end);
                    } else {
                        lookup = javaMethodName.substring(start);
                    }
                } else {
                    lookup = javaMethodName;
                }
                String name = null;
                Id id = Id.Factory.loadFrom(lookup);
                if (id != null) {
                    if (id.getPrefix() == EDefinitionIdPrefix.ADS_CLASS_METHOD) {
                        AdsMethodDef method = clazz.getMethods().findById(id, EScope.LOCAL).get();
                        if (method != null) {
                            name = method.getName();
                        }
                    } else {
                        AdsPropertyDef prop = clazz.getProperties().findById(id, EScope.LOCAL).get();
                        if (prop != null) {
                            name = prop.getName();
                        }
                    }
                    if (name != null) {
                        if (suffix != null) {
                            return name + suffix;
                        } else {
                            return name;
                        }
                    }
                } else {
                    return javaMethodName;
                }
            }
            return javaMethodName;
        }
    }
    private final Map<String, DefinitionInfo> class2Def = new HashMap<String, DefinitionInfo>();

    public NameResolver() {
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String className2DefinitionName(String className) {
        DefinitionInfo info = getInfo(className);
        if (info == null) {
            return className;
        } else {
            return info.getDefinitionName(className);
        }
    }

    public AdsDefinition findDefinitionByClassName(String className) {
        DefinitionInfo info = getInfo(className);
        if (info == null) {
            return null;
        } else {
            return info.getDefinition();
        }
    }

    public Branch getBranch() {
        return branch;
    }

    private DefinitionInfo getInfo(String className) {
        synchronized (this) {
            if (branch == null) {
                return null;
            }
            DefinitionInfo defName = class2Def.get(className);
            boolean justAllocated = false;
            if (defName == null) {
                AdsDefinition classDef = JavaSourceSupport.javaName2AdsDefinitionRef(className, branch);

                if (classDef != null) {
                    justAllocated = true;
                    defName = new DefinitionInfo(className, classDef);
                } else {
                    defName = NOT_A_DEF_NAME;
                }
                class2Def.put(className, defName);
            }
            if (defName == NOT_A_DEF_NAME) {
                return null;
            } else {
                if (!justAllocated) {
                    try {
                        defName.getDefinition();
                    } catch (RadixError e) {
                        class2Def.remove(className);
                        //reallocate reference
                        return getInfo(className);
                    }
                }
                return defName;
            }
        }
    }

    public String methodName(String className, String methodName) {
        DefinitionInfo info = getInfo(className);
        if (info == null) {
            return className + "." + methodName;
        } else {
            return info.getDefinitionName(className) + "." + info.getMethodName(methodName);
        }
    }
}
