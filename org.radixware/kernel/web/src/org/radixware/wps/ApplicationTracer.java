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

import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.Clipboard;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.kernel.common.client.eas.resources.IResourceManager;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.env.IEventLoop;
import org.radixware.kernel.common.client.env.progress.ProgressHandleManager;
import org.radixware.kernel.common.client.exceptions.SignatureException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.models.groupsettings.FilterSettingsStorage;
import org.radixware.kernel.common.client.models.groupsettings.GroupSettingsStorage;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.trace.DefaultClientTracer;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;


class ApplicationTracer extends DefaultClientTracer {

    public ApplicationTracer(final WpsApplication application) {
        this(new EmbeddedEnvironment(application));

    }

    private ApplicationTracer(final EmbeddedEnvironment env) {
        super(env);
        env.tracer = this;
    }

    @SuppressWarnings("PMD.UncommentedEmptyMethod")
    private final static class EmbeddedEnvironment implements IClientEnvironment {

        private final WpsApplication application;
        private final TimeZoneInfo timeZoneInfo;
        private ClientTracer tracer;

        public EmbeddedEnvironment(WpsApplication application) {
            this.application = application;
            timeZoneInfo = new TimeZoneInfo(TimeZone.getDefault(), getLocale());
        }

        @Override
        public String getUserName() {
            return "unspecified";
        }

        @Override
        public String getStationName() {
            return "unspecified";
        }

        @Override
        public TimeZoneInfo getServerTimeZoneInfo() {
            return timeZoneInfo;
        }

        @Override
        public Timestamp getCurrentServerTime() {
            return new Timestamp(System.currentTimeMillis());
        }

        @Override
        public IResourceManager getResourceManager() {
            return null;
        }

        @Override
        public MessageProvider getMessageProvider() {
            return new MessageProvider() {
                @Override
                public String translate(String key, String message) {
                    return message;
                }
            };
        }

        @Override
        public EIsoLanguage getLanguage() {
            return EIsoLanguage.ENGLISH;
        }

        @Override
        public EIsoCountry getCountry() {
            return EIsoCountry.RUSSIAN_FEDERATION;
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }

        @Override
        public IClientApplication getApplication() {
            return application;
        }

        @Override
        public ClientTracer getTracer() {
            return tracer;
        }

        @Override
        public String getTraceProfile() {
            return null;
        }

        @Override
        public String getTraceFile() {
            return null;
        }

        @Override
        public void processException(Throwable e) {
            tracer.put(e);
        }

        @Override
        public void processException(String title, Throwable e) {
            tracer.put(e);
        }

        @Override
        public void messageInformation(String title, String message) {
        }

        @Override
        public void messageError(String message) {
        }

        @Override
        public void messageError(String title, String message) {
        }

        @Override
        public void messageWarning(String message) {
        }

        @Override
        public void messageWarning(String title, String message) {
        }

        @Override
        public void messageException(String title, String message, Throwable e) {
        }

        @Override
        public boolean messageConfirmation(String title, String message) {
            return false;
        }

        @Override
        public EDialogButtonType messageBox(String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons) {
            return EDialogButtonType.CANCEL;
        }

        @Override
        public IMessageBox newMessageBoxDialog(String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons) {
            return null;
        }

        @Override
        public void alarmBeep() {
        }

        @Override
        public IEasSession getEasSession() {
            return null;
        }

        @Override
        public ConnectionOptions.SslOptions getSslOptions() {
            return null;
        }

        @Override
        public ProgressHandleManager getProgressHandleManager() {
            return null;
        }

        @Override
        public Clipboard getClipboard() {
            return null;
        }

        @Override
        public FilterSettingsStorage getFilterSettingsStorage() {
            return null;
        }

        @Override
        public ClientSettings getConfigStore() {
            return null;
        }

        @Override
        public ITextOptionsProvider getTextOptionsProvider() {
            return null;
        }

        @Override
        public GroupSettingsStorage getGroupSettingsStorage() {
            return null;
        }

        @Override
        public IExplorerTreeManager getTreeManager() {
            return null;
        }

        @Override
        public IWidget getMainWindow() {
            return null;
        }

        @Override
        public boolean canViewAuditTable() {
            return false;
        }

        @Override
        public boolean isContextlessCommandAccessible(Id id) {
            return false;
        }

        @Override
        public boolean isCustomFiltersAccessible() {
            return false;
        }

        @Override
        public boolean isCustomSortingsAccessible() {
            return false;
        }

        @Override
        public List<EIsoLanguage> getSupportedLanguages() {
            return Collections.<EIsoLanguage>emptyList();
        }

        @Override
        public ISqmlDefinitions getSqmlDefinitions() {
            return null;
        }

        @Override
        public String getWorkPath() {
            return null;
        }

        @Override
        public <T> T runInGuiThread(final Callable<T> task) throws InterruptedException, ExecutionException {
            try{
                return task.call();
            }catch(Exception ex){
                throw new ExecutionException(ex);
            }
        }

        @Override
        public void runInGuiThreadAsync(final Runnable task) {
            task.run();
        }

        @Override
        public DefManager getDefManager() {
            return application.getDefManager();
        }

        @Override
        public String setStatusBarLabel(String text) {
            return null;
        }

        @Override
        public IMainStatusBar getStatusBar() {
            return null;
        }                

        @Override
        public IEventLoop newEventLoop() {
            return null;
        }

        @Override
        public String signText(String text, X509Certificate certificate) throws SignatureException {
            return null;
        }

        @Override
        public void addConnectionListener(ConnectionListener listener) {
        }

        @Override
        public void removeConnectionListener(ConnectionListener listener) {
        }        
    }
}
