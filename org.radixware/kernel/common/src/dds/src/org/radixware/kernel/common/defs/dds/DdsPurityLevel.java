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

/**
 * Purity level.
 * Набор из purity level задается PL/SQL пакету, типу или функции.
 * Используется при генерации скрипта создания PL/SQL объекта для оптимизаций в Oracle.
 */
public abstract class DdsPurityLevel {

    /**
     * Rean No Database State.
     * Не производится операций чтения данных из таблиц базы данных.
     */
    private boolean RNDS = false;

    public boolean isRNDS() {
        return RNDS;
    }

    public void setRNDS(boolean RNDS) {
        if (this.RNDS != RNDS) {
            this.RNDS = RNDS;
            onChanged();
        }
    }
    /**
     * Rean No Parameters State.
     * Не производится операций чтения переменных пакета.
     */
    private boolean RNPS = false;

    public boolean isRNPS() {
        return RNPS;
    }

    public void setRNPS(boolean RNPS) {
        if (this.RNPS != RNPS) {
            this.RNPS = RNPS;
            onChanged();
        }
    }
    /**
     * Write No Package State.
     * Не производится изменений переменных пакета.
     */
    private boolean WNDS = false;

    public boolean isWNDS() {
        return WNDS;
    }

    public void setWNDS(boolean WNDS) {
        if (this.WNDS != WNDS) {
            this.WNDS = WNDS;
            onChanged();
        }
    }
    /**
     * Write No Package State.
     * Не производится изменений переменных пакета.
     */
    private boolean WNPS = false;

    public boolean isWNPS() {
        return WNPS;
    }

    public void setWNPS(boolean WNPS) {
        if (this.WNPS != WNPS) {
            this.WNPS = WNPS;
            onChanged();
        }
    }
    /**
     * To take on trust.
     * Верить на слово.
     */
    private boolean trust = false;

    public boolean isTrust() {
        return trust;
    }

    public void setTrust(boolean Trust) {
        if (this.trust != Trust) {
            this.trust = Trust;
            onChanged();
        }
    }

    protected abstract void onChanged();

    void loadFromBitMask(int bitmask) {
        setWNDS((bitmask & 1) != 0);
        setRNDS((bitmask & 2) != 0);
        setWNPS((bitmask & 4) != 0);
        setRNPS((bitmask & 8) != 0);
        setTrust((bitmask & 16) != 0);
    }

    int toBitMask() {
        int bitmask = 0;
        if (isWNDS()) {
            bitmask |= 1;
        }
        if (isRNDS()) {
            bitmask |= 2;
        }
        if (isWNPS()) {
            bitmask |= 4;
        }
        if (isRNPS()) {
            bitmask |= 8;
        }
        if (isTrust()) {
            bitmask |= 16;
        }
        return bitmask;
    }
}
