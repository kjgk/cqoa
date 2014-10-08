package com.withub.web.controller.system;

import com.withub.model.system.po.Account;
import com.withub.service.system.AccountService;
import com.withub.util.ConfigUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class AccountController extends BaseController {

    //=============================== 属性声明 ===========================================================

    @Autowired
    private AccountService accountService;

    //============================== Controller 方法 =====================================================

    @RequestMapping(value = "/account/save", method = RequestMethod.POST)
    public void saveAccount(ModelMap modelMap, Account account) throws Exception {

        accountService.saveAccount(account);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/account/load/{objectId}", method = RequestMethod.GET)
    public void loadAccount(ModelMap modelMap, @PathVariable("objectId") String objectId) throws Exception {

        Account account = accountService.get(Account.class, objectId);
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("objectId", account.getObjectId());
        data.put("name", account.getName());
        data.put("password", "password");
        data.put("confirmPassword", "password");

        modelMap.put("data", data);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/account/loadPasswordConfig", method = RequestMethod.GET)
    public void loadPasswordConfig(ModelMap modelMap) throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();

        data.put("allowChangeAccount", ConfigUtil.getSecurityConfigInfo().isAllowChangeAccount());
        data.put("passwordStrengthType", ConfigUtil.getSecurityConfigInfo().getPasswordStrength());
        data.put("passwordLength", ConfigUtil.getSecurityConfigInfo().getPasswordLength());

        modelMap.put("data", data);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/account/createRandomPassword/{objectId}", method = RequestMethod.GET)
    public void createRandomPassword(ModelMap modelMap, @PathVariable("objectId") String objectId) throws Exception {

        Account account = accountService.get(Account.class, objectId);
        accountService.createRandomPassword(account);
        modelMap.put("success", true);
    }
}
