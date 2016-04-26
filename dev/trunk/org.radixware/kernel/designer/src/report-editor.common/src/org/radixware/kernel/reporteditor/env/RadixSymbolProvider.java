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

package org.radixware.kernel.reporteditor.env;

import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
//import org.netbeans.api.project.Project;
//import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.jumpto.symbol.SymbolDescriptor;
import org.netbeans.spi.jumpto.symbol.SymbolProvider;
import org.openide.filesystems.FileObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassTitledMember;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.extrepository.UserExtRepository;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.chooseobject.EChooseDefinitionDisplayMode;
import org.radixware.kernel.designer.common.dialogs.chooseobject.NameMatcher;
import org.radixware.kernel.designer.common.dialogs.chooseobject.NameMatcherFactory;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class RadixSymbolProvider implements SymbolProvider {

    private VisitorProvider provider = null;

    public RadixSymbolProvider() {
        System.out.println("done");
    }

    @Override
    public void cancel() {
        synchronized (this) {
            if (provider != null) {
                provider.cancel();
            }
        }
    }

    @Override
    public void cleanup() {
        // NOTHING - no cashing
    }

    private static class RadixObjectSymbolDescriptor extends SymbolDescriptor {

        private final RadixObject radixObject;
        private final RadixObjectDescriptorAdapter adapter;
        private final String symbolTypeTitle;

        public RadixObjectSymbolDescriptor(final RadixObject radixObject, final String symbolTypeTitle) {
            super();
            this.radixObject = radixObject;
            this.adapter = new RadixObjectDescriptorAdapter(radixObject);
            this.symbolTypeTitle = symbolTypeTitle;
        }

        @Override
        public void open() {
            adapter.open();
        }

        @Override
        public String getSymbolName() {
            final RadixObject project = adapter.findProject();
            return radixObject.getQualifiedName(project);
        }

        @Override
        public String getProjectName() {
            return adapter.getProjectName();
        }

        @Override
        public Icon getProjectIcon() {
            return adapter.getProjectIcon();
        }

        @Override
        public int getOffset() {
            return adapter.getOffset();
        }

        @Override
        public Icon getIcon() {
            return adapter.getIcon();
        }

        @Override
        public FileObject getFileObject() {
            return adapter.getFileObject();
        }

        @Override
        public String getOwnerName() {
            return symbolTypeTitle;
        }
    }

    // TODO: refactor to declarative registration or method of RadixObject
    private static void collectSymbolsOnReadAccess(final Definition definition, final Map<String, String> symbolTypeTitle2Value) {
        symbolTypeTitle2Value.put("type", definition.getTypeTitle());

        final String name = definition.getName();
        if (name != null && !name.isEmpty()) {
            symbolTypeTitle2Value.put("name", name);
        }

        final Id id = definition.getId();
        if (id != null) {
            symbolTypeTitle2Value.put("identifier", id.toString());
        }

        if (definition instanceof IDdsDbDefinition) {
            final IDdsDbDefinition dbDefinition = (IDdsDbDefinition) definition;
            final String dbName = dbDefinition.getDbName();
            if (dbName != null && !dbName.isEmpty()) {
                symbolTypeTitle2Value.put("database name", dbName);
            }
        }

        if (definition instanceof AdsClassTitledMember) {
            final AdsClassTitledMember titledMember = (AdsClassTitledMember) definition;
            final Layer layer = titledMember.getModule().getSegment().getLayer();
            for (EIsoLanguage language : layer.getLanguages()) {
                final String title = titledMember.getTitle(language);
                if (title != null && !title.isEmpty()) {
                    symbolTypeTitle2Value.put(language.getName().toLowerCase() + " title", title);
                }
            }
        }
    }

    private static void collectSymbols(final Definition definition, final Map<String, String> symbolTypeTitle2Value) {
        RadixMutex.readAccess(new Runnable() {
            @Override
            public void run() {
                collectSymbolsOnReadAccess(definition, symbolTypeTitle2Value);
            }
        });
    }

    private static class Collector implements IVisitor {

        private final Result result;
        private final NameMatcher matcher;

        public Collector(final Result result, final NameMatcher matcher) {
            this.result = result;
            this.matcher = matcher;
        }

        @Override
        public void accept(final RadixObject radixObject) {
            if (!(radixObject instanceof Definition)) {
                return;
            }
            if (radixObject instanceof DdsExtTableDef) {
                return;
            }
            final Definition definition = (Definition) radixObject;
            final Map<String, String> symbolTypeTitle2Value = new HashMap<>();
            collectSymbols(definition, symbolTypeTitle2Value);
            for (Map.Entry<String, String> entry : symbolTypeTitle2Value.entrySet()) {
                final String value = entry.getValue();
                if (matcher.accept(value)) {
                    final String symbolTypeTitle = entry.getKey();
                    final SymbolDescriptor symbolDescriptor = new RadixObjectSymbolDescriptor(radixObject, symbolTypeTitle);
                    result.addResult(symbolDescriptor);
                }
            }
        }
    }

    private void computeSymbolNames(Result result, NameMatcher matcher) {
        //final Branch branch = project.getLookup().lookup(Branch.class);
        final Branch branch = UserExtRepository.getInstance().getUserExtSegment().getBranch();

        if (branch != null) {
            final Collector collector = new Collector(result, matcher);
            branch.visit(collector, provider);
        }
        for (ReportsModule rm : UserExtensionManager.getInstance().getUserReports().getReportModules()) {
            for (UserReport rpt : UserExtensionManager.getInstance().getUserReports().listReports(rm.getId())) {
                if (matcher.accept(rpt.getName())) {
                    try {
                        AdsReportClassDef clazz = rpt.getVersions().getCurrent().findReportDefinition();
                        result.addResult(new RadixObjectSymbolDescriptor(clazz, clazz.getTypeTitle()));
                    } catch (Throwable e) {
                        //ignore
                    }
                }
            }
        }
    }

    @Override
    public void computeSymbolNames(Context context, Result result) {
        final NameMatcher matcher = NameMatcherFactory.createNameMatcher(context.getText(), context.getSearchType(), EChooseDefinitionDisplayMode.NAME_AND_LOCATION);
        if (matcher == null) {
            return;
        }

        synchronized (this) {
            provider = VisitorProviderFactory.createDefaultVisitorProvider();
        }

//        final Project contextProject = context.getProject();
//        if (contextProject != null) {
        computeSymbolNames(result, matcher);
//        } else {
//            for (Project project : OpenProjects.getDefault().getOpenProjects()) {
//                computeSymbolNames(project, result, matcher);
//            }
//        }

        synchronized (this) {
            provider = null;
        }
    }

    @Override
    public String getDisplayName() {
        return "Radix Symbols";
    }

    @Override
    public String name() {
        return "Radix";
    }
}
