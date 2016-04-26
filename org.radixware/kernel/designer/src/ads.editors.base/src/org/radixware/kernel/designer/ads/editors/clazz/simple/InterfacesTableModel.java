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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


class InterfacesTableModel extends AbstractTableModel {

    private AdsClassDef adsClassDef;
    private boolean isTransparent = false;

    public InterfacesTableModel(AdsClassDef adsClassDef, boolean isTransparent) {
        super();
        this.adsClassDef = adsClassDef;
        this.isTransparent = isTransparent;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AdsTypeDeclaration interfaceDeclaration;
        if (isTransparent){
            interfaceDeclaration = getInterfaceType(rowIndex);
            if (interfaceDeclaration != null){
                if (interfaceDeclaration.isGeneric()){
                    return interfaceDeclaration.getName(adsClassDef);
                }
                AdsClassDef  interfaceClass = getInterface(interfaceDeclaration);
                if (interfaceClass != null){
                    return interfaceClass.getName();
                }
                return interfaceDeclaration.getQualifiedName();
            }
        } else {
            interfaceDeclaration = adsClassDef.getInheritance().getInerfaceRefList(EScope.LOCAL).get(rowIndex);
            if (interfaceDeclaration != null){
                if (interfaceDeclaration.isGeneric()){
                    return interfaceDeclaration.getName(adsClassDef);
                }
                AdsType rowType = interfaceDeclaration.resolve(adsClassDef).get();
                if (rowType != null &&
                    rowType instanceof AdsClassType.InterfaceType){
                    return (AdsClassType.InterfaceType) rowType;
                }
                return interfaceDeclaration.getQualifiedName();
            }
        }
         
        
        return "<Not Defined>";
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    @Override
    public int getRowCount() {
        if (isTransparent){
            return adsClassDef.getInheritance().getOwnAndPlatformInerfaceRefList(EScope.ALL).size();
        } else {
            return adsClassDef.getInheritance().getInerfaceRefList(EScope.LOCAL).size();
        }
    }

    public void addInterface(AdsTypeDeclaration adsTypeDeclaration) {
        adsClassDef.getInheritance().addSuperInterfaceRef(adsTypeDeclaration);
        fireTableDataChanged();
    }

    public AdsClassDef getInterface(int rowIndex) {
        if (isTransparent) return null;
        
        final AdsType type = adsClassDef.getInheritance().getInerfaceRefList(EScope.LOCAL).get(rowIndex).resolve(adsClassDef).get();
        return (type instanceof AdsClassType.InterfaceType)
                ? ((AdsClassType.InterfaceType) type).getSource()
                : null;
    }
    
    public void removeInterface(int index) {
        List<AdsTypeDeclaration> refList = adsClassDef.getInheritance().getInerfaceRefList(EScope.LOCAL);
        if (index > -1 && index < refList.size()){
            adsClassDef.getInheritance().removeSuperInterfaceRef(refList.get(index));
        }
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (isTransparent) {
            return getInterface(getInterfaceType(row)) != null;
        }
        
        return getInterface(row) != null;
    }
    
    public AdsClassDef getInterface(AdsTypeDeclaration adsTypeDeclaration){
        return TransparentClassUtils.findTransparentByType(adsClassDef, adsTypeDeclaration);
    }
    
    public AdsTypeDeclaration getInterfaceType(int rowIndex){
        return adsClassDef.getInheritance().getOwnAndPlatformInerfaceRefList(EScope.ALL).get(rowIndex);
    }

    public static final int INTERFACE_СOLUMN_INDEX = 0;
    private static final int COLUMNS_COUNT = 1;
}
