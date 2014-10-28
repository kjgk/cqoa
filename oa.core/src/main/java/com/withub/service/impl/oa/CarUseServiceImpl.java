package com.withub.service.impl.oa;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.CarUse;
import com.withub.model.oa.po.CarUse;
import com.withub.model.oa.po.CarUseInfo;
import com.withub.model.system.po.Code;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.CarUseService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("carUseUseService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CarUseServiceImpl extends EntityServiceImpl implements CarUseService {

    @Autowired
    private CodeService codeService;

    public CarUse getCarUse(String objectId) throws Exception {

        return get(CarUse.class, objectId);
    }

    public RecordsetInfo<CarUse> queryCarUse(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void deleteCarUse(String objectId) throws Exception {

        logicDelete(CarUse.class, objectId);
    }

    public void submitCarUse(CarUse carUse) throws Exception {

        if (StringUtil.isEmpty(carUse.getObjectId())) {
            addCarUse(carUse);
        } else {
            updateCarUse(carUse);
        }
    }

    @Override
    public void addCarUse(CarUse carUse) throws Exception {

        Code status = codeService.getCodeByTag("CarUseStatus", "Create");
        carUse.setStatus(status);
        carUse.setProposer(carUse.getCurrentUser());
        save(carUse);
    }

    @Override
    public void updateCarUse(CarUse carUse) throws Exception {

        save(carUse);
    }


    @Override
    public void addCarUseInfo(CarUseInfo carUseInfo) throws Exception {

        save(carUseInfo);
    }
}
