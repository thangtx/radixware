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

package org.radixware.kernel.common.client.env;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.CantUpdateVersionException;
import org.radixware.kernel.common.client.exceptions.KernelClassModifiedException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.EActualizeAction;
import org.radixware.kernel.starter.radixloader.IActualizeController;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.schemas.product.Directory.FileGroups.FileGroup.GroupType;


public abstract class AdsVersion {       

    public interface VersionListener {

        public void versionUpdated();
    }

    private final static class ActualizationUtils{
        
        private ActualizationUtils(){            
        }
                        
        private static String groupToString(final GroupType.Enum groupType) {
            switch (groupType.intValue()) {
                case GroupType.INT_ADS_COMMON:
                    return "common application definitions";
                case GroupType.INT_ADS_EXPLORER:
                    return "explorer application definitions";
                case GroupType.INT_ADS_SERVER:
                    return "server application definitions";
                case GroupType.INT_DDS:
                    return "database definitions";
                case GroupType.INT_KERNEL_COMMON:
                    return "common kernel";
                case GroupType.INT_KERNEL_EXPLORER:
                    return "explorer kernel";
                case GroupType.INT_KERNEL_SERVER:
                    return "server kernel";
                case GroupType.INT_KERNEL_DESIGNER:
                    return "designer kernel";
                case GroupType.INT_KERNEL_OTHER:
                    return "kernel";
                default:
                    return "unknown";
            }
        }        
        
        private static void printKernelChangesMessage(final MessageProvider mp, final ClientTracer tracer) {
            final String message = 
                mp.translate("ExplorerMessage", "Changes detected in kernel.\nYou should restart explorer to apply changes");        
            tracer.put(EEventSeverity.EVENT, message, EEventSource.EXPLORER);
        }
        
        public static void printListOfChanges(final long versionNum, final Set<String> modifiedFiles, final Set<String> removedFiles, final Set<String> changedGroups, final ClientTracer tracer){
            final StringBuffer message = new StringBuffer("");
            if (changedGroups != null) {
                final List<String> changedGroupNames = new ArrayList<String>();
                String groupName;
                for (String group : changedGroups) {
                    groupName = groupToString(GroupType.Enum.forString(group));
                    if (groupName != null) {
                        changedGroupNames.add(groupName);
                    }
                }
                if (!changedGroupNames.isEmpty()) {
                    message.append(changedGroupNames.get(0));
                    for (int i = 1; i < changedGroupNames.size(); i++) {
                        message.append(", ");
                        message.append(changedGroupNames.get(i));
                    }
                    message.insert(0, "Changes of ");
                    message.append(" detected in version ");
                    message.append(String.valueOf(versionNum));
                }
            }
            if (!modifiedFiles.isEmpty()) {
                message.append("\nFollowing files was modified:");
                for (String fileName : modifiedFiles) {
                    message.append("\n\t");
                    message.append(fileName);
                }
            }
            if (!removedFiles.isEmpty()) {
                message.append("\nFollowing files was removed:");
                for (String fileName : removedFiles) {
                    message.append("\n\t");
                    message.append(fileName);
                }
            }
            tracer.put(EEventSeverity.DEBUG, message.toString(), EEventSource.CLIENT_DEF_MANAGER);
        }
        
        public static boolean isKernelChanged(final Set<String> changedGroups){
            return changedGroups.contains(GroupType.KERNEL_EXPLORER.toString())
                    || changedGroups.contains(GroupType.KERNEL_WEB.toString())
                    || changedGroups.contains(GroupType.KERNEL_COMMON.toString());
        }
    }
    
    //Запрещает подъем версии, если были изменения в файлах, используемых проводником
    final private static class PrimaryActualizeController implements IActualizeController{
        
        private volatile long newVersion = -1;
        private volatile boolean kernelWasChanged = false;
        private volatile boolean wasActualized = false;
        private final boolean isAdsLoaded;
        private final IClientEnvironment environment;
        private final Object changedFilesSemaphore = new Object();
        private final Set<String> changedFiles = new HashSet<>();        
        
        public PrimaryActualizeController(final IClientEnvironment environment, final boolean adsWasLoaded){
            this.environment = environment;
            isAdsLoaded = adsWasLoaded;
        }
        
        public long getNewVersion(){
            return newVersion;
        }
        
        public boolean kernelWasChanged(){
            return kernelWasChanged;
        }
        
        public boolean isNewVersionAccepted(){
            return wasActualized;
        }
        
        public Set<String> getChangedFiles(){
            synchronized(changedFilesSemaphore){
                return changedFiles;
            }
        }        

