package com.withub.service.impl.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.std.po.EntitySequence;
import com.withub.model.std.po.EntitySequenceRegulation;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.EntitySequenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("entitySequenceService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class EntitySequenceServiceImpl extends EntityServiceImpl implements EntitySequenceService{

    public void createEntitySequence(EntitySequence entitySequence) throws Exception {

        String objectId = StringUtil.getUuid();
        entitySequence.setObjectId(objectId);
        save(entitySequence);

        if (CollectionUtil.isNotEmpty(entitySequence.getRegulationList())) {

            for (EntitySequenceRegulation entitySequenceRegulation : entitySequence.getRegulationList()) {
                entitySequenceRegulation.setEntitySequence(entitySequence);
                save(entitySequenceRegulation);
            }
        }
    }

    public void updateEntitySequence(EntitySequence entitySequence) throws Exception {

        save(entitySequence);

        List<EntitySequenceRegulation> tempList = get(EntitySequence.class, entitySequence.getObjectId()).getRegulationList();
        List<String> deleteIds = new ArrayList<String>();
        if (CollectionUtil.isNotEmpty(tempList)) {
            for (EntitySequenceRegulation temp : tempList) {
                boolean flag = true;
                if (CollectionUtil.isNotEmpty(entitySequence.getRegulationList())) {
                    for (EntitySequenceRegulation entitySequenceRegulation : entitySequence.getRegulationList()) {
                        if (entitySequenceRegulation.getObjectId() != null && entitySequenceRegulation.getObjectId().equals(temp.getObjectId())) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    deleteIds.add(temp.getObjectId());
                }
            }
        }

        if (CollectionUtil.isNotEmpty(deleteIds)) {
            for (String objectId : deleteIds) {
                delete(EntitySequenceRegulation.class, objectId);
            }
        }

        if (CollectionUtil.isNotEmpty(entitySequence.getRegulationList())) {
            for (EntitySequenceRegulation entitySequenceRegulation : entitySequence.getRegulationList()) {
                if (entitySequenceRegulation.getEntitySequence() == null) {
                    entitySequenceRegulation.setEntitySequence(entitySequence);
                }
                save(entitySequenceRegulation);
            }
        }
    }

}
