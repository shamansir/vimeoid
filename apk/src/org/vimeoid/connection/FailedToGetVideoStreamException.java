/**
 * 
 */
package org.vimeoid.connection;

@SuppressWarnings("serial")
public class FailedToGetVideoStreamException extends VideoStreamRequestException {
    
    public FailedToGetVideoStreamException(String description) {
        super(description);
    }
    
}