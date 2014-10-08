package com.withub.service.std;

import com.withub.model.std.po.Industry;
import com.withub.service.EntityService;


public interface IndustryService extends EntityService {

    public Industry getRootIndustry() throws Exception;

}