package com.withub.service.std;

import com.withub.service.EntityService;

public interface SmsService extends EntityService {

    public void sendSms(final String mobile, final String message) throws Exception;
}
