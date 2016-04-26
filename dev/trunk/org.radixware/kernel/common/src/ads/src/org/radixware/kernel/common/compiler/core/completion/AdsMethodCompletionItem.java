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
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;


public class AdsMethodCompletionItem implements ScmlCompletionProvider.CompletionItem {

    private AdsMethodDef method;
    private int relevance;
    private int replaceStart;
    int replaceEnd;

    public AdsMethodCompletionItem(AdsMethodDef method, int relevance, int replaceStart, int replaceEnd) {
        this.method = method;
        this.relevance = relevance+1;
        this.replaceStart = replaceStart;
        this.replaceEnd = replaceEnd;
    }

    @Override
    public String getSortText() {
        return method.getName();
    }

    @Override
    public int getRelevance() {
        return relevance;
    }

    @Override
    public String getLeadDisplayText() {
        StringBuilder profile = new StringBuilder();
        profile.append("<b>");
        profile.append(method.getName());
        profile.append("</b>");
        profile.append('(');

        AdsMethodParameters.printProfileHtml(method, false, method.getProfile().getParametersList(), profile);

        profile.append(')');

        return profile.toString();
    }

    @Override
    public String getTailDisplayText() {
        if (method.isConstructor()) {
            return "";
        } else {
            return method.getProfile().getReturnValue().getType().getHtmlName(method, false);
        }
    }

    @Override
    public String getEnclosingSuffix() {
        return "";
    }

    @Override
    public RadixIcon getIcon() {
        return method.getIcon();
    }

    @Override
    public Scml.Item[] getNewItems() {
        return new Scml.Item[]{
            JmlTagInvocation.Factory.newInstance(method),
            Scml.Text.Factory.newInstance("()")
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
        return method;
    }
}
