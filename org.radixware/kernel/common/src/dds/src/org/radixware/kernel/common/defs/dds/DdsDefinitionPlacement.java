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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.RadixObject.EEditState;

/**
 * Информация о визуальном расположении {@link DdsDefinition DDS дефиниции} в виде прямоугольника на диаграмме {@link DdsModelDef модели}.
 */
public class DdsDefinitionPlacement {

    private int posX = 0;

    /**
     * Получить горизонтальную координату левого верхнего угла прямоугольника.
     */
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        if (this.posX != posX) {
            this.posX = posX;
            ownerDefinition.setEditState(EEditState.MODIFIED);
        }
    }
    private int posY = 0;

    /**
     * Получить вертикальную координату левого верхнего угла прямоугольника.
     */
    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        if (this.posY != posY) {
            this.posY = posY;
            ownerDefinition.setEditState(EEditState.MODIFIED);
        }
    }

    public final DdsDefinition getDefinition() {
        return ownerDefinition;
    }

    private final DdsDefinition ownerDefinition;

    protected DdsDefinitionPlacement(DdsDefinition ownerDefinition) {
        this.ownerDefinition = ownerDefinition;
    }

    protected DdsDefinitionPlacement(DdsDefinition ownerDefinition, org.radixware.schemas.ddsdef.Placement xPlacement) {
        this.ownerDefinition = ownerDefinition;
        this.posX = xPlacement.getPosX();
        this.posY = xPlacement.getPosY();
    }

    protected DdsDefinitionPlacement(DdsDefinition ownerDefinition, int x, int y) {
        this.ownerDefinition = ownerDefinition;
        this.posX = x;
        this.posY = y;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsDefinitionPlacement newInstance(DdsDefinition ownerDefinition) {
            return new DdsDefinitionPlacement(ownerDefinition);
        }

        public static DdsDefinitionPlacement loadFrom(DdsDefinition ownerDefinition, org.radixware.schemas.ddsdef.Placement xPlacement) {
            return new DdsDefinitionPlacement(ownerDefinition, xPlacement);
        }

        public static DdsDefinitionPlacement newInstance(DdsDefinition ownerDefinition, int x, int y) {
            return new DdsDefinitionPlacement(ownerDefinition, x, y);
        }
    }
}

