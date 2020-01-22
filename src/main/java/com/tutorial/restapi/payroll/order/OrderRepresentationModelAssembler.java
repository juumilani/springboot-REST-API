package com.tutorial.restapi.payroll.order;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderRepresentationModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order){

        // unconditional links to single-item resource and aggregate root
        EntityModel<Order> orderEntityModel = new EntityModel<>(order,
                linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders")
                );

        // conditional links based on the status of the order
        if(order.getStatus() == Status.IN_PROGRESS) {
            orderEntityModel.add(
                    linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderEntityModel.add(
                    linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return orderEntityModel;
    }
}
