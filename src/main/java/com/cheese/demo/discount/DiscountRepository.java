package com.cheese.demo.discount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, DiscountIdEnum> {
}