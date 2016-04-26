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

package org.radixware.kernel.explorer.editors.jmleditor.completer;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem;
import org.radixware.kernel.common.scml.Scml.Item;


 public class SuggestCompletionItem implements CompletionItem{

        private final Object complItem;
        private final String typeName;

        public SuggestCompletionItem(final Object complItem, final String  typeName){
            this.complItem=complItem;
            this.typeName=typeName;
        }
        
        @Override
        public String getSortText() {
            if(complItem==null){
                return typeName;
            }else if(complItem instanceof XmlType){
                return typeName;
            }else if(complItem instanceof AdsMethodDef){
                return ((AdsMethodDef)complItem).getName();
            }
            return "";
        }

        @Override
        public int getRelevance() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getLeadDisplayText() {
            //if(complItem==null){
            //    return CompleterProcessor.NO_SUGGEST;
            //}else
            if(complItem instanceof XmlType){
                return typeName;
            }else if(complItem instanceof AdsMethodDef){
                return ((AdsMethodDef)complItem).getProfile().getName();
            }
            return typeName != null ? typeName : "";
        }
        
        public boolean isDeprecated(){
            return (complItem instanceof Definition) ? ((Definition)complItem).isDeprecated() : false;
        }

        @Override
        public String getTailDisplayText() {
            return "";
        }

        @Override
        public String getEnclosingSuffix() {
            return "";
        }

        @Override
        public RadixIcon getIcon() {
            return null;
        }

        @Override
        public Item[] getNewItems() {
           if(complItem instanceof AdsMethodDef) {
                return new Item[]{JmlTagInvocation.Factory.newInstance((AdsMethodDef)complItem)/*, Scml.Text.Factory.newInstance("()")*/};
           }else if ((complItem instanceof XmlType)&&(typeName!=null)){
               final XmlType xType=(XmlType)complItem;
               return new Item[]{new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml((IXmlDefinition)xType.getSource(), typeName))};
           }
           return null;
        }

        @Override
        public int getReplaceStartOffset() {
            return -1;
        }

        @Override
        public int getReplaceEndOffset() {
            return -1;
        }

        @Override
        public boolean removePrevious(final Item prevItem) {
            return true;
        }

        @Override
        public RadixObject getRadixObject() {
            return null;
        }

}
