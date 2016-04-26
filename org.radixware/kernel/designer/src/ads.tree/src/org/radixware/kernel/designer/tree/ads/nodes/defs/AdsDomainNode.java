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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.designer.ads.editors.creation.AdsNamedDefinitionCreature;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;


public class AdsDomainNode extends AdsObjectNode<AdsDomainDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsDomainDef> {

        @Override
        public Node newInstance(AdsDomainDef object) {
            return new AdsDomainNode(object);
        }
    }

    public AdsDomainNode(AdsDomainDef definition) {
        this(definition, new RadixObjectsNodeChildren<AdsDomainDef>(definition.getChildDomains()));
    }

    private AdsDomainNode(AdsDomainDef definition, RadixObjectsNodeChildren<AdsDomainDef> children) {
        super(definition, children);
        // getLookupContent().add(children);
    }

    @Override
    public AdsDomainDef getRadixObject() {
        return (AdsDomainDef) super.getRadixObject();
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new CreationSupport() {
            @Override
            public ICreatureGroup[] createCreatureGroups(RadixObject object) {
                return new ICreatureGroup[]{
                    new ICreatureGroup() {
                        @Override
                        public List<ICreature> getCreatures() {
                            return Collections.singletonList((ICreature) new AdsNamedDefinitionCreature(getRadixObject().getChildDomains(), "newDomain", "Domain") {
                                @Override
                                public RadixObject createInstance() {
                                    return AdsDomainDef.Factory.newInstance();
                                }
                            });
                        }

                        @Override
                        public String getDisplayName() {
                            return "Domain";
                        }
                    }
                };
            }
        };
    }
}
