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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.FormChoice;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.creation.NestedClassCreature;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;

public class AdsModuleDefinitionCreature<T extends AdsDefinition> extends Creature<T> {

    protected String definitionName;
    protected EDefType defType;
    protected AdsModule module;
    protected ERuntimeEnvironmentType targetEnv = ERuntimeEnvironmentType.COMMON;
    private final ERuntimeEnvironmentType env;

    public AdsModuleDefinitionCreature(AdsDefinitions<? extends AdsDefinition> definitions, EDefType defType, ERuntimeEnvironmentType env) {
        super(definitions);
        this.module = definitions.getModule();
        this.defType = defType;
        this.env = env;
    }

    public AdsModuleDefinitionCreature(AdsModule module, EDefType defType) {
        this(module, defType, null);
    }

    public AdsModuleDefinitionCreature(AdsModule module, EDefType defType, ERuntimeEnvironmentType env) {
        super(module.getDefinitions());
        this.module = module;
        this.defType = defType;
        this.env = env;
    }

    void setupLinkedDefinition(AdsModuleDefinitionWizardStep1Panel panel) {
    }

    @Override
    public RadixIcon getIcon() {
        switch (defType) {
            case CONTEXTLESS_COMMAND:
                return AdsDefinitionIcon.CONTEXTLESS_COMMAND;
            case ENUMERATION:
                return RadixObjectIcon.ENUM;
            case XML_SCHEME:
                return AdsDefinitionIcon.XML_SCHEME;
            case MSDL_SCHEME:
                return AdsDefinitionIcon.MSDL_SCHEME;
            case ROLE:
                return AdsDefinitionIcon.ROLE;
            case DATA_SEGMENT:
                return AdsDefinitionIcon.DATA_SEGMENT;
            case PARAGRAPH:
                return AdsDefinitionIcon.PARAGRAPH;
            case DOMAIN:
                return AdsDefinitionIcon.DOMAIN;
            case PHRASE_BOOK:
                return AdsDefinitionIcon.PHRASE_BOOK;
            default:
                return null;
        }
    }

    @Override
    public String getDisplayName() {

        switch (defType) {
            case CONTEXTLESS_COMMAND:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-ContextlessCommand");
            case ENUMERATION:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-Enumeration");
            case XML_SCHEME:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-XmlScheme");
            case MSDL_SCHEME:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-MsdlScheme");
            case ROLE:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-Role");
            case DATA_SEGMENT:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-Segment");
            case PARAGRAPH:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-Paragraph");
            case CUSTOM_DIALOG:
                if (env == ERuntimeEnvironmentType.WEB) {
                    return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-CustomDialog") + " for Web";
                } else {
                    return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-CustomDialog") + " for Desktop";
                }
            case CUSTOM_PROP_EDITOR:
                if (env == ERuntimeEnvironmentType.WEB) {
                    return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-CustomPropEditor") + " for Web";
                } else {
                    return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-CustomPropEditor") + " for Desktop";
                }
            case CUSTOM_WIDGET_DEF:
                if (env == ERuntimeEnvironmentType.WEB) {
                    return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-CustomWidget") + " for Web";
                } else {
                    return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-CustomWidget") + " for Desktop";
                }

            case DOMAIN:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-Domain");
            case PHRASE_BOOK:
                return NbBundle.getMessage(AdsModuleDefinitionCreature.class, "Type-Display-Name-PhraseBook");
            default:
                return null;
        }
    }

    @Override
    public String getDescription() {
        return "TODO:";
    }

