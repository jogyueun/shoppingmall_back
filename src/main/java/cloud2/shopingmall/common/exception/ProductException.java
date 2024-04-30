package cloud2.shopingmall.common.exception;

public class ProductException {
    public static class OrderExistsException extends RuntimeException{
       public OrderExistsException(){
           super("주문이 존재하기 때문에 상품 제거가 불가능합니다.");
       }
    }

}
