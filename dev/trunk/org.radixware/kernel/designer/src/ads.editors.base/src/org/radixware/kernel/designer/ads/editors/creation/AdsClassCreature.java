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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArguments;
import org.radixware.kernel.common.enums.EClassType;
import static org.radixware.kernel.common.enums.EClassType.REPORT;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.common.lookup.AdsClassLookupSupport;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


public class AdsClassCreature extends AdsModuleDefinitionCreature<AdsClassDef> {

    public static final class Factory {

        public static ICreature newTestInstance(AdsModule context) {
            return new TestCaseCreature(context);
        }

        public static AdsClassCreature newInstance(AdsModule context, EClassType classType) {
            return newInstance(context, classType, false);
        }

        public static AdsClassCreature newInstance(AdsModule context, EClassType classType, boolean userReport) {
            switch (classType) {
                case ENTITY:
                case ENTITY_GROUP:
                case APPLICATION:
                case PRESENTATION_ENTITY_ADAPTER:
                    return new AdsEntityClassCreature(context, classType);
                default:
                    return new AdsClassCreature(context, classType, userReport);
            }
        }

        public static List<ICreature> instanceList(AdsModule context, EnumSet<EClassType> classTypes) {
            ArrayList<ICreature> result = new ArrayList<>();
            for (EClassType ct : classTypes) {
                result.add(newInstance(context, ct));
            }

            return result;
        }
    }

    public static void updateEntityGroupAfterCreate(AdsEntityGroupClassDef groupClass, AdsEntityObjectClassDef selectedbasis) {
        AdsTypeDeclaration basisType = AdsTypeDeclaration.Factory.newEntityObject(selectedbasis);
     //   System.out.println(basisType.resolve(selectedbasis).get().getQualifiedName(selectedbasis));
        TypeArgument arg = TypeArgument.Factory.newInstance(basisType);
        TypeArguments args = TypeArguments.Factory.newInstance(groupClass);
        args.add(arg);
        AdsTypeDeclaration superRef = groupClass.getInheritance().getSuperClassRef();
        groupClass.getInheritance().setSuperClassRef(superRef.toGenericType(args));
    }
    protected EClassType classType;
    protected boolean isUserReport;
    
    protected AdsClassCreature(RadixObjects container, EClassType classType, boolean userReport) {
        super(container, EDefType.CLASS);
        this.classType = classType;
        this.isUserReport = userReport;
    }

    protected AdsClassCreature(AdsModule module, EClassType classType, boolean userReport) {
        super(module, EDefType.CLASS);
        this.classType = classType;
        this.isUserReport = userReport;
    }

    protected AdsClassCreature(AdsDefinitions<? extends AdsDefinition> definitions, EClassType classType) {
        super(definitions, EDefType.CLASS, null);
        this.classType = classType;
    }

    @Override
    boolean requiredEnvironment() {
        return classType == EClassType.DYNAMIC || classType == EClassType.INTERFACE
                || classType == EClassType.EXCEPTION || classType == EClassType.ENUMERATION;
    }
    private AdsModuleDefinitionWizardStep1Panel panel;

    @Override
    void setupLinkedDefinition(AdsModuleDefinitionWizardStep1Panel panel) {
        if (classType == EClassType.REPORT && isUserReport) {
            panel.setLinkedDefinition("Report context ", ChooseDefinitionCfg.Factory.newInstance(module, AdsVisitorProviders.newEntityTypeProvider()), null, null);
            this.panel = panel;
        }
    }

