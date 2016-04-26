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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.api.editor.settings.EditorStyleConstants;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;


public class TagCompletionHighlighter {

    private final Document doc;
    private final String mimeType;
    private final OffsetsBag bag;
    private TagCompletionRegion completionRegion;
    private final AttributeSet singleCharRectangle;
    private final AttributeSet leftChar;
    private final AttributeSet middleChar;
    private final AttributeSet rightChar;
    private final Color borderColor;
    private boolean active;

    public TagCompletionHighlighter(final Document doc, String mimeType) {
        this.doc = doc;
        this.mimeType = mimeType;
        bag = new OffsetsBag(doc);
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                completionRegion = (TagCompletionRegion) doc.getProperty(TagCompletionRegion.class);
                repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                completionRegion = (TagCompletionRegion) doc.getProperty(TagCompletionRegion.class);
                repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        borderColor = Color.BLUE;
        leftChar = AttributesUtilities.createImmutable(EditorStyleConstants.LeftBorderLineColor, borderColor,
                EditorStyleConstants.TopBorderLineColor, borderColor,
                EditorStyleConstants.BottomBorderLineColor, borderColor);
        rightChar = AttributesUtilities.createImmutable(EditorStyleConstants.RightBorderLineColor, borderColor,
                EditorStyleConstants.TopBorderLineColor, borderColor,
                EditorStyleConstants.BottomBorderLineColor, borderColor);
        middleChar = AttributesUtilities.createImmutable(EditorStyleConstants.TopBorderLineColor, borderColor,
                EditorStyleConstants.BottomBorderLineColor, borderColor);
        singleCharRectangle = AttributesUtilities.createComposite(leftChar, rightChar);
    }

    public OffsetsBag getHighlightsBag() {
        return bag;
    }

    private void repaint() {
        if (completionRegion != null) {//draw border
            active = true;
            bag.clear();
            if (completionRegion.getRegionStart() == completionRegion.getRegionEnd() - 1) {//one letter
                bag.addHighlight(completionRegion.getRegionStart(), completionRegion.getRegionEnd(), singleCharRectangle);
            } else {
                bag.addHighlight(completionRegion.getRegionStart(), completionRegion.getRegionStart() + 1, leftChar);
                if (completionRegion.getRegionEnd() - completionRegion.getRegionStart() > 2) {
                    bag.addHighlight(completionRegion.getRegionStart() + 1, completionRegion.getRegionEnd() - 1, leftChar);
                }
                bag.addHighlight(completionRegion.getRegionEnd() - 1, completionRegion.getRegionEnd(), rightChar);
            }
        } else {
            if (active) {
                active = false;
                bag.clear();
            }
        }
    }
}
