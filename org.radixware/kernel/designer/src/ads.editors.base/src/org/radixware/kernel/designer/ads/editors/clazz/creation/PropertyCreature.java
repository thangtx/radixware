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
package org.radixware.kernel.designer.ads.editors.clazz.creation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ObjectPropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.ObjectType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.enums.EClassType;
import static org.radixware.kernel.common.enums.EClassType.DIALOG_MODEL;
import static org.radixware.kernel.common.enums.EClassType.FILTER_MODEL;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropNature;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.agents.DefaultAgent;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.common.utils.agents.WrapAgent;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.TransparentPropertyCreature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;

/**
 * Creature for just typed properties: <ul> <li>Dynamic</li> <li>Expression</li>
 * <li>Presentation</li> <li>Form</li> </ul> Creates temporary instance of
 * preperty end then set it up
 *
 */
public class PropertyCreature extends Creature<AdsPropertyDef> {

    public static final class Factory {

        public static List<ICreature> createInstances(IObjectAgent<AdsPropertyGroup> agent) {
            return createInstances(agent, null);
        }

        private static void processNature(EClassType classType, IObjectAgent<AdsPropertyGroup> agent, EPropNature nature, List<ICreature> result, Set<EPropNature> requiredNature) {
            if (requiredNature == null || requiredNature.contains(nature)) {
                if (nature == EPropNature.EVENT_CODE && BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null) {
                    return;
                }
                if (nature == EPropNature.PROPERTY_PRESENTATION) {
                    switch (classType) {
                        case DIALOG_MODEL:
                        case PROP_EDITOR_MODEL:
                        case PARAGRAPH_MODEL:
                        case CUSTOM_WDGET_MODEL:
                            result.add(new PropertyCreature(agent, nature, true));
                            break;
                        default:
                            result.add(new PropertyCreature(agent, nature));
                            result.add(new PropertyCreature(agent, nature, true));
                            break;
                    }
                } else {
                    result.add(new PropertyCreature(agent, nature));
                }
            }
        }

        public static List<ICreature> createInstances(AdsPropertyGroup group, Set<EPropNature> requiredNature) {
            return createInstances(new DefaultAgent<>(group), requiredNature);
        }

