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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EReportCellType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

public class AdsReportImageCell extends AdsReportCell {

    private Id imageId = null;

    protected AdsReportImageCell() {
        super();
    }

    @Override
    public EReportCellType getCellType() {
        return EReportCellType.IMAGE;
    }

    protected AdsReportImageCell(org.radixware.schemas.adsdef.ReportCell xCell) {
        super(xCell);

        if (xCell.isSetImageId()) {
            imageId = xCell.getImageId();
        }
    }

    protected AdsReportImageCell(org.radixware.schemas.adsdef.ReportBand.Cells.Cell xCell) {
        super(xCell);

        if (xCell.isSetImageId()) {
            imageId = xCell.getImageId();
        }
    }

    @Override
    protected void appendTo(org.radixware.schemas.adsdef.ReportCell xCell, ESaveMode saveMode) {
        super.appendTo(xCell, saveMode);

        if (imageId != null) {
            xCell.setImageId(imageId);
        }
    }

    public Id getImageId() {
        return imageId;
    }

    public void setImageId(Id imageId) {
        if (!Utils.equals(this.imageId, imageId)) {
            this.imageId = imageId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public AdsImageDef findImage() {
        final AdsReportClassDef report = getOwnerReport();
        if (report != null) {
            final DefinitionSearcher<AdsImageDef> imageSearcher = AdsSearcher.Factory.newImageSearcher(report);
            return imageSearcher.findById(imageId).get();
        } else {
            return null;
        }
    }

    public AdsImageDef getImage() {
        final AdsImageDef image = findImage();
        if (image != null) {
            return image;
        } else {
            throw new DefinitionNotFoundError(imageId);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.REPORT_IMAGE_CELL;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        final AdsImageDef image = findImage();
        if (image != null) {
            list.add(image);
        }
    }

    @Override
    public boolean isModeSupported(AdsReportForm.Mode mode) {
        return mode == AdsReportForm.Mode.GRAPHICS;
    }
}