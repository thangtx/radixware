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

package org.radixware.kernel.designer.common.dialogs.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class DictionaryLanguageListModel extends AbstractListModel implements ComboBoxModel {

    final static String commonDict = "<Common>";
    Object selItem;
    List<EIsoLanguage> languages;

    public DictionaryLanguageListModel(Collection<EIsoLanguage> languages) {
        this(languages, null);
    }
    
    public DictionaryLanguageListModel(Collection<EIsoLanguage> languages, EIsoLanguage selected) {
        this.languages = new ArrayList<EIsoLanguage>(languages);
        selItem = selected;
    }
    
    @Override
    public int getSize() {
        return languages.size() + 1;
    }

    @Override
    public Object getElementAt(int index) {
        if (index == 0) {
            return commonDict;
        }
        return languages.get(index - 1);
    }

    @Override
    public void setSelectedItem(Object item) {
        selItem = item;
        fireContentsChanged(this, 0, 0);
    }

    @Override
    public Object getSelectedItem() {
        return selItem != null ? selItem : commonDict;
    }
}
