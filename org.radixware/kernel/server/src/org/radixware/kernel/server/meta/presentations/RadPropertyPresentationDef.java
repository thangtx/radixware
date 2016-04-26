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

import java.util.EnumSet;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropAttrInheritance;


public class RadPropertyPresentationDef {

    private final Id propId;
    private final EEditPossibility editPossibility;
    private final boolean readSeparately;
    private final EnumSet<EPropAttrInheritance> inheritanceMask;
    private final boolean isNotNull;
    private RadPresentationDef presentation = null;
    private RadClassPresentationDef classPresDef = null;

    public RadPropertyPresentationDef(
            final Id propId,
            final EEditPossibility editPossibility,
            final boolean isNotNull,
            final boolean readSeparately,
            final EnumSet<EPropAttrInheritance> inheritanceMask) {
        this.inheritanceMask = inheritanceMask;
        this.propId = propId;
        //this.editMask   		= editMask;
        this.isNotNull = isNotNull;
        this.editPossibility = editPossibility;
        this.readSeparately = readSeparately;
    }

    protected final RadPropertyPresentationDef getPropPres(RadClassPresentationDef classPres, final EPropAttrInheritance ihertBit) {
        final RadPropertyPresentationDef propPres;
        if (presentation != null) {
            propPres = presentation.getPropPres(classPres, getPropId(), ihertBit);
        } else {
            if (classPres == null) {
                classPres = classPresDef;
            }
            propPres = classPres.getPropPres(getPropId(), ihertBit);
        }
        if (propPres == null) {
            return null;
        }
        return propPres;
    }

    final void link(final RadPresentationDef pres) {
        presentation = pres;
    }

    final void link(@SuppressWarnings("hiding") final RadClassPresentationDef entityDef) {
        this.classPresDef = entityDef;
    }

    /**
     * @return the propId
     */
    public Id getPropId() {
        return propId;
    }

    /**
     * @return the editPossibility
     */
    public EEditPossibility getEditPossibility(final RadClassPresentationDef classPres) {
        final RadPropertyPresentationDef propPres = getPropPres(classPres, EPropAttrInheritance.EDITING);
        if (propPres != null) {
            return propPres.editPossibility;
        }
        return EEditPossibility.ALWAYS;
    }

    /**
     * @return the readSeparately
     */
    boolean isReadSeparately(final RadClassPresentationDef classPres) {
        final RadPropertyPresentationDef propPres = getPropPres(classPres, EPropAttrInheritance.EDITING);
        if (propPres != null) {
            return propPres.readSeparately;
        }
        return false;
    }

    /**
     * @return the inheritanceMask
     */
    public EnumSet<EPropAttrInheritance> getInheritanceMask() {
        return inheritanceMask;
    }

    /**
     * @return the isNotNull
     */
    public boolean isIsNotNull(final RadClassPresentationDef classPres) {
        final RadPropertyPresentationDef propPres = getPropPres(classPres, EPropAttrInheritance.EDITING);
        if (propPres != null) {
            return propPres.isNotNull;
        }
        return false;
    }
}
