package com.withub.model.std.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.User;

import javax.persistence.*;

@Entity
@Table(name = "STD_DOCUMENTHISTORYAUTHOR")
public class DocumentHistoryAuthor extends AbstractEntity {

    //=================== 属性声明 ============================================

    @ManyToOne(targetEntity = DocumentHistory.class)
    @JoinColumn(name = "DocumentHistoryId")
    private DocumentHistory documentHistory;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "Author")
    private User author;

    private Integer proportion;

    private Integer orderNo;

    //=================== 属性方法 ============================================

    public DocumentHistory getDocumentHistory() {

        return documentHistory;
    }

    public void setDocumentHistory(DocumentHistory documentHistory) {

        this.documentHistory = documentHistory;
    }

    public User getAuthor() {

        return author;
    }

    public void setAuthor(User author) {

        this.author = author;
    }

    public Integer getProportion() {

        return proportion;
    }

    public void setProportion(Integer proportion) {

        this.proportion = proportion;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}