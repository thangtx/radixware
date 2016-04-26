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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;
import org.radixware.kernel.designer.common.dialogs.components.tasks.TaskState;


final class ChooseJavaTypeStep extends ChooseTypeFromListStep<JavaTypeSelector, String> {

    @Override
    void open(TypeWizard.Settings settings) {
        getEditor().open(settings.filter, currentValue);
    }

    @Override
    public void selectionChanged(String newValue) {

        if (newValue != null && !newValue.isEmpty()) {
            isComplete = true;
            state.ok();
        } else {
            isComplete = false;
            state.error("Type not selected");
        }
    }

    @Override
    void apply(TypeWizard.Settings settings) {
        typeModel.setType(AdsTypeDeclaration.Factory.newPrimitiveType(currentValue));
    }

    @Override
    protected JavaTypeSelector createEditor() {
        final JavaTypeSelector javaTypeSelector = new JavaTypeSelector();
        javaTypeSelector.addSelectionListener(this);
        javaTypeSelector.addActionListener(createSelectActionListener());
        return javaTypeSelector;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseJavaTypeStep.class, "TypeWizard-ChooseJavaTypeStep-DisplayName");
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

final class JavaTypeSelector extends TypeListSelector<String, ITypeFilter> {

    private static final class JavaTypeRender extends RadixTypeCellRender {

        public JavaTypeRender(JList list) {
            super(list);
        }

        @Override
        public RadixIcon getObjectIcon(Object object) {

            String name = object != null ? object.toString() : "";
            final Icon icon;
            if (Integer.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.INT).getIcon();
            } else if (Boolean.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.BOOL).getIcon();
            } else if (Character.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.CHAR).getIcon();
            } else if (Long.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.INT).getIcon();
            } else if (Short.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.INT).getIcon();
            } else if (Byte.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.INT).getIcon();
            } else if (Float.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.INT).getIcon();
            } else if (Double.TYPE.getName().equals(name)) {
                icon = RadixObjectIcon.getForValType(EValType.INT).getIcon();
            } else {
                icon = null;
            }

            return new RadixIcon("") {

                @Override
                public Icon getIcon() {
                    return icon;
                }

            };
        }

    }

    public JavaTypeSelector() {
        super(new ItemNameFilter<String>());

        setListItemRender(new JavaTypeRender(itemList));
    }

    final List<String> allTypes = Arrays.asList("boolean", "byte", "char", "double", "float", "int", "long", "short");
    final List<String> types = new ArrayList<>();

    @Override
    protected AssemblyModelTask getAssemblyModelTask() {
        return new AssemblyModelTask() {

            @Override
            protected ListModel assemblyModel() {
                types.clear();
                for (final String type : allTypes) {
                    if (getState().get() == TaskState.CANCELLED || getState().get() == TaskState.RESTARTED) {
                        return null;
                    }
                    if (accept(type)) {
                        types.add(type);
                    }
                }
                return new SimpleListModel(types);
            }
        };
    }
}
