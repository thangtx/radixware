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

package org.radixware.kernel.server.arte.services.eas;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.schemas.eas.ExceptionEnum;

final class Messages {

    private Messages() {
    }
    static final String MLS_ID_USERLOGGED_IN_PASSWORD = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAQG7L7AK6VAATHQULTM6A6UZAU";//Event, "User "%1" logged in using station "%2" (%3). Authorized by password.",App.Audit
    static final String MLS_ID_USERLOGGED_IN_CERTIFICATE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsG3JM476GHZGCXJ5HJKKUZGMQDY";//Event, "User "%1" logged in using station "%2" (%3). Authorized by certificate",App.Audit
    static final String MLS_ID_USERLOGGED_IN_KERBEROS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsU3DNXFYE7ZCLHM2UH7EUILP7YE";//Event, "User "%1" logged in using station "%2" (%3). Authorized via kerberos protocol.",App.Audit
    static final String MLS_ID_USER_FAILED_TO_LOG_IN_PASSWORD = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsCTE44V7SHVHRTNTRXVNFWIWWRY";//Event, "User "%1" failed to log in using station "%2" (%3) and authorization by password" , App.Audit
    static final String MLS_ID_USER_FAILED_TO_LOG_IN_CERTIFICATE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsQPUDXX6WOFFXJKW3CHHLG2MHFM";//Event, "User "%1" failed to log in using station "%2" (%3) and authorization by certificate." , App.Audit
    static final String MLS_ID_USER_FAILED_TO_OPEN_SESSION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsY2RHF223AFBF7K2YTZTND73QOM";//Event, "User "%1" failed to open session using station "%2" (%3)." , App.Audit
    static final String MLS_ID_USER_FAILED_TO_OPEN_SESSION_WITH_REASON = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsP3ZVK4GFZ5HE5HVIK4V7FXK6RM";//Event, "User "%1" failed to open session using station "%2" (%3). Reason: %4." , App.Audit
    static final String MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS_WITH_REASON = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls7ZPF4GAB5ZE5LONEETM22QFWGE";//Event, "Unable to authenticate user on statition "%1" (%2) via kerberos protocol. Reason: %3.", App.Audit   
    static final String MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsLG43XUZ4NBAQLLX4F3ECBN5WXQ";//Event, "Unable to authenticate user on statition "%1" (%2) via kerberos protocol.", App.Audit
    static final String MLS_ID_TRY_TO_USE_LOCKED_USER = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsODXY2MR3S5EGJKYFEZCKG2UY4Y";//Warning, "Trying to use locked  user account "%1" from station "%2" (%3)." ,App.Audit
    static final String MLS_ID_LOGON_TIME_EXPIRED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsKK23KBIT6FANZGUXEUI4AZI3TY";//Event, "User "%1" failed to continue session using station "%2" (%3): logon time expires" ,App.Audit
    static final String MLS_ID_TEMPORARY_PASSWORD_EXPIRED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsG6CQL7XUU5CWBGAMIA33Z2VWPA";//Event, "User "%1" failed to continue session using station "%2" (%3): temporary password expired" ,App.Audit
    static final String MLS_ID_TRY_TO_USE_FORBIDDEN_AUTH_TYPE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsVEC6MFJPBFBWXNYQF7A5XR7YY4";//Event, "User "%1" failed to log in from station "%2" using forbidden authentication type "%3"." ,App.Audit
    static final String MLS_ID_LOGIN_REQUIRED = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIN3DJAQY6FG7TPPWYWI3YF3NF4";//Warning, "User "%1" failed to log in from station "%2": authentication process was not properly complete" ,App.Audit
    static final String MLS_ID_ATTEMP_TO_REGISTER_FROM_INVALID_STATION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsZMERMLJM5BCZZI5NAWJB27QWBQ";//Event, "Attempt to register in the system from non-existing station "%1" (%2)" ,App.Audit
    static final String MLS_ID_UNABLE_TO_LOAD_ROLE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsLXCSFHEJIBCSLH5VGW6I334OJM";//Error, "Unable to load role #%1 '%2': %3" , Arte.DefManager
    static final String MLS_ID_UNABLE_TO_LOAD_ALL_ROLES = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsH32XPQ2DRNFSBIE6GJAWMA2QWM";//Error, "Unable to load all roles assigned to current user, some functionality may be unaccessible." , Arte.DefManager    
    