        @Override
        public EActualizeAction canUpdateTo(final RevisionMeta revisionMeta, final Set<String> modifiedFiles, final Set<String> removedFiles, final Set<String> changedGroups) {            
            newVersion = revisionMeta.getNum();
            ActualizationUtils.printListOfChanges(newVersion, modifiedFiles, removedFiles, changedGroups, environment.getTracer());
            if (changedGroups != null) {
                if (ActualizationUtils.isKernelChanged(changedGroups)) {
                    kernelWasChanged = true;
                    ActualizationUtils.printKernelChangesMessage(environment.getMessageProvider(), environment.getTracer());
                    return EActualizeAction.POSTPONE;
                }
                final String clientGroupType;
                switch (environment.getApplication().getRuntimeEnvironmentType()){
                    case EXPLORER:
                        clientGroupType = GroupType.ADS_EXPLORER.toString();
                        break;
                    case WEB:
                        clientGroupType = GroupType.ADS_WEB.toString();
                        break;
                    default:
                        clientGroupType = GroupType.ADS_CLIENT.toString();
                }
                
                if (isAdsLoaded                        
                        && (changedGroups.contains(clientGroupType)
                        || changedGroups.contains(GroupType.ADS_COMMON.toString()))) {
                    synchronized(changedFilesSemaphore){
                        changedFiles.addAll(modifiedFiles);
                        changedFiles.addAll(removedFiles);
                    }
                    return EActualizeAction.POSTPONE;
                }
            }

            final String message = environment.getMessageProvider().translate("ExplorerMessage", "There are no changes of explorer definitions found in %s version. Just change number of version");
            environment.getTracer().put(EEventSeverity.DEBUG, String.format(message, String.valueOf(newVersion)), EEventSource.CLIENT_DEF_MANAGER);
            wasActualized = true;
            return EActualizeAction.PERFORM_ACTUALIZATION;
        }
        
    }
    
    //Разрешает подъем версии, если не изменялся сам проводник
    final private static class SecondaryActualizeController implements IActualizeController{
        
        private volatile long newVersion = -1;
        private volatile boolean kernelChanged = false;
        private final IClientApplication environment;

        public SecondaryActualizeController(final IClientApplication environment, final long newVersion){
            this.environment = environment;            
            this.newVersion = newVersion;
        }
        
        public long getNewVersion(){
            return newVersion;
        }        
        
        public boolean kernelWasChanged(){
            return kernelChanged;
        }        
        
        @Override
        public EActualizeAction canUpdateTo(RevisionMeta revisionMeta, Set<String> modifiedFiles, Set<String> removedFiles, Set<String> changedGroups) {
            if (revisionMeta.getNum()>newVersion){
                newVersion = revisionMeta.getNum();
                ActualizationUtils.printListOfChanges(newVersion, modifiedFiles, removedFiles, changedGroups, environment.getTracer());
                if (changedGroups != null && ActualizationUtils.isKernelChanged(changedGroups)) {
                    kernelChanged = true;
                    ActualizationUtils.printKernelChangesMessage(environment.getMessageProvider(), environment.getTracer());
                    return EActualizeAction.POSTPONE;
                }
            }
            return EActualizeAction.PERFORM_ACTUALIZATION;
        }        
    }
            
    private final Object releaseLock = new Object();    
    private volatile Release release;    
    private volatile long newVersion = -1;
    private volatile long currentVersion = -1;
    private volatile boolean kernelWasChanged;
    private volatile boolean isSupported = true;
    private volatile boolean isActualized = true;
    private volatile Collection<Id> changedDefinitionIds;
    private final List<VersionListener> listeners = new LinkedList<>();
    private final VersionListener versionListener = new VersionListener() {

        @Override
        public void versionUpdated() {
            synchronized (listeners) {
                final List<VersionListener> versionListenersCopy = new LinkedList<>();
                versionListenersCopy.addAll(listeners);
                for (VersionListener l : versionListenersCopy) {
                    l.versionUpdated();
                }
            }
        }
    };

