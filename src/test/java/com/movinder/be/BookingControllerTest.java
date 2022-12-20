package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movinder.be.entity.Food;
import com.movinder.be.entity.Seat;
import com.movinder.be.entity.Ticket;
import com.movinder.be.repository.FoodRepository;
import com.movinder.be.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;


@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    FoodRepository foodRepository;
    @Autowired
    TicketRepository ticketRepository;

    @BeforeEach
    public void clearDB() {
        foodRepository.deleteAll();
    }

    @Test
    public void should_get_food_when_perform_get_food_given_params() throws Exception {
        //given

        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);

        Food food2 = new Food();
        food2.setFoodName("popcorn");
        food2.setDescription("200g");
        food2.setPrice(40);

        foodRepository.save(food);
        foodRepository.save(food2);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/booking/foods"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].foodName", containsInAnyOrder("coke", "popcorn")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].price", containsInAnyOrder(10, 40)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description", containsInAnyOrder("1L", "200g")));
    }

    @Test
    public void should_create_food_when_perform_create_given_food_obj() throws Exception {
        //given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);


        String foodJson = new ObjectMapper().writeValueAsString(food);


        //when & then
        client.perform(MockMvcRequestBuilders.post("/booking/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(foodJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodName").value("coke"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("1L"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

    @Test
    public void should_return_a_food_when_get_food_by_id_given_a_food_id() throws Exception {
        // given
        Food food = new Food();
        food.setFoodName("coke");
        food.setDescription("1L");
        food.setPrice(10);

        // when
        Food savedFood = foodRepository.save(food);

        // then
        client.perform(MockMvcRequestBuilders.get("/booking/foods/{foodId}", food.getFoodId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.foodName").value("coke"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("1L"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

    @Test
    public void should_return_a_ticket_when_get_ticket_by_id_given_a_ticket_id() throws Exception {
        // given
        Ticket ticket = new Ticket("adult", 10, new Seat(0,1));

        // when
        Ticket savedTicket = ticketRepository.save(ticket);

        // then
        client.perform(MockMvcRequestBuilders.get("/booking/tickets/{ticketID}", ticket.getTicketId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketType").value("adult"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seat.row").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seat.column").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10));
    }

}
