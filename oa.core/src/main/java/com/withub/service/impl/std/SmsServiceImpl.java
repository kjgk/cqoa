package com.withub.service.impl.std;

import com.withub.service.EntityServiceImpl;
import com.withub.service.std.SmsService;
import com.withub.util.ConfigUtil;
import com.withub.util.SpringSecurityUtil;
import org.springframework.stereotype.Service;

@Service("smsSerivice")
public class SmsServiceImpl extends EntityServiceImpl implements SmsService {

    public void sendSms(String mobile, String message) throws Exception {

        String sql = "INSERT INTO smsserver_out(TYPE,recipient,TEXT,encoding,create_date,originator,sent_date) VALUES ('O',?,?,'U',%s,?, %s)";
        String originator = SpringSecurityUtil.getCurrentUser().getObjectId();

        if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equalsIgnoreCase("MySql")) {
            sql = String.format(sql, "now()", "date_format('1900-01-01','%Y-%m-%d')");
        }

        if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equalsIgnoreCase("Oracle")) {
            sql = String.format(sql, "sysdate", "to_date('1900-01-01' ,'YYYY-mm-dd')");
        }

        if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equalsIgnoreCase("SqlServer")) {
            sql = String.format(sql, "getdate()", "'1900-01-01'");
        }

        Object[] params = {mobile, message, originator};
        executeSql(sql, params);
    }
}
