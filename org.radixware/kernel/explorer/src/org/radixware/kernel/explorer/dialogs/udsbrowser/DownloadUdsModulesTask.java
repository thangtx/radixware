/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.udsbrowser;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.env.progress.ICancellableTask;
import org.radixware.kernel.explorer.env.progress.IMeasurableTask;
import org.radixware.kernel.explorer.env.progress.ITitledTask;
import org.radixware.kernel.starter.radixloader.IRepositoryEntry;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


final class DownloadUdsModulesTask implements Callable<DownloadingResult>, ICancellableTask, IMeasurableTask, ITitledTask{
        
    private final static String MODULE_XML_FILE_NAME = "module.xml";
    
    private final static class UdsModuleEntry{
        
        private final IRepositoryEntry repositoryEntry;
        private final UdsModuleNode udsModule;

        public UdsModuleEntry(final UdsModuleNode udsModule, final IRepositoryEntry repositoryEntry) {
            this.repositoryEntry = repositoryEntry;
            this.udsModule = udsModule;
        }

        public IRepositoryEntry getRepositoryEntry() {
            return repositoryEntry;
        }

        public UdsModuleNode getUdsModule() {
            return udsModule;
        }        
    }
    
    private final File targetDirectory;
    private final IClientEnvironment environment;
    private final List<UdsModuleNode> udsModules = new LinkedList<>();
    private final Object semaphore = new Object();
    private final long releaseVersion;
    private final String downloadMsgTemplate;
    private final List<File> downloadedFiles = new LinkedList<>();
    private final List<String> excludedDirectories = new LinkedList<>();
    private boolean alwaysRemoveDir;
    private boolean alwaysRemoveFile;
    
    private boolean wasCancelled;    
    private String title;
    private int maxValue = 0;
    private int currentValue = -1;
    
    public DownloadUdsModulesTask(final List<UdsModuleNode> modules, final File targetDirectory, final IClientEnvironment environment){
        this.targetDirectory = targetDirectory;
        this.environment = environment;
        releaseVersion = environment.getDefManager().getAdsVersion().getNumber();
        udsModules.addAll(modules);
        title = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Preparing for download");
        downloadMsgTemplate = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Downloading '%1$s'");
    }

    @Override
    public DownloadingResult call() throws Exception {
        final List<UdsModuleEntry> entriesToDownload = new LinkedList<>();
        for (UdsModuleNode module: udsModules){
            collectDownloadingEntries(module, entriesToDownload);
        }
        if (wasCancelled() || entriesToDownload.isEmpty()){
            return DownloadingResult.EMPTY;
        }
        if (entriesToDownload.size()>1){
            synchronized(semaphore){
                maxValue = entriesToDownload.size();
            }
        }
        final Set<UdsModuleNode> downloadedModules = new HashSet<>();
        File resultFile;
        IRepositoryEntry entry;
        UdsModuleEntry moduleEntry;
        for (int i=0,count=entriesToDownload.size(); i<count && !wasCancelled(); i++){
            moduleEntry = entriesToDownload.get(i);
            entry = moduleEntry.getRepositoryEntry();
            updateProgress(i, entry.getPath());
            resultFile = download(entry);
            if (resultFile!=null){
                synchronized(semaphore){
                    downloadedFiles.add(resultFile);
                }
                downloadedModules.add(moduleEntry.getUdsModule());
            }
        }
        return new DownloadingResult(downloadedFiles, downloadedModules.size());
    }
    
    private void collectDownloadingEntries(final UdsModuleNode module, final List<UdsModuleEntry> collectTo) throws RadixLoaderException{
        final Collection<IRepositoryEntry> childEntries = 
            RadixLoader.getInstance().listDir(module.getRepositoryPath(), releaseVersion, false);
        for (IRepositoryEntry entry: childEntries){
            if (UdsModulesDownloader.isDownloadableDirectory(entry)){
                collectDownloadingEntries(module, entry, collectTo);
            }
            if (wasCancelled()){
                break;
            }
        }
    }
    
