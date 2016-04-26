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

package org.radixware.kernel.designer.common.editors.jml.vtag;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml;

import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;


public abstract class VJmlTag<T extends Jml.Tag> extends ScmlEditorPane.VTag<T> {

    public VJmlTag(T tag) {
        super(tag);
    }

    @Override
    public String getTitle() {
        return getTag().getDisplayName();
    }

    @Override
    public AttributeSet updateAttributesBeforeRender(AttributeSet attributes) {
        attributes = super.updateAttributesBeforeRender(attributes);
        //if tag is pointing to deprecated definition, use strike through
        final Definition adsDef = getAdsDefinition(getTag());
        if (adsDef != null && adsDef.isDeprecated()) {
            final AttributeSet strikeThroughAttr = AttributesUtilities.createImmutable(
                    StyleConstants.StrikeThrough, attributes.getAttribute(StyleConstants.Foreground));
            attributes = AttributesUtilities.createImmutable(attributes, strikeThroughAttr);
        }

        return attributes;
    }

    private Definition getAdsDefinition(final Scml.Tag tag) {
        Jml.Tag jmlTag = null;
        if (tag instanceof Jml.Tag) {
            jmlTag = (Jml.Tag) tag;
        } else {
            return null;
        }
        final Jml jml = jmlTag.getOwnerJml();

        if (jmlTag instanceof JmlTagTypeDeclaration) {
            final JmlTagTypeDeclaration typeTag = (JmlTagTypeDeclaration) jmlTag;
            final AdsType type = typeTag.getType().resolve(jml.getOwnerDef()).get();
            if (type instanceof AdsDefinitionType) {
                return ((AdsDefinitionType) type).getSource();
            }
        }

        if (jmlTag instanceof JmlTagId) {
            final Definition def = ((JmlTagId) tag).resolve(jml.getOwnerDef());
            if (def instanceof AdsDefinition) {
                return (AdsDefinition) def;
            }
        }

        return null;
    }

    @Override
    public String getToolTip() {
        return getTag().getToolTip();
    }
}
