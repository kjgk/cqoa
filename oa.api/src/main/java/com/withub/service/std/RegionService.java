package com.withub.service.std;

import com.withub.model.std.po.Region;
import com.withub.service.EntityService;


public interface RegionService extends EntityService {

    public Region getDefaultCountry() throws Exception;

    public Region getCountryByCityId(String cityId) throws Exception;
}