    private void collectDownloadingEntries(final UdsModuleNode module, final IRepositoryEntry entry, final List<UdsModuleEntry> collectTo) throws RadixLoaderException{
        final Stack<IRepositoryEntry> entries = new Stack<>();
        entries.push(entry);
        IRepositoryEntry dirEntry;
        while(!entries.isEmpty() && !wasCancelled()){
            dirEntry = entries.pop();
            final Collection<IRepositoryEntry> childEntries = 
                RadixLoader.getInstance().listDir(dirEntry.getPath(), releaseVersion, false);
            for (IRepositoryEntry childEntry: childEntries){
                if (childEntry.isDirectory()){
                    entries.push(childEntry);
                }else{
                    collectTo.add(new UdsModuleEntry(module, childEntry));
                }
            }
        }
    }
    
    private File download(final IRepositoryEntry entry){
        final String path =entry.getPath();
        final File targetFile = new File(targetDirectory, fixPath(path));
        if ((!targetFile.exists() || confirmToOverwrite(targetFile)) && ensureDir(targetFile.getParentFile())){
            final byte[] data;
            try{
                data = RadixLoader.getInstance().export(path, releaseVersion);
            }catch(IOException exception){
                final String traceMessageTemplate = 
                    environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Failed to read file '%1$s'.");
                if (confirmToContinue(String.format(traceMessageTemplate, path), exception)){
                    return null;                    
                }else{
                    throw new CancellationException();
                }
            }
            try{
                FileUtils.writeBytes(targetFile, data);
            }catch(IOException exception){
                final String traceMessageTemplate = 
                    environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Failed to write file '%1$s'.");
                if (confirmToContinue(String.format(traceMessageTemplate, path), exception)){
                    return null;                    
                }else{
                    throw new CancellationException();
                }                
            }
            return targetFile;
        }else{
            return null;
        }
    }
    
    private static String fixPath(final String path){
        return path.replaceFirst("\\/uds\\/", "/");
    }
    
    private boolean ensureDir(final File dir){
        if (excludedDirectories.contains(dir.getAbsolutePath())){
            return false;
        }
        if (dir.exists()){
            if (dir.isDirectory()){
                return true;
            } else {
                if (alwaysRemoveFile){
                    deleteFile(dir);
                }else{
                    final String messageTitle = 
                        environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Confirm to Delete File");
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Unable to create directory '%1$s' because there is a file with the same name.\nDo you want to delete this file?");
                    final String message = String.format(messageTemplate, dir.getAbsolutePath());
                    final EnumSet<EDialogButtonType> buttonTypes = 
                            EnumSet.of(EDialogButtonType.YES, EDialogButtonType.YES_TO_ALL, EDialogButtonType.NO, EDialogButtonType.ABORT);
                    final EDialogButtonType button =
                        environment.messageBox(messageTitle, message, EDialogIconType.QUESTION, buttonTypes);
                    switch(button){
                        case YES:{
                            deleteFile(dir);
                            break;
                        }case YES_TO_ALL:{
                            deleteFile(dir);
                            alwaysRemoveFile = true;
                            break;
                        }case ABORT:{
                            throw new CancellationException();
                        }default:{
                            excludedDirectories.add(dir.getAbsolutePath());
                            return false;
                        }
                    }
                }
            }
        }
        
        final File canonFile;
        try {
            canonFile = dir.getCanonicalFile();
        } catch (IOException e) {
            final String traceMessageTemplate = 
                environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Failed to create directory '%1$s'.");            
            if (confirmToContinue(String.format(traceMessageTemplate, dir.getAbsolutePath()), e)){
                excludedDirectories.add(dir.getAbsolutePath());
                return false;
            }else{
                throw new CancellationException();
            }
        }
        
        final File parent = canonFile.getParentFile();
        if (parent==null){
            final String messageTemplate = 
                environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Failed to create directory '%1$s'.");            
            if (confirmToContinue(String.format(messageTemplate, dir.getAbsolutePath()), null)){
                excludedDirectories.add(dir.getAbsolutePath());
                return false;
            }else{
                throw new CancellationException();
            }            
        }
                
        if (ensureDir(parent)){
            if (dir.mkdir()){
                return true;
            }else{
                final String messageTemplate = 
                    environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Failed to create directory '%1$s'.");
                if (confirmToContinue(String.format(messageTemplate, dir.getAbsolutePath()), null)){
                    excludedDirectories.add(dir.getAbsolutePath());
                    return false;
                }else{
                    throw new CancellationException();
                }
            }
        }else{            
            return false;
        }
    }
    
