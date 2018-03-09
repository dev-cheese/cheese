package com.cheese.demo.discount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceImplTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountServiceImpl discountService;

    @Test
    public void create_valid_ReturnCouponDiscountObject() {
        //given
        final DiscountDto.Creation dto = buildAmountDiscount();
        final Discount discountEntity = dto.toEntity();
        given(discountRepository.save(any(Discount.class))).willReturn(discountEntity);

        //when
        final Discount discount = discountService.create(dto);

        //then
        verify(discountRepository, atLeastOnce()).save(any(Discount.class));
        assertThat(discount, equalTo(discountEntity));
        assertThatProperty(dto, discount);
    }

    @Test
    public void create_test() {
        //given
        final DiscountDto.Creation dto = buildRateDiscount();
        final Discount discountEntity = dto.toEntity();
        given(discountRepository.save(any(Discount.class))).willReturn(discountEntity);

        //when
        final Discount discount = discountService.create(dto);

        //then
        verify(discountRepository, atLeastOnce()).save(any(Discount.class));
        assertThat(discount, equalTo(discountEntity));
        assertThatProperty(dto, discount);

    }

    private void assertThatProperty(DiscountDto.Creation dto, Discount discount) {
        //테스트 커버리지를 위한 검사
        assertThat(discount.getDescription(), is(dto.getDescription()));
        assertThat(discount.getAmount(), is(dto.getAmount()));
        assertThat(discount.getRate(), is(dto.getRate()));
        assertThat(discount.getExpiration(), is(dto.getExpiration()));
        assertThat(discount.getId(), is(nullValue()));
        assertThat(discount.getCreatedDt(), is(nullValue()));
        assertThat(discount.getUpdatedDt(), is(nullValue()));
        assertThat(discount.getCoupons().size(), is(0));
    }

    private DiscountDto.Creation buildAmountDiscount() {
        return DiscountDto.Creation.builder()
                .amount(10)
                .description("테스트")
                .expiration(604800)
                .build();
    }

    private DiscountDto.Creation buildRateDiscount() {
        return DiscountDto.Creation.builder()
                .rate(0.4)
                .description("테스트")
                .expiration(604800)
                .build();
    }
}