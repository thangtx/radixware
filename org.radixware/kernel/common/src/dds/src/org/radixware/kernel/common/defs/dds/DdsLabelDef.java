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

package org.radixware.kernel.common.defs.dds;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.utils.Utils;

/**
 * Комментарий на диаграмме {@link DdsModelDef DDS модели}.
 */
public class DdsLabelDef extends DdsDefinition implements IPlacementSupport {

    protected DdsLabelDef() {
        super(EDefinitionIdPrefix.DDS_LABEL, "");
        this.placement = DdsDefinitionPlacement.Factory.newInstance(this);
        this.font = new Font();
    }

    protected DdsLabelDef(org.radixware.schemas.ddsdef.Label xLabel) {
        super(xLabel);

        this.font = new Font(xLabel.getFont());
        this.text = xLabel.getText();
        this.placement = DdsDefinitionPlacement.Factory.loadFrom(this, xLabel.getPlacement());
        //this.width = xLabel.getPlacement().getWidth();
        //this.height = xLabel.getPlacement().getHeight();
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CONST;
    }

    @Override
    public String getName() {
        return LABEL_TYPE_TITLE;
    }

    /**
     * Шрифт {@link DdsLabelDef надписи}.
     */
    public class Font {

        protected Font() {
        }

        protected Font(org.radixware.schemas.ddsdef.Label.Font xFont) {
            this.bold = xFont.getBold();
            this.italic = xFont.getItalic();
            this.sizePx = xFont.getSize();
        }
        private boolean bold = false;

        /**
         * Является-ли шрифт жирным.
         */
        public boolean isBold() {
            return bold;
        }

        public void setBold(boolean bold) {
            if (this.bold != bold) {
                this.bold = bold;
                setEditState(EEditState.MODIFIED);
            }
        }
        private boolean italic = false;

        /**
         * Является-ли шрифт наклонным.
         */
        public boolean isItalic() {
            return italic;
        }

        public void setItalic(boolean italic) {
            if (this.italic != italic) {
                this.italic = italic;
                setEditState(EEditState.MODIFIED);
            }
        }
        private int sizePx = 14;

        /**
         * Получить размер шрифта в пикселях.
         */
        public int getSizePx() {
            return sizePx;
        }

        public void setSizePx(int sizePx) {
            if (this.sizePx != sizePx) {
                this.sizePx = sizePx;
                setEditState(EEditState.MODIFIED);
            }
        }
    }
    private final Font font;

    /**
     * Получить информацию о шрифте, которым рисуется надпись.
     */
    public Font getFont() {
        return font;
    }
    private final DdsDefinitionPlacement placement;

    @Override
    public DdsDefinitionPlacement getPlacement() {
        return placement;
    }
    private String text = "";
//    private int width = 200;
//
//    public int getWidth() {
//        return width;
//    }
//
//    public void setWidth(int width) {
//        if (this.width != width) {
//            this.width = width;
//            setEditState(EEditState.MODIFIED);
//        }
//    }
//    private int height = 50;
//
//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        if (this.height != height) {
//            this.height = height;
//            setEditState(EEditState.MODIFIED);
//        }
//    }

    /**
     * Get a human-readable multiline text of label. 
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        synchronized (this) {
            if (!Utils.equals(this.text, text)) {
                this.text = text;
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsLabelDef newInstance() {
            return new DdsLabelDef();
        }

        public static DdsLabelDef loadFrom(org.radixware.schemas.ddsdef.Label xLabel) {
            return new DdsLabelDef(xLabel);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.LABEL;
    }

    private class DdsLabelClipboardSupport extends DdsClipboardSupport<DdsLabelDef> {

        public DdsLabelClipboardSupport() {
            super(DdsLabelDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Label xLabel = org.radixware.schemas.ddsdef.Label.Factory.newInstance();
            DdsModelWriter.writeLabel(DdsLabelDef.this, xLabel);
            return xLabel;
        }

        @Override
        protected DdsLabelDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Label xLabel = (org.radixware.schemas.ddsdef.Label) xmlObject;
            return DdsLabelDef.Factory.loadFrom(xLabel);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsLabelDef> getClipboardSupport() {
        return new DdsLabelClipboardSupport();
    }
    public static final String LABEL_TYPE_TITLE = "Label";
    public static final String LABEL_TYPES_TITLE = "Labels";

    @Override
    public String getTypeTitle() {
        return LABEL_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return LABEL_TYPES_TITLE;
    }
}
