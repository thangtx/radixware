/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.dialogs.stack;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.src.SrcPositionLocator;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

/**
 *
 * @author avoloshchuk
 */
public class StackTreeModelItem {

    boolean isStackString = false;
    int lineNumber = -1;
    String text;
    List<SrcPositionLocator.SrcLocation> locations = new LinkedList<>();
    ERuntimeEnvironmentType environment;
    List<Definition> defs = new LinkedList<>();

    @Override
    public String toString() {
        if (!locations.isEmpty()) {
            String name = "";
            for (SrcPositionLocator.SrcLocation location : locations) {
                name += location.getRadixObject().getQualifiedName() + "; ";
            }
            return name.substring(0, name.length() - 2);
        } else {
            return text;
        }
    }
}
