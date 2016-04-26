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
    HTML_TEXT("text/html", "HTML", "html"),
    IMAGE_BMP("image/bmp", "BMP", "bmp"),
    IMAGE_GIF("image/gif", "GIF", "gif"),
    IMAGE_JPEG("image/jpeg", "JPEG", "jpg"),
    IMAGE_PNG("image/png", "PNG", "png"),
    IMAGE_PORTABLE_BITMAP("image/x-portable-bitmap", "PBM", "pbm"),
    IMAGE_PORTABLE_GRAY_MAP("image/x-portable-graymap", "PGM" ,"pgm"),
    IMAGE_PORTABLE_PIX_MAP("image/x-portable-pixmap", "PPM" ,"ppm"),
    IMAGE_SVG("image/svg+xml", "SVG" ,"svg"),
    IMAGE_TIFF("image/tiff", "TIFF", "tiff"),
    IMAGE_XBITMAP("image/x-xbitmap", "XBM", "xbm"),
    IMAGE_XPIX_MAP("image/x-xpixmap", "XMP" ,"xmp"),
    PDF("application/pdf", "PDF" ,"pdf"),
    PLAIN_TEXT("text/plain", "Text", "txt"),
    VIDEO_XMNG("video/x-mng", "MNG" ,"mng"),
    XML("text/xml", "XML", "xml"),
    BINARY("application/octet-stream", "Binary", "binary"),
    //MsOffice mime-types are taken from http://filext.com/faq/office_mime_types.php
    APP_MSOFFICE_DOCUMENT("application/msword", "Microsoft Word 97/2000/XP/2003", "doc"),
    APP_MSOFFICE_SPREADSHEET("application/vnd.ms-excel", "Microsoft Excel 97/2000/XP/2003", "xls"),
    APP_MSOFFICE_PRESENTATION("application/vnd.ms-powerpoint", "Microsoft PowerPoint 97/2000/XP/2003", "ppt"),
    APP_MSOFFICE_DOCUMENT_X("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Office Open XML Text" ,"docx"),
    APP_MSOFFICE_SPREADSHEET_X("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Office Open XML Spreadsheet","xlsx"),
    APP_MSOFFICE_PRESENTATION_X("application/vnd.openxmlformats-officedocument.presentationml.presentation", "Office Open XML Presentation","pptx"),
    //OpenOffice mime-types are taken from http://framework.openoffice.org/documentation/mimetypes/mimetypes.html
    APP_OPEN_OFFICE_DOCUMENT("application/vnd.oasis.opendocument.text ", "ODF Text Document","odt"),
    APP_OPEN_OFFICE_PRESENTATION("application/vnd.oasis.opendocument.presentation", "ODF Presentation","odp"),
    APP_OPEN_OFFICE_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet", "ODF Spreadsheet","ods"),
    APP_RICH_TEXT("application/rtf", "Rich Text","rtf"),
    APP_ZIP("application/zip", "ZIP","zip");
            
    private final String value;
    private final String title;
    private final String ext;

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

    private EMimeType(final String mimeType, final String title, final String fileExtention) {
        value = mimeType;
        this.title = title;
        this.ext = fileExtention;
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
