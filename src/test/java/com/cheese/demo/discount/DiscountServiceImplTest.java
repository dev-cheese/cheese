package com.cheese.demo.discount;

import com.cheese.demo.SpringServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@Transactional
public class DiscountServiceImplTest {


    @Autowired
    private DiscountServiceImpl couponDiscountService;


    @Test
    public void create_valid_ReturnCouponDiscountObject() {
        DiscountDto.Creation dto = DiscountDto.Creation.builder()
                .amount(10)
                .description("테스트")
                .expiration(604800)
                .build();

        Discount discount = couponDiscountService.create(dto);
        assertThat(discount.getAmount(), is(dto.getAmount()));
        assertThat(discount.getExpiration(), is(dto.getExpiration()));
        assertThat(discount.getDescription(), is(dto.getDescription()));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_DescriptionIsNull_DataIntegrityViolationException() {
        DiscountDto.Creation dto = DiscountDto.Creation.builder()
                .amount(10)
                .build();
        couponDiscountService.create(dto);
    }
}