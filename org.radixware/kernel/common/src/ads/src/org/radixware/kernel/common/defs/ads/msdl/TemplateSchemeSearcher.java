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

package org.radixware.kernel.common.defs.ads.msdl;

import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.msdl.fields.ISchemeSearcher;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.msdl.MsdlStructureFields;
import org.radixware.kernel.common.msdl.MsdlVariantFields;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TemplateSchemeSearcher implements ISchemeSearcher {

    private class SchemeInternalVisitor implements IVisitor {

        public AbstractFieldModel target = null;

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof MsdlField) {
                target = ((MsdlField) radixObject).getFieldModel();
            } else if (radixObject instanceof AdsMsdlSchemeDef) {
                target = ((AdsMsdlSchemeDef) radixObject).getRootMsdlScheme().getFieldModel();
            }else if(radixObject instanceof  AbstractFieldModel) {
                target = (AbstractFieldModel)radixObject;
            }
        }
    }

    class SchemeInternalVisitorProvider extends VisitorProvider {

        String path = "/";
        AdsMsdlSchemeDef context = null;
        EFieldType type = EFieldType.STRUCTURE;

        public SchemeInternalVisitorProvider(String path, AdsMsdlSchemeDef context, EFieldType type) {
            this.path = path;
            this.context = context;
            this.type = type;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            boolean res = false;
            if(path == null)
                return false;
            String[] componentsArr = path.split("/");
            RootMsdlScheme scheme = context.getRootMsdlScheme();
            List<String> components = new LinkedList<>();
            for (String s : componentsArr) {
                if(!s.isEmpty())
                    components.add(s);
            }

            /* in case path had been specified like this:
                  /SomeTemplateFieldName
            */
            /*if (components.length == 2 && components[0].isEmpty()) {
                res = performSearchEffort(scheme, components[1]);
            } else {*/
                ListIterator<String> it = components.listIterator(components.size());
                RadixObject cur = radixObject;
                boolean fail = false;
                while(it.hasPrevious()) {
                    String component = it.previous();
                    if (component.equals(cur.getName())) {
                        cur = cur.getContainer();
                    }
                    else {
                        fail = true;
                        break;
                    }
                }
                res = !fail;
                //}
            return res;
        }

        @Override
        public boolean isContainer(RadixObject object) {
            boolean isMsdlField = object instanceof MsdlField;
            //suddenly, field model can live inside MsdlField
            boolean isFieldModel =  object instanceof AbstractFieldModel;
            boolean isFieldContainer = object instanceof MsdlStructureFields ||
                                       object instanceof MsdlVariantFields;
                                
            if (isMsdlField) {
                MsdlField f = (MsdlField) object;
                return f.getFieldModel().getType() == type;
            }
            if (isFieldModel) {
                return ((AbstractFieldModel)object).getType() == type;
            }
            if (isFieldContainer) {
                return true;
            }
            
            return false;
        }

        private boolean performSearchEffort(RadixObject fieldContainter, String component) {
            boolean res = false;
            /*if (type == EFieldType.STRUCTURE) {
                if (scheme.getFieldModel() instanceof StructureFieldModel) {
                    for (MsdlStructureField f : ((StructureFieldModel) scheme.getFieldModel()).getFields()) {
                        if (f.getName().equals(component)) {
                            res = true;
                            break;
                        }
                    }
                }
            } else if (type == EFieldType.CHOICE) {
                if (scheme.getFieldModel() instanceof ChoiceFieldModel) {
                    ChoiceFieldModel cfm = (ChoiceFieldModel) scheme.getFieldModel();
                    for (MsdlVariantField f : cfm.getFields()) {
                        if (f.getName().equals(component)) {
                            res = true;
                            break;
                        }
                    }
                }
            }*/
            return res;
        }
    }
    AdsMsdlSchemeDef parent = null;

    public TemplateSchemeSearcher(AdsMsdlSchemeDef parent) {
        this.parent = parent;
    }

    @Override
    public AbstractFieldModel findField(Id templateSchemeId, String templateSchemePath, EFieldType type) {
        if (parent == null) {
            return null;
        }
        AdsMsdlSchemeDef context = parent;
        if (templateSchemeId != null) {
            DefinitionSearcher<AdsDefinition> searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(parent);
            SearchResult<AdsDefinition> found = searcher.findById(templateSchemeId);
            if (!found.isEmpty()) {
                AdsDefinition foundDefinition = found.get();
                if (foundDefinition instanceof AdsMsdlSchemeDef) {
                    context = (AdsMsdlSchemeDef) foundDefinition;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        SchemeInternalVisitor visitor = new SchemeInternalVisitor();
        context.getRootMsdlScheme().visit(visitor, new SchemeInternalVisitorProvider(templateSchemePath, context, type));
        return visitor.target;
    }
}