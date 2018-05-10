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

package org.radixware.wps;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.dialogs.DialogFactory;
import org.radixware.kernel.common.client.env.AdsVersion;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.ImageManager;
import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.IFontManager;
import org.radixware.kernel.common.client.text.ITextOptionsManager;

import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.utils.ISecretStore;

import org.radixware.kernel.common.client.views.StandardViewFactory;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorFactory;
import org.radixware.kernel.common.client.widgets.WidgetFactory;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.meta.ILanguageContext;
import org.radixware.kernel.common.trace.IRadixTrace;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.wps.dialogs.WpsDialogFactory;
import org.radixware.wps.text.TextOptionsManager;
import org.radixware.wps.text.WpsFontManager;
import org.radixware.wps.views.WpsStandardViewFactory;
import org.radixware.wps.views.WpsWidgetFactory;


class WpsApplication implements IClientApplication {

    private DefManager defManager;
    private final Object defManagerLock = new Object();
    private final Object imageManagerLock = new Object();
    private final WpsEnvList userSessions = new WpsEnvList();
    private final IRadixTrace loggerTrace = new IRadixTrace() {
        private Level eventSeverity2loggerLevel(final EEventSeverity severity) {
            if (severity == null) {
                return Level.INFO;
            }
            switch (severity) {
                case ERROR:
                case ALARM:
                    return Level.SEVERE;
                case WARNING:
                    return Level.WARNING;
                case DEBUG:
                    return Level.FINE;
                default:
                    return Level.INFO;
            }
        }

        @Override
        public void put(EEventSeverity severity, String localizedMess, EEventSource source) {
            final Logger logger = Logger.getLogger(WpsApplication.class.getName());
            logger.log(eventSeverity2loggerLevel(severity), localizedMess);
        }

        @Override
        public void put(EEventSeverity severity, String code, List<String> words, String source) {
            final Logger logger = Logger.getLogger(WpsApplication.class.getName());
            StringBuilder logMessage = new StringBuilder();
            if (code != null) {
                logMessage.append(code);
                if (words != null && !words.isEmpty()) {
                    logMessage.append(" [ ");
                    boolean firstWord = false;
                    for (String word : words) {
                        if (firstWord) {
                            firstWord = false;
                        } else {
                            logMessage.append("; ");
                        }
                        logMessage.append(word);
                    }
                    logMessage.append(" ]");
                }
            } else if (words != null && !words.isEmpty()) {
                if (words.size() == 1) {
                    logMessage.append(words.get(0));
                } else {
                    logMessage.append(" [ ");
                    boolean firstWord = false;
                    for (String word : words) {
                        if (firstWord) {
                            firstWord = false;
                        } else {
                            logMessage.append("; ");
                        }
                        logMessage.append(word);
                    }
                    logMessage.append(" ]");
                }
            }
            logger.log(eventSeverity2loggerLevel(severity), logMessage.toString());
        }
    };
    private ImageManager imageManager;
    private List<HttpSessionContext> sessions = new LinkedList<>();
    private final Collection<Id> createReason;
    private final long version;
    final WebServer server;

    public WpsApplication(final WebServer server,
                                     final Collection<Id> createReason, 
                                     final long version) {
        this.server = server;
        if (createReason != null) {
            this.createReason = Collections.unmodifiableCollection(createReason);
        }else{
            this.createReason = Collections.emptyList();
        }
        this.version = version;
    }

    public Collection<Id> getCreateReason() {
        return createReason;
    }

    void register(HttpSessionContext e) {
        sessions.add(e);
    }

    void unregister(HttpSessionContext e) {
        sessions.remove(e);
        if (sessions.isEmpty()) {
            server.destroyApplication(this);
        }
    }

    @Override
    public MessageProvider getMessageProvider() {
        final IClientEnvironment environment = WpsEnvironment.getEnvironmentStatic();
        return environment == null ? DefaultMessageProvider.getInstance() : environment.getMessageProvider();
    }

