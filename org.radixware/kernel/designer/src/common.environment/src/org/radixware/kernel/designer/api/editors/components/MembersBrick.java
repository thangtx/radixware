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
package org.radixware.kernel.designer.api.editors.components;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.api.ApiEditorManager;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.OpenMode;

public class MembersBrick<T extends RadixObject> extends Brick<T> {

    protected static final String MEMBERS = "members";

    public MembersBrick(T source, GridBagConstraints constraints) {
        super(source, constraints, MEMBERS);
    }

    @Override
    protected void beforeBuild(OpenMode mode, final ApiFilter filter) {
        beforeBuild(getSource(), mode, filter);
    }
    
    protected void beforeBuild(final RadixObject provider, OpenMode mode, final ApiFilter filter) {
        
        if (provider == null) {
            return;
        }
        
        provider.visitChildren(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject instanceof Definitions && ((Definitions) radixObject).showApiBrowser() ) {

                    final Definitions defs = (Definitions) radixObject;
                    String name = defs.getName();
                    if (name == null || name.isEmpty()) {
                        ExtendableDefinitions container = RadixObjectsUtils.findContainer(radixObject, ExtendableDefinitions.class);
                        if (container != null) {
                            name = container.getName();
                        }
                    }

                    addMembers(name, defs.list(), filter);
                }
            }
        }, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject.getOwnerDefinition() == provider;
            }
        });
    }

    protected final void addMembers(String name, List<? extends RadixObject> objects, ApiFilter filter) {
        addMembers(name, objects, filter, true);
    }

    protected final void addMembers(String name, List<? extends RadixObject> objects, ApiFilter filter, boolean sort) {
        final List<RadixObject> apiMembers = getApiMembers(objects, filter);

        if (sort) {
            Collections.sort(apiMembers, new Comparator<RadixObject>() {
                @Override
                public int compare(RadixObject o1, RadixObject o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        if (!apiMembers.isEmpty()) {
            final IApiEditor<RadixObject> editor = ApiEditorManager.findEditor(apiMembers.get(0));
            final boolean embedded = editor != null && editor.isEmbedded();

            getBricks().add(new DefinitionsBrick(getSource(), name, apiMembers, embedded));
        }
    }

    protected final List<RadixObject> getApiMembers(List<? extends RadixObject> objects, ApiFilter filter) {
        final List<RadixObject> accepable = new ArrayList<>();

        for (final RadixObject member : objects) {
            if (filter.accept(member)) {
                accepable.add(member);
            }
        }

        return accepable;
    }
}
