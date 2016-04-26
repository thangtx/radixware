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

import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions.SslOptions;
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
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.types.TimeZoneInfo;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.environment.IEnvironmentAccessor;
import org.radixware.kernel.common.types.Id;


@SuppressWarnings({"PMD.UnusedModifier","PMD.DoNotUseThreads"})
public interface IClientEnvironment {

    @SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod","PMD.EmptyMethodInAbstractClassShouldBeAbstract"})
    public static abstract class ConnectionListener{
        public void afterOpenConnection(){
            //is empty in default implementation
        }
        public void afterCloseConnection(final boolean forced){            
            //is empty in default implementation
        }
    }

    String getUserName();

    String getStationName();

    TimeZoneInfo getServerTimeZoneInfo();

    Timestamp getCurrentServerTime();

    IResourceManager getResourceManager();

    MessageProvider getMessageProvider();

    EIsoLanguage getLanguage();

    EIsoCountry getCountry();

    Locale getLocale();

    IClientApplication getApplication();

    ClientTracer getTracer();

    String getTraceProfile();

    String getTraceFile();

    void processException(Throwable e);
    
    void processException(String title, Throwable e);

    void messageInformation(String title, String message);

    void messageError(String message);

    void messageError(String title, String message);

    void messageWarning(String message);

    void messageWarning(String title, String message);

    void messageException(String title, String message, Throwable e);

    boolean messageConfirmation(String title, String message);

    EDialogButtonType messageBox(String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons);

    IMessageBox newMessageBoxDialog(String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons);

    void alarmBeep();

    IEasSession getEasSession();

    SslOptions getSslOptions();

    ProgressHandleManager getProgressHandleManager();

    Clipboard getClipboard();

    FilterSettingsStorage getFilterSettingsStorage();

    ClientSettings getConfigStore();
    
    ITextOptionsProvider getTextOptionsProvider();

    GroupSettingsStorage getGroupSettingsStorage();

    IExplorerTreeManager getTreeManager();

    IWidget getMainWindow();

    boolean canViewAuditTable();

    boolean isContextlessCommandAccessible(Id id);

    boolean isCustomFiltersAccessible();

    boolean isCustomSortingsAccessible();

    List<EIsoLanguage> getSupportedLanguages();

    ISqmlDefinitions getSqmlDefinitions();

    String getWorkPath();

    <T> T runInGuiThread(Callable<T> task) throws InterruptedException, ExecutionException;
    
    void runInGuiThreadAsync(Runnable task);

    DefManager getDefManager();

    String setStatusBarLabel(String text);
    
    IMainStatusBar getStatusBar();
    
    IEventLoop newEventLoop();
    
    String signText(final String text, final X509Certificate certificate) throws SignatureException;

    public static class Locator {
        
        private Locator(){            
        }

        public static IClientEnvironment getEnvironment() {
            if (Thread.currentThread() instanceof IContextEnvironment) {
                return ((IContextEnvironment) Thread.currentThread()).getClientEnvironment();
            } else {
                if (Thread.currentThread().getContextClassLoader() instanceof IContextEnvironment) {
                    return ((IContextEnvironment) Thread.currentThread().getContextClassLoader()).getClientEnvironment();
                } else {
                    return null;
                }
            }

        }

        @SuppressWarnings({"PMD.UseProperClassLoader"})
        public static IClientApplication getApplication(Object context) {

            final Thread t = Thread.currentThread();
            IClientApplication env = null;
            if (t instanceof IEnvironmentAccessor) {
                env = (IClientApplication) ((IEnvironmentAccessor) t).getEnvironment();
            }
            if (env == null) {
                ClassLoader l = t.getContextClassLoader();
                if (l instanceof IEnvironmentAccessor) {
                    env = (IClientApplication) ((IEnvironmentAccessor) l).getEnvironment();
                }
                if (env == null) {
                    for (l = context.getClass().getClassLoader(); l != null; l = l.getParent()) {
                        if (l instanceof IEnvironmentAccessor) {
                            env = (IClientApplication) ((IEnvironmentAccessor) l).getEnvironment();
                            if (env != null) {
                                break;
                            }
                        }
                    }
                }
            }

            return env;
        }
    }
    
    void addConnectionListener(final ConnectionListener listener);
    
    void removeConnectionListener(final ConnectionListener listener);    
}