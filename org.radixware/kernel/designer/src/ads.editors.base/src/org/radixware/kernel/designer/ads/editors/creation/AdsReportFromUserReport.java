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
package org.radixware.kernel.designer.ads.editors.creation;


import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.DefaultAcceptResult;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptResult;
import org.radixware.kernel.designer.common.dialogs.utils.IAdvancedAcceptor;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.schemas.adsdef.AdsUserReportExchangeDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.adsdef.UserReportDefinitionType;
import org.radixware.schemas.adsdef.UserReportExchangeType;

/**
 *
 * @author avoloshchuk
 */
public class AdsReportFromUserReport extends AdsClassCreature {
    File file;
    LocalizingBundleDefinition xBundle;
    String newName;

    public AdsReportFromUserReport(AdsModule module) {
        super(module, EClassType.REPORT, false);
    }

    @Override
    public void afterAppend(AdsDefinition object) {
        super.afterAppend(object);
        if (xBundle != null) {
            AdsLocalizingBundleDef bundle = object.findLocalizingBundle();
            if (bundle == null) {
                return;
            }
            for (LocalizedString xStr : xBundle.getStringList()) {
                AdsMultilingualStringDef string = AdsMultilingualStringDef.Factory.loadFrom(xStr);
                bundle.getStrings().getLocal().add(string);
            }
            bundle.getStrings().updateStringsByLanguages();
            try {
                bundle.save();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public boolean afterCreate(AdsDefinition object) {
        return true;
    }

    @Override
    public AdsClassDef createInstance() {
        return importReport(file);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-ReportClass-From-UserReport");
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public WizardInfo getWizardInfo() {


        return new Creature.WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsReportFromUserReport.AdsReportFromUserReportStep();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
    
        
    private AdsReportClassDef importReport(File fromFile) {
        try {
            AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(fromFile);
            UserReportExchangeType xDefs = xDoc.getAdsUserReportExchange();
            List<UserReportDefinitionType> xReports = xDefs.getAdsUserReportDefinitionList();
            for (UserReportDefinitionType xReportBundle : xReports) {
                ClassDefinition xReport = xReportBundle.getReport();
                if (xReport != null) {
                    changeIds(xReport);
                    if (newName != null) {
                        xReport.setName(newName);
                    }
                    AdsReportClassDef report = AdsReportClassDef.Factory.loadFrom(xReport);
                    if (module.getDefinitions().findById(report.getId()) != null) {
                        continue;
                    }
                    
                    if (xReportBundle.getStrings() != null) {
                        xBundle = xReportBundle.getStrings();
                    }
                    return report;
                }
            }
        } catch (XmlException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    private void changeIds(ClassDefinition xReport){
        final Id id = xReport.getId();
        final IdInfo info = new IdInfo(id, Id.Factory.newInstance(EDefinitionIdPrefix.REPORT));
        XmlCursor cursor = xReport.newCursor();
        while(cursor.hasNextToken()) {
            cursor.toNextToken();
            XmlObject obj = cursor.getObject();
            if (obj instanceof XmlAnySimpleType) {
                replaceString((XmlAnySimpleType) obj, info);
            }
        }
    }
    
    private void replaceString(XmlAnySimpleType object, IdInfo info) {
        String value = object.getStringValue();
        if (value == null) {
            return;
        }
        
        if (value.contains(info.idAsStr)) {
            value = value.replace(info.idAsStr, info.newIdAsStr);
        } else if (value.contains(info.idWithoutPrefix)) {
            value = value.replace(info.idWithoutPrefix, info.newIdWithoutPrefix);
        }
        object.setStringValue(value);
    }
    
    private class AdsReportFromUserReportStep extends CreatureSetupStep<AdsReportFromUserReport, AdsModuleDefinitionFromFileWizardStep1Panel> implements ChangeListener {

        @Override
        public void open(AdsReportFromUserReport creature) {
            super.open(creature);
        }

        @Override
        public void apply(AdsReportFromUserReport creature) {
            super.apply(creature);
            file = getVisualPanel().getFile();
            newName = getVisualPanel().getCurrentName();
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public String getDisplayName() {
            return "Setup report from user report file";
        }

        @Override
        protected AdsModuleDefinitionFromFileWizardStep1Panel createVisualPanel() {
            final AdsModuleDefinitionFromFileWizardStep1Panel p = new AdsModuleDefinitionFromFileWizardStep1Panel();
            p.setNameAcceptor(NameAcceptorFactory.newCreateAcceptor(getContainer(), defType));
            p.setFileAcceptor(new IAdvancedAcceptor<File>() {
                File oldCandidate;
                IAcceptResult oldResult;

                @Override
                public IAcceptResult getResult(File candidate) {
                    if (candidate.equals(oldCandidate)) {
                        return oldResult;
                    }
                    oldCandidate = candidate;
                    try {
                        AdsUserReportExchangeDocument xDoc = AdsUserReportExchangeDocument.Factory.parse(candidate);
                        UserReportExchangeType xDefs = xDoc.getAdsUserReportExchange();
                        p.setCurrentName(xDefs.getName());
                    } catch (XmlException ex) {
                        return oldResult = new DefaultAcceptResult(false, "Wrong file format");
                    } catch (IOException ex) {
                        return oldResult = new DefaultAcceptResult(false, "Can not read file"); 
                    }
                    return oldResult = new DefaultAcceptResult(true, ""); 
                }
            });
            p.addChangeListener(this);
            return p;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }
    }
    
    private class IdInfo {
        final Id id;
        final Id newId;
        
        
        final String idAsStr;
        final String idWithoutPrefix;
        final String newIdAsStr;
        final String newIdWithoutPrefix;

        public IdInfo(Id id, Id newId) {
            this.id = id;
            this.newId = newId;
            this.idAsStr = id.toString();
            this.idWithoutPrefix = idAsStr.substring(0, 3);
            this.newIdAsStr = newId.toString();
            this.newIdWithoutPrefix = newIdAsStr.substring(0, 3);
        }
        
        
        
    }
}