    @Override
    public T createInstance() {
        switch (defType) {
            case CONTEXTLESS_COMMAND:
                return (T) AdsContextlessCommandDef.Factory.newInstance();
            case ENUMERATION:
                return (T) AdsEnumDef.Factory.newInstance();
            case XML_SCHEME:
                return (T) AdsXmlSchemeDef.Factory.newInstance();
            case MSDL_SCHEME:
                return (T) AdsMsdlSchemeDef.Factory.newInstance();
            case ROLE:
                return (T) AdsRoleDef.Factory.newInstance();
            case PARAGRAPH:
                return (T) AdsParagraphExplorerItemDef.Factory.newInstance();
            case CUSTOM_DIALOG:
                if (env == ERuntimeEnvironmentType.WEB) {
                    return (T) AdsRwtCustomDialogDef.Factory.newInstance();
                } else {
                    return (T) AdsCustomDialogDef.Factory.newInstance();
                }

            case CUSTOM_PROP_EDITOR:
                if (env == ERuntimeEnvironmentType.WEB) {
                    return (T) AdsRwtCustomPropEditorDef.Factory.newInstance();
                } else {
                    return (T) AdsCustomPropEditorDef.Factory.newInstance();
                }
            case CUSTOM_WIDGET_DEF:
                if (env == ERuntimeEnvironmentType.WEB) {
                    return (T) AdsRwtCustomWidgetDef.Factory.newInstance();
                } else {
                    return (T) AdsCustomWidgetDef.Factory.newInstance();
                }
            case DOMAIN:
                return (T) AdsDomainDef.Factory.newInstance();
            case PHRASE_BOOK:
                return (T) AdsPhraseBookDef.Factory.newInstance();
            default:
                return null;
        }
    }

    @Override
    public boolean isEnabled() {
        return !module.isReadOnly();
    }

    @Override
    public boolean afterCreate(AdsDefinition object) {
        object.setName(definitionName);
        if (object instanceof AdsDynamicClassDef) {
            ((AdsDynamicClassDef) object).setUsageEnvironment(targetEnv);
        }
        return true;
    }

    @Override
    public void afterAppend(AdsDefinition object) {
        //Do nothing
        if (object instanceof AdsXmlSchemeDef) {
            AdsXmlSchemeDef xsd = (AdsXmlSchemeDef) object;
            XmlObject xDoc = xsd.getXmlDocument();
            if (xDoc instanceof SchemaDocument) {
                SchemaDocument.Schema xsdObj = ((SchemaDocument) xDoc).getSchema();
                if (xsdObj != null) {
                    xsdObj.setElementFormDefault(FormChoice.QUALIFIED);
                    xsdObj.setAttributeFormDefault(FormChoice.UNQUALIFIED);
                    Layer layer = object.getLayer();
                    if (layer != null) {
                        String uri = layer.getURI();
                        if (uri != null) {
                            String[] parts = uri.split("\\.");
                            StringBuilder maker = new StringBuilder();
                            maker.append("http://schemas");
                            for (int i = parts.length - 1; i >= 0; i--) {
                                maker.append(".").append(parts[i]);
                            }
                            maker.append("/").append(object.getName()).append(".xsd");
                            xsdObj.setTargetNamespace(maker.toString());
                        }
                    }
                }
            }
        }
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {
            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsModuleDefinitionCreatureWizardStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    boolean requiredEnvironment() {
        return false;
    }

    private class AdsModuleDefinitionCreatureWizardStep1 extends CreatureSetupStep<AdsModuleDefinitionCreature, AdsModuleDefinitionWizardStep1Panel> implements ChangeListener {

        private AdsClassDef contextClass;

        @Override
        public String getDisplayName() {
            return "Specify Definition Name";
        }

        @Override
        protected AdsModuleDefinitionWizardStep1Panel createVisualPanel() {

            AdsModuleDefinitionWizardStep1Panel p = new AdsModuleDefinitionWizardStep1Panel(contextClass);
            p.setNameAcceptor(NameAcceptorFactory.newCreateAcceptor(getContainer(), defType));
            p.addChangeListener(this);

            return p;
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public boolean isFinishiable() {
            return true;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            fireChange();
        }

        @Override
        public void open(AdsModuleDefinitionCreature creature) {
            super.open(creature);
            if (creature instanceof NestedClassCreature) {
                NestedClassCreature ncc = (NestedClassCreature) creature;
                contextClass = ncc.getContextClass();
            }
            getVisualPanel().setCurrentName(creature.definitionName);
            getVisualPanel().enableEnvironemnt(requiredEnvironment(), targetEnv);
            creature.setupLinkedDefinition(getVisualPanel());
        }

        @Override
        public void apply(AdsModuleDefinitionCreature creature) {
            super.apply(creature);
            creature.definitionName = getVisualPanel().getCurrentName();
            creature.targetEnv = getVisualPanel().getEnvironemnt();
        }
    }
}
