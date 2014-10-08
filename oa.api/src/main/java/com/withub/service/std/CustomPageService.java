package com.withub.service.std;

import com.withub.model.std.po.CustomPage;
import com.withub.service.EntityService;

public interface CustomPageService extends EntityService {


    void saveCustomPage(CustomPage customPage) throws Exception;

}
