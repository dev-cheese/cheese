package com.cheese.demo.discount;

import com.cheese.demo.discount.exception.DiscountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscountService {
    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Transactional
    public Discount create(DiscountDto.Creation dto) {
        return discountRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public Discount findById(DiscountIdEnum id) {
        final Discount discount = discountRepository.findOne(id);
        if (discount == null)
            throw new DiscountNotFoundException(id);

        return discount;
    }
}