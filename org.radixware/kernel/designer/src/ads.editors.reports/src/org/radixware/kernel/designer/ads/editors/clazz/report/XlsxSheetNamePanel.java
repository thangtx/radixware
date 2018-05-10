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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import net.miginfocom.swing.MigLayout;
import org.apache.poi.ss.util.WorkbookUtil;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;

public class XlsxSheetNamePanel extends StateAbstractDialog.StateAbstractPanel {

    private static final int MAX_SHEET_NAME_LENGTH = 31;
    private AdsReportClassDef report;

    private final HandleInfo sheetNameHandleInfo = new HandleInfo() {
        private EIsoLanguage checkedLang = null;

        @Override
        public AdsDefinition getAdsDefinition() {
            return report;
        }

        @Override
        public Id getTitleId() {
            return report.getXlsxReportInfo().getSheetNameId();
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef stringDef) {
            if (stringDef != null) {
                report.getXlsxReportInfo().setSheetNameId(stringDef.getId());
                check();
            } else {
                report.getXlsxReportInfo().setSheetNameId(null);
                stateManager.ok();
            }
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            IMultilingualStringDef stringDef = getAdsMultilingualStringDef();
            if (stringDef != null) {
                stringDef.setValue(language, newStringValue);
                check();
            }
        }
    };

    private final StateDisplayer stateDisplayer = new StateDisplayer();
    private final LocalizingEditorPanel sheetNameEditor = new LocalizingEditorPanel("Sheet Name");

    public XlsxSheetNamePanel() {
        changeSupport = new ChangeSupport(this);

        this.setLayout(new MigLayout("fill", "[]", "[shrink][shrink]"));
        this.add(sheetNameEditor, "growx, shrinky, wrap");
        this.add(stateDisplayer, "shrinkx, shrinky");
    }

    public void open(AdsReportClassDef report) {
        this.report = report;
        sheetNameEditor.open(sheetNameHandleInfo);
        check();
    }

    private boolean checkSheetName(String sheetName) {
        try {
            WorkbookUtil.validateSheetName(sheetName);
        } catch (IllegalArgumentException e) {
            stateManager.error(formatErrorMessageString(e.getMessage(), sheetName));
            changeSupport.fireChange();
            return false;
        }

        stateManager.ok();
        changeSupport.fireChange();

        return true;
    }

    @Override
    public void check() {
        if (report.getXlsxReportInfo().getSheetNameId() != null) {
            if (sheetNameEditor.getLocalizingStringContext() != null && sheetNameEditor.getLocalizingStringContext().getValueMap() != null) {
                if (sheetNameEditor.getLocalizingStringContext().getValueMap().isEmpty()) {
                    stateManager.error("Sheet name must not be null");
                } else {                    
                    for (String sheetNameVariant : sheetNameEditor.getLocalizingStringContext().getValueMap().values()) {
                        if (!checkSheetName(sheetNameVariant)) {
                            break;
                        }
                    }
                }
            }
        } else {
            stateManager.ok();
        }
    }

    private String formatErrorMessageString(String message, String sheetName) {
        String result = message;
        if (sheetName.length() > MAX_SHEET_NAME_LENGTH) {
            String shortName = "'" + sheetName.substring(0, MAX_SHEET_NAME_LENGTH) + "...'";
            result = message.replaceFirst("'.*'", shortName);
        }

        if (result.startsWith("sheetName")) {
            result = result.replaceFirst("sheetName", "Sheet name");
        }

        return result;
    }
}
