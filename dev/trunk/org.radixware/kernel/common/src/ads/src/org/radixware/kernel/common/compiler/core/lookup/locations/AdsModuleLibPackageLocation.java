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

package org.radixware.kernel.common.compiler.core.lookup.locations;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.SERVER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsModuleLibPackageLocation extends AdsModulePackageLocation {

    private char[][] entryNames;

    public AdsModuleLibPackageLocation(AdsModule module, ERuntimeEnvironmentType env) {
        super(module, env);
    }

    @Override
    public boolean containsPackageName(char[][] packageName) {
        if (!prefixEquals(packageName)) {
            return false;
        }
        if (entryNames == null) {
            File binDir = new File(module.getBinDirContainer(), "bin");
            String[] files;
            switch (env) {
                case COMMON:
                    files = new String[]{"common.jar"};
                    break;
                case SERVER:
                    files = new String[]{"common.jar", "server.jar"};
                    break;
                case COMMON_CLIENT:
                    files = new String[]{"common.jar", "common-client.jar"};
                    break;
                case EXPLORER:
                    files = new String[]{"common.jar", "common-client.jar", "explorer.jar"};
                    break;
                case WEB:
                    files = new String[]{"common.jar", "common-client.jar", "web.jar"};
                    break;
                default:
                    files = new String[]{};
            }
            List<String> names = new LinkedList<>();
            for (String fileName : files) {
                File jar = new File(binDir, fileName);
                try {
                    ZipFile zip = new ZipFile(jar);
                    Enumeration<? extends ZipEntry> entries = zip.entries();

                    while (entries.hasMoreElements()) {
                        ZipEntry e = entries.nextElement();
                        if (e.isDirectory()) {
                            names.add(e.getName());
                        }
                    }

                } catch (ZipException ex) {
                } catch (IOException ex) {
                }
            }
            entryNames = new char[names.size()][];
            for (int i = 0; i < entryNames.length; i++) {
                entryNames[i] = names.get(i).toCharArray();
            }
        }
        char[] name = CharOperations.merge(entryNames, '/');
        for (char[] entry : entryNames) {
            if (CharOperations.equals(name, entry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NameEnvironmentAnswer findAnswer(char[][] packageName, char[][] typeName) {
        return null;
    }
}
