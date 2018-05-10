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
package org.radixware.kernel.common.builder.check.dds;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.repository.dds.DdsAadcTransform;
import org.radixware.kernel.common.repository.dds.DdsAadcTransformColumn;
import org.radixware.kernel.common.repository.dds.DdsAadcTransformTable;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.utils.Utils;

@RadixObjectCheckerRegistration
public class DdsScriptsChecker extends RadixObjectChecker<DdsScripts> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsScripts.class;
    }
    private static final boolean DEBUG = false;

    @Override
    public void check(DdsScripts scripts, IProblemHandler problemHandler) {
        super.check(scripts, problemHandler);

        Map<String, DdsUpdateInfo> fileName2Script = null;
        Map<String, DdsUpdateInfo> fileName2ReverseScript = null;
        if (scripts.isInBranch() && (scripts.getBranch().getType() == ERepositoryBranchType.OFFSHOOT || DEBUG)) {
            fileName2Script = new HashMap<>();
            fileName2ReverseScript = new HashMap<>();
        }
        for (DdsUpdateInfo updateInfo : scripts.getUpdatesInfo()) {
            if (updateInfo.findScript() == null) {
                final String updateFileName = String.valueOf(updateInfo.getUpdateFileName());
                error(scripts, problemHandler, "Scripts.xml referenced to nonexisted update file '" + updateFileName + "'");
            }
            if (updateInfo.isReverse()) {
                if (fileName2ReverseScript != null) {
                    String name = updateInfo.getStraightUpdateFileName();
                    if (name == null || name.isEmpty()) {
                        error(updateInfo, problemHandler, "No source update file name specified for reverse script " + updateInfo.getUpdateFileName());
                    } else {
                        fileName2ReverseScript.put(name, updateInfo);
                    }
                }
            } else {
                if (fileName2Script != null) {
                    fileName2Script.put(updateInfo.getUpdateFileName(), updateInfo);
                }
            }
        }

        String prevUpdateFileName = "0";
        for (DdsUpdateInfo updateInfo : scripts.getUpdatesInfo()) {
            final String updateFileName = updateInfo.getUpdateFileName();
            try {
                if (Utils.equals(prevUpdateFileName, updateFileName)) {
                    error(scripts, problemHandler, "Duplicate update file name: '" + prevUpdateFileName + "'");
                } else {
                    if (Utils.compareVersions(prevUpdateFileName, updateFileName) >= 0) {
                        error(scripts, problemHandler, "Upgrade files registered out of order: '" + prevUpdateFileName + "'=>'" + updateFileName + "'");
                    }
                }
            } catch (NumberFormatException ex) {
                error(scripts, problemHandler, "Wrong upgrade file name format: '" + ex.getMessage() + "'. Must be numeric, dot separated.");
                break;
            }
            prevUpdateFileName = updateFileName;
        }

        for (DdsUpdateInfo updateInfo : scripts.getUpdatesInfo()) {
            for (DdsUpdateInfo.BaseLayerInfo baseLayerInfo : updateInfo.getBaseLayersInfo()) {
                if (baseLayerInfo.findBaseLayer() == null) {
                    final String uri = String.valueOf(baseLayerInfo.getUri());
                    error(scripts, problemHandler, "Scripts.xml referenced to nonexisted base layer '" + uri + "'");
                } else {
                    final String lastUpdateFileName = baseLayerInfo.getLastUpdateFileName();

                    if (lastUpdateFileName != null && !lastUpdateFileName.isEmpty()) {
                        final DdsScript script = baseLayerInfo.findLastUpdateScript();
                        if (script == null) {
                            final String uri = String.valueOf(baseLayerInfo.getUri());
                            error(scripts, problemHandler, "Scripts.xml referenced to nonexisted update file '" + lastUpdateFileName + "' in layer '" + uri + "'");
                        } else {
                            if (!updateInfo.isReverse()) {
                                final DdsUpdateInfo baseUpdateInfo = script.getOwnerDatabaseScripts().getOwnerScripts().getUpdatesInfo().findByUpdateFileName(script.getFileBaseName());
                                if (baseUpdateInfo != null && baseUpdateInfo.isReverse()) {
                                    warning(scripts, problemHandler, "Update file " + updateInfo.getUpdateFileName() + " should not depend from reverse update file " + baseUpdateInfo.getUpdateFileName());
                                }
                            }
                        }
                    }
                }
                checkDdsAadcTransform(updateInfo, problemHandler);
            }
        }
        if (fileName2Script != null && fileName2ReverseScript != null) {
            for (DdsUpdateInfo info : fileName2Script.values()) {
                DdsUpdateInfo reverse = fileName2ReverseScript.get(info.getUpdateFileName());
                if (reverse == null) {
                    error(info, problemHandler, "Reverse script not found for update script " + info.getUpdateFileName());
                }
            }
            for (DdsUpdateInfo info : fileName2ReverseScript.values()) {
                DdsUpdateInfo straight = fileName2Script.get(info.getStraightUpdateFileName());
                if (straight == null) {
                    error(info, problemHandler, "Source script not found for reverse script " + info.getUpdateFileName());
                }
            }
        }
    }
    
    private void checkDdsAadcTransform(DdsUpdateInfo info, IProblemHandler problemHandler){
        DdsAadcTransform transform = info.getDdsAadcTransform();
        if (!transform.isRequired()) {
            return;
        }
        if (transform.getWarnings().isEmpty() && transform.getTables().isEmpty()) {
            error(transform, problemHandler, "AADC transform for " + info.getUpdateFileName() + " script must contains table or warning");
        } else {
            if (transform.getTables().isEmpty()) {
                return;
            }
            for (DdsAadcTransformTable table : transform.getTables()) {
                if (table.findTable() == null) {
                    error(table, problemHandler, "AADC transform table #" + table.getTargetId() + " not found");
                }
                for (DdsAadcTransformColumn column : table.getColumns()) {
                    if (column.findColumn()== null) {
                        error(column, problemHandler, "AADC transform column #" + column.getTargetId() + " not found");
                    }
                }
            }
        }
    }
}
