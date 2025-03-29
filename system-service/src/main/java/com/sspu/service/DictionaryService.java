package com.sspu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sspu.entity.Dictionary;

import java.util.List;

public interface DictionaryService extends IService<Dictionary> {

    /**
     * 查询所有数据字典内容
     *
     * @return
     */
    List<Dictionary> queryAll();

    /**
     * 根据code查询数据字典
     *
     * @param code -- String 数据字典Code
     * @return
     */
    Dictionary getByCode(String code);
}
