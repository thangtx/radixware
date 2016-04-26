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

package org.radixware.kernel.common.compiler.core.completion;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;


public class AdsPropertyCompletionItem implements ScmlCompletionProvider.CompletionItem {

    private AdsPropertyDef property;
    private int relevance;
    private int replaceStart;
    int replaceEnd;
    private ERuntimeEnvironmentType contextEnvironment;

    public AdsPropertyCompletionItem(AdsPropertyDef method, int relevance, int replaceStart, int replaceEnd, ERuntimeEnvironmentType contextEnvironment) {
        this.property = method;
        this.relevance = relevance + 2;
        this.replaceStart = replaceStart;
        this.replaceEnd = replaceEnd;
        this.contextEnvironment = contextEnvironment;
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
        AdsAccessFlags propAccessFlag = property.getAccessFlags();
        StringBuilder result = new StringBuilder();
        result.append("<b>");
        if (propAccessFlag.isStatic()) {
            result.append("<font color=\"330066\">");
        } else {
            result.append("<font color=\"0066FF\">");
        }
        if (property.isConst()){
            result.append("<i>");
        }
        result.append(property.getName());
        if (property.isConst()){
            result.append("</i>");
        }
        result.append("</font>");
        result.append("</b>");
        return result.toString();
    }

    @Override
    public String getTailDisplayText() {
        String type = property.getTypedObject().getType().getHtmlName(property, false);
        if (contextEnvironment.isClientEnv()){
            return "Property&lt;" + type + "&gt;";
        }
        return type;
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
    public boolean removePrevious(Scml.Item prevItem) {
        return false;
    }

    @Override
    public RadixObject getRadixObject() {
        return property;
    }
}
