package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Customer;

import javax.naming.Name;
import java.util.List;
import java.util.Map;

public interface CustomerDao {

    List<String> getCustomerName(String name);

    int getCustomerTotal(Map<String, Object> map);

    List<Customer> getCustomerList(Map<String, Object> map);

    Customer getCustomerByName(String customerName);

    int saveCustomer(Customer c);

    Customer getByName(String company);

    int save(Customer customer);

}
