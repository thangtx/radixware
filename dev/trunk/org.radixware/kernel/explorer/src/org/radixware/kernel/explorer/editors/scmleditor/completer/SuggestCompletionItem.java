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

package org.radixware.kernel.explorer.editors.scmleditor.completer;

import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionItem;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;


 public class SuggestCompletionItem implements IScmlCompletionItem{

        private Object complItem;
        private String typeName;

        public SuggestCompletionItem(Object complItem,String  typeName){
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

        public String getLeadDisplayText() {
            //if(complItem==null){
            //    return CompleterProcessor.NO_SUGGEST;
            //}else
            if(complItem instanceof XmlType){
                return typeName;
            }else if(complItem instanceof AdsMethodDef){
                return ((AdsMethodDef)complItem).getProfile().getName();
            }
            return "";
        }

        public String getTailDisplayText() {
            return "";
        }

        public String getEnclosingSuffix() {
            return "";
        }

    @Override
        public Icon getIcon() {
            return null;
        }

        public Item[] getNewItems() {
           if(complItem instanceof AdsMethodDef) {
                return new Item[]{JmlTagInvocation.Factory.newInstance((AdsMethodDef)complItem)/*, Scml.Text.Factory.newInstance("()")*/};
           }else if ((complItem instanceof XmlType)&&(typeName!=null)){
               XmlType xType=(XmlType)complItem;
               return new Item[]{new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml((IXmlDefinition)xType.getSource(), typeName))};
           }
           return null;
        }

    @Override
        public int getReplaceStartOffset() {
            return -1;
        }

        public int getReplaceEndOffset() {
            return -1;
        }

        public boolean removePrevious(IScmlItem prevItem) {
            return true;
        }

        public RadixObject getRadixObject() {
            return null;
        }

    @Override
    public String getLeadText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTailText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IScmlItem[] getItems() {
         /*if(complItem instanceof AdsMethodDef) {
                return new Item[]{JmlTagInvocation.Factory.newInstance((AdsMethodDef)complItem)};
           }else if ((complItem instanceof XmlType)&&(typeName!=null)){
               XmlType xType=(XmlType)complItem;
               return new Item[]{new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml((IXmlDefinition)xType.getSource(), typeName))};
           }*/
           return null;
    }
}
