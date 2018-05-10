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

package org.radixware.kernel.designer.eas.client;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.DialogFactory;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.IFontManager;
import org.radixware.kernel.common.client.text.ITextOptionsManager;
import org.radixware.kernel.common.client.trace.DefaultClientTracer;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.StandardViewFactory;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorFactory;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class DesignerClientApplication implements IClientApplication {

    private DefManager defManager;
    private DefaultClientTracer tracer;
    private final IClientEnvironment env;

    public DesignerClientApplication(IClientEnvironment env) {
        this.env = env;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return new MessageProvider() {
            @Override
            public String translate(String key, String defaultVal) {
                return defaultVal;
            }
        };
    }

    @Override
    public DefaultClientTracer getTracer() {
        synchronized (this) {
            if (tracer == null) {
                tracer = new DefaultClientTracer(env);
                tracer.setBuffer(tracer.createTraceBuffer());
            }
            return tracer;
        }
    }

    @Override
    public ImageManager getImageManager() {
        return new DesignerImageManager();
    }

    @Override
    public DefManager getDefManager() {
        synchronized (this) {
            if (defManager == null) {
                defManager = new DefManager(this) {
                    @Override
                    protected AdsVersion createVersion(IClientApplication ica) {
                        return new DesignerAdsVersion(ica);
                    }
                };
            }
            return defManager;
        }
    }

    @Override
    public StandardViewFactory getStandardViewsFactory() {
        return null;
    }

    @Override
    public ITaskWaiter newTaskWaiter() {
        return new ITaskWaiter() {
            private IProgressHandle handle;

            @Override
            public <T> T runAndWait(Callable<T> task) throws ExecutionException, InterruptedException {
                try {
                    handle = new ProgressHandleImpl();
                    handle.startProgress("", true);
                    return task.call();
                } catch (ExecutionException ex) {
                    throw ex;
                } catch (InterruptedException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new ExecutionException(ex);
                } finally {
                    if (handle != null) {
                        handle.finishProgress();
                        handle = null;
                    }
                }
            }

            @Override
            public void runAndWait(Runnable r) throws InterruptedException {
                try {
                    r.run();
                } finally {
                    if (handle != null) {
                        handle.finishProgress();
                        handle = null;
                    }
                }
            }

            @Override
            public void close() {
                if (handle != null) {
                    handle.finishProgress();
                }
            }

            @Override
            public void setMessage(String string) {
                if (handle != null) {
                    handle.setTitle(string);
                }
            }
        };
    }

    @Override
    public DialogFactory getDialogFactory() {
        return new DesignerDialogFactory();
    }
    private final EnvironmentCache envCache = new EnvironmentCache();

    @Override
    public EnvironmentCache getEnvironmentCache() {
        return envCache;
    }

    @Override
    public WidgetFactory getWidgetFactory() {
        return null;
    }

    @Override
    public IEditMaskEditorFactory getEditMaskEditorFactory() {
        return null;
    }

    @Override
    public IFontManager getFontManager() {
        return null;
    }

    @Override
    public ITextOptionsManager getTextOptionsManager() {
        return null;
    }

    @Override
    public ERuntimeEnvironmentType getRuntimeEnvironmentType() {
        return ERuntimeEnvironmentType.EXPLORER;
    }

    @Override
    public boolean isReleaseRepositoryAccessible() {
        return false;
    }

    @Override
    public EIsoLanguage getClientLanguage() {
        return EIsoLanguage.ENGLISH;
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        return defManager.getRepository().getLanguages();
    }

    @Override
    public IRadixTrace getTrace() {
        return getTracer();
    }

    public static String getProductName() {
        if (Starter.getProductName() != null && !Starter.getProductName().isEmpty()) {
            return Starter.getProductName();
        }
        if (RadixLoader.getInstance() == null) {
            return "RadixWare";
        }
        final List<String> topLayerUris = RadixLoader.getInstance().getTopLayerUris();
        if (topLayerUris == null) {
            return "";
        } else if (topLayerUris.size() == 1) {
            try {
                final String title = RadixLoader.getInstance().getCurrentRevisionMeta().getTopLayers().get(0).getTitle();
                if (title == null || title.isEmpty()) {
                    return topLayerUris.get(0);
                } else {
                    return title;
                }
            } catch (RuntimeException ex) {
                return topLayerUris.get(0);
            }
        } else {
            return RadixLoader.getInstance().getTopLayerUrisAsString();
        }

    }

    @Override
    public ISecretStore newSecretStore() {
        return new ISecretStore() {
            private byte[] secret;

            @Override
            public byte[] getSecret() {
                return secret == null ? null : Arrays.copyOf(secret, secret.length);
            }

            @Override
            public void setSecret(byte[] secret) {
                clearSecret();
                this.secret = Arrays.copyOf(secret, secret.length);
            }

            @Override
            public void clearSecret() {
                if (secret != null && secret.length > 0) {
                    Arrays.fill(secret, (byte) 0);
                }
                secret = null;
            }

            @Override
            public boolean isEmpty() {
                return secret==null || secret.length==0;
            }

        };
    }
    
    @Override
    public boolean isInGuiThread() {
        return true;
    }
    
    @Override
    public boolean isExtendedMetaInformationAccessible() {
        return true;
    }    
    
    @Override
    public boolean isSqmlAccessible() {
        return true;
    }    
}
