package com.example.demo;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controller {

    private CustomerClient customerClient;

    private AddressClient addressClient;

    private Logger logger = LoggerFactory.getLogger(Controller.class);

    private final MeterRegistry registry;

    @Autowired
    public Controller(CustomerClient customerClient, AddressClient addressClient,MeterRegistry registry) {
        this.customerClient = customerClient;
        this.addressClient = addressClient;
        this.registry = registry;
    }

    @GetMapping(path = "customerDetails/{id}")
    public CustomerAndAddress getCustomerWithAddress(@PathVariable("id") long customerId){
        logger.info("COLLECTING CUSTOMER AND ADDRESS WITH ID {} FROM UPSTREAM SERVICE", customerId);
        Customer customer = customerClient.getCustomer(customerId);
        Address address = addressClient.getAddressForCustomerId(customerId);
        registry.counter("visits.total", "name", "frontend").increment();
        return new CustomerAndAddress(customer, address);
    }

}
