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

import java.util.HashSet;
import java.util.Set;
import javax.swing.ListModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskState;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


class ChooseRadixXmlSchemeStep extends ChooseTypeFromListStep<XmlSelector, RadixTypeItem> {

    @Override
    void apply(Settings settings) {
        if (currentValue != null) {
            settings.typeModel.setObject(currentValue.getDefinition());
        }
    }

    @Override
    void open(Settings settings) {
        getEditor().open(settings.filter, currentValue);
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseRadixTypeStep.class, "TypeWizard-ChooseRadixXmlSchemeStep-DisplayName");
    }

    @Override
    protected XmlSelector createEditor() {
        final XmlSelector xmlSelector = new XmlSelector();
        xmlSelector.addSelectionListener(this);
        xmlSelector.addActionListener(createSelectActionListener());
        return xmlSelector;
    }

    @Override
    public void selectionChanged(RadixTypeItem newValue) {
        isComplete = newValue != null;
    }

    @Override
    public boolean hasNextStep() {
        return true;
    }

    @Override
    public Step createNextStep() {
        return new ChooseRadixXmlTypeStep();
    }
}

final class XmlSelector extends TypeListSelector<RadixTypeItem, ITypeFilter> {

    public XmlSelector() {
        super(new ItemNameFilter<RadixTypeItem>());
        setListItemRender(new RadixXmlTypeRenderer(itemList));
    }

    @Override
    protected AssemblyModelTask getAssemblyModelTask() {
        return new AssemblyModelTask() {

            @Override
            protected ListModel assemblyModel() {
                final Definition definition = getContext().getContext();
                final RadixTypeItem.TypeListModel listModel = RadixTypeItem.getXmlSchemesModel(definition);

                final Set<RadixTypeItem> items = new HashSet<>();
                for (final RadixTypeItem item : listModel.getItems()) {
                    if (getState().get() == TaskState.CANCELLED || getState().get() == TaskState.RESTARTED) {
                        return null;
                    }
                    if (accept(item)) {
                        items.add(item);
                    }
                }

                return RadixTypeItem.getModelFor(items);
            }
        };
    }
}
