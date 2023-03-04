package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{

        User user = userRepository2.findById(userId).get();
        String name = countryName.toUpperCase();


        if (user.getConnected()==true){
            throw  new Exception("Already Connected");
        }
        if ((user.getOriginalCountry().getCode()).equals(CountryName.valueOf(name).toCode())){
            return user;
        }

        int minId = Integer.MAX_VALUE;

        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        for (ServiceProvider serviceProvider : serviceProviderList){
            List<Country> countryList = serviceProvider.getCountryList();
            for (Country country : countryList){
                if (country.getCode().equals(CountryName.valueOf(name).toCode())){
                    minId = Math.min(minId,serviceProvider.getId());
                }
            }
        }

        if (minId == Integer.MAX_VALUE){
            throw new Exception("Unable to connect");
        }

        ServiceProvider serviceProvider = serviceProviderRepository2.findById(minId).get();
        user.setConnected(true);
        user.setMaskedIp(CountryName.valueOf(countryName).toCode()+"."+serviceProvider.getId()+"."+user.getId());

        Connection connection = new Connection();
        connection.setServiceProvider(serviceProvider);
        connection.setUser(user);

        List<Connection> userConnectionList = user.getConnectionList();
        userConnectionList.add(connection);
        user.setConnectionList(userConnectionList);

        List<Connection> serviceConnectionList = serviceProvider.getConnectionList();
        serviceConnectionList.add(connection);
        serviceProvider.setConnectionList(serviceConnectionList);

        userRepository2.save(user);

        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {

        User user = userRepository2.findById(userId).get();

        if (user.getConnected() == false){
            throw new Exception("Already disconnected");
        }

        user.setMaskedIp(null);
        user.setConnected(false);

        userRepository2.save(user);

        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        try {
            User sender = userRepository2.findById(senderId).get();
            User receiver = userRepository2.findById(receiverId).get();

            String senderCountryCode, receiverCountryCode;

            senderCountryCode = sender.getOriginalCountry().getCode();

            if (receiver.getConnected()==true) {
                String[] ip = receiver.getMaskedIp().split(".");
                receiverCountryCode = ip[0];
            } else {
                String[] ip = receiver.getOriginalIp().split(".");
                receiverCountryCode = ip[0];
            }

            if (!(senderCountryCode.equals(receiverCountryCode))) {
                connect(senderId, receiver.getOriginalCountry().getCountryName().toString());
            }
            return sender;
        }catch (Exception e){
            throw new Exception("Cannot establish communication");
        }
    }
}
