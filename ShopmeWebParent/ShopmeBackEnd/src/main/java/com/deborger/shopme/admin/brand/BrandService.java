package com.deborger.shopme.admin.brand;

import com.deborger.shopme.admin.category.CategoryNotFoundException;
import com.deborger.shopme.common.entity.Brand;
import com.deborger.shopme.common.entity.Category;
import com.deborger.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandService {

    public static final int BRANDS_PER_PAGE = 10;

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }

    public Page<Brand> listByPage(Integer pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
        if (keyword != null) {
            return brandRepository.findAll(keyword,pageable);
            }
        return brandRepository.findAll(pageable);
    }

    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws CategoryNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new CategoryNotFoundException("Could not find any brand with ID " + id);
        }
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = brandRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any brand with ID " + id);
        }
        brandRepository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {
        Brand brand = brandRepository.findByName(name);

        if (brand == null) return "OK";
        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            if (brand != null) return "DuplicateName";
        } else {
            if (brand.getId() != id ) {
                return "DuplicateName";
            }
        }
        return "OK";
    }

}
