/**
 * 
 */
package org.vimeoid.connection;

public enum ContentType { USER, VIDEO, GROUP, CHANNEL, ALBUM, ACTIVITY, TAG, 
                          COMMENT, LIKE, MESSAGE;

    public static ContentType fromAlias(String subjectType) {
        if ("user".equals(subjectType)) return USER;
        if ("video".equals(subjectType)) return VIDEO;
        if ("group".equals(subjectType)) return GROUP;
        if ("channel".equals(subjectType)) return CHANNEL;
        if ("album".equals(subjectType)) return ALBUM;
        if ("activity".equals(subjectType)) return ACTIVITY;
        if ("tag".equals(subjectType)) return TAG;
        if ("comment".equals(subjectType)) return COMMENT;
        if ("like".equals(subjectType)) return LIKE;
        if ("message".equals(subjectType)) return MESSAGE;
        throw new IllegalArgumentException("Unknown subject type: " + subjectType);
    }
    
    public String getAlias() {
        return name().toLowerCase();
    }

}