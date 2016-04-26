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

package org.radixware.kernel.common.utils;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import org.radixware.kernel.common.types.ArrNum;

public final class ValueFormatter {
//Constructor

    private ValueFormatter() {
    }
//Public methods

    /**
     * see RADIX-3171
     *
     * @param address - Remote host address, colon, port, less, local address,
     * colon, port. Sample: "123.123.123.123:1234&lt;127.0.01:1234"
     * @return
     */
    public static CompositeInetSocketAddress parseCompositeInetSocketAddress(final String address) {
        if (address == null)
            return null;
        
        final int delimPos = address.indexOf('<');
        final int len = address.length();
        if (len == 0) {
            throw new IllegalArgumentException("Invalid composite address: '" + address + "'");
        }
        if (delimPos < 0) {
            return new CompositeInetSocketAddress(
                    parseInetSocketAddress(address),
                    null);
            //throw new IllegalArgumentException( "Invalid composite address: '" + address + "'" );
        } else {
            return new CompositeInetSocketAddress(
                    parseInetSocketAddress(address.substring(0, delimPos)),
                    parseInetSocketAddress(address.substring(delimPos + 1)));
        }
    }

    public static InetSocketAddress parseInetSocketAddress(final String hostColonPort) {
        return parseInetSocketAddress(hostColonPort, 0);
    }

    /**
     *
     * @param hostColonPort - Host address, colon, port. Sample like
     * "127.0.01:1234"
     * @param portOffset -
     * @Deprecated("Use method without this parameter").
     * @return
     * @deprecated
     */
    @Deprecated
    public static InetSocketAddress parseInetSocketAddress(final String hostColonPort, final int portOffset) {
        if (hostColonPort == null)
            return null;
        
        final int len = hostColonPort.length();
        if (len == 0) {
            throw new IllegalArgumentException("Invalid address: '" + hostColonPort + "'");
        }
        final int portNo;
        final int colonPos = hostColonPort.lastIndexOf(':');
        if (colonPos < 0) {
            portNo = 0;// RADIX-3612
        } else {
            try {
                portNo = Integer.parseInt(hostColonPort.substring(colonPos + 1, len));
            } catch (NumberFormatException numberFormatException) {
                throw new IllegalArgumentException("Invalid address: '" + hostColonPort + "'", numberFormatException);
            }
        }
        return new InetSocketAddress(
                /*
                 * Host
                 */colonPos < 0 ? hostColonPort : hostColonPort.substring(0, colonPos),
                /*
                 * Port
                 */ portNo + portOffset);
    }
    
    public static BigDecimal normalizeBigDecimal(final BigDecimal value){
        if (value==null){
            return null;
        }else if (value.scale()==0){            
            return value;
        }else if (BigDecimal.ZERO.compareTo(value)==0){
            //workaround of http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6480539
            return BigDecimal.ZERO;
        }else if (value.scale()<0){
            return value.setScale(0);
        }else{
            final BigDecimal strippedValue = value.stripTrailingZeros();
            return strippedValue.scale()<0 ? strippedValue.setScale(0) : strippedValue;
        }
    }
    
    public static ArrNum normalizeArrNum(final ArrNum arr){
        if (arr==null){
            return null;
        }else{
            final ArrNum result = new ArrNum(arr.size());
            for (BigDecimal value: arr){
                result.add(normalizeBigDecimal(value));
            }
            return result;
        }
    }
}
