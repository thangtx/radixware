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

package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlProcessor;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;


public class JmlTagLocalizedString extends Jml.Tag implements ILocalizedDef {

    public enum EType {

        SIMPLE(0),
        OBJECT(1);
        private final int type;

        int getType() {
            return type;
        }

        private EType(final int type) {
            this.type = type;
        }

        static EType getForValue(final int val) {
            switch (val) {
                case 1:
                    return OBJECT;
                default:
                    return SIMPLE;
            }
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new CodeWriter(this, purpose) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        AdsDefinition def = getOwnerJml().getOwnerDef();
                        if (def instanceof AdsClassMember) {
                            def = ((AdsClassMember) def).getOwnerClass();
                        }
                        WriterUtils.writeNLSInvocation(printer, getBundleId(), stringId, def, usagePurpose, getType() == EType.OBJECT);
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }

    private boolean isInComment() {
        Jml jml = getOwnerJml();
        final boolean[] isInComment = new boolean[]{false};
        ScmlProcessor p = new ScmlProcessor() {
            private CommentsAnalizer a = CommentsAnalizer.Factory.newJavaCommentsAnalizer();

            @Override
            protected CommentsAnalizer getCommentsAnalizer() {
                return a;
            }

            @Override
            protected void processText(Scml.Text text) {
                a.process(text.getText());
            }

            @Override
            protected void processTag(Scml.Tag tag) {
            }

            @Override
            protected void processTagInComment(Scml.Tag tag) {
                if (tag == JmlTagLocalizedString.this) {
                    isInComment[0] = true;
                }
            }
        };
        p.process(jml);
        return isInComment[0];
    }

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        AdsLocalizingBundleDef bundle = getOwnerJml().getOwnerDef().findExistingLocalizingBundle();
        if (bundle == null) {
            error(problemHandler, "Localizing bundle not found");
        }

        AdsMultilingualStringDef string = bundle == null ? null : bundle.getStrings().findById(stringId, EScope.LOCAL_AND_OVERWRITE).get();

        if (string == null) {
            error(problemHandler, "Multilingual string not found: #" + stringId);
        }
    }

    public static final class Factory {

        /**
         * Creates an instance of {@linkplain JmlTagLocalizedString} or
         * {@linkplain JmlTagEventCode} according to given string parameter
         */
        public static final JmlTagLocalizedString newInstance(AdsMultilingualStringDef string) {
            if (string.getDefinitionType() == EDefType.MULTILINGUAL_EVENT_CODE) {
                return new JmlTagEventCode(string.getId(), EType.SIMPLE);
            } else {
                return new JmlTagLocalizedString(string.getId(), EType.SIMPLE);
            }
        }

        /**
         * Creates an instance of {@linkplain JmlTagLocalizedString} or
         * {@linkplain JmlTagEventCode} according to given source tag
         */
        public static final JmlTagLocalizedString newInstance(JmlTagLocalizedString src) {
            if (src instanceof JmlTagEventCode) {
                return new JmlTagEventCode((JmlTagEventCode) src);
            } else {
                return new JmlTagLocalizedString(src);
            }
        }

        public static final JmlTagLocalizedString newInstance(AdsDefinition context, EIsoLanguage language, String value) {
            Id stringId = context.setLocalizedStringValue(language, null, value);
            if (stringId == null) {
                throw new RadixObjectError("Can not create multilingual string");
            }
            return new JmlTagLocalizedString(stringId, EType.SIMPLE);
        }
    }
    protected Id stringId;
    protected EType type;

    protected JmlTagLocalizedString(JmlType.Item.LocalizedString xNls) {
        this(Id.Factory.loadFrom(xNls.getStringId()), EType.getForValue(xNls.getType()));
    }

    protected JmlTagLocalizedString(JmlTagLocalizedString src) {
        this(src.getStringId(), src.getType());
    }

//    protected JmlTagLocalizedString(String ownerId, String stringId){
//        this.ownerId = Id.Factory.loadFrom(ownerId);
//        this.stringId = Id.Factory.loadFrom(stringId);
//    }
    protected JmlTagLocalizedString(Id stringId, EType type) {
        this.stringId = stringId;
        this.type = type;
    }

    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        if (type != null) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void appendTo(Item item) {
        Item.LocalizedString str = item.addNewLocalizedString();
        if (stringId != null) {
            str.setStringId(stringId.toString());
        }
        if (type != EType.SIMPLE) {
            str.setType(type.type);
        }
    }

    public Id getStringId() {
        return stringId;
    }

    public void setStringId(Id stringId) {
        this.stringId = stringId;
    }

    protected Id getBundleId() {
        AdsDefinition ownerDef = getOwnerJml().getOwnerDef();
        if (ownerDef != null) {
            return ownerDef.getLocalizingBundleId();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag localized string={0}.{1}]", getBundleId(), stringId);
    }

    AdsMultilingualStringDef findString() {
        AdsLocalizingBundleDef bundle = getOwnerJml().getOwnerDef().findExistingLocalizingBundle();
        if (bundle == null) {
            return null;
        }
        return bundle.getStrings().findById(stringId, EScope.LOCAL_AND_OVERWRITE).get();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsMultilingualStringDef string = findString();
        if (string != null) {
            list.add(string);
        }
    }

    @Override
    public String getDisplayName() {
        AdsMultilingualStringDef string = findString();
        if (string != null) {
            String value = string.getValue(EIsoLanguage.ENGLISH);

            if (value == null || value.isEmpty()) {
                Set<EIsoLanguage> langs = string.getLanguages();
                for (EIsoLanguage lang : langs) {
                    value = string.getValue(lang);
                    if (value != null && !value.isEmpty()) {
                        break;
                    }
                }
            }
            if (value == null || value.isEmpty()) {
                value = "";
            }
            int index = value.indexOf("\n");
            if (index >= 0) {
                value = value.substring(0, index) + "...";
            }
            return MessageFormat.format("\"{0}\"", value.replace("\"", "\\\""));
        } else {
            return MessageFormat.format("!!!String not found: {0}", stringId.toString());
        }
    }

    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(JmlTagLocalizedString.this) {
            private byte isInComment = 0;

            @Override
            public Id getId() {
                return stringId;
            }

            @Override
            public void updateId(Id newId) {
                stringId = newId;
            }

            @Override
            public EAccess getAccess() {
                return EAccess.PRIVATE;
            }

            @Override
            public String getContextDescription() {
                return "String Constant in Program Code";
            }

            @Override
            public boolean isPublished() {
                return false;
            }

            @Override
            public boolean isInComment() {
                if (isInComment  == 0) {
                    boolean check = JmlTagLocalizedString.this.isInComment();
                    if (check) {
                        isInComment = 3;
                    } else {
                        isInComment = 1;
                    }
                }
                return (isInComment & 0x2) != 0 ? true : false;
            }

            @Override
            public EMultilingualStringKind getKind() {
                if (getOwner() instanceof JmlTagEventCode) {
                    return EMultilingualStringKind.EVENT_CODE;
                } else {
                    return EMultilingualStringKind.CODE;
                }
            }

        });
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        final Jml jml = getOwnerJml();
        if (jml != null) {
            return jml.getOwnerDef().findLocalizedString(stringId);
        } else {
            return null;
        }
    }
}
