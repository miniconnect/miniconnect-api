package hu.webarticum.miniconnect.api;

import java.io.InputStream;

import hu.webarticum.miniconnect.lang.CheckableCloseable;

public interface MiniSession extends CheckableCloseable {

    public MiniResult execute(String query);

    public MiniLargeDataSaveResult putLargeData(String variableName, long length, InputStream dataSource);

}
