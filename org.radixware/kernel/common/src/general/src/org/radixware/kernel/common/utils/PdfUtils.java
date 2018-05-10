/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.utils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.awt.Color;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author akrylov
 */
public class PdfUtils {

//    public static void main(String[] args) throws IOException {
//        //  applyWatermarkText(new FileInputStream(new File("/home/akrylov/pdfsrc.pdf")), "AAA", "Courier", 15, false, false, Color.red, 45, new FileOutputStream(new File("/home/akrylov/pdfsrc1.pdf")));
//        applyWatermarkImage(new FileInputStream(new File("/home/akrylov/pdfsrc.pdf")), new FileInputStream(new File("/home/akrylov/path3012.png")), new FileOutputStream(new File("/home/akrylov/pdfsrc1.pdf")));
//    }

    public static void applyWatermarkText(InputStream pdfIn, String text, String font, int fontSize, boolean bold, boolean italic, Color color,float opacity, float rotation, OutputStream pdfOut) throws IOException {
        try {
            PdfReader reader = new PdfReader(pdfIn);
            PdfStamper stamper = new PdfStamper(reader, pdfOut);

            int pagesCount = reader.getNumberOfPages();
            for (int i = 1; i <= pagesCount; i++) {
                Rectangle pageSize = reader.getPageSize(i);
                PdfContentByte over = stamper.getOverContent(i);

                int style = 0;
                if (bold) {
                    style |= Font.BOLD;
                }
                if (italic) {
                    style |= Font.ITALIC;
                }

                Font f = FontFactory.getFont(font, "UTF-8", fontSize, style);
                f.setColor(color);
                Phrase p = new Phrase(text, f);
                over.saveState();
                PdfGState gs1 = new PdfGState();
                gs1.setFillOpacity(opacity);
                over.setGState(gs1);
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, pageSize.getWidth() / 2, pageSize.getHeight() / 2, 45);
                over.restoreState();
            }
            stamper.close();
            reader.close();

        } catch (DocumentException ex) {
            throw new IOException(ex);
        }
    }

    public static void applyWatermarkImage(InputStream pdfIn, InputStream imageIn, OutputStream pdfOut) throws IOException {
        try {
            PdfReader reader = new PdfReader(pdfIn);
            PdfStamper stamper = new PdfStamper(reader, pdfOut);

            int pagesCount = reader.getNumberOfPages();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileUtils.copyStream(imageIn, out);
            for (int i = 1; i <= pagesCount; i++) {
                Rectangle pageSize = reader.getPageSize(i);
                PdfContentByte over = stamper.getOverContent(i);
                Image image = Image.getInstance(out.toByteArray());
                image.setAbsolutePosition((pageSize.getWidth() - image.getScaledWidth()) / 2, (pageSize.getHeight() - image.getScaledHeight()) / 2);
                over.addImage(image);
            }
            stamper.close();
            reader.close();

        } catch (DocumentException ex) {
            throw new IOException(ex);
        }
    }
}
