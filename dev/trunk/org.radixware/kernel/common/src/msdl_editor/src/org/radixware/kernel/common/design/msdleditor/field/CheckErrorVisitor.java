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

package org.radixware.kernel.common.design.msdleditor.field;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlField;


public class CheckErrorVisitor implements IVisitor {

    private JList list;
    private ProblemHandler problemHandler = new ProblemHandler();
    public CheckErrorVisitor(JList list) {
        this.list = list;
    }

    public class ListRadixProblem {

        public ListRadixProblem(RadixProblem problem, int count) {
            this.problem = problem;
            this.count = count;
        }
        public RadixProblem problem;
        private int count;

        @Override
        public String toString() {
            return String.valueOf(count+1) + ". " + problem.getSource().getQualifiedName() + ": '" + problem.getMessage() + "'";
        }

    }

    public class ProblemHandler implements IProblemHandler {

        @Override
        public void accept(RadixProblem problem) {
            DefaultListModel model = (DefaultListModel)(CheckErrorVisitor.this.list.getModel());
            int count = model.getSize();
            model.addElement(new ListRadixProblem(problem,count));
        }
        
    }
    @Override
    public void accept(RadixObject radixObject) {
        if (radixObject instanceof MsdlField) {
            ((MsdlField)radixObject).check(problemHandler);
            return;
        }
    }

}
