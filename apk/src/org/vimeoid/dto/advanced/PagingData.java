/**
 * 
 */
package org.vimeoid.dto.advanced;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.advanced</dd>
 * </dl>
 *
 * <code>PagingData</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 29, 2010 11:30:20 PM 
 *
 */
public class PagingData {
    
    public int perPage;
    public int pageNum;
    public int total;
    public int onThisPage;
    
    public static class FieldsKeys {
        public static final String PER_PAGE = "perpage";
        public static final String PAGE_NUM = "page_num";
        public static final String ON_THIS_PAGE = "on_this_page";
        public static final String TOTAL = "total";
    }
    
    public static PagingData collectFromJson(JSONObject jsonObj, String dataKey) throws JSONException {
        final JSONObject pagingSource = jsonObj.getJSONObject(dataKey); 
        
        final PagingData pagingData = new PagingData();
        
        pagingData.perPage = pagingSource.getInt(FieldsKeys.PER_PAGE);
        pagingData.pageNum = pagingSource.getInt(FieldsKeys.PAGE_NUM);
        pagingData.onThisPage = pagingSource.getInt(FieldsKeys.ON_THIS_PAGE);
        pagingData.total = pagingSource.getInt(FieldsKeys.TOTAL);
        
        return pagingData;
    }

}
