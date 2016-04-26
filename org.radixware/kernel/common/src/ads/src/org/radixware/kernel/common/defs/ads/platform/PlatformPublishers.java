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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.IRemoveListener;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.PlatformClassPublishingChangedEvent;
import org.radixware.kernel.common.repository.Layer;

import org.radixware.kernel.common.repository.ads.AdsSegment;

public class PlatformPublishers extends RadixObject {

    private final IPlatformClassPublisher.PlatformClassPublisherChangesListener publishingListener = new IPlatformClassPublisher.PlatformClassPublisherChangesListener() {
        @Override
        public void onEvent(PlatformClassPublishingChangedEvent e) {
            IPlatformClassPublisher.IPlatformClassPublishingSupport support = e.publisher.getPlatformClassPublishingSupport();
            removePublisher(e.publisher);
            if (support != null) {
                if (support.isPlatformClassPublisher()) {
                    synchronized (publishers) {
                        publishers.put(support.getPlatformClassName(), e.publisher);
                    }
                }
            }
        }
    };
    private final IRemoveListener removeListener = new IRemoveListener() {
        @Override
        public void onEvent(RemovedEvent e) {
            if (e.radixObject instanceof IPlatformClassPublisher) {
                removePublisher((IPlatformClassPublisher) e.radixObject);
                IPlatformClassPublisher.IPlatformClassPublishingSupport support = ((IPlatformClassPublisher) e.radixObject).getPlatformClassPublishingSupport();
                if (support != null) {
                    support.getPlatformClassPublishingChengesSupport().removeEventListener(publishingListener);
                }
            }
        }
    };
    private final HashMap<String, IPlatformClassPublisher> publishers = new HashMap<>();
    private boolean layerWasUploadedByMySelf = false;

    PlatformPublishers(BuildPath path) {
        setContainer(path);
    }

    public void attach(IPlatformClassPublisher publisher) {
        IPlatformClassPublisher.IPlatformClassPublishingSupport support = publisher.getPlatformClassPublishingSupport();
        if (support != null) {
            if (support.isPlatformClassPublisher()) {
                synchronized (publishers) {
                    publishers.put(support.getPlatformClassName(), publisher);
                }
            }
            support.getPlatformClassPublishingChengesSupport().addEventListener(publishingListener);
            if (publisher instanceof AdsDefinition) {
                ((AdsDefinition) publisher).getRemoveSupport().addEventListener(removeListener);
            }
        }
    }

    private final Object lock = new Object();

    public IPlatformClassPublisher findPublisherByName(String platformClassName) {

        IPlatformClassPublisher publisher = null;
        if (!isUserExtension()) {

            synchronized (publishers) {
                publisher = publishers.get(platformClassName);
            }

            if (publisher == null) {
                synchronized (lock) {
                    if (!layerWasUploadedByMySelf) {
                        layerWasUploadedByMySelf = true;
                        for (Module module : this.getLayer().getAds().getModules()) {
                            //upload 
                            ((AdsModule) module).getDefinitions();
                        }
                        publisher = publishers.get(platformClassName);
                    }
                }
            }
        }
        if (publisher == null) {
            PlatformPublishers pubs = getFromPrevLayer();
            if (pubs != null) {
                publisher = pubs.findPublisherByName(platformClassName);
            }
        }
        return publisher;
    }

    public IPlatformClassPublisher findPublisherBySignature(char[] platformClassSignature) {
        String name = TypeSignature.parseTypeName(platformClassSignature);
        if (name == null) {
            return null;
        }
        return findPublisherByName(String.valueOf(name));
    }

    private BuildPath getBuildPath() {
        return (BuildPath) getContainer();
    }

    private PlatformPublishers getFromPrevLayer() {
        final Layer root = getBuildPath().getSegment().getLayer();
        return Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<PlatformPublishers>() {
            @Override
            public void accept(HierarchyWalker.Controller<PlatformPublishers> controller, Layer layer) {
                if (layer != root) {
                    controller.setResultAndStop(((AdsSegment) layer.getAds()).getBuildPath().getPlatformPublishers());
                }
            }
        });
    }

    private void removePublisher(IPlatformClassPublisher def) {
        synchronized (publishers) {
            String keyToRemove = null;
            for (Map.Entry<String, IPlatformClassPublisher> entry : publishers.entrySet()) {
                if (entry.getValue() == def) {
                    keyToRemove = entry.getKey();
                }
            }
            if (keyToRemove != null) {
                publishers.remove(keyToRemove);
            }
        }
    }
}
