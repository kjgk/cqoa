package com.withub.service.std;

import com.withub.model.std.po.Favorite;
import com.withub.service.EntityService;

public interface FavoriteService extends EntityService {

    public void add(Favorite favorite, String objectType) throws Exception;

    public void update(Favorite favorite) throws Exception;
}
