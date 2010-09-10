/**
 * 
 */
package org.vimeoid.connection.advanced;

/**
 * <dl>
 * <dt>Project:</dt> <dd>vimeoid</dd>
 * <dt>Package:</dt> <dd>org.vimeoid.connection.advanced</dd>
 * </dl>
 *
 * <code>Methods</code>
 *
 * <p>Description</p>
 *
 * @author Ulric Wilfred <shaman.sir@gmail.com>
 * @date Sep 3, 2010 7:26:46 PM 
 *
 */
public class Methods {
    
    public static class activity {
        
        private static final String PREFIX = "activity."; 
        
        public static final String happenedToUser = PREFIX + "happenedToUser";
        public static final String userDid = PREFIX + "userDid";
        
    }
    
    public static class albums {
        
        private static final String PREFIX = "albums."; 
        
        public static final String addToWatchLater = PREFIX + "addToWatchLater";
        public static final String addVideo = PREFIX + "addVideo";
        public static final String create = PREFIX + "create";
        public static final String delete = PREFIX + "delete";
        public static final String getAll = PREFIX + "getAll";
        public static final String getVideos = PREFIX + "getVideos";
        public static final String getWatchLater = PREFIX + "getWatchLater";
        public static final String removeFromWatchLater = PREFIX + "removeFromWatchLater";
        public static final String removeVideo = PREFIX + "removeVideo";
        public static final String setDescription = PREFIX + "setDescription";
        public static final String setPassword = PREFIX + "setPassword";
        public static final String setTitle = PREFIX + "setTitle";
        
    }
    
    public static class channels {
        
        private static final String PREFIX = "channels."; 
        
        public static final String addVideo = PREFIX + "addVideo";
        public static final String getAll = PREFIX + "getAll";
        public static final String getInfo = PREFIX + "getInfo";
        public static final String getModerated = PREFIX + "getModerated";
        public static final String getModerators = PREFIX + "getModerators";
        public static final String getSubscribers = PREFIX + "getSubscribers";
        public static final String getVideos = PREFIX + "getVideos";
        public static final String removeVideo = PREFIX + "removeVideo";
        public static final String subscribe = PREFIX + "subscribe";
        public static final String unsubscribe = PREFIX + "unsubscribe";
        
    }
    
    public static class contacts {
        
        private static final String PREFIX = "contacts."; 
        
        public static final String getAll = PREFIX + "getAll";
        public static final String getMutual = PREFIX + "getMutual";
        public static final String getOnline = PREFIX + "getOnline";
        public static final String getWhoAdded = PREFIX + "getWhoAdded";
        
    }
    
    public static class groups {
        
        private static final String PREFIX = "groups."; 
        
        public static final String addVideo = PREFIX + "addVideo";
        public static final String getAddable = PREFIX + "getAddable";
        public static final String getAll = PREFIX + "getAll";
        public static final String getFiles = PREFIX + "getFiles";
        public static final String getInfo = PREFIX + "getInfo";
        public static final String getMembers = PREFIX + "getMembers";
        public static final String getModerators = PREFIX + "getModerators";
        public static final String getVideoComments = PREFIX + "getVideoComments";
        public static final String getVideos = PREFIX + "getVideos";
        public static final String join = PREFIX + "join";
        public static final String leave = PREFIX + "leave";
        
        public static class events {
            
            private static final String PREFIX = groups.PREFIX + "events."; 
            
            public static final String getMonth = PREFIX + "getMonth";
            public static final String getPast = PREFIX + "getPast";
            public static final String getUpcoming = PREFIX + "getUpcoming";
            
        }
        
        public static class forums {
            
            private static final String PREFIX = groups.PREFIX + "forums."; 
            
            public static final String getTopicComments = PREFIX + "getTopicComments";
            public static final String getTopics = PREFIX + "getTopics";
            
        }        
        
    }
    
    public static class oauth {
        
        private static final String PREFIX = "oauth."; 
        
        public static final String checkAccessToken = PREFIX + "checkAccessToken";
        public static final String convertAuthToken = PREFIX + "convertAuthToken";
        
    }
    
    public static class people {
        
        private static final String PREFIX = "people."; 
        
        public static final String addContact = PREFIX + "addContact";
        public static final String addSubscription = PREFIX + "addSubscription";
        public static final String findByEmail = PREFIX + "findByEmail";
        public static final String getInfo = PREFIX + "getInfo";
        public static final String getPortraitUrls = PREFIX + "getPortraitUrls";
        public static final String getSubscriptions = PREFIX + "getSubscriptions";
        public static final String removeContact = PREFIX + "removeContact";
        public static final String removeSubscription = PREFIX + "removeSubscription";
        
    }
    
    public static class test {

        private static final String PREFIX = "test."; 
        
        // public static final String echo = PREFIX + "echo";
        public static final String login = PREFIX + "login";
        public static final String _null = PREFIX + "null";
        
        
    }
    
    public static class videos {
        
        private static final String PREFIX = "people."; 
        
        //public static final String addCast = PREFIX + "addCast";
        //public static final String addPhotos = PREFIX + "addPhotos";
        public static final String addTags = PREFIX + "addTags";
        public static final String clearTags = PREFIX + "clearTags";
        public static final String delete = PREFIX + "delete";
        public static final String getAll = PREFIX + "getAll";
        public static final String getAppearsIn = PREFIX + "getAppearsIn";
        public static final String getByTag = PREFIX + "getByTag";
        public static final String getCast = PREFIX + "getCast";
        public static final String getContactsLiked = PREFIX + "getContactsLiked";
        public static final String getContactsUploaded = PREFIX + "getContactsUploaded";
        public static final String getInfo = PREFIX + "getInfo";
        public static final String getLikes = PREFIX + "getLikes";
        public static final String getSubscriptions = PREFIX + "getSubscriptions";
        public static final String getThumbnailUrls = PREFIX + "getThumbnailUrls";
        public static final String getUploaded = PREFIX + "getUploaded";
        public static final String removeCast = PREFIX + "removeCast";
        public static final String removeTag = PREFIX + "removeTag";
        public static final String search = PREFIX + "search";
        public static final String setDescription = PREFIX + "setDescription";
        public static final String setLike = PREFIX + "setLike";
        public static final String setPrivacy = PREFIX + "setPrivacy";
        public static final String setTitle = PREFIX + "setTitle";
        
        public static class comments {
            
            private static final String PREFIX = videos.PREFIX + "comments."; 
            
            public static final String addComment = PREFIX + "addComment";
            public static final String deleteComment = PREFIX + "deleteComment";
            public static final String editComment = PREFIX + "editComment";
            public static final String getList = PREFIX + "getList";
            
        }
        
        /* public static class embed {
            
            private static final String PREFIX = videos.PREFIX + "embed."; 
            
            public static final String getPresets = PREFIX + "getPresets";
            public static final String setPreset = PREFIX + "setPreset";
            
        } */
        
        /* public static class upload {
            
            private static final String PREFIX = videos.PREFIX + "upload."; 
            
            public static final String checkTicket = PREFIX + "checkTicket";
            public static final String complete = PREFIX + "complete";
            public static final String getQuota = PREFIX + "getQuota";
            public static final String verifyChunks = PREFIX + "verifyChunks";
            
        } */      
        
    }    
    
    

}
