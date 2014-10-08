package com.withub.model.std.vo;

import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "vw_smsserver_out_failed")
public class ViewSmsServerOutFailed extends ViewSmsServerOut {

    private Date sent_date;

    private String ref_no;

    private int errors;

    public String getRef_no() {

        return ref_no;
    }

    public void setRef_no(String ref_no) {

        this.ref_no = ref_no;
    }

    public int getErrors() {

        return errors;
    }

    public void setErrors(int errors) {

        this.errors = errors;
    }

    public Date getSent_date() {

        return sent_date;
    }

    public void setSent_date(Date sent_date) {

        this.sent_date = sent_date;
    }
}

