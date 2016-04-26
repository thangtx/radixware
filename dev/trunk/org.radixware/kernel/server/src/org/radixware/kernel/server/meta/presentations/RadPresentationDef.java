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

package org.radixware.kernel.server.meta.presentations;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEditPossibility;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.types.Restrictions;


public abstract class RadPresentationDef extends RadDefinition {

    static final private Id USER_FUNC_TAB_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");
    static final private Restrictions USER_FUNC_DEV_RESTR = Restrictions.Factory.newInstance(ERestriction.UPDATE.getValue().longValue() | ERestriction.CREATE.getValue().longValue() | ERestriction.DELETE.getValue().longValue() | ERestriction.DELETE_ALL.getValue().longValue() | ERestriction.ANY_COMMAND.getValue().longValue(), null, null, null);

    abstract public Collection<RadPropDef> getUsedPropDefs(final RadClassPresentationDef classPres);
    private final Id basePresentationId;
    private final int inheritanceMask;
    private RadClassPresentationDef classPres;
    private Map<Id, RadPropertyPresentationDef> propPresentationsById;
    private Restrictions restrictions;
    private boolean restrictionIsLiked = false;

    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param basePresentationId
     * @param inheritanceMask
     * @param dbuProps
     */
    public RadPresentationDef(
            final Id id,
            final String name,
            final Id basePresentationId,
            final int inheritanceMask,
            final RadPropertyPresentationDef[] propPresentations,//not used with current specification
            final Restrictions restrictions) {
        super(id, name);
        if (((inheritanceMask & CInheritance.RESTRICTION) == 0) || basePresentationId == null) {
            this.restrictions = restrictions != null ? restrictions : Restrictions.ZERO;
        } else {
            this.restrictions = null;
        }
        this.basePresentationId = basePresentationId;
        this.inheritanceMask = inheritanceMask;
        if (propPresentations == null) {
            this.propPresentationsById = Collections.emptyMap();
        } else {
            this.propPresentationsById = new HashMap<Id, RadPropertyPresentationDef>(propPresentations.length * 2 + 1);
            for (RadPropertyPresentationDef propPres : propPresentations) {
                this.propPresentationsById.put(propPres.getPropId(), propPres);
                propPres.link(this);
            }
            this.propPresentationsById = Collections.unmodifiableMap(this.propPresentationsById);
        }
    }

    @Override
    public void link() {
        super.link();
        linkRestr();
    }
    
    /**
     * @return the basePresentationId
     */
    protected Id getBasePresentationId() {
        return basePresentationId;
    }

    /**
     * @return the inheritanceMask
     */
    protected int getInheritanceMask() {
        return inheritanceMask;
    }

    public final Restrictions getDefRestrictions() {
        if ((this.getInheritanceMask() & CInheritance.RESTRICTION) == 0 || getBasePresentationId() == null) {
            linkRestr();
            return restrictions;
        } else {
            return getBasePresentation().getDefRestrictions();
        }
    }

    private void linkRestr() {
        if (restrictionIsLiked) {
            return;
        }
        final Restrictions classRestrictions = getClassPresentation().getDefRestrictions();
        restrictions = Restrictions.Factory.sum(restrictions, classRestrictions);
        restrictionIsLiked = true;
    }

    protected void link(final RadClassPresentationDef classPresentation) {
        this.classPres = classPresentation;
    }

    /**
     *
     * @param arte
     * @param classPres - презентация класса объекта передается для реализации
     * виртуальности (презентация наследует настройки свойства от класса
     * объекта, а не от класса презентации; в общем случае класс объекта -
     * потомок класса презентации).
     * @param propId
     * @param inherBit
     * @return
     */
    final RadPropertyPresentationDef getPropPres(RadClassPresentationDef classPres, final Id propId, final EPropAttrInheritance inherBit) {
        final RadPropertyPresentationDef propPres = propPresentationsById.get(propId);
        if (propPres != null && !propPres.getInheritanceMask().contains(inherBit)) //собственное презентационное свойство
        {
            return propPres;
        }
        if (classPres == null) {
            classPres = this.getClassPresentation();
        }
        if (getBasePresentation() != null) {
            return getBasePresentation().getPropPres(classPres, propId, inherBit);
        } else {
            return classPres.getPropPres(propId, inherBit);
        }
    }

    public final Id getPropClassCatalogIdByPropId(final RadClassPresentationDef classPres, final Id propId) {
        final RadPropertyPresentationDef propPres = getPropPresById(classPres, propId);
        if (propPres instanceof RadParentTitlePropertyPresentationDef) {
            return ((RadParentTitlePropertyPresentationDef) propPres).getParentClassCatalogId(classPres);
        }
        return null;
    }

