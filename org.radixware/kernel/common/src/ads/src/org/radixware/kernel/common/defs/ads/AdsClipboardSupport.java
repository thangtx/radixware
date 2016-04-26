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

package org.radixware.kernel.common.defs.ads;

import java.util.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.clazz.MembersGroup;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.schemas.adsdef.CbDefinitionDocument;
import org.radixware.schemas.adsdef.CbScml;
import org.radixware.schemas.adsdef.CbStringList;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.commondef.CbInfo;


public abstract class AdsClipboardSupport<T extends RadixObject> extends ClipboardSupport<T> {

    public static class AdsTransfer<T extends RadixObject> extends ClipboardSupport.Transfer<T> {

        protected Map<Id, IMultilingualStringDef> mlStrings = null;

        public AdsTransfer(T source) {
            super(source);
            if (source.getContainer() != null) {
                Collection<ILocalizedDef.MultilingualStringInfo> usedMlStrings = collectStringInfo(source);
                if (!usedMlStrings.isEmpty()) {
                    this.mlStrings = new HashMap<>();
                    for (ILocalizedDef.MultilingualStringInfo info : usedMlStrings) {
                        IMultilingualStringDef sd = info.findString();
                        if (sd != null) {
                            this.mlStrings.put(sd.getId(), sd);
                        }
                    }
                }
            }
        }

        public AdsTransfer(T source, CbInfo info) {
            this(source);
            if (info != null) {
                checkMlStrings(info);
            }
        }

        public AdsTransfer<T> copy() {
            @SuppressWarnings("unchecked")
            T newObject = (T) getObject().getClipboardSupport().copy();
            AdsTransfer<T> t = new AdsTransfer<>(newObject);
            if (mlStrings != null) {
                if (t.mlStrings == null) {
                    t.mlStrings = new HashMap<>();
                } else {
                    t.mlStrings.clear();
                }
                t.mlStrings.putAll(mlStrings);
            }
            t.afterCopy();
            return t;
        }

        @Override
        public void afterPaste() {
            super.afterPaste();
            RadixObject object = getObject();
            Definition ownerDef = object instanceof AdsDefinition ? (AdsDefinition) object : object.getOwnerDefinition();
            if (ownerDef != null) {
                afterPaste(ownerDef);
            }
            if (object instanceof MembersGroup) {
                ((MembersGroup) object).afterPaste();
            }
        }

        public void afterPaste(Definition ownerDef) {
            if (mlStrings != null && !mlStrings.isEmpty()) {
                if (ownerDef instanceof AdsDefinition) {
                    updateCopyMlStrings();
                    ILocalizingBundleDef bundle = ((AdsDefinition) ownerDef).findLocalizingBundle();
                    if (bundle != null) {
                        for (IMultilingualStringDef string : mlStrings.values()) {
                            assert string.getContainer() == null;
                            bundle.getStrings().getLocal().add((Definition) string);
                        }
                        bundle.getStrings().updateStringsByLanguages();
                    }
                }
            }
        }

        @Override
        protected void afterDuplicate() {
            super.afterDuplicate();
            updateCopyMlStrings();
        }

        @Override
        protected void afterCopy() {
            super.afterCopy();
            updateCopyMlStrings();
        }

        private void updateCopyMlStrings() {
            if (mlStrings != null) {
                RadixObject radixObject = getObject();
                Map<Id, IMultilingualStringDef> newMlStrings = new HashMap<>(mlStrings.size());
                Collection<ILocalizedDef.MultilingualStringInfo> stringInfo = collectStringInfo(radixObject);
                for (ILocalizedDef.MultilingualStringInfo info : stringInfo) {
                    IMultilingualStringDef string = mlStrings.get(info.getId());
                    if (string != null) {
                        IMultilingualStringDef newString;
                        if (info.canChangeId()){
                           newString = string.cloneString(null);
                        } else {
                            newString = string.cloneString(null,true);
                        }
                        newMlStrings.put(newString.getId(), newString);
                        info.updateId(newString.getId());
                    } else {
                        info.updateId(null);
                    }
                }
                this.mlStrings = newMlStrings;
            }
        }

        private static Collection<ILocalizedDef.MultilingualStringInfo> collectStringInfo(RadixObject definition) {
            final ArrayList<ILocalizedDef.MultilingualStringInfo> stringInfo = new ArrayList<>();
            definition.visit(new IVisitor() {
                @Override
                public void accept(RadixObject object) {
                    ILocalizedDef def = (ILocalizedDef) object;
                    def.collectUsedMlStringIds(stringInfo);
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof ILocalizedDef;
                }
            });

            return stringInfo;
        }

        public String encode() {
            if (getObject() instanceof Scml) {
                Scml src = (Scml) getObject();
                CbDefinitionDocument xDoc = CbDefinitionDocument.Factory.newInstance();
                CbScml xSxml = xDoc.addNewCbDefinition();
                if (src instanceof Jml) {
                    ((Jml) src).appendTo(xSxml.addNewJml(), AdsDefinition.ESaveMode.NORMAL);
                } else if (src instanceof Sqml) {
                    ((Sqml) src).appendTo(xSxml.addNewSqml());
                }
//                if (this.mlStrings != null && !this.mlStrings.isEmpty()) {
//                    CbStringList xStrings = xSxml.addNewStrings();
//                    for (AdsMultilingualStringDef string : mlStrings.values()) {
//                        string.appendTo(xStrings.addNewString(), AdsDefinition.ESaveMode.NORMAL);
//                    }
//                }
                CbStringList xStrings = saveMlStrings();
                if (xStrings != null) {
                    xSxml.setStrings(xStrings);
                }
                return xDoc.xmlText();
            } else {
                return null;
            }
        }

