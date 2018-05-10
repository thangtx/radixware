
/* Radix::Acs.Dlg::RoleArrPropEditor - Desktop Executable*/

/*Radix::Acs.Dlg::RoleArrPropEditor-Desktop Dynamic Class*/

package org.radixware.ads.Acs.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleArrPropEditor")
public published class RoleArrPropEditor  extends org.radixware.kernel.explorer.widgets.propeditors.PropArrEditor<org.radixware.kernel.common.types.ArrStr>  {

	private class DisplayStringProviderArrStr implements Explorer.ValEditors::IDisplayStringProvider {

	    @Override
	    public String format(final Explorer.EditMask::EditMask mask, final Object value, final String defaultDisplayString) {
	        ArrStr guids = (ArrStr) value;

	        if (guids != null && !guids.isEmpty()) {
	            return RoleEditorUtils.RoleTitleCache.getTitleByGuids(guids, getEnvironment());
	        }

	        final org.radixware.kernel.common.enums.EEditMaskType maskType = mask == null ? null : mask.getType();
	        final boolean isEditMaskCompatible
	                = maskType == org.radixware.kernel.common.enums.EEditMaskType.LIST || maskType == org.radixware.kernel.common.enums.EEditMaskType.ENUM || maskType == org.radixware.kernel.common.enums.EEditMaskType.BOOL;
	        final Explorer.Models.Properties::Property property = getProperty();
	        final org.radixware.kernel.common.enums.EValType valType = property.getType();
	        final boolean isValTypeCompatible = valType.isArrayType() || valType == org.radixware.kernel.common.enums.EValType.OBJECT || valType == org.radixware.kernel.common.enums.EValType.XML;
	        final boolean displayStringCanBeOverrided = isEditMaskCompatible || isValTypeCompatible || isReadonly();
	        if (displayStringCanBeOverrided) {
	            return getProperty().getOwner().getDisplayString(property.getId(), value, defaultDisplayString, !property.hasOwnValue());
	        }
	        return defaultDisplayString;
	    }
	}

	/*Radix::Acs.Dlg::RoleArrPropEditor:Nested classes-Nested Classes*/

	/*Radix::Acs.Dlg::RoleArrPropEditor:Properties-Properties*/

	/*Radix::Acs.Dlg::RoleArrPropEditor:Methods-Methods*/

	/*Radix::Acs.Dlg::RoleArrPropEditor:RoleArrPropEditor-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleArrPropEditor:RoleArrPropEditor")
	public published  RoleArrPropEditor (org.radixware.kernel.common.client.models.items.properties.Property property) {
		super(property);
		getValEditor().setDisplayStringProvider(new DisplayStringProviderArrStr());
	}


}

/* Radix::Acs.Dlg::RoleArrPropEditor - Desktop Meta*/

/*Radix::Acs.Dlg::RoleArrPropEditor-Desktop Dynamic Class*/

package org.radixware.ads.Acs.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleArrPropEditor_mi{
}

/* Radix::Acs.Dlg::RoleArrPropEditor - Localizing Bundle */
package org.radixware.ads.Acs.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleArrPropEditor - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(RoleArrPropEditor - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcV4SUMDH6JVDBTBP3TC6L2EICI4"),"RoleArrPropEditor - Localizing Bundle",$$$items$$$);
}
