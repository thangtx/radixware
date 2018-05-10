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
package org.radixware.kernel.designer.ads.common.reports;

import java.util.prefs.Preferences;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class ReportPreviewSettings {
    
    private static final String PREFS_KEY = "ReportPreviewSettings";
    
    public static final String PREVIEW_OUTPUT_NAME = "Preview Report";
    
    public static final ReportPreviewSettings AAS_ADDRESS = new ReportPreviewSettings("AasAddress");
    public static final ReportPreviewSettings AAS_REQUEST_TIMEOUT = new ReportPreviewSettings("AasRequestTimeout");    
    public static final ReportPreviewSettings EXPORT_LANGUAGE = new ReportPreviewSettings("ExportLanguage");
    public static final ReportPreviewSettings EXPORT_REGION = new ReportPreviewSettings("ExportRegion");
    public static final ReportPreviewSettings EXPORT_FORMAT = new ReportPreviewSettings("ExportFormat");
    public static final ReportPreviewSettings TEST_DATA_FOLDER_PATH = new ReportPreviewSettings("TestDataFolderPath");
    
    private static final Preferences PREFERENCES = Utils.findOrCreatePreferences(PREFS_KEY);
    
    private final String prefSuffix;

    private ReportPreviewSettings(String prefSuffix) {
        this.prefSuffix = prefSuffix;
    }

    public String get(Id reportId) {
        return get(reportId, "");
    }

    public String get(Id reportId, String defaultValue) {
        return PREFERENCES.get(reportId.toString() + prefSuffix, defaultValue);
    }
    
    public void set(Id reportId, String value) {
        PREFERENCES.put(reportId.toString() + prefSuffix, value);
    }        
}