        private CbStringList saveMlStrings() {
            if (this.mlStrings != null && !this.mlStrings.isEmpty()) {
                CbStringList xStrings = CbStringList.Factory.newInstance();
                for (IMultilingualStringDef string : mlStrings.values()) {
                    string.appendTo(xStrings.addNewString());
                }
                return xStrings;
            }
            return null;
        }

        private void checkMlStrings(CbInfo info) {
            if (info.getExtInfo() != null) {
                XmlObject obj = XmlObjectProcessor.castToXmlClass(CbStringList.class.getClassLoader(), info.getExtInfo(), CbStringList.class);
                if (obj instanceof CbStringList) {
                    List<LocalizedString> xStringList = ((CbStringList) obj).getStringList();
                    if (xStringList != null) {
                        mlStrings = new HashMap<>();
                        for (LocalizedString xString : xStringList) {
                            AdsMultilingualStringDef def = AdsMultilingualStringDef.Factory.loadFrom(xString);
                            if (def != null) {
                                mlStrings.put(def.getId(), def);
                            }
                        }
                    }
                }
            }
        }

        public static AdsTransfer<Scml> decode(String asString) throws XmlException {
            CbDefinitionDocument xDoc = CbDefinitionDocument.Factory.parse(asString);
            CbScml xSxml = xDoc.getCbDefinition();
            if (xSxml == null) {
                return null;
            }
            Scml object;
            if (xSxml.isSetJml()) {
                object = Jml.Factory.loadFrom(null, xSxml.getJml(), "");
            } else if (xSxml.isSetSqml()) {
                object = Sqml.Factory.newInstance();
                ((Sqml) object).loadFrom(xSxml.getSqml());
            } else {
                return null;
            }
            AdsTransfer<Scml> transfer = new AdsTransfer<>(object);
            if (xSxml.getStrings() != null) {
                List<LocalizedString> xStringList = xSxml.getStrings().getStringList();
                if (xStringList != null) {
                    transfer.mlStrings = new HashMap<>();
                    for (LocalizedString xString : xStringList) {
                        AdsMultilingualStringDef def = AdsMultilingualStringDef.Factory.loadFrom(xString);
                        if (def != null) {
                            transfer.mlStrings.put(def.getId(), def);
                        }
                    }
                }
            }
            return transfer;
        }
    }

    protected AdsClipboardSupport(T radixObject) {
        super(radixObject);
    }

//    @Override
//    public String encode(Transfer srcTransfer) {
//        if (!isEncodedFormatSupported()) {
//            return super.encode(srcTransfer);
//        } else {
//            Method decoder = getDecoderMethod();
//            if (decoder == null || decoder.getParameterTypes().length != 1) {
//                return super.encode(srcTransfer);
//            } else {
//                ClipboardInfoDocument xDoc = ClipboardInfoDocument.Factory.newInstance();
//                CbInfo info = xDoc.addNewClipboardInfo();
//                info.setDecoderClass(decoder.getDeclaringClass().getName());
//                info.setDecoderMethod(decoder.getName());
//                info.setDefinitionXmlClass(decoder.getParameterTypes()[0].getName());
//                info.setDefinition(initialToXml(srcTransfer));
//                Collection<ILocalizedDef.MultilingualStringInfo> infos = AdsTransfer.collectStringInfo(srcTransfer.getInitialObject());
//                if (!infos.isEmpty()) {
//                    CbStringList list = CbStringList.Factory.newInstance();
//                    for (ILocalizedDef.MultilingualStringInfo si : infos) {
//                        AdsMultilingualStringDef sd = si.findString();
//                        if (sd != null) {
//                            sd.appendTo(list.addNewString(), AdsDefinition.ESaveMode.NORMAL);
//                        }
//                    }
//                    info.setExtInfo(list);
//                }
//                return xDoc.xmlText();
//            }
//        } 
//    }
    protected CbStringList getExtInfoForEncodedFormat(Transfer srcTransfer) {
        Collection<ILocalizedDef.MultilingualStringInfo> infos = AdsTransfer.collectStringInfo(srcTransfer.getInitialObject());
        if (!infos.isEmpty()) {
            CbStringList list = CbStringList.Factory.newInstance();
            for (ILocalizedDef.MultilingualStringInfo si : infos) {
                IMultilingualStringDef sd = si.findString();
                if (sd != null) {
                    sd.appendTo(list.addNewString());
                }
            }
            return list;
        }
        return null;
    }

    @Override
    public boolean isEncodedFormatSupported() {
        return getDecoderMethod() != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Transfer decode(CbInfo info) {
        return newTransferInstance(info);
    }

    @Override
    protected Transfer<T> newTransferInstance() {
        return new AdsTransfer<>(radixObject);
    }

    @Override
    protected Transfer<T> newTransferInstance(CbInfo info) {
        return new AdsTransfer<>(radixObject, info);
    }

    @Override
    protected boolean isIdChangeRequired(RadixObject copyRoot) {
        return !(radixObject instanceof IDependentId);
    }
}
