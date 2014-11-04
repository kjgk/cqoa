package com.withub.service.impl.std;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.AppVersion;
import com.withub.model.std.po.FileInfo;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.AppVersionService;
import com.withub.service.std.FileService;
import com.withub.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;


@Service("appVersionService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class AppVersionServiceImpl extends EntityServiceImpl implements AppVersionService {

    @Autowired
    private FileService fileService;

    public AppVersion getAppVersion(String objectId) throws Exception {

        return get(AppVersion.class, objectId);
    }

    public RecordsetInfo<AppVersion> queryAppVersion(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveAppVersion(AppVersion appVersion) throws Exception {

        save(appVersion);

        fileService.save(appVersion, appVersion.getFileUploadInfo());

        FileInfo fileInfo = fileService.getFileInfoByRelatedObjectId(appVersion.getObjectId());

        String url = ConfigUtil.getSystemConfigInfo().getUrl() + "/std/appVersion/download/" + fileInfo.getObjectId() + "/" + fileInfo.getName();
        int width = 320;
        int height = 320;
        String format = "png";
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File(ConfigUtil.getSystemConfigInfo().getTempDirectory() + "/" + StringUtil.getUuid());
        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

        fileService.save(appVersion, outputFile, new Date().getTime() + ".png", "", 2);

        appVersion.setApkUrl(url);

    }

    public void deleteAppVersion(String objectId) throws Exception {

        logicDelete(AppVersion.class, objectId);
    }


    public void enableAppVersion(String objectId, User currentUser) throws Exception {

        executeHql("update " + AppVersion.class.getName() + " a set a.status = 0 where 1=1");

        AppVersion appVersion = getAppVersion(objectId);
        appVersion.setCurrentUser(currentUser);
        appVersion.setStatus(1);
        save(appVersion);
    }

    public void disableAppVersion(String objectId, User currentUser) throws Exception {

        AppVersion appVersion = getAppVersion(objectId);
        appVersion.setCurrentUser(currentUser);
        appVersion.setStatus(0);
        save(appVersion);
    }

    public AppVersion getEnabledAppVersion() throws Exception {

        return (AppVersion) getByHql("from " + AppVersion.class.getName() + " where status = 1 and objectStatus = 1");
    }
}
