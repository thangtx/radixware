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

package org.radixware.kernel.server.units;

import java.lang.reflect.InvocationTargetException;

import java.math.BigDecimal;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.instance.Instance;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.exceptions.UnsupportedUnitTypeException;
import org.radixware.kernel.server.utils.KernelLayers;


public final class Factory {

    public static final String LAYER_UNITS_FACTORY_CLASS_NAME = "server.units.LayerUnitFactory";

    private Factory() {
    }

    public static Unit newUnit(final Instance instance, final Long unitType, final Long id, final String title) throws UnsupportedUnitTypeException {
        for (final Class<?> c : KernelLayers.classListFromTopToBottomLayer(LAYER_UNITS_FACTORY_CLASS_NAME)) {
            try {
                final Unit unit = ((ILayerUnitFactory) c.newInstance()).newUnit(instance, unitType, id, title);
                if (!Utils.equals(unitType, unit.getUnitType())) {
                    throw new RadixError("Unit factory error: request unit type is " + String.valueOf(unitType) + " but got " + String.valueOf(unit.getUnitType()));
                }
                return unit;
            } catch (Throwable e) {
                Throwable ex = e;
                if (e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() != null) {
                    ex = ((InvocationTargetException) e).getTargetException();
                }
                if (ex instanceof UnsupportedUnitTypeException) {
                    continue;
                }
                throw new RadixError("Can't load unit class: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
            }
        }
        throw new UnsupportedUnitTypeException(unitType, "Unsupported unit type " + String.valueOf(unitType));
    }

    public static Map<Long, BigDecimal> getUnitLoadOrderByType() {
        try {
            final HashMap<Long, BigDecimal> map = new HashMap<Long, BigDecimal>();
            for (final Class<?> c : KernelLayers.classListFromBottomToTopLayer(LAYER_UNITS_FACTORY_CLASS_NAME)) {
                final Map<Long, BigDecimal> layerOrder = ((ILayerUnitFactory) c.newInstance()).getUnitLoadOrderByType();
                if (layerOrder != null) {
                    map.putAll(layerOrder);
                }
            }
            return Collections.unmodifiableMap(map);
        } catch (Throwable e) {
            if (e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() != null) {
                e = ((InvocationTargetException) e).getTargetException();
            }
            throw new RadixError("Can't determinate unit load order: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }

    public static String getUnitLoadOrderBySql(final String unitTypeColumnAlias) {
        final StringBuilder decode = new StringBuilder("order by decode(");
        decode.append(unitTypeColumnAlias);
        for (Map.Entry<Long, BigDecimal> e : getUnitLoadOrderByType().entrySet()) {
            decode.append(',');
            decode.append(String.valueOf(e.getKey()));
            decode.append(',');
            decode.append(String.valueOf(e.getValue()));
        }
        decode.append(", NULL) nulls last");
        return decode.toString();
    }
}
