/**
 * 
 */
package org.vimeoid.activity.base;

import org.vimeoid.util.PagingData_;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.activity.user</dd>
 * </dl>
 *
 * <code>ApiPagesReceiver</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 3, 2010 12:33:58 PM 
 *
 */
public interface ApiPagesReceiver<PageObject> {
    
    public void addSource(PageObject object) throws Exception;
    public PagingData_ getCurrentPagingData(PageObject lastPage);
    public int getCount();

}
