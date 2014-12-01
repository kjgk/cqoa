package com.withub.service.impl.oa;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.CarUse;
import com.withub.model.oa.po.CarUseInfo;
import com.withub.model.oa.po.CarUseUser;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.CarUseService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("carUseService")
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

    public void submitCarUse(CarUse carUse, User approver) throws Exception {

        addCarUse(carUse);
    }

    public void submitCarUse(CarUse carUse) throws Exception {

        updateCarUse(carUse);
    }

    public void addCarUse(CarUse carUse) throws Exception {

        Code status = codeService.getCodeByTag("CarUseStatus", "Create");
        carUse.setStatus(status);
        carUse.setProposer(carUse.getCurrentUser());
        carUse.setOrganization(carUse.getCurrentUser().getOrganization());
        save(carUse);

        for (CarUseUser carUseUser : carUse.getCarUseUserList()) {
            carUseUser.setCarUse(carUse);
            save(carUseUser);
        }
    }

    public void updateCarUse(CarUse carUse) throws Exception {

        CarUse temp = get(CarUse.class, carUse.getObjectId());
        carUse.setProposer(carUse.getCurrentUser());
        carUse.setOrganization(carUse.getCurrentUser().getOrganization());
        carUse.setStatus(temp.getStatus());
        save(carUse);

        executeHql("delete from CarUseUser a where a.carUse.objectId = ?", carUse.getObjectId());

        for (CarUseUser carUseUser : carUse.getCarUseUserList()) {
            carUseUser.setCarUse(carUse);
            save(carUseUser);
        }
    }


    public void addCarUseInfoList(CarUse carUse) throws Exception {

        // 将用车状态设置为已分配
        CarUse old = getCarUse(carUse.getObjectId());
        old.setCurrentUser(carUse.getCurrentUser());
        old.setStatus(codeService.getCodeByTag("CarUseStatus", "Alloted"));
        save(old);

        executeHql("delete from " + CarUseInfo.class.getName() + " a where a.carUse.objectId = ? ", carUse.getObjectId());
        for (CarUseInfo carUseInfo : carUse.getCarUseInfoList()) {
            carUseInfo.setCurrentUser(carUse.getCurrentUser());
            carUseInfo.setCarUse(old);
            save(carUseInfo);
        }
    }
}