    @Override
    public RadixIcon getIcon() {
        switch (classType) {
            case ALGORITHM:
                return AdsDefinitionIcon.CLASS_ALGORITHM;
            case FORM_HANDLER:
                return AdsDefinitionIcon.CLASS_FORM_HANDLER;
            case DYNAMIC:
                return AdsDefinitionIcon.CLASS_DYNAMIC;
            case ENUMERATION:
                return AdsDefinitionIcon.CLASS_ENUM;
            case EXCEPTION:
                return AdsDefinitionIcon.CLASS_EXCEPTION;
            case INTERFACE:
                return AdsDefinitionIcon.CLASS_INTERFACE;
            case ENTITY:
                return AdsDefinitionIcon.CLASS_ENTITY;
            case ENTITY_GROUP:
                return AdsDefinitionIcon.CLASS_ENTITY_GROUP;
            case PRESENTATION_ENTITY_ADAPTER:
                return AdsDefinitionIcon.CLASS_ENTITY_ADAPTER;
            case APPLICATION:
                return AdsDefinitionIcon.CLASS_APPLICATION;
            case SQL_CURSOR:
                return AdsDefinitionIcon.CLASS_CURSOR;
            case SQL_PROCEDURE:
                return AdsDefinitionIcon.CLASS_PROCEDURE;
            case SQL_STATEMENT:
                return AdsDefinitionIcon.CLASS_STATEMENT;
            case REPORT:
                return AdsDefinitionIcon.CLASS_REPORT;
            case COMMAND_MODEL:
                return AdsDefinitionIcon.COMMAND;
            default:
                return null;
        }
    }

    @Override
    public AdsClassDef createInstance() {
        switch (classType) {
            case ALGORITHM:
                return AdsAlgoClassDef.Factory.newInstance();
            case FORM_HANDLER:
                return AdsFormHandlerClassDef.Factory.newInstance();
            case DYNAMIC:
                return AdsDynamicClassDef.Factory.newInstance(targetEnv);
            case ENUMERATION:
                return AdsEnumClassDef.Factory.newInstance(targetEnv);
            case EXCEPTION:
                return AdsExceptionClassDef.Factory.newInstance(targetEnv);
            case INTERFACE:
                return AdsInterfaceClassDef.Factory.newInstance(targetEnv);
            case SQL_CURSOR:
                return AdsCursorClassDef.Factory.newInstance();
            case SQL_PROCEDURE:
                return AdsProcedureClassDef.Factory.newInstance();
            case SQL_STATEMENT:
                return AdsStatementClassDef.Factory.newInstance();
            case REPORT:
                if (isUserReport) {
                    return AdsUserReportClassDef.Factory.newUserInstance(panel == null ? null : (AdsEntityClassDef) panel.getLinkedDefinition());
                } else {
                    return AdsReportClassDef.Factory.newInstance();
                }
            default:
                return null;
        }
    }

    @Override
    public String getDisplayName() {
        switch (classType) {
            case ALGORITHM:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-AlgoClass");
            case FORM_HANDLER:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-FormClass");
            case DYNAMIC:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-DynamicClass");
            case ENUMERATION:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-EnumClass");
            case APPLICATION:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-ApplicationClass");
            case ENTITY:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-EntityClass");
            case PRESENTATION_ENTITY_ADAPTER:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-PresentationEntityAdapterClass");
            case ENTITY_GROUP:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-EntityGroupClass");
            case EXCEPTION:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-ExceptionClass");
            case INTERFACE:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-InterfaceClass");
            case SQL_CURSOR:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-CursorClass");
            case SQL_PROCEDURE:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-ProcedureClass");
            case SQL_STATEMENT:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-StatementClass");
            case REPORT:
                return NbBundle.getMessage(AdsClassCreature.class, "Type-Display-Name-ReportClass");
            case COMMAND_MODEL:
                return "Command Model Class";
            default:
                return null;
        }
    }

    @Override
    public void afterAppend(AdsDefinition object) {
        super.afterAppend(object);
        if (object.getHierarchy().findOverwritten().get() == null) {
            switch (classType) {
                case ENTITY:
                    AdsClassLookupSupport.setupEntityClass((AdsEntityClassDef) object);
                    break;
                case ENTITY_GROUP:
                    AdsClassLookupSupport.setupEntityGroupClass((AdsEntityGroupClassDef) object);
                    break;
                case SQL_CURSOR:
                    AdsClassLookupSupport.setupCursorClass((AdsCursorClassDef) object);
                    break;
                case REPORT:
                    AdsClassLookupSupport.setupReportClass((AdsReportClassDef) object);
                    break;
                case ALGORITHM:
                    AdsClassLookupSupport.setupAlgoClass((AdsAlgoClassDef) object);
                    break;
            }
        }
    }
}
