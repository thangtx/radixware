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

package org.radixware.kernel.designer.common.editors.jml;

import java.util.ArrayList;
import java.util.Collection;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.mimelookup.InstanceProvider;
import org.netbeans.spi.editor.mimelookup.MimeLocation;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.SimpleTagItem;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagGenerator;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagGeneratorFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagItem;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.IdTagFactory;


@MimeRegistration(mimeType="text/x-jml", service=TagGeneratorFactory.class)
public class JmlTagGeneratorFactory implements TagGeneratorFactory {

    @Override
    public Collection<TagGenerator> getGenerators(ScmlEditorPane pane, RadixObject radixObject) {
        Collection<TagGenerator> generators = new ArrayList<TagGenerator>();

        if (radixObject instanceof OrderedPage) {
            radixObject = ((OrderedPage) radixObject).findPage();
        }

        if (radixObject instanceof AdsMethodDef || radixObject instanceof AdsPropertyDef) {
            generators.add(new InvocationTagGenerator((AdsDefinition) radixObject));
        }

        if (radixObject instanceof AdsClassDef) {
            generators.add(new TypeDeclarationTagGenerator((AdsClassDef) radixObject));
        }

        if (radixObject instanceof AdsDefinition) {
            final Tag idTag = IdTagFactory.createIdTag((Definition) radixObject, pane);
            if (idTag != null) {
                generators.add(new TagGenerator() {

                    @Override
                    public TagItem createTagItem() {
                        return new SimpleTagItem("Id Tag", idTag);
                    }
                });
            }
        }

        return generators;
    }

    private static class InvocationTagGenerator implements TagGenerator {

        private final AdsDefinition adsDefinition;

        public InvocationTagGenerator(AdsDefinition adsDefinition) {
            this.adsDefinition = adsDefinition;
        }

        @Override
        public TagItem createTagItem() {
            return new TagItem() {

                @Override
                public String getDisplayName() {
                    return "Invocation Tag";
                }

                @Override
                public Tag getTag() {
                    return JmlTagInvocation.Factory.newInstance(adsDefinition);
                }
            };

        }
    }

    private static class TypeDeclarationTagGenerator implements TagGenerator {

        private final AdsClassDef adsDefinition;

        public TypeDeclarationTagGenerator(AdsClassDef adsDefinition) {
            this.adsDefinition = adsDefinition;
        }

        @Override
        public TagItem createTagItem() {
            return new TagItem() {

                @Override
                public String getDisplayName() {
                    return "Type Declaration Tag";
                }

                @Override
                public Tag getTag() {
                    return new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(adsDefinition));
                }
            };

        }
    }
}
