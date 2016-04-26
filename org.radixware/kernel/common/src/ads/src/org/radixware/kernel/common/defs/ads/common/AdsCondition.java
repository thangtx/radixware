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

package org.radixware.kernel.common.defs.ads.common;

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.src.AdsConditionWriter;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.ads.AdsSqmlEnvironment;
import org.radixware.schemas.adsdef.SqmlCondition;


public class AdsCondition extends RadixObject implements IJavaSource {

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsConditionWriter(this, AdsCondition.this, purpose);
            }
        };
    }

    public static final class Factory {

        public static AdsCondition loadFrom(AdsDefinition owner, SqmlCondition condition) {
            return new AdsCondition(owner, condition);
        }

        public static AdsCondition newInstance(AdsDefinition owner) {
            return new AdsCondition(owner);
        }
    }
    AdsSqmlEnvironment env = AdsSqmlEnvironment.Factory.newInstance(this);

    private final class ConditionSqml extends Sqml {

        public ConditionSqml(String name) {
            super(AdsCondition.this);
            setEnvironment(env);
        }
    }
    private final Sqml from = new ConditionSqml("From");
    private final Sqml where = new ConditionSqml("Where");
    private final Prop2ValueMap prop2ValueMap;

    private AdsCondition(AdsDefinition owner, SqmlCondition xCondition) {
        this.setContainer(owner);
        if (xCondition != null) {
            from.loadFrom(xCondition.getConditionFrom());
            where.loadFrom(xCondition.getConditionWhere());
            prop2ValueMap = new Prop2ValueMap(this, xCondition.getProp2ValueCondition());
        } else {
            prop2ValueMap = new Prop2ValueMap(this, null);
        }
    }

    private AdsCondition(AdsDefinition owner) {
        this.setContainer(owner);
        prop2ValueMap = new Prop2ValueMap(this, null);
    }

//    private AdsCondition(AdsDefinition owner, AdsCondition source) {
//        this.setContainer(owner);
//        prop2ValueMap = new Prop2ValueMap(this, null);
//    }
    public void changeContainer(RadixObject container) {
        setContainer(null);
        setContainer(container);
    }

    public void appendTo(SqmlCondition xDef) {
        this.from.appendTo(xDef.addNewConditionFrom());
        this.where.appendTo(xDef.addNewConditionWhere());
        if (!this.prop2ValueMap.getItems().isEmpty()) {
            this.prop2ValueMap.appendTo(xDef.addNewProp2ValueCondition());
        }
    }

    public boolean isEmpty() {
        return from.getItems().isEmpty() && where.getItems().isEmpty() && prop2ValueMap.getItems().isEmpty();
    }

    public Sqml getFrom() {
        return from;
    }

    public Sqml getWhere() {
        return where;
    }

    public Prop2ValueMap getProp2ValueMap() {
        return prop2ValueMap;
    }

    private class Unmodifiable extends AdsCondition {

        private AdsCondition source;

        Unmodifiable(AdsDefinition owner, AdsCondition source) {
            super(owner);
            this.source = source;
        }

        @Override
        public Sqml getFrom() {
            return source.getFrom();
        }

        @Override
        public Sqml getWhere() {
            return source.getWhere();
        }

        @Override
        public Prop2ValueMap getProp2ValueMap() {
            return source.getProp2ValueMap(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public AdsCondition unmodifiableInstance(AdsDefinition owner) {
        return new Unmodifiable(owner, this);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (from != null) {
            from.visit(visitor, provider);
        }
        if (where != null) {
            where.visit(visitor, provider);
        }
        prop2ValueMap.visit(visitor, provider);
    }
}
