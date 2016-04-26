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
package org.radixware.kernel.common.compiler.core.completion;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;

/**
 *
 * @author akrylov
 */
public class AdsEnumCompletionItem implements ScmlCompletionProvider.CompletionItem {

    private AdsEnumItemDef property;
    private int relevance;
    private int replaceStart;
    int replaceEnd;

    public AdsEnumCompletionItem(AdsEnumItemDef constant, int relevance, int replaceStart, int replaceEnd) {
        this.property = constant;
        this.relevance = relevance + 2;
        this.replaceStart = replaceStart;
        this.replaceEnd = replaceEnd;
    }

    @Override
    public String getSortText() {
        return property.getName();
    }

    @Override
    public int getRelevance() {
        return relevance;
    }

    @Override
    public String getLeadDisplayText() {

        return "<b><font color=\"FF00FF\">" + property.getName() + "</font></b>";

    }

    @Override
    public String getTailDisplayText() {
        return property.getOwnerEnum().getQualifiedName();
    }

    @Override
    public String getEnclosingSuffix() {
        return "";
    }

    @Override
    public RadixIcon getIcon() {
        return property.getIcon();
    }

    @Override
    public Scml.Item[] getNewItems() {
        return new Scml.Item[]{
            JmlTagInvocation.Factory.newInstance(property)
        };
    }

    @Override
    public int getReplaceStartOffset() {
        return replaceStart;
    }

    @Override
    public int getReplaceEndOffset() {
        return replaceEnd;
    }

    @Override
    public RadixObject getRadixObject() {
        return property;
    }

    @Override
    public boolean removePrevious(Scml.Item prevItem) {
        if (property == null) {
            return false;
        }
        if (prevItem instanceof JmlTagTypeDeclaration) {
            final JmlTagTypeDeclaration tag = (JmlTagTypeDeclaration) prevItem;
            final AdsType adsType = tag.getType().resolve(property).get();
            if (adsType instanceof AdsEnumType) {
                final AdsEnumType et = (AdsEnumType) adsType;
                if (et.getSource() != null && et.getSource().getId() == property.getOwnerEnum().getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
