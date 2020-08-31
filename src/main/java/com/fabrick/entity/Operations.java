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
@Table(name = "operations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operations.findAll", query = "SELECT o FROM Operations o")
    , @NamedQuery(name = "Operations.findByProgressiveId", query = "SELECT o FROM Operations o WHERE o.progressiveId = :progressiveId")
    , @NamedQuery(name = "Operations.findByApikey", query = "SELECT o FROM Operations o WHERE o.apikey = :apikey")
    , @NamedQuery(name = "Operations.findByRoute", query = "SELECT o FROM Operations o WHERE o.route = :route")
    , @NamedQuery(name = "Operations.findByDtCall", query = "SELECT o FROM Operations o WHERE o.dtCall = :dtCall")})
public class Operations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "progressive_id")
    private Integer progressiveId;
    @Basic(optional = false)
    @Column(name = "apikey")
    private String apikey;
    @Basic(optional = false)
    @Column(name = "route")
    private String route;
    @Basic(optional = false)
    @Column(name = "dt_call")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCall;

    public Operations() {
    }

    public Operations(Integer progressiveId) {
        this.progressiveId = progressiveId;
    }

    public Operations(Integer progressiveId, String apikey, String route, Date dtCall) {
        this.progressiveId = progressiveId;
        this.apikey = apikey;
        this.route = route;
        this.dtCall = dtCall;
    }

    public Integer getProgressiveId() {
        return progressiveId;
    }

    public void setProgressiveId(Integer progressiveId) {
        this.progressiveId = progressiveId;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Date getDtCall() {
        return dtCall;
    }

    public void setDtCall(Date dtCall) {
        this.dtCall = dtCall;
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
        if (!(object instanceof Operations)) {
            return false;
        }
        Operations other = (Operations) object;
        if ((this.progressiveId == null && other.progressiveId != null) || (this.progressiveId != null && !this.progressiveId.equals(other.progressiveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fabrick.entity.Operations[ progressiveId=" + progressiveId + " ]";
    }
    
}
