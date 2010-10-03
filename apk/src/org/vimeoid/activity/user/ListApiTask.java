/**
 * 
 */
package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.dto.advanced.PagingData;
import org.vimeoid.util.ApiParams;

import android.util.Log;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ListApiTask</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 3, 2010 12:22:41 PM 
 *
 */
public class ListApiTask extends ApiTask {
    
    private static final String TAG = "ListApiTask"; 
    
    private final ApiObjectsReceiver receiver;
    
    private int maxPages = 3;
    private int curPage = 1;
    private int perPage = 20;
    
    private ApiParams curParams;
    
    public ListApiTask(ApiObjectsReceiver receiver, String apiMethod) {
        super(apiMethod);
        this.receiver = receiver;
    }
    
    private ListApiTask(int curPage, ApiObjectsReceiver receiver, String apiMethod) {
        this(receiver, apiMethod);
        this.curPage = curPage;
    }
    
    @Override
    protected ApiParams extractParams(ApiParams... paramsList) {
        final ApiParams curParams = super.extractParams(paramsList);
        return curParams.add("page", String.valueOf(curPage))
                        .add("per_page", String.valueOf(perPage));
                      // TODO: params.add("sort", "");
    }
    
    @Override
    public void onAnswerReceived(JSONObject jsonObj) throws JSONException {
        
        receiver.addPage(jsonObj);
        PagingData pd = receiver.getLastPagingData();
        
        if (pd.pageNum != curPage) throw new IllegalStateException("Received page number do not matches actual");
        if (pd.perPage != perPage) throw new IllegalStateException("Received number of items per page do not matches actual");
        
        final int received = pd.onThisPage;
        final int total = pd.total;
        
        if ((received == 0) && (curPage == 1)) {
            // no items in list at all
            onNoItems();
            /* getListView().removeFooterView(footerView);
            emptyImage.setImageResource(R.drawable.no_more_small);
            emptyText.setText(R.string.no_items_in_list); */            
        } else if ((received < perPage) ||     
                   (curPage == maxPages) ||
                   ((total != -1) && (((perPage * (curPage - 1)) + received) == total))) {
            // no items more
            onNoMoreItems();
            //footerView.setVisibility(View.GONE);
        } else {
            // enable 'load more' button
            onNextPageExists();
            /* footerView.setEnabled(true);            
            footerText.setTextColor(getResources().getColor(R.color.load_more_default_text));
            footerText.setBackgroundResource(R.color.load_more_default_bg); 
            footerText.setText(R.string.load_more); */
        }
        
        Log.d(TAG, "Received " + received + " items");
        
        afterRequest(received);
        
        // TODO: allow to load next page manually, also check if we received all
        if (curPage < maxPages) new ListApiTask(++curPage, receiver, apiMethod).execute(curParams);        
        
        /*onContentChanged();
        
        // TODO: scroll to the first received item (smoothScrollToPosition in API 8)
        final int newPos = getListView().getCount() - howMuch - 2; // - 'load more' and one position before
        if (newPos >= 0) setSelection(newPos);
        else setSelection(0); */
        
        /* 
         * 
         * } else Dialogs.makeToast(this, getString(R.string.no_pages_more));
        } else Dialogs.makeToast(this, getString(R.string.please_do_not_touch)); */
        
    }
    
    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages; 
    }
    
    public void setPerPage(int perPage) {
        this.perPage = perPage; 
    }
    
    private void onNoMoreItems() { }

    private void onNextPageExists() { }

    private void afterRequest(int received) { }

    protected void onNoItems() { }
    
}
