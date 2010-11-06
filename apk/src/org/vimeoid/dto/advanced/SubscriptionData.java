/**
 * 
 */
package org.vimeoid.dto.advanced;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.activity.base.ApiPagesReceiver;
import org.vimeoid.util.PagingData_;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.dto.advanced</dd>
 * </dl>
 *
 * <code>SubscriptionData</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Oct 12, 2010 11:57:04 PM 
 *
 */
public class SubscriptionData {
    
    public Map<SubscriptionType, Set<Long>> data;    
    
    public enum SubscriptionType { LIKES, UPLOADS, APPEARS, CHANNEL, GROUP;

        public static SubscriptionType fromString(String value) {
            if ("likes".equals(value)) return LIKES;
            if ("uploads".equals(value)) return UPLOADS;
            if ("appears".equals(value)) return APPEARS;
            if ("channel".equals(value)) return CHANNEL;
            if ("group".equals(value)) return GROUP;
            return null;
        } 
        
        public String toString() {
            return super.name().toLowerCase();
        }
        
        public static String list(SubscriptionType[] types) {
            final StringBuffer buffer = new StringBuffer();
            for (SubscriptionType type: types) {
                buffer.append(type.toString()).append(',');
            }
            return buffer.toString();
        }

        public static String[] toArray(Set<SubscriptionType> subscriptonsStatus) {
            if (subscriptonsStatus == null) return new String[0];
            final String[] result = new String[subscriptonsStatus.size()];
            int i = 0;
            for (SubscriptionType type: subscriptonsStatus) {
                result[i++] = type.toString();
            }
            return result; 
        }
        
        public static Set<SubscriptionType> fromArray(String[] subscriptonsStatus) {
            final Set<SubscriptionType> result = new HashSet<SubscriptionType>();
            for (String typeStr: subscriptonsStatus) {
                result.add(fromString(typeStr));
            }
            return result;
        }        
    
    };    
    
    public static final class FieldsKeys {
        
        public static final String MULTIPLE_KEY = "subscriptions";
        public static final String SINGLE_KEY = "subscription";
        
        public static final String SUBJECT_ID = "subject_id";
        public static final String TYPE = "type";
        
    }
    
    public SubscriptionData() {
        data = new HashMap<SubscriptionType, Set<Long>>();
        for (SubscriptionType type: SubscriptionType.values()) {
            data.put(type, new HashSet<Long>());
        }
    }
    
    public static SubscriptionData collectFromJson(JSONObject page) throws JSONException {
        final SubscriptionData data = new SubscriptionData();
        passTo(page, data);
        return data;
    }
    
    public static void passTo(JSONObject source, SubscriptionData destination) throws JSONException {
        JSONArray array = source.getJSONObject(FieldsKeys.MULTIPLE_KEY)
                                .getJSONArray(FieldsKeys.SINGLE_KEY);
        for (int i = 0; i < array.length(); i++) {
            final SubscriptionType type = SubscriptionType.fromString(
                    array.getJSONObject(i).getString(FieldsKeys.TYPE));
            final long subjectId = 
                    array.getJSONObject(i).getLong(FieldsKeys.SUBJECT_ID);            
            destination.data.get(type).add(subjectId);
        }
    }
    
    public static abstract class SubscriptionsReceiver implements ApiPagesReceiver<JSONObject> {
        
        private final String rootKey;
        
        private final SubscriptionData subscriptions = new SubscriptionData();
        private int received = 0;
        
        public SubscriptionsReceiver(final String rootKey) {
            this.rootKey = rootKey;
        }

        @Override
        public void addSource(JSONObject page) throws Exception {
            received += 
                page.getJSONObject(rootKey)
                    .getInt(PagingData.FieldsKeys.ON_THIS_PAGE);
            passTo(page, subscriptions);
        }

        @Override public int getCount() { return received; }

        @Override
        public PagingData_ getCurrentPagingData(JSONObject lastPage) throws JSONException {
            return PagingData.collectFromJson(lastPage, SubscriptionData.FieldsKeys.MULTIPLE_KEY);
        }
        
        public SubscriptionData getSubscriptions() { return subscriptions; }
        
    }
    

}
