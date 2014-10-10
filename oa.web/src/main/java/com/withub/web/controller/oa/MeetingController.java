package com.withub.web.controller.oa;


import com.withub.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/oa/meeting")
public class MeetingController extends BaseController {


    @RequestMapping(value = "/meetingRooms", method = RequestMethod.GET)
    public void meetingRooms(ModelMap modelMap) throws Exception {

    }

}
