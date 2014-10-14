package com.withub.web.controller.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.Training;
import com.withub.service.oa.TrainingService;
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
@RequestMapping(value = "/oa")
public class TrainingController extends BaseController {

    @Autowired
    private TrainingService trainingService;

    @RequestMapping(value = "/training", method = RequestMethod.GET)
    public void queryTraining(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Training.class);
        setPageInfoQueryCondition(request, queryInfo);

        putRecordsetInfo(modelMap, trainingService.queryTraining(queryInfo));
    }

    @RequestMapping(value = "/training/{objectId}", method = RequestMethod.GET)
    public void getTraining(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        Training training = trainingService.getTraining(objectId);
        putData(modelMap, training);
    }

    @RequestMapping(value = "/training", method = RequestMethod.POST)
    public void createTraining(@RequestBody Training training) throws Exception {

        trainingService.saveTraining(training);
    }

    @RequestMapping(value = "/training", method = RequestMethod.PUT)
    public void updateTraining(@RequestBody Training training) throws Exception {

        trainingService.saveTraining(training);
    }


    @RequestMapping(value = "/training/{objectId}", method = RequestMethod.DELETE)
    public void deleteTraining(@PathVariable("objectId") String objectId) throws Exception {

        trainingService.deleteTraining(objectId);
    }

}
