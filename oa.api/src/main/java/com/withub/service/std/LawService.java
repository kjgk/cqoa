package com.withub.service.std;

import com.withub.model.std.po.Law;
import com.withub.model.std.po.LawCategory;
import com.withub.service.EntityService;

public interface LawService extends EntityService {

    public LawCategory getRootLawCategory() throws Exception;

    public void addLaw(Law law) throws Exception;

    public void updateLaw(Law law) throws Exception;
}