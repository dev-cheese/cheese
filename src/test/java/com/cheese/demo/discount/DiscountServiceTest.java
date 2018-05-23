package com.cheese.demo.discount;

import com.cheese.demo.discount.exception.DiscountNotFoundException;
import org.junit.Before;
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
public class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;


    private DiscountDto.Creation amountDiscount;
    private DiscountDto.Creation rateDiscount;

    @Before
    public void setUp() {
        amountDiscount = buildAmountDiscount();
        rateDiscount = buildRateDiscount();
    }

    @Test
    public void create_succeed_ReturnAmountDiscount() {
        //given
        final Discount disCountEntity = amountDiscount.toEntity();
        given(discountRepository.save(any(Discount.class))).willReturn(disCountEntity);

        //when
        final Discount discount = discountService.create(amountDiscount);

        //then
        verify(discountRepository, atLeastOnce()).save(any(Discount.class));
        assertThat(discount, equalTo(disCountEntity));
        assertThatProperty(amountDiscount, discount);
    }

    @Test
    public void create_succeed_ReturnRateDiscount() {
        //given
        final Discount disCountEntity = rateDiscount.toEntity();
        given(discountRepository.save(any(Discount.class))).willReturn(disCountEntity);

        //when
        final Discount discount = discountService.create(rateDiscount);

        //then
        verify(discountRepository, atLeastOnce()).save(any(Discount.class));
        assertThatProperty(rateDiscount, discount);
        assertThat(discount, equalTo(disCountEntity));
    }


    @Test
    public void findById_Existed_ReturnDiscount() {
        //given
        final Discount disCountEntity = rateDiscount.toEntity();
        given(discountRepository.findOne(disCountEntity.getId())).willReturn(disCountEntity);

        //when
        final Discount discount = discountService.findById(disCountEntity.getId());

        //then
        verify(discountRepository, atLeastOnce()).findOne(disCountEntity.getId());
        assertThatProperty(rateDiscount, discount);
        assertThat(discount, equalTo(disCountEntity));
    }


    @Test(expected = DiscountNotFoundException.class)
    public void findById_NotExisted_DiscountNotFoundException() {
        //given
        given(discountRepository.findOne(any(DiscountIdEnum.class))).willReturn(null);

        //when
        discountService.findById(any(DiscountIdEnum.class));
    }

    private void assertThatProperty(DiscountDto.Creation dto, Discount discount) {
        //테스트 커버리지를 위한 검사
        assertThat(discount.getDescription(), is(dto.getDescription()));
        assertThat(discount.getAmount(), is(dto.getAmount()));
        assertThat(discount.getRate(), is(dto.getRate()));
        assertThat(discount.getExpiration(), is(dto.getExpiration()));
        assertThat(discount.getCreatedDt(), is(nullValue()));
        assertThat(discount.getUpdatedDt(), is(nullValue()));
        assertThat(discount.getCoupons().size(), is(0));
        assertThat(discount.getId(), is(notNullValue()));
        assertThat(dto.getId(), is(notNullValue()));
    }

    private DiscountDto.Creation buildAmountDiscount() {
        return DiscountDto.Creation.builder()
                .id(DiscountIdEnum.A001)
                .amount(10)
                .description("테스트")
                .expiration(604800)
                .build();
    }

    private DiscountDto.Creation buildRateDiscount() {
        return DiscountDto.Creation.builder()
                .id(DiscountIdEnum.R001)
                .rate(0.4)
                .description("테스트")
                .expiration(604800)
                .build();
    }
}