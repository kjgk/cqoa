package com.withub.web.controller.oa;


import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.MeetingRoom;
import com.withub.service.oa.MeetingService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/oa/meeting")
public class MeetingController extends BaseController {

    @Autowired
    private MeetingService meetingService;


    @RequestMapping(value = "/meetingRoom", method = RequestMethod.GET)
    public void queryMeetingRoom(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(MeetingRoom.class);
        setPageInfoQueryCondition(request, queryInfo);

        putRecordsetInfo(modelMap, meetingService.queryMeetingRoom(queryInfo));
    }

    @RequestMapping(value = "/meetingRoom/{objectId}", method = RequestMethod.GET)
    public void getMeetingRoom(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        MeetingRoom meetingRoom = meetingService.getMeetingRoom(objectId);
        putData(modelMap, meetingRoom);
    }

    @RequestMapping(value = "/meetingRoom", method = RequestMethod.POST)
    public void createMeetingRoom(@RequestBody MeetingRoom meetingRoom) throws Exception {

        meetingService.saveMeetingRoom(meetingRoom);
    }

    @RequestMapping(value = "/meetingRoom", method = RequestMethod.PUT)
    public void updateMeetingRoom(@RequestBody MeetingRoom meetingRoom) throws Exception {

        meetingService.saveMeetingRoom(meetingRoom);
    }


    @RequestMapping(value = "/meetingRoom/{objectId}", method = RequestMethod.DELETE)
    public void deleteMeetingRoom(@PathVariable("objectId") String objectId) throws Exception {

        meetingService.deleteMeetingRoom(objectId);
    }

}
