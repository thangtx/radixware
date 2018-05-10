
/* Radix::Types::Report - Server Executable*/

/*Radix::Types::Report-Server Dynamic Class*/

package org.radixware.ads.Types.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::Report")
public abstract published class Report  extends org.radixware.kernel.server.types.Report  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Report_mi.rdxMeta;}

	/*Radix::Types::Report:Nested classes-Nested Classes*/

	/*Radix::Types::Report:Properties-Properties*/





























	/*Radix::Types::Report:Methods-Methods*/

	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:checkSuitability-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:export-Platform Method Wrapper*/


	/*Radix::Types::Report:afterExecute-Platform Method Wrapper*/


	/*Radix::Types::Report:beforeExecute-Platform Method Wrapper*/


	/*Radix::Types::Report:getGenerationTime-Platform Method Wrapper*/


	/*Radix::Types::Report:getCurGroupRecordCount-Platform Method Wrapper*/


	/*Radix::Types::Report:getCurrentPageNumber-Platform Method Wrapper*/


	/*Radix::Types::Report:getSubItemCount-Platform Method Wrapper*/


	/*Radix::Types::Report:getCellsSumValue-Platform Method Wrapper*/


	/*Radix::Types::Report:getTotalRecordCount-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:beforeInputParams-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:export-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:adjustCsvColumnValue-Platform Method Wrapper*/


	/*Radix::Types::Report:calcOutputFileName-Platform Method Wrapper*/


	/*Radix::Types::Report:getPdfSecurityOptions-Platform Method Wrapper*/


	/*Radix::Types::Report:setPdfSecurityOptions-Platform Method Wrapper*/


	/*Radix::Types::Report:afterXlsxRowWrite-Platform Method Wrapper*/


	/*Radix::Types::Report:beforeXlsxRowWrite-Platform Method Wrapper*/


	/*Radix::Types::Report:beforeFirstXlsxRowWrite-Platform Method Wrapper*/


	/*Radix::Types::Report:afterLastXlsxRowWrite-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:execute-Platform Method Wrapper*/


	/*Radix::Types::Report:export-Platform Method Wrapper*/


	/*Radix::Types::Report:export-Platform Method Wrapper*/


	/*Radix::Types::Report:getCurrentExportFormat-Platform Method Wrapper*/


	/*Radix::Types::Report:getMaxResultSetCacheSizeKb-Platform Method Wrapper*/


	/*Radix::Types::Report:setMaxResultSetCacheSizeKb-Platform Method Wrapper*/


	/*Radix::Types::Report:export-Platform Method Wrapper*/


	/*Radix::Types::Report:setDbImageResizeSupressed-Platform Method Wrapper*/



}

/* Radix::Types::Report - Server Meta*/

/*Radix::Types::Report-Server Dynamic Class*/

