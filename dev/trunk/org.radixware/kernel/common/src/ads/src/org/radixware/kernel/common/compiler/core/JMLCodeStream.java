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

package org.radixware.kernel.common.compiler.core;

import java.math.BigDecimal;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.codegen.ConstantPool;
import org.eclipse.jdt.internal.compiler.codegen.Opcodes;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;


public class JMLCodeStream extends CodeStream {

    private static final char[] JavaMathBigDecimalConstantPoolName = "java/math/BigDecimal".toCharArray(); //$NON-NLS-1$
    private static final char[] longBigDecimalSignature = "(J)Ljava/math/BigDecimal;".toCharArray(); //$NON-NLS-1$
    private static final char[] doubleBigDecimalSignature = "(D)Ljava/math/BigDecimal;".toCharArray(); //$NON-NLS-1$

    public JMLCodeStream(ClassFile givenClassFile) {
        super(givenClassFile);
    }

    public void generateBoxingConversionForBigDecimal(int unboxedTypeID, boolean isFloatingPoint) {

        if (unboxedTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBaseBigDecimal) {
            invoke(
                    Opcodes.OPC_invokestatic,
                    2, // receiverAndArgsSize
                    1, // return type size
                    JavaMathBigDecimalConstantPoolName,
                    ConstantPool.ValueOf,
                    isFloatingPoint ? doubleBigDecimalSignature : longBigDecimalSignature);
        } else {
            super.generateBoxingConversion(unboxedTypeID);
        }
    }

    @Override
    public void generateUnboxingConversion(int unboxedTypeID) {
        super.generateUnboxingConversion(unboxedTypeID);
    }

    @Override
    public void generateConstant(Constant constant, int implicitConversionCode) {
        int targetTypeID = (implicitConversionCode & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4;

        if (targetTypeID == AdsBinaryTypeBinding.TypeId_JavaMathBaseBigDecimal) {
            int constTypeId = constant.typeID();
            boolean isFloatingPoint = false;
            if (constTypeId == TypeIds.T_float || constTypeId == TypeIds.T_double) {
                generateInlinedValue(constant.doubleValue());
                isFloatingPoint = true;
            } else {
                generateInlinedValue(constant.longValue());
            }
            if ((implicitConversionCode & TypeIds.BOXING) != 0) {
                generateBoxingConversionForBigDecimal(targetTypeID, isFloatingPoint);
            }
        } else {
            super.generateConstant(constant, implicitConversionCode);
        }

    }
}
