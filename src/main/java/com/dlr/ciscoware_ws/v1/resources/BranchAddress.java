/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dlr.ciscoware_ws.v1.resources;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alex
 */
@Entity
@Table(name = "branch_address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BranchAddress.findAll", query = "SELECT b FROM BranchAddress b")
    , @NamedQuery(name = "BranchAddress.findById", query = "SELECT b FROM BranchAddress b WHERE b.id = :id")
    , @NamedQuery(name = "BranchAddress.findByStreet", query = "SELECT b FROM BranchAddress b WHERE b.street = :street")
    , @NamedQuery(name = "BranchAddress.findByCity", query = "SELECT b FROM BranchAddress b WHERE b.city = :city")
    , @NamedQuery(name = "BranchAddress.findByProvince", query = "SELECT b FROM BranchAddress b WHERE b.province = :province")
    , @NamedQuery(name = "BranchAddress.findByZipCode", query = "SELECT b FROM BranchAddress b WHERE b.zipCode = :zipCode")
    , @NamedQuery(name = "BranchAddress.findByCountry", query = "SELECT b FROM BranchAddress b WHERE b.country = :country")})
public class BranchAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "street")
    private String street;
    @Size(max = 255)
    @Column(name = "city")
    private String city;
    @Size(max = 255)
    @Column(name = "province")
    private String province;
    @Size(max = 20)
    @Column(name = "zip_code")
    private String zipCode;
    @Size(max = 255)
    @Column(name = "country")
    private String country;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;

    public BranchAddress() {
    }

    public BranchAddress(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchAddress)) {
            return false;
        }
        BranchAddress other = (BranchAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dlr.ciscoware_ws.v1.resources.BranchAddress[ id=" + id + " ]";
    }
    
}
