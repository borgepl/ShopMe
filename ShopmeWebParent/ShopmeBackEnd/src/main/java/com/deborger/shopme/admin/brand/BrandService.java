package com.deborger.shopme.admin.brand;

import com.deborger.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }
}
