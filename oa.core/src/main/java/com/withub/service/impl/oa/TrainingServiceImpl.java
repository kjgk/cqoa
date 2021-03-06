package com.withub.service.impl.oa;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Training;
import com.withub.model.oa.po.Training;
import com.withub.model.oa.po.Training;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.TrainingService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("trainingService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class TrainingServiceImpl extends EntityServiceImpl implements TrainingService {

    @Autowired
    private CodeService codeService;

    public Training getTraining(String objectId) throws Exception {

        return get(Training.class, objectId);
    }

    public RecordsetInfo<Training> queryTraining(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void deleteTraining(String objectId) throws Exception {

        logicDelete(Training.class, objectId);
    }

    public void submitTraining(Training training, User approver) throws Exception {

        addTraining(training);
    }

    public void submitTraining(Training training) throws Exception {

        updateTraining(training);
    }

    public void addTraining(Training training) throws Exception {

        Code status = codeService.getCodeByTag("TrainingStatus", "Create");
        training.setStatus(status);
        training.setProposer(training.getCurrentUser());
        training.setOrganization(training.getCurrentUser().getOrganization());
        save(training);
    }

    public void updateTraining(Training training) throws Exception {

        Training temp = get(Training.class, training.getObjectId());
        training.setProposer(training.getCurrentUser());
        training.setOrganization(training.getCurrentUser().getOrganization());
        training.setStatus(temp.getStatus());
        training.setTrainingType(temp.getTrainingType());
        save(training);
    }
}