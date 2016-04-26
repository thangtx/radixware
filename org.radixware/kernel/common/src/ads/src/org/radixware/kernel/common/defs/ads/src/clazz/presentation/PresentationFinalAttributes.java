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

package org.radixware.kernel.common.defs.ads.src.clazz.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.SelectorAddons;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.types.Id;


abstract class DefinitionFinalAttributes<T extends AdsDefinition> {

    protected final T def;

    protected DefinitionFinalAttributes(T def) {
        this.def = def;
    }

    @SuppressWarnings("unchecked")
    static <U extends AdsDefinition> Collection<U> collectOverwrittenPresentations(U def) {
        final ArrayList<U> ppss = new ArrayList<>();
        AdsDefinition ovr = def.getHierarchy().findOverwritten().get();
        while (ovr != null) {
            ppss.add((U) ovr);
            ovr = ovr.getHierarchy().findOverwritten().get();
        }
        return ppss;
    }
}

class FormPresentationFinalAttributes extends DefinitionFinalAttributes<AdsClassDef> {

    public final transient EditorPages finalEditorPages;
    public final transient Id finalIconId;
    public final transient Set<EPresentationAttrInheritance> finalInheritanceMask;

    @SuppressWarnings("unchecked")
    public FormPresentationFinalAttributes(final IAdsFormPresentableClass def) {
        super((AdsClassDef) def);

        final AbstractFormPresentations form = def.getPresentations();

        finalInheritanceMask = form.getInheritanceMask();
        form.isEditorPagesInherited();
        form.isIconInherited();
        final Collection<AdsClassDef> overwritten = collectOverwrittenPresentations((AdsClassDef) def);
        EditorPages pages = null;
        if (form.isEditorPagesInherited()) {
            for (AdsClassDef clazz : overwritten) {
                if (clazz instanceof IAdsFormPresentableClass) {
                    final IAdsFormPresentableClass fpc = (IAdsFormPresentableClass) clazz;
                    if (!fpc.getPresentations().isEditorPagesInherited()) {
                        pages = fpc.getPresentations().getEditorPages();
                        finalInheritanceMask.remove(EPresentationAttrInheritance.PAGES);
                        break;
                    }
                }
            }
        } else {
            pages = form.getEditorPages();
        }
        finalEditorPages = pages;
        Id id = null;
        if (form.isIconInherited()) {
            for (AdsClassDef clazz : overwritten) {
                if (clazz instanceof IAdsFormPresentableClass) {
                    final IAdsFormPresentableClass fpc = (IAdsFormPresentableClass) clazz;
                    if (!fpc.getPresentations().isIconInherited()) {
                        id = fpc.getPresentations().getIconId();
                        finalInheritanceMask.remove(EPresentationAttrInheritance.ICON);
                        break;
                    }
                }
            }
        } else {
            id = form.getIconId();
        }
        finalIconId = id;
    }
}

public abstract class PresentationFinalAttributes<T extends AdsPresentationDef> extends DefinitionFinalAttributes<T> {

    public final transient Id finalIconId;
    public final transient Restrictions finalRestrictions;
    public final transient Id finalTitleId;
    public final transient EnumSet<EPresentationAttrInheritance> finalInheritanceMask;

