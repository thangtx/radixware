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

package org.radixware.kernel.designer.ads.editors.role;

import java.util.ArrayList;
import java.util.Collection;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.EChooseDefinitionDisplayMode;
import org.radixware.kernel.designer.common.dialogs.chooseobject.IChooseDefinitionAdditionTextProvider;


public class ChooseKnownDefinitionCfg extends ChooseDefinitionCfg {

    private final Collection<Definition> list;
    private AdditionTextProvider additionTextProvider;

    protected ChooseKnownDefinitionCfg(RadixObject context, VisitorProvider provider, Collection<Definition> list) {
        super(context, provider);
        this.list = list;
    }

    @Override
    public Collection<Definition> collectAllowedDefinitions() {
        return list;
    }

    @Override
    public IChooseDefinitionAdditionTextProvider getAdditionTextProvider() {
        if (additionTextProvider == null) {
            additionTextProvider = new AdditionTextProvider(list);
        }
        return additionTextProvider;
        //super.getAdditionTextProvider(); //To change body of generated methods, choose Tools | Templates.
        //list

    }

    private static class AdditionTextProvider implements IChooseDefinitionAdditionTextProvider {

        private final Collection<Definition> list;

        AdditionTextProvider(final Collection<Definition> list) {
            this.list = list;
        }

        @Override
        public Collection<? extends RadixObject> filter(Collection<? extends RadixObject> currCollection, String text, boolean caseSensitive, EChooseDefinitionDisplayMode displayMode) {
            if (text != null && currCollection.isEmpty() && Id.DEFAULT_ID_AS_STR_LENGTH == text.trim().length()) {
                text = text.trim();
                Id id = Id.Factory.loadFrom(text);
                Collection<RadixObject> collection = new ArrayList();

                final boolean isEditor, isSelector;
                isEditor = EDefinitionIdPrefix.EDITOR_PRESENTATION.equals(id.getPrefix());
                isSelector = EDefinitionIdPrefix.SELECTOR_PRESENTATION.equals(id.getPrefix());

                for (final Definition def : list) {
                    if (def.getId() == id) {
                        collection.add(def);
                    } else if (isEditor || isSelector) {
                        if (def instanceof AdsEntityObjectClassDef) {
                            AdsEntityObjectClassDef cl = (AdsEntityObjectClassDef) def;
                            if (isEditor) {
                                if (!cl.getPresentations().getEditorPresentations().findById(id, ExtendableDefinitions.EScope.LOCAL).isEmpty()) {
                                    collection.add(def);
                                }
                            } else if (isSelector) {
                                if (!cl.getPresentations().getSelectorPresentations().findById(id, ExtendableDefinitions.EScope.LOCAL).isEmpty()) {
                                    collection.add(def);
                                }
                            }
                        }


                    }
                }
                return collection;
            }
            return currCollection;
        }
    }
}
