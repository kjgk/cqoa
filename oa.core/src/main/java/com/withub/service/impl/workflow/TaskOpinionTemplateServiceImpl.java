package com.withub.service.impl.workflow;

import com.withub.service.EntityServiceImpl;
import com.withub.service.workflow.TaskOpinionTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("taskOpinionTemplateService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class TaskOpinionTemplateServiceImpl extends EntityServiceImpl implements TaskOpinionTemplateService {
}
