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
package org.radixware.kernel.common.compiler;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;

public class CompilerUtils {

    public static void collectSourceFiles(File dir, Collection<File> results) {
        if (dir == null) {
            return;
        }
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".java");
            }
        });
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    collectSourceFiles(f, results);
                } else {
                    results.add(f);
                }
            }
        }
    }

    public static void writeBytes(File file, byte[] bytes) throws IOException {
        FileOutputStream to = null;
        try {
            to = new FileOutputStream(file);
            to.write(bytes, 0, bytes.length);
            to.flush();
        } finally {
            if (to != null) {
                to.close();
            }
        }
    }

    // analog of FileUtils.writeBytes, but not used Netbeans FileObject output stream for optimization (>2 times)
    public static void writeString(File file, String chars, String encoding) throws IOException {
        byte[] bytes = chars.getBytes(encoding);
        writeBytes(file, bytes);
    }

    public static String getTypeDisplayName(Definition definition, char[][] compoundName) {
        if (definition == null) {
            return "";
        } else {
            String prefix = definition.getQualifiedName();
            if (definition instanceof AbstractXmlDefinition) {
                int suffixStart = -1;
                if (compoundName != null) {
                    for (int i = 0; i < compoundName.length; i++) {
                        String component = String.valueOf(compoundName[i], 0, compoundName[i].length);
                        if (component.length() == 29 && component.startsWith("xsd") || component.startsWith("smd")) {
                            if (component.equals(definition.getId().toString())) {
                                if (i + 1 < compoundName.length && "impl".equals(String.valueOf(compoundName[i + 1], 0, compoundName[i + 1].length))) {
                                    suffixStart = i + 2;
                                } else {
                                    suffixStart = i + 1;
                                }
                                break;
                            }
                        }
                    }
                }
                if (suffixStart >= 0 && suffixStart < compoundName.length) {
                    prefix += ":";
                    for (int i = suffixStart; i < compoundName.length; i++) {
                        if (i > suffixStart) {
                            prefix += ".";
                        }
                        prefix += String.valueOf(compoundName[i], 0, compoundName[i].length);
                    }
                }
            }
            return "`" + prefix + "`";
        }
    }

    public static boolean isImmutable(ReferenceBinding binding, Map<String, Boolean> resultMap) {
        Map<String, MutabilityCheckResult> checkMap = new HashMap<>();
        return isImmutable(binding, checkMap, resultMap);
    }

    private static boolean isImmutable(ReferenceBinding binding, Map<String, MutabilityCheckResult> checkMap, Map<String, Boolean> resultMap) {
        String className = MutabilityCheckResult.getClassName(binding);
        Boolean flag = resultMap.get(className);
        if (flag != null) {
            return flag;
        }

        MutabilityCheckResult result = checkClassImmutable(binding, resultMap);

        checkMap.put(MutabilityCheckResult.getClassName(binding), result);

        if (result.mutable) {
            resultMap.put(className, false);
            return false;
        }

        result.checkSuperClassMutability(checkMap, resultMap);
        if (result.mutable) {
            resultMap.put(className, false);
            return false;
        }
        result.checkMemberTypesMutability(checkMap, resultMap);
        result.target = null;
        flag = !result.mutable;
        resultMap.put(className, flag);
        return flag;
    }

    public static class MutabilityCheckResult {

        boolean mutable;
        ReferenceBinding target;
        String mutabilityCause;
        private String className;

        public MutabilityCheckResult(ReferenceBinding target, String mutabilityCause) {
            this.mutable = true;
            this.target = target;
            this.mutabilityCause = mutabilityCause;
            className = getClassName(target);
        }

        public MutabilityCheckResult(ReferenceBinding target) {
            this.mutable = false;
            this.target = target;
            className = getClassName(target);
        }

        public boolean isMutable() {
            return mutable;
        }

        public boolean isValid() {
            return target != null;
        }

        public String getClassName() {
            return className;
        }

        public static String getClassName(ReferenceBinding binding) {
            return String.valueOf(CharOperation.concatWith(binding.compoundName, '.'));
        }

        private void checkSuperClassMutability(Map<String, MutabilityCheckResult> checkMap, Map<String, Boolean> resultMap) {

            if (target.superclass() != null) {
                final String className = getClassName(target.superclass());
                Boolean flag = resultMap.get(className);
                if (flag != null) {
                    if (!flag) {
                        mutable = true;
                        mutabilityCause = "mutable super type '" + className + "'";
                    } else {
                        return;
                    }
                } else {
                    if ("org/radixware/kernel/server/meta/clazzes/IRadPropReadWriteAccessor".equals(className)
                            || "org/radixware/kernel/server/meta/clazzes/IRadPropReadAccessor".equals(className)) {
                        resultMap.put(className, true);
                        return;

                    }
                }

                MutabilityCheckResult result = checkMap.get(className);
                if (result == null) {
                    isImmutable(target.superclass(), checkMap, resultMap);
                    result = checkMap.get(className);
                } else {
                    if (result.mutable) {
                        mutable = true;
                        mutabilityCause = "mutable member type '" + className + "'";
                    }
                    return;
                }
                flag = resultMap.get(className);

                if (!flag) {
                    mutable = true;
                    mutabilityCause = "mutable super type '" + String.valueOf(CharOperation.concatWith(target.superclass().compoundName, '.')) + "'";
                }
            }
        }

        private void checkMemberTypesMutability(Map<String, MutabilityCheckResult> checkMap, Map<String, Boolean> resultMap) {
            ReferenceBinding[] members = target.memberTypes();
            if (members != null) {
                for (int i = 0; i < members.length; i++) {

                    final String className = getClassName(members[i]);
                    Boolean flag = resultMap.get(className);
                    if (flag != null) {
                        if (!flag) {
                            mutable = true;
                            mutabilityCause = "mutable super type '" + className + "'";
                        }
                    }
                    MutabilityCheckResult result = checkMap.get(className);
                    if (result == null) {
                        isImmutable(members[i], checkMap, resultMap);
                    } else {
                        if (!members[i].isStatic()) {
                            if (result.mutable) {
                                mutable = true;
                                mutabilityCause = "mutable member type '" + String.valueOf(members[i].sourceName) + "'";
                                break;
                            }
                        }
                    }
                    flag = resultMap.get(className);

                    if (!flag) {
                        if (!members[i].isStatic()) {
                            mutable = true;
                            mutabilityCause = "mutable member type '" + String.valueOf(members[i].sourceName) + "'";
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return mutable ? className + ",  mutability cause: " + mutabilityCause : className;
        }
    }

    private static MutabilityCheckResult checkClassImmutable(ReferenceBinding binding, Map<String, Boolean> resultMap) {
        String className;
        boolean result = true;

        if (binding == null) {
            return new MutabilityCheckResult(null, "invalid binding. Unable to comute mutability");
        } else {

            className = String.valueOf(CharOperation.concatWith(binding.compoundName, '.'));
            if (!binding.isInterface()) {
                FieldBinding[] fields = binding.fields();
                if (fields == null) {
                    return new MutabilityCheckResult(binding, "invalid binding. Unable to comute mutability");
                }
                loop:
                for (int i = 0; i < fields.length; i++) {
                    FieldBinding field = fields[i];
                    if (field == null) {
                        return new MutabilityCheckResult(binding, "invalid binding. Unable to comute mutability");
                    }
                    if (field.isStatic()) {
                        if ((field.getAccessFlags() & ClassFileConstants.AccEnum) == 0 && (field.getAccessFlags() & ClassFileConstants.AccSynthetic) == 0) {
                            //enum constant immutable
                            TypeBinding fieldType = field.type;
                            if (fieldType == null) {
                                return new MutabilityCheckResult(binding, "invalid binding. Unable to comute mutability");
                            }
                            if (!field.isFinal()) {
                                boolean ignore = false;
                                if (String.valueOf(field.name).equals("serialVersionUID") && fieldType == BaseTypeBinding.LONG) {
                                    ignore = true;
                                }
                                if (!ignore) {
                                    return new MutabilityCheckResult(binding, "mutable static field '" + String.valueOf(field.name) + "'");
                                }
                            }

                            if (!fieldType.isBaseType()) {
                                char[] name = fieldType.constantPoolName();
                                String asString = String.valueOf(name);
                                if ("java/lang/String".equals(asString)
                                        || "java/math/BigDecimal".equals(asString)
                                        || "org/radixware/kernel/common/types/Id".equals(asString)
                                        || "org/radixware/kernel/common/environment/IRadixEnvironment".equals(asString)
                                        || "org/radixware/kernel/common/meta/RadEnumDef".equals(asString)) {
                                    continue;
                                }
                                if ("org/radixware/kernel/server/meta/clazzes/IRadPropReadWriteAccessor".equals(asString)
                                        || "org/radixware/kernel/server/meta/clazzes/IRadPropReadAccessor".equals(asString)) {
                                    continue;
                                }
                                if (String.valueOf(field.name).equals("EMPTY_THROWABLE_ARRAY") && "java.lang.Throwable".equals(className)) {
                                    continue;
                                }
                                if ("javax/xml/namespace/QName".equals(asString)) {
                                    int dollarIdx = CharOperation.indexOf('$', field.name);
                                    if (dollarIdx > 0) {
                                        boolean nameMatches = true;
                                        for (int c = dollarIdx + 1; c < field.name.length; c++) {
                                            if (!Character.isDigit(field.name[c])) {
                                                nameMatches = false;
                                            }
                                        }
                                        if (nameMatches) {
                                            //detect this is ADS xml  definition
                                            for (int n = 0; n < binding.compoundName.length; n++) {
                                                String part = String.valueOf(binding.compoundName[n]);
                                                if (part.startsWith("mdl") && part.length() == 29) {//start of module
                                                    if (n + 3 < binding.compoundName.length) {
                                                        String env = String.valueOf(binding.compoundName[n + 1]);
                                                        if ("common".equals(env) || "server".equals(env)) {
                                                            String id = String.valueOf(binding.compoundName[n + 2]);
                                                            if (id.startsWith("xsd") || id.startsWith("smd")) {
                                                                String impl = String.valueOf(binding.compoundName[n + 3]);
                                                                if ("impl".equals(impl)) {
                                                                    continue loop;
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                                return new MutabilityCheckResult(binding, "mutable static field '" + String.valueOf(field.name) + "'");
                            }
                        }
                    }
                }
            }
        }
        return new MutabilityCheckResult(binding);
    }
}
