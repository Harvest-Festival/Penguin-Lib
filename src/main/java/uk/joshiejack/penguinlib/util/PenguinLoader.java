package uk.joshiejack.penguinlib.util;

import net.minecraftforge.fml.network.NetworkDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PenguinLoader {
    String value() default "";

    @interface Packet {
        NetworkDirection value();
    }
}
