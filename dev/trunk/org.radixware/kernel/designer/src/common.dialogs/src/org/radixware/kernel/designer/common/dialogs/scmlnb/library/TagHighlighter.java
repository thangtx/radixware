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
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.api.editor.settings.FontColorSettings;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;

/**
 * Highlighting for tags
 *
 */
public class TagHighlighter {

    private static final AttributeSet UNKNOWN_TAG_COLORS = AttributesUtilities.createImmutable(
            StyleConstants.Foreground, new Color(200, 200, 250),
            StyleConstants.Underline, new Color(200, 200, 250));
    private final OffsetsBag bag;
    private final Document doc;
    private final String mimeType;
    private volatile FontColorSettings fcs;

    public TagHighlighter(final Document doc, final String mimeType) {
        this.mimeType = mimeType;
        this.doc = doc;
        bag = new OffsetsBag(doc);
        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent evt) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent evt) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent evt) {
                update();
            }
        });
        final TagMapper tagMapper = TagMapper.getInstance(doc);
        tagMapper.addListener(new TagMapperListener() {
            @Override
            public void tagRemoved(final TagBounds tagBounds) {
                update();
            }

            @Override
            public void tagAdded(final TagBounds tagBounds) {
                update();
            }

            @Override
            public void tagUpdated(final TagBounds tagBounds) {
                update();
            }
        });
        final Lookup lookup = MimeLookup.getLookup(mimeType);
        final Result<FontColorSettings> lookupResult = lookup.lookupResult(FontColorSettings.class);
        fcs = lookupResult.allInstances().iterator().next();
        lookupResult.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(final LookupEvent ev) {
                fcs = lookupResult.allInstances().iterator().next();
            }
        });
    }

    public String getMimeType() {
        return mimeType;
    }

    private void update() {
        if (isInAtomicEdit()) {
            return;
        }
        final OffsetsBag newBag = new OffsetsBag(doc);
        final TagMapper tagMapper = TagMapper.getInstance(doc);
        AttributeSet attributes = null;
        if (tagMapper != null) {
            ((ScmlDocument) doc).readLock();
            try {
                final Set<TagBounds> bounds = tagMapper.getBounds();
                final TokenSequence ts = TokenHierarchy.get(doc).tokenSequence();
                for (TagBounds tagBounds : bounds) {
                    //attributes from xml settings
                    attributes = fcs.getTokenFontColors(tagBounds.getVTag().getTokenName());
                    if (attributes == null) {
                        attributes = UNKNOWN_TAG_COLORS;
                    } else {
                        //if tag is in comment, use comment colors
                        if (ts != null) {
                            ts.move(tagBounds.getBeginOffset());
                            if (ts.moveNext()) {
                                if (ts.token().id().primaryCategory() != null && ts.token().id().primaryCategory().contains("comment")) {
                                    attributes = fcs.getTokenFontColors("comment");
                                }
                            }
                        }
                        final AttributeSet underlinedAttr = AttributesUtilities.createImmutable(
                                StyleConstants.Underline, attributes.getAttribute(StyleConstants.Foreground));
                        attributes = AttributesUtilities.createImmutable(attributes, underlinedAttr);
                    }

                    attributes = tagBounds.getVTag().updateAttributesBeforeRender(attributes);

                    newBag.addHighlight(
                            tagBounds.getBeginOffset(),
                            tagBounds.getEndOffset(),
                            attributes);
                }
            } finally {
                ((ScmlDocument) doc).readUnlock();
            }
        }
        bag.setHighlights(newBag);
    }

    private boolean isInAtomicEdit() {
        if (doc.getProperty(ScmlDocument.REBUILD_IN_PROGRESS) != null
                || doc.getProperty(ScmlDocument.UPDATE_IN_PROGRESS) != null
                || doc.getProperty(ScmlDocument.ATOMIC_EDIT_IN_PROGRESS) != null) {
            return true;
        }
        return false;
    }

    public OffsetsBag getHighlightsBag() {
        update();
        return bag;
    }
}
