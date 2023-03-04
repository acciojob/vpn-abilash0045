package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    private String originalIp;

    private String mappedIp;

    private boolean connected;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Country country;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Connection> connectionList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_service",joinColumns = @JoinColumn(name = "userId"),inverseJoinColumns = @JoinColumn(name = "serviceProviderId"))
    List<ServiceProvider> serviceProviderList = new ArrayList<>();

    public User() {
    }

    public User(int id, String username, String password, String originalIp, String mappedIp, boolean connected, Country country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.originalIp = originalIp;
        this.mappedIp = mappedIp;
        this.connected = connected;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalIp() {
        return originalIp;
    }

    public void setOriginalIp(String originalIp) {
        this.originalIp = originalIp;
    }

    public String getMappedIp() {
        return mappedIp;
    }

    public void setMappedIp(String mappedIp) {
        this.mappedIp = mappedIp;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }
}
