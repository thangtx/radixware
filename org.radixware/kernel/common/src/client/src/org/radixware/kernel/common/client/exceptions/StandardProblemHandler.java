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

package org.radixware.kernel.common.client.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IDisplayProblemsDialog;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.UniqueConstraintViolationError;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceCallFault;


public class StandardProblemHandler implements IProblemHandler, Iterable<IProblemHandler.Problem>{
    
    private final Map<Problem,ProblemSource> problemSourceToProblem = new HashMap<Problem,ProblemSource>();
    private final List<Problem> problems = new LinkedList<Problem>();

    @Override
    public void handle(final ProblemSource source, final Problem problem) {
        Problem actualProblem = problem;        
        if (source.getContextModel()!=null){
            final Model contextModel = source.getContextModel();
            for (Throwable toProcess = problem.getException(); toProcess!=null; toProcess = toProcess.getCause()){
                if (toProcess instanceof ObjectNotFoundError){
                    final ObjectNotFoundError error = (ObjectNotFoundError)toProcess;                    
                    if (error.inContextOf(contextModel) && contextModel instanceof EntityModel){
                        error.setContextEntity((EntityModel)contextModel);
                    }
                    actualProblem = new Problem<ObjectNotFoundError>(error){
                        @Override
                        public void goToProblem() {
                            problem.goToProblem();
                        }                        
                    };
                    break;
                }
                else if (toProcess instanceof UniqueConstraintViolationError){
                    actualProblem = new Problem<UniqueConstraintViolationError>((UniqueConstraintViolationError)toProcess){
                        @Override
                        public void goToProblem() {
                            problem.goToProblem();
                        }                        
                    };
                    break;
                }
                else if (toProcess instanceof ServiceCallFault) {
                    final ModelException modelException = ModelException.create(source.getContextModel(), (ServiceCallFault) toProcess);
                    if (modelException != null) {
                        toProcess = modelException;
                    }
                    if (toProcess instanceof ModelPropertyException) {
                        final ModelPropertyException exception = (ModelPropertyException) toProcess;
                        actualProblem = new Problem<ModelPropertyException>(exception){
                            @Override
                            public void goToProblem() {
                                problem.goToProblem();
                                getException().goToProblem(contextModel);
                            }                        
                        };
                    }
                }
            }
        }
        problems.add(actualProblem);
        problemSourceToProblem.put(actualProblem, source);
    }
    
    public List<Problem> getProblems(){
        return Collections.<Problem>unmodifiableList(problems);
    }
    
    public boolean wasProblems(){
        return !problems.isEmpty();
    }
    
    public ProblemSource getProblemSource(final Problem problem){
        return problemSourceToProblem.get(problem);
    }
    
    public void clear(){
        problems.clear();
        problemSourceToProblem.clear();
    }
    
    public void showProblems(final IClientEnvironment environment, final IWidget parentWidget, final String title){
        if (wasProblems()){
            final IProblemHandler.Problem firstProblem = getProblems().get(0);
            if (getProblems().size()==1){
                showProblem(environment, firstProblem);
            }
            else{
                final IDisplayProblemsDialog dialog = 
                    environment.getApplication().getDialogFactory().newDisplayProblemsDialog(environment, parentWidget, this);
                if (title!=null){
                    dialog.setWindowTitle(title);
                }
                dialog.execDialog();
                firstProblem.goToProblem();
            }
        }
    }
    
    public void showProblem(final IClientEnvironment environment, final IProblemHandler.Problem problem){
        final IProblemHandler.ProblemSource problemSource = getProblemSource(problem);
        if (problemSource.getContextModel()==null){
            environment.processException(problem.getException());
        }
        else{
            String title = problemSource.getOperationDescription();
            if (title!=null && !title.isEmpty() && problemSource.getSourceDescription()!=null && !problemSource.getSourceDescription().isEmpty()){
                title+=" "+problemSource.getSourceDescription();
            }
            if (title!=null && !title.isEmpty()){
                problemSource.getContextModel().showException(title, problem.getException());
            }
            else{
                problemSource.getContextModel().showException(problem.getException());
            }
        }
        problem.goToProblem();
    }

    @Override
    public Iterator<IProblemHandler.Problem> iterator() {
        return problems.iterator();
    }
}
