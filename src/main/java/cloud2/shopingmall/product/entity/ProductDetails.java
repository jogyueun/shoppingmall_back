package cloud2.shopingmall.product.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private Size size;
    @Column
    private Color color;
    @Column
    private Integer quantity;

    @Column
    private ProductStatus status = ProductStatus.ON;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductDetails(Size size, Color color, Integer quantity, ProductStatus status) {
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.status = status;
    }

    public enum ProductStatus {
        ON, OFF
    }


    public enum Size {
        S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");


        private final String size;

        Size(String size){
            this.size = size;
        }

        @JsonValue
        public String getSize() {
            return size;
        }


        @JsonCreator
        public static Size fromSize(String size) {
            for (Size sizeEnum : Size.values()) {
                if (sizeEnum.getSize().equals(size)) {
                    return sizeEnum;
                }
            }
            throw new IllegalArgumentException("No constant with imageFormat " + size + " found");
        }
    }

    public enum Color {
        BLACK("BLACK"), WHITE("WHITE"), RED("RED"), ORANGE("ORANGE"),
        YELLOW("YELLOW"), GREEN("GREEN"), BLUE("BLUE"), INDIGO("INDIGO"),
        PURPLE("PURPLE"), BROWN("BROWN"), GRAY("GRAY"), NAVY("NAVY"), KHAKI("KHAKI");

        private final String color;

        Color(String color){
            this.color = color;
        }


        @JsonValue
        public String getColor() {
            return color;
        }


        @JsonCreator
        public static Color fromColor(String color) {
            for (Color colorEnum : Color.values()) {
                if (colorEnum.getColor().equals(color)) {
                    return colorEnum;
                }
            }
            throw new IllegalArgumentException("No constant with imageFormat " + color + " found");
        }


    }


}
