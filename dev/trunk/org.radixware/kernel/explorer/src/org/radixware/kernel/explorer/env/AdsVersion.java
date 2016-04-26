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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.environment.IEnvironmentAccessor;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.Explorer;
import org.radixware.kernel.starter.radixloader.IRadixLoaderAccessor;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public final class AdsVersion extends org.radixware.kernel.common.client.env.AdsVersion {
    
    /**
     * Контекстный загрузчик классов.
     * Библиотека QtJambi может кэшировать результат вызова метода Thread.currentThread().getContextClassLoader(),
     * поэтому используется ClassLoader-посредник.
     */
    private static class ProxyClassLoader extends ClassLoader implements IEnvironmentAccessor, IRadixLoaderAccessor, IContextEnvironment {

        private volatile RadixClassLoader rxClassLoader;
        private final IRadixEnvironment environment;

        public ProxyClassLoader(final IRadixEnvironment environment, final RadixClassLoader classLoader) {
            this.environment = environment;
            this.rxClassLoader = classLoader;
        }

        public void setRadixClassLoader(final RadixClassLoader classLoader){
            rxClassLoader = classLoader;
        }

        private ClassLoader getActualClassLoader() {
            return rxClassLoader==null ? IClientApplication.class.getClassLoader() : rxClassLoader;
        }

        @Override
        protected final Class<?> findClass(String name) throws ClassNotFoundException {
            return getActualClassLoader().loadClass(name);
        }

        @Override
        protected final URL findResource(String name) {
            URL result = getActualClassLoader().getResource(name);
            if (result == null) {
                result = IClientApplication.class.getClassLoader().getResource(name);
            }
            return result;
        }

        @Override
        protected final Enumeration<URL> findResources(String name) throws IOException {
            return getActualClassLoader().getResources(name);
        }

        @Override
        public final IRadixEnvironment getEnvironment() {
            return environment;
        }

        @Override
        //Необходим для загрузки класса презентации пользовательского отчета
        //см. AdsClassLoader.loadUserReportJarByClassId
        public final IClientEnvironment getClientEnvironment() {
            return Application.getInstance().getEnvironment();
        }

        @Override
        public final Object getRadixLoader() {
            Object loader = RadixLoader.getInstance();
            ClassLoader cl = this;
            while (loader == null) {
                ClassLoader parent = cl.getParent();
                if (parent == null) {
                    break;
                }
                try {
                    Class<?> clazz = parent.loadClass(RadixLoader.class.getName());
                    if (clazz != null) {
                        try {
                            Method method = clazz.getDeclaredMethod("getInstance", new Class[0]);
                            if (method != null) {
                                loader = method.invoke(null, new Object[0]);
                                if (loader != null) {
                                    return loader;
                                }
                            }
                        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        }
                    }
                } catch (ClassNotFoundException e) {
                }
                cl = parent;
            }
            return loader;
        }

        private void close() {
            rxClassLoader = null;
        }
    }    

    private static class TrayButtonManager extends QObject {

        private OldVersionTrayItem trayItem;
        private final IClientApplication application;
        private final AdsVersion version;

        public TrayButtonManager(final IClientApplication app, final AdsVersion ver) {
            super((QObject)app);
            application = app;
            version = ver;
        }

        private static class UpdateChangeVersionButtonEvent extends QEvent {

            final boolean visible;

            public UpdateChangeVersionButtonEvent(final boolean isVisible) {
                super(QEvent.Type.User);
                visible = isVisible;
            }

            public boolean isVisible() {
                return visible;
            }
        }

        @Override
        protected void customEvent(final QEvent event) {
            if (event instanceof UpdateChangeVersionButtonEvent) {
                event.accept();
                final boolean isTrayItemVisible = 
                    ((UpdateChangeVersionButtonEvent) event).isVisible();
                if (trayItem == null && isTrayItemVisible) {
                    if (Application.getMainWindow() != null/*
                             * && Environment.getConnectionOptions() != null
                             */) {
                        trayItem = new OldVersionTrayItem(application.getMessageProvider(), Application.getMainWindow());
                        trayItem.clicked.connect(version, "tryToUpdateDefinitions()");
                        Application.getMainWindow().statusBar().addPermanentWidget(trayItem);
                    } else {
                        return;
                    }
                } else if (trayItem!=null && trayItem.nativeId() > 0) {
                    trayItem.setVisible(isTrayItemVisible);
                }
            }
            super.customEvent(event);
        }
        
        public void setOldVersionButtonVisible(final boolean isVisible) {
            QCoreApplication.postEvent(this, new UpdateChangeVersionButtonEvent(isVisible));
        }
    }
        
    private final TrayButtonManager trayManager;                

    public AdsVersion(final IClientApplication env, IClientEnvironment clientEnv) {
        super(env);
        trayManager = new TrayButtonManager(env, this);        
    }

    @Override
    protected void setupContextClassLoader(final RadixClassLoader rxClassLoader) {
        final ProxyClassLoader cl = new ProxyClassLoader(env, rxClassLoader);
        final Thread currentThread = Thread.currentThread();
        final ClassLoader prevClassLoader = currentThread.getContextClassLoader();
        if (prevClassLoader instanceof ProxyClassLoader){
            ((ProxyClassLoader)prevClassLoader).close();
        }
        currentThread.setContextClassLoader(cl);
        com.trolltech.qt.internal.QtJambiRuntime.setClassLoader(cl);
    }

    @Override
    protected boolean confirmToRestart() {
        final String title = env.getMessageProvider().translate("ExplorerMessage", "Confirm to Restart");
        final String message = env.getMessageProvider().translate("ExplorerMessage", "It is necessary to restart explorer to install new version.\nDo you want to restart now ?");
        return Application.getInstance().getEnvironment().messageConfirmation(title, message);
    }

    @Override
    protected void restartApplication() {
        Explorer.restart();
    }

    @Override
    protected void versionNumberUpdated() {
    }

    @Override
    protected void afterUpdateToNewVersion(final Integer oldClassLoaderId) {
        trayManager.setOldVersionButtonVisible(false);
        if (oldClassLoaderId!=null){
            clearQtJambiCachedMetaData(oldClassLoaderId);
        }
        super.afterUpdateToNewVersion(oldClassLoaderId);
    }
    
    @Override
    public void clear() {
        final RadixClassLoader oldClassLoader = getRadixClassLoader();
        final Integer oldClassLoaderId = oldClassLoader==null ? null : oldClassLoader.hashCode();        
        super.clear();
        trayManager.setOldVersionButtonVisible(false);
        if (oldClassLoaderId!=null){
            clearQtJambiCachedMetaData(oldClassLoaderId);
        }
    }
    
    @SuppressWarnings({"SleepWhileInLoop", "null", "ConstantConditions"})
    private void clearQtJambiCachedMetaData(final int classLoaderId){
        final int cachedClasses = 
            com.trolltech.qt.internal.MetaObjectTools.safelyClearCachedMetaData(classLoaderId);
        if (cachedClasses>0){
            String cachedClassName;
            int tryCount=0;
            do{
                tryCount++;
                System.gc();//NOPMD
                try{
                    Thread.sleep(50);
                }catch(InterruptedException exception){
                    
                }
                QApplication.sendPostedEvents(null, QEvent.Type.DeferredDelete.value());
                QApplication.processEvents();
                cachedClassName = 
                    com.trolltech.qt.internal.MetaObjectTools.findClassNameByClassLoader(classLoaderId);
            }while(cachedClassName!=null && !cachedClassName.isEmpty() && tryCount<=10);
            if (cachedClassName!=null && !cachedClassName.isEmpty()){
                final String message = env.getMessageProvider().translate("TraceMessage","Instance of \'%1$s\' class was not disposed");
                env.getTracer().put(EEventSeverity.WARNING, String.format(message,cachedClassName), EEventSource.CLIENT_DEF_MANAGER);
                com.trolltech.qt.internal.MetaObjectTools.forcedlyClearCachedMetaData(classLoaderId);
            }
        }
    }
       
    @Override
    public void setNewVersion(long version) {
        super.setNewVersion(version);
        trayManager.setOldVersionButtonVisible(true);
    }

    @Override
    protected IProgressHandle startChangeAnalyse(IClientEnvironment contextEnvironment) {
        final IProgressHandle progress = contextEnvironment.getProgressHandleManager().newProgressHandle();
        progress.startProgress(contextEnvironment.getMessageProvider().translate("ExplorerMessage", "Analysing changes"), false);
        return progress;
    }

    @Override
    protected void finishChangeAnalyse(IProgressHandle progress) {
        progress.finishProgress();
    }

    @SuppressWarnings("unused")
    private void tryToUpdateDefinitions() {
        if (isKernelWasChanged()) {
            if (confirmToRestart()) {
                restartApplication();
            }
            return;
        }
        final Collection<Id> changedDefs = checkForUpdates(Application.getInstance().getEnvironment());
        if (changedDefs != null) {
            ((Application) env).getEnvironment().getTreeManager().updateVersion(changedDefs);
        } else {
            trayManager.setOldVersionButtonVisible(false);
        }
    }
}
