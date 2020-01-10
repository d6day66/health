package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

   public void add(CheckGroup checkGroup);

   public void setCheckGroupAndCheckItem(Map<String, Integer> map);

     public Page<CheckGroup> findByCondition(String querryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    void delete(Integer id);

    List<CheckGroup> findAll();
    CheckGroup findCheckGroupById(Integer id);
}
