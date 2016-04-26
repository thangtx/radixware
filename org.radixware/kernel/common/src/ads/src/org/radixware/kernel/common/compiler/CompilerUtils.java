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
import java.util.Collection;
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
}
