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

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.sqml.*;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.meta.DirectoryMeta;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SqmlDefinitions implements ISqmlDefinitions{
        
    private final static String DIRECTORY_XML_SHORT_FILE_NAME = "directory.xml";
    private final static String SQML_DEFINITIONS_XML_SHORT_FILE_NAME = "sqmldefs.xml";
    private final static ILoadingHandler DUMMY_HANDLER = new ILoadingHandler() {
        @Override
        public boolean wasCancelled() {
            return false;
        }
    };    
    
    public final static class LoadingHandler implements ILoadingHandler{
        
        private final Object semaphore = new Object();
        
        private boolean wasCancelled;
        
        public LoadingHandler(){
        }
        
        @Override
        public boolean wasCancelled(){
            synchronized(semaphore){
                return wasCancelled;
            }
        }
        
        public void cancel(){
            synchronized(semaphore){
                wasCancelled = true;
            }
        }
    }
    
    private final IClientEnvironment environment;
    private final SqmlRepository repository;
    private volatile boolean wasLoaded;
    
    public SqmlDefinitions(final IClientEnvironment environment){
        this.environment = environment;
        repository = new SqmlRepository(environment);
    }
    
    public boolean loadDefinitions() throws ParserConfigurationException, SAXException, IOException{
        return loadDefinitions(null);
    }
    
    public boolean loadDefinitions(final LoadingHandler loadingHandler) throws ParserConfigurationException, SAXException, IOException{
        repository.clear();
        final boolean result = 
            loadingHandler==null ? loadDefinitionsImpl(DUMMY_HANDLER) : loadDefinitionsImpl(loadingHandler);
        if (result){
            wasLoaded = true;
        }else{
            repository.clear();            
        }
        return result;
    }
    
    private boolean loadDefinitionsImpl(final ILoadingHandler loadingHanlder) throws ParserConfigurationException, SAXException, IOException{
        final SAXParser saxParser = 
            SAXParserFactory.newInstance().newSAXParser();        
        final DefaultHandler sqmlHandler = new SqmlSaxHandler(environment, repository, loadingHanlder);
        final RadixClassLoader classLoader = environment.getDefManager().getClassLoader();
        final RevisionMeta revisionMeta = classLoader.getRevisionMeta();
        final List<LayerMeta> layers = new ArrayList<>(revisionMeta.getAllLayersSortedFromBottom());
        Collections.reverse(layers);
        for (LayerMeta layerMeta : layers) {
            final DirectoryMeta ddsSegment = findSegment(layerMeta, ERepositorySegmentType.DDS);
            if (ddsSegment!=null){
                loadSegment(ddsSegment, saxParser, sqmlHandler, loadingHanlder);
            }
            if (loadingHanlder.wasCancelled()){
                return false;
            }
            final DirectoryMeta adsSegment = findSegment(layerMeta, ERepositorySegmentType.ADS);
            if (adsSegment!=null){
                loadSegment(adsSegment, saxParser, sqmlHandler,  loadingHanlder);
            }
            if (loadingHanlder.wasCancelled()){
                return false;
            }
        }
        return true;
    }
    
    private DirectoryMeta findSegment(final LayerMeta layerMeta, final ERepositorySegmentType segment){
        for (DirectoryMeta seg : layerMeta.getDirectories()) {  
            final String segmentDir = seg.getDirectory();
            if (segmentDir!= null && segmentDir.endsWith(segment.getValue())) {            
                return seg;
            }
        }
        return null;
    }
    
    private void loadSegment(final DirectoryMeta seg, 
                                           final SAXParser saxParser, 
                                           final DefaultHandler sqmlHandler,
                                           final ILoadingHandler loadingHanlder) throws SAXException, IOException{
        final String segmentDir = seg.getDirectory();
        if (segmentDir!= null 
            && (segmentDir.endsWith(ERepositorySegmentType.ADS.getValue()) || segmentDir.endsWith(ERepositorySegmentType.DDS.getValue()))) {
            for (String moduleDirXmlFileName : seg.getIncludes()) {
                if (loadingHanlder.wasCancelled()){
                    break;
                }
                if (!moduleDirXmlFileName.endsWith(DIRECTORY_XML_SHORT_FILE_NAME)) {
                    final String message = 
                        environment.getMessageProvider().translate("TraceMessage", "Can\'t load modules in %s\n: \"%s\" was expected but was \"%s\"");
                    environment.getTracer().error(String.format(message, seg.getDirectory(), DIRECTORY_XML_SHORT_FILE_NAME, moduleDirXmlFileName), (Throwable) null);
                    continue;
                }
                final String moduleShortName = moduleDirXmlFileName.substring(0, moduleDirXmlFileName.length() - DIRECTORY_XML_SHORT_FILE_NAME.length() - 1);
                final String moduleDirName = seg.getDirectory() + "/" + moduleShortName + "/";
                final String defXmlName = moduleDirName + SQML_DEFINITIONS_XML_SHORT_FILE_NAME;
                final InputStream defXmlStream = getRepositoryFileStream(defXmlName);
                if (defXmlStream!=null){
                    try{
                        saxParser.parse(defXmlStream, sqmlHandler);
                    }finally{
                        try{
                            defXmlStream.close();
                        }catch(IOException ex){
                            environment.getTracer().error(ex);
                        }
                    }
                }
            }
        }
    }
    
    private InputStream getRepositoryFileStream(final String fileName){        
        final ReleaseRepository repository = environment.getDefManager().getRepository();
        if (repository.isRepositoryFileExists(fileName)){
            try {
                return repository.getRepositoryFileStream(fileName,0);
            } catch (RadixLoaderException ex) {
                Logger.getLogger(SqmlDefinitions.class.getName()).log(Level.SEVERE, null, ex);                
            }
        }
        return null;
    }
    
    public boolean wasLoaded(){
        return wasLoaded;
    }
    
    public void clear(){
        wasLoaded = false;
        repository.clear();        
    }
    
    @Override
    public ISqmlTableDef findTableById(final Id tableId) {        
        return wasLoaded ? repository.findTableById(tableId) : null;
    }

    @Override
    public ISqmlEnumDef findEnumById(final Id enumId) {
        return wasLoaded ? repository.findEnumById(enumId) : null;
    }

    @Override
    public ISqmlFunctionDef findFunctionById(final Id functionId) {
        return wasLoaded ? repository.findFunctionById(functionId) : null;
    }

    @Override
    public ISqmlPackageDef findPackageById(final Id packageId) {
        return wasLoaded ? repository.findPackageById(packageId) : null;
    }
    
    @Override
    public ISqmlEventCodeDef findEventCodeById(final Id id) {
        return wasLoaded ? repository.findEventCodeById(id) : null;
    }    

    @Override
    public ISqmlDefinition findDefinitionByIdPath(final List<Id> ids) {
        if (!wasLoaded){
            return null;
        }
        ISqmlDefinition definition=null,childDefinition;
        for (Id id: ids){
            childDefinition = findDefinition(definition, id);
            if (childDefinition==null){
                return new SqmlBrokenDefinitionImpl(definition, id);
            }else{
                definition = childDefinition;
            }
        }
        return definition;
    }
    
    private ISqmlDefinition findTopLevelDefinition(final Id id){
        switch(id.getPrefix()){
            case ADS_ENUMERATION:
                return repository.findEnumById(id);
            case DOMAIN:
                return repository.findDomainById(id);
            case DDS_PACKAGE:
                return repository.findPackageById(id);
            case ADS_LOCALIZING_BUNDLE:
                final RadMlStringBundleDef bundle;
                try{
                    bundle = environment.getDefManager().getMlStringBundleDef(id);
                }catch(DefinitionError error){
                    return null;
                }
                return new SqmlMlStringBundleDef(bundle);
            case ADS_ENTITY_CLASS:
            case ADS_APPLICATION_CLASS:
            case DDS_TABLE:
                return repository.findTableById(id);
            default:
                return null;
        }
    }

    private ISqmlDefinition findDefinition(final ISqmlDefinition ownerDefinition, final Id definitionId){
        if (ownerDefinition==null){
            return findTopLevelDefinition(definitionId);
        }
        final ISqmlDefinition childDefinition;
        if (ownerDefinition instanceof ISqmlEnumDef){
            childDefinition = ((ISqmlEnumDef)ownerDefinition).findItemById(definitionId);
        }else if (ownerDefinition instanceof ISqmlDomainDef){
            childDefinition = null;
            final List<ISqmlDomainDef> childDomains = ((ISqmlDomainDef)ownerDefinition).getChildDomains();            
            for (ISqmlDomainDef childDomain: childDomains){
                if (definitionId.equals(childDomain.getId())){
                    return childDomain;
                }
            }
        }else if (ownerDefinition instanceof ISqmlPackageDef){
            childDefinition = ((ISqmlPackageDef)ownerDefinition).getFunctionById(definitionId);            
        }else if (ownerDefinition instanceof ISqmlFunctionDef){
            childDefinition = ((ISqmlFunctionDef)ownerDefinition).getParameterById(definitionId);
        }else if (ownerDefinition instanceof SqmlMlStringBundleDef){
            childDefinition = repository.findEventCodeById(definitionId);
        }else if (ownerDefinition instanceof ISqmlTableDef){
            final ISqmlTableDef tableDef = ((ISqmlTableDef)ownerDefinition);
            final ISqmlColumnDef column = tableDef.getColumns().getColumnById(definitionId);
            if (column==null){
                final ISqmlTableIndexDef index = tableDef.getIndices().getIndexById(definitionId);
                childDefinition = index==null ? tableDef.getReferences().getReferenceById(definitionId) : index;
            }else{
                childDefinition = column;
            }
        }else{
            childDefinition = null;
        }
        return childDefinition;
    }
            
    @Override
    public Collection<ISqmlTableDef> getTables() {
        return wasLoaded ? repository.getTables() : Collections.<ISqmlTableDef>emptyList();
    }

    @Override
    public Collection<ISqmlEnumDef> getEnums() {
        return wasLoaded ? repository.getEnums() : Collections.<ISqmlEnumDef>emptyList();
    }

    @Override
    public Collection<ISqmlDomainDef> getDomains() {
        return wasLoaded ? repository.getDomains() : Collections.<ISqmlDomainDef>emptyList();
    }

    @Override
    public Collection<ISqmlPackageDef> getPackages() {
        return wasLoaded ? repository.getPackages() : Collections.<ISqmlPackageDef>emptyList();
    }

    @Override
    public Collection<ISqmlEventCodeDef> getEventCodes() {
        return wasLoaded ? repository.getEventCodes() : Collections.<ISqmlEventCodeDef>emptyList();
    }

    @Override
    public ISqmlBrokenDefinition createBrokenDefinition(final Id id, final String typeName) {
        return new SqmlBrokenDefinitionImpl(null, id, typeName);
    }            
}