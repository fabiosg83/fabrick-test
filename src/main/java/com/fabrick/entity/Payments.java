/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fabrick.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fabio.sgroi
 */
@Entity
@Table(name = "payments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p")
    , @NamedQuery(name = "Payments.findByProgressiveId", query = "SELECT p FROM Payments p WHERE p.progressiveId = :progressiveId")
    , @NamedQuery(name = "Payments.findByAccountId", query = "SELECT p FROM Payments p WHERE p.accountId = :accountId")
    , @NamedQuery(name = "Payments.findByCreditorName", query = "SELECT p FROM Payments p WHERE p.creditorName = :creditorName")
    , @NamedQuery(name = "Payments.findByCreditorAccountCode", query = "SELECT p FROM Payments p WHERE p.creditorAccountCode = :creditorAccountCode")
    , @NamedQuery(name = "Payments.findByDescription", query = "SELECT p FROM Payments p WHERE p.description = :description")
    , @NamedQuery(name = "Payments.findByAmount", query = "SELECT p FROM Payments p WHERE p.amount = :amount")
    , @NamedQuery(name = "Payments.findByCurrency", query = "SELECT p FROM Payments p WHERE p.currency = :currency")
    , @NamedQuery(name = "Payments.findByStatus", query = "SELECT p FROM Payments p WHERE p.status = :status")
    , @NamedQuery(name = "Payments.findByDtExecution", query = "SELECT p FROM Payments p WHERE p.dtExecution = :dtExecution")})
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "progressive_id")
    private Integer progressiveId;
    @Basic(optional = false)
    @Column(name = "account_id")
    private long accountId;
    @Basic(optional = false)
    @Column(name = "creditor_name")
    private String creditorName;
    @Basic(optional = false)
    @Column(name = "creditor_account_code")
    private String creditorAccountCode;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "amount")
    private double amount;
    @Basic(optional = false)
    @Column(name = "currency")
    private String currency;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Basic(optional = false)
    @Column(name = "dt_execution")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExecution;

    public Payments() {
    }

    public Payments(Integer progressiveId) {
        this.progressiveId = progressiveId;
    }

    public Payments(Integer progressiveId, long accountId, String creditorName, String creditorAccountCode, String description, double amount, String currency, short status, Date dtExecution) {
        this.progressiveId = progressiveId;
        this.accountId = accountId;
        this.creditorName = creditorName;
        this.creditorAccountCode = creditorAccountCode;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.dtExecution = dtExecution;
    }

    public Integer getProgressiveId() {
        return progressiveId;
    }

    public void setProgressiveId(Integer progressiveId) {
        this.progressiveId = progressiveId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorAccountCode() {
        return creditorAccountCode;
    }

    public void setCreditorAccountCode(String creditorAccountCode) {
        this.creditorAccountCode = creditorAccountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getDtExecution() {
        return dtExecution;
    }

    public void setDtExecution(Date dtExecution) {
        this.dtExecution = dtExecution;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (progressiveId != null ? progressiveId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payments)) {
            return false;
        }
        Payments other = (Payments) object;
        if ((this.progressiveId == null && other.progressiveId != null) || (this.progressiveId != null && !this.progressiveId.equals(other.progressiveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fabrick.entity.Payments[ progressiveId=" + progressiveId + " ]";
    }
    
}
