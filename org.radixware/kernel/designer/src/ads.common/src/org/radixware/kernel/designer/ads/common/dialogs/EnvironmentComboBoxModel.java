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

package org.radixware.kernel.designer.ads.common.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.Utils;


public final class EnvironmentComboBoxModel extends AbstractListModel<String>
        implements ComboBoxModel<String> {

    public static final class Factory {

        private Factory() {
        }

        public static EnvironmentComboBoxModel newInstance() {
            return new EnvironmentComboBoxModel();
        }

        public static EnvironmentComboBoxModel newInstance(AdsClassDef clazz) {
            if (clazz == null) {
                return newInstance();
            }
            if (clazz.isNested() && clazz.getOwnerClass() != null) {
                final AdsClassDef ownerClass = clazz.getOwnerClass();

                if (ownerClass.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return new EnvironmentComboBoxModel(Arrays.asList(
                            ERuntimeEnvironmentType.COMMON_CLIENT,
                            ERuntimeEnvironmentType.EXPLORER,
                            ERuntimeEnvironmentType.WEB));
                } else {
                    return new EnvironmentComboBoxModel(Arrays.asList(ownerClass.getUsageEnvironment()));
                }
            }

            return new EnvironmentComboBoxModel();
        }

        public static EnvironmentComboBoxModel newInstanceWithRestrictingOwner(AdsClassDef ownerClass) {
            if (ownerClass == null) {
                return newInstance();
            }
            if (ownerClass.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return new EnvironmentComboBoxModel(Arrays.asList(
                        ERuntimeEnvironmentType.COMMON_CLIENT,
                        ERuntimeEnvironmentType.EXPLORER,
                        ERuntimeEnvironmentType.WEB));
            } else if (ownerClass.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON) {
                return new EnvironmentComboBoxModel();
            } else {
                return new EnvironmentComboBoxModel(Arrays.asList(
                        ownerClass.getUsageEnvironment()));
            }



        }
    }
    private String selected;
    private final List<ERuntimeEnvironmentType> values;

    private EnvironmentComboBoxModel() {
        values = Arrays.asList(ERuntimeEnvironmentType.values());
    }

    private EnvironmentComboBoxModel(List<ERuntimeEnvironmentType> values) {
        this.values = new ArrayList<>(values != null ? values : Collections.<ERuntimeEnvironmentType>emptyList());
    }

    @Override
    public String getElementAt(int index) {
        return values.get(index).getName();
    }

    @Override
    public int getSize() {
        return values.size();
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public void setSelectedItem(Object anItem) {

        String matchValue;
        if (selected != anItem) {
            if (anItem instanceof ERuntimeEnvironmentType) {
                matchValue = ((ERuntimeEnvironmentType) anItem).getName();
            } else if (anItem != null) {
                matchValue = anItem.toString();
            } else {
                return;
            }
            ERuntimeEnvironmentType match = null;
            for (ERuntimeEnvironmentType env : values) {
                if (Utils.equals(matchValue, env.getName())) {
                    match = env;
                    break;
                }
            }
            if (match == null) {
                if (!values.isEmpty()) {
                    match = values.get(0);
                }
            }
            if (match == null) {
                return;
            }
            selected = match.getName();

            fireContentsChanged(this, -1, -1);
        }
    }
}
