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
package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.WeakListeners;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.common.defs.ads.ui.enums.ECursorShape;
import org.radixware.kernel.common.defs.ads.ui.enums.UIEnum;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.utils.Utils;

public class UIPropertySupport extends PropertySupport implements PropertyChangeListener {

    private final RadixObject node;
    private AdsUIProperty prop;
    private Reference<PropertyEditor> edRef = null;

    @SuppressWarnings("unchecked")
    public UIPropertySupport(AdsUIProperty prop, AdsAbstractUIDef uidef, RadixObject node) {
        super(prop.getName(), getClass(prop), prop.getName(), prop.getName(), true, !AdsUIUtil.isReadOnlyNode(uidef, node));
        String propName = prop.getName();
        if (AdsUIUtil.isUIObject(node) && (propName.equals("geometry") || propName.equals("currentIndex") || prop instanceof AdsUIProperty.LocalizedStringRefProperty)) {
            AdsUIUtil.addPropertyChangeListener(node, WeakListeners.propertyChange(this, node));
        }
        if (prop instanceof AdsUIProperty.StringProperty && !"html".equals(prop.getName())) {
            setValue("oneline", Boolean.TRUE);
        }
        this.node = node;
        this.prop = prop;
    }

    public RadixObject getNode() {
        return node;
    }

    public AdsUIProperty getProp() {
        return prop;
    }

    private static Class<?> getClass(AdsUIProperty prop) {
        if (prop instanceof AdsUIProperty.BooleanValueProperty) {
            return String.class;
        }
        if (prop instanceof AdsUIProperty.BooleanProperty) {
            return Boolean.TYPE;
        }
        if (prop instanceof AdsUIProperty.IntProperty) {
            return Integer.TYPE;
        }
        if (prop instanceof AdsUIProperty.LongProperty) {
            return Long.TYPE;
        }
        if (prop instanceof AdsUIProperty.DoubleProperty) {
            return Double.TYPE;
        }
        if (prop instanceof AdsUIProperty.FloatProperty) {
            return Float.TYPE;
        }
        if (prop instanceof AdsUIProperty.StringProperty) {
            return String.class;
        }
        if (prop instanceof AdsUIProperty.ColorProperty) {
            return Color.class;
        }
        if (prop instanceof AdsUIProperty.BrushProperty) {
            return Color.class;
        }
        if (prop instanceof AdsUIProperty.DateProperty || prop instanceof AdsUIProperty.DateTimeProperty || prop instanceof AdsUIProperty.TimeProperty) {
            return Calendar.class;
        }
        if (prop instanceof AdsUIProperty.EnumValueProperty) {
            AdsUIProperty.EnumValueProperty p = (AdsUIProperty.EnumValueProperty) prop;
            return p.value == null ? Object.class : p.value.getClass();
        }
        if (prop instanceof AdsUIProperty.CursorShapeProperty) {
            return ECursorShape.class;
        }
        if (prop instanceof AdsUIProperty.AccessProperty) {
            return EAccess.class;
        }
        return prop.getClass();
    }

