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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.TypeDeclaration;


public final class LastUsedTypeCollection {
    public final static Integer REMEMBER_UNTIL = 20;

    private static final String USED_LIST_NODE = "LastUsedTypesList";
    private final String usedListId;
    private final LinkedList<AdsTypeDeclaration> extendedLastUsed = new LinkedList<>();
    private final LinkedList<AdsTypeDeclaration> lastUsed = new LinkedList<>();

    private LastUsedTypeCollection(String usedListId) {
        this.usedListId = usedListId;
    }
    private static final Map<String, LastUsedTypeCollection> instances = new HashMap<>();

    public static synchronized LastUsedTypeCollection getInstance(String path) {

        LastUsedTypeCollection instance = instances.get(path);
        if (instance == null) {
            instance = new LastUsedTypeCollection(path);
            instances.put(path, instance);
        }
        return instance;
    }

    public synchronized void reloadLastUsed(ITypeFilter filter) {
        try {
            final Preferences usedList = Utils.findPreferences(USED_LIST_NODE);
            if (usedList == null) {
                return;
            }

            final String jmlStr = usedList.get(usedListId, "");
            if (jmlStr == null || jmlStr.isEmpty()) {
                return;
            }
            final JmlType jt = JmlType.Factory.parse(jmlStr);
            if (jt == null) {
                return;
            }

            final List<JmlType.Item> items = jt.getItemList();
            if (items == null || items.isEmpty()) {
                return;
            }

            lastUsed.clear();
            extendedLastUsed.clear();
            for (int i = items.size() - 1; i >= 0; i--) {
                final TypeDeclaration td = items.get(i).getTypeDeclaration();
                if (td == null) {
                    continue;
                }

                final AdsTypeDeclaration typeDeclaration = AdsTypeDeclaration.Factory.loadFrom(td);
                if (typeDeclaration == null) {
                    continue;
                }
                addExtendedLastUsedType(typeDeclaration);

                if (filter != null) {
                    final Definition context = filter.getContext();
                    final ETypeNature typeNature = ETypeNature.getByType(typeDeclaration, context);
                    if (filter.acceptNature(typeNature) && filter.acceptType(typeDeclaration)) {
                        final AdsType resolve = typeDeclaration.resolve(context).get();
                        if (resolve != null) {
                            if (context instanceof IEnvDependent && resolve instanceof AdsDefinitionType) {
                                final Definition source = ((AdsDefinitionType) resolve).getSource();
                                if (source instanceof IEnvDependent) {
                                    if (!ERuntimeEnvironmentType.compatibility(
                                        ((IEnvDependent) context).getUsageEnvironment(),
                                        ((IEnvDependent) source).getUsageEnvironment())) {

                                        continue;
                                    }
                                }
                            }
                            addLastUsedType(typeDeclaration);
                        }
                    }
                } else {
//                    addLastUsedType(typeDeclaration);
                }
            }
        } catch (BackingStoreException | XmlException ex) {
            //...
        }
    }

    private void addLastUsedType(AdsTypeDeclaration ltype) {
        if (!lastUsed.contains(ltype)) {
            if (lastUsed.size() < REMEMBER_UNTIL) {
                lastUsed.add(0, ltype);
            } else if (lastUsed.size() == REMEMBER_UNTIL) {
                lastUsed.remove(lastUsed.size() - 1);
                lastUsed.add(0, ltype);
            }
        }
    }

    private void addExtendedLastUsedType(AdsTypeDeclaration ltype) {
        if (!extendedLastUsed.contains(ltype)) {
            if (extendedLastUsed.size() < REMEMBER_UNTIL) {
                extendedLastUsed.add(0, ltype);
            } else if (extendedLastUsed.size() == REMEMBER_UNTIL) {
                extendedLastUsed.remove(lastUsed.size() - 1);
                extendedLastUsed.add(0, ltype);
            }
        }
    }

    public synchronized void addLastUsed(AdsTypeDeclaration ltype) {
        addLastUsedType(ltype);
        addExtendedLastUsedType(ltype);
    }

    public synchronized LinkedList<AdsTypeDeclaration> getLastUsedTypes(ITypeFilter filter) {
        reloadLastUsed(filter);
        return lastUsed;
    }

    public synchronized List<AdsTypeDeclaration> getLastUsed() {
        return lastUsed;
    }

    public synchronized List<AdsTypeDeclaration> getExtendedLastUsed() {
        return extendedLastUsed;
    }

    public synchronized void saveLastUsed() {
        final Preferences usedList = Utils.findOrCreatePreferences(USED_LIST_NODE);
        if (usedList != null) {
            final JmlType jt = JmlType.Factory.newInstance();
            for (final AdsTypeDeclaration t : getExtendedLastUsed()) {
                TypeDeclaration td = jt.addNewItem().addNewTypeDeclaration();
                t.appendTo(td);
            }

            final XmlOptions opt = new XmlOptions();
            opt.setSaveAggressiveNamespaces();
            final String toSave = jt.xmlText(opt);
            usedList.put(usedListId, toSave);
        }
    }
}
