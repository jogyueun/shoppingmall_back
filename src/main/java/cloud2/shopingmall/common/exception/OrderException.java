package cloud2.shopingmall.common.exception;

public class OrderException{
    public static class OrderNotFoundException extends RuntimeException{
       public OrderNotFoundException(Long id){
           super("order with id " +id +"not found");
       }public OrderNotFoundException(){
            super("order not found");
        }
    }
    public static class OrderCancellationNotAllowedException extends RuntimeException{
        public OrderCancellationNotAllowedException(Long id){
            super("order with id "+id +"is NOT Allowed cancel");
        }

    }

    public static class OrderNotFoundOrderProductException extends RuntimeException{
        public OrderNotFoundOrderProductException(){
            super("주문하실 상품이 없습니다.");
        }
    }

    public static class CustomException extends RuntimeException {
        public CustomException() {
            super("결제 실패: 유저 적립금 부족 또는 결제 오류");
        }
    }
    public static class DeliveryException extends RuntimeException {
        public DeliveryException() {
            super("배송 정보를 다시 한번 확인해주세요");
        }
    }
}