    public final boolean getPropIsNotNullByPropId(final RadClassPresentationDef classPres, final Id propId) {
        final RadPropertyPresentationDef propPres = getPropPresById(classPres, propId);
        if (propPres != null) {
            return propPres.isIsNotNull(classPres);
        }
        return false;
    }

    public EEditPossibility getPropEditPossibilityByPropId(final RadClassPresentationDef classPres, final Id propId) {
        final RadPropertyPresentationDef propPres = getPropPresById(classPres, propId);
        if (propPres != null) {
            return propPres.getEditPossibility(classPres);
        }
        return EEditPossibility.ALWAYS;
    }

    public RadEntityTitleFormatDef getPropParentTitleFormat(final RadClassPresentationDef classPres, final Id propId) {
        final RadPropertyPresentationDef propPres = getPropPres(classPres, propId, EPropAttrInheritance.PARENT_TITLE_FORMAT);
        if (propPres instanceof RadParentTitlePropertyPresentationDef) {
            return ((RadParentTitlePropertyPresentationDef) propPres).getTitleFormat(classPres);
        }
        return null;
    }

    public final boolean getPropIsReadSeparatelyByPropId(final RadClassPresentationDef classPres, final Id propId) {
        final RadPropertyPresentationDef propPres = getPropPresById(classPres, propId);
        if (propPres != null) {
            return propPres.isReadSeparately(classPres);
        }
        return false;
    }
//Protected abstract method

    protected abstract RadPresentationDef getBasePresentation();

    /**
     * @param classPres - презентация класса объекта передается для реализации
     * виртуальности (презентация наследует настройки свойства от класса
     * объекта, а не от класса презентации; в общем случае класс объекта -
     * потомок класса презентации).
     * @param propId
     * @return
     */
    public RadPropertyPresentationDef getPropPresById(RadClassPresentationDef classPres, final Id propId) {
        RadPropertyPresentationDef propPres = propPresentationsById.get(propId);
        if (classPres == null) {
            classPres = getClassPresentation();
        }
        if (propPres == null && getBasePresentation() != null) {
            propPres = getBasePresentation().getPropPresById(classPres, propId);
        }
        if (propPres == null) {
            propPres = classPres.getPropPresById(propId);
        }
        return propPres;
    }

    public RadClassPresentationDef getClassPresentation() {
        return classPres;
    }
    //private Map<Id, Restrictions> roleRestrById = null;

    final Map<Id, Restrictions> getRoleRestrById() {
        //  if (roleRestrById == null) {
        final Map<Id, Restrictions> roleRestrById = new HashMap<>();
        final Collection<RadRoleDef> roles = Arte.get().getDefManager().getRoleDefs();
        for (RadRoleDef role : roles) {
            final Restrictions restr = calcRoleRestr(role);
            if (!restr.getIsAccessRestricted()) {
                roleRestrById.put(role.getId(), restr);
            }
        }
        //roleRestrById = Collections.unmodifiableMap(roleRestrById);
        //}
        return Collections.unmodifiableMap(roleRestrById);
    }

    private final Restrictions getRoleRestrictions(final Id roleId) {
        final Restrictions r = getRoleRestrById().get(roleId);
        if (r == null) {
            return Restrictions.FULL;
        }
        return r;
    }

    public final Restrictions getTotalRestrictions(final List<Id> roleIds) {
        return Restrictions.Factory.sum(getDefRestrictions(), getAcsRestrictions(roleIds));
    }

    private final Restrictions getAcsRestrictions(final List<Id> roleIds) {
        Restrictions r = Restrictions.FULL;
        for (Id roleId : roleIds) {
            r = Restrictions.Factory.and(r, getRoleRestrictions(roleId));
        }
        final Arte contextArte = Arte.get();
        if (USER_FUNC_TAB_ID.equals(getClassPresentation().getClassDef().getEntityId())
                && contextArte != null && !contextArte.getRights().getCurUserCanAccess(EDrcServerResource.USER_FUNC_DEV)) {//RADIX-1340
            r = Restrictions.Factory.sum(r, USER_FUNC_DEV_RESTR);
        }
        return r;
    }

    abstract protected Restrictions calcRoleRestr(RadRoleDef role);

    static public final class CInheritance {

        static public final int RESTRICTION = 32;
        static public final int ICON = 16;
        static public final int TITLE = 128;
    }
}
