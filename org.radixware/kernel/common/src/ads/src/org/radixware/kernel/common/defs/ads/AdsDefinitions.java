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

package org.radixware.kernel.common.defs.ads;

import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.types.Id;

/**
 * Ads definition container
 *
 */
public class AdsDefinitions<T extends AdsDefinition> extends Definitions<T> {

    public AdsDefinitions(AdsDefinition owner) {
        super(owner);
    }

    public AdsDefinitions(RadixObject container) {
        super(container);
    }
    //for extendable definitions

    public AdsDefinitions() {
        super();
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
        if (isReadOnly()) {
            return CanPasteResult.NO;
        }
        for (Transfer transfer : transfers) {
            if (canPaste(transfer, resolver) != CanPasteResult.YES) {
                return CanPasteResult.NO;
            }
        }
        return CanPasteResult.YES;
    }

    private CanPasteResult canPaste(Transfer objectInClipboard, DuplicationResolver resolver) {
        final RadixObject object = objectInClipboard.getObject();
        if (object instanceof AdsDefinition) {
            final AdsDefinition def = (AdsDefinition) object;
            //  if (def instanceof IDependentId) {
            final AdsDefinition ex = findByIdForPasteAction(def.getId());
            if (ex != null && ex != def && resolver.resolveDuplication(def, ex) != DuplicationResolver.Resolution.REPLACE) {
                return CanPasteResult.NO;
            }
            // }
            return def.isSuitableContainer(this) ? CanPasteResult.YES : CanPasteResult.NO;
        } else {
            return CanPasteResult.NO;
        }
    }

    protected AdsDefinition findByIdForPasteAction(Id id) {
        return findById(id);
    }

    @Override
    public AdsModule getModule() {
        return (AdsModule) super.getModule();
    }
}
