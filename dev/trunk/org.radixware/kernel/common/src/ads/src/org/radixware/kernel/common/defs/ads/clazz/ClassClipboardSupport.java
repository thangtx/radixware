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

package org.radixware.kernel.common.defs.ads.clazz;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;


abstract class ClassClipboardSupport<T extends AdsDefinition> extends AdsClipboardSupport<T> {

    private final AdsClassDef ownerClass;
    private final AdsPropertyGroup contextPropGroup;
    private final AdsMethodGroup contextMethodGroup;

    ClassClipboardSupport(AdsMethodGroup g) {
        this(g, g.getOwnerClass(), g);
    }

    ClassClipboardSupport(AdsPropertyGroup g) {
        this(g, g.getOwnerClass(), g);
    }

    ClassClipboardSupport(AdsClassDef c) {
        this(c, c, null);
    }

    @SuppressWarnings("unchecked")
    private ClassClipboardSupport(AdsDefinition context, AdsClassDef ownerClass, MembersGroup contextGroup) {
        super((T) context);
        this.ownerClass = ownerClass;
        if (contextGroup != null) {
            if (contextGroup instanceof AdsMethodGroup) {
                contextMethodGroup = (AdsMethodGroup) contextGroup;
                contextPropGroup = null;
            } else if (contextGroup instanceof AdsPropertyGroup) {
                contextPropGroup = (AdsPropertyGroup) contextGroup;
                contextMethodGroup = null;
            } else {
                contextMethodGroup = null;
                contextPropGroup = null;
            }
        } else {
            contextMethodGroup = ownerClass.getMethodGroup();
            contextPropGroup = ownerClass.getPropertyGroup();
        }
    }

    @SuppressWarnings({ "unchecked", "unchecked" })
    @Override
    public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
        if (ownerClass == null) {
            return CanPasteResult.NO;
        }

        final List<Transfer> methodTransfers = new ArrayList<>();
        final List<Transfer> methodGroupTransfers = new ArrayList<>();

        final List<Transfer> propTransfers = new ArrayList<>();
        final List<Transfer> propGroupTransfers = new ArrayList<>();

        final List<Transfer> classesTransfers = new ArrayList<>();

        for (Transfer t : objectsInClipboard) {
            if (t.getObject() instanceof AdsMethodDef) {
                if (contextMethodGroup == null) {
                    return CanPasteResult.NO;
                }
                methodTransfers.add(t);
            } else if (t.getObject() instanceof AdsMethodGroup) {
                if (contextMethodGroup == null) {
                    return CanPasteResult.NO;
                }
                methodGroupTransfers.add(t);
            } else if (t.getObject() instanceof AdsPropertyDef) {
                if (contextPropGroup == null) {
                    return CanPasteResult.NO;
                }
                propTransfers.add(t);
            } else if (t.getObject() instanceof AdsPropertyGroup) {
                if (contextPropGroup == null) {
                    return CanPasteResult.NO;
                }
                propGroupTransfers.add(t);
            } else if (t.getObject() instanceof AdsClassDef) {
                classesTransfers.add(t);
            } else {
                return CanPasteResult.NO;
            }
        }
        CanPasteResult result;
        if (!methodTransfers.isEmpty()) {
            result = ownerClass.getMethods().getClipboardSupport().canPaste(methodTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return result;
            }
        }
        if (!methodGroupTransfers.isEmpty()) {
            result = contextMethodGroup.getChildGroups().getClipboardSupport().canPaste(methodGroupTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return result;
            }
        }
        if (!propTransfers.isEmpty()) {
            result = ownerClass.getProperties().getClipboardSupport().canPaste(propTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return result;
            }
        }
        if (!propGroupTransfers.isEmpty()) {
            result = contextPropGroup.getChildGroups().getClipboardSupport().canPaste(propGroupTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return CanPasteResult.NO;
            }
        }

        if (!classesTransfers.isEmpty()) {
            result = ownerClass.getNestedClasses().getClipboardSupport().canPaste(classesTransfers, resolver);
            if (result != CanPasteResult.YES) {
                return CanPasteResult.NO;
            }
        }
        return CanPasteResult.YES;
    }

    @Override
    public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {

        final List<Transfer> methodTransfers = new ArrayList<>();
        final List<Transfer> methodGroupTransfers = new ArrayList<>();

        final List<Transfer> propTransfers = new ArrayList<>();
        final List<Transfer> propGroupTransfers = new ArrayList<>();

        final List<Transfer> classesTransfers = new ArrayList<>();

        for (Transfer t : objectsInClipboard) {
            if (t.getObject() instanceof AdsMethodDef) {
                methodTransfers.add(t);
            } else if (t.getObject() instanceof AdsMethodGroup) {
                methodGroupTransfers.add(t);
            } else if (t.getObject() instanceof AdsPropertyDef) {
                propTransfers.add(t);
            } else if (t.getObject() instanceof AdsPropertyGroup) {
                propGroupTransfers.add(t);
            } else if (t.getObject() instanceof AdsDynamicClassDef) {
                classesTransfers.add(t);
            }
        }

        if (!methodTransfers.isEmpty()) {
            ownerClass.getMethods().getClipboardSupport().paste(methodTransfers, resolver);
        }
        if (!propTransfers.isEmpty()) {
            ownerClass.getProperties().getClipboardSupport().paste(propTransfers, resolver);
        }

        if (!classesTransfers.isEmpty()) {
            ownerClass.getNestedClasses().getClipboardSupport().paste(classesTransfers, resolver);
        }

        if (!methodGroupTransfers.isEmpty() && contextMethodGroup != null) {
            contextMethodGroup.getChildGroups().getClipboardSupport().paste(methodGroupTransfers, resolver);
        }
        if (!propGroupTransfers.isEmpty() && contextPropGroup != null) {
            contextPropGroup.getChildGroups().getClipboardSupport().paste(propGroupTransfers, resolver);
        }

        if (contextMethodGroup != null) {
            for (Transfer t : methodTransfers) {
                AdsMethodDef md = (AdsMethodDef) t.getObject();
                if (md.getOwnerClass() == ownerClass) {
                    contextMethodGroup.addMember(md);
                }
            }
        }
        if (contextPropGroup != null) {
            for (Transfer t : propTransfers) {
                AdsPropertyDef pd = (AdsPropertyDef) t.getObject();
                if (pd.getOwnerClass() == ownerClass) {
                    contextPropGroup.addMember(pd);
                }
            }
        }
    }
}