    static final Id MLS_OWNER_ID = Id.Factory.loadFrom("adcXCB5KK6HMJH7NP6E642OHPOMXY");
    static final Id MLS_ID_INSUF_PRIV_TO_CONNECT_TO_EAS = Id.Factory.loadFrom("mlsLGV273ZM7BESDAVMGL5XQM7WPQ"); // "Insufficient privileges to connect to Explorer Access Service";
    static final Id MLS_ID_INSUF_PRIV_TO_CREATE_CUST_SRT = Id.Factory.loadFrom("mlsAY3YLT7555DFFGRTGSNOBNQ4TE"); // "Insufficient privileges to create custom sorting";
    static final Id MLS_ID_IN_PRESENTATION = Id.Factory.loadFrom("mlsXIEZI5BRQRGWZGPDMDKRWJIO3I"); // "in presentation";
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_ANY_CNTX_ED_PRES = Id.Factory.loadFrom("mls4PKZY277ZFEH5AJU6N45M7UX7E"); // "Insufficient privileges to access any editor presentation of current context";
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_ED_PRES = Id.Factory.loadFrom("mls5G2PDTRARZAJPONURN3UKWIX44"); // "Insufficient privileges to access editor presentation";
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_CNTX_ED_PRES = Id.Factory.loadFrom("mlsQ7G56Q7FOJCFXLC5RAGUEP7S2A"); // "Insufficient privileges to access context editor presentation";
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_EXPLORER_ROOT = Id.Factory.loadFrom("mls4GALBVMPQZE5XEWHUVFNNROF6U"); // "Insufficient privileges to access explorer root";
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_PROPERTY = Id.Factory.loadFrom("mlsLMAP32SLWFEFLKBLEPGLXWLBOA"); // "Insufficient privileges to access property";
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_SEL_PRES = Id.Factory.loadFrom("mlsZNBW4RD2SRHHZEXZMRWPSOQUWU"); // "Insufficient privileges to access selector presentation";
    static final Id MLS_ID_INSUF_PRIV_FOR_CONTEXTLESS_USAGE_OF_SEL_PRES = Id.Factory.loadFrom("mlsOPCW72NPKRDCRC5LCHEHP3T6MI"); //Insufficient privileges for contextless usage of selector presentation
    static final Id MLS_ID_INSUF_PRIV_TO_ACCESS_THIS_EI = Id.Factory.loadFrom("mlsW2V3NYUUQJDJNOMLBL7ZFEYIPA"); // "Insufficient privileges to access this explorer item";
    static final Id MLS_ID_INSUF_PRIV_TO_CREATE_OBJECT = Id.Factory.loadFrom("mls3QJYB7X5IZDRRD46RBOGI5VD2U"); // "Insufficient privileges to create object";
    static final Id MLS_ID_INSUF_PRIV_TO_DELETE_GROUP = Id.Factory.loadFrom("mlsL5VHWBU4NVCK7FY4YP4STHWKQI"); // "Insufficient privileges to delete group";
    static final Id MLS_ID_INSUF_PRIV_TO_DELETE_OBJECT = Id.Factory.loadFrom("mlsEFQN5VEANBEYJCC3ZR3GUQYHNQ"); // "Insufficient privileges to delete object";
    static final Id MLS_ID_INSUF_PRIV_TO_DEVELOP_USER_FUNC = Id.Factory.loadFrom("mlsWI2FHJTWPRFBTKKYD2BFY65CBA"); // "Insufficient privileges to develop user functions";
    static final Id MLS_ID_INSUF_PRIV_TO_EXECUTE_COMMAND = Id.Factory.loadFrom("mls4FZS3RKQI5GQ3FENJ7GHOIJGZU"); // "Insufficient privileges to execute command";
    static final Id MLS_ID_INSUF_PRIV_TO_EXECUTE_CNTXLESS_CMD = Id.Factory.loadFrom("mlsP35YBIX2J5E73NZ7X5PCQRHPLU"); // "Insufficient privileges to execute contextless command";
    static final Id MLS_ID_INSUF_PRIV_TO_TRACE_REQUEST = Id.Factory.loadFrom("mlsZLTMPDUB4RH7JOP4ZPNOVIFSFY"); // "Insufficient privileges to trace request";
    static final Id MLS_ID_INSUF_PRIV_TO_UPDATE_CONTEXT_OBJ = Id.Factory.loadFrom("mls6E7BO62HGZGYJALMQ75GYITYNQ"); // "Insufficient privileges to update context object";
    static final Id MLS_ID_INSUF_PRIV_TO_UPDATE_FOREIGN_KEY_PROP = Id.Factory.loadFrom("mlsISTZ7EYJ7BFNBF2WL2FOGLIVNU"); // "Insufficient privileges to update foreign key property";
    static final Id MLS_ID_INSUF_PRIV_TO_UPDATE_OBJECT = Id.Factory.loadFrom("mlsS4CM5ZQS3RF25MZYGIVAGIHBHE"); // "Insufficient privileges to update object";
    static final Id MLS_ID_INSUF_PRIV_TO_UPDATE_PROPERTY = Id.Factory.loadFrom("mlsL7PVOGD4LNCAVPZ2YBG3HW5MRI"); // "Insufficient privileges to update property";
    static final Id MLS_ID_SUBQUERIES_ARE_FORBIDDEN = Id.Factory.loadFrom("mls3Y3MSECEUBDYREKAMJQMGIJC54");//"Subqueries are forbidden in custom filters"
    
