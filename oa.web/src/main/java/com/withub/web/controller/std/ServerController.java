package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.Server;
import com.withub.service.std.ServerService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/std")
public class ServerController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private ServerService serverService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/server/create", method = RequestMethod.POST)
    public void createServer(ModelMap modelMap, Server server) throws Exception {

        serverService.save(server);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/server/update", method = RequestMethod.POST)
    public void updateServer(ModelMap modelMap, Server server) throws Exception {

        serverService.save(server);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/server/load/{objectId}", method = RequestMethod.GET)
    public void loadServer(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Server server = (Server) serverService.get(Server.class, objectId);
        Map model = new HashMap();
        model.put("objectId", server.getObjectId());
        model.put("name", server.getName());
        model.put("ip", server.getIp());
        model.put("username", server.getUsername());
        model.put("password", server.getPassword());
        model.put("localhost", server.getLocalhost());
        model.put("orderNo", server.getOrderNo());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/server/delete/{objectId}", method = RequestMethod.GET)
    public void deleteServer(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        serverService.logicDelete(Server.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/server/query", method = RequestMethod.GET)
    public void listServer(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Server.class);

        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "name");
        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = serverService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (Server server : (List<Server>) list) {
            HashMap item = new HashMap();
            item.put("objectId", server.getObjectId());
            item.put("name", server.getName());
            item.put("ip", server.getIp());
            item.put("username", server.getUsername());
            item.put("password", server.getPassword());
            item.put("localhost", server.getLocalhost());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    //======================= 属性方法 ===================================================


    public ServerService getServerService() {

        return serverService;
    }

    public void setServerService(ServerService serverService) {

        this.serverService = serverService;
    }
}
