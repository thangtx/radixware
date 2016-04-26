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

package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.PasteAction;
import org.openide.util.WeakListeners;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPresentationSlotMethodDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil.IVisitorUI;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.*;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class BaseWidget extends Widget implements PropertyChangeListener, ContainerChangesListener {

    protected static class WidgetPopupMenu implements PopupMenuProvider {

        @Override
        public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
            final JPopupMenu popupMenu = new JPopupMenu();
            BaseWidget w = (BaseWidget) widget;
            addMenuItem(popupMenu, SystemAction.get(CutAction.class));
            addMenuItem(popupMenu, SystemAction.get(CopyAction.class));
            if (w.isContainer()) {
                addMenuItem(popupMenu, SystemAction.get(PasteAction.class));
            }
            final RadixObject node = w.getNode();
            if (node instanceof AdsWidgetDef || node instanceof AdsRwtWidgetDef) {
                popupMenu.addSeparator();
                addMenuItem(popupMenu, SystemAction.get(FindUsagesAction.class));
            }
            popupMenu.addSeparator();
            addMenuItem(popupMenu, SystemAction.get(DeleteAction.class));
            return popupMenu;
        }

        private JMenuItem addMenuItem(JPopupMenu menu, Action action) {
            final JMenuItem item = DialogUtils.createMenuItem(action);
            menu.add(item);
            return item;
        }
    }

    protected static final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(5);

    public static class Factory {

        private static final HashMap<String, Class<? extends BaseWidget>> hash = new HashMap<>();

        static {
            register(AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS, HorizontalLayoutWidget.class);
            register(AdsMetaInfo.VERTICAL_LAYOUT_CLASS, VerticalLayoutWidget.class);
            register(AdsMetaInfo.GRID_LAYOUT_CLASS, GridLayoutWidget.class);

            register(AdsMetaInfo.SPACER_CLASS, SpacerWidget.class);

            register(AdsMetaInfo.PUSH_BUTTON_CLASS, PushButtonWidget.class);

            register(AdsMetaInfo.TOOL_BUTTON_CLASS, ToolButtonWidget.class);
            register(AdsMetaInfo.RADIO_BUTTON_CLASS, RadioButtonWidget.class);
            register(AdsMetaInfo.CHECK_BOX_CLASS, CheckButtonWidget.class);
            register(AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS, DialogButtonBoxWidget.class);

            register(AdsMetaInfo.GROUP_BOX_CLASS, GroupBoxWidget.class);
            register(AdsMetaInfo.TAB_WIDGET_CLASS, TabWidget.class);
            register(AdsMetaInfo.SPLITTER_CLASS, SplitterWidget.class);
            register(AdsMetaInfo.TOOL_BAR_CLASS, ToolBarWidget.class);
            register(AdsMetaInfo.ACTION_CLASS, ActionWidget.class);

            register(AdsMetaInfo.ADVANCED_SPLITTER_CLASS, AdvancedSplitterWidget.class);
            register(AdsMetaInfo.FRAME_CLASS, FrameWidget.class);
            register(AdsMetaInfo.SCROLL_AREA_CLASS, ScrollAreaWidget.class);

            register(AdsMetaInfo.STACKED_WIDGET_CLASS, StackedWidget.class);
            register(AdsMetaInfo.COMMAND_TOOL_BUTTON_CLASS, CommandToolButtonWidget.class);

            register(AdsMetaInfo.VAL_BIN_EDITOR_CLASS, ValBinEditorWidget.class);
            register(AdsMetaInfo.VAL_BOOL_EDITOR_CLASS, ValBoolEditorWidget.class);
            register(AdsMetaInfo.VAL_CHAR_EDITOR_CLASS, ValCharEditorWidget.class);
            register(AdsMetaInfo.VAL_STR_EDITOR_CLASS, ValStrEditorWidget.class);
            register(AdsMetaInfo.VAL_INT_EDITOR_CLASS, ValIntEditorWidget.class);
            register(AdsMetaInfo.VAL_NUM_EDITOR_CLASS, ValNumEditorWidget.class);
            register(AdsMetaInfo.VAL_CONST_SET_EDITOR_CLASS, ValConstSetEditorWidget.class);
            register(AdsMetaInfo.VAL_LIST_EDITOR_CLASS, ValListEditorWidget.class);
            register(AdsMetaInfo.VAL_TIMEINTERVAL_EDITOR_CLASS, ValTimeIntervalEditorWidget.class);
            register(AdsMetaInfo.VAL_DATE_TIME_EDITOR_CLASS, ValDataTimeEditorWidget.class);
            register(AdsMetaInfo.VAL_REF_EDITOR_CLASS, ValRefEditorWidget.class);
            register(AdsMetaInfo.VAL_FILE_PATH_EDITOR_CLASS, ValFilePathEditorWidget.class);

            register(AdsMetaInfo.LABEL_CLASS, LabelWidget.class);
            register(AdsMetaInfo.PROGRESS_BAR_CLASS, ProgressBarWidget.class);

            register(AdsMetaInfo.COMBO_BOX_CLASS, ComboBoxWidget.class);
            register(AdsMetaInfo.LINE_EDIT_CLASS, LineEditWidget.class);
            register(AdsMetaInfo.TEXT_EDIT_CLASS, TextEditWidget.class);
            register(AdsMetaInfo.SPIN_BOX_CLASS, SpinBoxWidget.class);
            register(AdsMetaInfo.DOUBLE_SPIN_BOX_CLASS, DoubleSpinBoxWidget.class);
            register(AdsMetaInfo.TIME_EDIT_CLASS, TimeEditWidget.class);
            register(AdsMetaInfo.DATE_EDIT_CLASS, DateEditWidget.class);
            register(AdsMetaInfo.DATE_TIME_EDIT_CLASS, DateTimeEditWidget.class);

            register(AdsMetaInfo.LIST_WIDGET_CLASS, ListWidget.class);
            register(AdsMetaInfo.TREE_WIDGET_CLASS, TreeWidget.class);
            register(AdsMetaInfo.TABLE_WIDGET_CLASS, TableWidget.class);
            register(AdsMetaInfo.LIST_VIEW_CLASS, ListViewWidget.class);
            register(AdsMetaInfo.TREE_VIEW_CLASS, TreeViewWidget.class);
            register(AdsMetaInfo.TABLE_VIEW_CLASS, TableViewWidget.class);

            register(AdsMetaInfo.RWT_UI_PUSH_BUTTON, RwtPushButtonWidget.class);
            register(AdsMetaInfo.RWT_UI_TEXT_FIELD, RwtTextFieldWidget.class);
            register(AdsMetaInfo.RWT_UI_TEXT_AREA, RwtTextFieldWidget.class);
            register(AdsMetaInfo.RWT_UI_FILE_INPUT, RwtTextFieldWidget.class);
            register(AdsMetaInfo.RWT_UI_TREE, RwtTreeWidget.class);
            register(AdsMetaInfo.RWT_UI_INPUT_BOX, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_PROP_EDITOR, RwtPropEditorWidget.class);
            register(AdsMetaInfo.RWT_UI_LABEL, RwtLabelWidget.class);
            register(AdsMetaInfo.RWT_PROP_LABEL, RwtLabelWidget.class);
            register(AdsMetaInfo.RWT_UI_PANEL, RwtPanelWidget.class);
            register(AdsMetaInfo.RWT_UI_GROUP_BOX, RwtGroupBoxWidget.class);
            register(AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER, RwtGridBoxContainerWidget.class);
            register(AdsMetaInfo.RWT_UI_CONTAINER, RwtContainerWidget.class);
            register(AdsMetaInfo.RWT_UI_ABSTRACT_CONTAINER, RwtContainerWidget.class);
            register(AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER, RwtContainerWidget.class);
            register(AdsMetaInfo.RWT_HORIZONTAL_BOX_CONTAINER, RwtContainerWidget.class);
            register(AdsMetaInfo.RWT_ACTION, RwtActionWidget.class);
            register(AdsMetaInfo.RWT_TOOL_BAR, RwtToolBarWidget.class);
            register(AdsMetaInfo.RWT_TAB_SET, TabWidget.class);
            register(AdsMetaInfo.RWT_TAB_SET_TAB, RwtContainerWidget.class);
            register(AdsMetaInfo.RWT_SPLITTER, SplitterWidget.class);
            register(AdsMetaInfo.RWT_UI_LIST, RwtListWidget.class);
            register(AdsMetaInfo.RWT_UI_GRID, RwtTableWidget.class);
            register(AdsMetaInfo.RWT_LABELED_EDIT_GRID, RwtLabeledEditGridWidget.class);
            register(AdsMetaInfo.RWT_PROPERTIES_GRID, RwtLabeledEditGridWidget.class);
            register(AdsMetaInfo.RWT_UI_CHECK_BOX, CheckButtonWidget.class);
            register(AdsMetaInfo.RWT_UI_RADIO_BUTTON, CheckButtonWidget.class);
            register(AdsMetaInfo.RWT_COMMAND_PUSH_BUTTON, RwtPushButtonWidget.class);


            register(AdsMetaInfo.RWT_VAL_STR_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_INT_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_NUM_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_TIME_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_BIN_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_ENUM_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_LIST_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_REF_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_TIME_INTERVAL_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_DATE_TIME_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_ARR_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_BOOL_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_VAL_FILE_PATH_EDITOR, RwtInputBoxWidget.class);
            register(AdsMetaInfo.RWT_UI_BUTTON_BOX, RwtButtonBoxWidget.class);
            
        }

        public static BaseWidget newInstance(GraphSceneImpl scene, RadixObject node) {
            if (AdsUIUtil.isUIRoot(node)) {
                return new RootWidget(scene, (AdsUIItemDef) node);
            }
            String classUI = AdsUIUtil.getUiClassName(node);
            Class clazz = hash.get(classUI);
            if (clazz != null) {
                try {
                    @SuppressWarnings("unchecked")
                    Constructor c = clazz.getConstructor(new Class[]{scene.getClass(), node.getClass()});
                    return (BaseWidget) c.newInstance(new Object[]{scene, node});
                } catch (Throwable e) {
                    Logger.getLogger(BaseWidget.class.getName()).log(Level.WARNING, null, e);
                    return null;
                }
            }

            return new CustomWidget(scene, (AdsUIItemDef) node);
        }

        private static void register(String classQt, Class<? extends BaseWidget> classWidget) {
            hash.put(classQt, classWidget);
        }
    }

    private final class KeyAction extends WidgetAction.Adapter {

        @Override
        public State keyPressed(Widget widget, WidgetKeyEvent event) {
            return State.REJECTED;
        }

        @Override
        public State keyReleased(Widget widget, WidgetKeyEvent event) {
            /*
             * System.out.println("key code = " + event.getKeyCode());
             * System.out.println("key char = " + event.getKeyChar());
             * System.out.println("event id = " + event.getEventID());
             * System.out.println("VK_ESCAPE = " + KeyEvent.VK_ESCAPE);
             * System.out.println();
             */
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                final BaseWidget owner = getContainerWidget();
                if (owner != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            getSceneImpl().focus(owner.getNode());
                            getSceneImpl().validate();
                        }
                    });
                }
            }
            return State.CONSUMED;
        }
    }

    private final class GoToDoubleClickAction extends WidgetAction.Adapter {

        @Override
        public State mouseClicked(Widget widget, WidgetMouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2 && event.isControlDown()) {
                RadixObject node = getNode();
                if (!(node instanceof AdsWidgetDef)) {
                    return State.CONSUMED;
                }

                AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(node);
                if (uiDef instanceof AdsUIDef) {
                    AdsUIConnection con = ((AdsUIDef) uiDef).getConnections().get((AdsWidgetDef) node, "clicked");
                    if (con == null) {
                        for (AdsUIConnection c : ((AdsUIDef) uiDef).getConnections()) {
                            String n = c.getSignalName();
                            if (n != null && node.equals(c.getSender()) && n.contains("DoubleClicked")) {
                                con = c;
                                break;
                            }
                        }
                    }
                    if (con != null) {
                        AdsPresentationSlotMethodDef method = con.getSlot();
                        if (method != null) {
                            DialogUtils.goToObject(method);
                        }
                    }
                    return State.CONSUMED;
                }
            }
            return State.REJECTED;
        }
    }

    private class BaseSelectProvider implements SelectProvider {

        @Override
        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return false;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        @Override
        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            GraphSceneImpl scene = (GraphSceneImpl) widget.getScene();
            Object object = scene.findObject(widget);

            scene.setFocusedObject(object);
            if (object != null) {
                if (!invertSelection && scene.getSelectedObjects().contains(object)) {
                } else {
                    scene.userSelectionSuggested(Collections.singleton(object), invertSelection);
                }
            } else {
                scene.userSelectionSuggested(Collections.emptySet(), invertSelection);
            }

            notifySelected(localLocation);
        }
    }

    protected final Border BORDER_SELECTED = new ResizeBorder(this);
    private LayoutProcessor layoutProcessor = null;
    private final RadixObject widgetNode;

    protected BaseWidget(GraphSceneImpl scene, RadixObject node) {
        super(scene);

        assert node != null : "Node is null";
        this.widgetNode = node;

        setOpaque(true);
        setCheckClipping(true);
        AdsUIUtil.addPropertyChangeListener(node, WeakListeners.propertyChange(this, node));
        AdsUIUtil.addContainerListener(node, this);
        for (WidgetAction action : getInitialActions(scene, node)) {
            getActions().addAction(action);
        }
    }

    @Override
    protected void paintBackground() {
        RadixObject node = getNode();
        Item item = Item.getItem(node);
        assert item != null : "item cann't be null";

        Graphics2D gr = getScene().getGraphics();
        Rectangle clip = convertGeometryToLocal(getVisibleWidgetGeometry());

        Shape oldClip = gr.getClip();
        gr.clipRect(clip.x, clip.y, clip.width, clip.height);
        item.paintBackground(gr, getWidgetRect(), node);
        gr.setClip(oldClip);
    }

    @Override
    protected void paintWidget() {
        RadixObject node = getNode();
        Item item = Item.getItem(node);
        assert item != null : "item cann't be null";

        Graphics2D gr = getScene().getGraphics();
        Rectangle clip = convertGeometryToLocal(getVisibleWidgetGeometry());

        Shape oldClip = gr.getClip();
        gr.clipRect(clip.x, clip.y, clip.width, clip.height);
        item.paintBorder(gr, getWidgetRect(), node);
        item.paintWidget(gr, getWidgetRect(), node);
        gr.setClip(oldClip);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport) {
            getScene().revalidate();
            getScene().validate();
        }
    }

    @Override
    public void onEvent(final ContainerChangedEvent e) {

        RadixObject object = e.object;
        if (object instanceof AdsLayout.Item) {
            object = AdsUIUtil.getItemNode((AdsLayout.Item) e.object);
        }
        if (object instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) object;
            if (AdsUIUtil.getUiClassName(widget).equals(AdsMetaInfo.WIDGET_CLASS) && widget.getLayout() != null) {
                object = widget.getLayout();
            }
        }

        if (object == null) {
            return;
        }

        final GraphSceneImpl scene = getSceneImpl();
        AdsUIUtil.visitUI(object, new IVisitorUI() {

            @Override
            public void visit(RadixObject node, boolean active) {
                if (e.changeType == EChangeType.ENLARGE) {
                    if (scene.findWidget(node) == null) {
                        final BaseWidget wg = (BaseWidget) scene.addNode(node);
                        wg.restoreGeometry();
                        wg.setVisible(active);
                        wg.saveGeometry();
                    } // to remove geometry if nessesary
                }

                if (e.changeType == EChangeType.SHRINK) {
                    // delete connections
                    final AdsAbstractUIDef ui = AdsUIUtil.getUiDef(getNode());
                    if (node instanceof AdsWidgetDef && ui instanceof AdsUIDef) {
                        final AdsWidgetDef w = (AdsWidgetDef) node;
                        for (int i = ((AdsUIDef) ui).getConnections().size() - 1; i >= 0; i--) {
                            final AdsUIConnection c = ((AdsUIDef) ui).getConnections().get(i);
                            if (w.getId().equals(c.getSenderId())) {
                                c.delete();
                            }
                        }
                    }
                    final BaseWidget wg = (BaseWidget) scene.findWidget(node);
                    AdsUIUtil.removeContainerListener(node, wg);
                    if (scene.isNode(node)) //!!! bug
                    {
                        scene.removeNode(node);
                    }
                }
            }
        }, true);

        // fix later
        scene.validate();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                scene.revalidate();
                scene.validate();
            }
        });
    }

    @Override
    protected Cursor getCursorAt(Point localLocation) {

        if (!isResizable()) {
            return Cursor.getDefaultCursor();
        }

        Rectangle bounds = getBounds();
        int thic = ResizeBorder.THICKNESS;

        int dy = localLocation.y - bounds.y;
        int dx = localLocation.x - bounds.x;
        int hh = bounds.height / 2 - thic / 2, hw = bounds.width / 2 - thic / 2,
                ix = dx / hw, iy = dy / hh;

        dy %= hh;
        if (dy < 0 || dy >= thic) {
            iy = -1;
        }

        dx %= hw;
        if (dx < 0 || dx >= thic) {
            ix = -1;
        }

        return getResizeCursor(ix, iy);
    }
    private static final int[][] resizeCursor = new int[][]{
        {Cursor.NW_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR},
        {Cursor.N_RESIZE_CURSOR, Cursor.DEFAULT_CURSOR, Cursor.S_RESIZE_CURSOR},
        {Cursor.NE_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR}
    };

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        setBorder(state.isSelected() ? BORDER_SELECTED : BORDER_EMPTY);
    }

    @Override
    public boolean isHitAt(Point localLocation) {
        final Point sceneLocation = convertLocalToScene(localLocation);
        final Rectangle visibleRect = getVisibleWidgetGeometry();

        return isVisible() && (visibleRect.contains(sceneLocation)
                || (getState().isSelected() && ResizeAction.WIDGET_CONTROL_POINT_RESOLVER.resolveControlPoint(this, localLocation) != null));
    }

    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        final List<WidgetAction> actions = new ArrayList<>();
        actions.add(new KeyAction());
        actions.add(ActionFactory.createSelectAction(new BaseSelectProvider(), true));
        WidgetPopupMenu popupMenu = createPopupMenu();
        if (popupMenu != null) {
            actions.add(ActionFactory.createPopupMenuAction(popupMenu));
        }
        actions.add(new ResizeAction());
        actions.add(new MoveAction(scene.getInterLayer()));
        actions.add(new GoToDoubleClickAction());
        return actions;
    }

    protected WidgetPopupMenu createPopupMenu() {
        return new WidgetPopupMenu();
    }

    public void startContainerListening() {
        AdsUIUtil.addContainerListener(getNode(), this);
    }

    public void stopContainerListening() {
        AdsUIUtil.removeContainerListener(getNode(), this);
    }

    public void setGeometry(int x, int y, int width, int height) {
        Insets insets = getBorder().getInsets();
        setPreferredLocation(new Point(x, y));
        setPreferredBounds(new Rectangle(-insets.left, -insets.top, width + insets.left + insets.right, height + insets.top + insets.bottom));
    }

    public final void setGeometry(Rectangle r) {
        setGeometry(r.x, r.y, r.width, r.height);
    }

    public Point convertLocalToGeometry(Point localPoint) {
        return convertLocalToScene(localPoint);
    }

    public BaseWidget getContainerWidget() {
        RadixObject container = getNode().getContainer();

        while (container != null) {
            Widget widget = getSceneImpl().findWidget(container);
            if (widget != null) {
                return (BaseWidget) widget;
            }
            container = container.getContainer();
        }
        return null;
    }

    public void saveGeometry() {
        RadixObject node = getNode();
        AdsUIProperty.RectProperty geom = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(node, "geometry");
        if (geom == null) {
            return;
        }

        Rectangle rect = getGeometry();
        geom.x = rect.x;
        geom.y = rect.y;
        geom.width = rect.width;
        geom.height = rect.height;

        BaseWidget ownerWidget = getContainerWidget();
        if (ownerWidget != null) {
            rect = ownerWidget.getGeometry();
            geom.x -= rect.x;
            geom.y -= rect.y;
        }

        // delete if Layout is owner
        if (node instanceof AdsWidgetDef) {
            if (node.getContainer() instanceof AdsLayout.Item && geom.getContainer() != null) {
                geom.delete();
                return;
            }
        }

        if (geom.getContainer() == null) {
            AdsUIUtil.setUiProperty(node, geom);
        }
    }

    public void restoreGeometry() {
        RadixObject node = getNode();
        AdsUIProperty.RectProperty geom = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(node, "geometry");
        if (geom == null || geom.getContainer() == null) {
            return;
        }

        int x = geom.x, y = geom.y;
        BaseWidget ownerWg = getContainerWidget();
        if (ownerWg != null) {
            Rectangle r = ownerWg.getGeometry();
            x += r.x;
            y += r.y;

            ownerWg = ownerWg.getContainerWidget();
        }

        setGeometry(x, y, geom.width, geom.height);
    }

    protected Insets getInsets() {
        return getBorder().getInsets();
    }

    public Rectangle getInnerGeometry() {
        return getGeometry();
    }

    public Rectangle getGeometry() {
        Point location = getPreferredLocation();
        if (location == null) {
            location = getLocation();
        }
        Insets insets = getBorder().getInsets();
        Rectangle bounds = getPreferredBounds();

        assert insets != null : "Insets is null";
        assert bounds != null : "Bounds is null";
        assert location != null : "Location is null";

        return new Rectangle(
                location.x + bounds.x + insets.left,
                location.y + bounds.y + insets.top,
                bounds.width - insets.right - insets.left,
                bounds.height - insets.top - insets.bottom);
    }

    public Rectangle getLayoutGeometry() {
        RadixObject node = getNode();
        Item item = Item.getItem(node);
        assert item != null;
        return item.adjustLayoutGeometry(node, getGeometry());
    }

    /**
     * @return avaliable region for children
     */
    public Rectangle getContentGeometry() {
        RadixObject node = getNode();
        Item item = Item.getItem(node);
        return item.adjustContentGeometry(node, getGeometry());
    }

    protected final Rectangle getVisibleWidgetGeometry() {
        Rectangle geometry = getGeometry();
        BaseWidget parentWidget = getContainerWidget();

        if (getNode() instanceof AdsRwtWidgetDef && AdsMetaInfo.RWT_ACTION.equals(((AdsRwtWidgetDef) getNode()).getClassName())) {
            return geometry;
        }

        if (!isSelfContained() && parentWidget != null) {
            Rectangle visibleRect = parentWidget.getVisibleWidgetGeometry();
            Rectangle containerRect = parentWidget.getContentGeometry();

            geometry = geometry.intersection(visibleRect).intersection(containerRect);
        }
        return geometry;
    }

    public Rectangle getWidgetRect() {
        return convertGeometryToLocal(getGeometry());
    }

    protected final Rectangle convertGeometryToLocal(Rectangle geometry) {
        return convertSceneToLocal(geometry);
    }

    public void setLayoutProcessor(LayoutProcessor layoutProcessor) {
        this.layoutProcessor = layoutProcessor;
    }

    public LayoutProcessor getLayoutProcessor() {
        return layoutProcessor;
    }

    public void locate(DragDropLocator locator, Point localPoint) {
        if (layoutProcessor != null) {
            layoutProcessor.locate(locator, localPoint);
        }
    }

    protected RadixObject addTo(BaseWidget parent, Point localPoint) {
        if (parent.getLayoutProcessor() != null) {
            return parent.getLayoutProcessor().add(getNode(), localPoint);
        }
        return null;
    }

    protected RadixObject removeFrom(BaseWidget parent) {
        if (parent.getLayoutProcessor() != null) {
            return parent.getLayoutProcessor().remove(getNode());
        }
        return null;
    }

    public final RadixObject add(BaseWidget widget, Point localPoint) {
        RadixObject result = null;
        synchronized (LayoutProcessor.LOCK) {
            if (widget.isSelfContained() || layoutProcessor != null) {
                stopContainerListening();
                AdsUIUtil.setUiContainer(widget.getNode(), null);

                result = widget.addTo(this, localPoint);

                startContainerListening();
            }
            return result;
        }
    }

    public final RadixObject remove(BaseWidget widget) {
        synchronized (LayoutProcessor.LOCK) {
            RadixObject result = null;

            if (widget.isSelfContained() || layoutProcessor != null) {
                    stopContainerListening();
                    final AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(widget.getNode());

                    result = widget.removeFrom(this);

                    AdsUIUtil.setUiContainer(widget.getNode(), uiDef); // for dragging !!!
                    startContainerListening();
            }
            return result;
        }
    }

    public boolean isContainer() {
        return AdsUIUtil.isContainer(getNode());
    }

    boolean isContainerFor(String clazz) {
        Layout layout = getLayout();
        if (layout instanceof AbsoluteLayout) {
            if (AdsMetaInfo.SPACER_CLASS.equals(clazz)) {
                return false;
            }
        }
        RadixObject obj = getNode();
        if (obj instanceof AdsRwtWidgetDef) {
            if (AdsMetaInfo.RWT_PROPERTIES_GRID.equals(((AdsRwtWidgetDef) obj).getClassName())) {
                return AdsMetaInfo.RWT_PROP_EDITOR.equals(clazz);
            }
        }
        return isContainer();
    }

    public boolean isContainer(String clazz, Point localLocation) {

        if (!isHitAt(localLocation) || !isContainer()) {
            return false;
        }

        return isContainerFor(clazz);
    }

    public boolean isLayout() {
        return getNode() instanceof AdsLayout;
    }

    public RadixObject getNode() {
        return widgetNode; // return (RadixObject)getSceneImpl().findObject(this);
    }

    public GraphSceneImpl getSceneImpl() {
        return (GraphSceneImpl) super.getScene();
    }

    public void setModified() {
        getNode().setEditState(RadixObject.EEditState.MODIFIED);
    }

    public void toFront() {
        AdsUIUtil.visitUI(getNode(), new IVisitorUI() {

            @Override
            public void visit(RadixObject node, boolean active) {
                BaseWidget wg = (BaseWidget) getSceneImpl().findWidget(node);
                if (wg != null) {
                    wg.bringToFront();
                }
            }
        }, true);
    }

    public void visible(final boolean visible) {
        AdsUIUtil.visitUI(getNode(), new IVisitorUI() {

            @Override
            public void visit(RadixObject node, boolean active) {
                BaseWidget wg = (BaseWidget) getSceneImpl().findWidget(node);
                if (wg != null) {
                    wg.setVisible(active && visible);
                }
            }
        }, true);
    }

    protected void notifySelected(Point localLocation) {
    }

    public void notifyClicked(Point localLocation) {
    }

    protected WidgetAction createInplaceEditorAction(final String propName, final EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections) {
        return ActionFactory.createInplaceEditorAction(new TextFieldInplaceEditor() {

            @Override
            public boolean isEnabled(Widget widget) {
                return true;
            }

            @Override
            public String getText(Widget widget) {
                RadixObject node = getNode();
                AdsUIProperty property = AdsUIUtil.getUiProperty(node, propName);
                if (property instanceof AdsUIProperty.LocalizedStringRefProperty) {
                    AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) property;
                    return Item.getTextById(node, text.getStringId());
                }
                return null;
            }

            @Override
            public void setText(Widget widget, String s) {
                RadixObject node = getNode();

                AdsUIProperty property = AdsUIUtil.getUiProperty(node, propName);
                if (property instanceof AdsUIProperty.LocalizedStringRefProperty) {
                    AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) property;
                    Item.setTextById(node, text.getStringId(), s);
                    AdsUIUtil.fire(node, text, BaseWidget.this);
                    getScene().revalidate();
                    getScene().validate();
                }
            }
        }, expansionDirections);
    }

    protected final Cursor getResizeCursor(int ix, int iy) {
        if (ix >= 0 && iy >= 0 && ix < 3 && iy < 3) {
            return Cursor.getPredefinedCursor(resizeCursor[ix][iy]);
        }

        return Cursor.getDefaultCursor();
    }

    public boolean canChangeLayout() {
        return AdsUIUtil.isContainer(getNode());
    }

    public Point offsetPoint() {
        return new Point(0, 0);
    }

    public boolean isSelfContained() {
        return false;
    }

    public boolean isResizable() {
        return true;
    }
}
