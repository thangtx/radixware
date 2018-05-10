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

package org.radixware.kernel.common.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.radixware.kernel.common.client.dialogs.DialogFactory;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.IFontManager;
import org.radixware.kernel.common.client.text.ITextOptionsManager;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.views.StandardViewFactory;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorFactory;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.environment.IRadixEnvironment;


public interface IClientApplication extends IRadixEnvironment {

    public static class EnvironmentCache {

        private final List<IClientEnvironment> environments = new LinkedList<>();

        public void invalidateUserSessions() {
            synchronized (environments) {
                for (IClientEnvironment environment : environments) {
                    environment.getClipboard().reset();
                    environment.getFilterSettingsStorage().clear();
                }
            }
        }

        public void register(IClientEnvironment environment) {
            synchronized (environments) {
                environments.add(environment);
            }
        }

        public void unregister(IClientEnvironment environment) {
            synchronized (environments) {
                environments.remove(environment);
            }
        }

        public List<IClientEnvironment> listUserSessions() {
            return new ArrayList<>(environments);
        }
    }

    public MessageProvider getMessageProvider();

    public ClientTracer getTracer();

    public ImageManager getImageManager();    
    
    public IFontManager getFontManager();
    
    public ITextOptionsManager getTextOptionsManager();

    @Override
    public DefManager getDefManager();

    public StandardViewFactory getStandardViewsFactory();

    public ITaskWaiter newTaskWaiter();
    
    public ISecretStore newSecretStore();

    public DialogFactory getDialogFactory();

    public EnvironmentCache getEnvironmentCache();

    public WidgetFactory getWidgetFactory();

    public IEditMaskEditorFactory getEditMaskEditorFactory();

    public ERuntimeEnvironmentType getRuntimeEnvironmentType();
    
    public boolean isReleaseRepositoryAccessible();
    
    public boolean isInGuiThread();
    
    public boolean isExtendedMetaInformationAccessible();
    
    public boolean isSqmlAccessible();
}
