package controller;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class CartDto {

    private Long cartId;

    private Long productId;

    private  Integer quantity;
}
