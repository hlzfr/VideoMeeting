package com.vc.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vc.service.BaseService;

@Service("baseService")
@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class BaseServiceImpl implements BaseService {

}
