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
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeWizard.Settings;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskState;



class ChooseRadixXmlTypeStep extends ChooseTypeFromListStep<XmlTypeSelector, RadixTypeItem> {

    @Override
    void open(Settings settings) {
        final Object radixClass = settings.typeModel.getObject();

        if (radixClass instanceof Definition) {
            getEditor().open((Definition) radixClass, currentValue);
        }
    }

    @Override
    void apply(Settings settings) {
        final Object radixClass = settings.typeModel.getObject();
        if (radixClass instanceof IXmlDefinition && currentValue != null) {
            IXmlDefinition xmlDefinition = (IXmlDefinition) radixClass;
            AdsTypeDeclaration result = AdsTypeDeclaration.Factory.newXml(xmlDefinition, currentValue.getXmltype());
            settings.typeModel.setType(result);
        }
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SpecifyTypeStep.class, "TypeWizard-ChooseRadixXmlTypeStep-DisplayName");
    }

    @Override
    public void selectionChanged(RadixTypeItem newValue) {
        isComplete = newValue != null;
    }

    @Override
    protected XmlTypeSelector createEditor() {
        final XmlTypeSelector xmlTypeSelectorPanel = new XmlTypeSelector();
        xmlTypeSelectorPanel.addSelectionListener(this);
        xmlTypeSelectorPanel.addActionListener(createSelectActionListener());
        return xmlTypeSelectorPanel;
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }

}

/**
 * Visual panel for ChooseRadixXmlStep
 *
 */
final class XmlTypeSelector extends TypeListSelector<RadixTypeItem, Definition> {

    public XmlTypeSelector() {
        super(new ItemNameFilter<RadixTypeItem>());
        setListItemRender(new RadixXmlTypeRenderer(itemList));
    }

    @Override
    protected AssemblyModelTask getAssemblyModelTask() {
        return new AssemblyModelTask() {

            @Override
            protected ListModel assemblyModel() {

                final RadixTypeItem.TypeListModel listModel = RadixTypeItem.getXmlSchemeTypesModel(getContext());

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
