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

package org.radixware.kernel.common.userreport.repository;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsUserReportDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;


public class UserReports {

    public static final Id LIST_REPORTS_COMMAND_ID = Id.Factory.loadFrom("clc5VOFMBGOGZAH5J25FDBJTWAD2U");
    private Map<Id, UserReport> reports = null;
    private ReentrantLock readLock = new ReentrantLock();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public UserReports() {
    }

    public UserReport findReportById(Id id) {
        readLock.lock();
        try {
            if (reports == null) {
                return null;
            } else {
                return reports.get(id);
            }
        } finally {
            readLock.unlock();
        }
    }

    public List<UserReport> listReports(Id moduleId) {
        readLock.lock();
        try {
            if (reports == null) {
                return Collections.emptyList();
            } else {
                List<UserReport> result = new LinkedList<>();
                for (UserReport r : reports.values()) {
                    if (moduleId == null || r.getModuleId() == moduleId) {
                        result.add(r);
                    }
                }
                return new ArrayList<>(result);
            }
        } finally {
            readLock.unlock();
        }

    }

    public UserReport addNewReport(ReportsModule reportModule) {
        return UserExtensionManagerCommon.getInstance().getUserReportManager().addNewReport(reportModule);
       /* final AdsClassCreature creature = AdsClassCreature.Factory.newInstance(reportModule, EClassType.REPORT, true);

        ICreatureGroup.ICreature result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    return Collections.<ICreatureGroup.ICreature>singletonList(creature);
                }

                @Override
                public String getDisplayName() {
                    return "User Reports";
                }
            }}, creature);
        if (result != null) {

            final RadixObject radixObject = result.commit();
            if (radixObject instanceof AdsReportClassDef) {
                //try create user report in database using new report guid;
                AdsReportClassDef report = (AdsReportClassDef) radixObject;

                UserReport userReport = UserReport.create(reportModule, report);
                if (userReport != null) {
                    firePropertyChange("report-list");
                }
                return userReport;
            } else {
                if (radixObject != null) {
                    radixObject.delete();
                }
            }
        }


        return null;*/
    }

   /* private static final class UserReportModuleCreature extends NamedRadixObjectCreature<AdsModule> {

        public UserReportModuleCreature() {
            super(UserExtensionManager.getInstance().getReportsSegment().getModules(), "Create new Module");
        }

        @Override
        public AdsModule createInstance(String name) {
            return new ReportsModule(name);
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionIcon.MODULE;
        }
    }*/

    public ReportsModule addNewModule() {
        return UserExtensionManagerCommon.getInstance().getUserReportManager().addNewModule();
        /*final UserReportModuleCreature creature = new UserReportModuleCreature();
        ICreatureGroup.ICreature result = CreationWizard.execute(new ICreatureGroup[]{new ICreatureGroup() {
                @Override
                public List<ICreatureGroup.ICreature> getCreatures() {
                    return Collections.<ICreatureGroup.ICreature>singletonList(creature);
                }

                @Override
                public String getDisplayName() {
                    return "User Report Modules";
                }
            }}, creature);
        if (result != null) {

            final RadixObject radixObject = result.commit();
            if (radixObject instanceof ReportsModule) {
                //try create user report in database using new report guid;
                ReportsModule module = (ReportsModule) radixObject;
                if (module.create()) {
                    firePropertyChange("report-module-list");
                    return module;
                } else {
                    radixObject.delete();
                    return null;
                }
            } else {
                radixObject.delete();
                return null;
            }
        } else {
            return null;
        }*/
    }

    public ReportsModule findModuleById(Id id) {
        List<Module> result = new ArrayList<>(1);
        if(UserExtensionManagerCommon.getInstance().getReportsSegment()==null)
            return null;
        UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().findById(ERepositorySegmentType.ADS, id, true, result);
        if (result.size() > 0 && result.get(0) instanceof ReportsModule) {
            return (ReportsModule) result.get(0);
        } else {
            return null;
        }
    }

    public void reload() {
        if (readLock.tryLock()) {
            try {
                if (reports != null) {
                    for (UserReport r : reports.values()) {
                        r.notifyUnloaded();
                    }
                }
                reports = null;
                UserExtensionManagerCommon.getInstance().getReportsSegment().reload();
                firePropertyChange("report-module-list");
            } finally {
                readLock.unlock();
                firePropertyChange("report-module-list");
            }
        }
    }