package org.radixware.ads.Types.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Report_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcReport____________________"),"Report",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Types::Report:Properties-Properties*/
						null,

						/*Radix::Types::Report:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_report_execute___________"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("out",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprENLLPTXLYFBY7JO7SFYG4ZDXSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIKGBSHOZEFBYNGBJEA22KH6FR4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parameters",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCJSWJLE5WFDRDAZB2VRDPWAHE4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_report_check_suitability_"),"checkSuitability",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTBUBZYF3DFGBXBM52UTNKNCPZU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("stream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7FSE6BCNDVBAVJ7BGRZDLZKIFM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNW34QH4BEVFUZFB5E3BVVL6KXY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xReportMsdl",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSCDDMHR6BBAAVF5OCZRKQDXWAI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCXBYXMFWRJES3I3ZRTGW3ZAOCA"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("out",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5WGIVI3HMRHYJFXQDSE53QYX4Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE5BR54XDMZGRTAMZL3VMHKJNBA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKGBJ6Y3VP5BB5NBGBJVFEERTHA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGAROIDUNBJB2BLG4AZY6SCBGGU"),"afterExecute",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2TG5AZEOURAC3EGFHH6TXXY3R4"),"beforeExecute",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQHX4LLWWYVBYXGLICHUA2VLECU"),"getGenerationTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHHHY2ZA3EBHZLCUT2IKF7N6AIU"),"getCurGroupRecordCount",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNH3L2WGUIREMLLEREUSDDGHZZM"),"getCurrentPageNumber",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJAGTWE5CFBEJPJQJXB3R7RGIU4"),"getSubItemCount",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("arg0",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7QBSTBCRCZDNLBD3I5VCESDHYU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWCKFTGXHMNHSPFJSJ4V7TJAABM"),"getCellsSumValue",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("summaryType",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2U44LNF4H5B3NCNVLT4IEYODMA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propertyId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKJVWREZLDRF6PFMJZQVWZ7YVMQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("groupLevel",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT25PR4WR6FCDNLBV662B625XWU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5SVVMRH6D5BSFK7A4E5O2DBQHY"),"getTotalRecordCount",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKOX4Z4OYINGYXPIFY3DCPGB36A"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("stream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5SZY2D54D5GSNEBUK6OHHEBSKI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2FYDNP2XLJFZ5KO3X3VLSIJNSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportWriter",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZWKNZ6LNEFDFFGPNG5AQ5T7HKI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ77ZL4LE7ZA4DDSSG6NR3AV76E"),"beforeInputParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIM77KEEIJRF4LHMQ54UGRYZRWY"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("out",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKVJT5HSOKFBGDO4OQTLRUSMN54")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPWVBV4ASDFCRTHXAWJDXOTUKVQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("encoding",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAQCVVPPGBZBXRLHHYWFYHVWD6Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parameters",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPYRRKXEO65GZHMYDKTIZTTS7BY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXURV62NCSZGEJJKQBNVMHC4UWE"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFU77DXGYGRF7RD3YD2J3CA7OUQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOWWSIM5UZFDQ5OEIRDJL62IAYI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHDYZJOHWJJH7HFO4O4SDXLZAPY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN4QD6CA4PVEN7BKI34EILDBYQQ"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR3OWP6KCJRBOZNPZGZBFCJLFW4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRSB6A27CCVFVNDV7UIF5NRPQRE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("enconding",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKUKTIYTTFVCYXJUO4TSYWG2DJU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr63L5PZYOBBED3EUTJZU4P7FKDQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXVER56ODWRAWFOGLUA242RZ4QM"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCB3D6AKR7BF3FJKARNYW4SC7QQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7UYSRMG6PJAKJHLVJFIHVDFMUE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("writer",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYTS4LMTLHZDWBI2R6SKISOJWVI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBV5PGQGUWFD3BL5AJAT3GY3XDQ"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4CXJKDF2QRA2TFDWCEXNYODNKA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5CN47JPKZBE75EVYIDZOXNSD5U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("msdlType",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2DN3XKUYIRAKRLZCWCXCVAFLHI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOUG7P77GDJD7ZJC2FSOBHBOWWQ"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH2PYYXAN2RAHBPYT5ZLVRMDMWU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMKX3HD3DONA2NE3LHRSWGZIDPA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQGP3C5OBJAJDOTDYVVWMPV344"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMOMQLHBHAFBTVDDBMXLFKEFBFI"),"adjustCsvColumnValue",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rawValue",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3UDKLI2FIVCTTC7ZVWJARPQLEI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("autoEscapedValue",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4DEOU6J525DE7HPG2XVSWRDDTM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delimiterChar",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7PSWCOJW3BH3NKO7EWMSZZWKTY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD5S7RG6ULVC2NAWV3UJBGGAE5M"),"calcOutputFileName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileNumber",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMMNQ6HGFFJERJJYM5SDDSOYJCM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportFormat",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXIXCW4F2WFGGPDXPPGEVD5VKRI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("totalFileCount",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDDID7WTSNRAMPBAQIPONZVLSF4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIJXBWT5ACVH5FKOFFAIIKI75BU"),"getPdfSecurityOptions",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEQ4WY7DLKZF73BAGGPHBU255NU"),"setPdfSecurityOptions",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pdfSecurityOptions",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUTT5LV6NEZE35CPIWQKDQH6PKQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRVGXHBWXORARDN7NYKJP2PMXPU"),"afterXlsxRowWrite",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("row",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYBEGMRJOKZHYFJXLHEIQQZB4R4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJMS26JY7LBGWRBVAV2A5ZPKWZY"),"beforeXlsxRowWrite",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sheet",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMQKJGX6XAFHDVFKZVIL4UJTRPY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWYM7P6MLFJFKNG5NLDEP77JD6E"),"beforeFirstXlsxRowWrite",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sheet",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr63FBFT5SHJGFPNYKWLKLKJIFYY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMKTBKPJTI5FIVMU7TCAEIHMVXE"),"afterLastXlsxRowWrite",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sheet",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ4AHHD2ZZZGFTOOHTPMO2I36CM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEQWCBWDVYVFXTAHUB3ZX2QSPOU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2Q4VPN3VY5E27HD4TTL6GD4VXA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mimeTypes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr55HZSE5YCZFWVKKYOFJXA4FGQE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBYGQFXH6QZDCBKUZI345GGI7UM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxResultSetCacheSizeKb",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWVARKR3K2FAGFLOAJPXQY4TVWQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIXKJ2VVJRZEJ5PAN5VIM77LEPM"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJOFAQTJO45C43AAXBOUGHOFVLI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mimeTypes",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB7ULZDY7SBB4BD5AHT7Q7IH62I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("encoding",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7GDDWXQY6FCZ7L2OAAO6JX66PY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr76PPMQXZTVACPI5AFSOJ5LJN2A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxResultSetCacheSizeKb",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3MVZATHRD5G6NI5BHPMFTUK4BY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDZBMVLGPMRCADLZQDMVBNZTMNQ"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5BOSLOGMR5DHNKORN2VVFGRT6Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2M2NQFN6XRDFJH77POZQY64R6Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportFormats",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB4MIV6HBEZG3RFGM6WB44NQMKA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxResultSetCacheSizeKb",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR6CYAZHMBJFR7FAF5LM3DBTS3U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPV3DY7KVAVBGPLRHL2ODQA2EEI"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("controller",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH66AUKS2OFFHZFHM2WJETMDJQY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLRVNYCITU5EDPBEGQZEXIYTOIM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportFormats",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQA3XH6HNJC67GQ6TDOVAEB67Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxResultSetCacheSizeKb",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr25OUD4YYLRFCNDWOEII4UG5DTU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("encoding",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVO3BKGJ2OZC6ZIXTUUP7NGEDCY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWICC7CINLJDCZKFOJJNNIWK2RM"),"getCurrentExportFormat",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI5MPWDB2GVEP7JOG3W42CI6LGY"),"getMaxResultSetCacheSizeKb",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth43XMM5TUZ5G53MDQAU5XYLV7DI"),"setMaxResultSetCacheSizeKb",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("maxResultSetCacheSizeKb",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHUQP7O4YU5EPZKTWDUNI4I6XSM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5YK3XAASPZAR7MTAAYCYUUXXX4"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parameters",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5R44GD2JFFEHDHOSGI4JOP6UHQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQK6I2A2OFH3XE3YDTEBTW5V54"),"setDbImageResizeSupressed",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dbImageResizeSupressed",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4GPZ5OM3AJA4FOWQTX4XBGWE5I"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::Types::Report - Localizing Bundle */
