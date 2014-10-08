package com.withub.service.impl.std;


import com.withub.service.EntityServiceImpl;
import com.withub.service.std.ServerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("serverService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class ServerServiceImpl extends EntityServiceImpl implements ServerService {

}