    public List<ReportsModule> getReportModules() {
        if (readLock.tryLock()) {

            try {
                List<ReportsModule> allModules = new LinkedList<>();
                if (UserExtensionManagerCommon.getInstance() != null && UserExtensionManagerCommon.getInstance().getReportsSegment() != null) {
                    java.util.List<AdsModule> modules = UserExtensionManagerCommon.getInstance().getReportsSegment().getModules().list();


                    for (AdsModule module : modules) {
                        if (module instanceof ReportsModule) {
                            allModules.add((ReportsModule) module);
                        }
                    }
                }
                return allModules;
            } finally {
                readLock.unlock();
            }
        } else {
            return Collections.emptyList();
        }
    }

//    public void registerModule(FSRepositoryAdsModule repository) {
//        firePropertyChange("report-module-list");
//    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName) {
        propertyChangeSupport.firePropertyChange(propertyName, null, null);
    }

    public UserReport registerReport(Id moduleId, Id id, String name, String description, long currentVersion, long currentVersionOrder, AdsReportClassDef reportData, AdsTypeDeclaration contextParamType, long currentFormatVersion) throws IOException {
        UserReport report = new UserReport(moduleId, id, name, description, currentVersion, currentVersionOrder, reportData, contextParamType, currentFormatVersion);
        if (reportData != null) {//new report creation
            report.getVersions().getCurrent().save();
        }
        readLock.lock();
        try {
            if (reports == null) {
                reports = new HashMap<>();
            }
            reports.put(id, report);
        } finally {
            readLock.unlock();
        }
        firePropertyChange("report-list");
        return report;
    }

    public UserReport registerReport(Id moduleId, Id id, String name, String description, long currentVersion, long currentVersionOrder, AdsTypeDeclaration contextParamType, long currentFormatVersion) throws IOException {

        return registerReport(moduleId, id, name, description, currentVersion, currentVersionOrder, null, contextParamType, currentFormatVersion);
    }

    public void unregisterReport(UserReport report) {
        readLock.lock();
        try {
            if (reports != null) {
                reports.remove(report.getId());
                report.notifyUnloaded();
            }
        } finally {
            readLock.unlock();
        }
        firePropertyChange("report-list");
    }

    public void close() {
        readLock.lock();
        try {
            if (reports != null) {
                for (UserReport r : reports.values()) {
                    r.notifyUnloaded();
                }
                reports.clear();
            }
            propertyChangeSupport = new PropertyChangeSupport(this);
        } finally {
            readLock.unlock();
        }
    }

    public boolean importReport(ReportsModule context, InputStream in) throws IOException {
        //if(UserExtensionManagerCommon.getInstance().getUserReportManager()!=null){
       //     UserExtensionManagerCommon.getInstance().getUserReportManager().importReport(context, in);
        //}
        try {
            AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(in);
            UserReportExchangeType xEx = xDoc.getAdsUserReportExchange();
            if (xEx.getAdsUserReportDefinitionList().isEmpty()) {
                throw new IOException("No report version information found");
            }

            boolean hasData = false;
            for (int i = 0; i < xEx.getAdsUserReportDefinitionList().size(); i++) {
                UserReportDefinitionType xDef = xEx.getAdsUserReportDefinitionList().get(i);
                if (xDef.getReport() != null) {
                    hasData = true;
                    break;
                }
            }
            if (!hasData) {
                return false;
            }

            //check existing report
            Id currentReportId = Id.Factory.changePrefix(xEx.getId(), EDefinitionIdPrefix.USER_DEFINED_REPORT);
            //upload reports;

            UserReport existingReport = null;
            // if(context==null){               
            for (ReportsModule module : UserExtensionManagerCommon.getInstance().getUserReports().getReportModules()) {
                if (module.getDefinitions().containsId(currentReportId)) {
                    existingReport = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(currentReportId);
                    break;
                }
            }
           /* }else{
                if (context.getDefinitions().containsId(currentReportId)) {
                    existingReport = UserExtensionManagerCommon.getInstance().getUserReports().findReportById(currentReportId);
                } 
            }*/

            if (existingReport != null ) {
                if(existingReport.getModuleId().equals(context.getId())){
                    Id id= UserExtensionManagerCommon.getInstance().getUFRequestExecutor().importExistReport(existingReport, xDoc);
                    if(id==null){
                        return false;
                    }else{
                        currentReportId = id;
                    }
                    /*ChooseImportActionDialog dialog = new ChooseImportActionDialog(existingReport.getName());
                    ModalDisplayer.showModal(dialog);
                    switch (dialog.getOption()) {
                        case ChooseImportActionDialog.OPTION_CANCEL:
                            return;
                        case ChooseImportActionDialog.OPTION_ADD_VERSIONS:
                            existingReport.importNewVersion(xDoc);
                            return;
                        default:
                            currentReportId = Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT);
                    }*/
                }else{
                    currentReportId=Id.Factory.newInstance(EDefinitionIdPrefix.USER_DEFINED_REPORT);
                }
            }

            Id newReportId = currentReportId;

            String newReportName=existingReport == null ? xEx.getName() : xEx.getName() + " - Copy";
            newReportName=  calcReportName(newReportName, context,0);            
            UserReport report = UserReport.create(context, newReportName, newReportId, null);

            //import images

            Map<Id, Id> idReplaceMap = new HashMap<>();
            if (xEx.getImages() != null) {
                byte[] imgZipBytes = Base64.decode(xEx.getImages());
                File zipFile = null;
                try {
                    zipFile = File.createTempFile("aaa", "bbb");
                    FileUtils.writeBytes(zipFile, imgZipBytes);
                    ZipFile zip = new ZipFile(zipFile);
                    Enumeration<? extends ZipEntry> entries = zip.entries();

                    while (entries.hasMoreElements()) {
                        ZipEntry e = entries.nextElement();
                        if (e.getName().endsWith(".xml")) {
                            continue;
                        }
                        byte[] bytes = FileUtils.getZipEntryByteContent(e, zip);
                        File output = new File(context.getDirectory(), e.getName());
                        Id oldId = Id.Factory.loadFrom(FileUtils.getFileBaseName(output));
                        FileUtils.writeBytes(output, bytes);
                        AdsImageDef imageDef = context.getImages().importImage(output);
                        FileUtils.deleteFile(output);
                        idReplaceMap.put(oldId, imageDef.getId());
                    }
                } catch (IOException e) {
                    //ignore
                } finally {
                    if (zipFile != null) {
                        FileUtils.deleteFile(zipFile);
                    }
                }

                if (!idReplaceMap.isEmpty()) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    xEx.setImages(null);
                    XmlFormatter.save(xDoc, out);
                    String asString = new String(out.toByteArray(), FileUtils.XML_ENCODING);
                    out = null;
                    for (Map.Entry<Id, Id> e : idReplaceMap.entrySet()) {
                        asString = asString.replace(e.getKey().toString(), e.getValue().toString());
            }
                    xDoc = AdsUserReportExchangeDocument.Factory.parse(asString);
                    xEx = xDoc.getAdsUserReportExchange();
                }
            }

            if (report != null) {
                UserReport.ReportVersion current = report.getVersions().getCurrent();
                UserReport.ReportVersion newCurrent = null;
                report.setDescription(xEx.getDescription());

                for (int i = 0; i < xEx.getAdsUserReportDefinitionList().size(); i++) {
                    UserReportDefinitionType xDef = xEx.getAdsUserReportDefinitionList().get(i);
                    if (xDef.getReport() != null) {
                        UserReport.ReportVersion newVersion = report.getVersions().addNewVersion(xEx.getAdsUserReportDefinitionList().get(i));
                        if (newCurrent == null) {
                            newCurrent = newVersion;
                        }
                    }
                }

                if (newCurrent != null) {
                    AdsReportClassDef reportDef = newCurrent.findReportDefinition();
                    AdsPropertyDef cp = reportDef.findContextParameter();
                    if (cp != null) {
                        report.setContextParamType(cp.getValue().getType());
                    }
                }
                UserExtensionManagerCommon.getInstance().startBuild();
                try {
                    report.save();
                    for (UserReport.ReportVersion v : report.getVersions().list()) {
                        v.save();

                    }
                } finally {
                    UserExtensionManagerCommon.getInstance().finishBuild();
                }
                List<UserReport.ReportVersion> locked = new LinkedList<>();
                if (newCurrent != null) {
                    UserExtensionManagerCommon.getInstance().makeCurrent( false,newCurrent,report);
                  // UserExtensionManagerCommon.getInstance().getUFRequestExecutor().setCurrentVersionId(newCurrent, newReportId);
                    //newCurrent.makeCurrent(null,false);
                }
                try {

                    for (UserReport.ReportVersion v : report.getVersions().list()) {
                        v.lockDefinitionSearch(true);
                        locked.add(v);
                    }

                    if (current != null) {
                        report.getVersions().removeVersion(current);
                    }

                    for (UserReport.ReportVersion v : locked) {
                        v.reload();
                    }
                } finally {
                    for (UserReport.ReportVersion v : locked) {
                        v.lockDefinitionSearch(false);
                    }
                    for (UserReport.ReportVersion v : report.getVersions().list()) {
                        v.save();
                    }
                }

                context.invalidateDependences();
                return true;
            }
        } catch (XmlException | IOException ex) {
            throw new IOException("Error on reading user report exchange data", ex);
        } finally {
            firePropertyChange("report-list");
        }
        return false;
    }   
    
    private String calcReportName(final String newReportName,final ReportsModule context,final int index){
        String result=newReportName;
        for(UserReport rep:context.getReports()){
           if(rep.getName().equals(newReportName+(index==0 ? ""  : index))){
               return calcReportName(newReportName, context, index+1);
           } else{
               result=newReportName+(index==0 ? ""  : index);
           }
        }
        return result;
    }
}
