package com.rra.ebm.EBMApplication.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
@Entity
@Table
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int tinNumber;
    private String serialNumber;
    private String owner;
    private String ebmType;
    private String status;
    private LocalDate requestDate;
    private String letterPath;
    private String certPath;
    private String vatPath;
    private String idPath;

    public Requests() {
    }

    public Requests(int id, int tinNumber, String serialNumber, String owner, String ebmType, String status, LocalDate requestDate, String letterPath, String certPath, String vatPath, String idPath) {
        this.id = id;
        this.tinNumber = tinNumber;
        this.serialNumber = serialNumber;
        this.owner = owner;
        this.ebmType = ebmType;
        this.status = status;
        this.requestDate = requestDate;
        this.letterPath = letterPath;
        this.certPath = certPath;
        this.vatPath = vatPath;
        this.idPath = idPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(int tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEbmType() {
        return ebmType;
    }

    public void setEbmType(String ebmType) {
        this.ebmType = ebmType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getLetterPath() {
        return letterPath;
    }

    public void setLetterPath(String letterPath) {
        this.letterPath = letterPath;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getVatPath() {
        return vatPath;
    }

    public void setVatPath(String vatPath) {
        this.vatPath = vatPath;
    }

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }
}
