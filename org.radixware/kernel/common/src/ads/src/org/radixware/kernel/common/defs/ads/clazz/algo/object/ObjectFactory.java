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

package org.radixware.kernel.common.defs.ads.clazz.algo.object;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject.Prop;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject.Kind;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.schemas.algo.ScopeDef;


public class ObjectFactory {

    public static final <T extends AdsDefinition> AdsDefinitions<T> createList(RadixObject owner) {
        return new AdsDefinitions<T>(owner) {};
    }

// pages
    public static AdsPage createPage(final AdsDefinition owner) {
        return new AdsPage(owner);
    }

    public static AdsPage createPage(final ScopeDef xPage, final AdsDefinition owner) {
        return new AdsPage(xPage, owner);
    }

    public static AdsPage createPage(final AdsPage page, final AdsDefinition owner) {
        return new AdsPage(page, owner);
    }

// nodes
    public static AdsBaseObject createNode(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
       AdsBaseObject node = null;
       Kind kind = Kind.getForValue(xNode.getKind());
       switch (kind) {
           case START:
               node = new AdsStartObject(xNode);
               break;
           case CATCH:
               node = new AdsCatchObject(xNode);
               break;
           case EMPTY:
               node = new AdsEmptyObject(xNode);
               break;
           case FINISH:
               node = new AdsFinishObject(xNode);
               break;
           case FORK:
               node = new AdsForkObject(xNode);
               break;
           case INCLUDE:
               node = new AdsIncludeObject(xNode);
               break;
           case MERGE:
               node = new AdsMergeObject(xNode);
               break;
           case NOTE:
               node = new AdsNoteObject(xNode);
               break;
           case PROGRAM:
               node = new AdsProgramObject(xNode);
               break;
           case RETURN:
               node = new AdsReturnObject(xNode);
               break;
           case SCOPE:
               node = new AdsScopeObject(xNode);
               break;
           case TERMINATE:
               node = new AdsTermObject(xNode);
               break;
           case THROW:
               node = new AdsThrowObject(xNode);
               break;
           case APP:
               node = new AdsAppObject(xNode);
               break;
           case VAR:
               node = new AdsVarObject(xNode);
               break;
           default:
               assert false : "Unknown node type = " + xNode.getKind();
       }

       return node;
    }

    public static AdsBaseObject createNode(Kind kind, String clazz, Point location) {
        AdsBaseObject node = null;
        switch (kind) {
           case START:
               node = new AdsStartObject();
               break;
           case CATCH:
               node = new AdsCatchObject();
               break;
           case EMPTY:
               node = new AdsEmptyObject();
               break;
           case FINISH:
               node = new AdsFinishObject();
               break;
           case FORK:
               node = new AdsForkObject();
               break;
           case INCLUDE:
               node = new AdsIncludeObject();
               break;
           case MERGE:
               node = new AdsMergeObject();
               break;
           case NOTE:
               node = new AdsNoteObject();
               break;
           case PROGRAM:
               node = new AdsProgramObject();
               break;
           case RETURN:
               node = new AdsReturnObject();
               break;
           case SCOPE:
               node = new AdsScopeObject();
               break;
           case TERMINATE:
               node = new AdsTermObject();
               break;
           case THROW:
               node = new AdsThrowObject();
               break;
           case APP:
               node = new AdsAppObject(clazz);
               break;
           case VAR:
               node = new AdsVarObject();
               break;
           default:
               assert false : "Unknown node type = " + kind;
       }
       node.setBounds(new Rectangle(location.x, location.y, -1, -1));
       return node;
    }

    public static AdsBaseObject createNode(final AdsBaseObject n) {
        AdsBaseObject node = null;
        switch (n.getKind()) {
           case START:
               node = new AdsStartObject((AdsStartObject)n);
               break;
           case CATCH:
               node = new AdsCatchObject((AdsCatchObject)n);
               break;
           case EMPTY:
               node = new AdsEmptyObject((AdsEmptyObject)n);
               break;
           case FINISH:
               node = new AdsFinishObject((AdsFinishObject)n);
               break;
           case FORK:
               node = new AdsForkObject((AdsForkObject)n);
               break;
           case INCLUDE:
               node = new AdsIncludeObject((AdsIncludeObject)n);
               break;
           case MERGE:
               node = new AdsMergeObject((AdsMergeObject)n);
               break;
           case NOTE:
               node = new AdsNoteObject((AdsNoteObject)n);
               break;
           case PROGRAM:
               node = new AdsProgramObject((AdsProgramObject)n);
               break;
           case RETURN:
               node = new AdsReturnObject((AdsReturnObject)n);
               break;
           case SCOPE:
               node = new AdsScopeObject((AdsScopeObject)n);
               break;
           case TERMINATE:
               node = new AdsTermObject((AdsTermObject)n);
               break;
           case THROW:
               node = new AdsThrowObject((AdsThrowObject)n);
               break;
           case APP:
               node = new AdsAppObject((AdsAppObject)n);
               break;
           case VAR:
               node = new AdsVarObject((AdsVarObject)n);
               break;
           default:
               assert false : "Unknown node type = " + node.getKind();
        }

        return node;
    }

// edges
    public static AdsEdge createEdge(org.radixware.schemas.algo.ScopeDef.Edges.Edge xEdge) {
        return new AdsEdge(xEdge);
    }

    public static AdsEdge createEdge(AdsPin source, AdsPin target) {
        return new AdsEdge(source, target);
    }

    public static AdsEdge createEdge(final AdsEdge edge, AdsPin source, AdsPin target) {
        return new AdsEdge(edge, source, target);
    }
        
// pins
    public static AdsPin createPin() {
        return new AdsPin();
    }

    public static AdsPin createPin(String iconName) {
        AdsPin pin = new AdsPin();
        pin.setIconName(iconName);
        return pin;
    }

    public static AdsPin createPin(Id id) {
        return new AdsPin(id);
    }

    public static AdsPin createPin(final AdsPin pin) {
        return new AdsPin(pin);
    }

    public static AdsPin createPin(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Pins.Pin xPin) {
        return new AdsPin(xPin);
    }

// ids
    public static Id createPageId(Id ownerId) {
        return Id.Factory.changePrefix(ownerId, EDefinitionIdPrefix.ALGO_PAGE);
    }

    public static Id createNodeId() {
        return Id.Factory.newInstance(EDefinitionIdPrefix.ALGO_NODE);
    }

    public static Id createEdgeId() {
        return Id.Factory.newInstance(EDefinitionIdPrefix.ALGO_EDGE);
    }

    public static Id createPinId() {
        return Id.Factory.newInstance(EDefinitionIdPrefix.ALGO_PIN);
    }

    public static Id createParamId() {
        return Id.Factory.newInstance(EDefinitionIdPrefix.ALGO_PARAMETER);
    }

    public static Id createBlockParamId() {
        return Id.Factory.newInstance(EDefinitionIdPrefix.ALGO_BLOCK_PARAMETER);
    }

    public static Id createGlobalId() {
        return Id.Factory.newInstance(EDefinitionIdPrefix.ALGO_GLOBAL_VAR);
    }

    public static Id getId(String id) {
        return Id.Factory.loadFrom(id);
    }

    final static Map<String, Set<Prop>> clazz2Prop = new HashMap<>();
    public static void registerProp(String clazz, Prop prop) {
        Set<Prop> props = clazz2Prop.get(clazz);
        if (props == null) {
            props = new HashSet<>();
            clazz2Prop.put(clazz, props);
        }
        props.add(prop);
    }    
}