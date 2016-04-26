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

package org.radixware.kernel.designer.ads.editors.module;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.MetaInfServicesCatalog;
import org.radixware.kernel.common.defs.ads.type.interfacing.RadixClassInterfacingVisitiorProvider;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.selector.DefinitionSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionEvent;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionListener;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;


final class CreateServiceSteps extends WizardSteps {

    private AdsModule module;
    private ERuntimeEnvironmentType environment;
    private AdsPath serviceIdPath = null;

    public CreateServiceSteps(AdsModule module, ERuntimeEnvironmentType environment, AdsPath serviceIdPath) {
        this.module = module;
        this.environment = environment;
        this.serviceIdPath = serviceIdPath;
    }

    public CreateServiceSteps(AdsModule module, ERuntimeEnvironmentType environment) {
        this(module, environment, null);
    }

    private static final class ServiceSetting {

        final AdsModule module;
        final ERuntimeEnvironmentType environment;
        AdsPath interfaceIdPath;
        AdsPath implementationIdPath;
        private AdsClassDef interfaceDef = null;

        public ServiceSetting(AdsModule module, ERuntimeEnvironmentType environment) {
            this.module = module;
            this.environment = environment;
        }

        public ServiceSetting(AdsModule module, ERuntimeEnvironmentType environment, AdsPath interfaceName) {
            this.module = module;
            this.interfaceIdPath = interfaceName;
            this.environment = environment;
        }

        private AdsClassDef getInterface() {
            if (interfaceDef == null) {
                interfaceDef = (AdsClassDef) interfaceIdPath.resolve(module).get();
            }

            return interfaceDef;
        }
    }

    private static abstract class SelectDefinitionStep extends Step<JPanel> implements SelectionListener<Definition> {

        private static final class ServicesDefinitionSelector extends DefinitionSelector<ChooseDefinitionCfg> {

            @Override
            protected void updateList() {
                definitionsPanel.open(getContext());
            }
        };

        private final ServicesDefinitionSelector definitionSelector = new ServicesDefinitionSelector();

        Definition selected;

        SelectDefinitionStep() {
             definitionSelector.addSelectionListener(this);
        }

        @Override
        protected JPanel createVisualPanel() {
            return definitionSelector.getComponent();
        }

        @Override
        public void open(Object settings) {
            super.open(settings);

            final ServiceSetting serviceSetting = (ServiceSetting) settings;

            definitionSelector.open(getChooseDefinitionCfg(serviceSetting), (Definition) null);
        }

        @Override
        public void selectionChanged(SelectionEvent<Definition> event) {
            selected = event.newValue;
            fireChange();
        }

        protected abstract ChooseDefinitionCfg getChooseDefinitionCfg(ServiceSetting serviceSetting);
    }

    private static final class SelectInterfaceStep extends SelectDefinitionStep {

        @Override
        public String getDisplayName() {
            return "Choose Service Interface";
        }

        @Override
        protected ChooseDefinitionCfg getChooseDefinitionCfg(final ServiceSetting serviceSetting) {

            return ChooseDefinitionCfg.Factory.newInstance(
                    serviceSetting.module.getBranch(),
                    new MetaInfServicesCatalog.ServiceInterfaceVisitorProvider(serviceSetting.environment));
        }

        @Override
        public Step createNextStep() {
            return new SelectImplementationStep();
        }

        @Override
        public boolean isFinishiable() {
            return false;
        }

        @Override
        public boolean hasNextStep() {
            return true;
        }

        @Override
        public void apply(Object settings) {
            final ServiceSetting serviceSetting = (ServiceSetting) settings;

            if (selected != null) {
                serviceSetting.interfaceIdPath = new AdsPath(selected);
            }
        }

        @Override
        public boolean isComplete() {
            return selected != null;
        }
    }

    private static final class SelectImplementationStep extends SelectDefinitionStep {

        @Override
        public String getDisplayName() {
            return "Choose Service Implementation";
        }

        @Override
        protected ChooseDefinitionCfg getChooseDefinitionCfg(ServiceSetting serviceSetting) {
            final AdsClassDef serviceInterface = serviceSetting.getInterface();

            final List<Definition> definitions = new LinkedList<>();

            serviceSetting.module.visit(new IVisitor() {

                @Override
                public void accept(RadixObject radixObject) {
                    definitions.add((Definition) radixObject);
                }
            }, new RadixClassInterfacingVisitiorProvider(
                    new MetaInfServicesCatalog.ServiceImplementationVisitorProvider(
                    serviceSetting.environment), serviceInterface));

            return ChooseDefinitionCfg.Factory.newInstance(definitions);
        }

        @Override
        public boolean isFinishiable() {
            return true;
        }

        @Override
        public boolean isComplete() {
            return selected != null;
        }

        @Override
        public void apply(Object settings) {
            final ServiceSetting serviceSetting = (ServiceSetting) settings;

            if (selected != null) {
                serviceSetting.implementationIdPath = new AdsPath(selected);
            }
        }
    }

    @Override
    public Step createInitial() {
        if (serviceIdPath == null) {
            return new SelectInterfaceStep();
        } else {
            return new SelectImplementationStep();
        }
    }

    @Override
    public ServiceSetting createSettings() {
        if (serviceIdPath == null) {
            return new ServiceSetting(module, environment);
        } else {
            return new ServiceSetting(module, environment, serviceIdPath);
        }
    }

    @Override
    public String getDisplayName() {
        return "Create Exported Service";
    }

    @Override
    protected void finished() {
        final ServiceSetting setting = (ServiceSetting) getSettings();

        setting.module.getServicesCatalog().addService(setting.interfaceIdPath, setting.implementationIdPath);
    }
}
