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
package org.radixware.kernel.designer.common.editors.mml;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.mml.MmlTagId;
import org.radixware.kernel.common.scml.Scml;

import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;

public abstract class VMmlTag<T extends Mml.Tag> extends ScmlEditorPane.VTag<T> {

    public VMmlTag(T tag) {
        super(tag);
    }

    @Override
    public String getTitle() {
        return getTag().getDisplayName();
    }

    @Override
    public String getToolTip() {
        return getTag().getToolTip();
    }

    // TODO: ??? copy by jml
    @Override
    public AttributeSet updateAttributesBeforeRender(AttributeSet attributes) {
        return super.updateAttributesBeforeRender(attributes);
    }

    // TODO: ???
    private Definition getAdsDefinition(final Scml.Tag tag) {
        Mml.Tag mmlTag = null;
        if (tag instanceof Mml.Tag) {
            mmlTag = (Mml.Tag) tag;
        } else {
            return null;
        }
        final Mml mml = mmlTag.getOwnerMml();

        if (mmlTag instanceof MmlTagId) {
            final Definition def = ((MmlTagId) tag).resolve(mml.getOwnerDef());
            if (def instanceof AdsDefinition) {
                return (AdsDefinition) def;
            }
        }

        return null;
    }
}
