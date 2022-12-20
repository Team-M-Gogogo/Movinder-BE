package com.movinder.be.service;

import com.movinder.be.entity.Food;
import com.movinder.be.repository.FoodRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final FoodRepository foodMongoRepository;



    public BookingService(FoodRepository foodMongoRepository) {
        this.foodMongoRepository = foodMongoRepository;
    }

    /*
    Food
     */

    public List<Food> getFood(String foodName, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return foodMongoRepository.findByfoodNameIgnoringCaseContaining(foodName, pageable);
    }

}
