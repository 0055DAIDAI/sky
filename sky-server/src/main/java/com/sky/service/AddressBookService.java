package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void add(AddressBook addressBook);

    List<AddressBook> list(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void delete(Long id);

    void setDefault(AddressBook addressBook);
}
