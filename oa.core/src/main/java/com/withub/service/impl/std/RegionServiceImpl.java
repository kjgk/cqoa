package com.withub.service.impl.std;

import com.withub.model.std.po.Region;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.RegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("regionService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class RegionServiceImpl extends EntityServiceImpl implements RegionService {

    public Region getDefaultCountry() throws Exception {

        Region region = (Region) getByPropertyValue(Region.class, "nodeTag", "China");
        return region;
    }

    public Region getCountryByCityId(String cityId) throws Exception {

        Region city = (Region) get(Region.class, cityId);
        Region parent = city.getParent();
        while (!parent.getRegionType().getCodeTag().equalsIgnoreCase("Country")) {
            parent = parent.getParent();
        }
        return parent;
    }
}
