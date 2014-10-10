package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.MeetingRoom;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.MeetingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("meetingService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class MeetingServiceImpl extends EntityServiceImpl implements MeetingService {

    public MeetingRoom getMeetingRoom(String objectId) throws Exception {

        return get(MeetingRoom.class, objectId);
    }

    public RecordsetInfo<MeetingRoom> queryMeetingRoom(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveMeetingRoom(MeetingRoom meetingRoom) throws Exception {

        save(meetingRoom);
    }

    public void deleteMeetingRoom(String objectId) throws Exception {

        logicDelete(MeetingRoom.class, objectId);
    }

}
