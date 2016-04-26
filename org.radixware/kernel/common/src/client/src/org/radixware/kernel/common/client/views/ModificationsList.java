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

import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IAskForApplyChangesDialog;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.exceptions.IProblemHandler;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class ModificationsList {
    
    public final static ModificationsList EMPTY_LIST = new ModificationsList();

    private static class ViewsCollector implements IView.Visitor {

        final private List<IEmbeddedView> embeddedViews = new LinkedList<>();
        final private EnumSet<IEmbeddedViewContext.EType> contextTypes;
        final private Boolean isModified;

        public ViewsCollector(final EnumSet<IEmbeddedViewContext.EType> types, final Boolean isModified) {
            contextTypes = types;
            this.isModified = isModified;
        }

        public List<IEmbeddedView> getChildren() {
            return embeddedViews;
        }

        @Override
        public void visit(final IEmbeddedView embeddedView) {
            if (contextTypes == null || contextTypes.isEmpty()
                    || contextTypes.contains(embeddedView.getViewContext().getType())) {
                if (isModified == null) {
                    embeddedViews.add(embeddedView);
                } else {
                    embeddedView.getModel().finishEdit();
                    if (embeddedView.inModifiedStateNow() == isModified) {
                        embeddedViews.add(embeddedView);
                    }
                }
            }
        }

        @Override
        public boolean cancelled() {
            return false;
        }

        public void clear() {
            embeddedViews.clear();
        }
    }

    private static class ApplyChangesProblemSource extends IProblemHandler.ProblemSource {

        public ApplyChangesProblemSource(final String title, final IView view) {
            super(null, title, view.getModel());
        }
    }

    public interface ModifiedObject {

        boolean applyChanges(IProgressHandle progressHandle, IProblemHandler problemHandle);

        void cancelChanges();

        void activate();

        boolean hasUnsavedData();

        String getTitle();

        String getSaveConfirmationMessage(MessageProvider mp);

        String getCloseConfirmationMessage(MessageProvider mp);

        Icon getIcon();

        List<ModifiedObject> getChildren();
    }

    private static abstract class AbstractModifiedObject implements ModifiedObject {

        protected abstract Collection<IEmbeddedView> embeddedViewsToUpdate();

        @Override
        public boolean applyChanges(final IProgressHandle progressHandle, IProblemHandler problemHandler) {
            final Collection<IEmbeddedView> modifiedChildren = embeddedViewsToUpdate();
            for (IEmbeddedView embeddedView : modifiedChildren) {
                if (progressHandle.wasCanceled()) {
                    return false;
                }
                if (embeddedView.inModifiedStateNow() && !applyChanges(embeddedView.getView(), problemHandler)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void cancelChanges() {
            final Collection<IEmbeddedView> modifiedChildren = embeddedViewsToUpdate();
            for (IEmbeddedView embeddedView : modifiedChildren) {
                if (embeddedView.inModifiedStateNow()) {
                    cancelChanges(embeddedView.getView());
                }
            }
        }

        protected final boolean applyChanges(final IView view, final IProblemHandler problemHandler) {
            try {
                if (view instanceof IEditor) {
                    return ((IEditor) view).applyChanges();
                } else if (view instanceof ISelector) {
                    return ((ISelector) view).applyChanges();
                } else {
                    return false;
                }
            } catch (ObjectNotFoundError error) {
                final IProblemHandler.Error problem;
                if (error.inContextOf(view.getModel())) {
                    //Do not activate modified object because it was removed
                    problem = new IProblemHandler.Error<>(error);
                } else {
                    problem = new IProblemHandler.Error<ObjectNotFoundError>(error) {

                        @Override
                        public void goToProblem() {
                            AbstractModifiedObject.this.activate();
                        }
                    };
                }
                problemHandler.handle(new ApplyChangesProblemSource(getApplyChangesProblemTitle(), view), problem);
            } catch (ServiceClientException exception) {
                final IProblemHandler.Error problem = new IProblemHandler.Error<ServiceClientException>(exception) {

                    @Override
                    public void goToProblem() {
                        AbstractModifiedObject.this.activate();
                    }
                };
                problemHandler.handle(new ApplyChangesProblemSource(getApplyChangesProblemTitle(), view), problem);
            } catch (ModelException exception) {
                final IProblemHandler.Error problem = new IProblemHandler.Error<ModelException>(exception) {

                    @Override
                    public void goToProblem() {
                        AbstractModifiedObject.this.activate();
                    }
                };
                problemHandler.handle(new ApplyChangesProblemSource(getApplyChangesProblemTitle(), view), problem);
            }
            return false;
        }

        protected final void cancelChanges(final IView view) {
            if (view instanceof IEditor) {
                ((IEditor) view).cancelChanges();
            } else if (view instanceof ISelector) {
                ((ISelector) view).cancelChanges();
            }
        }

        protected abstract String getApplyChangesProblemTitle();
    }

    private static final class ModifiedView extends AbstractModifiedObject {

        private final IView self;
        private final Collection<IEmbeddedView> modifiedChildren;
        private final String applyChangesProblemTitle;
        private final String title;
        private final Icon icon;

        public ModifiedView(final Collection<IEmbeddedView> modifiedChildren, final IView self, final boolean isModified) {
            this.self = isModified ? self : null;
            this.modifiedChildren = modifiedChildren;
            if (self instanceof ISelector && modifiedChildren.size()==1){
                final IEmbeddedView modifiedView = modifiedChildren.iterator().next();
                if (modifiedView instanceof IEmbeddedEditor){
                    title = ((IEmbeddedEditor)modifiedView).getModel().getTitle();
                }else{
                    title = self.getModel().getTitle();
                }
            }else{
                title = self.getModel().getTitle();
            }
            icon = self.getModel().getIcon();
            final MessageProvider mp = self.getEnvironment().getMessageProvider();

            if ((self instanceof IEditor) && ((IEditor) self).getEntityModel().isNew()) {
                if (((IEditor) self).getEntityModel().getSrcPid() != null) {
                    applyChangesProblemTitle = mp.translate("Editor", "Failed to create copy");
                } else {
                    applyChangesProblemTitle = mp.translate("Editor", "Failed to create object");
                }
            } else {
                applyChangesProblemTitle =
                        String.format(mp.translate("ExplorerMessage", "Failed to apply changes in editor of \'%s\'"), title);
            }
        }

        @Override
        public boolean applyChanges(IProgressHandle progressHandle, IProblemHandler problemHandle) {
            if (self != null && !applyChanges(self, problemHandle)) {
                return false;
            }
            if (progressHandle.wasCanceled()) {
                return false;
            } else {
                return super.applyChanges(progressHandle, problemHandle);
            }
        }

        @Override
        public void cancelChanges() {
            if (self != null) {
                cancelChanges(self);
            }
            super.cancelChanges();
        }

        @Override
        public List<ModifiedObject> getChildren() {
            return Collections.<ModifiedObject>emptyList();
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSaveConfirmationMessage(final MessageProvider mp) {
            return String.format(mp.translate("ExplorerMessage", "Apply changes in editor of \'%s\'?"), title);
        }

        @Override
        public String getCloseConfirmationMessage(final MessageProvider mp) {
            return String.format(mp.translate("ExplorerMessage", "Close editor of \'%s\' without saving changes?"), title);
        }

        @Override
        public Icon getIcon() {
            return icon;
        }

        @Override
        public boolean hasUnsavedData() {
            return true;
        }

        @Override
        protected Collection<IEmbeddedView> embeddedViewsToUpdate() {
            return modifiedChildren;
        }

        @Override
        public void activate() {
            if (self instanceof IEditor == false || !setFocusedModifiedProperty((IEditor) self)) {
                for (IEmbeddedView embeddedView : modifiedChildren) {
                    if (embeddedView.getView() instanceof IEditor
                            && setFocusedModifiedProperty((IEditor) embeddedView.getView())) {
                        break;
                    }
                }
            }
        }

        @Override
        protected String getApplyChangesProblemTitle() {
            return applyChangesProblemTitle;
        }

        private boolean setFocusedModifiedProperty(final IEditor editor) {
            final List<Property> editedProperties = editor.getEntityModel().getEditedProperties();
            for (Property property : editedProperties) {
                if (property.setFocused()) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class ModifiedPage extends AbstractModifiedObject {

        private final List<IEmbeddedView> embeddedViews = new LinkedList<>();
        private final List<ModifiedObject> childPages = new LinkedList<>();
        private final EditorPageModelItem pageModelItem;
        private final String title;
        private final Icon icon;

        public ModifiedPage(final IEmbeddedView embeddedView) {
            if (embeddedView.inModifiedStateNow()) {
                embeddedViews.add(embeddedView);
            }
            collectModifiedItems(embeddedView.getView(), embeddedViews, childPages);
            final IEmbeddedViewContext.EditorPage pageContext =
                    (IEmbeddedViewContext.EditorPage) embeddedView.getViewContext();
            pageModelItem = pageContext.getEditorPage();
            final String pageTitle = pageModelItem.getTitle();
            final MessageProvider mp = embeddedView.getModel().getEnvironment().getMessageProvider();
            title = String.format(mp.translate("ExplorerMessage", "page \"%s\""), pageTitle);
            icon = pageContext.getEditorPage().getIcon();
        }

        @Override
        protected Collection<IEmbeddedView> embeddedViewsToUpdate() {
            return embeddedViews;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSaveConfirmationMessage(final MessageProvider mp) {
            return String.format(mp.translate("ExplorerMessage", "Apply changes in page \'%s\'?"), pageModelItem.getTitle());
        }

        @Override
        public String getCloseConfirmationMessage(final MessageProvider mp) {
            return String.format(mp.translate("ExplorerMessage", "Close page \'%s\' without saving changes?"), pageModelItem.getTitle());
        }

        @Override
        protected String getApplyChangesProblemTitle() {
            final MessageProvider mp = pageModelItem.getEnvironment().getMessageProvider();
            return String.format(mp.translate("ExplorerMessage", "Failed to apply changes in page \'%s\'"), pageModelItem.getTitle());
        }

        @Override
        public Icon getIcon() {
            return icon;
        }

        @Override
        public boolean hasUnsavedData() {
            return !embeddedViews.isEmpty();
        }

        @Override
        public List<ModifiedObject> getChildren() {
            return Collections.<ModifiedObject>unmodifiableList(childPages);
        }

        @Override
        public void activate() {
            pageModelItem.setFocused();
        }
    }
    
    private final List<ModifiedObject> modifiedObjectsTree = new LinkedList<>();

    public ModificationsList(final IView view) {
        final List<IEmbeddedView> modifiedChildren = new LinkedList<>();
        collectModifiedItems(view, modifiedChildren, modifiedObjectsTree);
        final boolean isViewModified = isViewModified(view) && userCanApplyChanges(view);
        if (!modifiedChildren.isEmpty() || isViewModified) {
            modifiedObjectsTree.add(0, new ModifiedView(modifiedChildren, view, isViewModified));
        }
    }
    
    private ModificationsList(){       
    }

    public boolean isEmpty() {
        return modifiedObjectsTree.isEmpty();
    }

    public List<ModifiedObject> getModifiedObjectsTree() {
        return Collections.<ModifiedObject>unmodifiableList(modifiedObjectsTree);
    }

    public List<ModifiedObject> getModifiedObjectsList() {
        return getModifiedObjectsListFromTree(modifiedObjectsTree);
    }

    private static List<ModifiedObject> getModifiedObjectsListFromTree(final List<ModifiedObject> modifiedObjects) {
        final List<ModifiedObject> result = new LinkedList<>();
        final Stack<ModifiedObject> stack = new Stack<>();
        for (int i = modifiedObjects.size() - 1; i >= 0; i--) {
            stack.push(modifiedObjects.get(i));
        }
        ModifiedObject modifiedObject;
        List<ModifiedObject> children;
        while (!stack.isEmpty()) {
            modifiedObject = stack.pop();
            if (modifiedObject.hasUnsavedData() && !result.contains(modifiedObject)) {
                result.add(modifiedObject);
            }
            children = modifiedObject.getChildren();
            for (final ModifiedObject child : children) {
                stack.push(child);
            }
        }
        return Collections.<ModifiedObject>unmodifiableList(result);
    }

    public List<ModifiedObject> askForApplyChanges(final IClientEnvironment environment, final IWidget parentWidget) {
        if (isEmpty()) {
            return Collections.<ModifiedObject>emptyList();
        }
        final ModifiedObject firstModified = getModifiedObjectsTree().get(0);
        if (environment.getDefManager().getAdsVersion().isSupported()) {
            final List<ModifiedObject> modifiedObjectsList = getModifiedObjectsList();
            if (modifiedObjectsList.size() == 1) {
                final ModifiedObject modifiedObject = modifiedObjectsList.get(0);
                Set<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);
                final EDialogButtonType answer = environment.messageBox(environment.getMessageProvider().translate("ExplorerDialog", "Save Changes?"),
                        modifiedObject.getSaveConfirmationMessage(environment.getMessageProvider()),
                        EDialogIconType.QUESTION,
                        buttons);
                switch (answer) {
                    case YES:
                        return Collections.<ModifiedObject>singletonList(modifiedObject);
                    case NO:
                        return Collections.<ModifiedObject>emptyList();
                    default:
                        modifiedObject.activate();
                        return null;
                }
            }
            final IAskForApplyChangesDialog dialog =
                    environment.getApplication().getDialogFactory().newAskForApplyChangesDialiog(environment, parentWidget, this);
            if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                return dialog.getItemsToApplyChanges();
            } else {
                firstModified.activate();
                return null;
            }
        } else {
            final String title = environment.getMessageProvider().translate("ExplorerDialog", "Close Editor?");
            final String message = firstModified.getCloseConfirmationMessage(environment.getMessageProvider());
            if (environment.messageConfirmation(title, message)) {
                return Collections.<ModifiedObject>emptyList();
            } else {
                firstModified.activate();
                return null;
            }
        }
    }

    public boolean applyChangesOnClosingView(final IClientEnvironment environment, final IWidget parentWidget, final List<ModifiedObject> objects) {
        final StandardProblemHandler problemHandler = new StandardProblemHandler();
        final List<ModifiedObject> processedObjects;
        try {
            processedObjects = applyChanges(environment, objects, problemHandler);
        } catch (InterruptedException exception) {
            return false;
        } finally {
            final String title = environment.getMessageProvider().translate("Editor", "Failed to apply changes");
            problemHandler.showProblems(environment, parentWidget, title);
        }
        if (problemHandler.wasProblems()){
            if (hasCriticalProblem(problemHandler)) {
                return false;
            } else {
                final List<ModifiedObject> modifiedObjectsList = getModifiedObjectsList();
                final List<ModifiedObject> objectsList = getModifiedObjectsListFromTree(objects);
                for (ModifiedObject object : modifiedObjectsList) {
                    if (!objectsList.contains(object)) {
                        object.cancelChanges();
                    }
                }
                return true;
            }
        }
        else{
            return processedObjects.containsAll(objects);
        }
    }

    public List<ModifiedObject> applyChanges(final IClientEnvironment environment, final List<ModifiedObject> items, final IProblemHandler problemHandler) throws InterruptedException {
        final List<ModifiedObject> processedObjects = new LinkedList<>();
        final IProgressHandle progressHandle = environment.getProgressHandleManager().newStandardProgressHandle();
        progressHandle.startProgress(environment.getMessageProvider().translate("Wait Dialog", "Saving Data..."), true);
        try {
            for (ModifiedObject item : items) {
                if (item.applyChanges(progressHandle, problemHandler)){
                    processedObjects.add(item);
                }
                if (progressHandle.wasCanceled()) {
                    throw new InterruptedException("Saving data was cancelled");
                }
            }
        } finally {
            progressHandle.finishProgress();
        }
        return processedObjects;
    }

    public void cancelChanges() {
        final List<ModifiedObject> modifiedObjectsList = getModifiedObjectsList();
        for (ModifiedObject object : modifiedObjectsList) {
            object.cancelChanges();
        }
    }

    private static boolean hasCriticalProblem(final StandardProblemHandler handler) {
        final List<IProblemHandler.Problem> problems = handler.getProblems();
        for (IProblemHandler.Problem problem : problems) {
            if (problem.getException() instanceof ObjectNotFoundError) {
                final ObjectNotFoundError error = (ObjectNotFoundError) problem.getException();
                final Model model = handler.getProblemSource(problem).getContextModel();
                if (error.inContextOf(model)) {
                    continue;
                }
                if (model instanceof GroupModel && model.getView() != null) {
                    final EntityModel currentEntity = ((ISelector) model.getView()).getCurrentEntity();
                    if (!error.inContextOf(currentEntity)) {
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private static void collectModifiedItems(final IView view, final List<IEmbeddedView> modifiedChildren, final List<ModifiedObject> modifiedPages) {
        modifiedChildren.addAll(findModifiedChildren(view));
        final ViewsCollector viewsCollector =
                new ViewsCollector(EnumSet.of(IEmbeddedViewContext.EType.EDITOR_PAGE), null);
        view.visitChildren(viewsCollector, false);
        final List<IEmbeddedView> pages = new LinkedList<>(viewsCollector.getChildren());
        IView pageView;
        for (int i = pages.size() - 1; i >= 0; i--) {
            pageView = pages.get(i).getView();
            pageView.finishEdit();
            if (pages.get(i).inModifiedStateNow()) {
                if (pages.get(i).isSynchronizedWithParentView()) {
                    modifiedChildren.add(pages.get(i));
                } else {
                    modifiedPages.add(new ModifiedPage(pages.get(i)));
                }
            } else if (!findModifiedChildren(pageView).isEmpty()) {
                modifiedPages.add(new ModifiedPage(pages.get(i)));
            }
        }
    }

    private static Collection<IEmbeddedView> findModifiedChildren(final IView view) {
        final Collection<IEmbeddedView> result = new LinkedList<>();
        final ViewsCollector viewsCollector =
                new ViewsCollector(EnumSet.of(IEmbeddedViewContext.EType.EMBEDDED_VIEW, IEmbeddedViewContext.EType.CURRENT_ENTITY_EDITOR), null);
        final Stack<IView> views = new Stack<>();
        views.push(view);
        IView currentView;
        Collection<IEmbeddedView> children;
        while (!views.isEmpty()) {
            currentView = views.pop();
            viewsCollector.clear();
            currentView.visitChildren(viewsCollector, false);
            children = viewsCollector.getChildren();
            for (final IEmbeddedView child : children) {
                views.push(child.getView());
                child.getModel().finishEdit();
                if (child.inModifiedStateNow() && userCanApplyChangesInEmbeddedView(child, currentView)) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    private static boolean userCanApplyChangesInEmbeddedView(final IEmbeddedView embeddedView, final IView view) {
        final Model model = embeddedView.getModel();
        if (model instanceof EntityModel && ((EntityModel) model).isNew()) {
            return embeddedView.isSynchronizedWithParentView() && userCanApplyChanges(view);
        } else {
            return userCanApplyChanges(embeddedView.getView());
        }
    }

    private static boolean userCanApplyChanges(final IView view) {
        final EntityModel model;
        if (view instanceof IEditor) {
            model = ((IEditor) view).getEntityModel();
        } else if (view instanceof ISelector) {
            model = ((ISelector) view).getCurrentEntity();
        } else {
            model = null;
        }
        return model != null && model.isExists() && !model.getRestrictions().getIsUpdateRestricted();
    }

    private static boolean isViewModified(final IView view) {
        if (view instanceof ISelector) {
            final EntityModel currentEntity = ((ISelector) view).getCurrentEntity();
            return currentEntity != null && currentEntity.isEdited();
        } else if (view instanceof IEditor) {
            return ((EntityModel) view.getModel()).isEdited();
        }
        return false;
    }
}