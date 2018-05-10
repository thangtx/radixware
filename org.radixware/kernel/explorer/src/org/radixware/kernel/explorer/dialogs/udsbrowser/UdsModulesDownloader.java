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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.starter.radixloader.IRepositoryEntry;
import org.xml.sax.SAXException;


public class UdsModulesDownloader {
    
    private final static String DOWNLOAD_DIR_PATH_CONFIG_KEY = 
        SettingNames.SYSTEM+"/path_to_uds_download_dir";
    private final static String[] DOWNLOADABLE_DIRECTORIES = new String[]{"src","etc"};
    
    private static List<LayerNode> cachedNodes = null;
    private static long cacheVersion=-1;    
    
    private UdsModulesDownloader(){
    }
            
    public static List<File> download(final IClientEnvironment environment, final QWidget parentWidget){
        final List<LayerNode> layerNodes;
        try{
            layerNodes = loadLayerNodes(environment, parentWidget);
        }catch(BranchNodesLoadException exception){
            final String messageTemplate = environment.getMessageProvider().translate("ExplorerException", "Failed to load UDS modules.");
            environment.processException(messageTemplate, exception);
            return Collections.emptyList();
        }catch(CancellationException exception){
            return Collections.emptyList();
        }
        if (layerNodes.isEmpty()){
            final String messageTemplate = environment.getMessageProvider().translate("ExplorerMessage", "No UDS modules found.");
            environment.messageInformation("",messageTemplate);
            return Collections.emptyList();
        }
        final UdsModulesSelectorDialog dialog = new UdsModulesSelectorDialog(layerNodes, environment, parentWidget);
        dialog.setWindowTitle(environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Select UDS Modules"));
        if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
            return doDownload(dialog.getSelectedModules(), environment, parentWidget);
        }else{
            return Collections.emptyList();
        }
    }    
    
    private static List<LayerNode> loadLayerNodes(final IClientEnvironment environment, final QObject parent) throws BranchNodesLoadException, CancellationException {
        if (cachedNodes==null || cacheVersion!=environment.getDefManager().getAdsVersion().getNumber()){
            final TaskWaiter taskWaiter = new TaskWaiter(environment, parent);
            try{
                taskWaiter.setCanBeCanceled(true);
                taskWaiter.setMessage(environment.getMessageProvider().translate("ExplorerMessage", "Loading UDS Modules..."));
                final SAXParser saxParser;
                try{
                    saxParser = SAXParserFactory.newInstance().newSAXParser();
                }catch(ParserConfigurationException | SAXException exception){
                    throw new BranchNodesLoadException(exception);
                }
                final LoadUdsModulesTask task = new LoadUdsModulesTask(environment, saxParser);
                final List<LayerNode> loadedNodes;
                try{
                    loadedNodes = taskWaiter.runAndWait(task);
                }catch(InterruptedException exception){
                    throw new CancellationException();
                }catch(ExecutionException exception){
                    throw new BranchNodesLoadException(exception.getCause());
                }
                if (task.wasCancelled()){
                    throw new CancellationException();
                }
                cachedNodes = loadedNodes;
                cacheVersion = environment.getDefManager().getAdsVersion().getNumber();
            }finally{
                taskWaiter.close();
            }
        }
        return cachedNodes;
    }    
    
    private static List<File> doDownload(final List<UdsModuleNode> modules, 
                                                            final IClientEnvironment environment, 
                                                            final QWidget parentWidget){
        final String title = 
            environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Select directory to save modules");
        final String initialDir = 
            environment.getConfigStore().readString(DOWNLOAD_DIR_PATH_CONFIG_KEY, System.getProperty("user.home"));
        final String path = QFileDialog.getExistingDirectory(parentWidget, title, initialDir);
        if (path!=null && !path.isEmpty()){
            environment.getConfigStore().writeString(DOWNLOAD_DIR_PATH_CONFIG_KEY, path);
        }
        final DownloadUdsModulesTask task = 
            new DownloadUdsModulesTask(modules, new File(path), environment);
        final TaskWaiter taskWaiter = new TaskWaiter(environment, parentWidget);
        final DownloadingResult result;
        try{            
            taskWaiter.setCanBeCanceled(true);            
            try{
                result = taskWaiter.runAndWait(task);
                completeMessage(environment, result);
            }catch(InterruptedException exception){
                return task.getDownloadedFiles();
                //ignore
            }catch(ExecutionException exception){
                final Throwable cause = exception.getCause();
                if (cause instanceof InterruptedException==false
                    && cause instanceof CancellationException==false){
                    final String message = 
                        environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Failed to export modules");
                    environment.processException(message, cause);
                }
                return task.getDownloadedFiles();
            }            
        }finally{
            taskWaiter.close();
        }
        return result.getFiles();
    }
    
    private static void completeMessage(final IClientEnvironment environment, final DownloadingResult result){
        final int modulesCount = result.getModulesCount();
        final int filesCount = result.getFiles().size();
        final String title = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Export Complete");
        final String message = environment.getMessageProvider().translate("UdsModulesSelectorDialog", "Number of exported modules: %1$s\nNumber of exported files: %2$s");
        environment.messageInformation(title, String.format(message, modulesCount, filesCount));
    }
    
    static boolean isDownloadableDirectory(final IRepositoryEntry entry){
        if (entry.isDirectory()){
            for (String name: DOWNLOADABLE_DIRECTORIES){
                if (name.equalsIgnoreCase(entry.getName())){
                    return true;
                }
            }
        }
        return false;
    }
}
