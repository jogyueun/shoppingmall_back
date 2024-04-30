package cloud2.shopingmall.common.exception;

public class CategoryException {
    public static class DuplicatedNameFoundException extends RuntimeException{
       public DuplicatedNameFoundException(String name){
           super("Duplicated Category name: " +name +" found");
       }
    }

}
