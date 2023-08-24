package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userAddressBookController")
@RequestMapping("/user/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前用户储存的地址
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> select() {
        List<AddressBook> list = addressBookService.select();
        return Result.success(list);
    }

    /**
     * 新增地址
     *
     * @return
     */
    @PostMapping
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 修改默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        AddressBook addressBook=addressBookService.getDefault();
        return Result.success(addressBook);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook=addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }
    @DeleteMapping
    public Result deleteById(Long id){
        addressBookService.delete(id);
        return Result.success();
    }
}
