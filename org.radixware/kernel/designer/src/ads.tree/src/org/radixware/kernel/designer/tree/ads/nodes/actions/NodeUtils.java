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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.lang.reflect.Field;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;


public class NodeUtils {

    public static Node getOriginalNode(Node node) {
        if (node instanceof FilterNode) {
            Field origNode;
            try {
                origNode = FilterNode.class.getDeclaredField("original");
                origNode.setAccessible(true);
                node = (Node) origNode.get(node);
                if (node instanceof FilterNode) {
                    return getOriginalNode(node);
                } else {
                    return node;
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        } else {
            return node;
        }
    }
}
