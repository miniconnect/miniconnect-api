package hu.webarticum.miniconnect.lang;

import java.util.Objects;

public class FindPositionResult {

    private final boolean found;
    
    private final LargeInteger position;

    private FindPositionResult(boolean found, LargeInteger position) {
        this.found = found;
        this.position = position;
    }

    public static FindPositionResult of(boolean found, LargeInteger position) {
        return new FindPositionResult(found, position);
    }

    public static FindPositionResult found(LargeInteger position) {
        return of(true, position);
    }

    public static FindPositionResult notFound(LargeInteger position) {
        return of(false, position);
    }

    public boolean found() {
        return found;
    }

    public LargeInteger position() {
        return position;
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(found, position);
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == this) {
    		return true;
    	}
    	if (obj == null) {
    		return false;
    	}
    	if (!(obj instanceof FindPositionResult)) {
    		return false;
    	}
    	FindPositionResult other = (FindPositionResult) obj;
    	return (
    			found == other.found &&
    			position.equals(other.position));
    }

    @Override
    public String toString() {
        return found ? "Found at " + position : "Not found; insert at " + position;
    }
    
}