    static final String MLS_ID_EAS_PREVENTED_ACCESS_VIOLATION = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsF5YDUAZQWJER7OWVASLO7NWSS4"; // "Explorer Access Service prevented access violation: Message: %1, User: %2, Station: %3.", App.Audit, Warning
    static final String MLS_ID_EAS_CREATE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls5W25BATDVNHHRCHXF4SBWKOHVQ";//"User '%1' created '%2' with PID = '%3'", EAS, Event
    static final String MLS_ID_EAS_UPDATE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsBY37QFZGSFCM3A55V6VHTVM4VI";//"User '%1' updated '%2' with PID = '%3'", EAS, Event
    static final String MLS_ID_EAS_DELETE = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls5JSFN2GEBVBPRHTALOW6QRK7TE";//"User '%1' deleted '%2' with PID = '%3'", EAS, Event
    static final String MLS_ID_EAS_COMMAND = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls2IMGLRG36JEMDIJTR3TX7Z7DFQ";//"User '%1' executed command '%2' for '%3' with PID = '%4'", EAS, Event
    static final String MLS_ID_EAS_DELETE_GRP = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsUP2YG6ZOWJHWJBLKKU4XUMZOSU";//"User '%1' deleted group of '%2'  in presentation '%3' within context '%4' with  filter '%5'", EAS, Event
    static final String MLS_ID_EAS_GRP_COMMAND = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsFVOEE7GFI5BE5NUFGZCWXF2IXY";//"User '%1' executed group command '%2' for '%3'  in presentation '%4' within context '%5' with  filter '%6'", EAS, Event
    static final String MLS_ID_EAS_CNTXTLESS_COMMAND = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIVCAP25C6FGLDMIIR273L75IEI";//"User '%1' executed contextless command '%2'", EAS, Event
    static final String MLS_ID_EAS_UNABLE_TO_LOAD_COMMON_FILTERS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3KGDZERAF5HF7HOP4GN2CWUC7I"; //"Unable to load common filters for table %1 and presentation %2:\n%3"
    static final String MLS_ID_EAS_UNABLE_TO_LOAD_COMMON_FILTER = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsNQZEJMYXQBAQNBHMMKYMKJQM4E";  //"Unable to load common filter '%1' #%2:\n%3"
    static final String MLS_ID_EAS_UNABLE_TO_LOAD_COLOR_SCHEME = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsAHHOKNTHWFFJTH3ZM6DOEQ2CEU"; //"Unable to load color scheme for entity '%1' #%2:\n%3"    
    static final String MLS_ID_EAS_FAILED_TO_APPLY_COLOR_SCHEME = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls3N6SSIJUU5G2BEDA4TXCQGXW3M";  //"Failed to apply color scheme for entity '%1' #%2:\n%3"
    static final String MLS_ID_EAS_ERR_ON_RELEASING_RESOURCES = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsIINTMB5BSFB73N2FITRITVB3YE"; //"Error on releasing system resources:\n%1"
    static final String MLS_ID_EAS_ERR_ON_GET_PROP_VAL = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsXFWW3VAYGRB2RDRTC2TPNEAUBQ"; //"Failed to get value of property '%1' (#%2):\n%3"
    static final String MLS_ID_EAS_ERR_ON_SET_PROP_VAL = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls47RFVDVQJBGH7DD42SC4VFCRCY"; //"Failed to set value of property '%1' (#%2):\n%3"
    static final String MLS_ID_EAS_ERR_ON_LOAD_INST_CLASS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsSKEBWPNA4VF2BOJO46ZC5LJZGI";//"Failed to load class #%1 for entity object instantiation:\n%2"
    static final String MLS_ID_EAS_SKIPPED_BY_ACS_RECORDS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mls336LWVJNWZGTVARVFYUP4A2QGI";//%1 records loaded from database where skipped by EAS due to access control settings
    static final String MLS_ID_EAS_FILTERED_RECORDS = "mlbadcXCB5KK6HMJH7NP6E642OHPOMXY-mlsFXKLWG6VRFGVXFKXL5QH3NGOYU";//%1 records loaded from database where programmatically filtered
    
    static final Id MLS_ID_REASON_NONEXISTING_USER = Id.Factory.loadFrom("mls3O7VHNY4EVFLNFE6FOTG7BKHRI"); //"there is no such user"
    static final Id MLS_ID_REASON_INVALID_PASSWORD = Id.Factory.loadFrom("mls3I3LCABD4RCHTHTLO2CHPTK72Q");//invalid password
    static final Id MLS_ID_REASON_INVALID_STATION = Id.Factory.loadFrom("mlsRE45XYWYTBESJGGVGIOBS7CXKM");//invalid station
    static final Id MLS_ID_REASON_KERBEROS_DISABLED = Id.Factory.loadFrom("mlsJJNVXZH4RFHY7CHSKBBJINKYPE");//kerberos authentication disabled
    static final Id MLS_ID_REASON_LOGON_TIME_RESTRICTION_VIOLATION = Id.Factory.loadFrom("mlsMNFMLJ67PBBUVJR2IESWAFFMMU");//logon time restriction violation
    static final Id MLS_ID_REASON_SESSIONS_LIMIT_EXCEED = Id.Factory.loadFrom("mlsCZEES2GNMVEQVI4F7BHCGB3BOY");//maximum number of sessions exceeded
    static final Id MLS_ID_REASON_TEMPORARY_PASSWORD_EXPIRED = Id.Factory.loadFrom("mlsHPCELFFCSRF6RKLSVU4NWVMKFM");//temporary password expired
    static final Id MLS_ID_REASON_WEBDRIVER_NOT_ALLOWED = Id.Factory.loadFrom("mlsPQKBBW3YZRHMLF63XSABPOKAVA");//using of WebDriver is not allowed

    public static String getReasonForException(final Arte arte, final String easExcpetionReason) {
        final Id messageId;
        if (ExceptionEnum.INVALID_USER.toString().equals(easExcpetionReason)) {
            messageId = MLS_ID_REASON_NONEXISTING_USER;
        } else if (ExceptionEnum.INVALID_STATION.toString().equals(easExcpetionReason)) {
            messageId = MLS_ID_REASON_INVALID_STATION;
        } else if (ExceptionEnum.INVALID_PASSWORD.toString().equals(easExcpetionReason)) {
            messageId = MLS_ID_REASON_INVALID_PASSWORD;
        } else if (ExceptionEnum.LOGON_TIME_RESTRICTION_VIOLATION.toString().equals(easExcpetionReason)){
            messageId = MLS_ID_REASON_LOGON_TIME_RESTRICTION_VIOLATION;
        } else if (ExceptionEnum.SESSIONS_LIMIT_EXCEED.toString().equals(easExcpetionReason)){
            messageId = MLS_ID_REASON_SESSIONS_LIMIT_EXCEED;
        } else if (ExceptionEnum.TEMPORARY_PASSWORD_EXPIRED.toString().equals(easExcpetionReason)){
            messageId = MLS_ID_REASON_TEMPORARY_PASSWORD_EXPIRED;
        }else if (ExceptionEnum.WEB_DRIVER_IS_NOT_ALLOWED.toString().equals(easExcpetionReason)){
            messageId = MLS_ID_REASON_WEBDRIVER_NOT_ALLOWED;
        } else{
            messageId = null;
        }
        return messageId==null ? null : MultilingualString.get(arte, MLS_OWNER_ID, messageId);
    }
}
