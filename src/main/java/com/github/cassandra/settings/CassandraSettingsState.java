package com.github.cassandra.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "com.github.cassandra.settings.CassandraSettingsState",
    storages = @Storage("cassandra-settings.xml")
)
public class CassandraSettingsState implements PersistentStateComponent<CassandraSettingsState> {
    public String hosts = "localhost";
    public int port = 9042;
    public String username = "";
    public String password = "";
    public String keyspace = "";
    public String datacenter = "datacenter1";

    public static CassandraSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(CassandraSettingsState.class);
    }

    @Nullable
    @Override
    public CassandraSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CassandraSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
} 