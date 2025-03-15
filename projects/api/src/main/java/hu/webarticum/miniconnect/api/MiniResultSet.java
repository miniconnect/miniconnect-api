package hu.webarticum.miniconnect.api;

import hu.webarticum.miniconnect.lang.CheckableCloseable;
import hu.webarticum.miniconnect.lang.ImmutableList;

public interface MiniResultSet extends CheckableCloseable {

    public ImmutableList<MiniColumnHeader> columnHeaders();
    
    public ImmutableList<MiniValue> fetch();
    
}
