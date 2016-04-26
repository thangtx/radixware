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

import java.util.List;
import java.util.ArrayList;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsMergeObject extends AdsBaseObject {

    private int m = 1;
    private int n;
    private int ec = 1;
    
    protected AdsMergeObject() {
        this(ObjectFactory.createNodeId(), EMPTY_NAME);
    }

    protected AdsMergeObject(Id id, String name) {
        super(Kind.MERGE, id, EMPTY_NAME);
        pins.add(new AdsPin());
        pins.add(new AdsPin());
    }
    
    protected AdsMergeObject(final AdsMergeObject node) {
        super(node);
        this.m = node.m;
        this.n = node.n;
        this.ec = node.ec;
    }

    protected AdsMergeObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        m = xNode.getM();
        n = xNode.getN();
        ec = pins.size()/2;
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return pins.indexOf(pin) >= pins.size()/2;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return pins.indexOf(pin) >= 0 && pins.indexOf(pin) < pins.size()/2;
    }
    
    @Override
    public void sync() {
        AdsPage page = getOwnerPage();
        List<AdsPin> pinList = getPins();
        
        if (ec == pinList.size()/2)
            return;

        List<AdsPin> entries = new ArrayList<AdsPin>(pinList.subList(0, pinList.size()/2));
        List<AdsPin> leaves = new ArrayList<AdsPin>(pinList.subList(pinList.size()/2, pinList.size()));

        if (ec < pinList.size()/2) {
            for (int i=entries.size()-1; i>ec-1; i--) {
                AdsPin pin = entries.get(i);
                AdsDefinitions<AdsEdge> edges = page.getEdges();
                for (int j=0; j<edges.size(); j++) {
                    AdsEdge edge = edges.get(j);
                    if (pin.equals(edge.getSource()) || pin.equals(edge.getTarget()))
                        page.remove(edge);
                }
                entries.remove(pin);
            }
            for (int i=leaves.size()-1; i>ec-1; i--) {
                AdsPin pin = leaves.get(i);
                AdsDefinitions<AdsEdge> edges = page.getEdges();
                for (int j=0; j<edges.size(); j++) {
                    AdsEdge edge = edges.get(j);
                    if (pin.equals(edge.getSource()) || pin.equals(edge.getTarget()))
                        page.remove(edge);
                }
                leaves.remove(pin);
            }
        }
        else {
            for (int i=entries.size(); i<ec; i++)
                entries.add(new AdsPin());
            for (int i=leaves.size(); i<ec; i++)
                leaves.add(new AdsPin());
        }

        pinList.clear();
        pinList.addAll(entries);
        pinList.addAll(leaves);
        setPins(pinList);
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        if (!Utils.equals(this.m, m)) {
            this.m = m;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        if (!Utils.equals(this.n, n)) {
            this.n = n;
            setEditState(EEditState.MODIFIED);
        }
    }

    public int getEC() {
        return ec;
    }

    public void setEC(int ec) {
        if (!Utils.equals(this.ec, ec)) {
            this.ec = ec;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode,ESaveMode saveMode) {
        super.appendTo(xNode,saveMode);
        xNode.setM(m);
        xNode.setN(n);
    }

    private static final String TYPE_TITLE = "Merge Node";
    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}