    public static boolean isEditorPagesInherited(AdsEditorPresentationDef presentation) {
        if (!presentation.isEditorPagesInherited()) {
            return false;
        } else {
            for (AdsEditorPresentationDef epr : collectOverwrittenPresentations(presentation)) {
                if (!epr.isEditorPagesInherited()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isEditorPagesInherited(AbstractFormPresentations presentation) {
        if (!presentation.isEditorPagesInherited()) {
            return false;
        } else {
            for (AdsClassDef clazz : collectOverwrittenPresentations(presentation.getOwnerClass())) {
                if (clazz instanceof IAdsFormPresentableClass) {
                    AbstractFormPresentations p = ((IAdsFormPresentableClass) clazz).getPresentations();
                    if (p != null && p.isEditorPagesInherited()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    protected PresentationFinalAttributes(T def, Collection<T> overwritten) {
        super(def);
        finalInheritanceMask = def.getInheritanceMask();

        Id id = null;
        if (def.isIconInherited()) {
            for (T spr : overwritten) {
                if (!spr.isIconInherited()) {
                    id = spr.getIconId();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.ICON);
                    break;
                }
            }
        } else {
            id = def.getIconId();
        }
        finalIconId = id;
        Restrictions r = null;
        if (def.isRestrictionsInherited()) {
            for (T spr : overwritten) {
                if (!spr.isRestrictionsInherited()) {
                    r = spr.getRestrictions();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.RESTRICTIONS);
                    break;
                }
            }
        } else {
            r = def.getRestrictions();
        }
        finalRestrictions = r;
        id = null;
        if (def.isTitleInherited()) {
            for (T spr : overwritten) {
                if (!spr.isTitleInherited()) {
                    id = spr.getTitleId();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.TITLE);
                    break;
                }
            }
        } else {
            id = def.getTitleId();
        }
        finalTitleId = id;
    }
}

class EditorPresentationFinalAttributes extends PresentationFinalAttributes<AdsEditorPresentationDef> {

    public final AdsObjectTitleFormatDef finalTitleFormat;
    public final EditorPages finalEditorPages;

    public EditorPresentationFinalAttributes(AdsEditorPresentationDef def) {
        this(def, collectOverwrittenPresentations(def));
    }

    private EditorPresentationFinalAttributes(AdsEditorPresentationDef def, Collection<AdsEditorPresentationDef> overwritten) {
        super(def, overwritten);

        AdsObjectTitleFormatDef titleFormat = null;

        EditorPages pages = null;
        if (def.isEditorPagesInherited()) {
            for (AdsEditorPresentationDef epr : overwritten) {
                if (!epr.isEditorPagesInherited()) {
                    pages = epr.getEditorPages();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.PAGES);
                    break;
                }
            }
        } else {
            pages = def.getEditorPages();
        }
        finalEditorPages = pages;

        if (def.isObjectTitleFormatInherited()) {
            for (AdsEditorPresentationDef epr : overwritten) {
                if (!epr.isObjectTitleFormatInherited()) {
                    titleFormat = epr.getObjectTitleFormat();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT);
                    break;
                }
            }
        } else {
            titleFormat = def.getObjectTitleFormat();
        }
        finalTitleFormat = titleFormat;

        if (def.isPropertyPresentationAttributesInherited()) {
            for (final AdsEditorPresentationDef over : overwritten) {
                if (!over.isPropertyPresentationAttributesInherited()) {
                    finalInheritanceMask.remove(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
                    break;
                }
            }
        }
    }
}

class SelectorPresentationFinalAttributes extends PresentationFinalAttributes<AdsSelectorPresentationDef> {

    public final SelectorAddons finalAddons;
    public final AdsCondition finalCondition;
    public final RadixObjects<SelectorColumn> finalColumns;
    public final Id finalCreationClassCatalogId;

    public SelectorPresentationFinalAttributes(AdsSelectorPresentationDef def) {
        this(def, collectOverwrittenPresentations(def));
    }

    private SelectorPresentationFinalAttributes(AdsSelectorPresentationDef def, Collection<AdsSelectorPresentationDef> overwritten) {
        super(def, overwritten);
        SelectorAddons addons = null;
        if (def.isAddonsInherited()) {
            for (AdsSelectorPresentationDef spr : overwritten) {
                if (!spr.isAddonsInherited()) {
                    addons = spr.getAddons();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.ADDONS);
                    break;
                }
            }
        } else {
            addons = def.getAddons();
        }
        finalAddons = addons;

        RadixObjects<SelectorColumn> columns = null;
        if (def.isColumnsInherited()) {
            for (AdsSelectorPresentationDef spr : overwritten) {
                if (!spr.isColumnsInherited()) {
                    columns = spr.getColumns();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.COLUMNS);
                    break;
                }
            }
        } else {
            columns = def.getColumns();
        }
        finalColumns = columns;

        AdsCondition condition = null;
        if (def.isConditionInherited()) {
            for (AdsSelectorPresentationDef spr : overwritten) {
                if (!spr.isConditionInherited()) {
                    condition = spr.getCondition();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.CONDITION);
                    break;
                }
            }
        } else {
            condition = def.getCondition();
        }
        finalCondition = condition;

        Id id = null;
        if (def.isCreationClassCatalogInherited()) {
            for (AdsSelectorPresentationDef spr : overwritten) {
                if (!spr.isCreationClassCatalogInherited()) {
                    id = spr.getCreationClassCatalogId();
                    finalInheritanceMask.remove(EPresentationAttrInheritance.CLASS_CATALOG);
                    break;
                }
            }
        } else {
            id = def.getCreationClassCatalogId();
        }
        finalCreationClassCatalogId = id;
    }
}
