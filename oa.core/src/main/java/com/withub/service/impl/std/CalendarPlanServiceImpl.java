package com.withub.service.impl.std;

import com.withub.service.EntityServiceImpl;
import com.withub.service.std.CalendarPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("calendarPlanService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CalendarPlanServiceImpl extends EntityServiceImpl implements CalendarPlanService {
}
