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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.ListModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.interfacing.JavaClassesInterfacing;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.EFilterPosition;
import org.radixware.kernel.designer.common.dialogs.components.selector.IItemFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskState;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


final class ChooseJavaClassStep extends ChooseTypeFromListStep<JavaClassSelector, RadixPlatformClassListItem> {

    private Definition definition;

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseJavaClassStep.class, "TypeWizard-ChooseJavaClassStep-DisplayName");
    }

    @Override
    protected JavaClassSelector createEditor() {
        final JavaClassSelector platformClassPanel = new JavaClassSelector();
        platformClassPanel.addSelectionListener(this);
        platformClassPanel.addActionListener(createSelectActionListener());
        return platformClassPanel;
    }

    @Override
    void open(TypeWizard.Settings settings) {
        definition = settings.filter.getContext();
        if (settings.wizardModel.isRefine()) {
            getEditor().enableRifine();

            final AdsTypeDeclaration baseType = settings.filter.getBaseType();
            if (baseType != null) {
                interfacing = new JavaClassesInterfacing(baseType.getExtStr(), definition.getLayer(), getEnvironment());
            }
        }
        getEditor().open(settings.filter, currentValue);
    }
    private JavaClassesInterfacing interfacing;
    private RadixPlatformClass platformClass;

    @Override
    public void selectionChanged(RadixPlatformClassListItem newValue) {

        if (newValue != null) {
            if (getSettings().wizardModel.isRefine() && !isDescendant()) {
                state.error(NbBundle.getMessage(ChooseJavaClassStep.class, "TypeEditErrors-IncompatibleClass"));
                isComplete = false;
                hasNextStep = false;
                isFinishable = false;
            } else {
                platformClass = getPlatformLib().findPlatformClass(newValue.getFullName());
                if (platformClass != null) {

                    isComplete = true;
                    state.ok();

                    boolean isGeneric = platformClass.hasGenericArguments();
                    hasNextStep = isGeneric;
                    isFinishable = !isGeneric;
                } else {
                    isComplete = false;
                    state.error(NbBundle.getMessage(ChooseJavaClassStep.class, "TypeEditErrors-ClassLoad") + newValue);
                }
            }
        } else {
            state.ok();
            isComplete = false;
        }
    }

    @Override
    void apply(Settings settings) {
        if (platformClass != null) {
            typeModel.setType(platformClass.getDeclaration());
            typeModel.setTypeArguments(typeModel.getType().getGenericArguments());
        }
    }

    @Override
    public Step createNextStep() {
        return new SpecifyGenericArgumentsStep();
    }

    @Override
    public void cancel(Object settings) {
        getEditor().cancel();
    }

    private PlatformLib getPlatformLib() {
        ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
        if (definition instanceof IEnvDependent) {
            env = ((IEnvDependent) definition).getUsageEnvironment();
        }
        return ((AdsSegment) definition.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(env);
    }

    private ERuntimeEnvironmentType getEnvironment() {
        ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
        if (definition instanceof IEnvDependent) {
            env = ((IEnvDependent) definition).getUsageEnvironment();
        }
        return env;
    }

    private boolean isDescendant() {
        if (interfacing != null) {
            return interfacing.isSuperFor(currentValue.getFullName());
        }
        return false;
    }
}

final class JavaClassSelector extends TypeListSelector<RadixPlatformClassListItem, ITypeFilter> {

//    {
//        PlatformLib.enableClassCaching(true);
//    }
    private RadixPlatformClassListModel model;

    public JavaClassSelector() {
        super(new ItemNameFilter<RadixPlatformClassListItem>());
        setListItemRender(new RadixPlatformClassRenderer(itemList));
    }
    final Object modelLock = new Object();
    private boolean enableRifine;

    public void enableRifine() {
        enableRifine = true;
    }
    private IItemFilter filter;

    private void setFilter(IItemFilter filter) {
        if (this.filter != null) {
            getSelectorLayout().removeFilterComponent(this.filter);
        }
        getSelectorLayout().addFilterComponent(EFilterPosition.BOTTOM, filter);
        this.filter = filter;
    }

    @Override
    protected AssemblyModelTask getAssemblyModelTask() {
        return new AssemblyModelTask() {
            @Override
            protected ListModel assemblyModel() {
                synchronized (modelLock) {
                    if (model == null) {
                        final AdsSegment segment = (AdsSegment) getContext().getContext().getModule().getSegment();
                        model = new RadixPlatformClassListModel(getClasses(segment));
                    }
                }

                final Set<RadixPlatformClassListItem> items = new HashSet<>();

                for (final RadixPlatformClassListItem item : model.getItems()) {
                    if (getState().get() == TaskState.CANCELLED || getState().get() == TaskState.RESTARTED) {
                        return null;
                    }
                    if (accept(item)) {
                        items.add(item);
                    }
                }
                return new RadixPlatformClassListModel(items);
            }
        };
    }

    @Override
    protected void openImpl(Collection<RadixPlatformClassListItem> items) {
        if (enableRifine) {
            final ITypeFilter typeFilter = getContext();
            if (typeFilter != null) {
                final AdsTypeDeclaration baseType = typeFilter.getBaseType();
                if (baseType != null) {
                    setFilter(new JavaClassInterfacingFilter(baseType.getExtStr(), getContext().getContext().getLayer(), getEnvironment()));
                }
            }
        }
        super.openImpl(items);
    }

    private ERuntimeEnvironmentType getEnvironment() {
        ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
        if (getContext().getContext() instanceof IEnvDependent) {
            env = ((IEnvDependent) getContext().getContext()).getUsageEnvironment();
        }
        return env;
    }

    private PlatformLib getKernelLib() {
        PlatformLib kernelLib = null;
        Definition context = getContext().getContext();
        if (context != null) {
            ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
            if (context instanceof IEnvDependent) {
                env = ((IEnvDependent) context).getUsageEnvironment();
            }
            kernelLib = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(env);
        }
        return kernelLib;
    }

    private List<String> getClasses(AdsSegment segment) {
        final Set<String> result = new HashSet<>();
        final ITypeFilter typeFilter = getContext();
        final Definition definition = typeFilter.getContext();

        if (segment != null) {
            ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
            if (definition instanceof IEnvDependent) {
                env = ((IEnvDependent) definition).getUsageEnvironment();
            }
            result.addAll(LookupUtils.collectLibraryClasses(definition.getLayer(), env));
        }

        return new LinkedList<>(result);
    }

    private static class JavaClassInterfacingFilter extends ItemFilter<RadixPlatformClassListItem> {

        private final JavaClassesInterfacing classesInterfacing;
        private final JCheckBox checkBox = new JCheckBox(NbBundle.getMessage(JavaClassInterfacingFilter.class, "JavaClassInterfacingFilter"));

        public JavaClassInterfacingFilter(String baseType, Layer layer, ERuntimeEnvironmentType env) {
            classesInterfacing = new JavaClassesInterfacing(baseType, layer, env);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    fireChange();
                }
            });
        }

        @Override
        public boolean accept(RadixPlatformClassListItem value) {
            return !checkBox.isSelected() || classesInterfacing.isSuperFor(value.getFullName());
        }

        @Override
        public Component getComponent() {
            return checkBox;
        }
    }
}
