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

package org.radixware.kernel.common.defs.ads.common;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;


public interface ICustomViewable<T extends RadixObject, E extends AdsAbstractUIDef> {

    class CustomViewChangedEvent extends RadixEvent {
    }

    interface CustomViewChangeListener extends IRadixEventListener<CustomViewChangedEvent> {
    }

    public abstract static class CustomViewSupport<T extends RadixObject, V extends AdsAbstractUIDef> {

        private V explorerCustomView;
        private V webCustomView;
        private final T context;
        private RadixEventSource<CustomViewChangeListener, CustomViewChangedEvent> changeSupport = null;

        public CustomViewSupport(T context) {
            this.context = context;
        }

        protected abstract V createOrLoadCustomView(T context, ERuntimeEnvironmentType env, AbstractDialogDefinition xDef);

        public V getCustomView(ERuntimeEnvironmentType env) {
            switch (env) {
                case EXPLORER:
                    return explorerCustomView;
                case WEB:
                    return webCustomView;
                default:
                    return null;
            }

        }

        public boolean isUseCustomView(ERuntimeEnvironmentType env) {
            switch (env) {
                case EXPLORER:
                    return explorerCustomView != null;
                case WEB:
                    return webCustomView != null;
                default:
                    return false;
            }
        }

        public boolean setUseCustomView(ERuntimeEnvironmentType env, boolean use) {
            synchronized (this) {
                if (use) {
                    if (getCustomView(env) == null) {
                        switch (env) {
                            case EXPLORER:
                                explorerCustomView = createOrLoadCustomView(context, env, null);
                                context.setEditState(EEditState.MODIFIED);
                                fireChange();
                                return true;
                            case WEB:
                                webCustomView = createOrLoadCustomView(context, env, null);
                                context.setEditState(EEditState.MODIFIED);
                                fireChange();
                                return true;
                            default:
                                return false;
                        }
                    }
                } else {
                    if (getCustomView(env) != null) {
                        switch (env) {
                            case EXPLORER:
                                this.explorerCustomView.delete();
                                this.explorerCustomView = null;
                                context.setEditState(EEditState.MODIFIED);
                                fireChange();
                                return true;
                            case WEB:
                                this.webCustomView.delete();
                                this.webCustomView = null;
                                context.setEditState(EEditState.MODIFIED);
                                fireChange();
                                return true;
                            default:
                                return false;
                        }
                    }
                }
                return false;
            }
        }

        public RadixEventSource<CustomViewChangeListener, CustomViewChangedEvent> getChangeSupport() {
            synchronized (this) {
                if (changeSupport == null) {
                    changeSupport = new RadixEventSource<CustomViewChangeListener, CustomViewChangedEvent>();
                }
                return changeSupport;
            }
        }

        private void fireChange() {
            synchronized (this) {
                if (changeSupport != null) {
                    changeSupport.fireEvent(new CustomViewChangedEvent());
                }
            }
        }

        public void loadCustomView(ERuntimeEnvironmentType env, AbstractDialogDefinition xDef) {
            if (!RadixObjectInitializationPolicy.get().isRuntime()){
                if (env == ERuntimeEnvironmentType.EXPLORER) {
                    this.explorerCustomView = createOrLoadCustomView(context, env, xDef);
                } else if (env == ERuntimeEnvironmentType.WEB) {
                    this.webCustomView = createOrLoadCustomView(context, env, xDef);
                }
            }
        }
    }

    public CustomViewSupport<T, E> getCustomViewSupport();
}
