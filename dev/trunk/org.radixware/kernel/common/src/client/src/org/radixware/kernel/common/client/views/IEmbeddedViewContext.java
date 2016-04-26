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

package org.radixware.kernel.common.client.views;

import org.radixware.kernel.common.client.models.items.EditorPageModelItem;


public interface IEmbeddedViewContext {
    public static enum EType{
        EDITOR_PAGE,CURRENT_ENTITY_EDITOR,EMBEDDED_VIEW
    }
    
    EType getType();
    
    public static final class EditorPage implements IEmbeddedViewContext{
        
        final private EditorPageModelItem editorPage;
        
        public EditorPage(final EditorPageModelItem editorPage){            
            this.editorPage = editorPage;
        }

        @Override
        public EType getType() {
            return EType.EDITOR_PAGE;
        }
                
        public EditorPageModelItem getEditorPage(){
            return editorPage;
        }
    }
    
    public static final class CurrentEntityEditor implements IEmbeddedViewContext{
        @Override
        public EType getType() {
            return EType.CURRENT_ENTITY_EDITOR;
        }        
    }
    
    public static final class EmbeddedView implements IEmbeddedViewContext{
        
        public EmbeddedView(){
        }

        @Override
        public EType getType() {
            return EType.EMBEDDED_VIEW;
        }                
    }
}