    public void addVersionListener(VersionListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeVersionListener(VersionListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    protected final IClientApplication env;

    public AdsVersion(IClientApplication env) {
        this.env = env;
    }
    
    private boolean creatingRelease = false;

    Release release(final boolean showWaitDialog) {
        synchronized (releaseLock) {
            if (release == null) {
                if (currentVersion == -1 && RadixLoader.getInstance() != null) {
                    try {
                        currentVersion = RadixLoader.getInstance().getCurrentRevision();
                    } catch (RadixLoaderException ex) {
                        throw new RadixError("Unnable to init definition manager: Cannot get current definition's version", ex);
                    }
                } else if (isNewVersionAvailable() && !isKernelWasChanged()) {
                    updateToNewVersion();
                    if (release != null) {
                        return release;
                    }
                }
                if (creatingRelease) {
                    return null;
                }
                creatingRelease = true;
                try {
                    release = new Release(env, currentVersion, showWaitDialog);
                } finally {
                    creatingRelease = false;
                }
                if (RadixLoader.getInstance()!=null){
                    env.getTracer().put(EEventSeverity.DEBUG, RadixLoader.getInstance().getDescription(), EEventSource.CLIENT_DEF_MANAGER);
                }                
                final String mess = env.getMessageProvider().translate("TraceMessage", "Explorer definition manager was initialized. Number of current definition version is %s");
                env.getTracer().put(EEventSeverity.DEBUG, String.format(mess, currentVersion), EEventSource.CLIENT_DEF_MANAGER);
                setupContextClassLoader(release.getClassLoader().getRadixClassLoader());
            }
            return release;
        }
    }
    
    protected void setupContextClassLoader(final RadixClassLoader rxClassLoader){
        //Библиотека XmlBeans кэширует классы и ресурсы, загруженные через контекстный ClassLoader. 
        //Чтобы сбросить этот кэш нужно поменять ссылку на ClassLoader.
        Thread.currentThread().setContextClassLoader(rxClassLoader);
    }

    public boolean isNewVersionAvailable() {
        synchronized (releaseLock) {
            return currentVersion < newVersion;
        }
    }

    public boolean isKernelWasChanged() {
        synchronized (releaseLock) {
            return kernelWasChanged;
        }
    }

    public void setNewVersion(final long version) {
        synchronized (releaseLock) {
            final String message = env.getMessageProvider().translate("ExplorerMessage", "New definition version with number %s detected.");
            env.getTracer().put(EEventSeverity.EVENT, String.format(message, String.valueOf(version)), EEventSource.CLIENT_DEF_MANAGER);
            newVersion = version;
        }
    }

    protected void afterUpdateToNewVersion(final Integer oldClassLoaderId){
        env.getImageManager().clearCache(true);
    }

    public final void updateToNewVersion() throws CantUpdateVersionException{        
        synchronized (releaseLock) {
            if (!kernelWasChanged){
                final SecondaryActualizeController acontroller = new SecondaryActualizeController(env, newVersion);
                actualizeRadixLoader(acontroller, null);
                if (acontroller.getNewVersion()>0){
                    newVersion = acontroller.getNewVersion();
                }
                kernelWasChanged = acontroller.kernelWasChanged();
            }
            if (kernelWasChanged) {
                if (confirmToRestart()) {
                    restartApplication();
                    throw new KernelClassModifiedException(true);
                }else{
                    throw new KernelClassModifiedException(false);
                }
            }
            if (newVersion >= 0) {
                final RadixClassLoader oldClassLoader = getRadixClassLoader();
                final Integer oldClassLoaderId = oldClassLoader==null ? null : oldClassLoader.hashCode();                
                env.getEnvironmentCache().invalidateUserSessions();
                if (release!=null){
                    release.getClassLoader().close();
                    release.clearCache();
                }
                release = null;
                changedDefinitionIds = null;
                currentVersion = newVersion;
                isSupported = true;
                newVersion = -1;
                final String message = env.getMessageProvider().translate("ExplorerMessage", "Update to version %s complete.");
                env.getTracer().put(EEventSeverity.EVENT, String.format(message, String.valueOf(currentVersion)), EEventSource.CLIENT_DEF_MANAGER);                
                versionUpdated();
                afterUpdateToNewVersion(oldClassLoaderId);
            }
        }
    }
    
    protected final RadixClassLoader getRadixClassLoader(){
        synchronized (releaseLock) {
            return release==null ? null : release.getClassLoader().getRadixClassLoader();
        }
    }

    public final void makeUnsupported() {
        synchronized (releaseLock) {
            isSupported = false;
        }
    }

    public boolean isSupported() {
        synchronized (releaseLock) {
            return isSupported;
        }
    }

    public boolean isActualized() {
        synchronized (releaseLock) {
            return isActualized;
        }
    }

    public void clear() {

        /*
         * remove entire release in clean method. Classloader makes cashe of
         * classes; auto-generated classes contains static fields with instances
         * of meta-info: so it\`s not enaph just clear cache of instances
         * meta-info classes to reinit it and we must use of another classloader
         * instance
         */
        synchronized (releaseLock) {
            
            if (release!=null){
                release.getClassLoader().close();
                release.clearCache();
            }
            release = null;
        }
        
        synchronized (listeners){
            listeners.clear();
        }
    }

    public long getNumber() {
        if (release == null){
            release(false);//init version
        }
        synchronized (releaseLock) {
            return currentVersion;
        }
    }
    
    public long getLastVersionNumber(){
        if (release == null){
            release(false);//init version
        }        
        synchronized (releaseLock) {
            return Math.max(newVersion,currentVersion);
        }
    }

    private String getKernelChangesMessage() {
        return env.getMessageProvider().translate("ExplorerMessage", "Changes detected in kernel.\nYou should restart explorer to apply changes");
    }

    protected boolean confirmToRestart() {
        return false;
    }

    protected abstract void restartApplication();

    private void versionUpdated() {
        if (versionListener != null) {
            versionListener.versionUpdated();
        }
    }

    protected abstract void versionNumberUpdated();
    
    private void actualizeRadixLoader(final IActualizeController controller, final IClientEnvironment contextEnvironment) throws CantUpdateVersionException{
        RadixLoader.getInstance().setActualizeController(controller);
        if (contextEnvironment==null || !contextEnvironment.getApplication().isInGuiThread()){
            try{
                isActualized = false;
                RadixLoader.getInstance().actualize(null, null, null, null);
                isActualized = true;
            }catch(RadixLoaderException exception){
                throw new CantUpdateVersionException("Error occuring during actualize version: " + exception.getMessage());
            }
        }else{
            try {
                final Callable<Set<String>> task = new Callable<Set<String>>() {
                    @Override
                    public Set<String> call() throws RadixLoaderException {
                        return RadixLoader.getInstance().actualize(null, null, null, null);
                    }
                };                    
                final ITaskWaiter waiter = contextEnvironment.getApplication().newTaskWaiter();
                waiter.setMessage(contextEnvironment.getMessageProvider().translate("ExplorerMessage", "Loading version..."));            
                try {
                    isActualized = false;
                    waiter.runAndWait(task);
                    isActualized = true;
                } finally {
                    waiter.close();
                }
            }catch (InterruptedException ex) {
                contextEnvironment.getTracer().warning("Actualizing version was interrupted");
                throw new CantUpdateVersionException("Actualizing version was interrupted");
            } catch (ExecutionException ex) {
                final Throwable exception = ex.getCause();
                contextEnvironment.getTracer().error("Error occuring during actualize version", exception);
                throw new CantUpdateVersionException("Error occuring during actualize version: " + exception.getMessage());
            }
        }
    }
        
    public Collection<Id> checkForUpdates(final IClientEnvironment contextEnvironment) throws CantUpdateVersionException {
        synchronized (releaseLock) {
            if (kernelWasChanged) {
                return Collections.emptyList();
            }
            final PrimaryActualizeController acontroller = new PrimaryActualizeController(contextEnvironment, release!=null);
            actualizeRadixLoader(acontroller, contextEnvironment);

            final long version = acontroller.getNewVersion();
            if (version>=0){
                if (changedDefinitionIds != null && version == newVersion) {
                    return changedDefinitionIds;
                }
                if (acontroller.isNewVersionAccepted()){
                    if (release!=null){
                        versionNumberUpdated();
                    }
                    currentVersion = version;
                    return Collections.emptyList();
                }else{
                    if (acontroller.kernelWasChanged()){
                        kernelWasChanged = true;
                        setNewVersion(version);
                        return Collections.emptyList();
                    }
                    if (changedDefinitionIds == null) {
                        changedDefinitionIds = new ArrayList<>();
                    }
                    setNewVersion(version);
                    final IProgressHandle ph = startChangeAnalyse(contextEnvironment);
                    final ReleaseRepository repository = release(true).getClassLoader().getRepository();
                    try {
                        changedDefinitionIds.addAll(repository.getDefinitionIdsForJarFiles(acontroller.getChangedFiles()));
                    } finally {
                        finishChangeAnalyse(ph);
                    }
                    return changedDefinitionIds;            
                }                
            }            
        }
        contextEnvironment.getTracer().put(EEventSeverity.DEBUG, contextEnvironment.getMessageProvider().translate("ExplorerMessage", "version was not changed"), EEventSource.CLIENT_DEF_MANAGER);
        return null;
    }

    protected abstract IProgressHandle startChangeAnalyse(IClientEnvironment contextEnvironment);

    protected abstract void finishChangeAnalyse(IProgressHandle progress);
}
