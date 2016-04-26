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

package org.radixware.kernel.common.builder.completion.sqml;

import org.radixware.kernel.common.builder.completion.CompletionProviderFactoryRegistration;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.scml.CompletionProviderFactory;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;
import org.radixware.kernel.common.sqml.Sqml;


@CompletionProviderFactoryRegistration
public class SqmlCompletionProviderFactory extends CompletionProviderFactory {

    @Override
    public ScmlCompletionProvider findCompletionProvider(final Item item) {
        if (item instanceof Scml.Text) {
            return new SqmlCompletionProviderText( (Sqml) item.getOwnerScml(), (Sqml.Text) item);
        } else {
            return null;
        }
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Sqml.class;
    }
}
