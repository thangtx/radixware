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
package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class AdsReportTextCell extends AdsReportCell implements ILocalizedDef {

    private Id textId = null;

    /**
     * Creates a new AdsReportTextCell.
     */
    protected AdsReportTextCell() {
    }

    protected AdsReportTextCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);

        if (xCell.isSetTextId()) {
            this.textId = xCell.getTextId();
        }
    }

    protected AdsReportTextCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);

        if (xCell.isSetTextId()) {
            this.textId = xCell.getTextId();
        }
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);

        if (textId != null) {
            xCell.setTextId(textId);
        }
    }

    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return true;
    }

    public AdsMultilingualStringDef findText() {
        return findLocalizedString(textId);
    }

    public AdsMultilingualStringDef getText() {
        final AdsMultilingualStringDef text = findText();
        if (text != null) {
            return text;
        } else {
            throw new DefinitionNotFoundError(textId);
        }
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.TEXT;
    }

    public Id getTextId() {
        return textId;
    }

    public void setTextId(Id textId) {
        if (!Utils.equals(this.textId, textId)) {
            this.textId = textId;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_TEXT_CELL;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final AdsMultilingualStringDef text = findText();
        if (text != null) {
            list.add(text);
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(AdsReportTextCell.this) {
            @Override
            public Id getId() {
                return textId;
            }

            @Override
            public void updateId(Id newId) {
                textId = newId;
                setEditState(EEditState.MODIFIED);
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PRIVATE;
            }

            @Override
            public String getContextDescription() {
                return "Report Cell Text";
            }

            @Override
            public boolean isPublished() {
                return false;
            }
        });
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        if (stringId != null) {
            final AdsReportClassDef report = getOwnerReport();
            if (report != null) {
                return report.findLocalizedString(stringId);
            }
        }
        return null;
    }
}