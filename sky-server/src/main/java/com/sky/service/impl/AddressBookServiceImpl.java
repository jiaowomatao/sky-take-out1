package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> select() {
        List<AddressBook> list=addressBookMapper.select(BaseContext.getCurrentId());
        return list;
    }

    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.save(addressBook);
    }

    @Override
    public void setDefault(AddressBook addressBook) {
        AddressBook aDefault = addressBookMapper.getDefault();
        if (aDefault!=null){
            aDefault.setIsDefault(0);
            addressBookMapper.update(aDefault.getId(),aDefault.getIsDefault());
        }
        addressBookMapper.update(addressBook.getId(),1);

    }

    @Override
    public AddressBook getDefault() {
        AddressBook addressBook=addressBookMapper.getDefault();
        return addressBook;
    }

    @Override
    public AddressBook getById(Long id) {
        AddressBook addressBook=addressBookMapper.getById(id);
        return addressBook;
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.modify(addressBook);
    }

    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }
}