        public static List<ICreature> createInstances(IObjectAgent<AdsPropertyGroup> agent, Set<EPropNature> requiredNature) {
            final List<ICreature> result = new ArrayList<>();
            final AdsClassDef ownerClass = agent.getObject().getOwnerClass();
            final EClassType classType = ownerClass.getClassDefType();

            if (AdsTransparence.isTransparent(ownerClass) && !(ownerClass instanceof AdsStatementClassDef) && !(ownerClass instanceof AdsProcedureClassDef)) {
                result.add(new TransparentPropertyCreature(ownerClass, agent.getObject(), ownerClass.getProperties().getLocal()));

                if (!ownerClass.getTransparence().isExtendable()) {
                    return result;
                }
            }

            if (classType != EClassType.INTERFACE && !ownerClass.isAnonymous()) {
                switch (agent.getObject().getUsageEnvironment()) {
                    case SERVER:
                        if (classType != EClassType.SQL_CURSOR && classType != EClassType.SQL_PROCEDURE && classType != EClassType.SQL_STATEMENT) {
                            processNature(classType, agent, EPropNature.DYNAMIC, result, requiredNature);
                            processNature(classType, agent, EPropNature.EVENT_CODE, result, requiredNature);
                        }
                        switch (classType) {
                            case SQL_CURSOR:
                            case REPORT: {
                                processNature(classType, agent, EPropNature.FIELD, result, requiredNature);
                                processNature(classType, agent, EPropNature.FIELD_REF, result, requiredNature);
                                processNature(classType, agent, EPropNature.SQL_CLASS_PARAMETER, result, requiredNature);
                                final AdsSqlClassDef clazz = (AdsSqlClassDef) ownerClass;
                                if (clazz.isParentPropsAllowed()) {
                                    processNature(classType, agent, EPropNature.PARENT_PROP, result, requiredNature);
                                }
                            }
                            break;
                            case SQL_PROCEDURE:
                            case SQL_STATEMENT:
                                processNature(classType, agent, EPropNature.SQL_CLASS_PARAMETER, result, requiredNature);
                                break;
                            case ENTITY:
                            case APPLICATION:
                                final AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) ownerClass;
                                processNature(classType, agent, EPropNature.INNATE, result, requiredNature);
                                if (clazz.isParentPropsAllowed()) {
                                    processNature(classType, agent, EPropNature.PARENT_PROP, result, requiredNature);
                                }
                                if (clazz.isDetailPropsAllowed()) {
                                    processNature(classType, agent, EPropNature.DETAIL_PROP, result, requiredNature);
                                }
                                if (clazz.isUserPropsAllowed()) {
                                    processNature(classType, agent, EPropNature.USER, result, requiredNature);
                                }
                                break;
//                            case FORM_HANDLER:
//                                processNature(group, EPropNature.FORM_PROPERTY, result, requiredNature);
//                                break;
                            case ENTITY_GROUP:
                                processNature(classType, agent, EPropNature.GROUP_PROPERTY, result, requiredNature);
                                break;
                            case FORM_HANDLER:
                                processNature(classType, agent, EPropNature.PARENT_PROP, result, requiredNature);
                                break;
                        }
                        if (ownerClass instanceof AdsEntityObjectClassDef) {
                            processNature(classType, agent, EPropNature.EXPRESSION, result, requiredNature);
                        }
                        break;
                    case COMMON:
                        processNature(classType, agent, EPropNature.DYNAMIC, result, requiredNature);
                        processNature(classType, agent, EPropNature.EVENT_CODE, result, requiredNature);
                        break;

                    case EXPLORER:
                    case WEB:
                    case COMMON_CLIENT:
                        result.add(new PropertyCreature(agent, EPropNature.DYNAMIC));
                        result.add(new PropertyCreature(agent, EPropNature.EVENT_CODE));
                        switch (ownerClass.getClassDefType()) {
                            case ENTITY_MODEL:
                            case GROUP_MODEL:
                            case FORM_MODEL:
                            case REPORT_MODEL:
                            case FILTER_MODEL:
                            case DIALOG_MODEL:
                            case PROP_EDITOR_MODEL:
                            case PARAGRAPH_MODEL:
                            case CUSTOM_WDGET_MODEL:
                                processNature(classType, agent, EPropNature.PROPERTY_PRESENTATION, result, requiredNature);
                                break;
                            default:
                                break;
                        }
                        break;
                }
            } else {
                result.add(new PropertyCreature(agent, EPropNature.DYNAMIC));
            }
            return result;
        }
    }
    private AdsPropertyDef temporary;
    protected final EPropNature propNature;
    private boolean localPresentation;
    final IObjectAgent<AdsPropertyGroup> agent;

    private PropertyCreature(IObjectAgent<AdsPropertyGroup> agent, EPropNature nature) {
        this(agent, nature, false);
    }

    private PropertyCreature(IObjectAgent<AdsPropertyGroup> agent, EPropNature nature, boolean localPresentation) {
        super(new WrapAgent<RadixObjects, AdsPropertyGroup>(agent) {
            @Override
            public RadixObjects getObject() {
                return getObjectSource().getOwnerClass().getProperties().getLocal();
            }
        });
        this.localPresentation = localPresentation;
        this.propNature = nature;
        this.agent = agent;
    }

    final AdsPropertyGroup getGroup() {
        return agent.getObject();
    }

    @Override
    public String getDisplayName() {
        switch (propNature) {
            case FIELD:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-FieldProperty");
            case SQL_CLASS_PARAMETER:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-ParameterProperty");
            case FIELD_REF:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-FieldRefProperty");
            case DYNAMIC:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-DynamicProperty");
            case DETAIL_PROP:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-DetailProperty");
            case EXPRESSION:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-ExpressionProperty");
            case EVENT_CODE:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-EventCodeProperty");
//            case FORM_PROPERTY:
//                return NbBundle.getMessage(getClass(), "Type-Display-Name-FormProperty");
            case GROUP_PROPERTY:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-GroupProperty");
            case INNATE:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-InnateProperty");
            case PARENT_PROP:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-ParentProperty");
            case PROPERTY_PRESENTATION:
                if (localPresentation) {
                    return NbBundle.getMessage(getClass(), "Type-Display-Name-LocalPropertyPresentation");
                } else {
                    return NbBundle.getMessage(getClass(), "Type-Display-Name-PropertyPresentation");
                }
            case USER:
                return NbBundle.getMessage(getClass(), "Type-Display-Name-UserProperty");
            default:
                return null;
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Property.PROPERTY_DYNAMIC;
    }

    @Override
    public AdsPropertyDef createInstance() {
        final AdsPropertyDef instance = getTemporaryInstance().getClipboardSupport().copy();
//        if (temporary instanceof IAdsPresentableProperty) {
//            ServerPresentationSupport support = ((IAdsPresentableProperty) temporary).getPresentationSupport();
//            if (support != null) {
//                PropertyPresentation tmp = support.getPresentation();
//                if (tmp != null) {
//                    if (instance instanceof IAdsPresentableProperty) {
//                        PropertyPresentation res = ((IAdsPresentableProperty) instance).getPresentationSupport().getPresentation();
//                        if (res != null) {
//                            res.setTitleInherited(tmp.isMayInheritTitle());
//                            res.setEditOptionsInherited(tmp.isMayInheritEditOptions());
//                        }
//                    }
//                }
//            }
//        }
        return instance;
    }

    String getPropertyName() {
        return getTemporaryInstance().getName();
    }

    void setPropertyName(String name) {
        getTemporaryInstance().setName(name);
    }

    private AdsPropertyDef createTemporaryInstance() {
        final AdsPropertyGroup group = getGroup();
        switch (propNature) {
            case FIELD:
                return AdsFieldPropertyDef.Factory.newTemporaryInstance(group);
            case SQL_CLASS_PARAMETER:
                return AdsParameterPropertyDef.Factory.newTemporaryInstance(group);
            case FIELD_REF:
                return AdsFieldRefPropertyDef.Factory.newTemporaryInstance(group);
            case DYNAMIC:
                return AdsDynamicPropertyDef.Factory.newTemporaryInstance(group);
            case USER:
                return AdsUserPropertyDef.Factory.newTemporaryInstance(group);
            case EXPRESSION:
                return AdsExpressionPropertyDef.Factory.newTemporaryInstance(group);
            case EVENT_CODE:
                return AdsEventCodePropertyDef.Factory.newTemporaryInstance(group);
//            case FORM_PROPERTY:
//                temporary = AdsFormPropertyDef.Factory.newTemporaryInstance(group);
//                break;
            case GROUP_PROPERTY:
                return AdsGroupPropertyDef.Factory.newTemporaryInstance(group);
            case PARENT_PROP:
                return AdsParentPropertyDef.Factory.newTemporaryInstance(group);
            case INNATE:
                return AdsInnateColumnPropertyDef.Factory.newTemporaryInstance(group);
            case DETAIL_PROP:
                return AdsDetailColumnPropertyDef.Factory.newTemporaryInstance(group);
            case PROPERTY_PRESENTATION:
                return AdsPropertyPresentationPropertyDef.Factory.newTemporaryInstance(group, localPresentation);
            default:
                throw new IllegalStateException();
        }
    }

    public AdsPropertyDef getTemporaryInstance() {
        if (temporary == null) {
            temporary = createTemporaryInstance();
        }
        return temporary;
    }

    @Override
    public boolean afterCreate(AdsPropertyDef object) {
        return true;
    }

    @Override
    public void afterAppend(AdsPropertyDef object) {

        if (object instanceof IAdsPresentableProperty) {
            ServerPresentationSupport support = ((IAdsPresentableProperty) object).getPresentationSupport();
            if (support != null) {
                PropertyPresentation presintation = support.getPresentation();
                if (presintation != null) {

                    // 
                    presintation.setTitleInherited(presintation.isMayInheritTitle());
                    presintation.setEditOptionsInherited(presintation.isMayInheritEditOptions());

                    //
                    if (object instanceof ColumnProperty && ((ColumnProperty) object).getColumnInfo() != null) {
                        DdsColumnDef column = ((ColumnProperty) object).getColumnInfo().findColumn();
                        if (column != null && column.isPrimaryKey()) {
                            if (presintation.isPresentable() && !presintation.isEditOptionsInherited()) {
                                presintation.getEditOptions().setEditPossibility(EEditPossibility.ON_CREATE);
                            }
                        }
                    }

                    // change init property if create in report (RADIX-14064)
                    AdsClassDef ownerClassDef = object.getOwnerClass();
                    if (ownerClassDef instanceof AdsReportClassDef) {
                        presintation.setPresentable(false);
                        if (object.getValue().getType().getTypeId() == EValType.ARR_REF) {
                            presintation.getEditOptions().setDuplicatesEnabled(false);
                        }
                    }

                }
            }
        }

        if (object instanceof AdsEventCodePropertyDef) {
            AdsLocalizingBundleDef bunfle = object.findLocalizingBundle();
            if (bunfle != null) {
                AdsEventCodeDef ec = AdsEventCodeDef.Factory.newEventCodeInstance();
                bunfle.getStrings().getLocal().add(ec);
                ((AdsEventCodePropertyDef) object).setEventId(ec.getId());
            }
        }

        AdsPropertyDef temporaryInstance = getTemporaryInstance();
        if (EnvSelectorPanel.isMeaningFullFor(temporaryInstance)) {
            object.setUsageEnvironment(temporaryInstance.getUsageEnvironment());
        }

        if (object.getNature() == EPropNature.USER && object instanceof IAdsPresentableProperty) {
            final AdsTypeDeclaration type = object.getValue().getType();
            if (object.getValue().getType().getTypeId() == EValType.OBJECT) {
                final AdsType adsType = type.resolve(object).get();
                if (adsType instanceof ObjectType) {
                    final AdsEntityObjectClassDef typeClass = ((ObjectType) adsType).getSource();

//                    final IAdsPresentableProperty presentable = (IAdsPresentableProperty) object;
//                    final ObjectPropertyPresentation opp = (ObjectPropertyPresentation) presentable.getPresentationSupport().getPresentation();
//                    final AdsEntityObjectClassDef clazz = opp.getCreatePresentationsList().findTargetClass();
//                    if (clazz != null) {
//                        final List<AdsEditorPresentationDef> editorPresentations = clazz.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL);
//
//                        if (!editorPresentations.isEmpty()) {
//                            opp.getCreatePresentationsList().addPresentationId(editorPresentations.get(0).getId());
//                        }
//                    }
                    final List<AdsEditorPresentationDef> editorPresentations = typeClass.getPresentations().getEditorPresentations().get(ExtendableDefinitions.EScope.ALL);
                    if (!editorPresentations.isEmpty()) {

                        final IAdsPresentableProperty presentable = (IAdsPresentableProperty) object;
                        final ObjectPropertyPresentation opp = (ObjectPropertyPresentation) presentable.getPresentationSupport().getPresentation();
                        opp.getCreatePresentationsList().addPresentationId(editorPresentations.get(0).getId());

                        final PropertyEditOptions editOptions = ((IAdsPresentableProperty) object).getPresentationSupport().getPresentation().getEditOptions();
                        editOptions.setObjectEditorPresentations(Collections.singletonList(editorPresentations.get(0).getId()));
                    }

                }
            }
        }

        getGroup().addMember(object);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && (getGroup() != null ? !getGroup().isReadOnly() : false);//by ygalkina RADIX-1562
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfoImpl();
    }

    private class WizardInfoImpl extends WizardInfo {

        public WizardInfoImpl() {
        }

        @Override
        public CreatureSetupStep createFirstStep() {
            return new PropertySetupStep1();
        }

        @Override
        public boolean hasWizard() {
            return true;
        }
    }

    void updateColumnPropertyKind(boolean createRef) {
        final AdsPropertyDef temp = getTemporaryInstance();
        switch (propNature) {
            case DETAIL_PROP:
                if (createRef) {
                    if (temp instanceof AdsDetailRefPropertyDef) {
                        return;
                    } else {
                        final String name = temp.getName();
                        temporary = AdsDetailRefPropertyDef.Factory.newTemporaryInstance(getGroup());
                        temporary.setName(name);
                    }
                } else {
                    if (temp instanceof AdsDetailColumnPropertyDef) {
                        return;
                    } else {
                        final String name = temp.getName();
                        temporary = AdsDetailColumnPropertyDef.Factory.newTemporaryInstance(getGroup());
                        temporary.setName(name);
                    }
                }
                break;
            case INNATE:
                if (createRef) {
                    if (temp instanceof AdsInnateRefPropertyDef) {
                        return;
                    } else {
                        final String name = temp.getName();
                        temporary = AdsInnateRefPropertyDef.Factory.newTemporaryInstance(getGroup());
                        temporary.setName(name);
                    }
                } else {
                    if (temp instanceof AdsInnateColumnPropertyDef) {
                        return;
                    } else {
                        final String name = temp.getName();
                        temporary = AdsInnateColumnPropertyDef.Factory.newTemporaryInstance(getGroup());
                        temporary.setName(name);
                    }
                }
                break;
        }
    }
}
