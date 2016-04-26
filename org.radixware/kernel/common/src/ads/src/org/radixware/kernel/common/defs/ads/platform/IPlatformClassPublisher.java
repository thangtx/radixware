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

package org.radixware.kernel.common.defs.ads.platform;

import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public interface IPlatformClassPublisher {

    public interface IPlatformClassPublishingSupport {

        public RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> getPlatformClassPublishingChengesSupport();

        public boolean isPlatformClassPublisher();

        public String getPlatformClassName();

        public boolean isExtendablePublishing();
    }

    public class PlatformClassPublishingChangedEvent extends RadixEvent {

        public final IPlatformClassPublisher publisher;

        public PlatformClassPublishingChangedEvent(IPlatformClassPublisher publisher) {
            this.publisher = publisher;
        }
    }

    public interface PlatformClassPublisherChangesListener extends IRadixEventListener<PlatformClassPublishingChangedEvent> {
    }

    public IPlatformClassPublishingSupport getPlatformClassPublishingSupport();
}
