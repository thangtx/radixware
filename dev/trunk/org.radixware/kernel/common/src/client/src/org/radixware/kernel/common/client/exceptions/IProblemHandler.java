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

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.enums.EEventSeverity;


public interface IProblemHandler {
    
    public static class ProblemSource{
        
        private final String source,operation;
        private final Model contextModel;
        
        protected ProblemSource(){
            source = null;
            operation = null;
            contextModel = null;
        }
                
        public ProblemSource(final String source, final String operation, final Model model){
            this.source = source;
            this.operation = operation;
            this.contextModel = model;
        }
        
        public String getSourceDescription() {
            return source;
        }
        
        public String getOperationDescription() {
            return operation;
        }
        
        public Model getContextModel() {
            return contextModel;
        }        
    }
    
    public static class Problem<T extends Exception>{
        
        private final String description;
        private final T exception;
        private final EEventSeverity severity;
        
        
        public Problem(final String description, final T exception, final EEventSeverity severity){
            this.description = description;
            this.exception = exception;
            this.severity = severity;
        }
        
        public Problem(final T exception){
            this(null,exception,EEventSeverity.ERROR);
        }
        
        
        
        public String getDescription(){
            return description;
        }
        
        public T getException(){
            return exception;
        }
        
        public EEventSeverity getSeverity(){
            return severity;
        }
        
        public void goToProblem(){
            
        }        
    }
    
    public static class Error<T extends Exception> extends Problem<T>{
        
        public Error(final String description, final T exception){
            super(description,exception,EEventSeverity.ERROR);
        }
        
        public Error(final T exception){
            super(null,exception,EEventSeverity.ERROR);
        }
                
        @Override
        public final EEventSeverity getSeverity(){
            return EEventSeverity.ERROR;
        }
    }    
    
    public static class Warning<T extends Exception> extends Problem<T>{
        
        public Warning(final String description, final T exception){
            super(description,exception,null);
        }
        
        public Warning(final T exception){
            super(null,exception,null);
        }        
        
        @Override
        public final EEventSeverity getSeverity(){
            return EEventSeverity.WARNING;
        }
    }        
    
    void handle(ProblemSource source, Problem problem);
}