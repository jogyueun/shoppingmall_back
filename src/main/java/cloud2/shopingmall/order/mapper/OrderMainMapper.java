package cloud2.shopingmall.order.mapper;

import cloud2.shopingmall.common.mapper.EntityMapper;
import cloud2.shopingmall.order.dto.*;
import cloud2.shopingmall.order.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OrderMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface DeliveryMapper extends EntityMapper<Delivery, DeliveryDTO> {
    }
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface OrderProductMapper extends EntityMapper<OrderProduct, OrderProductDTO> {
        @Mapping(target = "id", ignore = true)
        @Override
        OrderProduct toEntity(OrderProductDTO dto);
    }
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface OrderMapper extends EntityMapper<Orders, OrderDTO> {
    }
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface PaymentMapper extends EntityMapper<Payment, PaymentDTO> {
    }
//    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
//    interface RefundMapper extends EntityMapper<Refund, RefundDTO> {
//    }
    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface OrderInfoMapper extends EntityMapper<Orders, OrderInfoDTO> {
        @Mapping(source = "user.username",target = "userName")
        @Mapping(source = "payment.payTotalPrice",target = "totalPrice")
         OrderInfoDTO toDto(Orders order);
        @AfterMapping
        default void defineStatusDescription(Orders order, @MappingTarget OrderInfoDTO orderInfoDTO) {
        if (order.getStatus() != null) {
            orderInfoDTO.setStatusIndex(order.getStatus().getIndex());
        }

    }
            default Orders.OrderStatus map(int value) {
            return Orders.OrderStatus.findByIndex(value);
        }
    }
    @Mapper(componentModel = "spring")
    interface OrderDetailMapper extends EntityMapper<Orders, OrderInfoDTO.OrderDetailInfo> {
        @Mapping(source = "user.username",target = "userName")
        @Mapping(source = "payment.payTotalPrice",target = "totalPrice")
        @Mapping(target = "products", ignore = true)
        OrderInfoDTO.OrderDetailInfo toDto(Orders order);
        @AfterMapping
        default void customMapping(@MappingTarget OrderInfoDTO.OrderDetailInfo target, Orders source) {
            List<OrderInfoDTO.ProductInfo> productInfoList = new ArrayList<>();
            for (OrderProduct op : source.getOrderProducts()) {
                OrderInfoDTO.ProductInfo productInfo = new OrderInfoDTO.ProductInfo();
                Map<String, Integer> productsMap = new HashMap<>();
                productsMap.put(op.getName(), op.getCount());
                productInfo.setProductNameAndCount(productsMap);
                productInfo.setColor(op.getColor());
                productInfo.setSize(op.getSize());
                productInfo.setPrice(op.getPrice());
                productInfoList.add(productInfo);
            }
            target.setProducts(productInfoList);
        }
        default Orders.OrderStatus map(int value) {
            return Orders.OrderStatus.findByIndex(value);
        }
    }

}
