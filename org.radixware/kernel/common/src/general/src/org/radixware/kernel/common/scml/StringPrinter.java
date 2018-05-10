/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.scml;

/**
 *
 * @author achernomyrdin
 */
public class StringPrinter extends BufferedCodePrinter {

    @Override
    public CodePrinter printStringLiteral(String text) {
        return super.print("\'").print(text).print("\'");
    }

    @Override
    public CodePrinter printCommandSeparator() {
        return super.println();
    }
}
