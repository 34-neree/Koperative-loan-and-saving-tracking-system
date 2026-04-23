package dao;

import model.ShareConfig;

public interface ShareConfigDao {

    ShareConfig getConfig();

    void updateConfig(ShareConfig config);
}
