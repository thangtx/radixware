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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.HierarchyIterator.Chain;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.JavaClassType;
import org.radixware.kernel.common.repository.ads.AdsSegment;

public class ChooseDefinitionMemberCfgs {

    public static abstract class ChooseClassMembersCfg extends ChooseDefinitionMembers.ChooseDefinitionMembersCfg {

        public ChooseClassMembersCfg(AdsClassDef initialClass) {
            super(initialClass);
        }

        public ChooseClassMembersCfg(AdsClassDef initialClass, boolean selfDisplaying, List<? extends AdsDefinition> additionalDefinitions) {
            super(initialClass, selfDisplaying, additionalDefinitions);
        }

        public ChooseClassMembersCfg(AdsClassDef initialClass, boolean selfDisplaying) {
            super(initialClass, selfDisplaying);
        }

        @Override
        public List<AdsClassDef> listBaseDefinitions(AdsDefinition def, Collection<AdsDefinition> seen) {
            if (!(def instanceof AdsClassDef)) {
                return Collections.emptyList();
            }
            AdsClassDef root = (AdsClassDef) def;
            AdsClassDef ovr = root.getHierarchy().findOverwritten().get();
            ArrayList<AdsClassDef> classes = new ArrayList<AdsClassDef>();
            while (ovr != null) {
                classes.add(ovr);
                ovr = ovr.getHierarchy().findOverwritten().get();
            }

            ArrayList<AdsTypeDeclaration> refsToResolve = new ArrayList<AdsTypeDeclaration>();
            AdsTypeDeclaration superclass = root.getInheritance().getSuperClassRef();

            if (superclass != null) {
                refsToResolve.add(superclass);
            }

            for (AdsTypeDeclaration decl : root.getInheritance().getOwnAndPlatformInerfaceRefList(EScope.LOCAL)) {
                refsToResolve.add(decl);
            }

            for (AdsTypeDeclaration decl : refsToResolve) {
                AdsType type = decl.resolve(initialDef).get();
                if (type instanceof AdsClassType) {
                    AdsClassDef clazz = ((AdsClassType) type).getSource();
                    if (clazz != null) {
                        classes.add(clazz);
                    }
                } else if (type instanceof JavaClassType) {
                    JavaClassType ct = (JavaClassType) type;
                    IPlatformClassPublisher pub = ((AdsSegment) initialDef.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(ct.getJavaClassName());
                    if (pub instanceof AdsClassDef && pub != root) {
                        classes.add((AdsClassDef) pub);
                    }
                }
            }

            return classes;
        }
    }

    public static abstract class ChoosePresentationMemberCfg extends ChooseDefinitionMembers.ChooseDefinitionMembersCfg {

        public ChoosePresentationMemberCfg(AdsPresentationDef initialDef, boolean selfDisplaying, List<? extends AdsDefinition> additionalDefinitions) {
            super(initialDef, selfDisplaying, additionalDefinitions);
        }

        public ChoosePresentationMemberCfg(AdsPresentationDef initialDef, boolean selfDisplaying) {
            super(initialDef, selfDisplaying);
        }

        public ChoosePresentationMemberCfg(AdsPresentationDef initialDef) {
            super(initialDef);
        }

        @Override
        public boolean incremental() {
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public List<? extends AdsPresentationDef> listBaseDefinitions(AdsDefinition def, Collection<AdsDefinition> seen) {
            assert def == initialDef;
            List<AdsPresentationDef> result = new LinkedList<>();
            HierarchyIterator<AdsPresentationDef> iter = ((AdsPresentationDef) initialDef).getHierarchyIterator(EScope.ALL, HierarchyIterator.Mode.FIND_ALL);
            AdsPresentationDef first = iter.next().first();
            assert first == def;
            while (iter.hasNext()) {
                Chain<AdsPresentationDef> chain = iter.next();
                if (chain.isEmpty()) {
                    break;
                }
                for (AdsPresentationDef pr : chain) {
                    result.add(pr);
                }
            }
            return result;
//            seen.add(def);
//            AdsPresentationDef presentation = (AdsPresentationDef) def;
//            HierarchyIterator<AdsPresentationDef> iter = presentation.getHierarchyIterator(EScope.ALL, HierarchyIterator.Mode.FIND_ALL);
//            AdsPresentationDef next = iter.next().first();
//
//            assert next == def;
//
//            if (iter.hasNext()) {
//                return Collections.singletonList(iter.next().first());
//            } else {
//                return Collections.emptyList();
//            }
        }
    }
}
