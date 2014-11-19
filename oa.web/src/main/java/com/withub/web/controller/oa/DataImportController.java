package com.withub.web.controller.oa;

import com.withub.service.oa.DataImportService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/oa")
public class DataImportController extends BaseController {

    @Autowired
    private DataImportService dataImportService;


    @RequestMapping(value = "/dataImport/organization")
    public void importOrganization() throws Exception {

        dataImportService.importOrganization();
    }

    @RequestMapping(value = "/dataImport/user")
    public void importUser() throws Exception {

        dataImportService.importUser();
    }


}
