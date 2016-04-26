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

package org.radixware.wps.rwt;

import java.util.HashMap;
import java.util.Map;


public class FormBox extends LabeledEditGrid{
        

    private final static class MatcherImpl extends Editor2LabelMatcher{
            
        private final Map<UIObject, String> editor2Title = new HashMap<>();
        private final Map<UIObject, Label> editor2Label = new HashMap<>();
                
        public void addMatch(final UIObject editor, final String label){
            editor2Title.put(editor, label);
        }
        
        public void changeTitle(final UIObject editor, final String newTitle){
            editor2Title.put(editor, newTitle);            
            final Label label = editor2Label.get(editor);
            if (label!=null){
                label.setText(newTitle);                
            }                        
        }

        @Override
        protected UIObject createLabelComonent(final UIObject editorComponent) {
            final Label label = new Label(editor2Title.get(editorComponent));
            label.setTextWrapDisabled(true);
            editor2Label.put(editorComponent, label);
            return label;
        }

        @Override
        protected void acceptEditor2Label(final UIObject editorComponent, final UIObject labelComponent) {
        }

        @Override
        protected void closeEditor2Label(final UIObject editorComponent, final UIObject labelComponent) {
            editor2Title.remove(editorComponent);
            editor2Label.remove(editorComponent);
        }

        @Override
        protected boolean isVisible(final UIObject editorComponent) {
            return editorComponent.isVisible();
        }                          
    }
    
    private final MatcherImpl internalMatcher;
    private int rows;
            
    public FormBox(){
        super(new MatcherImpl());        
        internalMatcher = (MatcherImpl)matcher;
    }
    
    public void addLabledEditor(final String label, final UIObject editor){
        internalMatcher.addMatch(editor, label);
        super.addEditor(editor, 0, ++rows);
    }
    
    public void changeEditorLabel(final UIObject editor, final String newLabel){
        internalMatcher.changeTitle(editor, newLabel);
    }
}