    @Override
    public DefManager getDefManager() {
        synchronized (defManagerLock) {
            if (defManager == null) {
                defManager = new DefManager(this) {
                    @Override
                    protected AdsVersion createVersion(final IClientApplication env) {
                        return new WpsAdsVersion(env,version);
                    }
                };
            }
            return defManager;
        }
    }

    @Override
    public boolean isReleaseRepositoryAccessible() {
        return true;
    }

    public String getTopLayerName() {
        if (getClass().getClassLoader() instanceof RadixClassLoader) {
            final RadixClassLoader loader = (RadixClassLoader) getClass().getClassLoader();
            final StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (LayerMeta topLayer : loader.getRevisionMeta().getTopLayers()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(topLayer.getTitle() == null || topLayer.getTitle().isEmpty() ? topLayer.getUri() : topLayer.getTitle());
            }
        }
        return null;
    }

    public long getAdsVersionNumber() {
        return getDefManager().getAdsVersion().getNumber();
    }
    private ApplicationTracer ownTracer = null;

    @Override
    public ClientTracer getTracer() {
        Thread thread = Thread.currentThread();
        if (thread instanceof WebServer.WsThread) {
            return ((WebServer.WsThread) thread).userSession.getTracer();
        } else {
            //called not from within client environment; need another tracer 
            if (ownTracer == null) {
                ownTracer = new ApplicationTracer(this);
            }
            return ownTracer;
        }

    }
    
    void closeOwnTracer(){
        if (ownTracer!=null){
            ownTracer.close();
        }
    }

    @Override
    public ImageManager getImageManager() {
        synchronized (imageManagerLock) {
            if (imageManager == null) {
                imageManager = new WpsImageManager(this);
            }
            return imageManager;
        }
    }

    @Override
    public StandardViewFactory getStandardViewsFactory() {
        return new WpsStandardViewFactory();
    }

    @Override
    public ITaskWaiter newTaskWaiter() {
        return new WpsTaskWaiter();
    }

    @Override
    public DialogFactory getDialogFactory() {
        return new WpsDialogFactory();
    }

    @Override
    public EnvironmentCache getEnvironmentCache() {
        return userSessions;
    }

    @Override
    public EIsoLanguage getClientLanguage() {
        final Thread t = Thread.currentThread();
        if (t instanceof ILanguageContext) {
            return ((ILanguageContext) t).getLanguage();
        }
        return EIsoLanguage.ENGLISH;
    }

    @Override
    public IRadixTrace getTrace() {
        final WpsEnvironment env = WpsEnvironment.getEnvironmentStatic();
        if (env == null) {
            return loggerTrace;
        } else {
            return env.getTracer();
        }
    }
    private final WpsWidgetFactory widgetFactory = new WpsWidgetFactory();

    @Override
    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    @Override
    public IFontManager getFontManager() {
        return WpsFontManager.getInstance();
    }

    @Override
    public ITextOptionsManager getTextOptionsManager() {
        return TextOptionsManager.getInstance();
    }

    @Override
    public IEditMaskEditorFactory getEditMaskEditorFactory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ERuntimeEnvironmentType getRuntimeEnvironmentType() {
        return ERuntimeEnvironmentType.WEB;
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        return defManager.getRepository().getLanguages();
    }

    @Override
    public boolean isInGuiThread() {
        return WpsEnvironment.getEnvironmentStatic()!=null;
    }

    static class FileInfo {

        public final File file;
        public final String mimeType;
        public final boolean save;
        public final boolean delete;
        public final String desc;

        public FileInfo(File file, String mimeType, boolean save, String desc, boolean delete) {
            this.file = file;
            this.mimeType = mimeType;
            this.save = save;
            this.desc = desc;
            this.delete = delete;
        }
    }
    private final Map<String, FileInfo> fileResources = new HashMap<>();

    FileInfo getFileResource(String url) {
        return fileResources.remove(url);
    }

    String registerFileResource(String desc, File file, String mimeType, boolean open, boolean deleteOnDownload) {
        String url = UUID.randomUUID().toString();
        fileResources.put(url, new FileInfo(file, mimeType, !open, desc, deleteOnDownload));
        return url;
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
                return secret==null;
            }
        };
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
