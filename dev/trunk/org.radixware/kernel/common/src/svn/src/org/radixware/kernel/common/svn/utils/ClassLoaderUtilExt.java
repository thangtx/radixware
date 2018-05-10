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

import java.util.List;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;

/**
 *
 * @author npopov
 */
class ClassLoaderUtilExt extends org.radixware.kernel.common.lang.ClassLoaderUtil {

    static byte[] findClassBytesBySlashSeparatedName(String name, Utils.LayerInfo layerInfo, ERuntimeEnvironmentType env) throws ClassNotFoundException {

        byte[] result = layerInfo.getLib(env).findClassBytesBySlashSeparatedName(name);
        if (result != null) {
            return result;
        } else {
            result = findClassBytesBySlashSeparatedName(layerInfo, name, env);
            if (result != null) {
                return result;
            }
        }

        if (env != ERuntimeEnvironmentType.COMMON) {
            result = layerInfo.getLib(ERuntimeEnvironmentType.COMMON).findClassBytesBySlashSeparatedName(name);
            if (result != null) {
                return result;
            } else {
                result = findClassBytesBySlashSeparatedName(layerInfo, name, ERuntimeEnvironmentType.COMMON);
                if (result != null) {
                    return result;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    private static byte[] findClassBytesBySlashSeparatedName(org.radixware.kernel.common.svn.utils.Utils.LayerInfo layerInfo, String name, ERuntimeEnvironmentType env) {
        List<ClassLinkageAnalyzer.LayerInfo> prevs = layerInfo.findPrevLayer();
        for (final ClassLinkageAnalyzer.LayerInfo prev : prevs) {
            final org.radixware.kernel.common.svn.utils.Utils.LayerInfo info = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) prev;
            try {
                byte[] result = info.getLib(env).findClassBytesBySlashSeparatedName(name);
                if (result != null) {
                    return result;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        for (final ClassLinkageAnalyzer.LayerInfo prev : prevs) {
            final org.radixware.kernel.common.svn.utils.Utils.LayerInfo info = (org.radixware.kernel.common.svn.utils.Utils.LayerInfo) prev;
            byte[] result = findClassBytesBySlashSeparatedName(info, name, env);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
