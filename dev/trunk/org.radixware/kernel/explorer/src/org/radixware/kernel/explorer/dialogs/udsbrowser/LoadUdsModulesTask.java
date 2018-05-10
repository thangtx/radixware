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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.xml.parsers.SAXParser;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.progress.ICancellableTask;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.IRepositoryEntry;
import org.radixware.kernel.starter.radixloader.RadixDirLoader;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


final class LoadUdsModulesTask implements Callable<List<LayerNode>>, ICancellableTask {
    
    private final static class ModuleXmlSaxHandler extends DefaultHandler{
        
        private Id moduleId;
        private String name;
        private String description;
        private boolean inDescriptionElement;
        private StringBuilder descriptionBuilder;        

        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            if ("Module".equals(qName)){
                moduleId = Id.Factory.loadFrom(attributes.getValue("Id"));
                name = attributes.getValue("Name");
            }else if ("Description".equals(qName)){
                inDescriptionElement  =true;
            }
        }
        
        @Override
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            if (inDescriptionElement && length>0){
                if (descriptionBuilder==null){
                    descriptionBuilder = new StringBuilder();                    
                }
                descriptionBuilder.append(ch, start, length);
            }
        }        

        @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            if ("Description".equals(localName)){
                inDescriptionElement = false;
                if (descriptionBuilder!=null){
                    description = descriptionBuilder.toString();
                }                
            }
        }
        
        public Id getModuleId(){
            return moduleId;
        }
        
        public String getModuleName(){
            return name;
        }
        
        public String getModuleDescription(){
            return description;
        }
        
        public boolean isValid(){
            return moduleId!=null && name!=null && !name.isEmpty();
        }
    }
    
    private final static class LayerXmlSaxHandler extends DefaultHandler{
        
        private String layerUri;
        private String name;
        private String title;
        private boolean isLocalizing;       
        private boolean firstLayerElement = true;
        
        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            if (firstLayerElement && "Layer".equals(qName)){
                firstLayerElement = false;
                layerUri = attributes.getValue("Uri");
                name = attributes.getValue("Name");
                title = attributes.getValue("Title");
                isLocalizing = "true".equals(attributes.getValue("IsLocalizing"));
            }
        }
        
        public String getLayerUri(){
            return layerUri;
        }
        
        public String getLayerName(){
            return name;
        }
        
        public String getLayerTitle(){
            return title;
        }
        
        public boolean isLocalizingLayer(){
            return isLocalizing;
        }
        
        public boolean isValid(){
            return layerUri!=null && !layerUri.isEmpty();
        }
    }        

    private final static String LAYER_XML_FILE_NAME = "layer.xml";
    private final static String MODULE_XML_FILE_NAME = "module.xml";
    private final static String UDS_SEGMENT_NAME = "uds";
       
    private final IClientEnvironment environment;
    private final SAXParser saxParser;
    private final long releaseVersion;
    private final Object semaphore = new Object();
    private boolean wasCancelled;

    public LoadUdsModulesTask(final IClientEnvironment environment, final SAXParser saxParser) {
        this.environment = environment;
        this.saxParser = saxParser;
        releaseVersion = environment.getDefManager().getAdsVersion().getNumber();
    }

    @Override
    public List<LayerNode> call() throws Exception {
        final Collection<IRepositoryEntry> rootEntries = 
            RadixLoader.getInstance().listDir("", releaseVersion, false);
        final Map<String,LayerNode> nodesByUri = new HashMap<>();
        LayerNode node;
        for (IRepositoryEntry entry: rootEntries){
            if (wasCancelled()){
                return Collections.emptyList();
            }
            if (entry.isDirectory()){
                node = loadLayerNode(entry);
                if (node!=null){
                    nodesByUri.put(node.getUri(), node);
                }
            }
        }
        final List<LayerMeta> layers =
            environment.getDefManager().getClassLoader().getRevisionMeta().getAllLayersSortedFromBottom();
        final List<LayerNode> nodes = new ArrayList<>();
        for (LayerMeta layer: layers){
            node = nodesByUri.get(layer.getUri());
            if (node!=null){
                nodes.add(node);
            }
        }        
        if (wasCancelled()){
            return Collections.emptyList();
        }        
        return nodes;
    }

    @Override
    public boolean cancel() {
        synchronized(semaphore){
            wasCancelled = true;            
        }        
        return true;
    }

    public boolean wasCancelled(){
        synchronized(semaphore){
            return wasCancelled;
        }
    }
    
    private LayerNode loadLayerNode(final IRepositoryEntry layerEntry) throws RadixLoaderException{
        final Collection<IRepositoryEntry> layerEntries = 
            RadixLoader.getInstance().listDir(layerEntry.getPath(), releaseVersion, false);
        IRepositoryEntry layerXmlEntry=null, udsDirEntry=null;
        for (IRepositoryEntry entry: layerEntries){
            if (LAYER_XML_FILE_NAME.equals(entry.getName()) && !entry.isDirectory()){
                layerXmlEntry = entry;
            }else if (UDS_SEGMENT_NAME.equalsIgnoreCase(entry.getName()) && entry.isDirectory()){
                udsDirEntry = entry;
            }
        }
        if (layerXmlEntry!=null && udsDirEntry!=null && !wasCancelled()){            
            final byte[] layerXmlData;
            try{
                layerXmlData = RadixDirLoader.getInstance().export(layerXmlEntry.getPath(), releaseVersion);
            }catch(RadixLoaderException exception){
                final String messageTemplate = 
                    environment.getMessageProvider().translate("TraceMessage", "Failed to read '%1$s' file.");
                environment.getTracer().error(String.format(messageTemplate, layerEntry.getPath()), exception);
                return null;
            }
            
            final List<UdsModuleNode> udsNodes = loadUdsModules(udsDirEntry);
            if (!udsNodes.isEmpty()){
                final ByteArrayInputStream layerXmlInputStream = new ByteArrayInputStream(layerXmlData);
                final LayerXmlSaxHandler saxHandler = new LayerXmlSaxHandler();            
                try{
                    saxParser.parse(layerXmlInputStream, saxHandler);
                }catch(IOException | SAXException exception){
                    final String messageTemplate = 
                        environment.getMessageProvider().translate("TraceMessage", "Failed to load '%1$s' layer.");
                    environment.getTracer().error(String.format(messageTemplate, layerEntry.getPath()), exception);
                    return null;
                }
                if (saxHandler.isValid() && !saxHandler.isLocalizingLayer() && !wasCancelled()){
                    return new LayerNode(layerEntry, 
                                                       saxHandler.getLayerUri(), 
                                                       saxHandler.getLayerName(),
                                                       saxHandler.getLayerTitle(),
                                                       udsNodes);
                }
            }
        }
        return null;        
    }    
    
    private List<UdsModuleNode> loadUdsModules(final IRepositoryEntry udsDirEntry) throws RadixLoaderException{
        final Collection<IRepositoryEntry> udsEntries =
            RadixLoader.getInstance().listDir(udsDirEntry.getPath(), releaseVersion, false);
        final List<UdsModuleNode> nodes = new ArrayList<>();
        UdsModuleNode node;
        for (IRepositoryEntry entry: udsEntries){
            if (wasCancelled()){
                break;
            }
            if (entry.isDirectory() && isModuleDownloadable(entry)){
                node = loadUdsModule(entry);
                if (node!=null){
                    nodes.add(node);
                }
            }
        }
        Collections.sort(nodes, new Comparator<UdsModuleNode>(){
            @Override
            public int compare(UdsModuleNode o1, UdsModuleNode o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }            
        });
        return nodes;
    }    
    
    private UdsModuleNode loadUdsModule(final IRepositoryEntry udsModuleEntry){
        final String fileName = udsModuleEntry.getPath()+"/"+MODULE_XML_FILE_NAME;
        final byte[] moduleXmlData;
        try{
            moduleXmlData = RadixLoader.getInstance().export(fileName, releaseVersion);
        }catch(RadixLoaderException exception){
            final String messageTemplate = 
                environment.getMessageProvider().translate("TraceMessage", "Failed to read '%1$s' file.");
            environment.getTracer().error(String.format(messageTemplate, fileName), exception);
            return null;            
        }
        final ByteArrayInputStream moduleXmlInputStream = new ByteArrayInputStream(moduleXmlData);
        final ModuleXmlSaxHandler saxHandler = new ModuleXmlSaxHandler();
        try{
            saxParser.parse(moduleXmlInputStream, saxHandler);
        }catch(SAXException | IOException exception){
            final String messageTemplate = 
                environment.getMessageProvider().translate("TraceMessage", "Failed to read '%1$s' file.");
            environment.getTracer().error(String.format(messageTemplate, fileName), exception);
            return null;            
        }
        if (saxHandler.isValid()){            
            return new UdsModuleNode(udsModuleEntry, 
                                                       saxHandler.getModuleId(),
                                                       saxHandler.getModuleName(), 
                                                       saxHandler.getModuleDescription());
        }else{
            return null;
        }
    }
    
    private boolean isModuleDownloadable(final IRepositoryEntry udsModuleEntry) throws RadixLoaderException{
        final Collection<IRepositoryEntry> udsModuleContent = 
                RadixLoader.getInstance().listDir(udsModuleEntry.getPath(), releaseVersion, false);
        boolean isModuleXmlFileExists = false, isDownloadableDirExists = false;
        for (IRepositoryEntry entry: udsModuleContent){
            if (entry.isDirectory()){
                if (UdsModulesDownloader.isDownloadableDirectory(entry)){
                    isDownloadableDirExists = true;
                }
            }else if (MODULE_XML_FILE_NAME.equals(entry.getName())){                
                    isModuleXmlFileExists = true;
            }
            if (isDownloadableDirExists && isModuleXmlFileExists){
                return true;
            }
        }        
        return false;
    }
}
