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

package org.radixware.kernel.common.defs.dds;

import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definitions;

/**
 * Collection of DDS definitions.
 */
public class DdsDefinitions<T extends DdsDefinition> extends Definitions<T> {

    protected DdsDefinitions(DdsDefinition container) {
        super(container);
    }

    protected DdsDefinitions() { // for extendable definitions
        super();
    }

    protected CanPasteResult canPaste(Transfer objectInClipboard) {
        return isReadOnly() ? CanPasteResult.NO : CanPasteResult.YES;
    }

    @Override
    protected final CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
        for (Transfer objectInClipboard : objectsInClipboard) {
            if (canPaste(objectInClipboard) != CanPasteResult.YES) {
                return CanPasteResult.NO;
            }
        }
        return CanPasteResult.YES;
    }
}
