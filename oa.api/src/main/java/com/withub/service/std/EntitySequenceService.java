package com.withub.service.std;

import com.withub.model.std.po.EntitySequence;
import com.withub.service.EntityService;

public interface EntitySequenceService extends EntityService {

    public void createEntitySequence(EntitySequence entitySequence) throws Exception;

    public void updateEntitySequence(EntitySequence entitySequence) throws Exception;

}
