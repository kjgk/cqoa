package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.MeetingRoom;

public interface MeetingService {

    public MeetingRoom getMeetingRoom(String objectId) throws Exception;

    public RecordsetInfo<MeetingRoom> queryMeetingRoom(QueryInfo queryInfo) throws Exception;

    public void saveMeetingRoom(MeetingRoom meetingRoom) throws Exception;

    public void deleteMeetingRoom(String objectId) throws Exception;

}
