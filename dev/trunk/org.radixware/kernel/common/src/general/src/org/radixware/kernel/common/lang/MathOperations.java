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
package org.radixware.kernel.common.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

// order: bool, char, byte, short, int, long, float, long, double, BigDecimal
public class MathOperations {

    static public int compare(Character i, Character j) {
        if (i == null && j == null) {
            return 0;
        } else if (i != null) {
            return i.compareTo(j);
        } else {
            return j.compareTo(i);
        }
    }

    static public int compare(Character i, char j) {
        return compare(i, new Character(j));
    }

    static public int compare(char i, Character j) {
        return compare(new Character(i), j);
    }

    static public int compare(BigDecimal i, BigDecimal j) {
        return i.compareTo(j);
    }

    static public int compare(BigDecimal i, Byte j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, byte j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Byte i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(byte i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(BigDecimal i, Short j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, short j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Short i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(short i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(BigDecimal i, Character j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, char j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Character i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(char i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(BigDecimal i, Integer j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, int j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Integer i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(int i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(BigDecimal i, Long j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, long j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Long i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(long i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(BigDecimal i, Float j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, float j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Float i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(float i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(BigDecimal i, Double j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(BigDecimal i, double j) {
        return compare(i, new BigDecimal(j));
    }

    static public int compare(Double i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public int compare(double i, BigDecimal j) {
        return compare(new BigDecimal(i), j);
    }

    static public boolean equals(boolean i, Boolean j) {
        return j == null ? false : i == j.booleanValue();
    }

    static public boolean equals(Boolean i, boolean j) {
        return i == null ? false : i.booleanValue() == j;
    }

    static public boolean equals(Character i, char j) {
        return i == null ? false : i.charValue() == j;
    }

    static public boolean equals(char i, Character j) {
        return j == null ? false : j.charValue() == i;
    }

    @SuppressWarnings("null")
    static public boolean equals(Comparable<?> i, Comparable<?> j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.equals(j);
        }
    }

    static public boolean equals(Byte i, byte j) {
        return i == null ? false : i.byteValue() == j;
    }

    static public boolean equals(byte i, Byte j) {
        return j == null ? false : i == j.byteValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Byte i, Short j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.shortValue() == j.shortValue();
        }
    }

    static public boolean equals(Byte i, short j) {
        return i == null ? false : i.shortValue() == j;
    }

    static public boolean equals(short i, Byte j) {
        return j == null ? false : i == j.shortValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Byte i, Integer j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.intValue() == j.intValue();
        }
    }

    static public boolean equals(Byte i, int j) {
        return i == null ? false : i.intValue() == j;
    }

    static public boolean equals(int i, Byte j) {
        return j == null ? false : i == j.intValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Byte i, Long j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.longValue() == j.longValue();
        }
    }

    static public boolean equals(Byte i, long j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(long i, Byte j) {
        return j == null ? false : i == j.longValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Short i, Byte j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.shortValue() == j.shortValue();
        }
    }

    static public boolean equals(Short i, byte j) {
        return i == null ? false : i.shortValue() == j;
    }

    static public boolean equals(byte i, Short j) {
        return j == null ? false : i == j.shortValue();
    }

    static public boolean equals(Short i, short j) {
        return i == null ? false : i.shortValue() == j;
    }

    static public boolean equals(short i, Short j) {
        return j == null ? false : i == j.shortValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Short i, Integer j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.intValue() == j.intValue();
        }
    }

    static public boolean equals(Short i, int j) {
        return i == null ? false : i.intValue() == j;
    }

    static public boolean equals(int i, Short j) {
        return j == null ? false : i == j.intValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Short i, Long j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.longValue() == j.longValue();
        }
    }

    static public boolean equals(Short i, long j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(long i, Short j) {
        return j == null ? false : i == j.longValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Integer i, Byte j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.intValue() == j.intValue();
        }
    }

    static public boolean equals(Integer i, byte j) {
        return i == null ? false : i.intValue() == j;
    }

    static public boolean equals(byte i, Integer j) {
        return j == null ? false : i == j.intValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Integer i, Short j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.intValue() == j.intValue();
        }
    }

    static public boolean equals(Integer i, short j) {
        return i == null ? false : i.intValue() == j;
    }

    static public boolean equals(short i, Integer j) {
        return j == null ? false : i == j.intValue();
    }

    static public boolean equals(Integer i, int j) {
        return i == null ? false : i.intValue() == j;
    }

    static public boolean equals(int i, Integer j) {
        return j == null ? false : i == j.intValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Integer i, Long j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.longValue() == j.longValue();
        }
    }

    static public boolean equals(Integer i, long j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(long i, Integer j) {
        return j == null ? false : i == j.longValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Long i, Byte j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.longValue() == j.longValue();
        }
    }

    static public boolean equals(Long i, byte j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(byte i, Long j) {
        return j == null ? false : i == j.longValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Long i, Short j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.longValue() == j.longValue();
        }
    }

    static public boolean equals(Long i, short j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(short i, Long j) {
        return j == null ? false : i == j.longValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Long i, Integer j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.longValue() == j.longValue();
        }
    }

    static public boolean equals(Long i, int j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(int i, Long j) {
        return j == null ? false : i == j.longValue();
    }

    static public boolean equals(Long i, long j) {
        return i == null ? false : i.longValue() == j;
    }

    static public boolean equals(long i, Long j) {
        return j == null ? false : i == j.longValue();
    }

    static public boolean equals(Float i, float j) {
        return i == null ? false : i.floatValue() == j;
    }

    static public boolean equals(float i, Float j) {
        return j == null ? false : i == j.floatValue();
    }

    static public boolean equals(Timestamp i, Timestamp j) {
        if (i == null && j == null) {
            return true;
        }
        if (i != null && j != null) {
            return i.compareTo(j) == 0;
        }
        
        return false;
    }

    @SuppressWarnings("null")
    static public boolean equals(Float i, Double j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.doubleValue() == j.doubleValue();
        }
    }

    static public boolean equals(Float i, double j) {
        return i == null ? false : i.doubleValue() == j;
    }

    static public boolean equals(double i, Float j) {
        return j == null ? false : i == j.doubleValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(Double i, Float j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.doubleValue() == j.doubleValue();
        }
    }

    static public boolean equals(Double i, float j) {
        return i == null ? false : i.doubleValue() == j;
    }

    static public boolean equals(float i, Double j) {
        return j == null ? false : i == j.doubleValue();
    }

    static public boolean equals(Double i, double j) {
        return i == null ? false : i.doubleValue() == j;
    }

    static public boolean equals(double i, Double j) {
        return j == null ? false : i == j.doubleValue();
    }

    @SuppressWarnings("null")
    static public boolean equals(BigDecimal i, Byte j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.equals(new BigDecimal(j.byteValue()));
        }
    }

    static public boolean equals(BigDecimal i, byte j) {
        if (i == null) {
            return false;
        }
        return i.equals(new BigDecimal(j));
    }

    static public boolean equals(byte i, BigDecimal j) {
        if (j == null) {
            return false;
        }
        return new BigDecimal(i).equals(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, Byte j) {
        return i.add(new BigDecimal(j.byteValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, byte j) {
        return i.add(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(Byte i, BigDecimal j) {
        return new BigDecimal(i.byteValue()).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(byte i, BigDecimal j) {
        return new BigDecimal(i).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, Byte j) {
        return i.subtract(new BigDecimal(j.byteValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, byte j) {
        return i.subtract(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(Byte i, BigDecimal j) {
        return new BigDecimal(i.byteValue()).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(byte i, BigDecimal j) {
        return new BigDecimal(i).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, Byte j) {
        return i.multiply(new BigDecimal(j.byteValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, byte j) {
        return i.multiply(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(Byte i, BigDecimal j) {
        return new BigDecimal(i.byteValue()).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(byte i, BigDecimal j) {
        return new BigDecimal(i).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, Byte j) {
        return i.divide(new BigDecimal(j.byteValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, byte j) {
        return i.divide(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(Byte i, BigDecimal j) {
        return new BigDecimal(i.byteValue()).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(byte i, BigDecimal j) {
        return new BigDecimal(i).divide(j);
    }

    @SuppressWarnings("null")
    static public boolean equals(BigDecimal i, Short j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.equals(new BigDecimal(j.shortValue()));
        }
    }

    static public boolean equals(BigDecimal i, short j) {
        if (i == null) {
            return false;
        }
        return i.equals(new BigDecimal(j));
    }

    static public boolean equals(short i, BigDecimal j) {
        if (j == null) {
            return false;
        }
        return new BigDecimal(i).equals(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, Short j) {
        return i.add(new BigDecimal(j.shortValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, short j) {
        return i.add(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(Short i, BigDecimal j) {
        return new BigDecimal(i.shortValue()).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(short i, BigDecimal j) {
        return new BigDecimal(i).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, Short j) {
        return i.subtract(new BigDecimal(j.shortValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, short j) {
        return i.subtract(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(Short i, BigDecimal j) {
        return new BigDecimal(i.shortValue()).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(short i, BigDecimal j) {
        return new BigDecimal(i).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, Short j) {
        return i.multiply(new BigDecimal(j.shortValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, short j) {
        return i.multiply(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(Short i, BigDecimal j) {
        return new BigDecimal(i.shortValue()).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(short i, BigDecimal j) {
        return new BigDecimal(i).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, Short j) {
        return i.divide(new BigDecimal(j.shortValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, short j) {
        return i.divide(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(Short i, BigDecimal j) {
        return new BigDecimal(i.shortValue()).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(short i, BigDecimal j) {
        return new BigDecimal(i).divide(j);
    }

    @SuppressWarnings("null")
    static public boolean equals(BigDecimal i, Integer j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.equals(new BigDecimal(j.intValue()));
        }
    }

    static public boolean equals(BigDecimal i, int j) {
        if (i == null) {
            return false;
        }
        return i.equals(new BigDecimal(j));
    }

    static public boolean equals(int i, BigDecimal j) {
        if (j == null) {
            return false;
        }
        return new BigDecimal(i).equals(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, Integer j) {
        return i.add(new BigDecimal(j.intValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, int j) {
        return i.add(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(Integer i, BigDecimal j) {
        return new BigDecimal(i.intValue()).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(int i, BigDecimal j) {
        return new BigDecimal(i).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, Integer j) {
        return i.subtract(new BigDecimal(j.intValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, int j) {
        return i.subtract(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(Integer i, BigDecimal j) {
        return new BigDecimal(i.intValue()).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(int i, BigDecimal j) {
        return new BigDecimal(i).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, Integer j) {
        return i.multiply(new BigDecimal(j.intValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, int j) {
        return i.multiply(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(Integer i, BigDecimal j) {
        return new BigDecimal(i.intValue()).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(int i, BigDecimal j) {
        return new BigDecimal(i).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, Integer j) {
        return i.divide(new BigDecimal(j.intValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, int j) {
        return i.divide(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(Integer i, BigDecimal j) {
        return new BigDecimal(i.intValue()).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(int i, BigDecimal j) {
        return new BigDecimal(i).divide(j);
    }

    @SuppressWarnings("null")
    static public boolean equals(BigDecimal i, Long j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.equals(new BigDecimal(j.longValue()));
        }
    }

    static public boolean equals(BigDecimal i, long j) {
        if (i == null) {
            return false;
        }
        return i.equals(new BigDecimal(j));
    }

    static public boolean equals(long i, BigDecimal j) {
        if (j == null) {
            return false;
        }
        return new BigDecimal(i).equals(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, Long j) {
        return i.add(new BigDecimal(j.longValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, long j) {
        return i.add(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(Long i, BigDecimal j) {
        return new BigDecimal(i.longValue()).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(long i, BigDecimal j) {
        return new BigDecimal(i).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, Long j) {
        return i.subtract(new BigDecimal(j.longValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, long j) {
        return i.subtract(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(Long i, BigDecimal j) {
        return new BigDecimal(i.longValue()).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(long i, BigDecimal j) {
        return new BigDecimal(i).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, Long j) {
        return i.multiply(new BigDecimal(j.longValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, long j) {
        return i.multiply(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(Long i, BigDecimal j) {
        return new BigDecimal(i.longValue()).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(long i, BigDecimal j) {
        return new BigDecimal(i).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, Long j) {
        return i.divide(new BigDecimal(j.longValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, long j) {
        return i.divide(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(Long i, BigDecimal j) {
        return new BigDecimal(i.longValue()).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(long i, BigDecimal j) {
        return new BigDecimal(i).divide(j);
    }

    @SuppressWarnings("null")
    static public boolean equals(BigDecimal i, Float j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return i.equals(new BigDecimal(j.floatValue()));
        }
    }

    static public boolean equals(BigDecimal i, float j) {
        if (i == null) {
            return false;
        }
        return i.equals(new BigDecimal(j));
    }

    static public boolean equals(float i, BigDecimal j) {
        if (j == null) {
            return false;
        }
        return new BigDecimal(i).equals(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, Float j) {
        return i.add(new BigDecimal(j.floatValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, float j) {
        return i.add(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(Float i, BigDecimal j) {
        return new BigDecimal(i.floatValue()).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(float i, BigDecimal j) {
        return new BigDecimal(i).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, Float j) {
        return i.subtract(new BigDecimal(j.floatValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, float j) {
        return i.subtract(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(Float i, BigDecimal j) {
        return new BigDecimal(i.floatValue()).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(float i, BigDecimal j) {
        return new BigDecimal(i).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, Float j) {
        return i.multiply(new BigDecimal(j.floatValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, float j) {
        return i.multiply(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(Float i, BigDecimal j) {
        return new BigDecimal(i.floatValue()).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(float i, BigDecimal j) {
        return new BigDecimal(i).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, Float j) {
        return i.divide(new BigDecimal(j.floatValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, float j) {
        return i.divide(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(Float i, BigDecimal j) {
        return new BigDecimal(i.floatValue()).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(float i, BigDecimal j) {
        return new BigDecimal(i).divide(j);
    }

    @SuppressWarnings("null")
    static public boolean equals(BigDecimal i, Double j) {
        if ((i == null && j != null)
                || (i != null && j == null)) {
            return false;
        } else if (i == null && j == null) {
            return true;
        } else {
            return equals(i, new BigDecimal(j.doubleValue()));
        }
    }

    static public boolean equals(BigDecimal i, double j) {
        if (i == null) {
            return false;
        }
        return equals(i, new BigDecimal(j));
    }

    static public boolean equals(double i, BigDecimal j) {
        if (j == null) {
            return false;
        }
        return equals(new BigDecimal(i), (j));
    }
    
    static public boolean equals(BigDecimal i, BigDecimal j) {
        if (i == null && j == null) {
            return true;
        }
        if (i == null || j == null) {
            return false;
        }
        return i.compareTo(j) == 0;
    }    

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, Double j) {
        return i.add(new BigDecimal(j.doubleValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, double j) {
        return i.add(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal add(Double i, BigDecimal j) {
        return new BigDecimal(i.doubleValue()).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(double i, BigDecimal j) {
        return new BigDecimal(i).add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, Double j) {
        return i.subtract(new BigDecimal(j.doubleValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, double j) {
        return i.subtract(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(Double i, BigDecimal j) {
        return new BigDecimal(i.doubleValue()).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(double i, BigDecimal j) {
        return new BigDecimal(i).subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, Double j) {
        return i.multiply(new BigDecimal(j.doubleValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, double j) {
        return i.multiply(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(Double i, BigDecimal j) {
        return new BigDecimal(i.doubleValue()).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(double i, BigDecimal j) {
        return new BigDecimal(i).multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, Double j) {
        return i.divide(new BigDecimal(j.doubleValue()));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, double j) {
        return i.divide(new BigDecimal(j));
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(Double i, BigDecimal j) {
        return new BigDecimal(i.doubleValue()).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(double i, BigDecimal j) {
        return new BigDecimal(i).divide(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal add(BigDecimal i, BigDecimal j) {
        return i.add(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal subtract(BigDecimal i, BigDecimal j) {
        return i.subtract(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal multiply(BigDecimal i, BigDecimal j) {
        return i.multiply(j);
    }

    @SuppressWarnings("null")
    static public BigDecimal divide(BigDecimal i, BigDecimal j) {
        BigDecimal result = i.divide(j);
        System.err.println(result);
        return result;
    }

    static public Short extendedBoxToShort(Byte i) {
        return i == null ? null : new Short(i.shortValue());
    }

    static public Integer extendedBoxToInteger(Byte i) {
        return i == null ? null : new Integer(i.intValue());
    }

    static public Long extendedBoxToLong(Byte i) {
        return i == null ? null : new Long(i.longValue());
    }

    static public Float extendedBoxToFloat(Byte i) {
        return i == null ? null : new Float(i.floatValue());
    }

    static public Double extendedBoxToDouble(Byte i) {
        return i == null ? null : new Double(i.doubleValue());
    }

    static public Integer extendedBoxToInteger(Short i) {
        return i == null ? null : new Integer(i.intValue());
    }

    static public Long extendedBoxToLong(Short i) {
        return i == null ? null : new Long(i.longValue());
    }

    static public Float extendedBoxToFloat(Short i) {
        return i == null ? null : new Float(i.floatValue());
    }

    static public Double extendedBoxToDouble(Short i) {
        return i == null ? null : new Double(i.doubleValue());
    }

    static public Long extendedBoxToLong(Integer i) {
        return i == null ? null : new Long(i.longValue());
    }

    static public Float extendedBoxToFloat(Integer i) {
        return i == null ? null : new Float(i.floatValue());
    }

    static public Double extendedBoxToDouble(Integer i) {
        return i == null ? null : new Double(i.doubleValue());
    }

    static public Float extendedBoxToFloat(Long i) {
        return i == null ? null : new Float(i.floatValue());
    }

    static public Double extendedBoxToDouble(Long i) {
        return i == null ? null : new Double(i.doubleValue());
    }

    static public Double extendedBoxToDouble(Float i) {
        return i == null ? null : new Double(i.doubleValue());
    }

    static public BigDecimal extendedBoxToNum(Byte i) {
        return i == null ? null : new BigDecimal(i.byteValue());
    }

    static public BigDecimal extendedBoxToNum(Short i) {
        return i == null ? null : new BigDecimal(i.shortValue());
    }

    static public BigDecimal extendedBoxToNum(Integer i) {
        return i == null ? null : new BigDecimal(i.intValue());
    }

    static public BigDecimal extendedBoxToNum(Long i) {
        return i == null ? null : new BigDecimal(i.longValue());
    }

    static public BigDecimal extendedBoxToNum(Float i) {
        return i == null ? null : new BigDecimal(i.floatValue());
    }

    static public BigDecimal extendedBoxToNum(Double i) {
        return i == null ? null : new BigDecimal(i.doubleValue());
    }

    static public boolean and(Boolean i, Boolean j) {
        return i != null && j != null && i.booleanValue() && j.booleanValue();
    }
    
    static public boolean boolNvl(Boolean b){
        return b == null ? false : b.booleanValue();
    }
}