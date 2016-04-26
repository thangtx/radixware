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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;


abstract class FilterClipboardSupport extends AdsClipboardSupport<AdsFilterDef> {

    private final AdsFilterDef ownerFilter;

    @SuppressWarnings("unchecked")
    FilterClipboardSupport(AdsFilterDef ownerFilter) {
        super(ownerFilter);
        this.ownerFilter = ownerFilter;
    }

    @SuppressWarnings({"unchecked", "unchecked"})
    @Override
    public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
        if (ownerFilter == null || ownerFilter.getContainer() == null) {
            return CanPasteResult.NO;
        }
        List<Transfer> parameterTransfers = new ArrayList<Transfer>();
        List<Transfer> filterTransfers = new ArrayList<Transfer>();

        for (Transfer t : objectsInClipboard) {
            if (t.getObject() instanceof AdsFilterDef.Parameter) {
                parameterTransfers.add(t);
            } else if (t.getObject() instanceof AdsFilterDef) {
                filterTransfers.add(t);
            } else {
                return CanPasteResult.NO;
            }
        }
        if (!parameterTransfers.isEmpty()) {
            CanPasteResult result = ownerFilter.getParameters().getClipboardSupport().canPaste(parameterTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return result;
            }
        }
        if (!filterTransfers.isEmpty()) {
            CanPasteResult result = ownerFilter.getContainer().getClipboardSupport().canPaste(filterTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return result;
            }
        }
        return CanPasteResult.YES;
    }

    @Override
    public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {

        if (ownerFilter == null || ownerFilter.getContainer() == null) {
            return;
        }
        List<Transfer> parameterTransfers = new ArrayList<Transfer>();
        List<Transfer> filterTransfers = new ArrayList<Transfer>();

        for (Transfer t : objectsInClipboard) {
            if (t.getObject() instanceof AdsFilterDef) {
                filterTransfers.add(t);
            } else if (t.getObject() instanceof AdsFilterDef.Parameter) {
                parameterTransfers.add(t);
            }
        }

        if (!filterTransfers.isEmpty()) {
            ownerFilter.getContainer().getClipboardSupport().paste(filterTransfers, resolver);
        }
        if (!parameterTransfers.isEmpty()) {
            ownerFilter.getParameters().getClipboardSupport().paste(parameterTransfers, resolver);
        }
    }
}
