package com.withub.model.std.vo;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class ViewSmsServerOut extends AbstractEntity {

    private String name;

    private String recipient;

    private String text;

    private Date create_date;

    private String originator;

    private int status_report;

    private String status;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getRecipient() {

        return recipient;
    }

    public void setRecipient(String recipient) {

        this.recipient = recipient;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public Date getCreate_date() {

        return create_date;
    }

    public void setCreate_date(Date create_date) {

        this.create_date = create_date;
    }

    public String getOriginator() {

        return originator;
    }

    public void setOriginator(String originator) {

        this.originator = originator;
    }

    public int getStatus_report() {

        return status_report;
    }

    public void setStatus_report(int status_report) {

        this.status_report = status_report;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

}