package org.radixware.ads.Types.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Report - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Поток для записи данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2SZQAM7CPFFURJVES5ICDGOR24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество записей в подгруппе указанного уровня");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XEB3MHCGFBWJLAKIUZXJXO64U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2Y5FQKJFHRC2RK7WXSMJ6A56M4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение колонки автоматически заэкранированное генератором отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Column value that is automatically escaped by the report generator");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2ZQ52ER2XZDE7P7Z77XSGXWE2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип итогового значения (минимум, максимум, сумма и т.д.)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ISFI2LNJNDSRG43M4BT4YRISA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения текущей страницы отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4R4V5DS35NAYZPBIP2BESALH7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Поток для записи данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EVKY6SWK5E7JC76LIK7NCUXCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5IW65Y3PERF4XHYC2A4LTNKWCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контроллер файла отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5R76WA772RHH5FIPL6EWDYLFAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Итоговое значение, выводимое в полосу summary отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6PHIHPOUZJAURBB24J4BC5PP7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод проверки применимости отчета. Предназначен для перекрытия в классах-потомках для реализации специфичных проверок. Если данный метод возвращает false - отчет не отображается в списке доступных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7PJE52GFTJCSXKIPKDNGAMF26Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ULVQGSG75CPVFGL4EJFZCEFTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA5DUGSAR4RCXNOYYBZPCQFN5DY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Интерфейс генератора данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAABPLYZZDZHVFPZCKVWUYXBOU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check result");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAF3AU4PWXNDDVKG5HWAG7HFKCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"MSDL-тип отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"MSDL type of the report");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAOEFEXP44RGPPMJCECFUTLSGP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Интерфейс генератора данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPPCGML5AVDDLGRY2BZ7GNHZQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод расчета имени файла для экспорта данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBEH5AFREBFOVPQ35XBKDUBKLI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC6SSSRQRZBEJ7JWSCT4YZP4LDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYRDOYXR3FEC5G3SA4GWWJLHRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVP2HCCWRFFS5CP57NEZG5IT6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень группировки на котором подсчитывается итоговое значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKKZOMOPKZCNDM7HL5OZSBSVYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения количества записей в текущей группе");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFFD3P5WL2FE5ZG6P3NB72WXXMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор свойства по которому подсчитывается итоговое значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGDQ7REA6WJCZDN44SGDKXTMZXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод экспорта данных отчета в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGQAJEZSVGFERBKEOYKQQJDD4O4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Астрономическое время начала генерации отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH4PY3Z4AQVHRRLE3UBQZRNMZUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGF6T2KE5VELDB7YGYSYYYG57E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка в которой отчет записывается в поток");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTRIT2QVVNFPFCUMVBQ3U3GCMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уровень группировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKHAXDVFGZHGXLO6ROUCBT435Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIU7CIVYE6ZAIJMQZSVJZDGL4PE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее количество файлов в которые экспортируется отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIXBJURWURVEVHFXARDZSEHF5SE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение символа-разделителя");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value of the separator character");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRHFNRKZUJGODOZXQPYMJX7ZME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"MSDL-тип отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"MSDL type of the report");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKBCVNTRTGNCRBIBJIXNFXR4XOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Поток в который экспортируется отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTTQEYXJ45HSDAJV4LPJGZLXV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения даты и времени начала генерации отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDE7ADYBWRGCLBACL53R7RYMMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контроллер файла отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLSPMBUOINDOBBG2HNAKJZYJQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед выполнением отчета. Предназначен для перекрытия в классах потомках при необходимости выполнения каких-либо действий перед выполнением отчета.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMDCEGEJVSVAZJEYK4QOHHI3SDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество записей в текущей группе");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVULBU6JJZHCPJL4TG5WTOZIZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMYV2FGMVIZBCHDHPEFSULIYAS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLFRYBDUI5F77DF5XKEVBSBFMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNSVWAT6V4FD7DPJZ5WVFNVJCZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNTLKO6S4JNHBRBVEHV3SBCQZR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO3CTSQ766RBB7PWEK226Y6IFQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя файла для экспорта данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEUJBBHVYBBH3BDTMCLDUBQYW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контроллер файла отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsON7LB5S4ZBAMVLJSUFRZKAHDCY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс, представляющий отчет. Является базовым классом для всех отчетов.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wrapper for base class for all reports");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOQDTNUXMNJCZZAJGLJSUTR33PA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения итогового значения по группе строк");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP3C5AIUIEBEBPLLHONFH5VFULI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP7334W4KLZH6BBRR7MQNSCBCJI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBCZG5BGK5HADEBJFUAP42SKQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Поток для записи данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPIFRUSKNWVG2VKJ3FC2CJCR2YU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение колонки подлежащее записи в csv файл\n");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Column value to be written to a csv file");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPJFNJEEPEFCUPE7NVCDSSSK6LY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контроллер файла отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMYY5IFUHRFBTPBH5AGOWDUCZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSOX2RKOTNFKJIDFGUUTYJIEPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый после выполнения отчета. Предназначен для перекрытия в классах потомках при необходимости выполнения каких-либо действий после выполнения отчета.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPWGMTMQ6E5AYVBV4OZBAEOHASM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7MRFVHPSFDSVPH6W3Y3KVLPAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSISAS4FDDRAJZAAXSU2Q55E6MY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения количества записей в подгруппе");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTS35RNPCLRHZBDI2MYTZTEQEEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод получения общего количества строк в отчете");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU5YGIEFE2JFATG2WQAQ2KYVT3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report export format");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUJR2TYCRPZHQHBDE5AJGVEVTOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат экспорта отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report export format");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUN3ZNCCU2RE5FIDC6KZZDCPOPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Порядковый номер файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNNHK7MC2RDTXG3YPNLDMF6NMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик события, вызываемый перед установкой параметров отчета. Предназначен для перекрытия в классах-потомках при необходимости выполнения каких-либо действий перед установкой параметров");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVG7G5XK2PFBDDC7M4QHFMF4NOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество строк в отчете");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGQWDZE6YRAHHCVYDJDW6YRGTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5B2UYPGGJD4NH633Z4SMVDCQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Уточненное значение колонки отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHGUEFSOERFURE3LCD4QBTLEQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод предназначен для уточнения значения колонки отчета, выгружаемого в CSV формат.\nМожет использоваться например для принятия решения об обрамлении значения кавычками и т.д.\nПо умолчанию возвращает autoEscapedValue\n\n ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Refines the value of the column in the report being loaded to the CSV format.\nCan be used, for example, to decide whether the value needs to be quoted, etc.\nReturns autoEscapedValue by default ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWTBTXZZ6PVHVFILHFEPFNDSGBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5YY542QGJCC7N5CLFO3KXWI2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер текущей страницы отчета. Первая страница имеет номер 1, вторая - 2 и т.д.\nВозвращает 0, если нет ни одной страницы отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCVIXAZS4ZCO3ITCRAPZU3ZP6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контроллер файла отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG4OT4PKCZB7ZL5GMSS4PLHWCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Поток для записи данных отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIBVVPGAE5HYRHMXDH7TKFUR6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZMMWRPCWRZAOXPI77ZGSRQYSHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка в которой отчет записывается в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZQ2TDMJGLRBOTFWIF7NNBBAPME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод выполнения отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUXDRMOK5JHEHOKNIHPWGZKY4U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод экспорта данных отчета в поток");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZWR7ZCX5ZVEYZMEG5I3ANZZIBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_REP\\dev\\trunk\\org.radixware\\ads\\Types"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Report - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbpdcReport____________________"),"Report - Localizing Bundle",$$$items$$$);
}
