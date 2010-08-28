/**
 * 
 */
package org.vimeoid;

import java.util.List;

import android.database.AbstractCursor;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid</dd>
 * </dl>
 *
 * <code>ExtractablesCursor</code>
 *
 * <p>Description</p>
 * 
 * @deprecated Currently not implemented finally
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Aug 28, 2010 5:47:34 PM 
 *
 */
@Deprecated
public class ExtractablesCursor extends AbstractCursor {
    
    private final List<? extends Extractable> source;
    private final String[] keys;
    
    public ExtractablesCursor(List<? extends Extractable> source, String[] keys) {
        this.source = source;
        this.keys = keys;
    }

    @Override
    public String[] getColumnNames() {
        return keys;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public double getDouble(int column) {
        return 0;
    }

    /* (non-Javadoc)
     * @see android.database.AbstractCursor#getFloat(int)
     */
    @Override
    public float getFloat(int column) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.database.AbstractCursor#getInt(int)
     */
    @Override
    public int getInt(int column) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.database.AbstractCursor#getLong(int)
     */
    @Override
    public long getLong(int column) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.database.AbstractCursor#getShort(int)
     */
    @Override
    public short getShort(int column) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.database.AbstractCursor#getString(int)
     */
    @Override
    public String getString(int column) {
        return null;
    }

    /* (non-Javadoc)
     * @see android.database.AbstractCursor#isNull(int)
     */
    @Override
    public boolean isNull(int column) {
        // TODO Auto-generated method stub
        return false;
    }

}
