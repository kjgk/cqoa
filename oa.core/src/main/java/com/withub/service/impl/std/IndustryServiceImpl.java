package com.withub.service.impl.std;


import com.withub.model.std.po.Industry;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.IndustryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("industryService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class IndustryServiceImpl extends EntityServiceImpl implements IndustryService {

    public Industry getRootIndustry() throws Exception {

        return (Industry) getRootEntity(Industry.class);
    }
}
