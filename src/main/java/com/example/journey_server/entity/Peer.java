package com.example.journey_server.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Peer implements Serializable {

    String email;

    String gender;

    Integer age;

    Double score;

    Double longitude;

    Double latitude;

    Double dLongtitude;

    Double dLatitude;

    Long startTime;

    Long endTime;

    Integer order;

    String orderId;

    Integer limit;

    Boolean leader;

    Boolean furthest;

    String uuid;

    Boolean confirmed;

    List<String> blacklist;

    String ip;

    String port;

    String startAddress;

    String destination;

    Map<String,String> otherFields;

    Integer minAge;

    Integer maxAge;

    String genderCon;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getdLongtitude() {
        return dLongtitude;
    }

    public void setdLongtitude(Double dLongtitude) {
        this.dLongtitude = dLongtitude;
    }

    public Double getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(Double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getLeader() {
        return leader;
    }

    public void setLeader(Boolean leader) {
        this.leader = leader;
    }

    public Boolean getFurthest() {
        return furthest;
    }

    public void setFurthest(Boolean furthest) {
        this.furthest = furthest;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Map<String, String> getOtherFields() {
        return otherFields;
    }

    public void setOtherFields(Map<String, String> otherFields) {
        this.otherFields = otherFields;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getGenderCon() {
        return genderCon;
    }

    public void setGenderCon(String genderCon) {
        this.genderCon = genderCon;
    }

    public Peer(String email, String gender, Integer age, Double score, Double longitude, Double latitude, Double dLongtitude, Double dLatitude, Long startTime, Long endTime, Integer order, String orderId, Integer limit, Boolean leader, Boolean furthest, String uuid, Boolean confirmed, List<String> blacklist, String ip, String port, String startAddress, String destination, Map<String, String> otherFields, Integer minAge, Integer maxAge, String genderCon) {
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.score = score;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dLongtitude = dLongtitude;
        this.dLatitude = dLatitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.order = order;
        this.orderId = orderId;
        this.limit = limit;
        this.leader = leader;
        this.furthest = furthest;
        this.uuid = uuid;
        this.confirmed = confirmed;
        this.blacklist = blacklist;
        this.ip = ip;
        this.port = port;
        this.startAddress = startAddress;
        this.destination = destination;
        this.otherFields = otherFields;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.genderCon = genderCon;
    }
}
