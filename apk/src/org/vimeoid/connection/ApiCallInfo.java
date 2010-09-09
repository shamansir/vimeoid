/**
 * 
 */
package org.vimeoid.connection;

import android.os.Bundle;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.connection</dd>
 * </dl>
 *
 * <code>ApiCallInfo</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 9, 2010 8:16:03 PM 
 *
 */
public class ApiCallInfo {
    
    public StringBuffer apiUrlPart;
    public String subject;
    public String action;
    public ContentType subjectType;
    public ContentType resultType;
    public boolean multipleResult;
    
    public static class ExtrasKeys {
        public static final String API_URL_PART = "aci_apiUrlPart";
        public static final String SUBJECT = "aci_subject";
        public static final String ACTION = "aci_action";
        public static final String SUBJECT_TYPE = "aci_subjectType";
        public static final String RESULT_TYPE = "aci_resultType";
        public static final String MULTIPLE_RESULT = "aci_multipleResult";
    }
    
    protected void writeToExtras(Bundle bundle) {
        bundle.putString(ExtrasKeys.API_URL_PART, apiUrlPart.toString());
        bundle.putString(ExtrasKeys.SUBJECT, subject);
        bundle.putString(ExtrasKeys.ACTION, action);
        bundle.putString(ExtrasKeys.SUBJECT_TYPE, subjectType.getAlias());
        bundle.putString(ExtrasKeys.RESULT_TYPE, resultType.getAlias());
        bundle.putBoolean(ExtrasKeys.MULTIPLE_RESULT, multipleResult);
    }
    
    public static ApiCallInfo extractFromExtras(Bundle bundle) {
        final ApiCallInfo result = new ApiCallInfo();
        result.apiUrlPart = new StringBuffer(bundle.getString(ExtrasKeys.API_URL_PART));
        result.subject = bundle.getString(ExtrasKeys.SUBJECT);
        result.action = bundle.getString(ExtrasKeys.ACTION);
        result.subjectType = ContentType.fromAlias(bundle.getString(ExtrasKeys.SUBJECT_TYPE));
        result.resultType = ContentType.fromAlias(bundle.getString(ExtrasKeys.RESULT_TYPE));
        result.multipleResult = bundle.getBoolean(ExtrasKeys.MULTIPLE_RESULT);
        return result;
    }

}
