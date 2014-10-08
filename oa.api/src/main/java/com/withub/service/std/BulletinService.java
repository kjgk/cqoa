package com.withub.service.std;

import com.withub.model.std.po.Bulletin;
import com.withub.service.EntityService;

public interface BulletinService extends EntityService {

    public void addBulletin(Bulletin bulletin) throws Exception;

    public void updateBulletin(Bulletin bulletin) throws Exception;

    public void issueBulletin(String bulletinId) throws Exception;

    public void issueBulletin(Bulletin bulletin) throws Exception;
}