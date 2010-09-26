/**
 * 
 */
package org.vimeoid.connection;

@SuppressWarnings("serial")
public class FailedToGetVideoStreamException extends VideoLinkRequestException {
    
    public FailedToGetVideoStreamException(String description) {
        super(description);
    }
    
}