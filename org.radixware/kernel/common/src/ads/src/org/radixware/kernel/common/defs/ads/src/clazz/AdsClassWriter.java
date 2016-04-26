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
package org.radixware.kernel.common.defs.ads.src.clazz;

import java.util.*;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.src.command.AdsCommandWriter;
import org.radixware.kernel.common.defs.ads.src.exploreritems.AdsParagraphWriter;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.jml.LicenseCodeGenSupport;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public class AdsClassWriter<T extends AdsClassDef> extends AbstractDefinitionWriter<T> {

    protected static final char[] TEXT_CLASS = "class".toCharArray();
    protected static final char[] TEXT_INTERFACE = "interface".toCharArray();
    protected static final char[] TEXT_ENUM = "enum".toCharArray();
    protected static final char[] TEXT_EXTENDS = "extends".toCharArray();
    protected static final char[] TEXT_IMPLEMENTS = "implements".toCharArray();
    public static final char[] EXPLORER_MODEL_CLASS_NAME = "org.radixware.kernel.common.client.models.Model".toCharArray();
    public static final char[] RAD_PROP_ACCESSOR_PROVIDER = "org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider".toCharArray();
    private final EClassType classType;

    public AdsClassWriter(final JavaSourceSupport support, final T target, final UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
        classType = target.getClassDefType();
    }

    @Override
    public boolean writeCode(final CodePrinter printer) {
        Definition userDef = null;
        if (def instanceof AdsUserReportClassDef) {
            userDef = (AdsUserReportClassDef) def;
        } else if (def instanceof AdsReportModelClassDef) {
            AdsReportClassDef owner = ((AdsReportModelClassDef) def).getOwnerClass();
            if (owner.isUserReport()) {
                userDef = def;
            }
        }
        if (userDef != null) {
            WriterUtils.writeUserDefinitionHeader(printer, userDef);
        }

        switch (usagePurpose.getCodeType()) {
            case INVOKE:
                writeUsage(printer);
                printer.print(".this");
                return true;
            default:
                if (usagePurpose.getCodeType() == JavaSourceSupport.CodeType.EXCUTABLE) {
                    if (AdsTransparence.isTransparent(def, false)) {
                        return false;
                    }
                }
                return super.writeCode(printer);
        }
    }

    @Override
    protected boolean writeExecutable(final CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        boolean writeAllContent = true;
        switch (classType) {
            case ENTITY:
            case ENTITY_GROUP:
            case APPLICATION:
            case FORM_HANDLER:
            case REPORT:
                writeAllContent = env == ERuntimeEnvironmentType.SERVER;
                break;
            default:
            //ignore
        }

        if (!def.isNested()) {
            WriterUtils.writePackageDeclaration(printer, def, usagePurpose);

            if (writeAllContent) {
                writeExecutableHeader(printer);
            }
        }

        writeTypeDeclaration(printer, writeAllContent);
        printer.enterBlock(1);
        printer.println('{');
        //---------------------RADIX-1831-----------------------
//        if (!new AdsClassWriter<T>(getSupport(), def, UsagePurpose.getPurpose(usagePurpose.getEnvironment(), JavaSourceSupport.CodeType.META)).writeCode(printer)) {
//            return false;
//        }

        if (classType == EClassType.COMMAND_MODEL) {
            AdsCommandDef command = ((AdsCommandModelClassDef) def).findCommand();
            if (command == null) {
                return false;
            }
            command.getJavaSourceSupport().getCodeWriter(usagePurpose).writeSendMethodIfNeeded(printer);
        }

        if (writeAllContent) {
            writeExecutableBody(printer);
            switch (classType) {
                case FILTER_MODEL:
                    if (env == ERuntimeEnvironmentType.WEB || env == ERuntimeEnvironmentType.EXPLORER) {
                        writeMissingFilterParams(printer);
                    }
                    break;
            }
        } else {//write command classes
            if (env == ERuntimeEnvironmentType.WEB || env == ERuntimeEnvironmentType.EXPLORER) {
                if (!writeDefaultModelBodyAndIterfaceDetails(printer)) {
                    return false;
                }
            }
        }


        printer.println();
        final LicenseCodeGenSupport licenseSupport = LicenseCodeGenSupport.get(printer);
        if (licenseSupport != null) {
            licenseSupport.flushFields(printer);
        }
        printer.leaveBlock(1);
        printer.println();

        if (def instanceof AdsModelClassDef) {
            if (usagePurpose.getEnvironment() == ERuntimeEnvironmentType.EXPLORER || usagePurpose.getEnvironment() == ERuntimeEnvironmentType.WEB) {
                AdsAbstractUIDef.WrittenWidgetSupport support = (AdsAbstractUIDef.WrittenWidgetSupport) printer.getProperty(AdsAbstractUIDef.WrittenWidgetSupport.class);
                if (support != null) {
                    printer.enterBlock();
                    support.writeWidgetAccessors(def, printer, usagePurpose);
                    printer.leaveBlock();
                }
            }
        }
        printer.println('}');
        return true;
    }

    private boolean writeDefaultModelBodyAndIterfaceDetails(CodePrinter printer) {
        switch (classType) {
            case ENTITY:
                if (!writeDefaultSelectorModel(printer)) {
                    return false;
                }
                break;
        }



        if (classType != EClassType.ENTITY_GROUP && classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
            printer.print("public static class ");
            printer.print(def.getId());
            printer.print("_DefaultModel");
            if (!writeDefaultModelSuperClasses(printer)) {
                return false;
            }
            printer.print(" implements ");
            printer.print(def.getId());
            printer.enterBlock();
            printer.println("{");
        }

        if (!writeDefaultConstructorForModelClass(printer)) {
            return false;
        }

        List<AdsPropertyWriter> rootPropWriters = new LinkedList<>();
        switch (classType) {
            case ENTITY:
            case APPLICATION:
            case REPORT:
            case FORM_HANDLER:
                if (!writeExplorerPropLists(printer, rootPropWriters)) {
                    return false;
                }
                break;

            case ENTITY_GROUP:
                break;
            default:
                return false;

        }
        List<AdsScopeCommandDef> commands = new LinkedList<>();
        if (!writeExplorerCommandMethods(printer, commands)) {
            return false;
        }
        if (classType != EClassType.ENTITY_GROUP && classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
            printer.leaveBlock();
            printer.println("}");
        }
        if (classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
            for (AdsPropertyWriter w : rootPropWriters) {
                if (!w.writeAbstractDeclaration(printer)) {
                    return false;
                }
            }
        }
        printer.println();
        if (!writeCommandClassDecls(printer, commands)) {
            return false;
        }
        return true;
    }

    private boolean writeDefaultSelectorModel(CodePrinter printer) {
        String className = AdsModelClassDef.getDefaultModelLocalClassName(EClassType.GROUP_MODEL);
        printer.println("/*Group model adapter class. This is the base class for \nall of selector presentation models of this entity*/");
        printer.print("public static class ");
        printer.print(className);
        printer.print(" extends ");
        printer.print(AdsModelClassDef.GROUP_MODEL_JAVA_CLASS_NAME);
        printer.println("{");
        printer.enterBlock(1);
        printer.print("public " + className + "(");
        printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
        printer.print(" userSession,");
        printer.print(AdsSelectorPresentationWriter.SELECTOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
        printer.println(" def){super(userSession,def);}");

        if (!writeDefaultSelectorModelProps(printer)) {
            return false;
        }


        printer.print("public ");
        AdsType type = def.getType(EValType.USER_CLASS, null);

        //AdsType.TypeJavaSourceSupport.TypeCodeWriter defTypeCodeWriter = def.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON_CLIENT, usagePurpose.getCodeType()));
        writeUsage(printer, type);
        //defTypeCodeWriter.writeUsage(printer);
        printer.print('.');
        printer.print(def.getId());
        printer.print("_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (");
        writeUsage(printer, type);
        //defTypeCodeWriter.writeUsage(printer);
        printer.print('.');
        printer.print(def.getId());
        printer.println("_DefaultModel )  super.getEntity(i);}");

        printer.leaveBlock(1);
        printer.println('}');
        return true;
    }

    private boolean writeDefaultSelectorModelProps(CodePrinter printer) {
        AdsEntityClassDef clazz = (AdsEntityClassDef) def;
        AdsEntityGroupClassDef group = clazz.findEntityGroup();
        if (group != null) {
            List<AdsPropertyDef> props = group.getProperties().get(EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsPropertyDef>() {
                @Override
                public boolean isTarget(AdsPropertyDef radixObject) {
                    return radixObject.getNature() == EPropNature.GROUP_PROPERTY;
                }
            });

            try {
                AdsPropertyWriter.Factory.ppCache.initialize();
                for (AdsPropertyDef prop : props) {


                    AdsPropertyWriter paramWriter = AdsPropertyWriter.Factory.ppCache.getInstance(prop, usagePurpose);
                    if (paramWriter == null) {
                        return false;
                    }
                    if (!paramWriter.writeCode(printer)) {
                        return false;
                    }
                }
                AdsPropertiesWriter.writePropertiesCreator(group, props, printer, usagePurpose);
            } finally {
                AdsPropertyWriter.Factory.ppCache.reset();
            }


        }
        return true;
    }

    private boolean writeMissingFilterParams(CodePrinter printer) {
        AdsFilterModelClassDef model = (AdsFilterModelClassDef) def;
        AdsFilterDef filter = model.getOwnerFilterDef();
        if (filter == null) {
            return false;
        }
        try {
            AdsPropertyWriter.Factory.ppCache.initialize();
            for (AdsFilterDef.Parameter p : filter.getParameters()) {
                AdsPropertyDef prop = model.getProperties().findById(p.getId(), EScope.LOCAL).get();
                if (prop == null) {//parameter is not published
                    AdsPropertyWriter paramWriter = AdsPropertyWriter.Factory.ppCache.getInstance(p, usagePurpose);
                    if (paramWriter == null) {
                        return false;
                    }
                    if (!paramWriter.writeCode(printer)) {
                        return false;
                    }
                }
            }
        } finally {
            AdsPropertyWriter.Factory.ppCache.reset();
        }
        return true;
    }

    private static void collectPublishableProps(AdsClassDef clazz, Map<Id, AdsPropertyWriter> propWriters, List<AdsPropertyDef> presentations, UsagePurpose usagePurpose, AdsClassDef filter, AdsClassDef filter2, Set<Id> apiProps) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        AdsClassDef ssFilter = null;
        if (filter instanceof AdsModelClassDef) {
            ssFilter = ((AdsModelClassDef) filter).findServerSideClasDef();
        }
        AdsClassDef iterable = null;
        do {
            if (iterable == null) {
                iterable = clazz;
            } else {
                if (ssFilter == null) {
                    break;
                }
                iterable = iterable.getInheritance().findSuperClass().get();
                if (iterable == null) {
                    break;
                }
            }
            IModelPublishableProperty.Support pubSupport = iterable.getModelPublishablePropertySupport();
            if (pubSupport != null) {
                main:
                for (IModelPublishableProperty prop : pubSupport.list(env, EScope.LOCAL_AND_OVERWRITE, null)) {
                    if (prop instanceof IAdsPresentableProperty) {
                        if (prop.isTransferable(env) && !propWriters.containsKey(prop.getId())) {
                            if (filter != null) {
                                AdsClassDef filterClass = filter;
                                while (filterClass != null) {
                                    if (filterClass.getProperties().findById(prop.getId(), EScope.LOCAL_AND_OVERWRITE).get() != null) {
                                        continue main;
                                    }
                                    final AdsType superType = filterClass.getInheritance().getSuperClassRef().resolve(clazz).get();
                                    if (superType instanceof AdsClassType) {
                                        filterClass = ((AdsClassType) superType).getSource();
                                    } else {
                                        break;
                                    }
                                }
                            }
                            if (ssFilter != null) {
                                AdsClassDef filterClass = ssFilter;
                                while (filterClass != null) {
                                    final AdsPropertyDef ssFilterProp = ssFilter.getProperties().findById(prop.getId(), EScope.ALL).get();
                                    if (ssFilterProp != null) {
                                        ServerPresentationSupport support = ((IAdsPresentableProperty) ssFilterProp).getPresentationSupport();
                                        if (support != null) {
                                            PropertyPresentation pps = support.getPresentation();
                                            if (pps != null && pps.isPresentable()) {
                                                continue main;
                                            }
                                        }
                                    }
                                    final AdsType superType = filterClass.getInheritance().getSuperClassRef().resolve(clazz).get();
                                    if (superType instanceof AdsClassType) {
                                        filterClass = ((AdsClassType) superType).getSource();
                                    } else {
                                        break;
                                    }
                                }
                            }
                            ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
                            if (support != null) {
                                PropertyPresentation pps = support.getPresentation();
                                if (pps != null && pps.isPresentable()) {
                                    //this is posiible to write property,
                                    //no check is it really nessessary
                                    boolean nessessary = true;
                                    AdsPropertyDef propDef = (AdsPropertyDef) prop;

                                    AdsPropertyDef ovr = propDef.getHierarchy().findOverridden().get();
                                    while (ovr != null) {
                                        if (ovr instanceof IAdsPresentableProperty) {
                                            ServerPresentationSupport ovrSupport = ((IAdsPresentableProperty) ovr).getPresentationSupport();
                                            if (ovrSupport != null) {
                                                PropertyPresentation ovrPps = ovrSupport.getPresentation();
                                                if (ovrPps != null && ovrPps.isPresentable()) {
                                                    nessessary = false;
                                                    break;
                                                }
                                            }
                                        }
                                        ovr = ovr.getHierarchy().findOverridden().get();
                                    }
                                    if (!nessessary) {
                                        continue;
                                    }
                                    AdsPropertyWriter w = AdsPropertyWriter.Factory.ppCache.getInstance(propDef, usagePurpose);
                                    presentations.add((AdsPropertyDef) w.getSupport().getCurrentObject());
                                    propWriters.put(prop.getId(), w);
                                }
                            }
                        }
                    }
                }
            }
            List<Id> apiPropIds = iterable.getApiPropIds();
            //this is the code to be copied to 1.2.14,1.2.15-----------
            if (!apiPropIds.isEmpty()) {
                if (filter != null) {
                    for (Id id : apiPropIds) {
                        if (filter.getProperties().findById(id, EScope.ALL).get() != null) {
                            continue;
                        } else {
                            apiProps.add(id);
                        }
                    }
                } else {
                    apiProps.addAll(apiPropIds);
                }
            }
            //end of code to be copied to 1.2.14,1.2.15-----------
        } while (iterable != ssFilter);

    }

    private boolean writeExplorerPropLists(CodePrinter printer, List<AdsPropertyWriter> rootPropsList) {
        final Comparator<AdsPropertyWriter> comparator = new Comparator<AdsPropertyWriter>() {
            @Override
            public int compare(AdsPropertyWriter o1, AdsPropertyWriter o2) {
                return o1.getProperty().getId().compareTo(o2.getProperty().getId());
            }
        };
        Map<Id, AdsPropertyWriter> propWriters = new HashMap<>();
        List<AdsPropertyDef> presentations = new LinkedList<>();
        Set<Id> apiProps = new HashSet<>();
        try {
            AdsPropertyWriter.Factory.ppCache.initialize();
            collectPublishableProps(def, propWriters, presentations, usagePurpose, null, null, apiProps);
            List<AdsPropertyWriter> listToWrite = new ArrayList<>(propWriters.values());
            Collections.sort(listToWrite, comparator);
            if (classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
                for (AdsPropertyWriter w : listToWrite) {
                    if (!w.writeConcreteDeclaration(printer)) {
                        return false;
                    }
                }

            } else {
                for (AdsPropertyWriter w : listToWrite) {
                    if (!w.writeCode(printer)) {
                        return false;
                    }
                }
            }
            for (Id id : apiProps) {
                if (!propWriters.containsKey(id)) {
                    PropertyPresentationPropertyWriter.writePropertyGetterById(printer, id);
                }
            }
            rootPropsList.addAll(listToWrite);


            AdsPropertiesWriter.writePropertiesCreator(def, presentations, printer, usagePurpose);

            if (classType == EClassType.ENTITY || classType == EClassType.APPLICATION) {//generate outer adapters

                AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) def;

                List<AdsEditorPresentationDef> externals = new ArrayList<>(clazz.listPublishableExternals());

                if (!externals.isEmpty()) {
                    final Comparator<AdsEditorPresentationDef> eprcmp = new Comparator<AdsEditorPresentationDef>() {
                        @Override
                        public int compare(AdsEditorPresentationDef o1, AdsEditorPresentationDef o2) {
                            return o1.getId().compareTo(o2.getId());
                        }
                    };

                    Collections.sort(externals, eprcmp);
                    Map<Id, AdsPropertyWriter> propWriters2 = new HashMap<>();
                    Set<Id> apiProps2 = new HashSet<>();
                    List<AdsPropertyDef> presentations2 = new LinkedList<>();
                    for (AdsEditorPresentationDef e : externals) {
                        ERuntimeEnvironmentType effectiveEnv = e.getEffectiveClientEnvironment();

                        if (effectiveEnv != ERuntimeEnvironmentType.COMMON_CLIENT && effectiveEnv != usagePurpose.getEnvironment()) {
                            continue;
                        }
                        propWriters2.clear();
                        apiProps2.clear();
                        presentations2.clear();
                        AdsModelClassDef model;
                        if (e.isUseDefaultModel()) {
                            model = e.findFinalModel();
                        } else {
                            model = e.getModel();
                        }
                        if (model == null) {
                            continue;
                        }
                        printer.print("public static class ");
                        printer.print(e.getId());
                        printer.print("_ModelAdapter");
                        printer.print(" extends ");
                        writeCode(printer, model.getType(EValType.USER_CLASS, ""));
                        printer.print(" implements ");
                        printer.print(def.getId());
                        printer.println('{');
                        printer.enterBlock(1);
                        printer.print("public ");
                        printer.print(e.getId());
                        printer.print("_ModelAdapter(");
                        printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                        printer.print(" userSession,");
                        printer.print(AdsEditorPresentationWriter.EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                        printer.println(" def){super(userSession,def);}");


                        collectPublishableProps(def, propWriters2, presentations2, usagePurpose, model, null, apiProps2);

                        for (AdsPropertyDef p : presentations) {
                            if (!presentations2.contains(p)) {
                                presentations2.add(p);
                            }
                        }
                        listToWrite = new ArrayList<>(propWriters2.values());
                        Collections.sort(listToWrite, comparator);
                        for (AdsPropertyWriter p : listToWrite) {
                            if (!p.writeConcreteDeclaration(printer)) {
                                return false;
                            }
                        }

                        for (Id id : apiProps2) {
                            if (!propWriters2.containsKey(id)) {
                                PropertyPresentationPropertyWriter.writePropertyGetterById(printer, id);
                            }
                        }
                        List<AdsScopeCommandDef> commands = new LinkedList<>();
                        if (!writeExplorerCommandMethods(printer, commands)) {
                            return false;
                        }

                        AdsPropertiesWriter.writePropertiesCreator(def, presentations2, printer, usagePurpose);
                        printer.leaveBlock(1);

                        printer.println('}');
                    }
                }
            }
        } finally {
            AdsPropertyWriter.Factory.ppCache.reset();
        }
        return true;
    }

    private boolean writeCommandClassDecls(final CodePrinter printer, List<AdsScopeCommandDef> commands) {
        if (def instanceof IAdsPresentableClass) {
            // write default model classe for entity model
//            if (!writeDefaultModelClass(printer)) {
//                return false;
//            }

            //final IAdsPresentableClass clazz = (IAdsPresentableClass) def;

            Collections.sort(commands, new Comparator<AdsScopeCommandDef>() {
                @Override
                public int compare(AdsScopeCommandDef o1, AdsScopeCommandDef o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            for (AdsScopeCommandDef command : commands) {
                if (!command.getJavaSourceSupport().getCodeWriter(UsagePurpose.EXPLORER_ADDON).writeExplorerClass(printer, false, true)) {
                    return false;
                }
                printer.println();
            }
        }
        return true;
    }

    protected boolean writeExplorerCommandMethods(final CodePrinter printer, List<AdsScopeCommandDef> commands) {
        if (def instanceof IAdsPresentableClass) {
            final IAdsPresentableClass clazz = (IAdsPresentableClass) def;
            commands.clear();
            commands.addAll(clazz.getPresentations().getCommands().get(EScope.LOCAL_AND_OVERWRITE/*, new IFilter<AdsScopeCommandDef>() {
                     @Override
                     public boolean isTarget(AdsScopeCommandDef radixObject) {
                     if (radixObject.getHierarchy().findOverridden() != null) {
                     return false;
                     }
                     return true;
                     }
                     }*/));
            Collections.sort(commands, new Comparator<AdsScopeCommandDef>() {
                @Override
                public int compare(AdsScopeCommandDef o1, AdsScopeCommandDef o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            writeCommandIdsCache(printer, commands);

            //write dynamic method for inheritance handling

            if (classType != EClassType.ENTITY_GROUP) {
                printer.print("\n@Override\nprotected ");
                printer.print(AdsCommandWriter.EXPLORER_COMMAND_CLASS_NAME);
                printer.print(" createCommand(");
                printer.print(AdsCommandWriter.EXPLORER_COMMAND_META_CLASS_NAME);
                printer.enterBlock();
                printer.println(" def){");
                boolean hasSuperclass = false;
                if (classType == EClassType.APPLICATION) {
                    hasSuperclass = true;
                } else {
                    if (classType != EClassType.ENTITY) {
                        AdsClassDef superClass = def.getInheritance().findSuperClass().get();
                        if (superClass != null && superClass.getClassDefType() == classType) {
                            hasSuperclass = true;
                        }
                    } else {
                        hasSuperclass = true;
                    }
                }

                if (hasSuperclass) {
                    printer.print(AdsCommandWriter.EXPLORER_COMMAND_CLASS_NAME);
                    printer.println(" cmd = createCommand(this,def);");
                    printer.println("if(cmd==null){");
                    printer.enterBlock();
                    printer.println("return super.createCommand(def);");
                    printer.leaveBlock();
                    printer.println("}");
                    printer.println("return cmd;");
                } else {
                    printer.println("return createCommand(this,def);");
                }
                printer.leaveBlock();
                printer.println("}");
            }


//            printer.println("@SuppressWarnings(\"unused\")");
//            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
//            printer.println(" commandId = def.getId();");

            printer.print("public static ");
            printer.print(AdsCommandWriter.EXPLORER_COMMAND_CLASS_NAME);
            printer.print(" createCommand(");
            printer.print(AdsClassWriter.EXPLORER_MODEL_CLASS_NAME);
            printer.print(" model,");
            printer.print(AdsCommandWriter.EXPLORER_COMMAND_META_CLASS_NAME);
            printer.enterBlock();
            printer.println(" def){");

            if (commands.isEmpty()) {
                printer.print("return null");
                printer.leaveBlock();
                printer.printlnSemicolon();
            } else {
                printer.println("@SuppressWarnings(\"unused\")");
                printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                printer.println(" commandId = def.getId();");
                for (AdsScopeCommandDef info : commands) {
                    if (!info.getHierarchy().findOverridden().isEmpty()) {
                        continue;
                    }
                    printer.print("if(");
                    WriterUtils.writeAutoVariable(printer, info.getId().toCharArray());
                    printer.print(" == commandId) return new ");
                    printer.print(def.getRuntimeLocalClassName());
                    printer.print('.');
                    writeUsage(printer, info);
                    printer.println("(model,def);");
                    printer.print("else ");
                }
                boolean superclasswritten = false;
//            if (classType == EClassType.APPLICATION || classType == EClassType.FORM_HANDLER) {
//                final AdsClassDef superclass = def.getInheritance().findSuperClass();
//                if (superclass != null && superclass.getClassDefType() != EClassType.DYNAMIC) {
//                    printer.print("return null;");
////                    superclass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(UsagePurpose.EXPLORER_EXECUTABLE).writeCode(printer);
////                    printer.print(".createCommand(model,def)");
//                    superclasswritten = true;
//                }
//            }
                if (!superclasswritten) {
                    //  printer.print("return def.newCommand(model)");
                    printer.print("return null");
                }

                printer.leaveBlock();
                printer.printlnSemicolon();
            }
            printer.println("}");
        }
        return true;
    }

    private void writeCommandIdsCache(final CodePrinter printer, final List<AdsScopeCommandDef> commands) {
        printer.println("/**Executable command ids cache*/");

        for (AdsScopeCommandDef info : commands) {
            printer.println("@SuppressWarnings(\"unused\")");
            printer.print("private static final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.printSpace();
            WriterUtils.writeAutoVariable(printer, info.getId().toCharArray());
            printer.print(" = ");
            WriterUtils.writeIdUsage(printer, info.getId());
            printer.printlnSemicolon();
        }
    }

    public static boolean checkEnv(ERuntimeEnvironmentType objEnv, UsagePurpose usagePurpose) {
        boolean write = false;

        if (objEnv == usagePurpose.getEnvironment() || objEnv == ERuntimeEnvironmentType.SERVER || objEnv == ERuntimeEnvironmentType.COMMON) {
            write = true;
        } else {
            switch (usagePurpose.getEnvironment()) {
                case EXPLORER:
                case WEB:
                    if (objEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        write = true;
                    }
                    break;
            }
        }
        return write;
    }

    protected void writeExecutableHeader(final CodePrinter printer) {
        //Jml header = def.getHeader();
        List<AdsClassDef.ClassSource> headers = new LinkedList<>();


        AdsClassDef ovr = def;
        while (ovr != null) {
            AdsClassDef.ClassSource header = ovr.getHeader();
            if (header != null) {
                headers.add(0, header);
            }
            ovr = ovr.getHierarchy().findOverwritten().get();
        }

        if (!headers.isEmpty()) {

            for (AdsClassDef.ClassSource header : headers) {
                for (AdsClassDef.SourcePart part : header) {
                    if (checkEnv(part.getUsageEnvironment(), usagePurpose)) {
                        try {
                            writeCustomMarker(printer, part.getName());
                            printer.enterCodeSection(part.getLocationDescriptor());
                            writeCode(printer, part);
                        } finally {
                            printer.leaveCodeSection();
                        }
                    }
                }
                printer.println();
            }
        }
        writeCustomHeader(printer);
    }
    // private final char[] ARTE_STORAGE_NAME = "$arte_for_internal_use_storage$".toCharArray();

    protected void writeCustomHeader(final CodePrinter printer) {
    }

    protected void writeCustomBody(final CodePrinter printer) {//changed by yremizov
        switch (classType) {
            case ENTITY_MODEL:
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");
                printer.print(AdsEditorPresentationWriter.EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                printer.println(" def){super(userSession,def);}");
                break;
            case GROUP_MODEL:
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");
                printer.print(AdsSelectorPresentationWriter.SELECTOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                printer.println(" def){super(userSession,def);}");
                break;
            case PARAGRAPH_MODEL:
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");

                printer.print(AdsParagraphWriter.EXPLORER_ITEM_META_EXPLORER_CLASS_NAME);
                printer.println(" def){super(userSession,def);}");
                break;
            case FORM_MODEL:
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");
                printer.print(AdsFormPresentationWriter.EXPLORER_META_CLASS_NAME);
                printer.println(" def){super(userSession,def);}");
                break;
            case REPORT_MODEL:
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");

                printer.print(AdsReportPresentationWriter.EXPLORER_META_CLASS_NAME);
                printer.println(" def){super(userSession,def);}");
                break;
            case FILTER_MODEL:
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");

                printer.print(AdsFilterWriter.FILTER_META_EXPLORER_CLASS_NAME);
                printer.println(" def){super(userSession,def);}");
                break;
            case PROP_EDITOR_MODEL://added by yremizov
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");
                printer.print("org.radixware.kernel.common.client.views.IPropEditorDialog");
                printer.println(" def){super(userSession,def);}");
                break;
            case DIALOG_MODEL://added by yremizov
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");

                printer.print("org.radixware.kernel.common.client.views.ICustomDialog");
                printer.println(" def){super(userSession,def);}");
                break;
            case CUSTOM_WDGET_MODEL://added by yremizov
                printer.println();
                printer.print("public ");
                writeUsage(printer);
                printer.print('(');
                printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
                printer.print(" userSession,");

                if (def.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                    printer.print("org.radixware.kernel.explorer.views.CustomWidget");
                } else {
                    printer.print(AdsRwtCustomWidgetDef.PLATFORM_CLASS_NAME_STR);
                }
                printer.println(" def){super(userSession,def);}");
                break;
            case PRESENTATION_ENTITY_ADAPTER:
                printer.println();
                printer.print("public ");
                printer.print(def.getId().toCharArray());
                printer.print('(');
                printer.print(AdsEntityClassDef.PLATFORM_CLASS_NAME);
                printer.println(" e){super(e);}");
                break;
            case ENTITY_GROUP:
                printer.println();
                printer.print("public ");
                printer.print(def.getId().toCharArray());
                printer.print("(boolean isContextWrapper){super(isContextWrapper);}");
                break;
            case FORM_HANDLER:
                AdsMethodDef constructor = def.getMethods().findBySignature(("<init>(L" + AdsFormHandlerClassDef.PLATFORM_CLASS_NAME + ";)V").toCharArray(), EScope.LOCAL);
                if (constructor == null || !constructor.isConstructor()) {
                    printer.println();
                    printer.print("public ");
                    printer.print(def.getId().toCharArray());
                    printer.print('(');
                    printer.print(AdsFormHandlerClassDef.PLATFORM_CLASS_NAME);
                    printer.println(" prevForm){super(prevForm);}");
                }
                break;


        }
        if (def instanceof AdsModelClassDef) { //yremizov
            AdsAbstractUIDef ui = null;

            ICustomViewable owner = null;
            if (def instanceof AdsEntityModelClassDef) {
                owner = ((AdsEntityModelClassDef) def).getOwnerEditorPresentation();
            } else if (def instanceof AdsGroupModelClassDef) {
                owner = ((AdsGroupModelClassDef) def).getOwnerSelectorPresentation();
            } else if (def instanceof AbstractFormModelClassDef) {
                owner = ((IAdsFormPresentableClass) ((AbstractFormModelClassDef) def).getOwnerClass()).getPresentations();
            } else if (def instanceof AdsParagraphModelClassDef) {//added by yremizov
                owner = ((AdsParagraphModelClassDef) def).getOwnerParagraph();
            }
            if (def instanceof AdsFilterModelClassDef) {//added by yremizov
                owner = ((AdsFilterModelClassDef) def).getOwnerFilterDef();
            }


            if (owner != null) {
                final boolean isViewInherited;
                if (owner instanceof AdsPresentationDef) {
                    isViewInherited = ((AdsPresentationDef) owner).isCustomViewInherited();
                } else if (owner instanceof AdsFormHandlerClassDef) {
                    isViewInherited = ((AdsFormHandlerClassDef) owner).getPresentations().isCustomViewInherited();
                } else {
                    isViewInherited = false;
                }
                if (owner.getCustomViewSupport().isUseCustomView(usagePurpose.getEnvironment())) {
                    if (!isViewInherited) {
                        ui = owner.getCustomViewSupport().getCustomView(usagePurpose.getEnvironment());
                    } else {
                        ui = owner.getCustomViewSupport().getCustomView(usagePurpose.getEnvironment());
                        AdsDefinition parent = ui.getOwnerDef();
                        AdsDefinition ownerDef = (AdsDefinition) owner;
                        boolean unsetUI = true;
                        while (ownerDef != null) {
                            if (ownerDef == parent) {
                                unsetUI = false;
                                break;
                            }
                            ownerDef = ownerDef.getHierarchy().findOverwritten().get();
                        }
                        if (unsetUI) {
                            ui = null;
                        }
                    }
                }

            } else if (def.getOwnerDef() instanceof AdsAbstractUIDef) {//models of dialog and prop editor dialog
                ui = (AdsAbstractUIDef) def.getOwnerDef();
            }

            if (ui != null) {//yremizov, BAO
                final Id customDialogId = ui.getId();
                final AdsType type = ui.getType(EValType.USER_CLASS, null);
                final JavaSourceSupport.CodeWriter writer = type.getJavaSourceSupport().getCodeWriter(usagePurpose);

                printer.println();

                if (ui.getDefinitionType() != EDefType.CUSTOM_DIALOG && ui.getDefinitionType() != EDefType.CUSTOM_PROP_EDITOR && ui.getDefinitionType() != EDefType.CUSTOM_WIDGET_DEF) {
                    printer.println("@Override");
                    printer.println("@SuppressWarnings(\"deprecation\")");
                    printer.println("public org.radixware.kernel.common.client.views.IView createView() { return new ");
                    writer.writeUsage(printer);
                    printer.println("(getEnvironment());}");
                }
                printer.println("@Override");
                printer.print("public ");
                printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                printer.print(" getCustomViewId() {return ");
                WriterUtils.writeIdUsage(printer, customDialogId);
                printer.println(";}");

                printer.println();
//                printer.println("@SuppressWarnings(\"deprecation\")");
//                printer.print("protected ");
//                writer.writeUsage(printer);
//                printer.print(" getCustomView$$$");
//                printer.print(customDialogId.toCharArray());
//                printer.print("() {return (");
//                writer.writeUsage(printer);
//                printer.print(")getView();}"); // FIXME define const
            }
//            final EditorPages editorPages;
//            if (def instanceof AdsEntityModelClassDef) {
//                final AdsEditorPresentationDef p = ((AdsEntityModelClassDef) def).getOwnerEditorPresentation();
//                if (!PresentationFinalAttributes.isEditorPagesInherited(p)) {
//                    editorPages = p.getEditorPages();
//                } else {
//                    editorPages = null;
//                }
//            } else if (def instanceof AbstractFormModelClassDef) {
//                final AbstractFormPresentations p = ((IAdsFormPresentableClass) ((AbstractFormModelClassDef) def).getOwnerClass()).getPresentations();
//                if (!PresentationFinalAttributes.isEditorPagesInherited(p)) {
//                    editorPages = p.getEditorPages();
//                } else {
//                    editorPages = null;
//                }
//            } else if (def instanceof AdsFilterModelClassDef) {
//                final AdsFilterDef p = ((AdsFilterModelClassDef) def).getOwnerFilterDef();
//                editorPages = p.getEditorPages();
//            } else {
//                editorPages = null;
//            }
//            if (editorPages != null) {
//                for (AdsEditorPageDef page : editorPages.get(EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsEditorPageDef>() {
//                    @Override
//                    public boolean isTarget(AdsEditorPageDef radixObject) {
//                        return radixObject.getType() == EEditorPageType.CUSTOM;
//                    }
//                })) {
//                    if (page.getCustomViewSupport().isUseCustomView(usagePurpose.getEnvironment())) {
//                        ui = page.getCustomViewSupport().getCustomView(usagePurpose.getEnvironment());
//                        if (ui != null) {
//                            printer.println();
//                            printer.println("@SuppressWarnings(\"deprecation\")");
//                            printer.print("protected ");
//                            AdsType type = ui.getType(EValType.USER_CLASS, null);
//                            JavaSourceSupport.CodeWriter writer = type.getJavaSourceSupport().getCodeWriter(usagePurpose);
//                            writer.writeUsage(printer);
//                            printer.print(" getCustomView$$$");
//                            printer.print(ui.getId().toCharArray());
//                            printer.print("() {return (");// FIXME define const
//                            writer.writeUsage(printer);
//                            printer.print(")getEditorPage(");
//                            WriterUtils.writeIdUsage(printer, page.getId());
//                            printer.print(").getView();}"); // FIXME define const
//                        }
//                    }
//                }
//            }
        }
    }

//added by yremizov
    protected void writeExecutableBody(CodePrinter printer) {
        List<AdsClassDef.ClassSource> bodies = new LinkedList<>();

        if (def instanceof AdsModelClassDef) {
            printer.print("public ");
            printer.print(CLIENT_META_CLASS_NAME);
            printer.print(" getRadMeta(){ return ");
            printer.print(def.getRuntimeLocalClassName());
            printer.println("_mi.rdxMeta; }");
        }

        AdsClassDef ovr = def;
        while (ovr != null) {
            AdsClassDef.ClassSource body = ovr.getBody();
            if (body != null) {
                bodies.add(0, body);
            }
            ovr = ovr.getHierarchy().findOverwritten().get();
        }

        if (!bodies.isEmpty()) {
            //writeCustomMarker(printer, AdsClassDef.CLASS_BODY_NAME);
            for (AdsClassDef.ClassSource body : bodies) {
                for (AdsClassDef.SourcePart p : body) {
                    if (checkEnv(p.getUsageEnvironment(), usagePurpose)) {
                        try {
                            writeCustomMarker(printer, p.getName());
                            printer.enterCodeSection(p.getLocationDescriptor());
                            writeCode(printer, p);

                        } finally {
                            printer.leaveCodeSection();
                        }
                    }
                }
                printer.println();
            }
        }

        //utility methods
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                if (classType != EClassType.INTERFACE && !def.isAnonymous()) {
                    //metainfo accessor
                    printer.println("/**Metainformation accessor method*/");
                    printer.print("public ");
                    printer.print(META_CLASS_NAME);
                    printer.print(" getRadMeta(){return ");
                    //WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                    //printer.print(".getDefManager().getClassDef(");
                    //WriterUtils.writeIdUsage(printer, def.getId());
                    //printer.println(");}");
                    printer.print(def.getTopLevelEnclosingClass().getRuntimeLocalClassName());
                    printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
                    printer.print('.');
                    printer.print(getRdxMetaName(def));
                    printer.println(";}");
                }
                //arte accessor
                if (!def.isNested()) {
                    WriterUtils.writeServerArteAccessMethodDeclaration(def, printer);
                }
                break;

        }

        writeCustomBody(printer);
        writeCode(printer, def.getNestedClasses());
        writeCode(printer, def.getProperties());
        writeCode(printer, def.getMethods());
    }

    private boolean isInterface() {
        return def instanceof AdsInterfaceClassDef;
    }

    private boolean isEnumClass() {
        return def instanceof AdsEnumClassDef;
    }

    protected void writeTypeDeclaration(CodePrinter printer, boolean writeAllContent) {
        printer.println();
        WriterUtils.writeMetaAnnotation(printer, def, false);
        if (writeAllContent) {
            switch (classType) {
                case FILTER_MODEL:
                case PARAGRAPH_MODEL:
                case ENTITY_MODEL:
                case FORM_MODEL:
                case GROUP_MODEL:
                case CUSTOM_WDGET_MODEL:
                case PROP_EDITOR_MODEL:
                case DIALOG_MODEL:
                    printer.print("public ");
                    break;
                default:
                    writeCode(printer, def.getAccessFlags());
            }
            writeClassDefinitionMethod(printer);
        } else {
            switch (classType) {
                case ENTITY:
                case APPLICATION:
                    //case FORM_HANDLER:
                    //case REPORT:
                    printer.print("public interface");
                    break;
                default:
                    printer.print("public class");
            }
        }
        printer.printSpace();
        writeUsage(printer);
        writeCode(printer, def.getTypeArguments(), def);

        if (writeAllContent) {
            printer.printSpace();
            writeSuperclasses(printer);
        } else {
            printer.printSpace();
            switch (classType) {
                case APPLICATION:
                    printer.printSpace();
                    writeSuperclasses(printer);
                    break;
                case FORM_HANDLER:
                case REPORT:
                    printer.printSpace();
                    writeDefaultModelSuperClasses(printer);
            }

        }

    }

    protected void writeClassDefinitionMethod(CodePrinter printer) {
        if (isInterface()) {
            printer.print(TEXT_INTERFACE);
        } else {
            printer.print(TEXT_CLASS);
        }

    }

    private AdsClassDef findServerSideClass(AdsClassDef model) {
        RadixObject container = model.getContainer();
        while (container != null) {
            if (container instanceof AdsClassDef) {
                AdsClassDef clazz = (AdsClassDef) container;
                if (clazz.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                    return clazz;
                }
            }
            container = container.getContainer();
        }
        return null;
    }

    private boolean writeDefaultConstructorForModelClass(CodePrinter printer) {
        if (classType == EClassType.ENTITY_GROUP) {
            return true;
        }
        printer.print("public ");
        printer.print(def.getRuntimeLocalClassName());
        if (classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
            printer.print("_DefaultModel(");
        } else {
            printer.print('(');
        }
        printer.print(WriterUtils.USER_SESSION_CLASS_NAME);
        printer.print(" userSession,");
        switch (classType) {
            case ENTITY:
            case APPLICATION:
                printer.print(AdsEditorPresentationWriter.EDITOR_PRESENTATION_META_EXPLORER_CLASS_NAME);
                break;
            case FORM_HANDLER:
                printer.print(AdsFormPresentationWriter.EXPLORER_META_CLASS_NAME);
                break;
            case REPORT:
                printer.print(AdsReportPresentationWriter.EXPLORER_META_CLASS_NAME);
                break;
            default:
                return false;
        }
        printer.println(" def){super(userSession,def);}");
        return true;
    }

    private boolean writeDefaultModelSuperClasses(CodePrinter printer) {
        //final AdsTypeDeclaration decl = def.getInheritance().getSuperClassRef();
        AdsType resolvedSuperRef = null;
        switch (classType) {
            case APPLICATION:
            case ENTITY:
            case FORM_HANDLER:
            case REPORT:
                if (resolvedSuperRef == null) {
                    resolvedSuperRef = def.getInheritance().getSuperClassRef().resolve(def).get();
                }
                boolean writeDefaultSuperClass = false;
                if (resolvedSuperRef instanceof AdsClassType) {
                    if (((AdsClassType) resolvedSuperRef).getSource() instanceof AdsDynamicClassDef) {
                        writeDefaultSuperClass = true;
                    } else {
                        if (classType == EClassType.FORM_HANDLER) {
                            resolvedSuperRef = ((AdsFormHandlerClassDef) ((AdsClassType) resolvedSuperRef).getSource()).getPresentations().getModel().getType(EValType.USER_CLASS, null);
                        }
                    }
                } else if (resolvedSuperRef instanceof JavaClassType) {
                    writeDefaultSuperClass = true;
                }

                if (writeDefaultSuperClass) {
                    printer.print(" extends ");
                    switch (classType) {
                        case APPLICATION:
                            return false;
                        case ENTITY:
                            printer.print(AdsModelClassDef.ENTITY_MODEL_JAVA_CLASS_NAME);
                            break;
                        case FORM_HANDLER:
                            printer.print(AdsModelClassDef.FORM_MODEL_JAVA_CLASS_NAME);
                            break;
                        case REPORT:
                            printer.print(AdsModelClassDef.REPORT_MODEL_JAVA_CLASS_NAME);
                            break;
                    }
                } else {
                    printer.print(" extends ");
                    writeUsage(printer, resolvedSuperRef);
                    switch (classType) {
                        case APPLICATION:
                            //case FORM_HANDLER:
                            printer.print('.');
                            printer.print(((AdsClassType) resolvedSuperRef).getSource().getId());
                            printer.print("_DefaultModel");

                    }

                }
                return true;
            case ENTITY_GROUP:
                return true;
            default:
                return false;

        }
    }

    private boolean writeSuperDeclaration(final CodePrinter printer, AdsTypeDeclaration superClassDeclaration, AdsClassDef context) {
        final AdsType superType = superClassDeclaration.resolve(context).get();

//        if (superType instanceof AdsClassType) {
//            final AdsClassDef superClass = ((AdsClassType) superType).getSource();
//            if (superClass != null && superClass.isNested()) {
//
//                for (final AdsClassDef cls : superClass.getNestedClassesChain(false, true)) {
//                    if (!writeCode(printer, AdsTypeDeclaration.Factory.newInstance(cls), context)) {
//                        return false;
//                    }
//                    printer.print('.');
//                }
//            }
//        }
        return writeCode(printer, superClassDeclaration, context);
    }

    private boolean writeSuperclasses(CodePrinter printer) {
        boolean isExtendableTransparent = false;
        if (def.getTransparence() != null && def.getTransparence().isTransparent() && def.getTransparence().isExtendable()) {
            isExtendableTransparent = true;
        }
        boolean superClassWritten = false;
        final AdsTypeDeclaration decl = def.getInheritance().getSuperClassRef();
        AdsType resolvedSuperRef = null;
        if (this.usagePurpose.getEnvironment() == ERuntimeEnvironmentType.EXPLORER || this.usagePurpose.getEnvironment() == ERuntimeEnvironmentType.WEB) {
            switch (classType) {
                case GROUP_MODEL:
                case ENTITY_MODEL:
                case FORM_MODEL:
                case REPORT_MODEL:
                    if (resolvedSuperRef == null) {
                        resolvedSuperRef = def.getInheritance().getSuperClassRef().resolve(def).get();
                    }
                    if (resolvedSuperRef instanceof AdsClassType && ((AdsClassType) resolvedSuperRef).getSource().getClass() == def.getClass()) {
                        if (classType == EClassType.FORM_MODEL) {
                            printer.printSpace();
                            printer.print(TEXT_EXTENDS);
                            printer.printSpace();
                            writeUsage(printer, findServerSideClass(def).getType(EValType.USER_CLASS, null));
                            superClassWritten = true;
                        } else {
                            String adapterName = null;
                            if (classType == EClassType.ENTITY_MODEL) {
                                AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) def).getOwnerEditorPresentation();
                                if (epr == null) {
                                    return false;
                                }
                                if (epr.getBasePresentationId() != null) {
                                    AdsEditorPresentationDef base = epr.findBaseEditorPresentation().get();
                                    if (base == null) {
                                        return false;
                                    }
                                    if (base.getOwnerClass() != epr.getOwnerClass() && base.getOwnerClass().getId() != epr.getOwnerClass().getId()) {//inherit throw adapter
                                        adapterName = base.getId().toString() + "_ModelAdapter";
                                    }
                                }
                            }
                            if (adapterName != null) {
                                printer.printSpace();
                                printer.print(TEXT_EXTENDS);
                                printer.printSpace();
                                AdsClassDef serverClass = findServerSideClass(def);
                                writeUsage(printer, findServerSideClass(def).getType(EValType.USER_CLASS, null));
                                printer.print('.');
                                printer.print(serverClass.getId());
                                printer.print("_DefaultModel.");
                                printer.print(adapterName);
                                superClassWritten = true;
                            }
                        }
                    } else {
                        printer.printSpace();
                        printer.print(TEXT_EXTENDS);
                        printer.printSpace();
                        AdsClassDef serverClass = findServerSideClass(def);
                        writeUsage(printer, serverClass.getType(EValType.USER_CLASS, null));

                        if (classType == EClassType.GROUP_MODEL) {
                            printer.print('.');
                            printer.print(AdsModelClassDef.getDefaultModelLocalClassName(EClassType.GROUP_MODEL));
                        } else {
                            if (serverClass.getClassDefType() != EClassType.FORM_HANDLER && serverClass.getClassDefType() != EClassType.REPORT) {
                                printer.print('.');
                                printer.print(serverClass.getId());
                                printer.print("_DefaultModel");
                            }
                        }

                        superClassWritten = true;
                    }
                    break;
            }
        }

        if (!superClassWritten) {
            if (!isInterface() && !isEnumClass() || (isInterface() && isExtendableTransparent)) {
                if (decl != null) {
                    printer.printSpace();
                    printer.print(TEXT_EXTENDS);
                    printer.printSpace();
                    if (resolvedSuperRef == null) {
                        resolvedSuperRef = def.getInheritance().getSuperClassRef().resolve(def).get();
                    }
                    if (resolvedSuperRef instanceof JavaClassType) {//possibly extende published clas;
                        final String publishedClassName = ((JavaClassType) resolvedSuperRef).getJavaClassName();
                        final IPlatformClassPublisher publisher = ((AdsSegment) def.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(publishedClassName);
                        if (publisher != def && publisher instanceof AdsClassDef && publisher.getPlatformClassPublishingSupport().isExtendablePublishing()) {
                            writeUsage(printer, ((AdsClassDef) publisher).getType(EValType.USER_CLASS, null));
                        } else {
                            printer.print(publishedClassName);
                            AdsTypeDeclaration.TypeArguments args = def.getTypeArguments();
                            if (args != null && !args.isEmpty()) {
                                printer.print('<');
                                boolean first = true;
                                for (AdsTypeDeclaration.TypeArgument a : args.getArgumentList()) {
                                    if (first) {
                                        first = false;
                                    } else {
                                        printer.printComma();
                                    }
                                    printer.print(a.getName());
                                }
                                printer.print('>');
                            }
                        }
                    } else {
                        if (!writeSuperDeclaration(printer, decl, def)) {
                            return false;
                        }
                    }

                    printer.printSpace();
                } else {
                    if (classType == EClassType.EXCEPTION) {
                        printer.printSpace();
                        printer.print(TEXT_EXTENDS);
                        printer.printSpace();
                        printer.print("java.lang.Throwable");
                        printer.printSpace();
                    }
                }
                //}
            }
        }

        if (!writeSuperinterfaces(printer)) {
            return false;
        }
        printer.printSpace();
        return true;
    }

    protected void writeSuperInterfaceUsageMethod(CodePrinter printer) {
        if (classType == EClassType.INTERFACE) {
            printer.print(TEXT_EXTENDS);
        } else {
            printer.print(TEXT_IMPLEMENTS);
        }
    }

    private boolean writeSuperinterfaces(CodePrinter printer) {
        ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case EXPLORER:
            case WEB:
                switch (classType) {
                    case ENTITY:
                    case ENTITY_GROUP:
                    case APPLICATION:
                    case FORM_HANDLER:
                    case REPORT:
                        return true;
                }
        }
        List<AdsTypeDeclaration> interfaces = def.getInheritance().getInerfaceRefList(EScope.LOCAL_AND_OVERWRITE);
        if (willHavePropAccessors()) {
            if (interfaces == null) {
                interfaces = new ArrayList<>();
            }
            interfaces.add(AdsTypeDeclaration.Factory.newPlatformClass(new String(RAD_PROP_ACCESSOR_PROVIDER)));
        }

        if (interfaces != null && !interfaces.isEmpty()) {

            boolean isFirst = true;
            for (AdsTypeDeclaration type : interfaces) {

                if (def.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT && def.isDual()) {
                    AdsType resolvedType = type.resolve(def).get();
                    if (resolvedType instanceof AdsClassType) {
                        AdsClassDef resolvedClass = ((AdsClassType) resolvedType).getSource();
                        if (resolvedClass != null) {
                            ERuntimeEnvironmentType ifaceenv = resolvedClass.getUsageEnvironment();
                            if (ifaceenv != ERuntimeEnvironmentType.COMMON_CLIENT && env != ifaceenv) {
                                continue;
                            }
                        }
                    }
                }

                if (isFirst) {
                    printer.printSpace();
                    writeSuperInterfaceUsageMethod(printer);
                    printer.printSpace();
                    isFirst = false;
                } else {
                    printer.printComma();
                }

                if (!writeSuperDeclaration(printer, type, def)) {
                    return false;
                }
            }

            printer.printSpace();
        }
        return true;

    }

    private boolean willHavePropAccessors() {
        return AdsPropertiesWriter.willHaveAccessors(def, usagePurpose);
    }
    private static final char[] META_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.RadClassDef".toCharArray();
    private static final char[] ACCESS_AREAS_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.RadClassAccessArea".toCharArray();
    private static final char[] ACCESS_PARTITION_CLASS_NAME = "org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition".toCharArray();

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        assert !def.isNested() : "Write meta for nested class";
        WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
        WriterUtils.writeMetaShareabilityAnnotation(printer, def);
        printer.println();
        printer.print("public final class ");
        printer.print(getMetaClassName());
        printer.enterBlock(1);
        printer.println('{');
        if (!writeMetaImpl(printer, def)) {
            return false;
        }

        if (!writeMetaForNested(printer, def.getNestedClasses())) {
            return false;
        }
        printer.leaveBlock(1);
        printer.println('}');
        return true;
    }

    private String getMetaClassName() {
        return def.getRuntimeLocalClassName() + "_mi";
    }

    protected boolean writeMetaForNested(CodePrinter printer, NestedClasses nested) {
        for (final AdsClassDef clazz : nested.getLocal()) {
            if (!writeMetaImpl(printer, clazz)) {
                return false;
            }
            if (!writeMetaForNested(printer, clazz.getNestedClasses())) {
                return false;
            }
        }
        return true;
    }

    private String getRdxMetaName(AdsClassDef clazz) {
        if (clazz.isNested()) {
            return "rdxMeta_" + clazz.getId().toString();
        }
        return "rdxMeta";
    }
    private static final char[] CLIENT_META_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadClassPresentationDef".toCharArray(), '.');

    protected boolean writeMetaImpl(CodePrinter printer, AdsClassDef clazz) {

        switch (usagePurpose.getEnvironment()) {
            //case EXPLORER:
            case EXPLORER:
            case WEB:
                if (clazz instanceof IAdsPresentableClass) {
                    printer.print("public static final ");
                    AdsClassPresentationsWriter writer = (AdsClassPresentationsWriter) (((IAdsPresentableClass) clazz).getPresentations()).getJavaSourceSupport().getCodeWriter(usagePurpose);
                    printer.print(writer.getExplorerMetaClassName());
                    printer.printSpace();
                    printer.print(getRdxMetaName(clazz));
                    printer.print(" = ");
                    return writer.writeCode(printer);
                } else if (clazz instanceof AdsModelClassDef) {
                    printer.print("public static final ");

                    printer.print(CLIENT_META_CLASS_NAME);
                    printer.printSpace();
                    printer.print(getRdxMetaName(clazz));
                    printer.print(" =  new ");
                    printer.print(CLIENT_META_CLASS_NAME);
                    printer.enterBlock(5);
                    printer.print("(");

//                      final Id id, 
//                      final String name, 
//                      final Id baseClassId,
//                      final Id iconId, 
//                      final Id defaultSelectorPresentationId,
//                      final Id classTitleId, 
//                      final Id grpTitleId, 
//                      final Id objTitleId, 
//                      final long restrictionsMask, 
//                      final RadPropertyDef[] properties, 
//                      final RadCommandDef[] commands, 
//                      final RadFilterDef[] filters, 
//                      final RadSortingDef[] sortings, 
//                      final RadReferenceDef[] references, 
//                      final Id[] presentationIds, 
//                      final boolean isClassCatalogsDefined, 
//                      final boolean isAuditEnabled,
//                      final boolean isUserFunction
                    WriterUtils.writeIdUsage(printer, def.getId());
                    printer.printComma();
                    printer.println();
                    printer.printStringLiteral(def.getName());
                    printer.printComma();
                    printer.println();
                    AdsClassDef baseClass = def.getInheritance().findSuperClass().get();
                    if (baseClass != null && baseClass instanceof AdsDynamicClassDef == false) {
                        WriterUtils.writeIdUsage(printer, baseClass.getId());
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    printer.println();
                    //iconid
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //defaultSPrId
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //classTitleId
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //grpTitleId
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //objTitleId
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //restrictionMask
                    printer.print(0L);
                    printer.printComma();
                    def.getProperties().getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                    printer.printComma();
                    printer.println();
                    //commands                    
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //filters
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //sortings
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //references
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //presentations
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    printer.println();
                    //isCCDefined
                    printer.print(false);
                    printer.printComma();
                    printer.println();
                    //isAuditEnabled
                    printer.print(false);
                    printer.printComma();
                    printer.println();
                    //isUf
                    printer.print(false);
                    printer.println();
                    printer.println(");");
                    printer.leaveBlock(5);
                    return true;
                } else {
                    return true;
                }

            case SERVER:
                /**
                 * public RadClassDef(final Arte arte, final Id id, final String
                 * name, final Id titleId, final EClassType type, final
                 * RadClassPresentationDef pres, final Id entityId, final Id
                 * ancestorId, final RadPropDef[] props, final Id[]
                 * detailsRefIds, final EAccessAreaType accessAreaType, final Id
                 * accessAreaInheritRefId, final RadClassAccessArea[]
                 * accessAreas )
                 */
                //---------------------RADIX-1831-----------------------
                if (!clazz.isNested()) {
                    WriterUtils.writeServerArteAccessMethodDeclaration(clazz, JavaSourceSupport.CodeType.META, printer);
                }
                printer.println("@SuppressWarnings(\"deprecation\")");
                //--------------------- end of RADIX-1831-----------------------
                printer.print("public static final ");
                printer.print(META_CLASS_NAME);
                printer.printSpace();
                printer.print(getRdxMetaName(clazz));
                printer.print(" = new ");
                printer.print(META_CLASS_NAME);
                printer.print('(');
                WriterUtils.writeReleaseAccessorInMetaClass(printer);
                printer.printComma();
                WriterUtils.writeIdUsage(printer, clazz.getId());
                printer.printComma();
                printer.printStringLiteral(clazz.getName());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, clazz.getTitleId());
                printer.printComma();
                printer.println();
                printer.enterBlock(5);
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, classType);
                printer.printComma();
                printer.println();
                if (clazz instanceof IAdsPresentableClass) {
                    ((IAdsPresentableClass) clazz).getPresentations().getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
                } else {
                    WriterUtils.writeNull(printer);
                }

                printer.printComma();
                printer.println();
                if (clazz instanceof AdsEntityBasedClassDef) {
                    WriterUtils.writeIdUsage(printer, ((AdsEntityBasedClassDef) clazz).getEntityId());
                } else {
                    WriterUtils.writeNull(printer);
                }

                printer.printComma();
                printer.println();
                AdsClassDef superClass = clazz.getInheritance().findSuperClass().get();
                if (superClass != null && (superClass.getTransparence() == null || !superClass.getTransparence().isTransparent() || superClass.getTransparence().isExtendable())) {
                    WriterUtils.writeIdUsage(printer, superClass.getId());
                } else {
                    WriterUtils.writeNull(printer);
                }

                printer.printComma();
                printer.println();
                writeCode(printer, clazz.getProperties());
                printer.printComma();
                printer.println();
                writeCode(printer, clazz.getMethods());
                printer.printComma();
                printer.println();

                if (clazz instanceof AdsEntityObjectClassDef) {
                    final List<AdsEntityObjectClassDef.DetailReferenceInfo> list = ((AdsEntityObjectClassDef) clazz).getAllowedDetailRefs();
                    if (!list.isEmpty()) {
                        Id[] ids = new Id[list.size()];
                        for (int i = 0; i < ids.length; i++) {
                            ids[i] = list.get(i).getReferenceId();
                        }

                        WriterUtils.writeIdArrayUsage(printer, ids);
                    } else {
                        WriterUtils.writeNull(printer);
                    }

                } else {
                    WriterUtils.writeNull(printer);
                }

                printer.printComma();
                printer.println();
                if (clazz instanceof AdsEntityClassDef) {
                    final AdsEntityClassDef.AccessAreas areas = ((AdsEntityClassDef) clazz).getAccessAreas();
                    WriterUtils.writeEnumFieldInvocation(printer, areas.getType());
                    printer.printComma();
                    WriterUtils.writeIdUsage(printer, areas.getInheritReferenceId());
                    printer.printComma();
                    new WriterUtils.SameObjectArrayWriter<AdsEntityClassDef.AccessAreas.AccessArea>(ACCESS_AREAS_CLASS_NAME) {
                        @Override
                        public void writeItemConstructorParams(CodePrinter printer, AccessArea item) {
                            /*
                             * RadClassAccessArea(final Partition[] partitions)
                             */
                            new WriterUtils.SameObjectArrayWriter<AdsEntityClassDef.AccessAreas.AccessArea.Partition>(ACCESS_PARTITION_CLASS_NAME) {
                                @Override
                                public void writeItemConstructorParams(CodePrinter printer, Partition item) {
                                    //public Partition(final Id familyId, final Id[] referenceIds, final Id propId)
                                    WriterUtils.writeIdUsage(printer, item.getFamilyId());
                                    printer.printComma();
                                    WriterUtils.writeIdArrayUsage(printer, item.getReferenceIds());
                                    printer.printComma();
                                    WriterUtils.writeIdUsage(printer, item.getPropertyId());
                                }
                            }.write(printer, item.getPartitions().list());
                        }
                    }.write(printer, areas.list());
                } else {
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    WriterUtils.writeNull(printer);
                    printer.printComma();
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                printer.print(clazz.isDeprecated());
                printer.leaveBlock(5);
                printer.println(");");

                return true;
            default:
                return true;

        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        if (def.isNested()) {
            printer.print(def.getId().toCharArray());
        } else {
            printer.print(def.getRuntimeLocalClassName());
        }
    }
}
