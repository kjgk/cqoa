package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Training;

public interface TrainingService {

    public Training getTraining(String objectId) throws Exception;

    public RecordsetInfo<Training> queryTraining(QueryInfo queryInfo) throws Exception;

    public void deleteTraining(String objectId) throws Exception;

    public void submitTraining(Training training) throws Exception;

    public void addTraining(Training training) throws Exception;

    public void updateTraining(Training training) throws Exception;
    
}