    private boolean confirmToContinue(final String msg, final IOException exception){
        if (exception!=null){
            environment.getTracer().error(msg, exception);
        }
        final String msgTitle = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Confirm to Continue");
        final String question = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Do you want to continue?");
        return environment.messageConfirmation(msgTitle, msg+"\n"+question);
    }
    
    private boolean confirmToOverwrite(final File targetFile){
        if (targetFile.isDirectory()){
            if (alwaysRemoveDir){
                deleteDirectory(targetFile);
                return true;
            }
            final String messageTitle = 
                environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Confirm to Delete Directory");
            final String messageTemplate = 
                environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Unable to write file '%1$s' because there is a directory with the same name.\nDo you want to delete this directory?");
            final String message = String.format(messageTemplate, targetFile.getAbsolutePath());
            final EnumSet<EDialogButtonType> buttonTypes = 
                    EnumSet.of(EDialogButtonType.YES, EDialogButtonType.YES_TO_ALL, EDialogButtonType.NO, EDialogButtonType.ABORT);
            final EDialogButtonType button =
                environment.messageBox(messageTitle, message, EDialogIconType.QUESTION, buttonTypes);
            switch(button){
                case YES:{
                    deleteDirectory(targetFile);
                    return true;
                }case YES_TO_ALL:{
                    deleteDirectory(targetFile);
                    alwaysRemoveDir = true;
                    return true;
                }case ABORT:{
                    throw new CancellationException();
                }default:{
                    return false;
                }                    
            }
        }else{
            if (alwaysRemoveFile){
                deleteFile(targetFile);
                return true;
            }
            final String messageTitle = 
                environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Confirm to Overwrite");
            final String messageTemplate = 
                environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Unable to write file '%1$s' because another file with the same name exists.\nDo you want to overwite this file?");            
            final String message = String.format(messageTemplate, targetFile.getAbsolutePath());
            final EnumSet<EDialogButtonType> buttonTypes = 
                    EnumSet.of(EDialogButtonType.YES, EDialogButtonType.YES_TO_ALL, EDialogButtonType.NO, EDialogButtonType.ABORT);
            final EDialogButtonType button =
                environment.messageBox(messageTitle,  message, EDialogIconType.QUESTION, buttonTypes);
            switch(button){
                case YES:{
                    deleteFile(targetFile);
                    return true;
                }case YES_TO_ALL:{
                    deleteFile(targetFile);
                    alwaysRemoveFile = true;
                    return true;
                }case ABORT:{
                    throw new CancellationException();
                }default:{
                    return false;
                }                    
            }            
        }
    }
    
    private void deleteDirectory(final File dir){
        if (!FileUtils.deleteDirectory(dir)){
            final String msgTitle = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Confirm to Continue");
            final String questionTemplate = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Unable to delete directory '%1$s'.\nDo you want to continue?");
            if ( !environment.messageConfirmation(msgTitle, String.format(questionTemplate, dir.getAbsolutePath())) ){
                throw new CancellationException();
            }
        }
    }
    
    private void deleteFile(final File file){
        if (!FileUtils.deleteFile(file)){
            final String msgTitle = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Confirm to Continue");
            final String questionTemplate = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Unable to delete file '%1$s'.\nDo you want to continue?");
            if ( !environment.messageConfirmation(msgTitle, String.format(questionTemplate, file.getAbsolutePath())) ){
                throw new CancellationException();
            }
        }
    }    

    @Override
    public boolean cancel() {
        synchronized(semaphore){
            wasCancelled = true;
        }
        return true;
    }    

    @Override
    public int getProgressMaxValue() {
        synchronized(semaphore){
            return maxValue;
        }
    }

    @Override
    public int getProgressCurValue() {
        synchronized(semaphore){
            return currentValue;
        }
    }

    @Override
    public String getTitle() {
        synchronized(semaphore){
            return title;
        }
    }
    
    public final boolean wasCancelled(){
        synchronized(semaphore){
            return wasCancelled;
        }
    }
    
    public List<File> getDownloadedFiles(){
        synchronized(semaphore){
            return downloadedFiles;
        }
    }
    
    private void updateProgress(final int value, final String fileName){
        synchronized(semaphore){
            currentValue = value;
            title = String.format(downloadMsgTemplate, fileName);
        }
    }
}