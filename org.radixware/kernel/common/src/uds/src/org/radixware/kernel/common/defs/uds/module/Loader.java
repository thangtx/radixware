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

package org.radixware.kernel.common.defs.uds.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.userfunc.xml.ParseInfo;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncImportInfo;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncLibInfo;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UdsXmlParser;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserReportInfo;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory;
import org.radixware.kernel.common.defs.uds.files.UdsFile;
import org.radixware.kernel.common.defs.uds.files.UdsFileRadixObjects;
import org.radixware.kernel.common.defs.uds.files.UdsXmlFile;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;
import org.radixware.kernel.common.defs.uds.userfunc.UdsDummyUserFuncDef;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncLib;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;


public class Loader {
    private final static String SVN_FOLDER = ".svn";
    private Loader() {
    }

    public static AdsDefinition loadFromStream(InputStream is, File file, boolean importMode) throws IOException {
        try {
            UdsDefinitionDocument xDoc = UdsDefinitionDocument.Factory.parse(is);
            if (xDoc.getUdsDefinition() != null) {

                AdsDefinition def = loadFromXml(xDoc.getUdsDefinition(), importMode);
                if (def != null) {
                    if (file != null) {
                        def.setFileLastModifiedTime(file.lastModified());
                    } else {
                        def.setFileLastModifiedTime(0l);
                    }
                    def.setEditState(EEditState.NONE);
                    return def;
                }
            }
            throw new RadixError("Definition format is not supported");
        } catch (XmlException ex) {
            try (InputStream inputStream = getInputStream(file)){
               return org.radixware.kernel.common.defs.ads.module.Loader.loadFromStream(inputStream, file, true,false); 
            }
        }
    }

    public static AdsDefinition loadFromRepository(IRepositoryDefinition rep) {
        InputStream is = null;
        try {
            is = rep.getData();
            if (is != null) {                
                return loadFromStream(is, rep.getFile(), false);
            } else {
                throw new IOException("No input stream");
            }

        } catch (IOException ex) {
            throw new RadixError(ex.getMessage(), ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
            }
        }
    }
    
    private static UdsDefinition loadFromXml(UdsDefinition def) {
        if (def != null) {
            if (def.isSaveable()){
                def.setEditState(EEditState.NONE);
            }
            return def;
        }
        throw new RadixError("Definition format is not supported");
    }
    
    public static UdsDefinition loadFromXml(UserFuncImportInfo xDef, boolean importMode) {
        UdsDefinition def = null;
        if (xDef != null) {
            def  = UdsUserFuncDef.Factory.loadFrom(xDef, importMode);
        }
        return loadFromXml(def);
    }
    
    public static AdsDefinition loadFromXml(UdsDefinitionDocument.UdsDefinition xDef, boolean importMode) {
        UdsDefinition def = null;
        if (xDef != null) {
            if (xDef.getUserFunc() != null) {
                def = UdsUserFuncDef.Factory.loadFrom(xDef.getUserFunc(), importMode);
            }
        }
        return loadFromXml(def);
    }

    public static List<ParseInfo> parseXmlFile(File file) throws XmlException, IOException {
        List<ParseInfo> ufInfos = new ArrayList<>();
        if (file != null) {
            ufInfos = UdsXmlParser.Factory.newInstance().parse(file);

        }
        return ufInfos;
    }
    
    public static void collectDirectoryFiles(File file, UdsDirectory udsDirectory, UdsFiles udsFiles) {
        if (file == null || !file.isDirectory()) {
            return;
        }
        
        File[] list = file.listFiles();
        if (list == null) {
            return;
        }
        for (File f : list) {
            RadixObject radixObject = parseFile(f, udsFiles);
            if (radixObject != null) {
                UdsDirectory.UdsFiles files = udsDirectory.getFiles();
                files.setLoading(true);
                files.add(radixObject);
                files.setLoading(false);
            }
        }
    }
    
