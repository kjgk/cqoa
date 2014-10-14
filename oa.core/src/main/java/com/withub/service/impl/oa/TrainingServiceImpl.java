package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Training;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.TrainingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("trainingService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class TrainingServiceImpl extends EntityServiceImpl implements TrainingService {

    public Training getTraining(String objectId) throws Exception {

        return get(Training.class, objectId);
    }

    public RecordsetInfo<Training> queryTraining(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveTraining(Training training) throws Exception {

        save(training);
    }

    public void deleteTraining(String objectId) throws Exception {

        logicDelete(Training.class, objectId);
    }

}
