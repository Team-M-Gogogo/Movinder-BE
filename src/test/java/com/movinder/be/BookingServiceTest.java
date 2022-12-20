package com.movinder.be;

import com.movinder.be.entity.Food;
import com.movinder.be.repository.FoodRepository;
import com.movinder.be.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class BookingServiceTest {

    @Mock
    FoodRepository foodRepository;

    @InjectMocks
    BookingService bookingService;

    @Test
    public void should_return_a_list_of_food_when_get_food_given_name(){
        // given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);

        Food food2 = new Food();
        food2.setFoodName("coke zero");
        food2.setDescription("200g");
        food2.setPrice(40);

        List<Food> foodList = Arrays.asList(food, food2);
        Pageable pageable = PageRequest.of(0, 2);

        given(foodRepository.findByfoodNameIgnoringCaseContaining("coke", pageable)).willReturn(foodList);

        // when
        List<Food> fetchFood = bookingService.getFood("coke", 0, 2);

        // then
        assertThat(fetchFood, equalTo(foodList));


        verify(foodRepository).findByfoodNameIgnoringCaseContaining("coke", pageable);
    }
}
