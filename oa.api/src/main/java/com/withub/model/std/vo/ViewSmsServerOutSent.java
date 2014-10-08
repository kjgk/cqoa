package com.withub.model.std.vo;

import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "vw_smsserver_out_sent")
public class ViewSmsServerOutSent extends ViewSmsServerOut {

    private Date sent_date;

    public Date getSent_date() {

        return sent_date;
    }

    public void setSent_date(Date sent_date) {

        this.sent_date = sent_date;
    }

}