    public static void loadFrom(List<ParseInfo> ufInfos, UdsFileRadixObjects udsFileRadixObjects, UdsFiles files) {
        for (ParseInfo info : ufInfos) {
            if (info instanceof UserFuncImportInfo) {
                UdsDummyUserFuncDef def = UdsUserFuncDef.Factory.loadFrom((UserFuncImportInfo) info, null);
                if ((def.getXPath() == null || udsFileRadixObjects.findByXPath(def.getXPath()) == null) && cheakId(def, files)) {
                    udsFileRadixObjects.add(def);
                }
            }
            if (info instanceof UserFuncLibInfo) {
                UdsUserFuncLib lib = UdsUserFuncLib.Factory.loadFrom((UserFuncLibInfo) info);
                if ((lib.getXPath() == null || udsFileRadixObjects.findByXPath(lib.getXPath()) == null)) {
                    udsFileRadixObjects.add(lib);
                }
            }
            if (info instanceof UserReportInfo) {
                UdsExchangeReport udsReport = new UdsExchangeReport((UserReportInfo) info);
                boolean isLoaded = false;
                for (RadixObject r : udsReport) {
                    if (!cheakId(r, files)) {
                        isLoaded = true;
                        break;
                    }
                }
                if ((udsReport.getXPath() == null || udsFileRadixObjects.findByXPath(udsReport.getXPath()) == null) && !isLoaded) {
                    udsFileRadixObjects.add(udsReport);
                }
            }
        }
    }

    public static InputStream getInputStream(File defFile) throws FileNotFoundException {
        if (defFile != null) {
            return new FileInputStream(defFile);
        } else {
            return null;
        }
    }
    
    private static boolean cheakId(RadixObject radixObject, UdsFiles files){
        if (radixObject == null){
            return false;
        }
        if (files == null){
            return true;
        }
        if (!(radixObject instanceof AdsDefinition)){
            return true;
        }
        
        RadixObject ro = files.findById(((AdsDefinition) radixObject).getId());
        if (ro != null){
            if (ro.getFile() != null && ro.getFile().equals(radixObject.getFile())){
                  Logger.getLogger(Loader.class.getName()).log(Level.WARNING, "Unable to load ''{0}'': unable to duplicate definition ''{1}''", new Object[]{radixObject.getQualifiedName(), ro.getQualifiedName()});
                  return false;
            }
        }
        return  true;
    }

    public static RadixObject parseFile(File file, UdsFiles udsFiles) {
        if (file == null || !file.exists()) {
            return null;
        }
        String name = file.getName();
        if (file.isDirectory()) {
            if (name.equals(SVN_FOLDER)){
                return null;
            }
            UdsDirectory directory = new UdsDirectory(name);
            collectDirectoryFiles(file, directory, udsFiles);
            directory.setEditState(EEditState.NONE);
            return directory;
        } else {
            String ext = FileUtils.getFileExt(name);
            boolean isXml = "xml".equals(ext);
            if (isXml) {
                try {
                    try {
                        AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(file);
                        UdsExchangeReport report = new UdsExchangeReport(xDoc.getAdsUserReportExchange(), name);
                        for (RadixObject r : report) {
                            if (!cheakId(r, udsFiles)) {
                                UdsXmlFile uf = new UdsXmlFile(name);
                                uf.setEditState(EEditState.NONE);
                                uf.setFileLastModifiedTime(file.lastModified());
                                return uf;
                            }
                        }
                        report.setFileLastModifiedTime(file.lastModified());
                        report.setEditState(EEditState.NONE);
                        return report;
                    } catch (XmlException | IOException ex) {
                    }

                    try (InputStream is = getInputStream(file)) {
                        AdsDefinition defenition = loadFromStream(is, file, false);
                        if (!cheakId(defenition, udsFiles)) {
                            return null;
                        }
                        return defenition;
                    } catch (RadixError | IOException ex) {
                        try {
                            UdsXmlFile uf = new UdsXmlFile(name);
                            uf.setFileLastModifiedTime(file.lastModified());
                            List<ParseInfo> ufInfos = parseXmlFile(file);
                            loadFrom(ufInfos, uf.getUdsDefinitions(), udsFiles);
                            uf.setEditState(EEditState.NONE);
                            return uf;
                        } catch (XmlException | IOException ex1) {
                            Logger.getLogger(Loader.class.getName()).log(Level.INFO, "Unable to load xml file '" + name + "'", ex1);
                        }
                    }
                } catch (Throwable e) {
                    Logger.getLogger(Loader.class.getName()).log(Level.INFO, "Unable to load xml file '" + name + "'", e);
                }
            }
            UdsFile uf = new UdsFile(name);
            uf.setEditState(EEditState.NONE);
            uf.setFileLastModifiedTime(file.lastModified());
            return uf;
        }
    }
}