    public void setProp(AdsUIProperty prop) {
        this.prop = prop;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        if (prop instanceof AdsUIProperty.BooleanProperty) {
            return ((AdsUIProperty.BooleanProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.IntProperty) {
            return ((AdsUIProperty.IntProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.LongProperty) {
            return ((AdsUIProperty.LongProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.DoubleProperty) {
            return ((AdsUIProperty.DoubleProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.FloatProperty) {
            return ((AdsUIProperty.FloatProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.StringProperty) {
            return ((AdsUIProperty.StringProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.ColorProperty) {
            return ((AdsUIProperty.ColorProperty) prop).color;
        }
        if (prop instanceof AdsUIProperty.BrushProperty) {
            return ((AdsUIProperty.BrushProperty) prop).color;
        }
        if (prop instanceof AdsUIProperty.EnumValueProperty) {
            return ((AdsUIProperty.EnumValueProperty) prop).value;
        }
        if (prop instanceof AdsUIProperty.CursorShapeProperty) {
            return ((AdsUIProperty.CursorShapeProperty) prop).shape;
        }
        if (prop instanceof AdsUIProperty.AccessProperty) {
            return ((AdsUIProperty.AccessProperty) prop).getAccess();
        }
        if (prop instanceof AdsUIProperty.BooleanValueProperty) {
            return ((AdsUIProperty.BooleanValueProperty) prop).value;
        }
        return prop;
    }

    @Override
    public void setValue(Object val) {
        try {
            if (!(node instanceof AdsCustomWidgetDef)) {
                UiProperties props = AdsUIUtil.getUiProperties(node);
                if (props.indexOf(prop) < 0) {
                    props.add(prop);
                }
            }

            if (val instanceof AdsUIProperty.PropertyRefProperty) {
                prop = (AdsUIProperty) val;
                AdsUIUtil.setUiProperty(node, prop);
            }

            if (prop instanceof AdsUIProperty.BooleanValueProperty) {
                if (!Utils.equals(((AdsUIProperty.BooleanValueProperty) prop).value, (Boolean) val)) {
                    ((AdsUIProperty.BooleanValueProperty) prop).value = (Boolean) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.BooleanProperty) {
                if (!Utils.equals(((AdsUIProperty.BooleanProperty) prop).value, (Boolean) val)) {
                    ((AdsUIProperty.BooleanProperty) prop).value = (Boolean) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.IntProperty) {
                if (!Utils.equals(((AdsUIProperty.IntProperty) prop).value, (Integer) val)) {
                    ((AdsUIProperty.IntProperty) prop).value = (Integer) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.LongProperty) {
                if (!Utils.equals(((AdsUIProperty.LongProperty) prop).value, (Long) val)) {
                    ((AdsUIProperty.LongProperty) prop).value = (Long) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.DoubleProperty) {
                if (!Utils.equals(((AdsUIProperty.DoubleProperty) prop).value, (Double) val)) {
                    ((AdsUIProperty.DoubleProperty) prop).value = (Double) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.FloatProperty) {
                if (!Utils.equals(((AdsUIProperty.FloatProperty) prop).value, (Float) val)) {
                    ((AdsUIProperty.FloatProperty) prop).value = (Float) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.StringProperty) {
                final String value = (String) val;
                final String name = prop.getName();
                if (name.equals("objectName") || name.equals("layoutName") || name.equals("spacerName")) {
                    if (!Utils.equals(name, AdsUIUtil.getUiName(node))) {
                        AdsUIUtil.setUiName(node, value);
                    }
                } else {
                    if (!Utils.equals(((AdsUIProperty.StringProperty) prop).value, value)) {
                        ((AdsUIProperty.StringProperty) prop).value = value;
                        prop.setEditState(EEditState.MODIFIED);
                    }
                }
            }
            if (prop instanceof AdsUIProperty.ColorProperty) {
                if (!Utils.equals(((AdsUIProperty.ColorProperty) prop).color, (Color) val)) {
                    ((AdsUIProperty.ColorProperty) prop).color = (Color) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.BrushProperty) {
                if (!Utils.equals(((AdsUIProperty.BrushProperty) prop).color, (Color) val)) {
                    ((AdsUIProperty.BrushProperty) prop).color = (Color) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.EnumValueProperty) {
                if (!Utils.equals(((AdsUIProperty.EnumValueProperty) prop).value, (UIEnum) val)) {
                    ((AdsUIProperty.EnumValueProperty) prop).value = (UIEnum) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.CursorShapeProperty) {
                if (!Utils.equals(((AdsUIProperty.CursorShapeProperty) prop).shape, (ECursorShape) val)) {
                    ((AdsUIProperty.CursorShapeProperty) prop).shape = (ECursorShape) val;
                    prop.setEditState(EEditState.MODIFIED);
                }
            }
            if (prop instanceof AdsUIProperty.AccessProperty) {
                ((AdsUIProperty.AccessProperty) prop).setAccess((EAccess) val);
            }

            if (prop instanceof AdsUIProperty.RectProperty
                    || prop instanceof AdsUIProperty.SizeProperty
                    || prop instanceof AdsUIProperty.SizePolicyProperty) {

                prop.setEditState(EEditState.MODIFIED);
            }

            if (prop instanceof AdsUIProperty.EmbeddedEditorOpenParamsProperty
                    || prop instanceof AdsUIProperty.EmbeddedSelectorOpenParamsProperty) {

                prop.setEditState(EEditState.MODIFIED);
            }

            AdsUIUtil.fire(node, prop, this);
        } catch (Throwable ex) {
            java.util.logging.Logger.getLogger(UIPropertySupport.class.getName()).log(Level.WARNING, "Can not change property", ex);
        }
        
    }
    private final static Set<String> extendableProps = new HashSet<>(Arrays.asList("toolTip"));

    @Override
    public PropertyEditor getPropertyEditor() {

        //if (edRef == null) {
        if (prop instanceof AdsUIProperty.StringProperty && "buddy".equals(prop.getName())) {
            edRef = new SoftReference<PropertyEditor>(new BuddyEditor(this));
        } else if (prop instanceof AdsUIProperty.RectProperty) {
            edRef = new SoftReference<PropertyEditor>(new RectEditor(this));
        } else if (prop instanceof AdsUIProperty.SizeProperty) {
            edRef = new SoftReference<PropertyEditor>(new SizeEditor(this));
        } else if (prop instanceof AdsUIProperty.SizePolicyProperty) {
            edRef = new SoftReference<PropertyEditor>(new SizePolicyEditor(this));
        } else if (prop instanceof AdsUIProperty.LocalizedStringRefProperty) {

            boolean extendedMode = extendableProps.contains(prop.getName());

            edRef = new SoftReference<PropertyEditor>(new LocalizedStringEditor(this, extendedMode));
        } else if (prop instanceof AdsUIProperty.ImageProperty) {
            edRef = new SoftReference<PropertyEditor>(new ImageEditor(this));
        } else if (prop instanceof AdsUIProperty.SetProperty) {
            if ("alignment".equals(prop.getName()) || "textAlignment".equals(prop.getName())) {
                edRef = new SoftReference<PropertyEditor>(new AlignmentEditor(this));
            }
            if ("standardButtons".equals(prop.getName())) {
                edRef = new SoftReference<PropertyEditor>(new StandardButtonsEditor(this));
            }
        } else if (prop instanceof AdsUIProperty.DateTimeProperty) {
            edRef = new SoftReference<PropertyEditor>(new DateTimeEditor(this));
        } else if (prop instanceof AdsUIProperty.DateProperty) {
            edRef = new SoftReference<PropertyEditor>(new DateEditor(this));
        } else if (prop instanceof AdsUIProperty.TimeProperty) {
            edRef = new SoftReference<PropertyEditor>(new TimeEditor(this));
        } else if (prop instanceof AdsUIProperty.PropertyRefProperty) {
            edRef = new SoftReference<PropertyEditor>(new PropertyRefEditor(this));
        } else if (prop instanceof AdsUIProperty.CommandRefProperty) {
            edRef = new SoftReference<PropertyEditor>(new CommandRefEditor(this));
        } else if (prop instanceof AdsUIProperty.EditorPageRefProperty) {
            edRef = new SoftReference<PropertyEditor>(new EditorPageRefEditor(this));
        } else if (prop instanceof AdsUIProperty.EmbeddedEditorOpenParamsProperty) {
            edRef = new SoftReference<PropertyEditor>(new EmbeddedEditorParamsEditor(this));
        } else if (prop instanceof AdsUIProperty.EmbeddedSelectorOpenParamsProperty) {
            edRef = new SoftReference<PropertyEditor>(new EmbeddedSelectorParamsEditor(this));
        } else if (prop instanceof AdsUIProperty.AccessProperty) {
            edRef = new SoftReference<PropertyEditor>(new AccessEditor(this));
        } else if (prop instanceof AdsUIProperty.BooleanValueProperty) {
            edRef = new SoftReference<PropertyEditor>(new BoolValueEditor(this));
        } else if (prop instanceof AdsUIProperty.EnumRefProperty) {
            edRef = new SoftReference<PropertyEditor>(new EnumRefEditor(this));
        } else if (prop instanceof AdsUIProperty.AnchorProperty) {
            edRef = new SoftReference<PropertyEditor>(new AnchorEditor(this));
        } else if (prop instanceof AdsUIProperty.IdListProperty) {
            edRef = new SoftReference<PropertyEditor>(new IdListEditor(this));
        } else if (prop instanceof AdsUIProperty.TypeDeclarationProperty) {
            edRef = new SoftReference<PropertyEditor>(new TypeDeclarationEditor(this));
        }

        //}
        if (edRef != null) {
            return edRef.get();
        }

        return super.getPropertyEditor();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport) {
            return;
        }
        Sheet.Set set = (Sheet.Set) getValue("set");
        set.put(this); // refresh sheet
    }
}
