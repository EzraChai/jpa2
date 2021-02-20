package com.bjpowernode.springboot.blog.service.impl;

import com.bjpowernode.springboot.blog.dao.TypeRepository;
import com.bjpowernode.springboot.blog.entity.Type;
import com.bjpowernode.springboot.blog.exception.NotFoundException;
import com.bjpowernode.springboot.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Override
    public Type getTypeByName(String name) {
        Type byName = typeRepository.findByName(name);
        return byName;
    }

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        Optional<Type> byId = typeRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }else{
            throw new NotFoundException("Not Exists.");
        }
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Optional<Type> type1 = typeRepository.findById(id);
        if (!type1.isPresent()){
            throw new NotFoundException("Not Exist.");
        }
        BeanUtils.copyProperties(type,type1.get());
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        Optional<Type> byId = typeRepository.findById(id);
        if (byId.isPresent()) {
            typeRepository.delete(byId.get());
        }else{
            throw new NotFoundException("Not Exist.");
        }
    }
}
