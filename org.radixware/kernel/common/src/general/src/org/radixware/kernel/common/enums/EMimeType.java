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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EMimeType implements IKernelStrEnum {

    //constant values for compiling
    HTML_TEXT("text/html", "HyperText Markup Language (HTML)", "html", "(*.html *.htm)"),
    IMAGE_BMP("image/bmp", "Bitmap Image File", "bmp", "(*.bmp)"),
    IMAGE_GIF("image/gif", "Graphics Interchange Format (GIF)", "gif", "(*.gif)"),
    IMAGE_JPEG("image/jpeg", "JPEG Image", "jpg", "(*.jpg *.jpeg)"),
    IMAGE_PNG("image/png", "Portable Network Graphics (PNG)", "png", "(*.png)"),
    IMAGE_PORTABLE_BITMAP("image/x-portable-bitmap", "Portable Bitmap Format (PBM)", "pbm", "(*.pbm)"),
    IMAGE_PORTABLE_GRAY_MAP("image/x-portable-graymap", "Portable Graymap Format (PGM)" ,"pgm", "(*.pgm)"),
    IMAGE_PORTABLE_PIX_MAP("image/x-portable-pixmap", "Portable Pixmap Format (PPM)" ,"ppm", "(*.ppm)"),
    IMAGE_SVG("image/svg+xml", "Scalable Vector Graphics (SVG)" ,"svg", "(*.svg)"),
    IMAGE_TIFF("image/tiff", "Tagged Image File Format (TIFF)", "tiff", "(*.tiff)"),
    IMAGE_XBITMAP("image/x-xbitmap", "X BitMap", "xbm", "(*.xbm)"),
    IMAGE_XPIX_MAP("image/x-xpixmap", "X PixMap" ,"xpm", "(*.xpm)"),
    PDF("application/pdf", "Adobe Portable Document (PDF)" ,"pdf", "(*.pdf)"),
    PLAIN_TEXT("text/plain", "Text File", "txt", "(*.txt)"),
    CSV("text/csv", "CSV", "csv", "(*.csv)"),
    VIDEO_XMNG("video/x-mng", "MNG" ,"mng", "(*.mng)"),
    XML("text/xml", "XML Document", "xml", "(*.xml)"),
    BINARY("application/octet-stream", "Binary File", "binary", "(*.bin *.binary)"),
    //MsOffice mime-types are taken from http://filext.com/faq/office_mime_types.php
    APP_MSOFFICE_DOCUMENT("application/msword", "Microsoft Word 97/2000/XP/2003", "doc", "(*.doc)"),
    APP_MSOFFICE_SPREADSHEET("application/vnd.ms-excel", "Microsoft Excel 97/2000/XP/2003", "xls", "(*.xls)"),
    APP_MSOFFICE_PRESENTATION("application/vnd.ms-powerpoint", "Microsoft PowerPoint 97/2000/XP/2003", "ppt", "(*.ppt)"),
    APP_MSOFFICE_DOCUMENT_X("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Office Open XML Text" ,"docx", "(*.docx)"),
    APP_MSOFFICE_SPREADSHEET_X("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Office Open XML Spreadsheet","xlsx", "(*.xlsx)"),
    APP_MSOFFICE_PRESENTATION_X("application/vnd.openxmlformats-officedocument.presentationml.presentation", "Office Open XML Presentation","pptx","(*.pptx)"),
    //OpenOffice mime-types are taken from http://framework.openoffice.org/documentation/mimetypes/mimetypes.html
    APP_OPEN_OFFICE_DOCUMENT("application/vnd.oasis.opendocument.text ", "ODF Text Document","odt","(*.odt)"),
    APP_OPEN_OFFICE_PRESENTATION("application/vnd.oasis.opendocument.presentation", "ODF Presentation","odp","(*.odp)"),
    APP_OPEN_OFFICE_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet", "ODF Spreadsheet","ods","(*.ods)"),
    APP_RICH_TEXT("application/rtf", "Rich Text","rtf","(*.rtf)"),
    APP_ZIP("application/zip", "Zip Archive","zip","(*.zip)"),
    ALL_SUPPORTED("all","All Supported Formats","",""),
    ALL_FILES("any","All Files","*","(*.*)");
            
    private final String value;
    private final String title;
    private final String ext;
    private final String qtFilter;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

    public String getExt() {
        return ext;
    }
    
    public String getTitle(){
        return title;
    }

    public String getQtFilter() {
        return qtFilter;
    }        

    private EMimeType(final String mimeType, final String title, final String fileExtention, final String qtFilter) {
        value = mimeType;
        this.title = title;
        this.ext = fileExtention;
        this.qtFilter = qtFilter;
    }

    public static EMimeType getForValue(final String val) {
        for (EMimeType t : EMimeType.values()) {
            if (t.value.equals(val)) {
                return t;
            }
        }
        throw new NoConstItemWithSuchValueError("EMimeType has no item with value: " + String.valueOf(val), val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
