package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    @Select("select * from address_book where user_id = #{currentId}")
    List<AddressBook> select(Long currentId);

    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, " +
            "province_name, city_code, city_name, district_code, district_name, " +
            "detail, label) " +
            "values (#{userId},#{consignee},#{sex},#{phone},#{provinceCode}," +
            "#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName}," +
            "#{detail},#{label})")
    void save(AddressBook addressBook);

    @Select("select * from address_book where is_default = 1")
    AddressBook getDefault();

    @Update("update address_book set is_default =#{isDefault} where id = #{id}")
    void update(Long id, Integer isDefault);

    @Select("select * from address_book where id =#{id}")
    AddressBook getById(Long id);

    void modify(AddressBook addressBook);

    @Delete("delete from address_book where id = #{id}")
    void delete(Long id);
}
