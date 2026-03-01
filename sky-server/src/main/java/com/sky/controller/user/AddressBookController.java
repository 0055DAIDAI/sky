package com.sky.controller.user;


import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端-地址簿接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址")
    public Result<List<AddressBook>> list(){
        log.info("查询当前登录用户所有地址");
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("新增地址")
    public Result add(@RequestBody AddressBook addressBook){
        log.info("新增地址");
        addressBookService.add(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id){
        log.info("根据id查询地址");
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("修改地址")
    public Result update(@RequestBody AddressBook addressBook){
        log.info("修改地址");
        addressBookService.update(addressBook);
        return Result.success();
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址");
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除地址")
    public Result delete(Long id){
        log.info("删除地址");
        addressBookService.delete(id);
        return Result.success();
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault(){
        log.info("查询默认地址");
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1){
            return Result.success(list.get(0));
        }
        return Result.error("没有查询到默认地址");
    }





}
