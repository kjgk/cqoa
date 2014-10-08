package com.withub.service.impl.std;

import com.withub.service.EntityServiceImpl;
import com.withub.service.std.NotifyServiceTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("notifyServiceTypeService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class NotifyServiceTypeServiceImpl extends EntityServiceImpl implements NotifyServiceTypeService {
}
